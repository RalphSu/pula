/**
 * Created on 2008-12-19 11:27:42
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

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import puerta.support.mls.Mls;
import puerta.system.base.HibernateGenericDao;
import puerta.system.dao.ParameterDao;
import puerta.system.dao.ParameterFolderDao;
import puerta.system.dao.ParameterPageDao;
import puerta.system.intfs.IWxlPluginSetting;
import puerta.system.intfs.JDOMTransfer;
import puerta.system.po.Parameter;
import puerta.system.po.ParameterFolder;
import puerta.system.po.ParameterPage;
import puerta.system.transfer.ParameterTransfer;
import puerta.system.vo.ParameterWithPageAndFolder;

/**
 * 
 * @author tiyi
 * 
 */
@Repository
public class ParameterDaoImpl extends HibernateGenericDao<Parameter, String>
		implements ParameterDao, IWxlPluginSetting<Parameter> {
	@Resource
	private ParameterFolderDao parameterFolderMgr;
	@Resource
	private ParameterPageDao parameterPageMgr;

	public ParameterFolderDao getParameterFolderMgr() {
		return parameterFolderMgr;
	}

	public void setParameterFolderMgr(ParameterFolderDao parameterFolderMgr) {
		this.parameterFolderMgr = parameterFolderMgr;
	}

	public ParameterPageDao getParameterPageMgr() {
		return parameterPageMgr;
	}

	public void setParameterPageMgr(ParameterPageDao parameterPageMgr) {
		this.parameterPageMgr = parameterPageMgr;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wxlplatform.parameter.ParameterDao#loadParameters()
	 */
	public Map<String, Parameter> loadParameters() {
		String hql = "FROM Parameter p WHERE p.removed=? ORDER BY p.indexNo";
		List<Parameter> l = find(hql, false);
		HashMap<String, Parameter> hm = new LinkedHashMap<String, Parameter>();
		for (Iterator<Parameter> iter = l.iterator(); iter.hasNext();) {
			Parameter p = (Parameter) iter.next();
			if (p.getParamType() == Parameter.TYPE_PASSWORD) {
				logger.info(p.getNo() + "=******");
			} else {
				logger.info(p.getNo() + "=" + p.getValue());
			}
			hm.put(p.getNo(), p);
		}

		logger.debug("parameters.size=" + hm.size());

		return hm;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wxlplatform.parameter.ParameterDao#saveParameters(java.
	 * util.HashMap)
	 */
	public void saveParameters(HashMap<String, Parameter> hm) {
		for (Iterator<Parameter> iter = hm.values().iterator(); iter.hasNext();) {
			Parameter p = (Parameter) iter.next();
			saveParameter(p);
		}

	}

	private void saveParameter(Parameter p) {
		Parameter po = this.findByNo(p.getNo());

		po.setValue(p.getValue());
		getHibernateTemplate().update(po);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wxlplatform.parameter.ParameterMgr#loadParameterByAppField
	 * (java.lang.String)
	 */
	public Map<String, List<Parameter>> loadParameterByAppField(
			String appFieldNo) {

		String hql = "FROM Parameter u WHERE u.appField.no=? AND u.removed=? ORDER BY u.folder.id,u.indexNo";
		List<Parameter> l = find(hql, new Object[] { appFieldNo, false });
		HashMap<String, List<Parameter>> m = new LinkedHashMap<String, List<Parameter>>();
		for (Iterator<Parameter> iter = l.iterator(); iter.hasNext();) {
			Parameter p = (Parameter) iter.next();
			String key = p.getFolder().getId();
			List<Parameter> params = null;
			if (m.containsKey(key)) {
				params = (ArrayList<Parameter>) m.get(key);
			} else {
				params = new ArrayList<Parameter>();
				m.put(key, params);
			}

			params.add(p);
		}

		return m;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wxlplatform.parameter.ParameterMgr#loadParametersByPage(java.lang
	 * .String)
	 */
	public Map<String, List<Parameter>> loadParametersByPage(
			String parameterPageId) {
		String hql = "FROM Parameter u WHERE u.page.id=? AND u.removed=? ORDER BY u.indexNo";
		List<Parameter> l = find(hql, new Object[] { parameterPageId, false });
		HashMap<String, List<Parameter>> m = new LinkedHashMap<String, List<Parameter>>();
		for (Iterator<Parameter> iter = l.iterator(); iter.hasNext();) {
			Parameter p = (Parameter) iter.next();
			String key = p.getFolder().getId();
			List<Parameter> params = null;
			logger.debug(key + ">>" + p.getName());
			if (m.containsKey(key)) {
				params = (ArrayList<Parameter>) m.get(key);
			} else {
				params = new ArrayList<Parameter>();
				m.put(key, params);
			}

			params.add(p);
		}

		return m;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wxlplatform.parameter.ParameterMgr#remove(java.lang.String,
	 * java.lang.String)
	 */
	public void remove(String className, String id) {
		String[] classNames = new String[] { "Parameter", "ParameterFolder",
				"ParameterPage" };
		if (!ArrayUtils.contains(classNames, className)) {
			Mls.raise("platform.parameter.noExists", className);
		}

		String hql = "UPDATE " + className + " SET removed=? WHERE id=?";
		super.updateBatch(hql, new Object[] { true, id });

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wxlplatform.parameter.ParameterMgr#save(wxlplatform.po.Parameter )
	 */
	public Parameter save(Parameter p) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("no", p.getNo());
		if (this.exists(map)) {
			Mls.raise("platform.parameter.noExists", p.getNo());
		}

		getHibernateTemplate().save(p);
		return p;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wxlplatform.parameter.ParameterMgr#update(wxlplatform.po.Parameter )
	 */
	public Parameter update(Parameter parameter) {
		String hql = "FROM Parameter WHERE no=? AND id<>?";
		long c = this
				.getCountByQuery(hql, parameter.getNo(), parameter.getId());
		if (c > 0) {
			Mls.raise("platform.parameter.noExists", parameter.getNo());

		}

		Parameter pp = findById(parameter.getId());

		pp.setIndexNo(parameter.getIndexNo());
		pp.setName(parameter.getName());
		pp.setNo(parameter.getNo());
		pp.setMask(parameter.getMask());
		pp.setParamType(parameter.getParamType());
		pp.setFolder(parameter.getFolder());
		pp.setPage(parameter.getPage());
		pp.setValue(parameter.getValue());

		return pp;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wxlplatform.parameter.ParameterMgr#updateParams(java.lang.String[],
	 * java.lang.String[])
	 */
	public void updateParams(String[] params, String[] values) {
		int len = params.length;
		for (int i = 0; i < len; i++) {
			Parameter p = this.findById(params[i]);
			p.setValue(StringUtils.trim(values[i]));

		}
		getHibernateTemplate().flush();
		loggerDao.doLog(mls.t("platform.parameter.editParameter"));

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wxlplatform.parameter.ParameterMgr#beforeEdit(java.lang.String)
	 */
	public ParameterWithPageAndFolder beforeEdit(String id) {

		Parameter parameter = this.findById(id);

		getHibernateTemplate().initialize(parameter.getFolder());
		getHibernateTemplate().initialize(parameter.getFolder().getPage());
		getHibernateTemplate().initialize(parameter.getFolder().getAppField());

		List<ParameterPage> parameterPages = parameterPageMgr
				.loadPages(parameter.getFolder().getAppField().getNo());
		List<ParameterFolder> parameterFolders = parameterFolderMgr
				.loadFolders(parameter.getPage().getId());

		return new ParameterWithPageAndFolder(parameter, parameterPages,
				parameterFolders);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wxlplatform.parameter.ParameterMgr#doClearAll()
	 */
	public void doClearAll() {
		String hql = "DELETE FROM Parameter";
		delete(hql);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wxlplatform.parameter.ParameterMgr#doImport(java.util.List, boolean)
	 */
	public void doImport(List<Parameter> params, boolean b) {
		for (Iterator<Parameter> iter = params.iterator(); iter.hasNext();) {
			Parameter pp = iter.next();
			// get page
			String no = pp.getFolder().getNo();
			ParameterFolder pf = (ParameterFolder) this.findByNo(
					ParameterFolder.class, no);
			pp.setFolder(pf);
			if (pf != null) {
				pp.setPage(pf.getPage());
				pp.setAppField(pf.getAppField());
			}
			if (b) {
				updateByNoOrSave(pp);
			} else {
				save(pp);
			}
		}

	}

	/**
	 * @param b
	 */
	private void updateByNoOrSave(Parameter next) {
		Parameter po = this.findByNo(next.getNo());
		if (po == null) {
			save(next);
		} else {
			next.setRemoved(false);
			po.setRemoved(false);

			po.setAppField(next.getAppField());
			po.setFolder(next.getFolder());
			po.setIndexNo(next.getIndexNo());
			po.setMagicNo(next.getMagicNo());
			po.setMask(next.getMask());
			po.setName(next.getName());
			po.setNo(next.getNo());
			po.setPage(next.getPage());
			po.setParamType(next.getParamType());
			po.setValue(next.getValue());
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wxlplatform.parameter.ParameterMgr#getMaxIndexNo(java.lang.String)
	 */
	public int getMaxIndexNo(String parameterFolderId) {
		String hql = "SELECT MAX(indexNo) FROM Parameter WHERE folder.id=? ";

		Integer l = findSingle(hql, parameterFolderId);
		if (l == null)
			return 10;
		return l.intValue() + 10;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wxlplatform.parameter.ParameterMgr#loadInstance(java.lang.String)
	 */
	public Parameter loadInstance(String id) {
		Parameter p = this.findById(id);
		getHibernateTemplate().initialize(p.getFolder());
		getHibernateTemplate().initialize(p.getAppField());
		getHibernateTemplate().initialize(p.getPage());
		return p;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wxlplatform.setting.IWxlPluginSetting#doClear()
	 */
	public void doClear() {
		doClearAll();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wxlplatform.setting.IWxlPluginSetting#doExports()
	 */
	public List<Parameter> doExports(String afNo) {

		if (StringUtils.isEmpty(afNo)) {
			List<Parameter> lst = new ArrayList<Parameter>(this
					.loadParameters().values());

			this.loadParameterByAppField(afNo);
			for (Parameter p : lst) {
				getHibernateTemplate().initialize(p.getFolder());
				getHibernateTemplate().initialize(p.getPage());
			}

			return lst;
		} else {
			return this.loadByAppFieldNo(afNo);
		}
	}

	private List<Parameter> loadByAppFieldNo(String afNo) {
		String hql = "FROM Parameter u WHERE u.appField.no=? AND u.removed=? ORDER BY u.folder.indexNo,u.indexNo";
		List<Parameter> l = find(hql, afNo, false);
		return l;
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
	public JDOMTransfer<Parameter> getTransfer() {
		return new ParameterTransfer();
	}

}
