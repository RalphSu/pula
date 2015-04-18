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
import pula.sys.PurviewConstants;
import pula.sys.conditions.CourseProductCondition;
import pula.sys.daos.BranchDao;
import pula.sys.daos.CourseProductDao;
import pula.sys.daos.SysCategoryDao;
import pula.sys.domains.Course;
import pula.sys.domains.CourseProduct;
import pula.sys.domains.SysUser;
import pula.sys.forms.CourseProductForm;

@Controller
public class CourseProductController {

	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(Course.class);
	private static final YuiResultMapper<CourseProduct> MAPPING = new YuiResultMapper<CourseProduct>() {
		@Override
		public Map<String, Object> toMap(CourseProduct obj) {

			Map<String, Object> m = WxlSugar.newHashMap();
			m.put("no", obj.getNo());
			m.put("id", obj.getId());
			m.put("name", obj.getName());

			m.put("beginTime", obj.getBeginTime());
			m.put("endTime", obj.getEndTime());
			m.put("courseCount", obj.getCourseCount());
			m.put("price", obj.getPrice());
			// m.put("showInWeb", obj.isShowInWeb());
			m.put("enabled", obj.isEnabled());

			if (obj.getBranch() != null) {
				m.put("branchName", obj.getBranch().getName());
			}

			return m;
		}
	};

	private static final YuiResultMapper<CourseProduct> MAPPING_FULL = new YuiResultMapper<CourseProduct>() {
		@Override
		public Map<String, Object> toMap(CourseProduct obj) {

			Map<String, Object> m = MAPPING.toMap(obj);

			m.put("comments", obj.getComments());
			if (obj.getBranch() != null) {
				m.put("branchNo", obj.getBranch().getNo());
				m.put("branchId", obj.getBranch().getId());
			}

			return m;
		}
	};

	@Resource
	CourseProductDao courseProductDao;
	@Resource
	ParameterKeeper parameterKeeper;
	@Resource
	SessionService sessionService;
	@Resource
	SysCategoryDao sysCategoryDao;
	@Resource
	BranchDao branchDao;

	@RequestMapping
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	@Barrier(PurviewConstants.COURSE_PRODUCT)
	public ModelAndView entry(
			@ObjectParam("condition") CourseProductCondition condition) {
		if (condition == null) {
			condition = new CourseProductCondition();
		}
		MapList branchList = branchDao.loadMetaWithoutHeadquarter();

		return new ModelAndView().addObject("condition", condition).addObject(
				"branches", branchList);
	}

	@RequestMapping
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	@ResponseBody
	@Barrier(PurviewConstants.COURSE_PRODUCT)
	public YuiResult list(
			@ObjectParam("condition") CourseProductCondition condition,
			@RequestParam(value = "pageIndex", required = false, defaultValue = "0") int pageIndex) {
		if (condition == null) {
			condition = new CourseProductCondition();
		}
		PaginationSupport<CourseProduct> results = null;
		results = courseProductDao.search(condition, pageIndex);
		return YuiResult.create(results, MAPPING);
	}

	@RequestMapping
	@Barrier(PurviewConstants.COURSE_PRODUCT)
	public String create() {
		return null;
	}

	@RequestMapping
	@Transactional
	@Barrier(PurviewConstants.COURSE_PRODUCT)
	public String _create(@ObjectParam("courseProduct") CourseProductForm cli) {

		CourseProduct cc = cli.toCourseProduct();
		cc.setCreator(SysUser.create(sessionService.getActorId()));

		courseProductDao.save(cc);

		return ViewResult.JSON_SUCCESS;
	}

	@RequestMapping
	@Transactional()
	@Barrier(PurviewConstants.COURSE_PRODUCT)
	public String _update(@ObjectParam("courseProduct") CourseProductForm cli) {

		CourseProduct cc = cli.toCourseProduct();
		cc.setUpdater(SysUser.create(sessionService.getActorId()));
		courseProductDao.update(cc);

		return ViewResult.JSON_SUCCESS;
	}

	@Transactional
	@RequestMapping
	@Barrier(PurviewConstants.COURSE_PRODUCT)
	public String remove(
			@RequestParam(value = "objId", required = false) Long[] id) {
		courseProductDao.deleteById(id);
		return ViewResult.JSON_SUCCESS;
	}

	@RequestMapping
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	@ResponseBody
	@Barrier()
	public JsonResult get(@RequestParam("id") Long id) {
		CourseProduct u = courseProductDao.findById(id);

		Map<String, Object> m = MAPPING_FULL.toMap(u);

		return JsonResult.s(m);
	}

	@RequestMapping
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	@ResponseBody
	@Barrier()
	public JsonResult getByNo(@RequestParam("no") String no) {
		CourseProduct u = courseProductDao.findByNo(no);
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
		MapList list = courseProductDao.loadByKeywords(no, t, prefix);
		logger.debug("list.size=" + list.size());
		m.addObject("list", list);
		return m;
	}

	@Transactional
	@RequestMapping
	@Barrier(PurviewConstants.COURSE_PRODUCT)
	public String enable(
			@RequestParam(value = "objId", required = false) Long[] id,
			@RequestParam(value = "enable", required = false) boolean enable) {
		courseProductDao.doEnable(id, enable);
		return ViewResult.JSON_SUCCESS;
	}

}
