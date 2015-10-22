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

import puerta.support.PaginationSupport;
import puerta.support.Pe;
import puerta.support.ViewResult;
import puerta.support.annotation.Barrier;
import puerta.support.annotation.ObjectParam;
import puerta.support.utils.WxlSugar;
import puerta.system.keeper.ParameterKeeper;
import puerta.system.service.SessionService;
import puerta.system.vo.JsonResult;
import puerta.system.vo.YuiResult;
import puerta.system.vo.YuiResultMapper;
import pula.sys.PurviewConstants;
import pula.sys.conditions.BranchCondition;
import pula.sys.daos.BranchDao;
import pula.sys.domains.Branch;
import pula.sys.forms.BranchForm;

@Controller
@Barrier(ignore = true)
public class BranchController {

	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger
			.getLogger(BranchController.class);
	private static final YuiResultMapper<Branch> MAPPING = new YuiResultMapper<Branch>() {
		@Override
		public Map<String, Object> toMap(Branch obj) {

			Map<String, Object> m = WxlSugar.newHashMap();
			m.put("no", obj.getNo());
			m.put("id", obj.getId());
			m.put("name", obj.getName());
			m.put("linkman", obj.getLinkman());
			m.put("phone", obj.getPhone());
			m.put("email", obj.getEmail());
			m.put("enabled", obj.isEnabled());
			m.put("prefix", obj.getPrefix());
			return m;
		}
	};

	private static final YuiResultMapper<Branch> MAPPING_FULL = new YuiResultMapper<Branch>() {
		@Override
		public Map<String, Object> toMap(Branch obj) {

			Map<String, Object> m = MAPPING.toMap(obj);
			m.put("address", obj.getAddress());
			m.put("comments", obj.getComments());
			m.put("fax", obj.getFax());
			m.put("mobile", obj.getMobile());
			m.put("showInWeb", obj.isShowInWeb());
			m.put("headquarter", obj.isHeadquarter());

			return m;
		}
	};

	@Resource
	BranchDao branchDao;
	@Resource
	ParameterKeeper parameterKeeper;
	@Resource
	SessionService sessionService;

	// @Resource
	// DictLimbKeeper dictLimbKeeper;

	@RequestMapping
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	@Barrier(PurviewConstants.BRANCH)
	public ModelAndView entry(
			@ObjectParam("condition") BranchCondition condition) {
		if (condition == null) {
			condition = new BranchCondition();
		}

		return new ModelAndView().addObject("condition", condition);
	}

	@RequestMapping
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	@ResponseBody
    @Barrier(ignore = true)
	public YuiResult list(
			@ObjectParam("condition") BranchCondition condition,
			@RequestParam(value = "pageIndex", required = false, defaultValue = "0") int pageIndex) {
		if (condition == null) {
			condition = new BranchCondition();
		}
		PaginationSupport<Branch> results = null;
		results = branchDao.search(condition, pageIndex);
		return YuiResult.create(results, MAPPING);
	}

	@RequestMapping
	@Barrier(PurviewConstants.BRANCH)
	public String create() {
		return null;
	}

	@RequestMapping
	@Transactional
	@Barrier(PurviewConstants.BRANCH)
	public String _create(@ObjectParam("branch") BranchForm cli) {
		cli.setEnabled(true);
		Branch cc = cli.toBranch();

		branchDao.save(cc);
		return ViewResult.JSON_SUCCESS;
	}

	@RequestMapping
	@Transactional()
	@Barrier(PurviewConstants.BRANCH)
	public String _update(@ObjectParam("branch") BranchForm cli) {

		Branch cc = cli.toBranch();
		branchDao.update(cc);
		return ViewResult.JSON_SUCCESS;
	}

	@Transactional
	@RequestMapping
	@Barrier(PurviewConstants.BRANCH)
	public String remove(
			@RequestParam(value = "objId", required = false) Long[] id) {
		// 总部不允许删

		for (Long i : id) {
			if (branchDao.isHeadQuarter(i)) {
				Pe.raise("总部数据不可删除");
			}
		}

		branchDao.deleteById(id);
		return ViewResult.JSON_SUCCESS;
	}

	@RequestMapping
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	@ResponseBody
	@Barrier(PurviewConstants.BRANCH)
	public JsonResult get(@RequestParam("id") Long id) {
		Branch u = branchDao.findById(id);
		return JsonResult.s(MAPPING_FULL.toMap(u));
	}

	@RequestMapping
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	@ResponseBody
	@Barrier(PurviewConstants.BRANCH)
	public JsonResult getByNo(@RequestParam("no") String no) {
		Branch u = branchDao.findByNo(no);
		if (u == null) {
			Pe.raise("找不到指定的编号:" + no);
		}
		return JsonResult.s(MAPPING_FULL.toMap(u));
	}

	@RequestMapping
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	@Barrier(PurviewConstants.BRANCH)
	public ModelAndView find(
			@RequestParam(value = "no", required = false) String no) {
		ModelAndView m = new ModelAndView(ViewResult.JSON_LIST);
		List<Branch> list = branchDao.loadByKeywords(no);
		logger.debug("list.size=" + list.size());
		m.addObject("list", list);
		return m;
	}

	@Transactional
	@RequestMapping
	@Barrier(PurviewConstants.BRANCH)
	public String enable(
			@RequestParam(value = "objId", required = false) Long[] id,
			@RequestParam(value = "enable", required = false) boolean enable) {

		for (Long i : id) {
			if (branchDao.isHeadQuarter(i)) {
				Pe.raise("总部数据不可禁用或启用");
			}
		}
		branchDao.doEnable(id, enable);
		return ViewResult.JSON_SUCCESS;
	}
}
