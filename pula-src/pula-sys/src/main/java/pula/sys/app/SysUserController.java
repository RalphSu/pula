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
import puerta.support.Pe;
import puerta.support.ViewResult;
import puerta.support.annotation.Barrier;
import puerta.support.annotation.ObjectParam;
import puerta.support.dao.LoggablePo;
import puerta.support.service.SessionBox;
import puerta.support.utils.WxlSugar;
import puerta.support.vo.Mix;
import puerta.support.vo.NoNameVo;
import puerta.support.vo.TreeNodeDTO;
import puerta.system.base.PurviewJSon;
import puerta.system.dao.ActorPurviewDao;
import puerta.system.dao.LoggerDao;
import puerta.system.dao.ModuleDao;
import puerta.system.dao.PurviewDao;
import puerta.system.dao.SysRoleDao;
import puerta.system.keeper.ParameterKeeper;
import puerta.system.po.Module;
import puerta.system.po.Purview;
import puerta.system.po.SysRole;
import puerta.system.service.SessionService;
import puerta.system.vo.JsonResult;
import puerta.system.vo.MapList;
import puerta.system.vo.YuiResult;
import puerta.system.vo.YuiResultMapper;
import pula.sys.BhzqConstants;
import pula.sys.PurviewConstants;
import pula.sys.conditions.SysUserCondition;
import pula.sys.daos.BranchDao;
import pula.sys.daos.SysUserDao;
import pula.sys.daos.SysUserGroupDao;
import pula.sys.domains.SysUser;
import pula.sys.domains.SysUserGroup;
import pula.sys.forms.SysUserForm;
import pula.sys.helpers.HgMenuHelper;
import pula.sys.helpers.PurviewHelper;
import pula.sys.services.SessionUserService;

@Controller
public class SysUserController {

	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger
			.getLogger(SysUserController.class);
	private static final YuiResultMapper<SysUser> MAPPING = new YuiResultMapper<SysUser>() {
		@Override
		public Map<String, Object> toMap(SysUser obj) {
			Map<String, Object> m = WxlSugar.newHashMap();
			m.put("loginId", obj.getLoginId());
			m.put("id", obj.getId());
			m.put("name", obj.getName());
			m.put("enabled", obj.isEnabled());
			// m.put("printer", obj.getPrinter());
			// m.put("camNo", obj.getCamNo());
			if (obj.getRole() != null) {
				m.put("roleName", obj.getRole().getName());

				m.put("roleNo", obj.getRole().getNo());
			}
			// m.put("role", obj.getRoleName());
			if (obj.getBelongsToGroup() != null) {
				m.put("groupName", obj.getBelongsToGroup().getName());

			}
			// m.put("positionName", obj.getPositionName());
			return m;
		}
	};

	private static final YuiResultMapper<SysUser> MAPPING_FULL = new YuiResultMapper<SysUser>() {
		@Override
		public Map<String, Object> toMap(SysUser obj) {
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
			if (obj.getRole() != null) {
				m.put("roleId", obj.getRole().getId());
			}
			if (obj.getBelongsToGroup() != null) {

				m.put("groupId", obj.getBelongsToGroup().getId());
			}

			if (obj.getBranch() != null) {
				m.put("branchId", obj.getBranch().getId());
			}

			// m.put("position", obj.getPosition());

			return m;
		}
	};

	@Resource
	SysUserDao sysUserDao;
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
	private SysRoleDao sysRoleDao;
	@Resource
	LoggerDao loggerDao;
	@Resource
	SessionUserService sessionUserService;
	@Resource
	BranchDao branchDao;

	@RequestMapping
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	@Barrier(PurviewConstants.SYS_USER)
	public ModelAndView entry(
			@ObjectParam("condition") SysUserCondition condition) {
		if (condition == null) {
			condition = new SysUserCondition();
		}

		// SelectOptionList list = AlertsHelper.typeList();
		// SelectOptionList positions = SysUserHelper.getPositions(0);

		List<SysRole> roles = sysRoleDao.loadAll(PuertaWeb.AS_WEBUSER);
		List<SysUserGroup> groups = sysUserGroupDao.loadEnabled();
		MapList branches = branchDao.loadMeta();
		return new ModelAndView().addObject("condition", condition)
				.addObject("groups", groups).addObject("roles", roles)
				.addObject("admin", sessionUserService.admin())
				.addObject("branches", branches);

	}

	@RequestMapping
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	@ResponseBody
	@Barrier(PurviewConstants.SYS_USER)
	public YuiResult list(
			@ObjectParam("condition") SysUserCondition condition,
			@RequestParam(value = "pageIndex", required = false, defaultValue = "0") int pageIndex) {
		if (condition == null) {
			condition = new SysUserCondition();
		}
		PaginationSupport<SysUser> results = null;
		results = sysUserDao.search(condition, pageIndex);
		return YuiResult.create(results, MAPPING);
	}

	@RequestMapping
	@Barrier(PurviewConstants.SYS_USER)
	public String create() {
		return null;
	}

	@RequestMapping
	@Transactional
	@Barrier(PurviewConstants.SYS_USER)
	public String _create(@ObjectParam("sysUser") SysUserForm cli) {

		SysUser su = sysUserDao.save(cli.toSysUser());
		// subscribeAlertsDao.assign(su.getId(), cli.getTypes());

		// 用户组权限匹配一下
		String[] objId = actorPurviewDao.loadIdByActor(cli.getGroupId(),
				PuertaWeb.AS_WEBUSER);

		actorPurviewDao.doAssign(su.getId(), objId, BhzqConstants.AP_GROUP);
		return ViewResult.JSON_SUCCESS;
	}

	@RequestMapping
	@Transactional
	@Barrier(PurviewConstants.SYS_USER)
	public String _update(@ObjectParam("sysUser") SysUserForm cli) {
		Mix<SysUser, Boolean> mix = sysUserDao.update(cli.toSysUser(),
				cli.isChangePassword());

		// subscribeAlertsDao.clear(cli.getId());
		// subscribeAlertsDao.assign(cli.getId(), cli.getTypes());

		if (mix.object2) {
			// 重新设置组权限(可能和当前已经设定的个人权限会冲突）
			String[] objId = actorPurviewDao.loadIdByActor(cli.getGroupId(),
					PuertaWeb.AS_WEBUSER);

			actorPurviewDao
					.doAssign(cli.getId(), objId, BhzqConstants.AP_GROUP);
		}

		return ViewResult.JSON_SUCCESS;
	}

	@Transactional
	@RequestMapping
	@Barrier(PurviewConstants.SYS_USER)
	public String remove(
			@RequestParam(value = "objId", required = false) String[] id) {
		sysUserDao.deleteById(id);
		return ViewResult.JSON_SUCCESS;
	}

	@RequestMapping
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	@ResponseBody
	@Barrier(PurviewConstants.SYS_USER)
	public JsonResult get(@RequestParam("id") String id) {
		SysUser u = sysUserDao.findById(id);
		Map<String, Object> m = (MAPPING_FULL.toMap(u));

		// List<Integer> type = subscribeAlertsDao.list(id);

		// m.put("types", type);

		return JsonResult.s(m);
	}

	@Transactional
	@RequestMapping
	@Barrier(PurviewConstants.SYS_USER)
	public String enable(
			@RequestParam(value = "objId", required = false) String[] id,
			@RequestParam(value = "enable", required = false) boolean enable) {
		sysUserDao.doEnable(id, enable);
		return ViewResult.JSON_SUCCESS;
	}

	@RequestMapping
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	@Barrier(PurviewConstants.SYS_USER)
	public ModelAndView purview(@RequestParam(value = "id") String id) {

		SysUser user = sysUserDao.findById(id);

		// purview by user
		List<Purview> purviews = purviewDao.loadAll(PuertaWeb.AS_WEBUSER);

		logger.debug("user purview.size=" + purviews.size());

		List<Module> modules = moduleDao.loadAll(PuertaWeb.AS_WEBUSER);
		List<Object[]> checkedPurviews = this.actorPurviewDao
				.loadByActorWithDataFrom(id, PuertaWeb.AS_WEBUSER);

		logger.debug("checkedPurviews.size=" + checkedPurviews.size());

		Map<String, Object> map = WxlSugar.newHashMap();
		List<TreeNodeDTO> roots = new ArrayList<TreeNodeDTO>();
		List<TreeNodeDTO> menus = new ArrayList<TreeNodeDTO>();
		PurviewHelper.transferModules(modules, roots, map);
		PurviewHelper.transferPurviews(purviews, map);
		PurviewHelper.transferCheckedPurviews(checkedPurviews, map);

		for (Iterator<TreeNodeDTO> iter = roots.iterator(); iter.hasNext();) {
			TreeNodeDTO dto = (TreeNodeDTO) iter.next();
			if (dto.getNodes().size() > 0) {
				menus.add(dto);
			}
		}

		// this.menus = roots;
		String json = makeJson(menus);

		return new ModelAndView().addObject("sysUser", user)
				.addObject("json", json).addObject("id", id);
	}

	@Transactional
	@RequestMapping
	@Barrier(PurviewConstants.SYS_USER)
	public String _purview(@RequestParam("pid") String pid,
			@RequestParam("id") String id) {
		String[] objId = StringUtils.split(pid, ",");
		logger.debug("length=" + objId.length);
		actorPurviewDao.doAssign(id, objId, BhzqConstants.AP_USER);

		loggerDao.doLog("设置权限", (LoggablePo) sysUserDao.findById(id));

		return ViewResult.JSON_SUCCESS;
	}

	@RequestMapping
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	@Barrier(PurviewConstants.SYS_USER)
	public ModelAndView find(
			@RequestParam(value = "no", required = false) String no,
			@RequestParam(value = "roleNo", required = false) String roleNo) {
		ModelAndView m = new ModelAndView(ViewResult.JSON_LIST);
		List<SysUser> list = null;
		long bid = sessionUserService.getBranch().getIdLong();
		if (StringUtils.isEmpty(roleNo)) {
			list = sysUserDao.loadByKeywords(no, bid);
		} else {
			// list = sysUserDao.loadByKeywords(no, roleNo, bid);
		}
		List<NoNameVo> rs = WxlSugar.newArrayList();
		logger.debug("list.size=" + list.size());
		for (SysUser su : list) {
			NoNameVo v = new NoNameVo();
			v.setId(su.getId());
			v.setName(su.getName());
			v.setNo(su.getLoginId());
			rs.add(v);
		}
		m.addObject("list", rs);
		return m;
	}

	@RequestMapping
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	@Barrier()
	public ModelAndView findSalesman(
			@RequestParam(value = "no", required = false) String no) {
		ModelAndView m = new ModelAndView(ViewResult.JSON_LIST);

		long bid = sessionUserService.getBranch().getIdLong();
		if (sessionUserService.isHeadquarter()) {
			bid = 0;
		}
		MapList list = sysUserDao.loadByKeywords(no, BhzqConstants.ROLE_SALES,
				bid);

		// List<NoNameVo> rs = WxlSugar.newArrayList();
		// logger.debug("list.size=" + list.size());
		// for (MapBean su : list) {
		// NoNameVo v = new NoNameVo();
		// v.setId(su.getId());
		// v.setName(su.getName());
		// v.setNo(su.getLoginId());
		// rs.add(v);
		// }
		m.addObject("list", list);
		return m;
	}

	private String makeJson(List<TreeNodeDTO> menus) {
		StringBuilder sb = new StringBuilder();

		sb.append("[\n");

		PurviewJSon.makeJson(sb, menus, true);

		sb.append("\n ]");

		return sb.toString();
	}

	@RequestMapping
	@ResponseBody
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	@Barrier(PurviewConstants.SYS_USER)
	public JsonResult beHim(@RequestParam("id") String id) {

		if (!sessionUserService.admin()) {
			Pe.raise("只有管理员方可转换");
		}

		// 除系统管理员外，其他人不能转，就是只能转一次
		SessionBox su = sessionService.get();
		SysUser a = sysUserDao.loadWithRole(id);
		// 角色转换

		String menu = HgMenuHelper.toString(
				actorPurviewDao.loadMenuList(id, PuertaWeb.AS_WEBUSER), true);
		su.setMenu(menu);
		su.setPurviewActorId(id);
		su.setName(su.getName() + "#" + a.getName());
		if (a.getRole() != null) {
			NoNameVo role = NoNameVo.create(a.getRole().getId(), a.getRole()
					.getNo(), a.getRole().getName());
			su.getProps().put(BhzqConstants.SESSION_ROLE, role);
		}
		// // 如果是工组长，还要加载工组类型
		// Integer[] type_sess = workgroupDao.getTypes(a.getId());
		// su.getProps().put(BhzqConstants.SESSION_TYPES, type_sess);
		//
		// su.getProps().put(BhzqConstants.SESSION_CAM_NO, a.getCamNo());
		// su.getProps().put(BhzqConstants.SESSION_PRINTER, a.getPrinter());

		// su.setId(a.getId());
		sessionService.set(su);

		return JsonResult.s();
	}

	/*
	 * @RequestMapping
	 * 
	 * @Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	 * 
	 * @Barrier(PurviewConstants.SYS_USER) public ModelAndView
	 * department(@RequestParam(value = "id") String id) {
	 * 
	 * SysUser user = sysUserDao.findById(id);
	 * 
	 * // purview by user List<Department> depts = departmentDao.loadByTree();
	 * 
	 * List<String> checkedDepts = this.sysUserChargeDepartmentDao
	 * .loadByUser(id);
	 * 
	 * Map<String, Object> map = WxlSugar.newHashMap(); List<TreeNodeDTO> roots
	 * = new ArrayList<TreeNodeDTO>(); // transferModules(modules, roots, map);
	 * 
	 * DepartmentHelper.transferDepartments(depts, roots, map);
	 * DepartmentHelper.transferCheckedDepartments(checkedDepts, map);
	 * 
	 * List<TreeNodeDTO> menus = new ArrayList<TreeNodeDTO>(); for (TreeNodeDTO
	 * dto : roots) { // 和权限不同的是，所有根级都要呈现 // if (dto.getNodes().size() > 0) {
	 * menus.add(dto); // } }
	 * 
	 * // this.menus = roots; String json = makeJson(menus);
	 * 
	 * return new ModelAndView().addObject("sysUser", user) .addObject("json",
	 * json).addObject("id", id); }
	 */

	/*
	 * @Transactional
	 * 
	 * @RequestMapping
	 * 
	 * @Barrier(PurviewConstants.SYS_USER) public String
	 * _department(@RequestParam("pid") String pid,
	 * 
	 * @RequestParam("id") String id) { String[] objId = StringUtils.split(pid,
	 * ","); logger.debug("length=" + objId.length);
	 * sysUserChargeDepartmentDao.assign(id, objId);
	 * 
	 * loggerDao.doLog("设置管理部门", (LoggablePo) sysUserDao.findById(id));
	 * 
	 * return ViewResult.JSON_SUCCESS; }
	 */

}
