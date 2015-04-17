package pula.sys.app;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

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
import puerta.support.utils.DateHelper;
import puerta.support.vo.SelectOptionList;
import puerta.system.vo.JsonResult;
import puerta.system.vo.MapBean;
import puerta.system.vo.MapList;
import puerta.system.vo.YuiResult;
import puerta.system.vo.YuiResultMapper;
import pula.sys.BhzqConstants;
import pula.sys.PurviewConstants;
import pula.sys.conditions.ChargebackCondition;
import pula.sys.daos.BranchDao;
import pula.sys.daos.ChargebackDao;
import pula.sys.daos.CounterDao;
import pula.sys.daos.CourseProductDao;
import pula.sys.daos.FileAttachmentDao;
import pula.sys.daos.OrderFormDao;
import pula.sys.domains.Chargeback;
import pula.sys.domains.CourseProduct;
import pula.sys.domains.FileAttachment;
import pula.sys.domains.OrderForm;
import pula.sys.forms.ChargebackForm;
import pula.sys.helpers.ChargebackHelper;
import pula.sys.helpers.OrderFormHelper;
import pula.sys.helpers.StudentHelper;
import pula.sys.services.SessionUserService;

@Controller
public class ChargebackController {

	@Resource
	ChargebackDao chargebackDao;
	@Resource
	SessionUserService sessionUserService;
	@Resource
	private OrderFormDao orderFormDao;
	@Resource
	BranchDao branchDao;
	@Resource
	CounterDao counterDao;
	@Resource
	CourseProductDao courseProductDao;
	@Resource
	FileAttachmentDao fileAttachmentDao;

	private static final YuiResultMapper<MapBean> MAPPING_FIX = new YuiResultMapper<MapBean>() {

		@Override
		public Map<String, Object> toMap(MapBean obj) {
			// String ps = ChargebackHelper.getPayStatusName(obj
			// .asInteger("payStatus"));
			return obj

			.add("statusName",
					ChargebackHelper.getStatusName(obj.asInteger("status")))
					.add("commissionTypeName",
							OrderFormHelper.getCommissionTypeName(obj
									.asInteger("commissionType")));
		}
	};

	@RequestMapping
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	@Barrier(PurviewConstants.CHARGE_BACK)
	public ModelAndView entry(
			@ObjectParam("condition") ChargebackCondition condition) {
		return _entry(condition);

	}

	private ModelAndView _entry(ChargebackCondition condition) {
		if (condition == null) {
			condition = new ChargebackCondition();
			condition.setBeginDate(DateHelper.getThisMonthBegin());
			condition.setEndDate(DateHelper.getThisMonthEnd());
		}
		MapList branchList = branchDao.loadMetaWithoutHeadquarter();

		// SelectOptionList genders = BaseHelper.getGenderList(0);

		// SelectOptionList levelList = StudentHelper.getLevelList(0);

		// MapList branchList = branchDao.loadMeta();

		// SelectOptionList statusList = PuertaWeb.getYesNoList(PuertaWeb.YES,
		// new String[] { "有效", "无效" });

		// SelectOptionList payStatusList =
		// ChargebackHelper.getPayStatusList(0);
		SelectOptionList statusList = ChargebackHelper.getStatusList(0);
		List<CourseProduct> cps = courseProductDao
				.listByBranch(sessionUserService.getBranch().getIdLong());
		SelectOptionList commisionTypeList = OrderFormHelper
				.getCommissionTypes(0);

		return new ModelAndView()
				.addObject("condition", condition)
				// .addObject("payStatusList", payStatusList)
				.addObject("statusList", statusList)
				.addObject("commissionTypeList", commisionTypeList)
				.addObject("courseProducts", cps)
				.addObject("branches", branchList)
				.addObject("headquarter", sessionUserService.isHeadquarter());
	}

	@RequestMapping
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	@ResponseBody
	@Barrier(PurviewConstants.CHARGE_BACK)
	public YuiResult list(
			@ObjectParam("condition") ChargebackCondition condition,
			@RequestParam(value = "pageIndex", required = false, defaultValue = "0") int pageIndex) {
		if (condition == null) {
			condition = new ChargebackCondition();
		}

		PaginationSupport<MapBean> results = null;

		// 强制
		if (!sessionUserService.isHeadquarter()) {
			condition.setBranchId(sessionUserService.getBranch().getIdLong());
		}

		results = chargebackDao.search(condition, pageIndex);
		return YuiResult.create(results, MAPPING_FIX);
	}

	@RequestMapping
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	@Barrier(PurviewConstants.CHARGE_BACK_CONFIRM)
	public ModelAndView confirm(
			@ObjectParam("condition") ChargebackCondition condition) {
		return _entry(condition);

	}

	@RequestMapping
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	@ResponseBody
	@Barrier(PurviewConstants.CHARGE_BACK_CONFIRM)
	public YuiResult list4confirm(
			@ObjectParam("condition") ChargebackCondition condition,
			@RequestParam(value = "pageIndex", required = false, defaultValue = "0") int pageIndex) {
		if (condition == null) {
			condition = new ChargebackCondition();
		}

		PaginationSupport<MapBean> results = null;
		condition.setStatus(Chargeback.STATUS_INPUT);
		// condition.setPayStatus(Chargeback.PAY_STATUS_PAID);

		// 强制
		if (!sessionUserService.isHeadquarter()) {
			condition.setBranchId(sessionUserService.getBranch().getIdLong());
		}

		results = chargebackDao.search(condition, pageIndex);
		return YuiResult.create(results, MAPPING_FIX);
	}

	@RequestMapping
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	@Barrier(PurviewConstants.CHARGE_BACK_SEARCH)
	public ModelAndView search(
			@ObjectParam("condition") ChargebackCondition condition) {
		return _entry(condition);

	}

	@RequestMapping
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	@ResponseBody
	@Barrier(PurviewConstants.CHARGE_BACK_SEARCH)
	public YuiResult list4search(
			@ObjectParam("condition") ChargebackCondition condition,
			@RequestParam(value = "pageIndex", required = false, defaultValue = "0") int pageIndex) {
		if (condition == null) {
			condition = new ChargebackCondition();
		}

		PaginationSupport<MapBean> results = null;
		// condition.setStatus(Chargeback.STATUS_INPUT);
		// condition.setPayStatus(Chargeback.PAY_STATUS_PAID);

		// 强制
		if (!sessionUserService.isHeadquarter()) {
			condition.setBranchId(sessionUserService.getBranch().getIdLong());
		}

		results = chargebackDao.search(condition, pageIndex);
		return YuiResult.create(results, MAPPING_FIX);
	}

	@Transactional
	@RequestMapping
	@Barrier(PurviewConstants.CHARGE_BACK_CONFIRM)
	public String _confirm(
			@RequestParam(value = "objId", required = false) Long[] id) {

		// materialRequireDao.precheckForRemove(id,);

		long branchId = sessionUserService.getBranch().getIdLong();

		chargebackDao.confirm(id, branchId, sessionUserService.getActorId());
		return ViewResult.JSON_SUCCESS;
	}

	@Transactional
	@RequestMapping
	@Barrier(PurviewConstants.CHARGE_BACK)
	public String remove(
			@RequestParam(value = "objId", required = false) Long[] id) {

		long branchId = sessionUserService.getBranch().getIdLong();
		chargebackDao.precheckForRemove(id, branchId);

		chargebackDao.deleteById(id);
		return ViewResult.JSON_SUCCESS;
	}

	@RequestMapping
	@Transactional()
	@Barrier(PurviewConstants.CHARGE_BACK_CREATE)
	@ResponseBody
	public JsonResult _create(@ObjectParam("form") ChargebackForm form) {
		long branchId = sessionUserService.getBranch().getIdLong();

		Chargeback cc = form.toChargeback();

		prepare(cc, form);

		if (sessionUserService.isHeadquarter()) {
			Pe.raise("总部不能直接创建退单信息");
			// TODO: 测试
		} else {
			// 分支机构

		}

		String branchNo = branchDao.getPrefix(branchId);
		// 学生编号生成
		// 从自己的位置生成
		// 找到自己的计数器
		int counter = 1;
		synchronized (BhzqConstants.ORDER_FORM_SYNC) {
			counter = counterDao.inc(String.valueOf(branchId),
					BhzqConstants.COUNTER_ORDER_FORM);
		}

		cc.setNo("R" + StudentHelper.makeNo(branchNo, counter));
		cc.setCreator(sessionUserService.getUser());

		cc = chargebackDao.save(cc);

		return JsonResult.s();
	}

	private void prepare(Chargeback cc, ChargebackForm form) {
		if (chargebackDao.hasChargeback(form.getOrderFormId(), form.getId())) {
			Pe.raise("指定订单已经存在退单");
		}
		OrderForm of = null;
		if (form.getId() != 0) {
			if (!sessionUserService.isHeadquarter()) {
				long branch_id = sessionUserService.getBranch().getIdLong();
				chargebackDao.checkAllowEdit(cc.getId(), branch_id);
			}
			Chargeback cb = chargebackDao.findById(form.getId());
			of = cb.getOrderForm();
		} else {
			of = orderFormDao.findById(form.getOrderFormId());
		}

		if (of.isRemoved() || of.isCanceled()
				|| of.getStatus() != OrderForm.STATUS_CONFIRM) {
			Pe.raise("指定的订单不允许退单;可能是状态未确认或已完成;可能是越权访问");
		}
		if (of.getCourseCount() - of.getConsumeCourseCount() < cc
				.getBackCourses()) {
			Pe.raise("退课数超过剩余可退数量");
		}

	}

	@RequestMapping
	@Transactional()
	@Barrier(PurviewConstants.CHARGE_BACK_CREATE)
	@ResponseBody
	public JsonResult _update(@ObjectParam("form") ChargebackForm form) {
		// long branchId = sessionUserService.getBranch().getIdLong();

		Chargeback cc = form.toChargeback();

		prepare(cc, form);

		cc.setUpdater(sessionUserService.getUser());

		cc = chargebackDao.update(cc);

		return JsonResult.s();
	}

	@RequestMapping
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	@Barrier(PurviewConstants.CHARGE_BACK)
	public ModelAndView update(@RequestParam("id") Long id) {
		// long branchId = sessionUserService.getBranch().getIdLong();

		return load4show(id, false);

	}

	@RequestMapping
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	@Barrier(PurviewConstants.CHARGE_BACK)
	public ModelAndView view(@RequestParam("id") Long id) {
		return load4show(id, true);
	}

	private ModelAndView load4show(Long id, boolean view) {

		long branchId = sessionUserService.getBranch().getIdLong();
		if (sessionUserService.isHeadquarter()) {

		} else {
			// 分支机构
			if (view) {
				chargebackDao.checkAllowView(id, branchId);
			} else {
				chargebackDao.checkAllowEdit(id, branchId);
			}

		}

		Chargeback cb = chargebackDao.findById(id);
		OrderForm u = cb.getOrderForm();

		cb.getUpdater().getLoginId();

		if (u.getSlaveSalesman() != null) {
			u.getSlaveSalesman().getName();
		}
		u.getMasterSalesman().getName();
		u.getBranch().getName();
		u.getCourseProduct().getName();
		u.getStudent().getName();
		if (u.getTeacher() != null)
			u.getTeacher().getName();

		FileAttachment icon = fileAttachmentDao.getByRefId(StudentHelper
				.buildFileRefId(u.getStudent().getId(), u.getStudent()
						.getAttachmentKey()), FileAttachment.TYPE_STUDENT_ICON);

		return new ModelAndView().addObject("updateMode", true)
				.addObject("chargeback", cb).addObject("orderForm", u)
				.addObject("icon", icon).addObject("cbUpdateMode", true);
	}
}
