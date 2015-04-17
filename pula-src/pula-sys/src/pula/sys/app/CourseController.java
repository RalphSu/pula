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

import puerta.PuertaWeb;
import puerta.support.PaginationSupport;
import puerta.support.Pe;
import puerta.support.ViewResult;
import puerta.support.annotation.Barrier;
import puerta.support.annotation.ObjectParam;
import puerta.support.utils.WxlSugar;
import puerta.system.keeper.ParameterKeeper;
import puerta.system.service.SessionService;
import puerta.system.vo.JsonResult;
import puerta.system.vo.MapList;
import puerta.system.vo.YuiResult;
import puerta.system.vo.YuiResultMapper;
import pula.sys.BhzqConstants;
import pula.sys.PurviewConstants;
import pula.sys.conditions.CourseCondition;
import pula.sys.daos.BranchDao;
import pula.sys.daos.CourseDao;
import pula.sys.daos.CourseDeploymentDao;
import pula.sys.daos.SysCategoryDao;
import pula.sys.domains.Course;
import pula.sys.domains.SysCategory;
import pula.sys.domains.SysUser;
import pula.sys.forms.CourseForm;
import pula.sys.services.SessionUserService;

@Controller
public class CourseController {

	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(Course.class);
	private static final YuiResultMapper<Course> MAPPING = new YuiResultMapper<Course>() {
		@Override
		public Map<String, Object> toMap(Course obj) {

			Map<String, Object> m = WxlSugar.newHashMap();
			m.put("no", obj.getNo());
			m.put("id", obj.getId());
			m.put("name", obj.getName());
			if (obj.getCategory() != null) {
				m.put("categoryName", obj.getCategory().getName());
			} else {

			}
			if (obj.getSubCategory() != null) {
				m.put("subCategoryName", obj.getSubCategory().getName());
			} else {

			}
			m.put("expiredTime", obj.getExpiredTime());
			m.put("indexNo", obj.getIndexNo());
			m.put("publishTime", obj.getPublishTime());
			m.put("showInWeb", obj.isShowInWeb());
			m.put("enabled", obj.isEnabled());

			return m;
		}
	};

	private static final YuiResultMapper<Course> MAPPING_FULL = new YuiResultMapper<Course>() {
		@Override
		public Map<String, Object> toMap(Course obj) {

			Map<String, Object> m = MAPPING.toMap(obj);
			if (obj.getCategory() != null) {
				m.put("categoryId", obj.getCategory().getId());
			} else {
			}
			if (obj.getSubCategory() != null) {
				m.put("subCategoryId", obj.getSubCategory().getId());
			} else {
			}
			m.put("comments", obj.getComments());
			m.put("key", obj.getKey());

			return m;
		}
	};

	@Resource
	CourseDao courseDao;
	@Resource
	ParameterKeeper parameterKeeper;
	@Resource
	SessionService sessionService;
	@Resource
	SysCategoryDao sysCategoryDao;
	@Resource
	BranchDao branchDao;
	@Resource
	CourseDeploymentDao courseDeploymentDao;
	@Resource
	SessionUserService sessionUserService;

	@RequestMapping
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	@Barrier(PurviewConstants.COURSE)
	public ModelAndView entry(
			@ObjectParam("condition") CourseCondition condition) {
		if (condition == null) {
			condition = new CourseCondition();
		}

		List<SysCategory> mts = sysCategoryDao
				.getUnder(BhzqConstants.SC_COURSE_CATEGORY);

		return new ModelAndView().addObject("condition", condition).addObject(
				"types", mts);
	}

	@RequestMapping
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	@ResponseBody
	@Barrier(PurviewConstants.COURSE)
	public YuiResult list(
			@ObjectParam("condition") CourseCondition condition,
			@RequestParam(value = "pageIndex", required = false, defaultValue = "0") int pageIndex) {
		if (condition == null) {
			condition = new CourseCondition();
		}
		PaginationSupport<Course> results = null;
		results = courseDao.search(condition, pageIndex);
		return YuiResult.create(results, MAPPING);
	}

	@RequestMapping
	@Barrier(PurviewConstants.COURSE)
	public String create() {
		return null;
	}

	@RequestMapping
	@Transactional
	@Barrier(PurviewConstants.COURSE)
	public String _create(@ObjectParam("course") CourseForm cli) {

		Course cc = cli.toCourse();
		cc.setCreator(SysUser.create(sessionService.getActorId()));

		courseDao.save(cc);

		return ViewResult.JSON_SUCCESS;
	}

	@RequestMapping
	@Transactional()
	@Barrier(PurviewConstants.COURSE)
	public String _update(@ObjectParam("course") CourseForm cli) {

		Course cc = cli.toCourse();
		cc.setUpdater(SysUser.create(sessionService.getActorId()));

		courseDao.update(cc);

		return ViewResult.JSON_SUCCESS;
	}

	@Transactional
	@RequestMapping
	@Barrier(PurviewConstants.COURSE)
	public String remove(
			@RequestParam(value = "objId", required = false) Long[] id) {
		courseDao.deleteById(id);
		return ViewResult.JSON_SUCCESS;
	}

	@RequestMapping
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	@ResponseBody
	@Barrier()
	public JsonResult get(@RequestParam("id") Long id) {
		Course u = courseDao.findById(id);

		Map<String, Object> m = MAPPING_FULL.toMap(u);

		return JsonResult.s(m);
	}

	@RequestMapping
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	@ResponseBody
	@Barrier()
	public JsonResult getByNo(@RequestParam("no") String no) {
		Course u = courseDao.findByNo(no);
		if (u == null) {
			Pe.raise("找不到指定的编号:" + no);
		}
		return JsonResult.s(MAPPING_FULL.toMap(u));
	}

	@RequestMapping
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	@Barrier()
	public ModelAndView find(
			@RequestParam(value = "no", required = false) String no,

			@RequestParam(value = "t", required = false) String t,

			@RequestParam(value = "prefix", required = false) String prefix) {
		ModelAndView m = new ModelAndView(ViewResult.JSON_LIST);
		MapList list = courseDao.loadByKeywords(no, t, prefix);
		logger.debug("list.size=" + list.size());
		m.addObject("list", list);
		return m;
	}

	@Transactional
	@RequestMapping
	@Barrier(PurviewConstants.COURSE)
	public String enable(
			@RequestParam(value = "objId", required = false) Long[] id,
			@RequestParam(value = "enable", required = false) boolean enable) {
		courseDao.doEnable(id, enable);
		return ViewResult.JSON_SUCCESS;
	}

	@RequestMapping
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	@Barrier(PurviewConstants.COURSE_DEPLOYMENT)
	public ModelAndView deploy(
			@ObjectParam("condition") CourseCondition condition) {
		if (condition == null) {
			condition = new CourseCondition();
		}

		List<SysCategory> mts = sysCategoryDao
				.getUnder(BhzqConstants.SC_COURSE_CATEGORY);

		MapList branchList = branchDao.loadMetaWithoutHeadquarter();

		return new ModelAndView().addObject("condition", condition)
				.addObject("types", mts).addObject("branches", branchList);
	}

	@RequestMapping
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	@ResponseBody
	@Barrier(PurviewConstants.COURSE)
	public YuiResult list4Deploy(
			@ObjectParam("condition") CourseCondition condition,
			@RequestParam(value = "pageIndex", required = false, defaultValue = "0") int pageIndex) {
		if (condition == null) {
			condition = new CourseCondition();
		}
		// 只显示启用的
		condition.setEnabledStatus(PuertaWeb.YES);
		PaginationSupport<Course> results = null;
		results = courseDao.search(condition, pageIndex);
		return YuiResult.create(results, MAPPING);
	}

	@RequestMapping
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	@ResponseBody
	@Barrier({ PurviewConstants.COURSE, PurviewConstants.COURSE_TASK_RESULT,
			PurviewConstants.COURSE_TASK_RESULT_SEARCH })
	public JsonResult listByCategory(
			@RequestParam("categoryId") String categoryId) {

		MapList courseList = null;

		if (sessionUserService.isHeadquarter()) {

			courseList = courseDao.list(categoryId);
		} else {
			courseList = courseDeploymentDao.listByBranch(sessionUserService
					.getBranch().getIdLong(), categoryId);
		}

		return JsonResult.s(courseList);
	}
}
