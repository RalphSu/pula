package pula.sys.app;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import puerta.PuertaWeb;
import puerta.support.PaginationSupport;
import puerta.support.Pe;
import puerta.support.ViewResult;
import puerta.support.annotation.Barrier;
import puerta.support.annotation.ObjectParam;
import puerta.support.utils.WxlSugar;
import puerta.system.keeper.ParameterKeeper;
import puerta.system.service.SessionService;
import puerta.system.vo.JsonResult;
import puerta.system.vo.MapList;
import puerta.system.vo.YuiResult;
import puerta.system.vo.YuiResultMapper;
import pula.sys.PurviewConstants;
import pula.sys.conditions.ActivityCondition;
import pula.sys.daos.ActivityDao;
import pula.sys.daos.BranchDao;
import pula.sys.domains.Activity;
import pula.sys.domains.ActivityBranch;
import pula.sys.forms.ActivityForm;
import pula.sys.services.SessionUserService;

@Controller
public class ActivityController {

	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger
			.getLogger(ActivityController.class);
	private static final YuiResultMapper<Activity> MAPPING = new YuiResultMapper<Activity>() {
		@Override
		public Map<String, Object> toMap(Activity obj) {

			Map<String, Object> m = WxlSugar.newHashMap();
			m.put("no", obj.getNo());
			m.put("id", obj.getId());
			m.put("name", obj.getName());
			m.put("enabled", obj.isEnabled());
			m.put("partner", obj.getPartner());
			m.put("beginDate", obj.getBeginDate());
			m.put("endDate", obj.getEndDate());

			return m;
		}
	};

	private static final YuiResultMapper<Activity> MAPPING_FULL = new YuiResultMapper<Activity>() {
		@Override
		public Map<String, Object> toMap(Activity obj) {

			Map<String, Object> m = MAPPING.toMap(obj);
			m.put("comments", obj.getComments());

			List<Long> list = WxlSugar.newArrayList();

			for (ActivityBranch ab : obj.getActivityBranches()) {
				list.add(ab.getBranch().getId());
			}

			// list
			m.put("branchIds", list);

			return m;
		}
	};

	@Resource
	ActivityDao activityDao;
	@Resource
	ParameterKeeper parameterKeeper;
	@Resource
	SessionService sessionService;
	@Resource
	SessionUserService sessionUserService;
	@Resource
	BranchDao branchDao;

	// @Resource
	// DictLimbKeeper dictLimbKeeper;

	@RequestMapping
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	@Barrier(PurviewConstants.ACTIVITY)
	public ModelAndView entry(
			@ObjectParam("condition") ActivityCondition condition) {
		if (condition == null) {
			condition = new ActivityCondition();
		}
		MapList branchList = branchDao.loadMeta();

		return new ModelAndView().addObject("condition", condition).addObject(
				"branches", branchList);
	}

	@RequestMapping
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	@ResponseBody
	@Barrier(PurviewConstants.ACTIVITY)
	public YuiResult list(
			@ObjectParam("condition") ActivityCondition condition,
			@RequestParam(value = "pageIndex", required = false, defaultValue = "0") int pageIndex) {
		if (condition == null) {
			condition = new ActivityCondition();
		}
		PaginationSupport<Activity> results = null;
		results = activityDao.search(condition, pageIndex);
		return YuiResult.create(results, MAPPING);
	}

	@RequestMapping
	@Barrier(PurviewConstants.ACTIVITY)
	public String create() {
		return null;
	}

	@RequestMapping
	@Transactional
	@Barrier(PurviewConstants.ACTIVITY)
	public String _create(@ObjectParam("activity") ActivityForm cli) {
		cli.setEnabled(true);
		Activity cc = cli.toActivity();

		cc.setCreator(sessionUserService.getUser());

		cc = activityDao.save(cc);

		activityDao.saveBranch(cc.getId(), cli.getBranchId(), false);

		return ViewResult.JSON_SUCCESS;
	}

	@RequestMapping
	@Transactional()
	@Barrier(PurviewConstants.ACTIVITY)
	public String _update(@ObjectParam("activity") ActivityForm cli) {

		Activity cc = cli.toActivity();
		cc.setUpdater(sessionUserService.getUser());
		activityDao.update(cc);

		activityDao.saveBranch(cc.getId(), cli.getBranchId(), true);

		return ViewResult.JSON_SUCCESS;
	}

	@Transactional
	@RequestMapping
	@Barrier(PurviewConstants.ACTIVITY)
	public String remove(
			@RequestParam(value = "objId", required = false) Long[] id) {
		// 总部不允许删

		activityDao.deleteById(id);
		return ViewResult.JSON_SUCCESS;
	}

	@RequestMapping
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	@ResponseBody
	@Barrier(PurviewConstants.ACTIVITY)
	public JsonResult get(@RequestParam("id") Long id) {
		Activity u = activityDao.findById(id);
		return JsonResult.s(MAPPING_FULL.toMap(u));
	}

	@RequestMapping
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	@ResponseBody
	@Barrier(PurviewConstants.ACTIVITY)
	public JsonResult getByNo(@RequestParam("no") String no) {
		Activity u = activityDao.findByNo(no);
		if (u == null) {
			Pe.raise("找不到指定的编号:" + no);
		}
		return JsonResult.s(MAPPING_FULL.toMap(u));
	}

	@RequestMapping
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	@Barrier(PurviewConstants.ACTIVITY)
	public ModelAndView find(
			@RequestParam(value = "no", required = false) String no) {
		ModelAndView m = new ModelAndView(ViewResult.JSON_LIST);

		List<Activity> list = activityDao.loadByKeywords(no, sessionUserService
				.getBranch().getIdLong());
		logger.debug("list.size=" + list.size());
		m.addObject("list", list);
		return m;
	}

	@Transactional
	@RequestMapping
	@Barrier(PurviewConstants.ACTIVITY)
	public String enable(
			@RequestParam(value = "objId", required = false) Long[] id,
			@RequestParam(value = "enable", required = false) boolean enable) {

		activityDao.doEnable(id, enable);
		return ViewResult.JSON_SUCCESS;
	}

	@RequestMapping
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	@Barrier(PurviewConstants.ACTIVITY)
	public ModelAndView current(
			@ObjectParam("condition") ActivityCondition condition,
			@RequestParam(value = "pageIndex", required = false, defaultValue = "0") int pageIndex) {
		if (condition == null) {
			condition = new ActivityCondition();
		}
		MapList branchList = null;

		if (sessionUserService.isHeadquarter()) {
			branchList = branchDao.loadMeta();
			if (condition.getBranchId() <= 0) {
				condition.setBranchId(sessionUserService.getBranch()
						.getIdLong());
			}
		} else {
			condition.setBranchId(sessionUserService.getBranch().getIdLong());
		}

		if (condition.getEnabledStatus() == 0) {
			condition.setEnabledStatus(PuertaWeb.YES);
		}
		if (condition.isHistory()) {
			condition.setEnabledStatus(PuertaWeb.NO);
		}

		PaginationSupport<Activity> results = activityDao.search(condition,
				pageIndex);

		// all
		return new ModelAndView().addObject("condition", condition)
				.addObject("branches", branchList)
				.addObject("results", results);
	}
}
