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

import puerta.support.PaginationSupport;
import puerta.support.Pe;
import puerta.support.ViewResult;
import puerta.support.annotation.Barrier;
import puerta.support.annotation.ObjectParam;
import puerta.support.utils.WxlSugar;
import puerta.support.vo.Mix;
import puerta.support.vo.SelectOptionList;
import puerta.system.keeper.ParameterKeeper;
import puerta.system.vo.JsonResult;
import puerta.system.vo.MapBean;
import puerta.system.vo.MapList;
import puerta.system.vo.YuiResult;
import puerta.system.vo.YuiResultMapper;
import pula.sys.PurviewConstants;
import pula.sys.conditions.StockEventCondition;
import pula.sys.daos.BranchDao;
import pula.sys.daos.MaterialDao;
import pula.sys.daos.StockDao;
import pula.sys.daos.StockEventDao;
import pula.sys.daos.StockLogDao;
import pula.sys.domains.Branch;
import pula.sys.domains.Material;
import pula.sys.domains.StockEvent;
import pula.sys.domains.StockLog;
import pula.sys.domains.SysUser;
import pula.sys.forms.StockEventForm;
import pula.sys.helpers.StockEventHelper;
import pula.sys.services.SessionUserService;

@Controller
public class StockEventController {
	@SuppressWarnings("unused")
	private static final Logger log1ger = Logger
			.getLogger(StockEventController.class);
	private static final YuiResultMapper<MapBean> MAPPING_FIX = new YuiResultMapper<MapBean>() {

		@Override
		public Map<String, Object> toMap(MapBean obj) {
			// TODO Auto-generated method stub
			return obj.add("targetName",
					StockEventHelper.getTargetName(obj.integer("target")));
		}

	};

	@Resource
	MaterialDao materialDao;
	@Resource
	BranchDao branchDao;
	@Resource
	SessionUserService sessionUserService;
	@Resource
	StockLogDao stockLogDao;
	@Resource
	ParameterKeeper parameterKeeper;
	@Resource
	StockDao stockDao;

	@Resource
	StockEventDao stockEventDao;

	@RequestMapping
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	@Barrier(PurviewConstants.MATERIAL_STOCK_IN)
	public ModelAndView in(
			@ObjectParam("condition") StockEventCondition condition) {

		int in = StockLog.IN;

		return _entry(condition, in);
	}

	@RequestMapping
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	@ResponseBody
	@Barrier(PurviewConstants.MATERIAL_STOCK_IN)
	public YuiResult list4In(
			@ObjectParam("condition") StockEventCondition condition,
			@RequestParam(value = "pageIndex", required = false, defaultValue = "0") int pageIndex) {

		int in = StockLog.IN;
		return _list4All(condition, pageIndex, in);
	}

	@RequestMapping
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	@Barrier(PurviewConstants.MATERIAL_CONSUME)
	public ModelAndView out(
			@ObjectParam("condition") StockEventCondition condition) {
		return _entry(condition, StockLog.OUT);
	}

	@RequestMapping
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	@ResponseBody
	@Barrier(PurviewConstants.MATERIAL_CONSUME)
	public YuiResult list4Out(
			@ObjectParam("condition") StockEventCondition condition,
			@RequestParam(value = "pageIndex", required = false, defaultValue = "0") int pageIndex) {
		int in = StockLog.OUT;
		return _list4All(condition, pageIndex, in);
	}

	@RequestMapping
	@Transactional
	@Barrier(PurviewConstants.MATERIAL_STOCK_IN)
	public String _createIn(@ObjectParam("form") StockEventForm cli) {
		int flag = StockLog.IN;
		return _create4All(cli, flag);
	}

	@RequestMapping
	@Transactional()
	@Barrier(PurviewConstants.STOCK_LOG)
	public String _updateIn(@ObjectParam("form") StockEventForm cli) {
		int flag = StockLog.IN;

		return _update4All(cli, flag);
	}

	@RequestMapping
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	@ResponseBody
	@Barrier({ PurviewConstants.MATERIAL_CONSUME,
			PurviewConstants.MATERIAL_STOCK_IN })
	public JsonResult get(@RequestParam("id") Long id) {
		MapBean u = stockEventDao.unique(id);
		return JsonResult.s(u);
	}

	@RequestMapping
	@Transactional()
	@Barrier(PurviewConstants.MATERIAL_STOCK_IN)
	public String _removeIn(
			@RequestParam(value = "objId", required = false) Long[] id) {
		int flag = StockLog.IN;

		return _remove4All(id, flag);
	}

	@RequestMapping
	@Transactional
	@Barrier(PurviewConstants.MATERIAL_CONSUME)
	public String _createOut(@ObjectParam("form") StockEventForm cli) {
		int flag = StockLog.OUT;
		return _create4All(cli, flag);
	}

	@RequestMapping
	@Transactional()
	@Barrier(PurviewConstants.MATERIAL_CONSUME)
	public String _updateOut(@ObjectParam("form") StockEventForm cli) {
		int flag = StockLog.OUT;

		return _update4All(cli, flag);
	}

	@RequestMapping
	@Transactional()
	@Barrier(PurviewConstants.MATERIAL_CONSUME)
	public String _removeOut(
			@RequestParam(value = "objId", required = false) Long[] id) {
		int flag = StockLog.OUT;

		return _remove4All(id, flag);
	}

	// //////////////// private ////////////////////////////////////

	private ModelAndView _entry(StockEventCondition condition, int in) {
		if (condition == null) {
			condition = new StockEventCondition();
		}

		SelectOptionList typeList = StockEventHelper.getTargets(in);

		// 分区还是总部
		MapList branchList = null;

		if (sessionUserService.isHeadquarter()) {
			branchList = branchDao.loadMeta();
		}
		return new ModelAndView().addObject("condition", condition)
				.addObject("branchList", branchList)
				.addObject("typeList", typeList)
				.addObject("branch", sessionUserService.getBranch())
				.addObject("headquarter", sessionUserService.isHeadquarter());
	}

	private YuiResult _list4All(StockEventCondition condition, int pageIndex,
			int in) {
		if (condition == null) {
			condition = new StockEventCondition();
		}
		if (!sessionUserService.isHeadquarter()) {
			condition.setBranchId(sessionUserService.getBranch().getIdLong());
		}
		PaginationSupport<MapBean> results = null;
		condition.setType(in);
		results = stockEventDao.search(condition, pageIndex);

		return YuiResult.create(results, MAPPING_FIX);
	}

	private String _create4All(StockEventForm cli, int flag) {
		StockEvent cc = cli.toStockEvent();
		// check all target match the type ?

		if (!StockEventHelper.manual(cc.getTarget(), flag)) {
			Pe.raise("无效的类型");
		}
		Long mid = materialDao.getIdByNo(cli.getMaterialNo());
		if (mid == null) {
			Pe.raise("指定的材料编号不存在:" + cli.getMaterialNo());
		}

		cc.setMaterial(Material.create(mid));
		cc.setBranch(Branch.create(sessionUserService.getBranch().getIdLong()));

		cc.setCreator(SysUser.create(sessionUserService.getActorId()));

		StockEvent se = stockEventDao.save(cc);

		StockLog sl = stockLogDao.save(se);

		stockDao.update(sl);

		return ViewResult.JSON_SUCCESS;
	}

	private String _update4All(StockEventForm cli, int flag) {
		StockEvent cc = cli.toStockEvent();
		// cc.setUpdater(SysUser.create(sessionUserService.getActorId()));
		// check all target match the type ?

		if (!StockEventHelper.manual(cc.getTarget(), flag)) {
			Pe.raise("无效的类型");
		}

		Long mid = materialDao.getIdByNo(cli.getMaterialNo());
		if (mid == null) {
			Pe.raise("指定的材料编号不存在:" + cli.getMaterialNo());
		}

		cc.setMaterial(Material.create(mid));
		cc.setBranch(Branch.create(sessionUserService.getBranch().getIdLong()));

		Mix<StockEvent, StockEvent> mix1 = stockEventDao.update(cc);

		Mix<StockLog, StockLog> mix = stockLogDao.save(mix1);

		stockDao.update(mix.object1, mix.object2);

		return ViewResult.JSON_SUCCESS;
	}

	private String _remove4All(Long[] id, int flag) {
		// load before remove
		List<StockEvent> stockEvents = stockEventDao.load(id);

		// precheck
		for (StockEvent sl : stockEvents) {

			if (!StockEventHelper.manual(sl.getTarget(), flag)
					|| sl.getBranch().getId() != sessionUserService.getBranch()
							.getIdLong()) {
				Pe.raise("越权访问");
			}
		}

		stockEventDao.deleteById(id);

		// 日志
		List<StockLog> logs = generateRemoveLogs(stockEvents);

		stockLogDao.log(logs);

		// 更新库存
		stockDao.update(logs.toArray(new StockLog[logs.size()]));

		return ViewResult.JSON_SUCCESS;
	}

	// 生成删除日志
	private List<StockLog> generateRemoveLogs(List<StockEvent> stockEvents) {
		List<StockLog> results = WxlSugar.newArrayList();
		for (StockEvent se : stockEvents) {
			int type = StockLog.IN;
			if (StockEventHelper.manual(se.getTarget(), StockLog.IN)) {
				// 做反向操作
				type = StockLog.OUT;
				// 数量?

			}
			//
			StockLog sl = se.toStockLog();
			sl.setType(type);
			results.add(sl);
		}
		return results;

	}
}
