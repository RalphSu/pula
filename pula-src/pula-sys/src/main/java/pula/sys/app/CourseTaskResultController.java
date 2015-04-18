package pula.sys.app;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import puerta.support.PaginationSupport;
import puerta.support.Pe;
import puerta.support.ViewResult;
import puerta.support.annotation.Barrier;
import puerta.support.annotation.ObjectParam;
import puerta.support.utils.DateExTool;
import puerta.support.utils.DateHelper;
import puerta.support.utils.WxlSugar;
import puerta.support.vo.SelectOption;
import puerta.support.vo.SelectOptionList;
import puerta.system.keeper.ParameterKeeper;
import puerta.system.vo.JsonResult;
import puerta.system.vo.MapBean;
import puerta.system.vo.MapList;
import puerta.system.vo.YuiResult;
import puerta.system.vo.YuiResultMapper;
import pula.sys.BhzqConstants;
import pula.sys.PurviewConstants;
import pula.sys.conditions.CourseTaskResultCondition;
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
import pula.sys.domains.Branch;
import pula.sys.domains.CourseTaskResult;
import pula.sys.domains.CourseTaskResultWork;
import pula.sys.domains.FileAttachment;
import pula.sys.domains.SysCategory;
import pula.sys.domains.Teacher;
import pula.sys.forms.CourseTaskResultForm;
import pula.sys.helpers.CourseTaskResultHelper;
import pula.sys.helpers.TrainLogHelper;
import pula.sys.services.SessionUserService;
import pula.sys.services.StudentPointsService;

@Controller
public class CourseTaskResultController {

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

//	@RequestMapping
//	@Transactional
//	@ResponseBody
//	public String mock_save() {
//		CourseTaskResult ctr = new CourseTaskResult();
//
//		ctr.setAssistant1(teacherDao.findByNo("123"));
//		ctr.setAssistant2(teacherDao.findByNo("1232"));
//		ctr.setMaster(teacherDao.findByNo("007"));
//
//		ctr.setBranch(Branch.create(sessionUserService.getBranch().getIdLong()));
//		ctr.setClassroom(Classroom.create(2));
//		ctr.setComments("OK");
//		ctr.setCourse(Course.create(1L));
//		ctr.setCreator(sessionUserService.getUser());
//		ctr.setStartTime(Calendar.getInstance());
//		ctr.setStudentCount(1);
//		ctr.setSubmitType(CourseTaskResult.ST_MANUAL);
//
//		ctr = courseTaskResultDao.save(ctr);
//
//		// students
//		CourseTaskResultStudent s = new CourseTaskResultStudent();
//		s.setCourseTaskResult(ctr);
//		s.setStudent(Student.create(16));
//		courseTaskResultStudentDao.save(s);
//		CourseTaskResultStudent s1 = new CourseTaskResultStudent();
//		s1.setCourseTaskResult(ctr);
//		s1.setStudent(Student.create(17));
//		courseTaskResultStudentDao.save(s1);
//
//		// work
//		CourseTaskResultWork w = new CourseTaskResultWork();
//		w.setCourseTaskResultStudent(s);
//
//		w.setAttachmentKey(RandomTool.getRandomString(5));
//		w.setScore1(1);
//		w.setScore2(2);
//		w.setScore3(3);
//		w.setScore4(4);
//		w.setScore5(5);
//		w.setScoreTime(Calendar.getInstance());
//		w = courseTaskResultWorkDao.save(w);
//		makeFile(w);
//
//		w = new CourseTaskResultWork();
//		w.setCourseTaskResultStudent(s1);
//		w.setAttachmentKey(RandomTool.getRandomString(5));
//		w.setScore1(4);
//		w.setScore2(5);
//		w.setScore3(6);
//		w.setScore4(7);
//		w.setScore5(8);
//		w.setScoreTime(Calendar.getInstance());
//		w = courseTaskResultWorkDao.save(w);
//		makeFile(w);
//
//		return "OK";
//	}

	private void makeFile(CourseTaskResultWork w) {
		FileAttachment fa = new FileAttachment();
		fa.setCreatedTime(Calendar.getInstance());
		fa.setExtName("jpg");
		fa.setFileId("2014.jpg");
		fa.setName("original.name");
		fa.setRefId(w.toRefId());
		fa.setType(w.getTypeRange());

		List<FileAttachment> attachments = WxlSugar.newArrayList();
		attachments.add(fa);

		fileAttachmentDao.save(w, attachments, false);

	}

	@RequestMapping
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	@Barrier(PurviewConstants.COURSE_TASK_RESULT)
	public ModelAndView entry(
			@ObjectParam("condition") CourseTaskResultCondition condition) {
		return _entry(condition);

	}

	private ModelAndView _entry(CourseTaskResultCondition condition) {
		if (condition == null) {
			condition = new CourseTaskResultCondition();
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
	@Barrier(PurviewConstants.COURSE_TASK_RESULT)
	public YuiResult list(
			@ObjectParam("condition") CourseTaskResultCondition condition,
			@RequestParam(value = "pageIndex", required = false, defaultValue = "0") int pageIndex) {
		// if (condition == null) {
		// condition = new SysCategoryCondition();
		// }
		// PaginationSupport<SysCategory> results = null;

		// MapList ml = new MapList();
		// Calendar now = Calendar.getInstance();
		// ml.add(MapBean.map("createdTime", now).add("studentCount", "10")
		// .add("teacherName", "王老师").add("courseCategoryName", "A")
		// .add("courseName", "艺术").add("branchName", "金桥总校")
		// .add("classroomName", "一号教室").add("id", 1));
		//
		// ml.add(MapBean
		// .map("createdTime", DateJedi.create(now).yesterday().to())
		// .add("studentCount", "8").add("teacherName", "王老师")
		// .add("courseCategoryName", "A").add("courseName", "艺术")
		// .add("branchName", "金桥总校").add("classroomName", "一号教室")
		// .add("id", 2));

		if (condition == null) {
			condition = new CourseTaskResultCondition();
			condition.setBeginDate(DateHelper.getThisMonthBegin());
			condition.setEndDate(DateHelper.getThisMonthEnd());
		}
		// PaginationSupport<Teacher> results = null;
		// results = teacherDao.search(condition, pageIndex);

		PaginationSupport<MapBean> results = courseTaskResultDao.search(
				condition, pageIndex);

		return YuiResult.create(results, new YuiResultMapper<MapBean>() {

			@Override
			public Map<String, Object> toMap(MapBean r) {
				r.add("allowEdit",
						CourseTaskResultController.this.allowEdit(
								r.calendar("startTime"), r.calendar("endTime")));
				return MAPPING_FIX.toMap(r);
			}
		});
	}

	@RequestMapping
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	@Barrier(PurviewConstants.COURSE_TASK_RESULT)
	public ModelAndView view(@RequestParam(value = "id") Long id) {
		if (!sessionUserService.isHeadquarter()) {
			this.courseTaskResultDao.checkAllowView(id, sessionUserService
					.getBranch().getIdLong());
		}

		Map<String, Object> mb = MAPPING_FIX.toMap(this.courseTaskResultDao
				.unique(id));

		return new ModelAndView().addObject("courseTaskResult", mb);

	}

	@RequestMapping
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	@Barrier(PurviewConstants.COURSE_TASK_RESULT)
	public ModelAndView create() {

		return loadBeforeView().addObject("updateMode", false)
				.addObject("startDate", DateExTool.getToday())
				.addObject("endDate", DateExTool.getToday());
	}

	@RequestMapping
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	@Barrier(PurviewConstants.COURSE_TASK_RESULT)
	public ModelAndView update(@RequestParam("id") Long id) {
		if (!sessionUserService.isHeadquarter()) {
			this.courseTaskResultDao.checkAllowView(id, sessionUserService
					.getBranch().getIdLong());
		}

		MapBean r = courseTaskResultDao.unique(id);

		Calendar startCal = r.calendar("startTime");
		Calendar endCal = r.calendar("endTime");

		int shour = startCal.get(Calendar.HOUR);
		int smins = startCal.get(Calendar.MINUTE);

		int ehour = endCal.get(Calendar.HOUR);
		int emins = endCal.get(Calendar.MINUTE);

		return loadBeforeView().addObject("courseTaskResult", r)
				.addObject("allowEdit", allowEdit(startCal, endCal))
				.addObject("updateMode", true).addObject("startHour", shour)
				.addObject("startMinute", smins).addObject("endHour", ehour)
				.addObject("endMinute", emins)
				.addObject("startDate", DateExTool.getText(startCal))
				.addObject("endDate", DateExTool.getText(endCal));
	}

	private ModelAndView loadBeforeView() {
		SelectOptionList minutes = SelectOption.getNumbers(0, 59, 0);
		SelectOptionList hours = SelectOption.getNumbers(8, 20, 0);
		String today = DateExTool.getToday();

		MapList branchList = branchDao.loadMetaWithoutHeadquarter();

		List<SysCategory> mts = sysCategoryDao
				.getUnder(BhzqConstants.SC_COURSE_CATEGORY);

		MapList classroomList = null;
		if (!sessionUserService.isHeadquarter()) {
			classroomList = classroomDao.list(sessionUserService.getBranch()
					.getIdLong());
		}

		return new ModelAndView().addObject("courseCategories", mts)
				.addObject("classroomList", classroomList)
				.addObject("branch", sessionUserService.getBranch())
				.addObject("today", today).addObject("minutes", minutes)
				.addObject("hours", hours).addObject("branches", branchList)
				.addObject("headquarter", sessionUserService.isHeadquarter());
	}

	// xinzeng

	@RequestMapping
	@Transactional
	@Barrier(PurviewConstants.COURSE_TASK_RESULT)
	@ResponseBody
	public JsonResult _create(@ObjectParam("form") CourseTaskResultForm form) {

		// 检查数据
		CourseTaskResult cc = form.toCourseTaskResult();
		prepare(cc, form);

		cc.setCreator(sessionUserService.getUser());
		cc.setSubmitType(CourseTaskResult.ST_MANUAL);
		cc = courseTaskResultDao.save(cc);

		return JsonResult.s(cc.getId());// 切入到更新状态

	}

	@RequestMapping
	@Transactional
	@Barrier(PurviewConstants.COURSE_TASK_RESULT)
	@ResponseBody
	public JsonResult _update(@ObjectParam("form") CourseTaskResultForm form) {

		// 检查数据
		CourseTaskResult cc = form.toCourseTaskResult();
		prepare(cc, form);

		cc.setUpdater(sessionUserService.getUser());
		cc = courseTaskResultDao.update(cc);

		return JsonResult.s(cc.getId());// 切入到更新状态

	}

	private void prepare(CourseTaskResult cc, CourseTaskResultForm form) {

		MapBean ctrMeta = null;
		if (form.getId() != 0) {
			ctrMeta = courseTaskResultDao.meta(form.getId());

			if (ctrMeta == null) {
				Pe.raise("无法找到对应的课程结果数据:" + form.getId());
			}

			if (!sessionUserService.isHeadquarter()) {
				long branch_id = ctrMeta.asLong("branchId");
				if (branch_id != sessionUserService.getBranch().getIdLong()) {
					Pe.raise("越权访问");
				}

			}

		}

		// 时间检查

		if (!cc.getStartTime().before(cc.getEndTime())) {
			Pe.raise("开始时间必须小于结束时间");
		}

		// 教学总监只能修改指定时间的
		if (!allowEdit(cc.getStartTime(), cc.getEndTime())) {
			Pe.raise("不允许填写历史月份的数据");
		}

		if (ctrMeta != null) {
			if (!allowEdit(ctrMeta.calendar("startTime"),
					ctrMeta.calendar("endTime"))) {
				Pe.raise("不允许修改历史月份的数据");
			}
		}

		// 锁定教室
		long branch_id = 0L;
		if (!sessionUserService.isHeadquarter()) {
			branch_id = sessionUserService.getBranch().getIdLong();
			if (!classroomDao.isBelongsTo(form.getClassroomId(), branch_id)) {
				Pe.raise("指定的教室并非属于当前分支机构");
			}
			// 有这个课程吗?在这个教室里
			// 检查课程部署
			if (!courseDeploymentDao.hasCourse(form.getClassroomId(),
					form.getCourseId())) {
				Pe.raise("指定的教室并未部署指定课程");
			}
		} else {
			branch_id = form.getBranchId();
			if (!classroomDao.isBelongsTo(form.getClassroomId(), branch_id)) {
				Pe.raise("指定的教室并非属于指定分支机构");
			}

		}

		// 教师,三名

		MapBean m_meta = getTeacherMeta(form.getMasterNo(), branch_id,
				"指定的教师编号无法找到", true);
		MapBean a1_meta = getTeacherMeta(form.getAssistant1No(), branch_id,
				"指定的助教1编号无法找到", true);
		MapBean a2_meta = getTeacherMeta(form.getAssistant2No(), branch_id,
				"指定的助教2编号无法找到", false);

		cc.setMaster(Teacher.create(m_meta.asLong("id")));
		cc.setAssistant1(Teacher.create(a1_meta.asLong("id")));
		if (a2_meta != null) {
			cc.setAssistant2(Teacher.create(a2_meta.asLong("id")));
		}

		cc.setBranch(Branch.create(branch_id));

	}

	private MapBean getTeacherMeta(String no, long branch_id, String msg,
			boolean notAllowEmpty) {

		MapBean mb = null;
		if (StringUtils.isEmpty(no)) {
			mb = null;
			if (notAllowEmpty) {
				Pe.raise(msg);
			}
		} else {
			mb = teacherDao.meta4plan(no, branch_id);
		}
		if (mb == null) {
			Pe.raise(msg + ":" + no);
		}

		return mb;
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

	@Transactional
	@RequestMapping
	@Barrier(PurviewConstants.COURSE_TASK_RESULT)
	public String remove(
			@RequestParam(value = "objId", required = false) Long[] id) {
		if (!sessionUserService.isHeadquarter()) {
			courseTaskResultDao.checkAllowRemove(id, sessionUserService
					.getBranch().getIdLong());

		}

		courseTaskResultDao.deleteById(id);
		return ViewResult.JSON_SUCCESS;
	}

}
