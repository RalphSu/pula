package pula.sys.app;

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

import puerta.support.AttachmentFile;
import puerta.support.PaginationSupport;
import puerta.support.annotation.Barrier;
import puerta.support.annotation.ObjectParam;
import puerta.system.helper.ParameterHelper;
import puerta.system.keeper.ParameterKeeper;
import puerta.system.vo.MapBean;
import puerta.system.vo.MapList;
import puerta.system.vo.YuiResult;
import puerta.system.vo.YuiResultMapper;
import pula.sys.PurviewConstants;
import pula.sys.conditions.StockCondition;
import pula.sys.daos.BranchDao;
import pula.sys.daos.MaterialDao;
import pula.sys.daos.StockDao;
import pula.sys.exports.StockExport;
import pula.sys.services.SessionUserService;

//Stock
@Controller
public class StockController {

	@SuppressWarnings("unused")
	private static final Logger log1ger = Logger
			.getLogger(StockController.class);
	private static final YuiResultMapper<MapBean> MAPPING_FIX = new YuiResultMapper<MapBean>() {
		@Override
		public Map<String, Object> toMap(MapBean obj) {

			return obj;
		}
	};

	@Resource
	MaterialDao materialDao;
	@Resource
	SessionUserService sessionUserService;
	@Resource
	StockDao stockDao;
	@Resource
	ParameterKeeper parameterKeeper;
	@Resource
	BranchDao branchDao;

	@RequestMapping
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	@Barrier(PurviewConstants.STOCK)
	public ModelAndView entry(@ObjectParam("condition") StockCondition condition) {
		if (condition == null) {
			condition = new StockCondition();
		}

		// 分区还是总部
		MapList branchList = null;

		if (sessionUserService.isHeadquarter()) {
			branchList = branchDao.loadMeta();
		}

		return new ModelAndView().addObject("condition", condition)
				.addObject("branchList", branchList)
				.addObject("branch", sessionUserService.getBranch())
				.addObject("headquarter", sessionUserService.isHeadquarter());
	}

	@RequestMapping
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	@ResponseBody
	@Barrier(PurviewConstants.STOCK)
	public YuiResult list(
			@ObjectParam("condition") StockCondition condition,
			@RequestParam(value = "pageIndex", required = false, defaultValue = "0") int pageIndex) {
		if (condition == null) {
			condition = new StockCondition();
		}

		if (!sessionUserService.isHeadquarter()) {
			condition.setBranchId(sessionUserService.getBranch().getIdLong());
		}
		PaginationSupport<MapBean> results = stockDao.searchMapBean(condition,
				pageIndex);

		return YuiResult.create(results, MAPPING_FIX);
	}

	@RequestMapping
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	@ResponseBody
	@Barrier(PurviewConstants.STOCK)
	public AttachmentFile export(
			@ObjectParam("condition") StockCondition condition) {
		if (condition == null) {
			condition = new StockCondition();
		}

		MapList results = stockDao.export(condition);

		String path = parameterKeeper.getFilePath(ParameterHelper.WORK_DIR);
		return StockExport.export(path, results);
	}

	// @RequestMapping
	// @Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	// @Barrier(PurviewConstants.STOCK_PREPAY)
	// public ModelAndView available(
	// @ObjectParam("condition") StockCondition condition,
	// @RequestParam(value = "pageIndex", required = false, defaultValue = "0")
	// int pageIndex) {
	//
	// boolean search = false;
	// if (condition == null) {
	//
	// condition = new StockCondition();
	// } else {
	// search = true;
	// }
	// PaginationSupport<MapBean> results = null;
	// if (search) {
	// results = stockDao.searchAvailable(condition, pageIndex);
	//
	// // 放入req
	// // 解拆
	//
	// // List<Long> materialIds = MapList.extract(results.getItems(),
	// // "materialId");
	//
	// Map<Long, MapBean> map = MapList.toMap(results.getItems(),
	// "materialId");
	//
	// // id形成List 一次加载出来
	// MapList tokenRequirements = tokenRequirementDao.listInMaterials(map
	// .keySet());
	//
	// for (MapBean mb : tokenRequirements) {
	// Long mid = mb.asLong("materialId");
	// if (map.containsKey(mid)) {
	// // 数量塞进去
	// MapBean res = map.get(mid);
	// int oldQuantity = res.integer("stillNeedQuantity");
	// long stockQuantity = res.asLong("quantity");
	// int stillNeed = oldQuantity
	// + mb.integer("stillNeedQuantity");
	// res.put("stillNeedQuantity", stillNeed);
	// // 最后核算一下库存和所需量的差距？
	// long left = stockQuantity - stillNeed;
	// res.put("availableQuantity", left);
	// }
	// }
	// }
	//
	// return new ModelAndView().addObject("condition", condition).addObject(
	// "results", results);
	//
	// }
}
