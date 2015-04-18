package pula.sys.app;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
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
import puerta.support.utils.WxlSugar;
import puerta.support.vo.NoNameVo;
import puerta.system.keeper.DictLimbKeeper;
import puerta.system.keeper.ParameterKeeper;
import puerta.system.service.SessionService;
import puerta.system.vo.JsonResult;
import puerta.system.vo.YuiResult;
import puerta.system.vo.YuiResultMapper;
import pula.sys.PurviewConstants;
import pula.sys.conditions.SysCategoryCondition;
import pula.sys.daos.SysCategoryDao;
import pula.sys.domains.SysCategory;
import pula.sys.forms.SysCategoryForm;

@Controller
public class SysCategoryController {

	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(SysCategory.class);
	private static final YuiResultMapper<SysCategory> MAPPING = new YuiResultMapper<SysCategory>() {
		@Override
		public Map<String, Object> toMap(SysCategory obj) {

			Map<String, Object> m = WxlSugar.newHashMap();
			m.put("no", obj.getNo());
			m.put("id", obj.getIdentify());
			m.put("name", obj.getName());
			m.put("level", obj.getLevel());
			m.put("indexNo", obj.getIndexNo());
			m.put("enabled", obj.isEnabled());
			if (obj.getParent() != null) {
				m.put("parentId", obj.getParent().getIdentify());
			} else {
				m.put("parentId", "");
			}
			return m;
		}
	};

	@Resource
	SysCategoryDao sysCategoryDao;
	@Resource
	ParameterKeeper parameterKeeper;
	@Resource
	SessionService sessionService;
	@Resource
	DictLimbKeeper dictLimbKeeper;

	@RequestMapping
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	@Barrier(PurviewConstants.SYS_CATEGORY)
	public ModelAndView entry(
			@ObjectParam("condition") SysCategoryCondition condition,
			HttpServletRequest request) {
		if (condition == null) {
			condition = new SysCategoryCondition();
		}

		return new ModelAndView().addObject("condition", condition);
	}

	@RequestMapping
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	@ResponseBody
	@Barrier(PurviewConstants.SYS_CATEGORY)
	public YuiResult list(
			@ObjectParam("condition") SysCategoryCondition condition,
			@RequestParam(value = "pageIndex", required = false, defaultValue = "0") int pageIndex) {
		if (condition == null) {
			condition = new SysCategoryCondition();
		}
		PaginationSupport<SysCategory> results = null;
		results = sysCategoryDao.search(condition, pageIndex);
		return YuiResult.create(results, MAPPING);
	}

	@RequestMapping
	@Barrier(PurviewConstants.SYS_CATEGORY)
	public String create() {
		return null;
	}

	@RequestMapping
	@Transactional
	@Barrier(PurviewConstants.SYS_CATEGORY)
	public String _create(@ObjectParam("sysCategory") SysCategoryForm cli2) {

		SysCategory cli = cli2.toSysCategory();

		cli = sysCategoryDao.save(cli);

		return ViewResult.JSON_SUCCESS;
	}

	@RequestMapping
	@Transactional()
	@Barrier(PurviewConstants.SYS_CATEGORY)
	public String _update(@ObjectParam("sysCategory") SysCategoryForm cli2) {

		sysCategoryDao.update(cli2.toSysCategory());

		return ViewResult.JSON_SUCCESS;
	}

	@Transactional
	@RequestMapping
	@Barrier(PurviewConstants.SYS_CATEGORY)
	public String remove(
			@RequestParam(value = "objId", required = false) String[] id) {
		sysCategoryDao.deleteById(id);

		return ViewResult.JSON_SUCCESS;
	}

	@RequestMapping
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	@ResponseBody
	@Barrier(PurviewConstants.SYS_CATEGORY)
	public JsonResult get(@RequestParam("id") String id) {
		SysCategory u = sysCategoryDao.findById(id);
		return JsonResult.s(MAPPING.toMap(u));
	}

	@Transactional
	@RequestMapping
	@Barrier(PurviewConstants.SYS_CATEGORY)
	public String enable(
			@RequestParam(value = "objId", required = false) String[] id,
			@RequestParam(value = "enable", required = false) boolean enable) {
		sysCategoryDao.doEnable(id, enable);

		return ViewResult.JSON_SUCCESS;
	}

	@RequestMapping
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	@ResponseBody
	@Barrier()
	public JsonResult children(@RequestParam("parentNo") String parentNo,
			@RequestParam(value = "orderBy", required = false) String orderBy) {
		List<SysCategory> list = this.sysCategoryDao.getChildren(parentNo,
				orderBy);
		return JsonResult.sl(list, MAPPING);
	}

	@RequestMapping
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	@ResponseBody
	@Barrier()
	public JsonResult underById(@RequestParam("id") String id) {
		List<SysCategory> list = this.sysCategoryDao.getUnderById(id);
		return JsonResult.sl(list, MAPPING);
	}

	@RequestMapping
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	@Barrier()
	public ModelAndView find(
			@RequestParam(value = "parentId", required = false) String id,
			@RequestParam(value = "no", required = false) String no) {

		ModelAndView m = new ModelAndView(ViewResult.JSON_LIST);
		List<SysCategory> list = sysCategoryDao.loadByKeywords(id, no);
		logger.debug("list.size=" + list.size());
		List<NoNameVo> r = WxlSugar.newArrayList(list.size());
		for (SysCategory sc : list) {
			r.add(NoNameVo.create(sc.getName(), sc.getName(), sc.getName()));
		}
		m.addObject("list", r);
		return m;
	}
}
