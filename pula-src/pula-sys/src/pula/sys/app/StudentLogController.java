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
import pula.sys.conditions.StudentLogCondition;
import pula.sys.daos.BranchDao;
import pula.sys.daos.StudentDao;
import pula.sys.daos.StudentLogDao;
import pula.sys.services.SessionUserService;

@Controller
public class StudentLogController {

	@SuppressWarnings("unused")
	private static final Logger logger = Logger
			.getLogger(StudentLogController.class);

	public static final String SPLIT = ",";

	public static final String SPLIT_ROW = ";";

	@Resource
	private StudentLogDao studentLogDao;
	@Resource
	SessionUserService sessionUserService;
	@Resource
	BranchDao branchDao;
	@Resource
	StudentDao studentDao;

	@RequestMapping
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	@Barrier(PurviewConstants.STUDENT_LOG)
	public ModelAndView entry(
			@ObjectParam("condition") StudentLogCondition condition) {

		if (condition == null) {
			condition = new StudentLogCondition();
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
	@Barrier(PurviewConstants.STUDENT_LOG)
	public YuiResult list(
			@ObjectParam("condition") StudentLogCondition condition,
			@RequestParam(value = "pageIndex", required = false, defaultValue = "0") int pageIndex) {
		if (condition == null) {
			condition = new StudentLogCondition();
		}
		PaginationSupport<MapBean> results = null;
		results = studentLogDao.search(condition, pageIndex);
		return YuiResult.create(results);
	}

	@RequestMapping
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	@ResponseBody
	@Barrier(PurviewConstants.STUDENT_LOG)
	public YuiResult list4PersonalLogs(
			@ObjectParam("condition") StudentLogCondition condition,
			@RequestParam(value = "pageIndex", required = false, defaultValue = "0") int pageIndex) {

		if (condition == null) {
			condition = new StudentLogCondition();
		}

		if (!sessionUserService.isHeadquarter()) {
			condition.setBranchId(sessionUserService.getBranch().getIdLong());
			studentDao.checkAllowView(condition.getStudentId(),
					sessionUserService.getBranch().getIdLong());
		}

		PaginationSupport<MapBean> results = null;
		results = studentLogDao.search(condition, pageIndex);

		return YuiResult.create(results);
	}
}
