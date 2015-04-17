package pula.sys.app;

import java.util.Calendar;
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
import puerta.support.vo.SelectOptionList;
import puerta.system.keeper.ParameterKeeper;
import puerta.system.service.SessionService;
import puerta.system.vo.JsonResult;
import puerta.system.vo.MapBean;
import puerta.system.vo.MapList;
import puerta.system.vo.YuiResult;
import puerta.system.vo.YuiResultMapper;
import pula.sys.BaseHelper;
import pula.sys.PurviewConstants;
import pula.sys.conditions.SalesmanCondition;
import pula.sys.daos.BranchDao;
import pula.sys.daos.ChargebackDao;
import pula.sys.daos.OrderFormDao;
import pula.sys.daos.SalesmanDao;
import pula.sys.domains.Salesman;
import pula.sys.domains.SysUser;
import pula.sys.forms.SalesmanForm;
import pula.sys.helpers.SalesmanHelper;
import pula.sys.services.SessionUserService;
import pula.sys.services.SysUserService;

@Controller
public class SalesmanController {

	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(Salesman.class);
	private static final YuiResultMapper<Salesman> MAPPING = new YuiResultMapper<Salesman>() {
		@Override
		public Map<String, Object> toMap(Salesman obj) {

			Map<String, Object> m = WxlSugar.newHashMap();
			m.put("no", obj.getNo());
			m.put("id", obj.getId());
			m.put("name", obj.getName());
			m.put("branchName", obj.getBranch().getName());
			m.put("enabled", obj.isEnabled());
			m.put("genderName", obj.getGenderName());
			m.put("mobile", obj.getMobile());
			m.put("giftPoints", obj.getGiftPoints());

			return m;
		}
	};

	private static final YuiResultMapper<Salesman> MAPPING_FULL = new YuiResultMapper<Salesman>() {
		@Override
		public Map<String, Object> toMap(Salesman obj) {
			Map<String, Object> m = MAPPING.toMap(obj);
			m.put("branchId", obj.getBranch().getId());
			m.put("comments", obj.getComments());
			m.put("gender", obj.getGender());
			m.put("phone", obj.getPhone());
			m.put("branchNo", obj.getBranch().getNo());
			return m;
		}
	};

	@Resource
	SalesmanDao salesmanDao;
	@Resource
	ParameterKeeper parameterKeeper;
	@Resource
	SessionService sessionService;
	@Resource
	BranchDao branchDao;
	@Resource
	SessionUserService sessionUserService;
	@Resource
	SysUserService sysUserService;
	@Resource
	OrderFormDao orderFormDao;
	@Resource
	ChargebackDao chargebackDao;

	@RequestMapping
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	@Barrier(PurviewConstants.SALESMAN)
	public ModelAndView entry(
			@ObjectParam("condition") SalesmanCondition condition) {
		return _entry(condition);

	}

	@RequestMapping
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	@ResponseBody
	@Barrier(PurviewConstants.SALESMAN)
	public YuiResult list(
			@ObjectParam("condition") SalesmanCondition condition,
			@RequestParam(value = "pageIndex", required = false, defaultValue = "0") int pageIndex) {
		if (condition == null) {
			condition = new SalesmanCondition();

		}
		if (!sessionUserService.isHeadquarter()) {
			condition.setBranchId(sessionUserService.getBranch().getIdLong());

		}
		PaginationSupport<Salesman> results = null;
		results = salesmanDao.search(condition, pageIndex);
		return YuiResult.create(results, MAPPING);
	}

	@RequestMapping
	@Barrier(PurviewConstants.SALESMAN)
	public String create() {
		return null;
	}

	@RequestMapping
	@Transactional
	@Barrier(PurviewConstants.SALESMAN)
	public String _create(@ObjectParam("salesman") SalesmanForm cli) {

		Salesman cc = cli.toSalesman();

		cc.setCreator(SysUser.create(sessionService.getActorId()));
		cc = salesmanDao.save(cc);

		// 自动生成一个销售账号
		sysUserService.createSalesmen(cc);

		return ViewResult.JSON_SUCCESS;
	}

	@RequestMapping
	@Transactional()
	@Barrier(PurviewConstants.SALESMAN)
	public String _update(@ObjectParam("salesman") SalesmanForm cli) {

		Salesman cc = cli.toSalesman();
		cc.setUpdater(SysUser.create(sessionService.getActorId()));
		salesmanDao.update(cc);

		// 修改销售信息
		sysUserService.updateSalesmen(cc);

		return ViewResult.JSON_SUCCESS;
	}

	@Transactional
	@RequestMapping
	@Barrier(PurviewConstants.SALESMAN)
	public String remove(
			@RequestParam(value = "objId", required = false) Long[] id) {
		salesmanDao.deleteById(id);
		// 删除销售
		sysUserService.removeSalesmen(id);
		return ViewResult.JSON_SUCCESS;
	}

	@RequestMapping
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	@ResponseBody
	@Barrier()
	public JsonResult get(@RequestParam("id") Long id) {
		Salesman u = salesmanDao.findById(id);

		Map<String, Object> m = MAPPING_FULL.toMap(u);

		return JsonResult.s(m);
	}

	@RequestMapping
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	@ResponseBody
	@Barrier()
	public JsonResult getByNo(@RequestParam("no") String no) {
		Salesman u = salesmanDao.findByNo(no);
		if (u == null) {
			Pe.raise("找不到指定的编号:" + no);
		}
		return JsonResult.s(MAPPING_FULL.toMap(u));
	}

	@RequestMapping
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	@Barrier()
	public ModelAndView find(
			@RequestParam(value = "no", required = false) String no) {
		// ModelAndView m = new ModelAndView(ViewResult.JSON_LIST);
		// MapList list = salesmanDao.loadByKeywords(no, t, prefix);
		// logger.debug("list.size=" + list.size());
		// m.addObject("list", list);
		// return m;

		ModelAndView m = new ModelAndView(ViewResult.JSON_LIST);
		long branchId = 0;

		if (!sessionUserService.isHeadquarter()) {
			branchId = sessionUserService.getBranch().getIdLong();
		}
		MapList list = salesmanDao.loadByKeywords(no, branchId);
		m.addObject("list", list);

		return m;
	}

	@Transactional
	@RequestMapping
	@Barrier(PurviewConstants.SALESMAN)
	public String enable(
			@RequestParam(value = "objId", required = false) Long[] id,
			@RequestParam(value = "enable", required = false) boolean enable) {
		salesmanDao.doEnable(id, enable);

		// 删除销售
		sysUserService.enableSalesmen(id, enable);
		return ViewResult.JSON_SUCCESS;
	}

	@RequestMapping
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	@Barrier(PurviewConstants.SALESMAN)
	public ModelAndView search(
			@ObjectParam("condition") SalesmanCondition condition) {
		return _entry(condition);

	}

	private ModelAndView _entry(SalesmanCondition condition) {
		if (condition == null) {
			condition = new SalesmanCondition();
		}

		MapList branchList = branchDao.loadMeta();
		SelectOptionList genders = BaseHelper.getGenderList(0);

		return new ModelAndView().addObject("condition", condition)
				.addObject("branches", branchList)
				.addObject("genders", genders)
				.addObject("branch", sessionUserService.getBranch())
				.addObject("headquarter", sessionUserService.isHeadquarter());
	}

	@RequestMapping
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	@ResponseBody
	@Barrier(PurviewConstants.SALESMAN)
	public YuiResult list4Search(
			@ObjectParam("condition") SalesmanCondition condition,
			@RequestParam(value = "pageIndex", required = false, defaultValue = "0") int pageIndex) {
		if (condition == null) {
			condition = new SalesmanCondition();

		}
		if (!sessionUserService.isHeadquarter()) {
			condition.setBranchId(sessionUserService.getBranch().getIdLong());
		}
		condition.setEnabledStatus(PuertaWeb.YES);
		PaginationSupport<Salesman> results = null;
		results = salesmanDao.search(condition, pageIndex);
		return YuiResult.create(results, MAPPING);
	}

	@RequestMapping
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	@Barrier(PurviewConstants.SALESMAN_PERFORMANCE)
	public ModelAndView performance(
			@RequestParam(value = "year", required = false, defaultValue = "0") int year,
			@RequestParam(value = "month", required = false, defaultValue = "0") int month,
			@RequestParam(value = "branchId", required = false, defaultValue = "0") long branchId

	) {

		MapList branchList = branchDao.loadMetaWithoutHeadquarter();

		Calendar cal = Calendar.getInstance();

		int thisYear = cal.get(Calendar.YEAR);
		int thisMonth = cal.get(Calendar.MONTH) + 1;
		int java_month = 0;
		if (year == 0) {
			year = thisYear;
			month = thisMonth;

		} else {
		}
		java_month = month - 1;
		MapList salesmanList = null;
		Map<String, MapBean> table = null;
		if (branchId == 0 && sessionUserService.isHeadquarter()) {

		} else {

			if (!sessionUserService.isHeadquarter()) {
				branchId = sessionUserService.getBranch().getIdLong();
			}

			// start!

			// 加载所有的销售!
			// MapList salesmanList = salesmanDao.mapList();

			// 加载当月确认的订单和退单
			Map<String, MapBean> orders = orderFormDao.stat4Salesman(year,
					java_month, branchId);

			Map<String, MapBean> chargebacks = chargebackDao.stat4Salesman(
					year, java_month, branchId);

			// 形成最终salesman list
			// 销售人员数据完全从订单和退单中抽取
			salesmanList = SalesmanHelper.collect(orders, chargebacks);
			table = SalesmanHelper.merge(orders, chargebacks);

			logger.debug("size=" + table.size());
		}

		// results

		return new ModelAndView().addObject("year", year)
				.addObject("salesmanList", salesmanList)
				.addObject("table", table).addObject("thisYear", thisYear)
				.addObject("thisMonth", thisMonth).addObject("month", month)
				.addObject("branches", branchList)
				.addObject("branch", sessionUserService.getBranch())
				.addObject("branchId", branchId)
				.addObject("headquarter", sessionUserService.isHeadquarter());
	}
	
	
	
	
}
