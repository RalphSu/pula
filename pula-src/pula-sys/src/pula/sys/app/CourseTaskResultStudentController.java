package pula.sys.app;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import puerta.support.PaginationSupport;
import puerta.support.Pe;
import puerta.support.annotation.Barrier;
import puerta.support.annotation.ObjectParam;
import puerta.support.utils.DateHelper;
import puerta.system.keeper.ParameterKeeper;
import puerta.system.vo.JsonResult;
import puerta.system.vo.MapBean;
import puerta.system.vo.MapList;
import puerta.system.vo.YuiResult;
import puerta.system.vo.YuiResultMapper;
import pula.sys.BhzqConstants;
import pula.sys.PurviewConstants;
import pula.sys.conditions.CourseTaskResultStudentCondition;
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
import pula.sys.domains.CourseTaskResult;
import pula.sys.domains.CourseTaskResultStudent;
import pula.sys.domains.Student;
import pula.sys.domains.StudentPoints;
import pula.sys.domains.SysCategory;
import pula.sys.helpers.CourseTaskResultHelper;
import pula.sys.helpers.TrainLogHelper;
import pula.sys.services.OrderFormService;
import pula.sys.services.SessionUserService;
import pula.sys.services.StudentPointsService;

@Controller
public class CourseTaskResultStudentController {
	private static final YuiResultMapper<MapBean> MAPPING_FIX = new YuiResultMapper<MapBean>() {

		@Override
		public Map<String, Object> toMap(MapBean obj) {

			obj.put("submitTypeName", CourseTaskResultHelper
					.getSubmitTypeName(obj.asInteger("submitType")));

			return obj;

		}
	};

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
	@Resource
	OrderFormService orderFormService;

	// 新建一个学员

	@Transactional
	@RequestMapping
	@Barrier(check=false,value=PurviewConstants.COURSE_TASK_RESULT)
	@ResponseBody
	public JsonResult _createStudent(
			@RequestParam("studentNo") String studentNo,
			@RequestParam("id") Long courseTaskResultId) {

		MapBean ctrMeta = courseTaskResultDao.meta(courseTaskResultId);

		if (!sessionUserService.isHeadquarter()) {
			long branch_id = ctrMeta.asLong("branchId");
			if (branch_id != sessionUserService.getBranch().getIdLong()) {
				Pe.raise("越权访问");
			}
		}

		if (ctrMeta != null) {
			if (!allowEdit(ctrMeta.calendar("startTime"),
					ctrMeta.calendar("endTime"))) {
				Pe.raise("不允许修改历史月份的数据");
			}
		} else

		{
			Pe.raise("无法找到对应的课程结果数据:" + courseTaskResultId);
		}

		MapBean studentMeta = studentDao.meta4exhange(studentNo);

		if (studentMeta == null) {
			Pe.raise("指定的学员编号不存在:" + studentNo);
		}

		if (!sessionUserService.isHeadquarter()) {
			long branch_id = studentMeta.asLong("branchId");
			if (branch_id != sessionUserService.getBranch().getIdLong()) {
				Pe.raise("指定的学员并非从属于当前分支机构");
			}
		}
		long studentId = studentMeta.asLong("id");
		if (courseTaskResultStudentDao.exists(courseTaskResultId, studentId)) {
			Pe.raise("指定的学生数据已经存在于结果中,请勿重复添加");
		}

		CourseTaskResultStudent s = new CourseTaskResultStudent();
		s.setCourseTaskResult(CourseTaskResult.create(courseTaskResultId));
		s.setStudent(Student.create(studentId));
		s.setSubmitType(CourseTaskResult.ST_MANUAL);
		s = courseTaskResultStudentDao.save(s);

		courseTaskResultDao.incStudent(courseTaskResultId, 1);

		// 分数情况
		int ptz = parameterKeeper.getInt(BhzqConstants.POINTS_TAKE_CLASS, 10);
		studentPointsService.save(studentId, StudentPoints.FORM_TAKE_CLASS,
				ptz, "上课积分", 0, null, buildRefId(s.getId()));

		// 销课!

		orderFormService.consume(studentId);

		return JsonResult.s();
	}

	private String buildRefId(long id) {
		return "CTRS:" + id;
	}

	@Transactional
	@RequestMapping
	@Barrier(check=false,value=PurviewConstants.COURSE_TASK_RESULT)
	@ResponseBody
	public JsonResult _removeStudent(
			@RequestParam("id") Long courseTaskResultStudentId) {
		MapBean meta = courseTaskResultStudentDao
				.meta(courseTaskResultStudentId);

		precheckCourseTaskResult(meta);

		// 扣分~
		studentPointsService.erase(buildRefId(courseTaskResultStudentId),
				"取消结果");

		// 删除
		courseTaskResultStudentDao.deleteById(courseTaskResultStudentId);

		courseTaskResultDao.incStudent(meta.asLong("courseTaskResultId"), -1);

		// 取消销课
		orderFormService.restore(meta.asLong("studentId"));

		return JsonResult.s();
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
	@Barrier(value=PurviewConstants.COURSE_TASK_RESULT,check=false)
	public ModelAndView search(
			@ObjectParam("condition") CourseTaskResultStudentCondition condition) {
		return _entry(condition);

	}

	private ModelAndView _entry(CourseTaskResultStudentCondition condition) {
		if (condition == null) {
			condition = new CourseTaskResultStudentCondition();
		}

		// SelectOptionList levelList = StudentHelper.getLevelList(0);

		MapList branchList = branchDao.loadMetaWithoutHeadquarter();

		List<SysCategory> mts = sysCategoryDao
				.getUnder(BhzqConstants.SC_COURSE_CATEGORY);

		MapList classroomList = null;
		if (!sessionUserService.isHeadquarter()) {
			classroomList = classroomDao.list(sessionUserService.getBranch()
					.getIdLong());
		}

		return new ModelAndView().addObject("condition", condition)
				.addObject("courseCategories", mts)
				.addObject("classroomList", classroomList)
				.addObject("branches", branchList)
				.addObject("headquarter", sessionUserService.isHeadquarter());
	}

	@RequestMapping
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	@ResponseBody
	@Barrier(value=PurviewConstants.COURSE_TASK_RESULT,check=false)
	public YuiResult list4search(
			@ObjectParam("condition") CourseTaskResultStudentCondition condition,
			@RequestParam(value = "pageIndex", required = false, defaultValue = "0") int pageIndex) {

		if (condition == null) {
			condition = new CourseTaskResultStudentCondition();
			condition.setBeginDate(DateHelper.getThisMonthBegin());
			condition.setEndDate(DateHelper.getThisMonthEnd());
		}

		PaginationSupport<MapBean> results = courseTaskResultStudentDao.search(
				condition, pageIndex);

		return YuiResult.create(results, MAPPING_FIX);
	}

	@RequestMapping
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	@Barrier(check=false,value={ PurviewConstants.TEACHER, PurviewConstants.TEACHER_SEARCH })
	public ModelAndView teacher(@RequestParam("id") Long id) {

		if (!sessionUserService.isHeadquarter()) {
			teacherDao.checkAllowView(id, sessionUserService.getBranch()
					.getIdLong());
		}

		MapBean meta = teacherDao.meta(id);
		if (meta == null) {
			Pe.raise("找不到指定的教师");
		}
		return _entry(null).addObject("id", id).addObject("teacher_meta", meta);

	}

	@RequestMapping
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	@ResponseBody
	@Barrier(value={ PurviewConstants.TEACHER, PurviewConstants.TEACHER_SEARCH },check=false)
	public YuiResult list4teacher(
			@ObjectParam("condition") CourseTaskResultStudentCondition condition,
			@RequestParam(value = "pageIndex", required = false, defaultValue = "0") int pageIndex) {

		if (condition == null) {
			condition = new CourseTaskResultStudentCondition();
			condition.setBeginDate(DateHelper.getThisMonthBegin());
			condition.setEndDate(DateHelper.getThisMonthEnd());
		}

		if (!sessionUserService.isHeadquarter()) {
			teacherDao.checkAllowView(condition.getTeacherId(),
					sessionUserService.getBranch().getIdLong());
			condition.setBranchId(sessionUserService.getBranch().getIdLong());
		}

		PaginationSupport<MapBean> results = courseTaskResultStudentDao.search(
				condition, pageIndex);

		return YuiResult.create(results, MAPPING_FIX);
	}

	@RequestMapping
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	@Barrier(value={ PurviewConstants.STUDENT },check=false)
	public ModelAndView student(@RequestParam("id") Long id) {

		if (!sessionUserService.isHeadquarter()) {
			studentDao.checkAllowView(id, sessionUserService.getBranch()
					.getIdLong());
		}

		MapBean meta = studentDao.meta(id);
		if (meta == null) {
			Pe.raise("找不到指定的教师");
		}
		return _entry(null).addObject("id", id).addObject("student_meta", meta);

	}

	@RequestMapping
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	@ResponseBody
	@Barrier(value={ PurviewConstants.STUDENT },check=false)
	public YuiResult list4student(
			@ObjectParam("condition") CourseTaskResultStudentCondition condition,
			@RequestParam(value = "pageIndex", required = false, defaultValue = "0") int pageIndex) {

		if (condition == null) {
			condition = new CourseTaskResultStudentCondition();
			condition.setBeginDate(DateHelper.getThisMonthBegin());
			condition.setEndDate(DateHelper.getThisMonthEnd());
		}
		if (!sessionUserService.isHeadquarter()) {
			studentDao.checkAllowView(condition.getTeacherId(),
					sessionUserService.getBranch().getIdLong());
			condition.setBranchId(sessionUserService.getBranch().getIdLong());
		}

		PaginationSupport<MapBean> results = courseTaskResultStudentDao.search(
				condition, pageIndex);

		return YuiResult.create(results, MAPPING_FIX);
	}
}
