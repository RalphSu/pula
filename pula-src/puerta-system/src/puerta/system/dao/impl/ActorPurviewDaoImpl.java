/**
 * Created on 2008-12-20 11:31:34
 *
 * NILVAR 2008
 * $Id$
 */
package puerta.system.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import puerta.support.Pe;
import puerta.support.utils.WxlSugar;
import puerta.system.base.HibernateGenericDao;
import puerta.system.base.PurviewJSon;
import puerta.system.dao.ActorPurviewDao;
import puerta.system.dao.ModuleDao;
import puerta.system.dao.PurviewDao;
import puerta.system.helper.MenuHelper;
import puerta.system.po.ActorPurview;
import puerta.system.po.Module;
import puerta.system.po.Purview;
import puerta.system.vo.MenuVo;

/**
 * 
 * @author tiyi
 * 
 */
@Repository
public class ActorPurviewDaoImpl extends
		HibernateGenericDao<ActorPurview, String> implements ActorPurviewDao {

	@Resource
	private ModuleDao moduleDao;
	@Resource
	private PurviewDao purviewDao;

	public PurviewDao getPurviewDao() {
		return purviewDao;
	}

	public void setPurviewDao(PurviewDao purviewDao) {
		this.purviewDao = purviewDao;
	}

	public ModuleDao getModuleDao() {
		return moduleDao;
	}

	public void setModuleDao(ModuleDao moduleDao) {
		this.moduleDao = moduleDao;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wxlplatform.actorpurview.ActorPurviewDao#loadMenu(java. lang.String)
	 */
	public String loadMenu(String id, String asNo, boolean strict) {
		List<MenuVo> ms = this.loadMenuList(id, asNo);
		return MenuHelper.toString(ms, strict);
	}

	/**
	 * @param id
	 * @return
	 */
	private List<Purview> loadMenus(String id, String asNo, String moduleId) {
		String hql = "SELECT distinct( pp ) FROM ActorPurview p,Purview pp WHERE p.actorId=? AND p.purview.id=pp.id"
				+ " and pp.menuItem=? AND pp.appField.no=? "
				+ " AND pp.module.id=? AND pp.removed=? ORDER BY pp.treePath ";
		return find(hql, id, true, asNo, moduleId, false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wxlplatform.actorpurview.ActorPurviewDao#loadByActor(java
	 * .lang.String)
	 */
	public List<Purview> loadByActor(String id) {
		String hql = "Select rp.purview FROM ActorPurview rp WHERE rp.actorId=?  AND "
				+ "rp.purview.removed=? ORDER BY rp.purview.treePath ";
		return find(hql, false, id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wxlplatform.actorpurview.ActorPurviewDao#loadByActor(java
	 * .lang.String, java.lang.String)
	 */
	public List<Purview> loadByActor(String id, String asNo) {
		String hql = "Select rp.purview FROM ActorPurview rp WHERE rp.actorId=? AND "
				+ " rp.purview.removed=? "
				+ "AND rp.purview.appField.no=? ORDER BY rp.purview.treePath ";
		return find(hql, id, false, asNo);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wxlplatform.actorpurview.ActorPurviewDao#doAssign(java. lang.String,
	 * java.lang.String[])
	 */
	public void doAssign(String actorId, String[] purviewId) {
		String hql = "DELETE FROM ActorPurview p WHERE p.actorId=?";
		super.delete(hql, actorId);

		if (purviewId == null) {
			return;
		}

		for (int i = 0; i < purviewId.length; i++) {
			Purview p = new Purview();
			p.setId(purviewId[i]);
			ActorPurview rp = new ActorPurview();
			rp.setPurview(p);
			rp.setActorId(actorId);
			getHibernateTemplate().save(rp);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wxlplatform.actorpurview.ActorPurviewDao#doAssignAll(java
	 * .lang.String, java.lang.String)
	 */
	public void doAssignAll(String actorId, String applicableNo) {
		String hql = "DELETE FROM ActorPurview ap WHERE ap.actorId=?";
		delete(hql, actorId);

		hql = "FROM Purview u WHERE u.appField.no=? AND u.removed=?";
		List<Purview> l = find(hql, applicableNo, false);

		for (Iterator<Purview> iter = l.iterator(); iter.hasNext();) {
			Purview p = (Purview) iter.next();
			ActorPurview ap = new ActorPurview();
			ap.setPurview(p);
			ap.setActorId(actorId);
			getHibernateTemplate().save(ap);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * wxlplatform.actorpurview.ActorPurviewDao#getPurviews(java.lang.String ,
	 * java.lang.String)
	 */
	public Map<Module, List<Purview>> getPurviews(String id, String asWebuser) {
		List<Purview> purviews = this.loadMenus(id);
		// TreeNodeDTO
		Map<Module, List<Purview>> map = new LinkedHashMap<Module, List<Purview>>();
		for (Purview p : purviews) {
			Module m = p.getModule();
			List<Purview> lst = null;
			if (map.containsKey(m)) {
				lst = map.get(m);
			} else {
				lst = new ArrayList<Purview>();
				map.put(m, lst);
			}
			lst.add(p);
		}

		return map;
	}

	/**
	 * @param id
	 * @return
	 */
	private List<Purview> loadMenus(String id) {
		String hql = "SELECT p.purview FROM ActorPurview p WHERE p.actorId=? AND "
				+ "p.purview.menuItem=? ORDER BY p.purview.treePath ";
		return find(hql, id, true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wxlplatform.actorpurview.ActorPurviewDao#getFunctions(java.lang.
	 * String, java.lang.String)
	 */
	public Map<String, String> getFunctions(String id, String asWebuser) {
		String hql = "SELECT p.purview.no FROM ActorPurview p WHERE p.actorId=? AND "
				+ "p.purview.menuItem=? ORDER BY p.purview.treePath ";
		List<String> list = find(hql, id, false);
		Map<String, String> map = new HashMap<String, String>();
		for (String no : list) {
			map.put(no, no);
		}

		return map;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wxlplatform.actorpurview.ActorPurviewDao#loadMenu(java.lang.String)
	 */
	public String loadMenu(String no, boolean strict) {
		List<Module> modules = moduleDao.loadAll(no);

		StringBuffer sb = new StringBuffer();
		sb.append("").append("[\n");

		// homepage
		sb.append("");

		// module ;
		for (Iterator<Module> iter = modules.iterator(); iter.hasNext();) {
			Module m = iter.next();
			List<Purview> purviews = purviewDao.loadAll(no);
			PurviewJSon.makeMenuTree(purviews, m, sb, "contentFrame", strict);

			if (iter.hasNext()) {
				sb.append(",\n");
			} else {
				sb.append("\n");
			}
		}

		sb.append("];\n");

		logger.debug("js:" + sb.toString());
		return sb.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wxlplatform.actorpurview.ActorPurviewDao#hasPurview(java.lang.String
	 * , java.lang.String)
	 */
	public boolean hasPurview(String purviewNo, String actorId) {
		String hql = "SELECT u.id FROM ActorPurview u WHERE u.purview.no=? AND u.actorId=?";
		String id = findSingle(hql, purviewNo, actorId);

		return id != null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wxlplatform.actorpurview.ActorPurviewDao#getPurviewUnder(java.lang
	 * .String, java.lang.String)
	 */
	public Map<String, String> getPurviewUnder(String no, String actorId) {
		Purview p = purviewDao.findByNo(no);
		if (p == null) {
			Pe.raise("无效的权限编号:" + no);
		}

		String hql = "SELECT distinct u.purview.no FROM ActorPurview u WHERE u.purview.treePath LIKE ? AND u.actorId=?  ";
		List<String> list = find(hql, p.getTreePath() + "%", actorId);
		Map<String, String> mp = WxlSugar.newHashMap();
		for (String s : list) {
			mp.put(s, s);
		}
		return mp;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wxlplatform.actorpurview.ActorPurviewDao#doAssign(java.lang.String,
	 * java.lang.String)
	 */
	public void doAssign(String id, String no) {
		String hql = "SELECT id from Purview WHERE no=? AND removed=?";
		String fid = findSingle(hql, no, false);
		if (fid == null) {
			return;
		}
		ActorPurview ap = new ActorPurview();
		ap.setPurview(new Purview());
		ap.getPurview().setId(fid);
		ap.setActorId(id);
		getHibernateTemplate().save(ap);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wxlplatform.actorpurview.ActorPurviewDao#doAssign(java.lang.String,
	 * java.util.List)
	 */
	public void doAssign(String actorId, List<Purview> purviews) {
		String hql = "DELETE FROM ActorPurview p WHERE p.actorId=?";
		super.delete(hql, actorId);

		if (purviews == null) {
			return;
		}

		for (Purview p : purviews) {

			ActorPurview rp = new ActorPurview();
			rp.setPurview(p);
			rp.setActorId(actorId);
			getHibernateTemplate().save(rp);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wxlplatform.actorpurview.ActorPurviewDao#loadMenuByActor(java.lang
	 * .String)
	 */
	public List<Purview> loadMenuByActor(String id) {
		String hql = "Select rp.purview FROM ActorPurview rp WHERE rp.actorId=?  AND "
				+ "rp.purview.removed=? AND rp.purview.menuItem=? ORDER BY rp.purview.treePath ";
		return find(hql, id, false, true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wxlplatform.actorpurview.ActorPurviewDao#doCopy(java.lang.String,
	 * java.lang.String)
	 */
	public void doCopy(String src, String dest) {
		String hql = "delete from ActorPurview WHERE actorId=?";
		delete(hql, dest);

		// start copy
		hql = "select purview.id from ActorPurview where actorId=?";
		List<String> ids = find(hql, src);
		for (String id : ids) {
			ActorPurview ap = ActorPurview.create(id, dest);
			getHibernateTemplate().save(ap);
		}

	}

	// @Override
	// public List<MenuVo> loadMenuWithGroup(String g, String id, String asNo) {
	// if (g == null) {
	// return loadMenuList(id, asNo);
	// }
	// List<MenuVo> v1 = loadMenuList(g, asNo);
	// List<MenuVo> v2 = loadMenuList(id, asNo);
	//
	// return MenuHelper.mix(v1, v2);
	//
	// }

	public List<MenuVo> loadMenuList(String id, String asNo) {
		List<Module> modules = moduleDao.loadModules(id);
		List<MenuVo> menus = MenuHelper.transferModule(modules); // root

		// module ;
		for (Module m : modules) {
			List<Purview> purviews = this.loadMenus(id, asNo, m.getId());
			menus.addAll(MenuHelper.transferPurview(purviews));
		}

		return menus;
	}

	//
	// @Override
	// public void doAttach(String id, String[] objId, Integer dataFrom) {
	// if (objId == null || objId.length == 0)
	// return;
	//
	// // clear the dataFrom
	// Map<String, String> map = null;
	// if (dataFrom != null) {
	// String hql =
	// "select purview.id,id from ActorPurview where actorId=? and dataFrom=?";
	// map = HibernateTool.asMap(find(hql, id, dataFrom));
	// } else {
	// // empty;
	// map = WxlSugar.newHashMap();
	// }
	//
	// String hql =
	// "select id from ActorPurview where actorId=? and purview.id=? and dataForm=?";
	// for (String pid : objId) {
	// if (exists(hql, id, pid)) {
	// continue;
	// }
	// ActorPurview ap = ActorPurview.create(pid, id);
	// ap.setDataFrom(dataFrom);
	// getHibernateTemplate().save(ap);
	//
	// // remove one
	// map.remove(pid);
	// }
	//
	// // 这个都是多余的
	// hql = "delete from ActorPurview where id=?";
	// // left force to clear
	// for (String k : map.keySet()) {
	// String apId = map.get(k);
	// delete(hql, apId);
	// }
	//
	// }

	@Override
	public void doAssign(String actorId, String[] purviewId, Integer dataFrom) {
		if (dataFrom != null) {
			// 说明是有绑定关系的， 所以null的应该是从insider里面产生的
			String sq = "DELETE FROM ActorPurview p WHERE p.actorId=? and dataFrom is null";
			delete(sq, actorId);
		}

		String hql = "DELETE FROM ActorPurview p WHERE p.actorId=? and dataFrom=?";
		super.delete(hql, actorId, dataFrom);

		if (purviewId == null) {
			return;
		}

		for (String pid : purviewId) {
			ActorPurview rp = ActorPurview.create(pid, actorId);

			rp.setDataFrom(dataFrom);
			getHibernateTemplate().save(rp);
		}

	}

	@Override
	public List<Object[]> loadByActorWithDataFrom(String id, String asNo) {
		String hql = "Select rp.purview,rp.dataFrom FROM ActorPurview rp WHERE rp.actorId=? AND "
				+ " rp.purview.removed=? "
				+ "AND rp.purview.appField.no=? ORDER BY rp.purview.treePath ";
		return find(hql, id, false, asNo);
	}

	@Override
	public String[] loadIdByActor(String id, String asNo) {

		String hql = "Select rp.purview.id FROM ActorPurview rp WHERE rp.actorId=? AND "
				+ " rp.purview.removed=? "
				+ "AND rp.purview.appField.no=? ORDER BY rp.purview.treePath ";
		List<String> pids = find(hql, id, false, asNo);

		return pids.toArray(new String[pids.size()]);
	}
}
