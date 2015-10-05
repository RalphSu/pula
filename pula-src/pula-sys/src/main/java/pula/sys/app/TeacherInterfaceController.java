package pula.sys.app;

import java.io.File;
import java.util.Calendar;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import puerta.support.PaginationSupport;
import puerta.support.Pe;
import puerta.support.annotation.Barrier;
import puerta.system.keeper.ParameterKeeper;
import puerta.system.vo.JsonResult;
import puerta.system.vo.MapBean;
import puerta.system.vo.MapList;
import pula.sys.BhzqConstants;
import pula.sys.daos.CourseTaskPlanDao;
import pula.sys.daos.CourseTaskResultWorkDao;
import pula.sys.daos.FileAttachmentDao;
import pula.sys.daos.TeacherDao;
import pula.sys.daos.TeacherLogDao;
import pula.sys.domains.CourseTaskResultWork;
import pula.sys.domains.FileAttachment;
import pula.sys.miscs.MD5Checker;

@Controller
@Barrier(ignore = true)
public class TeacherInterfaceController {

	private static final Logger logger = Logger
			.getLogger(TeacherInterfaceController.class);

	@Resource
	TeacherDao teacherDao;
	@Resource
	TeacherLogDao teacherLogDao;
	@Resource
	ParameterKeeper parameterKeeper;
	@Resource
	CourseTaskResultWorkDao courseTaskResultWorkDao;
	@Resource
	FileAttachmentDao fileAttachmentDao;
	@Resource
	CourseTaskPlanDao courseTaskPlanDao;

	@ResponseBody
	@RequestMapping
	@Transactional
	public JsonResult login(@RequestParam("loginId") String loginId,
			@RequestParam("password") String password,
			@RequestParam("ip") String ip, @RequestParam("md5") String md5) {

		// MD5Checker.check(parameterKeeper, md5, loginId, password, ip);

		MapBean mb = teacherDao.meta4login(loginId, password);
		if (mb == null) {
			return JsonResult.e("无效的账号或密码");
		}

		teacherLogDao.save(mb.asLong("id"), mb.asLong("branchId"), ip, "登录");

		return JsonResult.s(mb);

	}

	@ResponseBody
	@RequestMapping
	@Transactional
	public JsonResult logout(@RequestParam("actorId") Long actorId,
			@RequestParam("ip") String ip, @RequestParam("md5") String md5) {

		MD5Checker.check(parameterKeeper, md5, actorId, ip);

		Long branchId = teacherDao.getBranchId(actorId);

		if (branchId == null) {
			return JsonResult.s();
		}

		teacherLogDao.save(actorId, branchId, ip, "退出");

		return JsonResult.s();

	}

	@ResponseBody
	@RequestMapping
	@Transactional
	public JsonResult updatePassword(@RequestParam("actorId") Long actorId,
			@RequestParam("oldPassword") String oldPassword,
			@RequestParam("newPassword") String newPassword,
			@RequestParam("ip") String ip, @RequestParam("md5") String md5) {

		MD5Checker.check(parameterKeeper, md5, actorId, oldPassword,
				newPassword, ip);

		Long branchId = teacherDao.updatePassword(actorId, oldPassword,
				newPassword);

		teacherLogDao.save(actorId, branchId, ip, "修改密码");

		return JsonResult.s();

	}

	@ResponseBody
	@RequestMapping
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	public JsonResult list4score(
			@RequestParam(value = "pageIndex", required = false, defaultValue = "1") int pageIndex,
			@RequestParam("actorId") Long actorId,
			@RequestParam("ip") String ip, @RequestParam("md5") String md5) {

		MD5Checker.check(parameterKeeper, md5, pageIndex, actorId, ip);

		PaginationSupport<MapBean> results = courseTaskResultWorkDao
				.list4score(actorId, pageIndex);

		return JsonResult.s(results);

	}

	@ResponseBody
	@RequestMapping
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	public JsonResult getCourseTaskStudent4Score(
			@RequestParam(value = "id") long courseTaskResultWorkId,
			@RequestParam("actorId") Long actorId,
			@RequestParam("ip") String ip, @RequestParam("md5") String md5) {

		MD5Checker.check(parameterKeeper, md5, courseTaskResultWorkId, actorId,
				ip);

		MapBean mb = courseTaskResultWorkDao.meta4score(courseTaskResultWorkId,
				actorId);

		return JsonResult.s(mb);

	}

	@ResponseBody
	@RequestMapping
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	public JsonResult getStudentWorkFile(
			@RequestParam(value = "id") long courseTaskResultWorkId,
			@RequestParam("actorId") Long actorId,
			@RequestParam("ip") String ip, @RequestParam("md5") String md5) {

		MD5Checker.check(parameterKeeper, md5, courseTaskResultWorkId, actorId,
				ip);

		MapBean mb = courseTaskResultWorkDao.getFile(courseTaskResultWorkId,
				actorId);

		if (mb == null) {
			Pe.raise("不允许读取");
		}

		// file time
		FileAttachment fa = fileAttachmentDao.getByRefId(
				CourseTaskResultWork.buildRefId(courseTaskResultWorkId,
						mb.string("attachmentKey")),
				FileAttachment.TYPE_STUENDT_WORK);

		String srcPath = null;

		srcPath = parameterKeeper
				.getFilePath(BhzqConstants.FILE_STUDENT_WORK_DIR);

		String fp = fa.getFileId();
		String fullPath = srcPath + File.separatorChar + fp;

		logger.debug("fullPath=" + fullPath);

		return JsonResult.s(MapBean.map("filePath", fullPath));

	}

	@ResponseBody
	@RequestMapping
	@Transactional
	public JsonResult makeScore(

	@RequestParam(value = "id") long id, @RequestParam("s1") int s1,
			@RequestParam("s2") int s2, @RequestParam("s3") int s3,
			@RequestParam("s4") int s4, @RequestParam("s5") int s5,
			@RequestParam("actorId") Long actorId,
			@RequestParam("ip") String ip, @RequestParam("md5") String md5) {

		MD5Checker.check(parameterKeeper, md5, id, s1, s2, s3, s4, s5, actorId,
				ip);

		MapBean mb = courseTaskResultWorkDao.meta4score(id, actorId);

		if (mb == null) {
			Pe.raise("越权访问或该作品评分时间超过3个小时");
		}

		Long branchId = teacherDao.getBranchId(actorId);

		Calendar time = mb.calendar("scoreTime");

		courseTaskResultWorkDao.makeScore(id, s1, s2, s3, s4, s5, time == null);

		teacherLogDao.save(actorId, branchId, ip,
				"评分:" + id + " ," + mb.string("studentName"));

		return JsonResult.s();

	}

	@ResponseBody
	@RequestMapping
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	public JsonResult getCalendar(@RequestParam(value = "year") int year,
			@RequestParam(value = "month") int month,
			@RequestParam("actorId") Long actorId,
			@RequestParam("ip") String ip, @RequestParam("md5") String md5) {

		MD5Checker.check(parameterKeeper, md5, year, month, actorId, ip);

		MapList ml = courseTaskPlanDao.listByTeacher(year, month, actorId);

		return JsonResult.s(ml);

	}
}
