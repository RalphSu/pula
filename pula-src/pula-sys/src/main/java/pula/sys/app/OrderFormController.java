package pula.sys.app;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
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
import puerta.support.utils.DateExTool;
import puerta.support.utils.DateHelper;
import puerta.support.vo.Mix;
import puerta.support.vo.SelectOption;
import puerta.support.vo.SelectOptionList;
import puerta.system.vo.JsonResult;
import puerta.system.vo.MapBean;
import puerta.system.vo.MapList;
import puerta.system.vo.YuiResult;
import puerta.system.vo.YuiResultMapper;
import pula.sys.BaseHelper;
import pula.sys.BhzqConstants;
import pula.sys.PurviewConstants;
import pula.sys.conditions.OrderFormCondition;
import pula.sys.daos.BranchDao;
import pula.sys.daos.ChargebackDao;
import pula.sys.daos.CounterDao;
import pula.sys.daos.CourseProductDao;
import pula.sys.daos.FileAttachmentDao;
import pula.sys.daos.OrderFormDao;
import pula.sys.daos.SalesmanDao;
import pula.sys.daos.StudentDao;
import pula.sys.daos.TeacherDao;
import pula.sys.domains.Branch;
import pula.sys.domains.CourseProduct;
import pula.sys.domains.FileAttachment;
import pula.sys.domains.OrderForm;
import pula.sys.domains.Salesman;
import pula.sys.domains.Student;
import pula.sys.domains.StudentPoints;
import pula.sys.domains.Teacher;
import pula.sys.forms.OrderFormForm;
import pula.sys.helpers.OrderFormHelper;
import pula.sys.helpers.StudentHelper;
import pula.sys.services.SessionUserService;
import pula.sys.services.StudentPointsService;

@Controller
public class OrderFormController {

	private static final Logger logger = Logger
			.getLogger(OrderFormController.class);

	private static final YuiResultMapper<MapBean> MAPPING_FIX = new YuiResultMapper<MapBean>() {

		@Override
		public Map<String, Object> toMap(MapBean obj) {
			String ps = OrderFormHelper.getPayStatusName(obj
					.asInteger("payStatus"));
			return obj
					.add("payStatusName", ps)
					.add("statusName",
							OrderFormHelper.getStatusName(obj
									.asInteger("status")))
					.add("commissionTypeName",
							OrderFormHelper.getCommissionTypeName(obj
									.asInteger("commissionType")));
		}
	};

	@Resource
	private OrderFormDao orderFormDao;
	@Resource
	private StudentDao studentDao;
	@Resource
	private SalesmanDao salesmanDao;
	@Resource
	private TeacherDao teacherDao;
	@Resource
	private CourseProductDao courseProductDao;
	@Resource
	SessionUserService sessionUserService;
	@Resource
	CounterDao counterDao;
	@Resource
	BranchDao branchDao;
	@Resource
	StudentPointsService studentPointsService;
	@Resource
	FileAttachmentDao fileAttachmentDao;
	@Resource
	ChargebackDao chargebackDao;

	@RequestMapping
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	@Barrier(PurviewConstants.ORDERFORM_CREATE)
	public ModelAndView create() {
		long branchId = sessionUserService.getBranch().getIdLong();
		List<CourseProduct> courseProductList = courseProductDao
				.listByBranch(branchId);

		// 学生界面中的性别

		SelectOptionList genders = BaseHelper.getGenderList(0);
		SelectOptionList payStatusList = OrderFormHelper.getPayStatusList(0);
		SelectOptionList commisionTypeList = OrderFormHelper
				.getCommissionTypes(0);

		return new ModelAndView().addObject("genders", genders)
				.addObject("productList", courseProductList)
				.addObject("payStatusList", payStatusList)
				.addObject("commisionTypeList", commisionTypeList)
				.addObject("updateMode", false);
	}

	@RequestMapping
	@Transactional()
	@Barrier(PurviewConstants.ORDERFORM_CREATE)
	@ResponseBody
	public JsonResult _create(@ObjectParam("form") OrderFormForm form) {
		long branchId = sessionUserService.getBranch().getIdLong();

		OrderForm cc = form.toOrderForm();

		prepare(cc, form);

		if (sessionUserService.isHeadquarter()) {
			// Pe.raise("总部不能直接创建订单信息");
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

		cc.setNo(StudentHelper.makeNo(branchNo, counter));
		cc.setBranch(Branch.create(branchId));
		cc.setCreator(sessionUserService.getUser());

		cc = orderFormDao.save(cc);

		studentPointsService.save(StudentPoints.create(cc.getStudent(),
				StudentPoints.FROM_ORDER_FORM, cc.getPoints(),
				"新增订单:" + cc.getNo(), OrderFormHelper.buildRefId(cc.getId())));

		return JsonResult.s();
	}

	private void prepare(OrderForm cc, OrderFormForm form) {

		// 学生是否合理
		long branch_id = sessionUserService.getBranch().getIdLong();
		Long s = studentDao.getIdByNoWithBranchId(form.getStudentNo(),
				branch_id);

		if (s == null) {
			Pe.raise("指定的学生编号无法找到，或不属于当前分校:" + form.getStudentNo());
		}
		String no = orderFormDao.getOpenOrderForm(s, form.getId());
		if (no != null) {
			Pe.raise("该学员已签署了订单[" + no + "],其中课程尚未完成");
		}

		cc.setStudent(Student.create(s));

		// 产品是否合理
		MapBean cp = courseProductDao.meta4order(form.getCourseProductId(),
				branch_id);

		if (cp == null) {
			Pe.raise("指定的产品不属于当前分校或产品已禁用");
		}

		if (form.getPayStatus() == OrderForm.PAY_STATUS_PREPAY) {
			// 预付超过总价
			if (form.getPrepay() >= cp.asDouble("price")) {
				Pe.raise("预付款已超出或等于总价");
			}
		}

		if (!sessionUserService.isHeadquarter()) {
			//
			Calendar beginTime = cp.calendar("beginTime");
			Calendar endTime = cp.calendar("endTime");
			Calendar now = Calendar.getInstance();
			boolean notthere = false;
			String text = "";
			if (beginTime != null && beginTime.after(now)) {
				// now = '2013-11-12 10:10';
				// beginTime = 2013-11-12
				// 还没启用
				notthere = true;
				text = "尚未开始:" + DateExTool.getText(beginTime);
			} else if (endTime != null
					&& !now.before(DateExTool.tomorrow(endTime))) {
				// now = '2013-11-12 10:10'
				// endTime = 2013-11-12' --> 2013-11-13
				notthere = true;
				text = "已经结束:" + DateExTool.getText(endTime);
			}

			if (notthere) {
				// 提醒
				Pe.raise("指定的课程产品不在有效期间," + text);
			}
		}

		cc.setTotalAmount(cp.asDouble("price"));
		cc.setCourseCount(cp.asInteger("courseCount"));
		cc.setCourseProduct(CourseProduct.create(form.getCourseProductId()));

		// 销售两名是否合理（是否本分校）
		MapBean ms = salesmanDao.meta4order(form.getMasterSalesmanNo(),
				branch_id);
		if (ms == null) {
			Pe.raise("需填写主销售人员编号；或该销售人员不属于本分部");
		}

		// 分数是否超限制
		int sales_points_limit = ms.asInteger("giftPoints");
		if (sales_points_limit < form.getPoints()) {
			Pe.raise("超过主销售可赠分上限:" + sales_points_limit);
		}

		cc.setMasterSalesman(Salesman.create(ms.asLong("id")));

		if (StringUtils.isEmpty(form.getSlaveSalesmanNo())) {

			// 如果不是100的单子，应该要填写辅助销售
			if (form.getCommissionType() != OrderForm.COMMISSION_TYPE_100) {
				Pe.raise("非个人的佣金类型需填写辅助销售人员编号");
			}
		} else {
			ms = salesmanDao.meta4order(form.getSlaveSalesmanNo(), branch_id);
			if (ms == null) {
				Pe.raise("请正确填写辅销售人员编号或留空；或该销售人员不属于本分部");
			}
			cc.setSlaveSalesman(Salesman.create(ms.asLong("id")));

			if (cc.getMasterSalesman().getId() == cc.getSlaveSalesman().getId()) {
				Pe.raise("主、辅销售人员不能为同一人");
			}
		}

		// 教师是否合理
		if (!StringUtils.isEmpty(form.getTeacherNo())) {
			Long t = teacherDao.getIdByNoWithBranchId(form.getTeacherNo(),
					branch_id);

			if (t == null) {
				Pe.raise("指定的教师编号无法找到，或不属于当前分校:" + form.getTeacherNo());
			}

			cc.setTeacher(Teacher.create(t));
		}

	}

	@RequestMapping
	@Transactional()
	@Barrier(PurviewConstants.ORDERFORM)
	@ResponseBody
	public JsonResult _update(@ObjectParam("form") OrderFormForm form) {
		long branchId = sessionUserService.getBranch().getIdLong();

		OrderForm cc = form.toOrderForm();

		prepare(cc, form);

		if (sessionUserService.isHeadquarter()) {

		} else {
			// 分支机构
			orderFormDao.checkAllowEdit(form.getId(), branchId);
		}

		cc.setUpdater(sessionUserService.getUser());

		Mix<OrderForm, StudentPoints> ret = orderFormDao.update(cc,
				sessionUserService.isHeadquarter());

		StudentPoints s = ret.getObject2();
		if (Math.abs(s.getPoints()) == cc.getPoints()
				&& s.getOwner().getId() == cc.getStudent().getId()) {
			// 全部相同的,没有必要扣分和加分了
		} else {
			// 先扣分
			studentPointsService.save(s);
			// 再加分
			studentPointsService.save(StudentPoints.create(cc.getStudent(),
					StudentPoints.FROM_ORDER_FORM, cc.getPoints(), "修改订单:"
							+ ret.object1.getNo(),
					OrderFormHelper.buildRefId(form.getId())));

		}
		return JsonResult.s();
	}

	@RequestMapping
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	@Barrier({ PurviewConstants.ORDERFORM })
	public ModelAndView update(@RequestParam("id") Long id) {
		long branchId = sessionUserService.getBranch().getIdLong();

		List<CourseProduct> courseProductList = null;
		SelectOptionList genders = null;

		courseProductList = courseProductDao.listByBranch(branchId);
		genders = BaseHelper.getGenderList(0);
		SelectOptionList commisionTypeList = OrderFormHelper
				.getCommissionTypes(0);

		SelectOptionList payStatusList = OrderFormHelper.getPayStatusList(0);

		return load4show(id, false).addObject("payStatusList", payStatusList)
				.addObject("genders", genders)
				.addObject("commisionTypeList", commisionTypeList)
				.addObject("productList", courseProductList);

	}

	private ModelAndView load4show(Long id, boolean view) {
		long branchId = sessionUserService.getBranch().getIdLong();
		if (sessionUserService.isHeadquarter()) {

		} else {
			// 分支机构
			if (view) {
				orderFormDao.checkAllowView(id, branchId);
			} else {
				orderFormDao.checkAllowEdit(id, branchId);
			}

		}

		OrderForm u = orderFormDao.findById(id);

		u.getUpdater().getLoginId();

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
				.addObject("orderForm", u).addObject("icon", icon);
	}

	@RequestMapping
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	@Barrier({ PurviewConstants.ORDERFORM, PurviewConstants.ORDER_FORM_SEARCH,
			PurviewConstants.ORDERFORM_CONFIRM })
	public ModelAndView view(@RequestParam("id") Long id) {
		return load4show(id, true);
	}

	@RequestMapping
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	@Barrier(PurviewConstants.ORDERFORM)
	public ModelAndView view4confirm(@RequestParam("id") Long id) {
		return load4show(id, true);
	}

	@RequestMapping
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	@Barrier(PurviewConstants.ORDERFORM)
	public ModelAndView entry(
			@ObjectParam("condition") OrderFormCondition condition) {
		return _entry(condition);

	}

	private ModelAndView _entry(OrderFormCondition condition) {
		if (condition == null) {
			condition = new OrderFormCondition();
			condition.setBeginDate(DateHelper.getThisMonthBegin());
			condition.setEndDate(DateHelper.getThisMonthEnd());
		}
		MapList branchList = branchDao.loadMetaWithoutHeadquarter();

		// SelectOptionList genders = BaseHelper.getGenderList(0);

		// SelectOptionList levelList = StudentHelper.getLevelList(0);

		// MapList branchList = branchDao.loadMeta();

		// SelectOptionList statusList = PuertaWeb.getYesNoList(PuertaWeb.YES,
		// new String[] { "有效", "无效" });

		SelectOptionList payStatusList = OrderFormHelper.getPayStatusList(0);
		SelectOptionList statusList = OrderFormHelper.getStatusList(0);
		List<CourseProduct> cps = courseProductDao
				.listByBranch(sessionUserService.getBranch().getIdLong());
		SelectOptionList commisionTypeList = OrderFormHelper
				.getCommissionTypes(0);

		return new ModelAndView().addObject("condition", condition)
				.addObject("payStatusList", payStatusList)
				.addObject("statusList", statusList)
				.addObject("commissionTypeList", commisionTypeList)
				.addObject("courseProducts", cps)
				.addObject("branches", branchList)
				.addObject("headquarter", sessionUserService.isHeadquarter());
	}

	@RequestMapping
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	@ResponseBody
	@Barrier(PurviewConstants.ORDERFORM)
	public YuiResult list(
			@ObjectParam("condition") OrderFormCondition condition,
			@RequestParam(value = "pageIndex", required = false, defaultValue = "0") int pageIndex) {
		if (condition == null) {
			condition = new OrderFormCondition();
		}

		PaginationSupport<MapBean> results = null;

		// 强制
		if (!sessionUserService.isHeadquarter()) {
			condition.setBranchId(sessionUserService.getBranch().getIdLong());
		}

		results = orderFormDao.search(condition, pageIndex);
		return YuiResult.create(results, MAPPING_FIX);
	}

	@RequestMapping
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	@Barrier(PurviewConstants.ORDERFORM_CONFIRM)
	public ModelAndView confirm(
			@ObjectParam("condition") OrderFormCondition condition) {
		return _entry(condition);

	}

	@RequestMapping
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	@ResponseBody
	@Barrier(PurviewConstants.ORDERFORM_CONFIRM)
	public YuiResult list4confirm(
			@ObjectParam("condition") OrderFormCondition condition,
			@RequestParam(value = "pageIndex", required = false, defaultValue = "0") int pageIndex) {
		if (condition == null) {
			condition = new OrderFormCondition();
		}

		PaginationSupport<MapBean> results = null;
		condition.setStatus(OrderForm.STATUS_INPUT);
		condition.setPayStatus(OrderForm.PAY_STATUS_PAID);

		// 强制
		if (!sessionUserService.isHeadquarter()) {
			condition.setBranchId(sessionUserService.getBranch().getIdLong());
		}

		results = orderFormDao.search(condition, pageIndex);
		return YuiResult.create(results, MAPPING_FIX);
	}

	@RequestMapping
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	@Barrier(PurviewConstants.ORDER_FORM_SEARCH)
	public ModelAndView search(
			@ObjectParam("condition") OrderFormCondition condition) {
		return _entry(condition);

	}

	@RequestMapping
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	@ResponseBody
	@Barrier(PurviewConstants.ORDER_FORM_SEARCH)
	public YuiResult list4search(
			@ObjectParam("condition") OrderFormCondition condition,
			@RequestParam(value = "pageIndex", required = false, defaultValue = "0") int pageIndex) {
		if (condition == null) {
			condition = new OrderFormCondition();
		}

		PaginationSupport<MapBean> results = null;
		// condition.setStatus(OrderForm.STATUS_INPUT);
		// condition.setPayStatus(OrderForm.PAY_STATUS_PAID);

		// 强制
		if (!sessionUserService.isHeadquarter()) {
			condition.setBranchId(sessionUserService.getBranch().getIdLong());
		}

		results = orderFormDao.search(condition, pageIndex);
		return YuiResult.create(results, MAPPING_FIX);
	}

	@Transactional
	@RequestMapping
	@Barrier(PurviewConstants.ORDERFORM_CONFIRM)
	public String _confirm(
			@RequestParam(value = "objId", required = false) Long[] id) {

		// materialRequireDao.precheckForRemove(id,);

		logger.debug("size:" + id.length);

		long branchId = sessionUserService.getBranch().getIdLong();

		orderFormDao.confirm(id, branchId, sessionUserService.getActorId());
		return ViewResult.JSON_SUCCESS;
	}

	@Transactional
	@RequestMapping
	@Barrier(PurviewConstants.ORDERFORM)
	public String remove(
			@RequestParam(value = "objId", required = false) Long[] id) {

		long branchId = sessionUserService.getBranch().getIdLong();
		MapList ml = orderFormDao.precheckForRemove(id, branchId);

		logger.debug("size:" + id.length);

		orderFormDao.deleteById(id);

		// 回滚积分
		for (MapBean mb : ml) {

			studentPointsService.save(StudentPoints.create(
					Student.create(mb.asLong("studentId")),
					StudentPoints.FROM_ORDER_FORM, mb.asInteger("points") * -1,
					"删除订单:" + mb.string("no"),
					OrderFormHelper.buildRefId(mb.asLong("id"))));
		}

		return ViewResult.JSON_SUCCESS;
	}

	@RequestMapping
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	@Barrier(check = false, value = PurviewConstants.CHARGE_BACK_CREATE)
	public ModelAndView chargeback(
			@ObjectParam("condition") OrderFormCondition condition) {
		return _entry(condition);

	}

	@RequestMapping
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	@ResponseBody
	@Barrier(check = false, value = PurviewConstants.CHARGE_BACK_CREATE)
	public YuiResult list4chargeback(
			@ObjectParam("condition") OrderFormCondition condition,
			@RequestParam(value = "pageIndex", required = false, defaultValue = "0") int pageIndex) {
		if (condition == null) {
			condition = new OrderFormCondition();
		}

		PaginationSupport<MapBean> results = null;
		condition.setStatus(OrderForm.STATUS_CONFIRM);
		condition.setForChargeback(true);
		// condition.setPayStatus(OrderForm.PAY_STATUS_PAID);

		// 强制
		if (!sessionUserService.isHeadquarter()) {
			condition.setBranchId(sessionUserService.getBranch().getIdLong());
		}

		results = orderFormDao.search(condition, pageIndex);
		return YuiResult.create(results, MAPPING_FIX);
	}

	@RequestMapping
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	@Barrier(check = false, value = PurviewConstants.CHARGE_BACK_CREATE)
	public ModelAndView view4chargeback(@RequestParam("id") Long id) {

		// 预检
		if (chargebackDao.hasChargeback(id, 0L)) {
			Pe.raise("指定订单已经存在退单");
		}
		orderFormDao.checkAllowChargeback(id, sessionUserService.getBranch()
				.getIdLong());

		return load4show(id, true).addObject("cbUpdateMode", false);
	}

	@RequestMapping
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	@Barrier(check = false, value = PurviewConstants.SALESMAN_PERFORMANCE)
	public ModelAndView monthlyReport(
			@RequestParam(value = "year", required = false, defaultValue = "0") int year,
			@RequestParam(value = "branchId", required = false, defaultValue = "0") long branchId

	) {

		MapList branchList = branchDao.loadMetaWithoutHeadquarter();

		Calendar cal = Calendar.getInstance();

		int thisYear = cal.get(Calendar.YEAR);
		if (year == 0) {
			year = thisYear;

		} else {
		}

		SelectOptionList monthList = null;
		Map<String, MapBean> table = null;
		if (branchId == 0 && sessionUserService.isHeadquarter()) {

		} else {

			if (!sessionUserService.isHeadquarter()) {
				branchId = sessionUserService.getBranch().getIdLong();
			}

			// start!

			// 加载当月确认的订单和退单
			MapList orders = orderFormDao.stat4Monthly(year, branchId);

			MapList chargebacks = chargebackDao.stat4Monthly(year, branchId);

			// 月份
			monthList = SelectOption.getList(0, 1, 12);

			table = OrderFormHelper.merge(orders, chargebacks);

			logger.debug("size=" + table.size());
		}

		// results

		return new ModelAndView().addObject("year", year)
				.addObject("monthList", monthList).addObject("table", table)
				.addObject("thisYear", thisYear)
				.addObject("branches", branchList)
				.addObject("branch", sessionUserService.getBranch())
				.addObject("branchId", branchId)
				.addObject("headquarter", sessionUserService.isHeadquarter());
	}

	@RequestMapping
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	@Barrier(check = false, value = PurviewConstants.STUDENT_COURSE_PROCESS)
	public ModelAndView students(
			@ObjectParam("condition") OrderFormCondition condition) {
		return _entry(condition);

	}

	@RequestMapping
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	@ResponseBody
	@Barrier(check = false, value = PurviewConstants.STUDENT_COURSE_PROCESS)
	public YuiResult list4students(
			@ObjectParam("condition") OrderFormCondition condition,
			@RequestParam(value = "pageIndex", required = false, defaultValue = "0") int pageIndex) {
		if (condition == null) {
			condition = new OrderFormCondition();
		}

		PaginationSupport<MapBean> results = null;

		// 强制
		if (!sessionUserService.isHeadquarter()) {
			condition.setBranchId(sessionUserService.getBranch().getIdLong());
		}
		// condition.setStatus(OrderForm.STATUS_CONFIRM);
		condition.setForStudents(true);

		results = orderFormDao.search(condition, pageIndex);
		return YuiResult.create(results, MAPPING_FIX);
	}
}
