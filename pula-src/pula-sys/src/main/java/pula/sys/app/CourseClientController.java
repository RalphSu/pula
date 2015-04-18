package pula.sys.app;

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
import puerta.support.ViewResult;
import puerta.support.annotation.Barrier;
import puerta.support.annotation.ObjectParam;
import puerta.support.vo.SelectOptionList;
import puerta.system.vo.JsonResult;
import puerta.system.vo.MapBean;
import puerta.system.vo.MapList;
import puerta.system.vo.YuiResult;
import puerta.system.vo.YuiResultMapper;
import pula.sys.PurviewConstants;
import pula.sys.conditions.CourseClientCondition;
import pula.sys.daos.BranchDao;
import pula.sys.daos.CourseClientDao;
import pula.sys.domains.CourseClient;
import pula.sys.domains.SysUser;
import pula.sys.forms.CourseClientForm;
import pula.sys.helpers.CourseClientHelper;
import pula.sys.services.SessionUserService;

@Controller
public class CourseClientController {

	private static final YuiResultMapper<MapBean> MAPPING_FIX = new YuiResultMapper<MapBean>() {
		@Override
		public Map<String, Object> toMap(MapBean obj) {

			obj.add("statusName",
					CourseClientHelper.getStatusName(obj.asInteger("status")));

			return obj;
		}
	};

	@Resource
	BranchDao branchDao;

	@Resource
	CourseClientDao courseClientDao;
	@Resource
	SessionUserService sessionUserService;

	@RequestMapping
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	@Barrier(PurviewConstants.COURSE_CLIENT)
	public ModelAndView entry(
			@ObjectParam("condition") CourseClientCondition condition) {
		if (condition == null) {
			condition = new CourseClientCondition();
		}

		MapList branchList = branchDao.loadMetaWithoutHeadquarter();

		SelectOptionList statusList = CourseClientHelper.getStatusList(0);
		statusList.remove(0);
		return new ModelAndView().addObject("condition", condition)
				.addObject("statusList", statusList)
				.addObject("branchList", branchList);

	}

	@RequestMapping
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	@ResponseBody
	@Barrier(PurviewConstants.COURSE_CLIENT)
	public YuiResult list(
			@ObjectParam("condition") CourseClientCondition condition,
			@RequestParam(value = "pageIndex", required = false, defaultValue = "0") int pageIndex) {
		if (condition == null) {
			condition = new CourseClientCondition();
		}
		// PaginationSupport<Teacher> results = null;
		// results = teacherDao.search(condition, pageIndex);

		PaginationSupport<MapBean> results = courseClientDao.search(condition,
				pageIndex);

		return YuiResult.create(results, MAPPING_FIX);
	}

	@RequestMapping
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	@Barrier(PurviewConstants.COURSE_CLIENT)
	public ModelAndView apply(
			@ObjectParam("condition") CourseClientCondition condition) {
		if (condition == null) {
			condition = new CourseClientCondition();
		}

		MapList branchList = branchDao.loadMetaWithoutHeadquarter();

		SelectOptionList statusList = CourseClientHelper.getStatusList(0);
		statusList.remove(0);
		statusList.removeLast();
		return new ModelAndView().addObject("condition", condition)
				.addObject("statusList", statusList)
				.addObject("branchList", branchList);

	}

	@RequestMapping
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	@ResponseBody
	@Barrier(PurviewConstants.COURSE_CLIENT)
	public YuiResult list4Apply(
			@ObjectParam("condition") CourseClientCondition condition,
			@RequestParam(value = "pageIndex", required = false, defaultValue = "0") int pageIndex) {
		if (condition == null) {
			condition = new CourseClientCondition();
		}
		// PaginationSupport<Teacher> results = null;
		// results = teacherDao.search(condition, pageIndex);

		condition.setStatus(CourseClient.STATUS_NEW);
		condition.setForApply(true);

		PaginationSupport<MapBean> results = courseClientDao.search(condition,
				pageIndex);

		return YuiResult.create(results, MAPPING_FIX);
	}

	@RequestMapping
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	@ResponseBody
	@Barrier(PurviewConstants.COURSE_CLIENT)
	public JsonResult get(@RequestParam("id") long id) {
		MapBean mb = courseClientDao.unique(id);
		return JsonResult.s(mb);
	}

	@RequestMapping
	@Transactional()
	@ResponseBody
	@Barrier(PurviewConstants.COURSE_CLIENT)
	public JsonResult _apply(@ObjectParam("form") CourseClientForm form) {
		CourseClient cc = form.toCourseClient();
		cc.setApplier(SysUser.create(sessionUserService.getActorId()));
		courseClientDao.apply(cc);
		return JsonResult.s();
	}

	@RequestMapping
	@Transactional()
	@ResponseBody
	@Barrier(PurviewConstants.COURSE_CLIENT)
	public JsonResult _update(@ObjectParam("form") CourseClientForm form) {
		CourseClient cc = form.toCourseClient();
		cc.setApplier(SysUser.create(sessionUserService.getActorId()));
		courseClientDao.update(cc);
		return JsonResult.s();
	}

	@Transactional
	@RequestMapping
	@Barrier(PurviewConstants.COURSE_CLIENT)
	public String remove(
			@RequestParam(value = "objId", required = false) Long[] id) {
		courseClientDao.deleteById(id);
		return ViewResult.JSON_SUCCESS;
	}

	@Transactional
	@RequestMapping
	@Barrier(PurviewConstants.COURSE_CLIENT)
	public String enable(
			@RequestParam(value = "objId", required = false) Long[] id,
			@RequestParam(value = "enable", required = false) boolean enable) {
		courseClientDao.doEnable(id, enable);
		return ViewResult.JSON_SUCCESS;
	}

}
