/**
 * Created on 2008-3-24 12:01:14
 *
 * NILVAR 2008
 * $Id$
 */
package puerta.system.dao.impl;

import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import puerta.support.mls.Mls;
import puerta.system.base.HibernateGenericDao;
import puerta.system.dao.ParameterPageDao;
import puerta.system.intfs.JDOMTransfer;
import puerta.system.po.AppField;
import puerta.system.po.ParameterPage;
import puerta.system.transfer.ParameterPageTransfer;

/**
 * 
 * @author tiyi
 * 
 */
@Repository
public class ParameterPageDaoImpl extends
		HibernateGenericDao<ParameterPage, String> implements ParameterPageDao {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * wxl.pew.platform.parameterpage.ParameterPageMgr#doAppendPage(java.util
	 * .List)
	 */
	public void doAppendPage(List<ParameterPage> pages) {
		for (Iterator<ParameterPage> iter = pages.iterator(); iter.hasNext();) {
			ParameterPage pp = iter.next();
			// get page

			String no = pp.getAppField().getNo();

			ParameterPage po = this.findByNo(pp.getNo());

			AppField as = (AppField) this.findByNo(AppField.class, no);
			pp.setAppField(as);
			if (po == null) {

				save(pp);
			} else {
				pp.setId(po.getId());
				update(pp);
			}
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * wxl.pew.platform.parameterpage.ParameterPageMgr#doImportPage(java.util
	 * .List)
	 */
	public void doImportPage(List<ParameterPage> pages) {
		for (Iterator<ParameterPage> iter = pages.iterator(); iter.hasNext();) {
			ParameterPage pp = iter.next();
			// get page
			String no = pp.getAppField().getNo();
			AppField as = (AppField) this.findByNo(AppField.class, no);
			pp.setAppField(as);

			save(pp);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wxl.pew.platform.parameterpage.ParameterPageMgr#loadPages()
	 */
	public List<ParameterPage> loadPages() {
		String hql = "FROM ParameterPage u WHERE u.removed=? ORDER BY u.appField.indexNo, u.indexNo ";
		return find(hql, new Object[] { false });
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * wxl.pew.platform.parameterpage.ParameterPageMgr#loadPages(java.lang.String
	 * )
	 */
	public List<ParameterPage> loadPages(String appFieldNo) {
		String hql = "FROM ParameterPage u WHERE u.appField.no=? AND u.removed=? ORDER BY u.indexNo ";
		return find(hql, new Object[] { appFieldNo, false });
		// return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * wxl.pew.platform.parameterpage.ParameterPageMgr#save(wxl.pew.platform
	 * .po.ParameterPage)
	 */
	public ParameterPage save(ParameterPage parameterPage) {
		String hql = "FROM ParameterPage WHERE no=?";
		long c = this.getCountByQuery(hql, parameterPage.getNo());
		if (c > 0) {
			Mls.raise("platform.parameterfolder.noExists",
					parameterPage.getNo());
		}

		getHibernateTemplate().save(parameterPage);

		return parameterPage;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * wxl.pew.platform.parameterpage.ParameterPageMgr#update(wxl.pew.platform
	 * .po.ParameterPage)
	 */
	public ParameterPage update(ParameterPage parameterPage) {
		ParameterPage pp = null;

		if (StringUtils.isEmpty(parameterPage.getId())) {
			pp = findByNo(parameterPage.getNo());
		} else {
			pp = findById(parameterPage.getId());

		}
		String hql = "FROM ParameterPage WHERE no=? AND id<>?";
		long c = this.getCountByQuery(hql, parameterPage.getNo(), pp.getId());
		if (c > 0) {
			Mls.raise("platform.parameterfolder.noExists",
					parameterPage.getNo());
		}

		pp.setIndexNo(parameterPage.getIndexNo());
		pp.setName(parameterPage.getName());
		pp.setNo(parameterPage.getNo());

		return pp;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wxlplatform.parameterpage.ParameterPageMgr#beforeEdit(java.lang.
	 * String)
	 */
	public ParameterPage loadPageAndAppField(String id) {
		ParameterPage pp = this.findById(id);
		getHibernateTemplate().initialize(pp.getAppField());
		return pp;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wxlplatform.setting.IWxlPluginSetting#doClear()
	 */
	public void doClear() {
		String hql = "DELETE FROM ParameterPage";
		delete(hql);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wxlplatform.setting.IWxlPluginSetting#doExports()
	 */
	public List<ParameterPage> doExports(String asNo) {
		if (StringUtils.isEmpty(asNo)) {

			List<ParameterPage> ret = loadAll();
			for (ParameterPage p : ret) {
				getHibernateTemplate().initialize(p.getAppField());
			}
			return ret;
		} else {
			return this.loadPages(asNo);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wxlplatform.setting.IWxlPluginSetting#doImport(java.util.List,
	 * boolean)
	 */
	public void doImport(List<ParameterPage> objs, boolean update) {
		if (update) {
			doAppendPage(objs);
		} else {
			doImportPage(objs);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wxlplatform.setting.IWxlPluginSetting#getPluginName()
	 */
	public String getPluginName() {
		return this.getPojoClassName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wxlplatform.setting.IWxlPluginSetting#getTransfer()
	 */
	public JDOMTransfer<ParameterPage> getTransfer() {
		return new ParameterPageTransfer();
	}

}
