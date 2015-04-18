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
import puerta.support.vo.SelectOptionList;
import puerta.system.helper.ParameterHelper;
import puerta.system.keeper.ParameterKeeper;
import puerta.system.vo.MapBean;
import puerta.system.vo.MapList;
import puerta.system.vo.YuiResult;
import puerta.system.vo.YuiResultMapper;
import pula.sys.PurviewConstants;
import pula.sys.conditions.StockLogCondition;
import pula.sys.daos.BranchDao;
import pula.sys.daos.MaterialDao;
import pula.sys.daos.StockDao;
import pula.sys.daos.StockLogDao;
import pula.sys.exports.StockLogExport;
import pula.sys.helpers.StockLogHelper;
import pula.sys.services.SessionUserService;

//Stock
@Controller
public class StockLogController {

	@SuppressWarnings("unused")
	private static final Logger log1ger = Logger
			.getLogger(StockLogController.class);
	private static final YuiResultMapper<MapBean> MAPPING_FIX = new YuiResultMapper<MapBean>() {

		@Override
		public Map<String, Object> toMap(MapBean obj) {
			// TODO Auto-generated method stub
			return obj.add("typeName",
					StockLogHelper.getTypeName(obj.integer("type")));
		}

	};


	@RequestMapping
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	@Barrier(PurviewConstants.STOCK_LOG)
	public ModelAndView entry(
			@ObjectParam("condition") StockLogCondition condition) {
		if (condition == null) {
			condition = new StockLogCondition();
		}

		SelectOptionList typeList = StockLogHelper.getTypes(0);

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

	@RequestMapping
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	@ResponseBody
	@Barrier(PurviewConstants.STOCK_LOG)
	public YuiResult list(
			@ObjectParam("condition") StockLogCondition condition,
			@RequestParam(value = "pageIndex", required = false, defaultValue = "0") int pageIndex) {
		if (condition == null) {
			condition = new StockLogCondition();
		}
		if (!sessionUserService.isHeadquarter()) {
			condition.setBranchId(sessionUserService.getBranch().getIdLong());
		}
		PaginationSupport<MapBean> results = null;

		results = stockLogDao.search(condition, pageIndex);

		return YuiResult.create(results, MAPPING_FIX);
	}

	@RequestMapping
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	@ResponseBody
	@Barrier(PurviewConstants.STOCK_LOG)
	public AttachmentFile export(
			@ObjectParam("condition") StockLogCondition condition) {
		if (condition == null) {
			condition = new StockLogCondition();
		}
		if (!sessionUserService.isHeadquarter()) {
			condition.setBranchId(sessionUserService.getBranch().getIdLong());
		}
		MapList results = stockLogDao.export(condition);

		String path = parameterKeeper.getFilePath(ParameterHelper.WORK_DIR);
		return StockLogExport.export(path, results);
	}


}
