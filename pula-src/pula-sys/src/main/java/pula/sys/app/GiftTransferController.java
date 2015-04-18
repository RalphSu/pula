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
import puerta.support.vo.SelectOptionList;
import puerta.system.keeper.ParameterKeeper;
import puerta.system.vo.JsonResult;
import puerta.system.vo.MapBean;
import puerta.system.vo.MapList;
import puerta.system.vo.YuiResult;
import puerta.system.vo.YuiResultMapper;
import pula.sys.PurviewConstants;
import pula.sys.conditions.GiftTransferCondition;
import pula.sys.daos.BranchDao;
import pula.sys.daos.GiftDao;
import pula.sys.daos.GiftStockDao;
import pula.sys.daos.GiftStockLogDao;
import pula.sys.daos.GiftTransferDao;
import pula.sys.domains.Branch;
import pula.sys.domains.Gift;
import pula.sys.domains.GiftStockLog;
import pula.sys.domains.GiftTransfer;
import pula.sys.domains.SysUser;
import pula.sys.forms.GiftTransferForm;
import pula.sys.helpers.GiftTransferHelper;
import pula.sys.services.SessionUserService;

@Controller
public class GiftTransferController {

	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(GiftTransfer.class);
	private static final YuiResultMapper<MapBean> MAPPING_FIX = new YuiResultMapper<MapBean>() {

		@Override
		public Map<String, Object> toMap(MapBean obj) {
			return obj.add("statusName",
					GiftTransferHelper.getStatusName(obj.asInteger("status")));
		}
	};
	// private static final YuiResultMapper<GiftTransfer> MAPPING = new
	// YuiResultMapper<GiftTransfer>() {
	// @Override
	// public Map<String, Object> toMap(GiftTransfer obj) {
	//
	// Map<String, Object> m = WxlSugar.newHashMap();
	// m.put("no", obj.getNo());
	// m.put("id", obj.getId());
	// m.put("name", obj.getName());
	// m.put("branchName", obj.getBranch().getName());
	// m.put("enabled", obj.isEnabled());
	//
	// return m;
	// }
	// };
	//
	// private static final YuiResultMapper<GiftTransfer> MAPPING_FULL = new
	// YuiResultMapper<GiftTransfer>() {
	// @Override
	// public Map<String, Object> toMap(GiftTransfer obj) {
	//
	// Map<String, Object> m = MAPPING.toMap(obj);
	//
	// m.put("branchId", obj.getBranch().getId());
	//
	// return m;
	// }
	// };

	@Resource
	GiftTransferDao giftTransferDao;
	@Resource
	ParameterKeeper parameterKeeper;
	@Resource
	SessionUserService sessionUserService;
	@Resource
	BranchDao branchDao;
	@Resource
	GiftDao giftDao;
	@Resource
	GiftStockDao giftStockDao;
	@Resource
	GiftStockLogDao giftStockLogDao;

	@RequestMapping
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	@Barrier(PurviewConstants.GIFT_SENT)
	public ModelAndView entry(
			@ObjectParam("condition") GiftTransferCondition condition) {
		if (condition == null) {
			condition = new GiftTransferCondition();
		}

		SelectOptionList statusList = GiftTransferHelper.getStatusList(0);
		MapList branchList = branchDao.loadMeta();

		return new ModelAndView().addObject("condition", condition)
				.addObject("branchList", branchList)
				.addObject("statusList", statusList)
				.addObject("branch", sessionUserService.getBranch());
	}

	@RequestMapping
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	@ResponseBody
	@Barrier(PurviewConstants.GIFT_SENT)
	public YuiResult list(
			@ObjectParam("condition") GiftTransferCondition condition,
			@RequestParam(value = "pageIndex", required = false, defaultValue = "0") int pageIndex) {
		if (condition == null) {
			condition = new GiftTransferCondition();
		}
		condition.setTarget(GiftTransferCondition.TARGET_MY);
		PaginationSupport<MapBean> results = null;

		// 强制
		if (!sessionUserService.isHeadquarter()) {
			condition.setBranchId(sessionUserService.getBranch().getIdLong());
		}

		results = giftTransferDao.search(condition, pageIndex);
		return YuiResult.create(results, MAPPING_FIX);
	}

	@RequestMapping
	@Transactional
	@Barrier(PurviewConstants.GIFT_SENT)
	public String _create(@ObjectParam("giftTransfer") GiftTransferForm cli) {

		long branchId = sessionUserService.getBranch().getIdLong();

		if (branchId == cli.getToBranchId()) {
			Pe.raise("目标分支机构不能为当前分支机构");
		}

		GiftTransfer cc = cli.toGiftTransfer();

		Long mid = giftDao.getIdByNo(cli.getGiftNo());
		if (mid == null) {
			Pe.raise("指定的材料编号不存在:" + cli.getGiftNo());
		}

		cc.setGift(Gift.create(mid));
		cc.setBranch(Branch.create(branchId));

		cc.setCreator(SysUser.create(sessionUserService.getActorId()));
		giftTransferDao.save(cc);

		return ViewResult.JSON_SUCCESS;
	}

	@RequestMapping
	@Transactional()
	@Barrier(PurviewConstants.GIFT_SENT)
	public String _update(@ObjectParam("giftTransfer") GiftTransferForm cli) {

		long branchId = sessionUserService.getBranch().getIdLong();

		if (branchId == cli.getToBranchId()) {
			Pe.raise("目标分支机构不能为当前分支机构");
		}

		GiftTransfer cc = cli.toGiftTransfer();
		cc.setUpdater(SysUser.create(sessionUserService.getActorId()));

		Long mid = giftDao.getIdByNo(cli.getGiftNo());
		if (mid == null) {
			Pe.raise("指定的材料编号不存在:" + cli.getGiftNo());
		}

		cc.setGift(Gift.create(mid));

		cc.setBranch(Branch.create(branchId));

		giftTransferDao.update(cc);

		return ViewResult.JSON_SUCCESS;
	}

	@Transactional
	@RequestMapping
	@Barrier(PurviewConstants.GIFT_SENT)
	public String remove(
			@RequestParam(value = "objId", required = false) Long[] id) {

		long branchId = sessionUserService.getBranch().getIdLong();
		giftTransferDao.precheckForRemove(id, branchId);

		giftTransferDao.deleteById(id);
		return ViewResult.JSON_SUCCESS;
	}

	@RequestMapping
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	@ResponseBody
	@Barrier()
	public JsonResult get(@RequestParam("id") Long id) {
		MapBean m = giftTransferDao.unique(id);

		return JsonResult.s(MAPPING_FIX.toMap(m));
	}

	@Transactional
	@RequestMapping
	@Barrier(PurviewConstants.GIFT_SENT)
	public String _submit(
			@RequestParam(value = "objId", required = false) Long[] id) {

		// giftTransferDao.precheckForRemove(id,);

		long branchId = sessionUserService.getBranch().getIdLong();

		giftTransferDao.submit(id, branchId);
		return ViewResult.JSON_SUCCESS;
	}

	@RequestMapping
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	@Barrier(PurviewConstants.GIFT_TRANSFER_SENT_CONFIRM)
	public ModelAndView apply(
			@ObjectParam("condition") GiftTransferCondition condition) {
		if (condition == null) {
			condition = new GiftTransferCondition();
		}

		SelectOptionList statusList = GiftTransferHelper.getStatusList(0);
		MapList branchList = branchDao.loadMetaWithoutHeadquarter();

		return new ModelAndView().addObject("condition", condition)
				.addObject("branchList", branchList)
				.addObject("statusList", statusList);
	}

	@RequestMapping
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	@ResponseBody
	@Barrier(PurviewConstants.GIFT_TRANSFER_SENT_CONFIRM)
	public YuiResult list4Apply(
			@ObjectParam("condition") GiftTransferCondition condition,
			@RequestParam(value = "pageIndex", required = false, defaultValue = "0") int pageIndex) {
		if (condition == null) {
			condition = new GiftTransferCondition();
		}
		condition.setStatus(GiftTransfer.STATUS_SUBMIT);
		condition.setTarget(GiftTransferCondition.TARGET_AUDIT);
		PaginationSupport<MapBean> results = null;
		results = giftTransferDao.search(condition, pageIndex);
		return YuiResult.create(results, MAPPING_FIX);
	}

	@Transactional
	@RequestMapping
	@Barrier(PurviewConstants.GIFT_TRANSFER_SENT_CONFIRM)
	public String _reject(
			@RequestParam(value = "objId", required = false) Long[] id,
			@RequestParam(value = "comments", required = false) String comments) {

		giftTransferDao.precheckForRejectOrApply(id);

		giftTransferDao.reject(id, sessionUserService.getActorId(), comments);
		return ViewResult.JSON_SUCCESS;
	}

	@Transactional
	@RequestMapping
	@Barrier(PurviewConstants.GIFT_TRANSFER_SENT_CONFIRM)
	public String _apply(
			@RequestParam(value = "objId", required = false) Long[] id,
			@RequestParam(value = "comments", required = false) String comments,
			@RequestParam(value = "quantity", required = false) Integer qty) {

		giftTransferDao.precheckForRejectOrApply(id);
		giftTransferDao.apply(id, sessionUserService.getActorId(), comments,
				qty);
		return ViewResult.JSON_SUCCESS;
	}

	@RequestMapping
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	@Barrier(PurviewConstants.GIFT_TRANSFER_SENT_CONFIRM)
	public ModelAndView send(
			@ObjectParam("condition") GiftTransferCondition condition) {
		if (condition == null) {
			condition = new GiftTransferCondition();
		}

		SelectOptionList statusList = GiftTransferHelper.getStatusList(0);
		MapList branchList = branchDao.loadMetaWithoutHeadquarter();

		return new ModelAndView().addObject("condition", condition)
				.addObject("branchList", branchList)
				.addObject("statusList", statusList);
	}

	@RequestMapping
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	@ResponseBody
	@Barrier(PurviewConstants.GIFT_TRANSFER_SENT_CONFIRM)
	public YuiResult list4Send(
			@ObjectParam("condition") GiftTransferCondition condition,
			@RequestParam(value = "pageIndex", required = false, defaultValue = "0") int pageIndex) {
		if (condition == null) {
			condition = new GiftTransferCondition();
		}
		condition.setStatus(GiftTransfer.STATUS_SUBMIT);
		condition.setTarget(GiftTransferCondition.TARGET_AUDIT);
		PaginationSupport<MapBean> results = null;
		results = giftTransferDao.search(condition, pageIndex);
		return YuiResult.create(results, MAPPING_FIX);
	}

	@Transactional
	@RequestMapping
	@Barrier(PurviewConstants.GIFT_TRANSFER_SENT_CONFIRM)
	public String _send(
			@RequestParam(value = "objId", required = false) Long[] id,
			@RequestParam(value = "no", required = false) String no) {

		List<GiftTransfer> mrs = giftTransferDao.precheckForSent(id);
		giftTransferDao.send(id, sessionUserService.getActorId(), no);

		// 把东西出库！
		// 从指定的库中
		long branchId = sessionUserService.getBranch().getIdLong();

		List<GiftStockLog> sls = giftStockLogDao.saveSend(mrs, branchId);
		giftStockDao.update(sls.toArray(new GiftStockLog[sls.size()]));

		return ViewResult.JSON_SUCCESS;
	}

	@RequestMapping
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	@Barrier(PurviewConstants.GIFT_ARRIVED)
	public ModelAndView receive(
			@ObjectParam("condition") GiftTransferCondition condition) {
		if (condition == null) {
			condition = new GiftTransferCondition();
		}

		SelectOptionList statusList = GiftTransferHelper.getStatusList(0);
		MapList branchList = branchDao.loadMetaWithoutHeadquarter();

		return new ModelAndView().addObject("condition", condition)
				.addObject("branchList", branchList)
				.addObject("statusList", statusList);
	}

	@RequestMapping
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	@ResponseBody
	@Barrier(PurviewConstants.GIFT_ARRIVED)
	public YuiResult list4Receive(
			@ObjectParam("condition") GiftTransferCondition condition,
			@RequestParam(value = "pageIndex", required = false, defaultValue = "0") int pageIndex) {
		if (condition == null) {
			condition = new GiftTransferCondition();
		}
		condition.setStatus(GiftTransfer.STATUS_SENT);
		condition.setTarget(GiftTransferCondition.TARGET_RECEIVE);

		// 强制
		if (!sessionUserService.isHeadquarter()) {
			condition.setToBranchId(sessionUserService.getBranch().getIdLong());
		}

		PaginationSupport<MapBean> results = null;
		results = giftTransferDao.search(condition, pageIndex);
		return YuiResult.create(results, MAPPING_FIX);
	}

	@Transactional
	@RequestMapping
	@Barrier(PurviewConstants.GIFT_ARRIVED)
	public String _receive(
			@RequestParam(value = "objId", required = false) Long[] id,
			@RequestParam(value = "quantity", required = false) Integer qty) {

		long branchId = sessionUserService.getBranch().getIdLong();
		List<GiftTransfer> mrs = giftTransferDao.precheckForReceive(id,
				branchId);
		giftTransferDao.receive(id, sessionUserService.getActorId(), qty,
				branchId);

		// 库存处理 <入库了>
		// 先匹配一下数量吧！
		Map<Long, GiftTransfer> backup = WxlSugar.newHashMap();
		for (GiftTransfer mr : mrs) {
			backup.put(mr.getId(), mr.copy());
		}

		for (int i = 0; i < id.length; i++) {
			if (backup.containsKey(id[i])) {
				GiftTransfer mr = backup.get(id[i]);
				int q = mr.getSentQuantity();
				if (qty != null) {
					q = qty;
				}

				// 放到对象里
				mr.setArriveQuantity(q);
			}
		}

		logger.debug("backup.size=" + backup.values().size());

		List<GiftStockLog> sls = giftStockLogDao.saveReceive(backup.values(),
				sessionUserService.getBranch().getIdLong());
		giftStockDao.update(sls.toArray(new GiftStockLog[sls.size()]));
		return ViewResult.JSON_SUCCESS;
	}
}
