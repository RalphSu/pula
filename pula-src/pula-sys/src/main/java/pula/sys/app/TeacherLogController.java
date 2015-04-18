package pula.sys.app;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import puerta.support.PaginationSupport;
import puerta.support.annotation.Barrier;
import puerta.support.annotation.ObjectParam;
import puerta.support.utils.DateExTool;
import puerta.system.vo.MapBean;
import puerta.system.vo.MapList;
import puerta.system.vo.YuiResult;
import pula.sys.PurviewConstants;
import pula.sys.conditions.TeacherLogCondition;
import pula.sys.daos.BranchDao;
import pula.sys.daos.TeacherDao;
import pula.sys.daos.TeacherLogDao;
import pula.sys.services.SessionUserService;

@Controller
public class TeacherLogController {

	@SuppressWarnings("unused")
	private static final Logger logger = Logger
			.getLogger(TeacherLogController.class);

	public static final String SPLIT = ",";

	public static final String SPLIT_ROW = ";";

	@Resource
	private TeacherLogDao teacherLogDao;
	@Resource
	SessionUserService sessionUserService;
	@Resource
	BranchDao branchDao;
	@Resource
	TeacherDao teacherDao;

	@RequestMapping
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	@Barrier(PurviewConstants.TEACHER_LOG)
	public ModelAndView entry(
			@ObjectParam("condition") TeacherLogCondition condition) {

		if (condition == null) {
			condition = new TeacherLogCondition();
			condition.setBeginDate(DateExTool.getToday());
			condition.setEndDate(DateExTool.getToday());
		}
		MapList branches = branchDao.loadMeta();

		return new ModelAndView().addObject("condition", condition).addObject(
				"branches", branches);

	}

	@RequestMapping
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	@ResponseBody
	@Barrier(PurviewConstants.TEACHER_LOG)
	public YuiResult list(
			@ObjectParam("condition") TeacherLogCondition condition,
			@RequestParam(value = "pageIndex", required = false, defaultValue = "0") int pageIndex) {
		if (condition == null) {
			condition = new TeacherLogCondition();
		}
		PaginationSupport<MapBean> results = null;
		results = teacherLogDao.search(condition, pageIndex);
		return YuiResult.create(results);
	}

	@RequestMapping
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	@ResponseBody
	@Barrier(PurviewConstants.TEACHER_LOG)
	public YuiResult list4PersonalLogs(
			@ObjectParam("condition") TeacherLogCondition condition,
			@RequestParam(value = "pageIndex", required = false, defaultValue = "0") int pageIndex) {
		// if (condition == null) {
		// condition = new SysCategoryCondition();
		// }
		// PaginationSupport<SysCategory> results = null;

		// WARN:必须传入teacher_id
		// 分校还得传入分校ID

		// MapList ml = new MapList();
		// Calendar now = Calendar.getInstance();
		// ml.add(MapBean.map("eventTime", now).add("branchName", "金桥分校")
		// .add("event", "登录").add("ip", "192.168.1.1")
		// .add("extendInfo", ""));
		// ml.add(MapBean.map("eventTime", now).add("branchName", "金桥分校")
		// .add("event", "评分").add("ip", "192.168.1.3")
		// .add("extendInfo", "学生：PDA00001-张晓光"));

		if (condition == null) {
			condition = new TeacherLogCondition();
		}

		if (!sessionUserService.isHeadquarter()) {
			teacherDao.checkAllowView(condition.getTeacherId(),
					sessionUserService.getBranch().getIdLong());
			condition.setBranchId(sessionUserService.getBranch().getIdLong());
		}

		// teacher id 假设是从condition中来的

		PaginationSupport<MapBean> results = null;
		results = teacherLogDao.search(condition, pageIndex);

		return YuiResult.create(results);
	}

}
