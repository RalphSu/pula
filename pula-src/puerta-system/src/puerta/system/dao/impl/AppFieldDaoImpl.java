package puerta.system.dao.impl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.springframework.stereotype.Repository;

import puerta.support.PageInfo;
import puerta.support.PaginationSupport;
import puerta.support.mls.Mls;
import puerta.support.utils.StringTool;
import puerta.support.utils.WxlSugar;
import puerta.system.base.HibernateGenericDao;
import puerta.system.condition.AppFieldCondition;
import puerta.system.dao.AppFieldDao;
import puerta.system.dao.LoggerDao;
import puerta.system.intfs.JDOMTransfer;
import puerta.system.po.AppField;
import puerta.system.transfer.AppFieldTransfer;
import puerta.system.vo.AppFieldData;

@Repository
public class AppFieldDaoImpl extends HibernateGenericDao<AppField, String>
		implements AppFieldDao {

	@Resource
	private LoggerDao loggerDao;
	@Resource
	private Mls mls;

	@Override
	public AppField save(AppField af) {
		getHibernateTemplate().save(af);
		loggerDao.doLog("新增应用领域", af);
		return af;
	}

	@Override
	public Map<String, AppFieldData> loadPathMap() {
		String hql = "select no,path from AppField where removed=? ";
		List<Object[]> objsList = find(hql, false);
		Map<String, AppFieldData> map = new HashMap<String, AppFieldData>();
		for (Object[] objs : objsList) {
			String m = (String) objs[0];
			String p = (String) objs[1];
			map.put(p, AppFieldData.create(p, m));
		}
		return map;
	}

	public AppField update(AppField m) {
		// Map<String, Object> v = new HashMap<String, Object>();
		// v.put("no", m.getNo());
		//
		// Map<String, Object> filter = new HashMap<String, Object>();
		// filter.put("id", m.getId());

		// if (this.exists(v, filter))
		// Mls.raise("platform.appfield.noExists", m.getNo());

		// m.setNo(m.getNo());
		// m.setName(m.getName());
		AppField po = null;
		if (StringUtils.isEmpty(m.getId())) {
			po = this.findByNo(m.getNo());
		} else {
			po = this.findById(m.getId());
		}

		po.setNo(m.getNo());
		po.setComments(m.getComments());
		po.setIndexNo(m.getIndexNo());
		po.setName(m.getName());
		po.setRemoved(m.isRemoved());
		po.setPath(m.getPath());

		loggerDao.doLog(mls.t("platform.appfield.edit"), StringTool.getBean(m));
		return m;
	}

	@Override
	public List<AppField> loadByIndexNo() {
		String hql = "select u FROM AppField u WHERE u.removed=? ORDER BY u.indexNo desc ";
		return find(hql, false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wxlplatform.appfield.AppFieldDao#doClear()
	 */
	public void doClear() {
		String hql = "DELETE FROM AppField p";
		super.delete(hql);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wxlplatform.appfield.AppFieldDao#doImport(java.util .List, boolean)
	 */
	public void doImport(List<AppField> ases, boolean b) {
		for (Iterator<AppField> iter = ases.iterator(); iter.hasNext();) {
			if (!b) {
				this.save(iter.next());
			} else {
				updateByNoOrSave(iter.next());
			}
		}

	}

	/**
	 * @param next
	 */
	private void updateByNoOrSave(AppField next) {
		AppField po = this.findByNo(next.getNo());
		if (po == null) {
			save(next);
		} else {
			next.setId(po.getId());
			update(next);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wxlplatform.setting.IWxlPluginSetting#doExports()
	 */

	public List<AppField> doExports(String appFieldNo) {
		if (StringUtils.isEmpty(appFieldNo)) {
			return loadAll();
		} else {
			List<AppField> fields = WxlSugar.newArrayList();
			AppField af = findByNo(appFieldNo);
			fields.add(af);
			return fields;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wxlplatform.setting.IWxlPluginSetting#getName()
	 */
	public String getPluginName() {
		return this.getPojoClassName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wxlplatform.setting.IWxlPluginSetting#getTransfer()
	 */
	public JDOMTransfer<AppField> getTransfer() {
		return new AppFieldTransfer();
	}

	@Override
	public PaginationSupport<AppField> search(AppFieldCondition condition,
			int pageIndex) {

		DetachedCriteria criteria = makeDetachedCriteria(condition);
		return super.findPageByCriteria(criteria, new PageInfo(pageIndex),
				Order.asc("indexNo"));
	}

}
