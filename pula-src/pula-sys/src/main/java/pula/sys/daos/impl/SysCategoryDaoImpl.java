/**
 * Created on 2008-7-24 03:05:26
 *
 * NILVAR 2008
 * $Id$
 */
package pula.sys.daos.impl;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import puerta.support.PageInfo;
import puerta.support.PaginationSupport;
import puerta.support.Pe;
import puerta.support.dao.HibernateTool;
import puerta.support.mls.Mls;
import puerta.support.utils.WxlSugar;
import puerta.system.base.HibernateGenericDao;
import puerta.system.base.TreePathTool;
import puerta.system.intfs.IWxlPluginSetting;
import puerta.system.intfs.JDOMTransfer;
import puerta.system.service.SettingService;
import puerta.system.vo.MapBean;
import puerta.system.vo.MapList;
import pula.sys.conditions.SysCategoryCondition;
import pula.sys.daos.SysCategoryDao;
import pula.sys.domains.SysCategory;
import pula.sys.transfers.SysCategoryTransfer;

/**
 * 
 * @author tiyi
 * 
 */
@Repository
public class SysCategoryDaoImpl extends
		HibernateGenericDao<SysCategory, String> implements SysCategoryDao,
		IWxlPluginSetting<SysCategory> {

	@Resource
	Mls mls;
	@Resource
	private SettingService settingService;

	/*
	 * (non-Javadoc)
	 * 
	 * @see wxl.mibv5studio.mibv5on.basicinfo.sysCategory.SysCategoryMgr#save
	 * (wxl.mibv5studio.mibv5on.basicinfo.po.SysCategory)
	 */
	@Override
	public SysCategory save(SysCategory rt) {
		if (super.existsNo(rt.getNo())) {
			Mls.raise("existsNo", rt.getNo());
		}
		// SysCategory p = rt.getParentCategory();
		rt.setCreatedTime(Calendar.getInstance());
		rt.setUpdatedTime(Calendar.getInstance());

		TreePathTool.setup(this, rt);

		_save(rt);
		return rt;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wxl.mibv5studio.mibv5on.basicinfo.sysCategory.SysCategoryMgr#search
	 * (wxl.mibv5studio.mibv5on.basicinfo.sysCategory.SysCategoryCondition, int)
	 */
	@Override
	public PaginationSupport<SysCategory> search(
			SysCategoryCondition condition, int pageNo) {
		DetachedCriteria criteria = makeDetachedCriteria(condition);
		return super.findPageByCriteria(criteria, new PageInfo(pageNo),
				Order.asc("indexNo"));
	}

	private DetachedCriteria makeDetachedCriteria(SysCategoryCondition condition) {
		DetachedCriteria dc = super.makeDetachedCriteria(condition);
		if (!HibernateTool.eqIfNotEmpty(dc, "parentCategory.id",
				condition.getParentId())) {
			dc.add(Restrictions.isNull("parentCategory.id"));
		}
		return dc;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wxl.mibv5studio.mibv5on.basicinfo.sysCategory.SysCategoryMgr#update
	 * (wxl.mibv5studio.mibv5on.basicinfo.po.SysCategory)
	 */
	@Override
	public SysCategory update(SysCategory rt) {
		if (super.existsNo(rt.getNo(), rt.getIdentify())) {
			Mls.raise("existsNo", rt.getNo());
		}

		SysCategory po = this.findById(rt.getIdentify());
		po.setName(rt.getName());
		po.setNo(rt.getNo());

		TreePathTool.setup(this, rt, po);

		po.setUpdatedTime(Calendar.getInstance());
		_update(rt);
		return po;
	}

	@Override
	public List<SysCategory> loadByNo() {
		String hql = "select u FROM SysCategory u WHERE u.removed=? ORDER BY u.no ";
		return find(hql, false);
	}

	@Override
	public List<SysCategory> loadByTree() {
		String hql = "select u from SysCategory u where u.removed=? order by u.treePath";
		return find(hql, false);
	}

	@Override
	public List<SysCategory> getUnder(String information) {

		String hql = "select u FROM SysCategory u WHERE u.removed=? AND u.parentCategory.no=? and u.enabled=? ORDER BY u.indexNo ";
		return find(hql, false, information, true);
	}

	@Override
	public List<SysCategory> get() {
		String hql = "select u FROM SysCategory u where u.removed=? AND u.enabled=? AND u.level=? ORDER BY u.treePath";
		return find(hql, false, true, 0);
	}

	@Override
	public Map<String, SysCategory> fetchId(String... no) {
		Map<String, SysCategory> m = WxlSugar.newHashMap();
		String hql = "from SysCategory where no=? AND removed=?";
		for (String s : no) {
			SysCategory sys = findSingle(hql, s, false);
			if (sys == null) {
				Pe.raise("指定系统分类编号未登记:" + s);
			}
			m.put(s, sys);
		}
		return m;
	}

	@Override
	public String getName(String no) {
		String hql = "select u.name from SysCategory u where u.no=? and u.removed=?";
		return findSingle(hql, no, false);
	}

	@Override
	public List<SysCategory> doExports(String afNo) {
		return this.loadByTree();
	}

	@Override
	public void doImport(List<SysCategory> objs, boolean update) {
		for (SysCategory c : objs) {
			boolean created = true;
			if (update) {
				SysCategory po = this.findByNo(c.getNo());
				if (po != null) {
					po.setName(c.getName());
					po.setNo(c.getNo());
					created = false;
					po.setUpdatedTime(Calendar.getInstance());
				} else {
					created = true;
				}

			}

			if (created) {
				SysCategory parent = null;
				if (c.getParent() != null) {
					parent = this.findByNo(c.getParent().getNo());
					c.setParent(parent);
				}
				save(c);
			}
		}
	}

	@Override
	public String getPluginName() {
		return this.getPojoClassName();
	}

	@Override
	public JDOMTransfer<SysCategory> getTransfer() {
		return new SysCategoryTransfer();
	}

	@Override
	public void doClear() {
		String hql = "delete from SysCategory";
		delete(hql);
	}

	@Override
	public void registerSetting() {
		settingService.register((IWxlPluginSetting<SysCategory>) this);
	}

	@Override
	public List<SysCategory> getChildren(String parentNo, String orderBy) {
		if (StringUtils.isEmpty(orderBy)) {
			orderBy = "treePath";
		}
		String hql = "select u from SysCategory u where u.parentCategory.no=? and u.removed=? and u.enabled=? order by u."
				+ orderBy;

		return find(hql, parentNo, false, true);
	}

	@Override
	public List<SysCategory> loadByKeywords(String id, String no) {
		List<Object> values = WxlSugar.newArrayList();
		String hql = "FROM SysCategory WHERE (no LIKE ? or name LIKE ? ) AND removed=? and enabled=? AND parentCategory.no=? ";

		String v = "%" + StringUtils.defaultIfEmpty(no, "") + "%";
		values.add(v);
		values.add(v);
		values.add(false);
		values.add(true);
		values.add(id);

		hql += " ORDER BY treePath";

		return findLimitByQuery(hql, 40, values.toArray());
	}

	@Override
	public SysCategory fetchRefByName(String groupToName, String no) {
		String hql = "select id from SysCategory where name=? and parentCategory.no=? and removed=?";
		String id = findSingle(hql, groupToName, no, false);
		return SysCategory.create(id);
	}

	@Override
	public Map<String, MapBean> map(String parentNo) {
		String sql = "select u.no as no,u.id as id from SysCategory u where u.parentCategory.no=?"
				+ " and u.removed=? and u.enabled=? ORDER BY u.indexNo ";
		MapList ml = mapList(sql, parentNo, false, true);
		return ml.toMap("no", true);
	}

	@Override
	public List<SysCategory> getUnderById(String id) {

		String hql = "select u FROM SysCategory u WHERE u.removed=? AND u.parentCategory.id=? and u.enabled=? ORDER BY u.indexNo ";
		return find(hql, false, id, true);
	}

}
