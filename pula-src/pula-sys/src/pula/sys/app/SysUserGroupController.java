package pula.sys.app;

import java.util.ArrayList;
import java.util.Iterator;
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

import puerta.PuertaWeb;
import puerta.support.PaginationSupport;
import puerta.support.ViewResult;
import puerta.support.annotation.Barrier;
import puerta.support.annotation.ObjectParam;
import puerta.support.utils.WxlSugar;
import puerta.support.vo.NoNameVo;
import puerta.support.vo.TreeNodeDTO;
import puerta.system.base.PurviewJSon;
import puerta.system.dao.ActorPurviewDao;
import puerta.system.dao.ModuleDao;
import puerta.system.dao.PurviewDao;
import puerta.system.keeper.ParameterKeeper;
import puerta.system.po.Module;
import puerta.system.po.Purview;
import puerta.system.service.SessionService;
import puerta.system.vo.JsonResult;
import puerta.system.vo.YuiResult;
import puerta.system.vo.YuiResultMapper;
import pula.sys.BhzqConstants;
import pula.sys.PurviewConstants;
import pula.sys.conditions.SysUserGroupCondition;
import pula.sys.daos.SysUserDao;
import pula.sys.daos.SysUserGroupDao;
import pula.sys.domains.SysUserGroup;
import pula.sys.forms.SysUserGroupForm;

@Controller
public class SysUserGroupController {

	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger
			.getLogger(SysUserGroupController.class);
	private static final YuiResultMapper<SysUserGroup> MAPPING = new YuiResultMapper<SysUserGroup>() {
		@Override
		public Map<String, Object> toMap(SysUserGroup obj) {
			Map<String, Object> m = WxlSugar.newHashMap();
			m.put("no", obj.getNo());
			m.put("id", obj.getId());
			m.put("name", obj.getName());
			m.put("enabled", obj.isEnabled());
			return m;
		}
	};

	private static final YuiResultMapper<SysUserGroup> MAPPING_FULL = new YuiResultMapper<SysUserGroup>() {
		@Override
		public Map<String, Object> toMap(SysUserGroup obj) {
			Map<String, Object> m = MAPPING.toMap(obj);
			// if (obj.getManager() != null) {
			// m.put("managerNo", obj.getManager().getLoginId());
			// }
			//
			// List<NoNameVo> rs = WxlSugar.newArrayList();
			// if (obj.getRoles() != null) {
			// for (SysUserRole r : obj.getRoles()) {
			// SysRole sr = r.getRole();
			// rs.add(NoNameVo.create(sr.getId(), sr.getNo(), sr.getName()));
			// }
			// }
			// m.put("roles", rs);

			return m;
		}
	};

	@Resource
	ParameterKeeper parameterKeeper;
	@Resource
	SessionService sessionService;
	@Resource
	private PurviewDao purviewDao;
	@Resource
	private ActorPurviewDao actorPurviewDao;
	@Resource
	private ModuleDao moduleDao;
	@Resource
	SysUserGroupDao sysUserGroupDao;
	@Resource
	SysUserDao sysUserDao;

	@RequestMapping
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	@Barrier(PurviewConstants.SYS_USER_GROUP)
	public ModelAndView entry(
			@ObjectParam("condition") SysUserGroupCondition condition) {
		if (condition == null) {
			condition = new SysUserGroupCondition();
		}
		// List<SysRole> roles = sysRoleDao.loadAll(PuertaWeb.AS_WEBUSER);

		List<SysUserGroup> groups = sysUserGroupDao.loadEnabled();
		return new ModelAndView().addObject("condition", condition).addObject(
				"groups", groups);
	}

	@RequestMapping
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	@ResponseBody
	@Barrier(PurviewConstants.SYS_USER_GROUP)
	public YuiResult list(
			@ObjectParam("condition") SysUserGroupCondition condition,
			@RequestParam(value = "pageIndex", required = false, defaultValue = "0") int pageIndex) {
		if (condition == null) {
			condition = new SysUserGroupCondition();
		}
		PaginationSupport<SysUserGroup> results = null;
		results = sysUserGroupDao.search(condition, pageIndex);
		return YuiResult.create(results, MAPPING);
	}

	@RequestMapping
	@Barrier(PurviewConstants.SYS_USER_GROUP)
	public String create() {
		return null;
	}

	@RequestMapping
	@Transactional
	@Barrier(PurviewConstants.SYS_USER_GROUP)
	public String _create(@ObjectParam("sysUserGroup") SysUserGroupForm cli) {

		sysUserGroupDao.save(cli.toSysUserGroup());
		return ViewResult.JSON_SUCCESS;
	}

	@RequestMapping
	@Transactional
	@Barrier(PurviewConstants.SYS_USER_GROUP)
	public String _update(@ObjectParam("sysUserGroup") SysUserGroupForm cli) {
		sysUserGroupDao.update(cli.toSysUserGroup());
		return ViewResult.JSON_SUCCESS;
	}

	@Transactional
	@RequestMapping
	@Barrier(PurviewConstants.SYS_USER_GROUP)
	public String remove(
			@RequestParam(value = "objId", required = false) String[] id) {
		sysUserGroupDao.deleteById(id);
		return ViewResult.JSON_SUCCESS;
	}

	@RequestMapping
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	@ResponseBody
	@Barrier(PurviewConstants.SYS_USER_GROUP)
	public JsonResult get(@RequestParam("id") String id) {
		SysUserGroup u = sysUserGroupDao.findById(id);
		return JsonResult.s(MAPPING_FULL.toMap(u));
	}

	@Transactional
	@RequestMapping
	@Barrier(PurviewConstants.SYS_USER_GROUP)
	public String enable(
			@RequestParam(value = "objId", required = false) String[] id,
			@RequestParam(value = "enable", required = false) boolean enable) {
		sysUserGroupDao.doEnable(id, enable);
		return ViewResult.JSON_SUCCESS;
	}

	@RequestMapping
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	@Barrier(PurviewConstants.SYS_USER_GROUP)
	public ModelAndView purview(@RequestParam(value = "id") String id) {

		SysUserGroup user = sysUserGroupDao.findById(id);

		// purview by user
		List<Purview> purviews = purviewDao.loadAll(PuertaWeb.AS_WEBUSER);

		logger.debug("user purview.size=" + purviews.size());

		List<Module> modules = moduleDao.loadAll(PuertaWeb.AS_WEBUSER);
		List<Purview> checkedPurviews = this.actorPurviewDao.loadByActor(id,
				PuertaWeb.AS_WEBUSER);

		logger.debug("checkedPurviews.size=" + checkedPurviews.size());

		Map<String, Object> map = WxlSugar.newHashMap();
		List<TreeNodeDTO> roots = new ArrayList<TreeNodeDTO>();
		List<TreeNodeDTO> menus = new ArrayList<TreeNodeDTO>();
		transferModules(modules, roots, map);
		transferPurviews(purviews, map);
		transferCheckedPurviews(checkedPurviews, map);

		for (Iterator<TreeNodeDTO> iter = roots.iterator(); iter.hasNext();) {
			TreeNodeDTO dto = (TreeNodeDTO) iter.next();
			if (dto.getNodes().size() > 0) {
				menus.add(dto);
			}
		}

		// this.menus = roots;
		String json = makeJson(menus);

		return new ModelAndView().addObject("group", user)
				.addObject("json", json).addObject("id", id);
	}

	@Transactional
	@RequestMapping
	@Barrier(PurviewConstants.SYS_USER_GROUP)
	public String _purview(@RequestParam("pid") String pid,
			@RequestParam("id") String id) {
		String[] objId = StringUtils.split(pid, ",");
		logger.debug("length=" + objId.length);
		// 先把他关联到组里面
		actorPurviewDao.doAssign(id, objId);

		// 然后把所有用户的权限都设置一下，主要是一个叠加效果
		List<String> idList = sysUserDao.listByGroup(id);
		for (String userId : idList) {
			actorPurviewDao.doAssign(userId, objId, BhzqConstants.AP_GROUP);
		}

		return ViewResult.JSON_SUCCESS;
	}

	@RequestMapping
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	@Barrier(PurviewConstants.SYS_USER_GROUP)
	public ModelAndView find(
			@RequestParam(value = "no", required = false) String no) {
		ModelAndView m = new ModelAndView(ViewResult.JSON_LIST);
		List<SysUserGroup> list = null;
		list = sysUserGroupDao.loadByKeywords(no);
		List<NoNameVo> rs = WxlSugar.newArrayList();
		logger.debug("list.size=" + list.size());
		for (SysUserGroup su : list) {
			NoNameVo v = new NoNameVo();
			v.setId(su.getId());
			v.setName(su.getName());
			v.setNo(su.getNo());
			rs.add(v);
		}
		m.addObject("list", rs);
		return m;
	}

	private String makeJson(List<TreeNodeDTO> menus) {
		StringBuilder sb = new StringBuilder();

		sb.append("[\n");

		PurviewJSon.makeJson(sb, menus, true);

		sb.append("\n ]");

		return sb.toString();
	}

	private void transferCheckedPurviews(List<Purview> checkedPurviews,
			Map<String, Object> map) {

		for (Iterator<Purview> iter = checkedPurviews.iterator(); iter
				.hasNext();) {
			Purview p = (Purview) iter.next();

			String key = p.getId();

			TreeNodeDTO dto = (TreeNodeDTO) map.get(key);

			if (dto != null) {
				dto.setChecked(true);
			}

		}
	}

	private void transferPurviews(List<Purview> purviews,
			Map<String, Object> map) {
		for (Iterator<Purview> iter = purviews.iterator(); iter.hasNext();) {
			Purview p = (Purview) iter.next();
			// logger.debug("p.name=" + p.getName());
			addToTree(p, map);
		}

	}

	private void addToTree(Purview p, Map<String, Object> map) {
		TreeNodeDTO thisDto = getDTO(p);
		if (p.getParentPurview() != null) {
			if (map.containsKey(p.getParentPurview().getId())) {
				TreeNodeDTO dto = (TreeNodeDTO) map.get(p.getParentPurview()
						.getId());

				thisDto.setRemark(p.getParentPurview().getId());
				dto.addChildNode(thisDto);
				// return;
			} else {
				// logger.debug("p.name=" + p.getName());
				// logger.debug(";p.getParent=" + p.getParentPurview().getId());
				// logger.debug(";p.getParentName="
				// + p.getParentPurview().getName());
			}

		} else {
			// roots.add(thisDto);

			String key = p.getModule().getId();
			thisDto.setRemark(key);
			TreeNodeDTO dto = (TreeNodeDTO) map.get(key);
			if (dto != null)
				dto.getNodes().add(thisDto);
		}

		map.put(p.getId(), thisDto);

	}

	private void transferModules(List<Module> modules, List<TreeNodeDTO> roots,
			Map<String, Object> map) {
		for (Iterator<Module> iter = modules.iterator(); iter.hasNext();) {
			Module m = (Module) iter.next();
			TreeNodeDTO d = getDTO(m);
			roots.add(d);
			map.put(d.getId(), d);
		}
	}

	private TreeNodeDTO getDTO(Module p) {
		TreeNodeDTO d = new TreeNodeDTO();
		d.setId(p.getId());
		d.setNo(p.getNo());
		d.setName(p.getName());
		return d;
	}

	private TreeNodeDTO getDTO(Purview p) {
		TreeNodeDTO d = new TreeNodeDTO();
		d.setId(p.getId());
		d.setNo(p.getNo());
		d.setName(p.getName());
		d.setRemark(p.getDefaultURL());
		d.setLevel(p.getLevel());
		return d;
	}
}
