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
import pula.sys.conditions.GiftStockEventCondition;
import pula.sys.daos.BranchDao;
import pula.sys.daos.GiftDao;
import pula.sys.daos.GiftStockDao;
import pula.sys.daos.GiftStockEventDao;
import pula.sys.daos.GiftStockLogDao;
import pula.sys.domains.Branch;
import pula.sys.domains.Gift;
import pula.sys.domains.GiftStockEvent;
import pula.sys.domains.GiftStockLog;
import pula.sys.domains.SysUser;
import pula.sys.forms.GiftStockEventForm;
import pula.sys.helpers.GiftStockEventHelper;
import pula.sys.services.SessionUserService;

@Controller
public class GiftStockEventController {
	@SuppressWarnings("unused")
	private static final Logger log1ger = Logger
			.getLogger(GiftStockEventController.class);
	private static final YuiResultMapper<MapBean> MAPPING_FIX = new YuiResultMapper<MapBean>() {

		@Override
		public Map<String, Object> toMap(MapBean obj) {
			// TODO Auto-generated method stub
			return obj.add("targetName",
					GiftStockEventHelper.getTargetName(obj.integer("target")));
		}

	};

	@Resource
	GiftDao giftDao;
	@Resource
	BranchDao branchDao;
	@Resource
	SessionUserService sessionUserService;
	@Resource
	GiftStockLogDao giftStockLogDao;
	@Resource
	ParameterKeeper parameterKeeper;
	@Resource
	GiftStockDao giftStockDao;

	@Resource
	GiftStockEventDao giftStockEventDao;

	@RequestMapping
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	@Barrier(check = false, value = PurviewConstants.GIFT_STOCK_IN)
	public ModelAndView in(
			@ObjectParam("condition") GiftStockEventCondition condition) {

		int in = GiftStockLog.IN;

		return _entry(condition, in);
	}

	@RequestMapping
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	@ResponseBody
	@Barrier(check = false, value = PurviewConstants.GIFT_STOCK_IN)
	public YuiResult list4In(
			@ObjectParam("condition") GiftStockEventCondition condition,
			@RequestParam(value = "pageIndex", required = false, defaultValue = "0") int pageIndex) {

		int in = GiftStockLog.IN;
		return _list4All(condition, pageIndex, in);
	}

	@RequestMapping
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	@Barrier(check = false, value = PurviewConstants.GIFT_CONSUME)
	public ModelAndView out(
			@ObjectParam("condition") GiftStockEventCondition condition) {
		return _entry(condition, GiftStockLog.OUT);
	}

	@RequestMapping
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	@ResponseBody
	@Barrier(check = false, value = PurviewConstants.GIFT_CONSUME)
	public YuiResult list4Out(
			@ObjectParam("condition") GiftStockEventCondition condition,
			@RequestParam(value = "pageIndex", required = false, defaultValue = "0") int pageIndex) {
		int in = GiftStockLog.OUT;
		return _list4All(condition, pageIndex, in);
	}

	@RequestMapping
	@Transactional
	@Barrier(check = false, value = PurviewConstants.STOCK_LOG)
	public String _createIn(@ObjectParam("form") GiftStockEventForm cli) {
		int flag = GiftStockLog.IN;
		return _create4All(cli, flag);
	}

	@RequestMapping
	@Transactional()
	@Barrier(check = false, value = PurviewConstants.STOCK_LOG)
	public String _updateIn(@ObjectParam("form") GiftStockEventForm cli) {
		int flag = GiftStockLog.IN;

		return _update4All(cli, flag);
	}

	@RequestMapping
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	@ResponseBody
	@Barrier(check = false, value = { PurviewConstants.GIFT_CONSUME,
			PurviewConstants.GIFT_STOCK_IN })
	public JsonResult get(@RequestParam("id") Long id) {
		MapBean u = giftStockEventDao.unique(id);
		return JsonResult.s(u);
	}

	@RequestMapping
	@Transactional()
	@Barrier(check = false, value = PurviewConstants.GIFT_STOCK_IN)
	public String _removeIn(
			@RequestParam(value = "objId", required = false) Long[] id) {
		int flag = GiftStockLog.IN;

		return _remove4All(id, flag);
	}

	@RequestMapping
	@Transactional
	@Barrier(check = false, value = PurviewConstants.GIFT_CONSUME)
	public String _createOut(@ObjectParam("form") GiftStockEventForm cli) {
		int flag = GiftStockLog.OUT;
		return _create4All(cli, flag);
	}

	@RequestMapping
	@Transactional()
	@Barrier(check = false, value = PurviewConstants.GIFT_CONSUME)
	public String _updateOut(@ObjectParam("form") GiftStockEventForm cli) {
		int flag = GiftStockLog.OUT;

		return _update4All(cli, flag);
	}

	@RequestMapping
	@Transactional()
	@Barrier(check = false, value = PurviewConstants.GIFT_CONSUME)
	public String _removeOut(
			@RequestParam(value = "objId", required = false) Long[] id) {
		int flag = GiftStockLog.OUT;

		return _remove4All(id, flag);
	}

	// //////////////// private ////////////////////////////////////

	private ModelAndView _entry(GiftStockEventCondition condition, int in) {
		if (condition == null) {
			condition = new GiftStockEventCondition();
		}

		SelectOptionList typeList = GiftStockEventHelper.getTargets(in);

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

	private YuiResult _list4All(GiftStockEventCondition condition,
			int pageIndex, int in) {
		if (condition == null) {
			condition = new GiftStockEventCondition();
		}
		if (!sessionUserService.isHeadquarter()) {
			condition.setBranchId(sessionUserService.getBranch().getIdLong());
		}
		PaginationSupport<MapBean> results = null;
		condition.setType(in);
		results = giftStockEventDao.search(condition, pageIndex);

		return YuiResult.create(results, MAPPING_FIX);
	}

	private String _create4All(GiftStockEventForm cli, int flag) {
		GiftStockEvent cc = cli.toGiftStockEvent();
		// check all target match the type ?

		if (!GiftStockEventHelper.manual(cc.getTarget(), flag)) {
			Pe.raise("无效的类型");
		}
		Long mid = giftDao.getIdByNo(cli.getGiftNo());
		if (mid == null) {
			Pe.raise("指定的材料编号不存在:" + cli.getGiftNo());
		}

		cc.setGift(Gift.create(mid));
		cc.setBranch(Branch.create(sessionUserService.getBranch().getIdLong()));

		cc.setCreator(SysUser.create(sessionUserService.getActorId()));

		GiftStockEvent se = giftStockEventDao.save(cc);

		GiftStockLog sl = giftStockLogDao.save(se);

		giftStockDao.update(sl);

		return ViewResult.JSON_SUCCESS;
	}

	private String _update4All(GiftStockEventForm cli, int flag) {
		GiftStockEvent cc = cli.toGiftStockEvent();
		// cc.setUpdater(SysUser.create(sessionUserService.getActorId()));
		// check all target match the type ?

		if (!GiftStockEventHelper.manual(cc.getTarget(), flag)) {
			Pe.raise("无效的类型");
		}

		Long mid = giftDao.getIdByNo(cli.getGiftNo());
		if (mid == null) {
			Pe.raise("指定的材料编号不存在:" + cli.getGiftNo());
		}

		cc.setGift(Gift.create(mid));
		cc.setBranch(Branch.create(sessionUserService.getBranch().getIdLong()));

		Mix<GiftStockEvent, GiftStockEvent> mix1 = giftStockEventDao.update(cc);

		Mix<GiftStockLog, GiftStockLog> mix = giftStockLogDao.save(mix1);

		giftStockDao.update(mix.object1, mix.object2);

		return ViewResult.JSON_SUCCESS;
	}

	private String _remove4All(Long[] id, int flag) {
		// load before remove
		List<GiftStockEvent> stockEvents = giftStockEventDao.load(id);

		// precheck
		for (GiftStockEvent sl : stockEvents) {

			if (!GiftStockEventHelper.manual(sl.getTarget(), flag)
					|| sl.getBranch().getId() != sessionUserService.getBranch()
							.getIdLong()) {
				Pe.raise("越权访问");
			}
		}

		giftStockEventDao.deleteById(id);

		// 日志
		List<GiftStockLog> logs = generateRemoveLogs(stockEvents);

		giftStockLogDao.log(logs);

		// 更新库存
		giftStockDao.update(logs.toArray(new GiftStockLog[logs.size()]));

		return ViewResult.JSON_SUCCESS;
	}

	// 生成删除日志
	private List<GiftStockLog> generateRemoveLogs(
			List<GiftStockEvent> stockEvents) {
		List<GiftStockLog> results = WxlSugar.newArrayList();
		for (GiftStockEvent se : stockEvents) {
			int type = GiftStockLog.IN;
			if (GiftStockEventHelper.manual(se.getTarget(), GiftStockLog.IN)) {
				// 做反向操作
				type = GiftStockLog.OUT;
				// 数量?

			}
			//
			GiftStockLog sl = se.toGiftStockLog();
			sl.setType(type);
			results.add(sl);
		}
		return results;

	}
}
