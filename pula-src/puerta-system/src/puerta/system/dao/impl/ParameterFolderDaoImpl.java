/**
 * Created on 2008-3-24 12:06:39
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

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import puerta.support.mls.Mls;
import puerta.system.base.HibernateGenericDao;
import puerta.system.dao.ParameterFolderDao;
import puerta.system.dao.ParameterPageDao;
import puerta.system.intfs.JDOMTransfer;
import puerta.system.po.AppField;
import puerta.system.po.ParameterFolder;
import puerta.system.po.ParameterPage;
import puerta.system.transfer.ParameterFolderTransfer;
import puerta.system.vo.ParameterFolderWithPage;

/**
 * 
 * @author tiyi
 * 
 */
@Repository
public class ParameterFolderDaoImpl extends
		HibernateGenericDao<ParameterFolder, String> implements
		ParameterFolderDao {
	@Resource
	private ParameterPageDao parameterPageMgr;

	/*
	 * (non-Javadoc)
	 * 
	 * @see wxl.pew.platform.parameterfolder.ParameterFolderMgr#doAppendFolder
	 * (java.util.List)
	 */
	@Override
	public void doAppendFolder(List<ParameterFolder> folders) {
		for (Iterator<ParameterFolder> iter = folders.iterator(); iter
				.hasNext();) {
			ParameterFolder pf = iter.next();
			ParameterFolder po = (ParameterFolder) this.findByNo(
					ParameterFolder.class, pf.getNo());
			// get page
			String no = pf.getPage().getNo();
			ParameterPage page = (ParameterPage) this.findByNo(
					ParameterPage.class, no);
			pf.setPage(page);
			pf.setAppField(page.getAppField());
			if (po == null) {

				save(pf);
			} else {
				pf.setId(po.getId());
				update(pf);
			}

		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wxl.pew.platform.parameterfolder.ParameterFolderMgr#doImportFolder
	 * (java.util.List)
	 */
	public void doImportFolder(List<ParameterFolder> folders) {
		for (Iterator<ParameterFolder> iter = folders.iterator(); iter
				.hasNext();) {
			ParameterFolder pf = iter.next();

			// get page
			String no = pf.getPage().getNo();
			ParameterPage page = super.findByNo(ParameterPage.class, no);
			pf.setPage(page);
			pf.setAppField(page.getAppField());

			save(pf);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wxl.pew.platform.parameterfolder.ParameterFolderMgr#loadFolders
	 * (java.lang.String)
	 */
	public List<ParameterFolder> loadFolders(String parameterPageId) {
		String hql = "FROM ParameterFolder u WHERE u.page.id=? AND u.removed=? ORDER BY u.indexNo ";
		return find(hql, new Object[] { parameterPageId, false });
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seewxl.pew.platform.parameterfolder.ParameterFolderMgr#
	 * loadFoldersByAppField(java.lang.String)
	 */
	public Map<String, List<ParameterFolder>> loadFoldersByAppField(
			String appFieldNo) {
		String hql = "FROM ParameterFolder u WHERE u.appField.no=? AND u.removed=? ORDER BY u.page.id,u.indexNo";
		List<ParameterFolder> l = find(hql, new Object[] { appFieldNo, false });
		HashMap<String, List<ParameterFolder>> m = new LinkedHashMap<String, List<ParameterFolder>>();
		for (Iterator<ParameterFolder> iter = l.iterator(); iter.hasNext();) {
			ParameterFolder p = (ParameterFolder) iter.next();
			String key = p.getPage().getId();
			List<ParameterFolder> folders = null;
			if (m.containsKey(key)) {
				folders = (ArrayList<ParameterFolder>) m.get(key);
			} else {
				folders = new ArrayList<ParameterFolder>();
				m.put(key, folders);
			}

			folders.add(p);
		}

		return m;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wxl.pew.platform.parameterfolder.ParameterFolderMgr#save(wxl
	 * .pew.platform.po.ParameterFolder)
	 */
	public ParameterFolder save(ParameterFolder parameterFolder) {
		String hql = "FROM ParameterFolder WHERE no=?";
		long c = this.getCountByQuery(hql, parameterFolder.getNo());
		if (c > 0) {
			Mls.raise("platform.parameterfolder.noExists",
					parameterFolder.getNo());
		}

		getHibernateTemplate().save(parameterFolder);

		return parameterFolder;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wxl.pew.platform.parameterfolder.ParameterFolderMgr#update(com
	 * .nilvar.pew.platform.po.ParameterFolder)
	 */
	public ParameterFolder update(ParameterFolder parameterFolder) {
		ParameterFolder pf = null;
		if (!StringUtils.isEmpty(parameterFolder.getId())) {
			pf = findById(parameterFolder.getId());
		} else {
			pf = findByNo(parameterFolder.getNo());
		}

		String hql = "FROM ParameterFolder WHERE no=? AND id<>? and removed=?";
		long c = this.getCountByQuery(hql, parameterFolder.getNo(), pf.getId(),
				false);
		if (c > 0) {
			Mls.raise("platform.parameterfolder.noExists",
					parameterFolder.getNo());
		}

		pf.setIndexNo(parameterFolder.getIndexNo());
		pf.setName(parameterFolder.getName());
		pf.setNo(parameterFolder.getNo());

		pf.setPage(parameterFolder.getPage());
		return pf;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wxl.pew.platform.parameterfolder.ParameterFolderMgr#loadFolders()
	 */
	public List<ParameterFolder> loadFolders() {
		String hql = "FROM ParameterFolder u WHERE u.removed=? ORDER BY appField.indexNo,u.indexNo ";
		return find(hql, new Object[] { false });
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wxlplatform.parameterfolder.ParameterFolderMgr#beforeEdit(java.lang
	 * .String)
	 */
	public ParameterFolderWithPage beforeEdit(String id) {
		ParameterFolder folder = this.findById(id);
		getHibernateTemplate().initialize(folder.getAppField());
		getHibernateTemplate().initialize(folder.getPage());
		AppField appField = folder.getAppField();

		List<ParameterPage> parameterPages = parameterPageMgr
				.loadPages(appField.getNo());

		return new ParameterFolderWithPage(folder, appField, parameterPages);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wxlplatform.parameterfolder.ParameterFolderMgr#loadFolderWithParent
	 * (java.lang.String)
	 */
	public ParameterFolder loadFolderWithParent(String id) {
		ParameterFolder folder = this.findById(id);
		getHibernateTemplate().initialize(folder.getAppField());
		getHibernateTemplate().initialize(folder.getPage());

		return folder;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wxlplatform.setting.IWxlPluginSetting#doClear()
	 */
	public void doClear() {
		String hql = "DELETE FROM ParameterFolder";
		delete(hql);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wxlplatform.setting.IWxlPluginSetting#doExports()
	 */
	public List<ParameterFolder> doExports(String afNo) {

		if (StringUtils.isEmpty(afNo)) {
			List<ParameterFolder> ret = loadAll();
			for (ParameterFolder p : ret) {
				getHibernateTemplate().initialize(p.getAppField());
				getHibernateTemplate().initialize(p.getPage());
			}
			return ret;
		} else {
			return this.loadByAppFieldNo(afNo);
		}
	}

	private List<ParameterFolder> loadByAppFieldNo(String afNo) {
		String hql = "select u from ParameterFolder u where u.removed=? and u.appField.no=? order by u.page.indexNo, u.indexNo";
		return find(hql, false, afNo);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wxlplatform.setting.IWxlPluginSetting#doImport(java.util.List,
	 * boolean)
	 */
	public void doImport(List<ParameterFolder> objs, boolean update) {

		if (update) {
			doAppendFolder(objs);
		} else {
			this.doImportFolder(objs);
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
	public JDOMTransfer<ParameterFolder> getTransfer() {
		return new ParameterFolderTransfer();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * wxl.basement.bonding.dao.DomainInspector#afterEnable(java.lang.Object,
	 * boolean)
	 */
	public void afterEnable(ParameterFolder obj, boolean enable) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * wxl.basement.bonding.dao.DomainInspector#afterRemove(java.lang.Object)
	 */
	public void afterRemove(ParameterFolder obj) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * wxl.basement.bonding.dao.DomainInspector#afterVisible(java.lang.Object,
	 * boolean)
	 */
	public void afterVisible(ParameterFolder obj, boolean visible) {
		// TODO Auto-generated method stub

	}

}
