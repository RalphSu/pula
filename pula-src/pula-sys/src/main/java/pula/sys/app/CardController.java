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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import puerta.PuertaWeb;
import puerta.support.PaginationSupport;
import puerta.support.Pe;
import puerta.support.ViewResult;
import puerta.support.annotation.Barrier;
import puerta.support.annotation.ObjectParam;
import puerta.support.vo.SelectOptionList;
import puerta.system.keeper.ParameterKeeper;
import puerta.system.service.SessionService;
import puerta.system.vo.JsonResult;
import puerta.system.vo.MapBean;
import puerta.system.vo.MapList;
import puerta.system.vo.YuiResult;
import puerta.system.vo.YuiResultMapper;
import pula.sys.PurviewConstants;
import pula.sys.conditions.CardCondition;
import pula.sys.daos.CardDao;
import pula.sys.domains.Card;
import pula.sys.helpers.CardHelper;

@Controller
public class CardController {

	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(CardController.class);

	private static final YuiResultMapper<MapBean> MAPPING_FIX = new YuiResultMapper<MapBean>() {

		@Override
		public Map<String, Object> toMap(MapBean obj) {
			// TODO Auto-generated method stub
			return obj.add("statusName",
					CardHelper.getStatusName(obj.integer("status")));
		}
	};
	@Resource
	CardDao cardDao;
	@Resource
	ParameterKeeper parameterKeeper;
	@Resource
	SessionService sessionService;

	// @Resource
	// DictLimbKeeper dictLimbKeeper;

	@RequestMapping
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	@Barrier(PurviewConstants.CARD)
	public ModelAndView entry(@ObjectParam("condition") CardCondition condition) {
		if (condition == null) {
			condition = new CardCondition();
		}

		SelectOptionList enabled_statusList = PuertaWeb.getYesNoList(0,
				new String[] { "有效", "无效" });
		SelectOptionList statusList = CardHelper.getStatusList(0);
		return new ModelAndView().addObject("condition", condition)
				.addObject("enabledStatusList", enabled_statusList)
				.addObject("statusList", statusList);
	}

	@RequestMapping
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	@ResponseBody
	@Barrier(PurviewConstants.CARD)
	public YuiResult list(
			@ObjectParam("condition") CardCondition condition,
			@RequestParam(value = "pageIndex", required = false, defaultValue = "0") int pageIndex) {
		if (condition == null) {
			condition = new CardCondition();
		}
		PaginationSupport<MapBean> results = null;
		results = cardDao.search(condition, pageIndex);
		return YuiResult.create(results, MAPPING_FIX);
	}

	@RequestMapping
	@Barrier(PurviewConstants.CARD)
	public String create() {
		return null;
	}

	/**
	 * 导入BOM
	 * 
	 * @param id
	 * @param file
	 * @return
	 */
	@RequestMapping
	@Transactional()
	@Barrier(PurviewConstants.CARD)
	public ModelAndView importXls(
			@RequestParam(value = "file", required = false) MultipartFile file) {

		if (file == null) {
			Pe.raise("无效的文件");
		}

		List<Card> afs = null;
		try {
			afs = CardHelper.read(file.getInputStream());

		} catch (Exception e) {

			e.printStackTrace();
			return Pe.raise("无效的文件:" + e.getMessage());
		}

		MapBean result = cardDao.importCards(afs);

		return new ModelAndView().addObject("result", result);

	}

	@RequestMapping
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	@ResponseBody
	@Barrier(PurviewConstants.CARD)
	public JsonResult get(@RequestParam("id") Long id) {
		MapBean u = cardDao.unique(id);
		return JsonResult.s(u);
	}

	@RequestMapping
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	@ResponseBody
	@Barrier(PurviewConstants.CARD)
	public JsonResult getByNo(@RequestParam("no") String no) {
		Card u = cardDao.findByNo(no);
		if (u == null) {
			Pe.raise("找不到指定的编号:" + no);
		}
		return JsonResult.s(u);
	}

	@RequestMapping
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	@Barrier(PurviewConstants.CARD)
	@ResponseBody
	public JsonResult find(
			@RequestParam(value = "no", required = false) String no) {
		// ModelAndView m = new ModelAndView(ViewResult.JSON_LIST);
		MapList list = cardDao.loadByKeywords(no);
		logger.debug("list.size=" + list.size());

		return JsonResult.s(list);
	}

	@Transactional
	@RequestMapping
	@Barrier(PurviewConstants.CARD)
	public String enable(
			@RequestParam(value = "objId", required = false) Long[] id,
			@RequestParam(value = "enable", required = false) boolean enable) {

		cardDao.doEnable(id, enable);
		return ViewResult.JSON_SUCCESS;
	}

	@Transactional
	@RequestMapping
	@Barrier(PurviewConstants.CARD)
	public String remove(
			@RequestParam(value = "objId", required = false) Long[] id) {
		cardDao.deleteById(id);
		return ViewResult.JSON_SUCCESS;
	}

}
