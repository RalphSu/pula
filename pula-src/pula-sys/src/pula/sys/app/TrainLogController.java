package pula.sys.app;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

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
import puerta.support.utils.FreemarkerUtil;
import puerta.support.utils.JacksonUtil;
import puerta.support.utils.WxlSugar;
import puerta.system.vo.JsonResult;
import puerta.system.vo.MapBean;
import puerta.system.vo.MapList;
import puerta.system.vo.YuiResult;
import pula.sys.PurviewConstants;
import pula.sys.conditions.TrainLogCondition;
import pula.sys.daos.BranchDao;
import pula.sys.daos.TeacherAssignmentDao;
import pula.sys.daos.TeacherDao;
import pula.sys.daos.TrainLogDao;
import pula.sys.daos.TrainLogItemDao;
import pula.sys.domains.Branch;
import pula.sys.domains.TrainLog;
import pula.sys.domains.TrainLogItem;
import pula.sys.forms.TrainLogForm;
import pula.sys.forms.TrainLogItemForm;
import pula.sys.helpers.TrainLogHelper;
import pula.sys.services.SessionUserService;

@Controller
public class TrainLogController {

	@Resource
	TrainLogDao trainLogDao;
	@Resource
	TeacherDao teacherDao;
	@Resource
	TrainLogItemDao trainLogItemDao;
	@Resource
	SessionUserService sessionUserService;
	@Resource
	BranchDao branchDao;
	@Resource
	TeacherAssignmentDao teacherAssignmentDao;

	@RequestMapping
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	@Barrier(PurviewConstants.TRAIN_LOG)
	public ModelAndView entry(
			@ObjectParam("condition") TrainLogCondition condition) {
		return _entry(condition);

	}

	private ModelAndView _entry(TrainLogCondition condition) {
		if (condition == null) {
			condition = new TrainLogCondition();
			condition.setBeginDate(DateHelper.getThisMonthBegin());
			condition.setEndDate(DateHelper.getThisMonthEnd());
		}

		MapList branchList = branchDao.loadMetaWithoutHeadquarter();

		return new ModelAndView().addObject("condition", condition)
				.addObject("branches", branchList)
				.addObject("headquarter", sessionUserService.isHeadquarter());
	}

	@RequestMapping
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	@ResponseBody
	@Barrier(PurviewConstants.TRAIN_LOG)
	public YuiResult list(
			@ObjectParam("condition") TrainLogCondition condition,
			@RequestParam(value = "pageIndex", required = false, defaultValue = "0") int pageIndex) {
		if (condition == null) {
			condition = new TrainLogCondition();
		}
		// PaginationSupport<TrainLog> results = null;
		// results = trainLogDao.search(condition, pageIndex);
		if (!sessionUserService.isHeadquarter()) {
			condition.setBranchId(sessionUserService.getBranch().getIdLong());
		}

		PaginationSupport<MapBean> results = trainLogDao.search(condition,
				pageIndex);

		return YuiResult.create(results);
	}

	@RequestMapping
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	@Barrier(PurviewConstants.TRAIN_LOG)
	public ModelAndView create(HttpServletResponse res) {

		// load the teachers;
		// enabled & status - in
		MapList teachers = teacherDao.listMetaEnabledIn(sessionUserService
				.getBranch().getIdLong());

		return new ModelAndView().addObject("teachers", teachers)
				.addObject("updateMode", false)
				.addObject("today", DateExTool.getToday());
	}

	// 保存

	@RequestMapping
	@ResponseBody
	@Transactional
	@Barrier(PurviewConstants.TRAIN_LOG)
	public JsonResult _create(@ObjectParam("trainLog") TrainLogForm form,
			@RequestParam("json") String json) {

		TrainLog cc = form.toTrainLog();
		cc.setCreator(sessionUserService.getUser());
		cc.setBranch(Branch.create(sessionUserService.getBranch().getIdLong()));
		List<TrainLogItem> items = prepareData(form, json, cc);

		cc = trainLogDao.save(cc);
		trainLogItemDao.save(items, cc, false);
		return JsonResult.s();
	}

	private List<TrainLogItem> prepareData(TrainLogForm form, String json,
			TrainLog cc) {

		// 日期检查

		Calendar allowEditDate = TrainLogHelper.getAllowEditDate();
		if (allowEditDate.after(cc.getTrainDate())) {
			Pe.raise("不可新增或修改限制日期[" + DateExTool.getText(allowEditDate)
					+ "]之前的数据:");
		}

		List<TrainLogItemForm> items = null;
		try {
			items = JacksonUtil.getList(json, TrainLogItemForm.class);
		} catch (Exception e) {
			e.printStackTrace();
			Pe.raise(e.getMessage());
		}
		List<Long> teacher_ids = WxlSugar.newArrayList();
		List<TrainLogItem> it = WxlSugar.newArrayList();
		for (TrainLogItemForm f : items) {
			it.add(f.toTrainLogItem());
			teacher_ids.add(f.getTeacherId());
		}
		if (!sessionUserService.isHeadquarter()) {
			teacherAssignmentDao.checkAllowUse(teacher_ids, sessionUserService
					.getBranch().getIdLong(), cc.getTrainDate());
			if (form.getId() != 0) {
				trainLogDao.checkAllowEdit(form.getId(), sessionUserService
						.getBranch().getIdLong());
			}
		}

		return it;

	}

	@RequestMapping
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	@Barrier(PurviewConstants.TRAIN_LOG)
	public ModelAndView update(@RequestParam("id") Long id) {
		return load4show(id, false);
	}

	public ModelAndView load4show(long id, boolean forView) {

		if (forView) {
			if (!sessionUserService.isHeadquarter()) {

				trainLogDao.checkAllowView(id, sessionUserService.getBranch()
						.getIdLong());

			}
		}

		TrainLog u = trainLogDao.findById(id);

		if (u.getUpdater() != null) {
			u.getUpdater().getLoginId();
		}

		Map<Long, TrainLogItem> map = WxlSugar.newHashMap();

		MapList teachers = null;

		if (!forView) {
			teachers = teacherDao.listMetaEnabledIn(sessionUserService
					.getBranch().getIdLong());
		} else {
			u.getBranch().getName();
		}

		for (TrainLogItem item : u.getItems()) {
			map.put(item.getTeacher().getId(), item);
			if (forView) {
				item.getTeacher().getName();
			}
		}

		return new ModelAndView().addObject("updateMode", true)
				.addObject("items", FreemarkerUtil.hash(map))
				.addObject("trainLog", u).addObject("teachers", teachers);
	}

	@RequestMapping
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	@Barrier(PurviewConstants.TRAIN_LOG)
	public ModelAndView view(@RequestParam("id") Long id) {
		return load4show(id, true);
	}

	@RequestMapping
	@ResponseBody
	@Transactional
	@Barrier(PurviewConstants.TRAIN_LOG)
	public JsonResult _update(@ObjectParam("trainLog") TrainLogForm form,
			@RequestParam("json") String json) {

		TrainLog cc = form.toTrainLog();
		cc.setUpdater(sessionUserService.getUser());
		List<TrainLogItem> items = prepareData(form, json, cc);

		cc = trainLogDao.update(cc);
		trainLogItemDao.save(items, cc, true);
		return JsonResult.s();
	}

	@Transactional
	@RequestMapping
	@Barrier(PurviewConstants.TRAIN_LOG)
	public String remove(
			@RequestParam(value = "objId", required = false) Long[] id) {

		if (!sessionUserService.isHeadquarter()) {
			trainLogDao.checkAllowRemove(id, sessionUserService.getBranch()
					.getIdLong());
		}

		trainLogDao.deleteById(id);
		return ViewResult.JSON_SUCCESS;
	}

}
