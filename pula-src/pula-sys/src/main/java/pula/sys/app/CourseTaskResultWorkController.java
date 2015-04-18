package pula.sys.app;

import java.io.File;
import java.util.Calendar;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import puerta.support.AttachmentFile;
import puerta.support.PaginationSupport;
import puerta.support.Pe;
import puerta.support.annotation.Barrier;
import puerta.support.annotation.ObjectParam;
import puerta.support.utils.RandomTool;
import puerta.support.utils.WxlSugar;
import puerta.system.keeper.ParameterKeeper;
import puerta.system.vo.JsonResult;
import puerta.system.vo.MapBean;
import puerta.system.vo.MapList;
import puerta.system.vo.YuiResult;
import pula.sys.BhzqConstants;
import pula.sys.PurviewConstants;
import pula.sys.daos.BranchDao;
import pula.sys.daos.ClassroomDao;
import pula.sys.daos.CourseDeploymentDao;
import pula.sys.daos.CourseTaskResultDao;
import pula.sys.daos.CourseTaskResultStudentDao;
import pula.sys.daos.CourseTaskResultWorkDao;
import pula.sys.daos.FileAttachmentDao;
import pula.sys.daos.StudentDao;
import pula.sys.daos.SysCategoryDao;
import pula.sys.daos.TeacherDao;
import pula.sys.domains.CourseTaskResultStudent;
import pula.sys.domains.CourseTaskResultWork;
import pula.sys.domains.FileAttachment;
import pula.sys.forms.FileAttachmentForm;
import pula.sys.helpers.TrainLogHelper;
import pula.sys.services.SessionUserService;
import pula.sys.services.StudentPointsService;

@Controller
public class CourseTaskResultWorkController {

	@Resource
	TeacherDao teacherDao;
	@Resource
	SessionUserService sessionUserService;
	@Resource
	CourseTaskResultDao courseTaskResultDao;
	@Resource
	CourseTaskResultStudentDao courseTaskResultStudentDao;
	@Resource
	CourseTaskResultWorkDao courseTaskResultWorkDao;
	@Resource
	FileAttachmentDao fileAttachmentDao;
	@Resource
	BranchDao branchDao;
	@Resource
	SysCategoryDao sysCategoryDao;
	@Resource
	ClassroomDao classroomDao;
	@Resource
	CourseDeploymentDao courseDeploymentDao;
	@Resource
	StudentDao studentDao;
	@Resource
	StudentPointsService studentPointsService;
	@Resource
	ParameterKeeper parameterKeeper;

	@Transactional
	@RequestMapping
	@Barrier(check = false, value = PurviewConstants.COURSE_TASK_RESULT)
	@ResponseBody
	public JsonResult _replaceWork(
			@RequestParam("id") Long courseTaskResultStudentId,
			@ObjectParam("form") FileAttachmentForm form) {

		MapBean meta = courseTaskResultStudentDao
				.meta4work(courseTaskResultStudentId);

		precheckCourseTaskResult(meta);

		// Long resultId = meta.asLong("courseTaskResultId");
		// Long studentId = meta.asLong("studentId");
		//
		MapBean workMeta = courseTaskResultWorkDao
				.meta4upload(courseTaskResultStudentId);

		CourseTaskResultWork w = null;
		if (workMeta == null) {
			// 新增作品

			w = new CourseTaskResultWork();
			w.setCourseTaskResultStudent(CourseTaskResultStudent
					.create(courseTaskResultStudentId));
			w = courseTaskResultWorkDao.save(w);

		} else {
			// 修改作品
			String newKey = RandomTool.getRandomString(10);
			courseTaskResultWorkDao.updateKey(newKey, workMeta.asLong("id"));
			w = new CourseTaskResultWork();
			w.setId(workMeta.asLong("id"));
			w.setAttachmentKey(newKey);
		}

		FileAttachment fa = processFile(form);
		fileAttachmentDao.save(w, WxlSugar.asList(fa), true);// 旧的都删掉了

		return JsonResult.s();

	}

	private FileAttachment processFile(FileAttachmentForm a) {
		String filePath = parameterKeeper
				.getFilePath(BhzqConstants.FILE_STUDENT_WORK_DIR);

		String srcPath = parameterKeeper
				.getFilePath(BhzqConstants.FILE_UPLOAD_DIR);
		FileAttachment fa = new FileAttachment();
		fa.setExtName(FilenameUtils.getExtension(a.getFileName()));
		fa.setName(a.getFileName());
		fa.setType(FileAttachment.TYPE_STUENDT_WORK);
		fa.setFileId(a.getFileId() + "."
				+ Calendar.getInstance().getTimeInMillis());

		if (a.getId() == 0) {
			// build
			String dest = filePath + File.separatorChar + fa.getFileId();
			File f = new File(srcPath + File.separatorChar + a.getFileId());
			f.renameTo(new File(dest));

		} else {
			fa.setId(a.getId());
		}

		return fa;
	}

	/**
	 * 下载学生作品
	 * 
	 * @param id
	 *            -- 应该是作品的id,必须检查访问者是否有权限.
	 * @param res
	 * @return
	 */
	@RequestMapping
	@ResponseBody
	@Barrier(check = false, value = PurviewConstants.COURSE_TASK_RESULT)
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	public AttachmentFile file(
			@RequestParam(value = "id", required = false) Long id,
			HttpServletResponse res) {

		MapBean meta = courseTaskResultWorkDao.meta4view(id);

		// 权限检查,主要是branch 检查 ,是否到达这个级别了?
		if (!sessionUserService.isHeadquarter()) {
			long branch_id = sessionUserService.getBranch().getIdLong();
			if (branch_id != meta.asLong("branchId")) {
				Pe.raise("越权访问");
			}
		}

		// 要的几个信息,包括文件的存放位置

		String ak = meta.string("attachmentKey");
		// MapBean fileMeta = fileAttachmentDao.meta(id, ak,
		// FileAttachment.TYPE_STUENDT_WORK);

		FileAttachment fa = fileAttachmentDao.getByRefId(
				CourseTaskResultWork.buildRefId(id, ak),
				FileAttachment.TYPE_STUENDT_WORK);

		String srcPath = null;

		srcPath = parameterKeeper
				.getFilePath(BhzqConstants.FILE_STUDENT_WORK_DIR);

		String fp = fa.getFileId();
		String fullPath = srcPath + File.separatorChar + fp;

		File f = new File(fullPath);

		if (!f.exists()) {
			res.setStatus(404);
			return null;
		}

		AttachmentFile af = AttachmentFile.attach(f,
				AttachmentFile.APPLICATION_UNKNOWN);

		af.setFileName(fa.getName());
		af.setBurnAfterReading(false);

		return af;
	}

	private boolean allowEdit(Calendar... cals) {

		if (!sessionUserService.isHeadquarter()) {
			boolean chk = true;
			for (Calendar c : cals) {
				chk = chk && TrainLogHelper.allowEdit(c);
			}

			return chk;
		}

		return true;
	}

	private void precheckCourseTaskResult(MapBean meta) {
		if (meta == null) {
			Pe.raise("已经删除或越权访问,请刷新界面");
		}

		if (!sessionUserService.isHeadquarter()) {
			long branch_id = meta.asLong("branchId");
			if (branch_id != sessionUserService.getBranch().getIdLong()) {
				Pe.raise("越权访问");
			}
		}

		// 检查能不能删除
		if (!allowEdit(meta.calendar("startTime"), meta.calendar("endTime"))) {
			Pe.raise("不允许修改历史月份的数据");
		}
	}

	@RequestMapping
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	@ResponseBody
	@Barrier(check = false, value = PurviewConstants.COURSE_TASK_RESULT)
	public YuiResult listInResult(@RequestParam("id") long resultId) {

		// MapList students = this.courseTaskResultStudentDao.mapList(resultId);
		// final Map<String, MapBean> works = this.courseTaskResultWorkDao
		// .map(resultId);
		//
		// return YuiResult.create(students, new YuiResultMapper<MapBean>() {
		//
		// @Override
		// public Map<String, Object> toMap(MapBean obj) {
		// String studentNo = obj.string("studentNo");
		// if (works.containsKey(studentNo)) {
		// obj.putAll(works.get(studentNo));
		// }
		//
		// return obj;
		// }
		// });

		MapList ml = courseTaskResultWorkDao.mapList(resultId);
		return YuiResult.create(new PaginationSupport<MapBean>(ml, ml.size()));

	}

}
