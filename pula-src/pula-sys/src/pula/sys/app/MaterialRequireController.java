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
import pula.sys.conditions.MaterialRequireCondition;
import pula.sys.daos.BranchDao;
import pula.sys.daos.MaterialDao;
import pula.sys.daos.MaterialRequireDao;
import pula.sys.daos.StockDao;
import pula.sys.daos.StockLogDao;
import pula.sys.domains.Branch;
import pula.sys.domains.Material;
import pula.sys.domains.MaterialRequire;
import pula.sys.domains.StockLog;
import pula.sys.domains.SysUser;
import pula.sys.forms.MaterialRequireForm;
import pula.sys.helpers.MaterialRequireHelper;
import pula.sys.services.SessionUserService;

@Controller
public class MaterialRequireController {

	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger
			.getLogger(MaterialRequire.class);
	private static final YuiResultMapper<MapBean> MAPPING_FIX = new YuiResultMapper<MapBean>() {

		@Override
		public Map<String, Object> toMap(MapBean obj) {
			return obj.add("statusName", MaterialRequireHelper
					.getStatusName(obj.asInteger("status")));
		}
	};
	// private static final YuiResultMapper<MaterialRequire> MAPPING = new
	// YuiResultMapper<MaterialRequire>() {
	// @Override
	// public Map<String, Object> toMap(MaterialRequire obj) {
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
	// private static final YuiResultMapper<MaterialRequire> MAPPING_FULL = new
	// YuiResultMapper<MaterialRequire>() {
	// @Override
	// public Map<String, Object> toMap(MaterialRequire obj) {
	//
	// Map<String, Object> m = MAPPING.toMap(obj);
	//
	// m.put("branchId", obj.getBranch().getId());
	//
	// return m;
	// }
	// };

	@Resource
	MaterialRequireDao materialRequireDao;
	@Resource
	ParameterKeeper parameterKeeper;
	@Resource
	SessionUserService sessionUserService;
	@Resource
	BranchDao branchDao;
	@Resource
	MaterialDao materialDao;
	@Resource
	StockDao stockDao;
	@Resource
	StockLogDao stockLogDao;

	@RequestMapping
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	@Barrier(PurviewConstants.MATERIAL_REQUIRE)
	public ModelAndView entry(
			@ObjectParam("condition") MaterialRequireCondition condition) {
		if (condition == null) {
			condition = new MaterialRequireCondition();
		}

		SelectOptionList statusList = MaterialRequireHelper.getStatusList(0);
		MapList branchList = branchDao.loadMeta();

		return new ModelAndView().addObject("condition", condition)
				.addObject("branches", branchList)
				.addObject("statusList", statusList);
	}

	@RequestMapping
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	@ResponseBody
	@Barrier(PurviewConstants.MATERIAL_REQUIRE)
	public YuiResult list(
			@ObjectParam("condition") MaterialRequireCondition condition,
			@RequestParam(value = "pageIndex", required = false, defaultValue = "0") int pageIndex) {
		if (condition == null) {
			condition = new MaterialRequireCondition();
		}
		condition.setTarget(MaterialRequireCondition.TARGET_MY);
		PaginationSupport<MapBean> results = null;

		// 强制
		if (!sessionUserService.isHeadquarter()) {
			condition.setBranchId(sessionUserService.getBranch().getIdLong());
		}

		results = materialRequireDao.search(condition, pageIndex);
		return YuiResult.create(results, MAPPING_FIX);
	}

	@RequestMapping
	@Transactional
	@Barrier(PurviewConstants.MATERIAL_REQUIRE)
	public String _create(
			@ObjectParam("materialRequire") MaterialRequireForm cli) {

		MaterialRequire cc = cli.toMaterialRequire();

		Long mid = materialDao.getIdByNo(cli.getMaterialNo());
		if (mid == null) {
			Pe.raise("指定的材料编号不存在:" + cli.getMaterialNo());
		}

		cc.setMaterial(Material.create(mid));
		cc.setBranch(Branch.create(sessionUserService.getBranch().getIdLong()));

		cc.setCreator(SysUser.create(sessionUserService.getActorId()));
		materialRequireDao.save(cc);

		return ViewResult.JSON_SUCCESS;
	}

	@RequestMapping
	@Transactional()
	@Barrier(PurviewConstants.MATERIAL_REQUIRE)
	public String _update(
			@ObjectParam("materialRequire") MaterialRequireForm cli) {

		MaterialRequire cc = cli.toMaterialRequire();
		cc.setUpdater(SysUser.create(sessionUserService.getActorId()));

		Long mid = materialDao.getIdByNo(cli.getMaterialNo());
		if (mid == null) {
			Pe.raise("指定的材料编号不存在:" + cli.getMaterialNo());
		}

		cc.setMaterial(Material.create(mid));

		cc.setBranch(Branch.create(sessionUserService.getBranch().getIdLong()));

		materialRequireDao.update(cc);

		return ViewResult.JSON_SUCCESS;
	}

	@Transactional
	@RequestMapping
	@Barrier(PurviewConstants.MATERIAL_REQUIRE)
	public String remove(
			@RequestParam(value = "objId", required = false) Long[] id) {

		long branchId = sessionUserService.getBranch().getIdLong();
		materialRequireDao.precheckForRemove(id, branchId);

		materialRequireDao.deleteById(id);
		return ViewResult.JSON_SUCCESS;
	}

	@RequestMapping
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	@ResponseBody
	@Barrier()
	public JsonResult get(@RequestParam("id") Long id) {
		MapBean m = materialRequireDao.unique(id);

		return JsonResult.s(MAPPING_FIX.toMap(m));
	}

	@Transactional
	@RequestMapping
	@Barrier(PurviewConstants.MATERIAL_REQUIRE)
	public String _submit(
			@RequestParam(value = "objId", required = false) Long[] id) {

		// materialRequireDao.precheckForRemove(id,);

		long branchId = sessionUserService.getBranch().getIdLong();

		materialRequireDao.submit(id, branchId);
		return ViewResult.JSON_SUCCESS;
	}

	@RequestMapping
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	@Barrier(PurviewConstants.MATERIAL_REQUIRE)
	public ModelAndView apply(
			@ObjectParam("condition") MaterialRequireCondition condition) {
		if (condition == null) {
			condition = new MaterialRequireCondition();
		}

		SelectOptionList statusList = MaterialRequireHelper.getStatusList(0);
		MapList branchList = branchDao.loadMetaWithoutHeadquarter();

		return new ModelAndView().addObject("condition", condition)
				.addObject("branchList", branchList)
				.addObject("statusList", statusList);
	}

	@RequestMapping
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	@ResponseBody
	@Barrier(PurviewConstants.MATERIAL_REQUIRE)
	public YuiResult list4Apply(
			@ObjectParam("condition") MaterialRequireCondition condition,
			@RequestParam(value = "pageIndex", required = false, defaultValue = "0") int pageIndex) {
		if (condition == null) {
			condition = new MaterialRequireCondition();
		}
		condition.setStatus(MaterialRequire.STATUS_SUBMIT);
		condition.setTarget(MaterialRequireCondition.TARGET_AUDIT);
		PaginationSupport<MapBean> results = null;
		results = materialRequireDao.search(condition, pageIndex);
		return YuiResult.create(results, MAPPING_FIX);
	}

	@Transactional
	@RequestMapping
	@Barrier(PurviewConstants.MATERIAL_REQUIRE)
	public String _reject(
			@RequestParam(value = "objId", required = false) Long[] id,
			@RequestParam(value = "comments", required = false) String comments) {

		materialRequireDao.precheckForRejectOrApply(id);

		materialRequireDao
				.reject(id, sessionUserService.getActorId(), comments);
		return ViewResult.JSON_SUCCESS;
	}

	@Transactional
	@RequestMapping
	@Barrier(PurviewConstants.MATERIAL_REQUIRE)
	public String _apply(
			@RequestParam(value = "objId", required = false) Long[] id,
			@RequestParam(value = "comments", required = false) String comments,
			@RequestParam(value = "quantity", required = false) Integer qty) {

		materialRequireDao.precheckForRejectOrApply(id);
		materialRequireDao.apply(id, sessionUserService.getActorId(), comments,
				qty);
		return ViewResult.JSON_SUCCESS;
	}

	@RequestMapping
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	@Barrier(PurviewConstants.MATERIAL_REQUIRE)
	public ModelAndView send(
			@ObjectParam("condition") MaterialRequireCondition condition) {
		if (condition == null) {
			condition = new MaterialRequireCondition();
		}

		SelectOptionList statusList = MaterialRequireHelper.getStatusList(0);
		MapList branchList = branchDao.loadMetaWithoutHeadquarter();

		return new ModelAndView().addObject("condition", condition)
				.addObject("branchList", branchList)
				.addObject("statusList", statusList);
	}

	@RequestMapping
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	@ResponseBody
	@Barrier(PurviewConstants.MATERIAL_REQUIRE)
	public YuiResult list4Send(
			@ObjectParam("condition") MaterialRequireCondition condition,
			@RequestParam(value = "pageIndex", required = false, defaultValue = "0") int pageIndex) {
		if (condition == null) {
			condition = new MaterialRequireCondition();
		}
		condition.setStatus(MaterialRequire.STATUS_ACCEPT);
		condition.setTarget(MaterialRequireCondition.TARGET_AUDIT);
		PaginationSupport<MapBean> results = null;
		results = materialRequireDao.search(condition, pageIndex);
		return YuiResult.create(results, MAPPING_FIX);
	}

	@Transactional
	@RequestMapping
	@Barrier(PurviewConstants.MATERIAL_REQUIRE)
	public String _send(
			@RequestParam(value = "objId", required = false) Long[] id,
			@RequestParam(value = "no", required = false) String no) {

		List<MaterialRequire> mrs = materialRequireDao.precheckForSent(id);
		materialRequireDao.send(id, sessionUserService.getActorId(), no);

		// 把东西出库！
		// 从指定的库中
		long branchId = sessionUserService.getBranch().getIdLong();

		List<StockLog> sls = stockLogDao.saveSend(mrs, branchId);
		stockDao.update(sls.toArray(new StockLog[sls.size()]));

		return ViewResult.JSON_SUCCESS;
	}

	@RequestMapping
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	@Barrier(PurviewConstants.MATERIAL_REQUIRE)
	public ModelAndView receive(
			@ObjectParam("condition") MaterialRequireCondition condition) {
		if (condition == null) {
			condition = new MaterialRequireCondition();
		}

		SelectOptionList statusList = MaterialRequireHelper.getStatusList(0);
		MapList branchList = branchDao.loadMetaWithoutHeadquarter();

		return new ModelAndView().addObject("condition", condition)
				.addObject("branchList", branchList)
				.addObject("statusList", statusList);
	}

	@RequestMapping
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	@ResponseBody
	@Barrier(PurviewConstants.MATERIAL_REQUIRE)
	public YuiResult list4Receive(
			@ObjectParam("condition") MaterialRequireCondition condition,
			@RequestParam(value = "pageIndex", required = false, defaultValue = "0") int pageIndex) {
		if (condition == null) {
			condition = new MaterialRequireCondition();
		}
		condition.setStatus(MaterialRequire.STATUS_SENT);
		condition.setTarget(MaterialRequireCondition.TARGET_RECEIVE);

		// 强制
		if (!sessionUserService.isHeadquarter()) {
			condition.setBranchId(sessionUserService.getBranch().getIdLong());
		}

		PaginationSupport<MapBean> results = null;
		results = materialRequireDao.search(condition, pageIndex);
		return YuiResult.create(results, MAPPING_FIX);
	}

	@Transactional
	@RequestMapping
	@Barrier(PurviewConstants.MATERIAL_REQUIRE)
	public String _receive(
			@RequestParam(value = "objId", required = false) Long[] id,
			@RequestParam(value = "quantity", required = false) Integer qty) {

		long branchId = sessionUserService.getBranch().getIdLong();
		List<MaterialRequire> mrs = materialRequireDao.precheckForReceive(id,
				branchId);
		materialRequireDao.receive(id, sessionUserService.getActorId(), qty,
				branchId);

		// 库存处理 <入库了>
		// 先匹配一下数量吧！
		Map<Long, MaterialRequire> backup = WxlSugar.newHashMap();
		for (MaterialRequire mr : mrs) {
			backup.put(mr.getId(), mr.copy());
		}

		for (int i = 0; i < id.length; i++) {
			if (backup.containsKey(id[i])) {
				MaterialRequire mr = backup.get(id[i]);
				int q = mr.getSentQuantity();
				if (qty != null) {
					q = qty;
				}

				// 放到对象里
				mr.setArriveQuantity(q);
			}
		}

		logger.debug("backup.size=" + backup.values().size());

		List<StockLog> sls = stockLogDao.saveReceive(backup.values());
		stockDao.update(sls.toArray(new StockLog[sls.size()]));
		return ViewResult.JSON_SUCCESS;
	}
}
