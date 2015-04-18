package pula.sys.app;

import java.util.Calendar;
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
import puerta.support.annotation.Barrier;
import puerta.support.annotation.ObjectParam;
import puerta.support.utils.JacksonUtil;
import puerta.support.utils.StringTool;
import puerta.support.utils.WxlSugar;
import puerta.system.vo.JsonResult;
import puerta.system.vo.MapBean;
import puerta.system.vo.MapList;
import puerta.system.vo.YuiResult;
import puerta.system.vo.YuiResultMapper;
import pula.sys.PurviewConstants;
import pula.sys.conditions.TeacherPerformanceCondition;
import pula.sys.daos.BranchDao;
import pula.sys.daos.ChargebackDao;
import pula.sys.daos.CourseTaskResultDao;
import pula.sys.daos.OrderFormDao;
import pula.sys.daos.TeacherDao;
import pula.sys.daos.TeacherPerformanceDao;
import pula.sys.domains.Branch;
import pula.sys.forms.TeacherPerformanceForm;
import pula.sys.helpers.TeacherHelper;
import pula.sys.helpers.TrainLogHelper;
import pula.sys.services.SessionUserService;

@Controller
public class TeacherPerformanceController {

	private static final Logger logger = Logger
			.getLogger(TeacherPerformanceController.class);

	private static final YuiResultMapper<MapBean> MAPPING_FIX = new YuiResultMapper<MapBean>() {

		@Override
		public Map<String, Object> toMap(MapBean obj) {
			int y = obj.asInteger("statYear");
			int m = obj.asInteger("statMonth");

			obj.put("month", y + "-" + StringTool.fillZero(m, 2));
			return obj;
		}
	};

	@Resource
	TeacherPerformanceDao teacherPerformanceDao;
	@Resource
	BranchDao branchDao;
	@Resource
	TeacherDao teacherDao;
	@Resource
	SessionUserService sessionUserService;
	@Resource
	ChargebackDao chargebackDao;
	@Resource
	OrderFormDao orderFormDao;
	@Resource
	CourseTaskResultDao courseTaskResultDao;

	@RequestMapping
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	@Barrier(PurviewConstants.TEACHER_PERFORMANCE)
	public ModelAndView entry(
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
		MapList teacherList = teacherDao.listMetaEnabledIn(branchId);
		Map<String, MapBean> table = null;
		String json_data = null;
		if (branchId == 0 && sessionUserService.isHeadquarter()) {

		} else {

			if (!sessionUserService.isHeadquarter()) {
				branchId = sessionUserService.getBranch().getIdLong();
			}

			// start!

			// 加载所有的销售!
			// MapList salesmanList = salesmanDao.mapList();

			// 加载当月确认的订单和退单
			Map<String, MapBean> records = teacherPerformanceDao.map(year,
					java_month, branchId);

			// TODO
			// 还应该载入课时数,包括在update 里面也是一样,必须把课时数填充进去

			Map<String, MapBean> courses = courseTaskResultDao.stat4Teacher(
					year, java_month, branchId);

			Map<String, MapBean> orders = orderFormDao.stat4Teacher(year,
					java_month, branchId);
			Map<String, MapBean> chargebacks = chargebackDao.stat4Teacher(year,
					java_month, branchId);

			// 形成最终salesman list
			// 销售人员数据完全从订单和退单中抽取
			teacherList = TeacherHelper.collect(teacherList, orders,
					chargebacks, records, courses);
			table = TeacherHelper.merge(records, orders, chargebacks, courses);

			logger.debug("size=" + table.size());

			// 转成JSON 数据得了!

			json_data = makeJson(teacherList, table);

		}

		// results

		return new ModelAndView().addObject("year", year)
				.addObject("teacherList", teacherList)
				.addObject("table", table).addObject("thisYear", thisYear)
				.addObject("thisMonth", thisMonth).addObject("month", month)
				.addObject("branches", branchList)
				.addObject("branch", sessionUserService.getBranch())
				.addObject("branchId", branchId)
				.addObject("json_data", json_data)
				.addObject("allowEdit", allowEdit(year, month))
				.addObject("headquarter", sessionUserService.isHeadquarter());
	}

	private String makeJson(MapList teacherList, Map<String, MapBean> table) {
		teacherList.eat(table, "no");

		return JacksonUtil.toString(teacherList);
	}

	@RequestMapping
	@Transactional
	@ResponseBody
	@Barrier(PurviewConstants.TEACHER_PERFORMANCE)
	public JsonResult _update(
			@RequestParam(value = "year", required = false, defaultValue = "0") int year,
			@RequestParam(value = "month", required = false, defaultValue = "0") int month,
			@RequestParam(value = "branchId", required = false, defaultValue = "0") long branchId,
			@RequestParam("json") String json) {

		if (branchId == 0 && sessionUserService.isHeadquarter()) {
			Pe.raise("总部需指定要操作的分支机构");
		} else if (!sessionUserService.isHeadquarter()) {
			branchId = sessionUserService.getBranch().getIdLong();
		}

		if (!allowEdit(year, month)) {
			Pe.raise("不允许修改历史月份的数据");
		}

		int java_month = month - 1;

		List<TeacherPerformanceForm> items = prepareData(json);

		// 理论上还要判断 teacher的id 是否都来自这个branch
		List<Long> teacherIds = WxlSugar.newArrayList();
		Map<Long, String> teacher_id_nos = null;
		for (TeacherPerformanceForm tpf : items) {

			teacherIds.add(tpf.getTeacherId());
		}
		teacher_id_nos = teacherDao.allIn(teacherIds, branchId);

		// 更新自己未关闭的

		Map<Long, Long> myIds = teacherPerformanceDao.getExists(year,
				java_month, branchId);

		Map<String, MapBean> orders = orderFormDao.stat4Teacher(year,
				java_month, branchId);
		Map<String, MapBean> chargebacks = chargebackDao.stat4Teacher(year,
				java_month, branchId);

		Branch branch = Branch.create(branchId);

		for (TeacherPerformanceForm item : items) {

			boolean exists = myIds.containsKey(item.getId());
			item.setBranch(branch);
			item.setStatYear(year);
			item.setStatMonth(java_month);

			String key = teacher_id_nos.get(item.getTeacherId());
			if (key == null) {
				continue;
			} else {
				MapBean mb = orders.get(key);
				if (mb != null) {
					int totalOrders = mb.asLong("totalOrders").intValue();
					item.setOrders(totalOrders);
				}

				mb = chargebacks.get(key);
				if (mb != null) {
					int totalCb = mb.asLong("totalBackOrders").intValue();
					item.setChargebacks(totalCb);
				}
			}

			teacherPerformanceDao.update(item.toTeacherPerformance(),
					sessionUserService.getActorId(), exists);

			if (exists) {
				myIds.remove(item.getId());
			}

		}

		// myIds left to remove
		if (myIds.size() != 0)
			teacherPerformanceDao.remove(myIds.values());

		return JsonResult.s();

	}

	private List<TeacherPerformanceForm> prepareData(String json) {
		List<TeacherPerformanceForm> items = null;
		try {
			items = JacksonUtil.getList(json, TeacherPerformanceForm.class);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("jsonDetail=" + json);
			Pe.raise(e.getMessage());
		}

		return items;
	}

	private boolean allowEdit(int year, int month) {

		if (!sessionUserService.isHeadquarter()) {
			return TrainLogHelper.allowEdit(year, month);
		}

		return true;
	}

	@RequestMapping
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	@Barrier({ PurviewConstants.TEACHER, PurviewConstants.TEACHER_SEARCH })
	public ModelAndView teacher(@RequestParam("id") Long id) {
		if (!sessionUserService.isHeadquarter()) {
			teacherDao.checkAllowView(id, sessionUserService.getBranch()
					.getIdLong());
		}
		MapBean meta = teacherDao.meta(id);
		if (meta == null) {
			Pe.raise("找不到指定的教师");
		}
		return new ModelAndView().addObject("id", id)
				.addObject("teacher_meta", meta)
				.addObject("headquarter", sessionUserService.isHeadquarter());

	}

	@RequestMapping
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	@ResponseBody
	@Barrier({ PurviewConstants.TEACHER, PurviewConstants.TEACHER_SEARCH })
	public YuiResult list4teacher(
			@ObjectParam("condition") TeacherPerformanceCondition condition,
			@RequestParam(value = "pageIndex", required = false, defaultValue = "0") int pageIndex) {
		if (condition == null) {
			condition = new TeacherPerformanceCondition();
		}

		if (!sessionUserService.isHeadquarter()) {
			// 不管传没传,就是要检查
			teacherDao.checkAllowView(condition.getTeacherId(),
					sessionUserService.getBranch().getIdLong());
			condition.setBranchId(sessionUserService.getBranch().getIdLong());
		}

		PaginationSupport<MapBean> results = teacherPerformanceDao.search(
				condition, pageIndex);

		return YuiResult.create(results, MAPPING_FIX);

		// MapList ml = new MapList();
		// // Calendar now = Calendar.getInstance();
		// ml.add(MapBean.map("month", "2013-11").add("branchName", "金桥分校")
		// .add("courseCount", 10).add("trainScore", 20.5)
		// .add("workDays", 20).add("realWorkDays", 18)
		// .add("attendanceScore", 10).add("review", 10));
		//
		// ml.add(MapBean.map("month", "2013-10").add("branchName", "金桥分校")
		// .add("courseCount", 10).add("trainScore", 30.5)
		// .add("leaveCount", 10).add("attendanceScore", 10)
		// .add("review", 10));
		//
		// ml.add(MapBean.map("month", "2013-09").add("branchName", "金桥分校")
		// .add("courseCount", 10).add("trainScore", 20.5)
		// .add("attendanceScore", 7).add("review", 8));
		//
		// return YuiResult.create(new PaginationSupport<MapBean>(ml, 0));
	}

}
