package pula.sys.app;

import java.util.Calendar;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import puerta.support.Pe;
import puerta.support.annotation.Barrier;
import puerta.support.annotation.ObjectParam;
import puerta.support.utils.DateExTool;
import puerta.support.vo.SelectOptionList;
import puerta.system.vo.JsonResult;
import puerta.system.vo.MapBean;
import puerta.system.vo.MapList;
import pula.sys.BhzqConstants;
import pula.sys.PurviewConstants;
import pula.sys.daos.BranchDao;
import pula.sys.daos.ClassroomDao;
import pula.sys.daos.CourseDeploymentDao;
import pula.sys.daos.CourseTaskPlanDao;
import pula.sys.daos.SysCategoryDao;
import pula.sys.daos.TeacherDao;
import pula.sys.domains.Branch;
import pula.sys.domains.Classroom;
import pula.sys.domains.Course;
import pula.sys.domains.CourseTaskPlan;
import pula.sys.domains.SysCategory;
import pula.sys.domains.Teacher;
import pula.sys.forms.CourseTaskPlanForm;
import pula.sys.helpers.CourseTaskPlanHelper;
import pula.sys.services.SessionUserService;

@Controller
public class CourseTaskPlanController {

	@Resource
	SessionUserService sessionUserService;
	@Resource
	BranchDao branchDao;
	@Resource
	TeacherDao teacherDao;
	@Resource
	ClassroomDao classroomDao;

	@Resource
	SysCategoryDao sysCategoryDao;
	@Resource
	CourseTaskPlanDao courseTaskPlanDao;
	@Resource
	CourseDeploymentDao courseDeploymentDao;

	@RequestMapping
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	@Barrier({ PurviewConstants.COURSE_TASK_PLAN })
	public ModelAndView entry(
			@RequestParam(value = "year", required = false, defaultValue = "0") int year,
			@RequestParam(value = "month", required = false, defaultValue = "0") int month,
			@RequestParam(value = "branchId", required = false, defaultValue = "0") long branchId) {

		return _entry(year, month, branchId, false);

	}

	@RequestMapping
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	@Barrier({ PurviewConstants.COURSE_TASK_PLAN_VIEW })
	public ModelAndView calendar(
			@RequestParam(value = "year", required = false, defaultValue = "0") int year,
			@RequestParam(value = "month", required = false, defaultValue = "0") int month,
			@RequestParam(value = "branchId", required = false, defaultValue = "0") long branchId) {

		return _entry(year, month, branchId, true);

	}

	private ModelAndView _entry(int year, int month, long branchId,
			boolean forceView) {
		Calendar now = Calendar.getInstance();
		int nowYear = now.get(Calendar.YEAR);
		int nowMonth = now.get(Calendar.MONTH) + 1;
		if (year == 0) {
			year = nowYear;
			month = nowMonth;
		}
		MapList branchList = branchDao.loadMetaWithoutHeadquarter();
		if (!sessionUserService.isHeadquarter()) {
			branchId = sessionUserService.getBranch().getIdLong();
		}

		boolean canSetup = sessionUserService.isHeadquarter() ||

		CourseTaskPlanHelper.allowEdit(year, month);

		// 没有指定分部强制不能设定
		boolean canShow = false;
		if (branchId == 0 && sessionUserService.isHeadquarter()) {
			// do nothing
			canSetup = false;
			canShow = false;
		} else {
			canShow = true;
		}

		MapList classroomList = new MapList();
		List<SysCategory> mts = null;
		if (canShow) {
			classroomList = classroomDao.list(branchId);

			mts = sysCategoryDao.getUnder(BhzqConstants.SC_COURSE_CATEGORY);
		}

		SelectOptionList hours = CourseTaskPlanHelper.getHours();
		SelectOptionList minutes = CourseTaskPlanHelper.getMinutes();

		return new ModelAndView().addObject("year", year)
				.addObject("hours", hours).addObject("minutes", minutes)
				.addObject("canSetup", canSetup && !forceView)
				.addObject("forceView", forceView)
				.addObject("canShow", canShow)
				.addObject("branches", branchList)
				.addObject("classroomList", classroomList)
				.addObject("branch", sessionUserService.getBranch())
				.addObject("branchId", branchId)
				.addObject("headquarter", sessionUserService.isHeadquarter())
				.addObject("nowMonth", nowMonth).addObject("month", month)
				.addObject("nowYear", nowYear).addObject("types", mts);
	}

	/**
	 * 加载数据 三种信息要融合，1.时段信息，2.休息日或工作日，3.时段内人员安排
	 * 
	 * @param year
	 * @param month
	 * @return
	 */
	@RequestMapping
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	@ResponseBody
	@Barrier({ PurviewConstants.COURSE_TASK_PLAN })
	public JsonResult _calendar(
			@RequestParam(value = "year", required = false, defaultValue = "0") int year,
			@RequestParam(value = "month", required = false, defaultValue = "0") int month,
			@RequestParam(value = "branchId", required = false, defaultValue = "0") long branchId) {

		Calendar calendar = Calendar.getInstance();
		if (year == 0) {
			year = calendar.get(Calendar.YEAR);
			month = calendar.get(Calendar.MONTH) + 1;
		}

		// MapList restDays = restDayDao.mapList(year);
		// // MapList validities = attendancePeriodValidityDao
		// // .mapList(AttendancePeriod.DEPENDS_ON_SCHEDULE);
		//
		// RestDayHolder rdHolder = new RestDayHolder(restDays);
		//
		// // 时段没有意义，由于调整会导致历史的时段消失或出现，不能精准了解，所以时段默认全部列出
		// // AttendancePeriodValidityHolder apvHolder = new
		// // AttendancePeriodValidityHolder(
		// // validities);
		//
		// // apvHolder.hasPeriod ( )
		//
		// // 载入这个月的安排
		// MapList ml = attendanceScheduleDao.mapList(year, month);
		MapBean result = MapBean.map();

		// MapList ml = new MapList();
		// ml.add(MapBean.map("id", 1).add("name", "8:00 张老师"));
		// ml.add(MapBean.map("id", 2).add("name", "9:00 李老师"));
		// result.add("1", MapBean.map().add("employee", ml));
		//

		if (!sessionUserService.isHeadquarter()) {
			branchId = sessionUserService.getBranch().getIdLong();
		}
		MapList ml = courseTaskPlanDao.mapList(year, month, branchId);

		// 展开一下
		for (MapBean mb : ml) {
			Calendar cal = mb.calendar("startTime");
			int day = cal.get(Calendar.DAY_OF_MONTH);
			// int hour = cal.get(Calendar.HOUR_OF_DAY);
			// int min = cal.get(Calendar.MINUTE);

			String key = String.valueOf(day);

			MapList employees = null;
			if (result.containsKey(key)) {
				employees = (MapList) ((MapBean) result.get(key))
						.get("employee");
			} else {
				employees = new MapList();
				result.put(key, MapBean.map("employee", employees));
			}
			employees.add(MapBean.map("id", mb.asLong("id")).add(
					"name",
					CourseTaskPlanHelper.getTimeShortText(cal) + " "
							+ mb.string("teacherName")));

		}

		//
		// // 再按本月的天数生成所有数据
		//
		// int maxDay = DateJedi.create(year, month - 1).moveMonth(1)
		// .firstDayOfMonth().yesterday().to().get(Calendar.DAY_OF_MONTH);
		//
		// for (int i = 1; i <= maxDay; i++) {
		// String key = String.valueOf(i);
		// MapBean mb = null;
		// if (result.containsKey(key)) {
		// mb = (MapBean) result.get(key);
		// } else {
		// mb = new MapBean();
		// result.put(key, mb);
		// }
		//
		// // mb 里面要放 是否休息日信息，加上上面的人员信息
		// // 三个时段的key 都要有
		// for (int k : AttendancePeriodHelper.ITEM_TYPES) {
		// boolean restDay = rdHolder.isRestDay(year, month, i, k);
		// mb.add(String.valueOf(k), restDay);
		// }
		//
		// }
		//
		// // 把人员数据分配一遍
		// for (MapBean mb : ml) {
		// Calendar cal = mb.calendar("scheduleDate");
		// int day = cal.get(Calendar.DAY_OF_MONTH);
		//
		// MapList list = null;
		// String key = String.valueOf(day);
		//
		// MapBean data = result.asMapBean(key);
		// String ekey = "employee" + mb.asInteger("period");
		// if (data.containsKey(ekey)) {
		// list = data.object(ekey);
		// } else {
		// list = new MapList();
		// data.add(ekey, list);
		// }
		//
		// list.add(mb);
		// }
		boolean canSetup = CourseTaskPlanHelper.allowEdit(year, month);

		result.add("canSetup", canSetup);

		return JsonResult.s(result);

	}

	@RequestMapping
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	@ResponseBody
	@Barrier({ PurviewConstants.COURSE_TASK_PLAN })
	public JsonResult _get(
			@RequestParam("date") String date,
			@RequestParam(value = "branchId", required = false, defaultValue = "0") long branchId) {

		if (!sessionUserService.isHeadquarter()) {
			branchId = sessionUserService.getBranch().getIdLong();
		}

		return _loadByDate(date, branchId);

	}

	private JsonResult _loadByDate(String date, long branchId) {
		Calendar sDate = DateExTool.getDate(date);
		MapList ml = courseTaskPlanDao.planList(sDate, branchId);

		MapList result = new MapList();
		for (MapBean mb : ml) {
			Calendar cal = mb.calendar("startTime");
			String name = CourseTaskPlanHelper.getTimeShortText(cal) + " "
					+ mb.string("masterName") + ","
					+ mb.string("assistant1Name");

			if (!StringUtils.isEmpty(mb.string("assistant2Name"))) {
				name += "," + mb.string("assistant2Name");
			}
			name += " " + mb.string("courseName");
			result.add(MapBean.map("id", mb.asLong("id")).add("name", name)
					.add("no", mb.string("classroomName"))
					.add("comments", mb.string("comments")));
		}

		return JsonResult.s(result);
	}

	@RequestMapping
	@Transactional
	@ResponseBody
	@Barrier({ PurviewConstants.COURSE_TASK_PLAN })
	public JsonResult _update(@ObjectParam("form") CourseTaskPlanForm form) {
		// 日期,分支机构是否允许编辑

		long branch_id = form.getBranchId();
		if (!sessionUserService.isHeadquarter()) {
			branch_id = sessionUserService.getBranch().getIdLong();
		}

		if (form.getId() != 0 && !sessionUserService.isHeadquarter()) {
			courseTaskPlanDao.checkAllowEdit(form.getId(), branch_id);
		}

		// 不允许编辑啊!

		// 检查数个信息
		// 时间是否正确
		CourseTaskPlanHelper.checkDate(form.getHour(), form.getMinute());

		// 教师对还是不对
		MapBean tmb = teacherDao.meta4plan(form.getTeacherNo(), branch_id);

		if (tmb == null) {
			Pe.raise("指定的教师编号不存在或不属于当前分支机构:" + form.getTeacherNo());
		}

		MapBean amb = teacherDao.meta4plan(form.getAssistant1No(), branch_id);

		if (amb == null) {
			Pe.raise("指定的助教编号不存在或不属于当前分支机构:" + form.getAssistant1No());
		}

		if (amb.asLong("id") == tmb.asLong("id")) {
			Pe.raise("教师和助教1不能为同一个人");
		}
		MapBean amb2 = null;
		if (!StringUtils.isEmpty(form.getAssistant2No())) {
			amb2 = teacherDao.meta4plan(form.getAssistant2No(), branch_id);

			if (amb2 == null) {
				Pe.raise("指定的助教编号不存在或不属于当前分支机构:" + form.getAssistant2No());
			}

			if (amb2.asLong("id") == tmb.asLong("id")) {
				Pe.raise("教师和助教2不能为同一个人");
			}

			if (amb2.asLong("id") == amb.asLong("id")) {
				Pe.raise("助教1和助教2不能为同一个人");
			}
		}

		// classroom
		if (!classroomDao.isBelongsTo(form.getClassroomId(), branch_id)) {
			Pe.raise("指定的教室不属于当前分支机构");
		}

		// 教程!
		if (!courseDeploymentDao.hasCourse(form.getClassroomId(),
				form.getCourseId())) {
			Pe.raise("指定的教室尚未部署该课程,请与总部申请");
		}

		// save all
		CourseTaskPlan plan = form.toCourseTaskPlan();

		Calendar obj = plan.getStartTime();
		if (!CourseTaskPlanHelper.allowEdit(obj.get(Calendar.YEAR),
				obj.get(Calendar.MONTH) + 1)) {
			Pe.raise("数据已经超过可修改期限");
		}

		plan.setMaster(Teacher.create(tmb.asLong("id")));
		plan.setAssistant1(Teacher.create(amb.asLong("id")));
		if (amb2 != null)
			plan.setAssistant2(Teacher.create(amb2.asLong("id")));
		plan.setBranch(Branch.create(branch_id));
		plan.setClassroom(Classroom.create(form.getClassroomId()));
		plan.setCourse(Course.create(form.getCourseId()));
		plan.setCreator(sessionUserService.getUser());

		if (form.getId() == 0) {
			plan = courseTaskPlanDao.save(plan);
		} else {
			plan = courseTaskPlanDao.update(plan);
		}

		// MapList ml = new MapList();
		// ml.add(MapBean.map("id", 1).add("name", "8:00 张老师,李老师   某某课程")
		// .add("no", "某某教室 "));
		//
		// ml.add(MapBean.map("id", 2).add("name", "8:00 张老师2,李老师   某某课程")
		// .add("no", "某某教室 "));
		// return JsonResult.s(ml);

		return _loadByDate(form.getDate(), branch_id);

		// return JsonResult.s(MapBean.map("id", plan.getId()).add(
		// "name",
		// CourseTaskPlanHelper.getTimeShortText(plan.getStartTime())
		// + " " + tmb.string("name")));

	}

	@RequestMapping
	@Transactional()
	@ResponseBody
	@Barrier({ PurviewConstants.COURSE_TASK_PLAN })
	public JsonResult _cancel(@RequestParam("id") Long id) {

		// remove id

		if (!sessionUserService.isHeadquarter()) {
			long branch_id = sessionUserService.getBranch().getIdLong();
			courseTaskPlanDao.checkAllowEdit(id, branch_id);
		}

		courseTaskPlanDao.deleteById(id);

		return JsonResult.s();

	}

	@RequestMapping
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	@ResponseBody
	@Barrier({ PurviewConstants.COURSE_TASK_PLAN })
	public JsonResult _getItem(@RequestParam("id") Long id) {

		MapBean mb = courseTaskPlanDao.unique(id);

		//
		return JsonResult.s(mb);
	}
}
