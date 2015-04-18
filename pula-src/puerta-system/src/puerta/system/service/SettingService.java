/**
 * Created on 2007-1-1 08:36:34
 *
 * DiagCN.COM 2004-2006
 * $Id: SettingMgrImpl.java,v 1.2 2007/01/09 07:44:29 tiyi Exp $
 */
package puerta.system.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.jdom.Comment;
import org.jdom.Document;
import org.jdom.Element;
import org.springframework.stereotype.Service;

import puerta.support.Pe;
import puerta.support.dao.IWxlActor;
import puerta.support.utils.DateExTool;
import puerta.support.utils.WxlSugar;
import puerta.system.dao.ActorPurviewDao;
import puerta.system.dao.AppFieldDao;
import puerta.system.dao.DictLimbDao;
import puerta.system.dao.InsiderDao;
import puerta.system.dao.ModuleDao;
import puerta.system.dao.OrderNoRuleDao;
import puerta.system.dao.ParameterDao;
import puerta.system.dao.ParameterFolderDao;
import puerta.system.dao.ParameterPageDao;
import puerta.system.dao.PurviewDao;
import puerta.system.dao.PurviewToRequestUriDao;
import puerta.system.dao.RequestUriDao;
import puerta.system.dao.ShortcutDao;
import puerta.system.dao.SysRoleDao;
import puerta.system.intfs.IWxlPluginActor;
import puerta.system.intfs.IWxlPluginKeeper;
import puerta.system.intfs.IWxlPluginSetting;
import puerta.system.keeper.DictLimbKeeper;
import puerta.system.keeper.ParameterKeeper;
import puerta.system.keeper.RequestUriKeeper;
import puerta.system.po.Insider;
import puerta.system.po.SysRole;
import puerta.system.transfer.TransferWorker;

/**
 * 不是dao
 * 
 * @author tiyi
 * 
 */
@Service
public class SettingService {

	private Map<String, IWxlPluginSetting<?>> settings = new LinkedHashMap<String, IWxlPluginSetting<?>>();
	private List<IWxlPluginKeeper> keepers = new ArrayList<IWxlPluginKeeper>();
	private Map<String, IWxlPluginActor<? extends IWxlActor>> actors = WxlSugar
			.newHashMap();

	/*
	 * 导出全部
	 * 
	 * @see com.nut.groundwork.platform.setting.SettingMgr#exportAll()
	 */
	public Document exportAll(List<String> supports, String afNo) {

		Element rootElement = new Element("wxl-setting");
		Document myDocument = new Document(rootElement);
		rootElement.setAttribute("exportTime",
				DateExTool.dateTime2String(Calendar.getInstance()));

		// 版本
		Comment c = null;
		List<Element> elements = null;
		List<? extends Object> l = null;

		for (String s : supports) {
			try {
				c = new Comment(s);
				IWxlPluginSetting<? extends Object> plugin = this.settings
						.get(s);
				l = plugin.doExports(afNo);
				elements = TransferWorker.transferElements(l, s);
				rootElement.addContent(c);
				rootElement.addContent(elements);
			} catch (RuntimeException ex) {
				ex.printStackTrace();
				Pe.raise("error on:" + s);
			}
		}

		return myDocument;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.nut.groundwork.platform.setting.SettingMgr#save(org.jdom.Document)
	 */
	@SuppressWarnings("unchecked")
	public <T> void save(Document doc) {
		Element root = doc.getRootElement();

		// 清除时需倒序
		List<IWxlPluginSetting<? extends Object>> l = new ArrayList<IWxlPluginSetting<? extends Object>>(
				settings.values());
		Collections.reverse(l);
		for (IWxlPluginSetting<? extends Object> plugin : l) {
			plugin.doClear();
		}

		for (IWxlPluginSetting<? extends Object> plugin : settings.values()) {
			@SuppressWarnings("rawtypes")
			List ases = TransferWorker.transferObjects(root,
					plugin.getPluginName());
			plugin.doImport(ases, false);
		}

		callKeepersReload();

	}

	/**
	 * 
	 */
	private void callKeepersReload() {
		for (IWxlPluginKeeper keeper : keepers) {
			keeper.reload();
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.nilnut.groundwork.platform.setting.SettingMgr#append(org.jdom.Document
	 * )
	 */
	@SuppressWarnings("unchecked")
	public void doAppend(Document doc) {
		Element root = doc.getRootElement();

		for (@SuppressWarnings("rawtypes")
		IWxlPluginSetting plugin : settings.values()) {
			@SuppressWarnings("rawtypes")
			List objs = TransferWorker.transferObjects(root,
					plugin.getPluginName());
			plugin.doImport(objs, true);
		}

		callKeepersReload();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wxlplatform.setting.SettingMgr#register(wxlplatform.setting
	 * .IWxlPluginSetting)
	 */
	public void register(IWxlPluginSetting<?> pluginSetting) {
		settings.put(pluginSetting.getPluginName(), pluginSetting);
		TransferWorker.register(pluginSetting.getPluginName(),
				pluginSetting.getTransfer());
	}

	@Resource
	AppFieldDao appFieldDao;
	@Resource
	RequestUriDao sysUrlDao;
	@Resource
	ModuleDao moduleDao;
	@Resource
	PurviewDao purviewDao;
	@Resource
	PurviewToRequestUriDao purviewToUrlDao;
	@Resource
	ShortcutDao shortcutDao;
	@Resource
	ParameterDao parameterDao;
	@Resource
	ParameterFolderDao parameterFolderDao;
	@Resource
	ParameterPageDao parameterPageDao;

	@Resource
	SysRoleDao sysRoleDao;
	@Resource
	DictLimbDao dictLimbDao;

	@Resource
	OrderNoRuleDao orderNoRuleDao;
	@Resource
	InsiderDao insiderDao;

	@Resource
	ParameterKeeper parameterKeeper;
	@Resource
	DictLimbKeeper dictLimbKeeper;
	@Resource
	RequestUriKeeper sysUriKeeper;

	@Resource
	ActorPurviewDao actorPurviewDao;

	/*
	 * (non-Javadoc)
	 * 
	 * @see wxlplatform.setting.SettingMgr#registerDefault()
	 */
	public void registerDefault() {

		// all leader
		register(appFieldDao);

		register(sysUrlDao);
		register(moduleDao);
		register(purviewDao);
		register(purviewToUrlDao);
		register(shortcutDao);

		// parameters
		register(parameterPageDao);
		register(parameterFolderDao);
		register(parameterDao);

		register(sysRoleDao);

		// singlger
		register(dictLimbDao);
		register(orderNoRuleDao);
		register((IWxlPluginSetting<Insider>) insiderDao);

		register(this.parameterKeeper);
		register(this.dictLimbKeeper);
		register(this.sysUriKeeper);

		register((IWxlPluginActor<Insider>) this.insiderDao);
	}

	/**
	 * @param insiderMgr2
	 */
	public void register(IWxlPluginActor<? extends IWxlActor> actor) {
		this.actors.put(actor.getAppFieldNo(), actor);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wxlplatform.setting.SettingMgr#register(wxlplatform.setting
	 * .IWxlPluginKeeper)
	 */
	public void register(IWxlPluginKeeper keeper) {
		keepers.add(keeper);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wxlplatform.setting.SettingMgr#getPluginNames()
	 */
	public List<String> getPluginNames() {
		return new ArrayList<String>(this.settings.keySet());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wxlplatform.setting.SettingMgr#doAssignPurview(java.lang.String,
	 * java.lang.String)
	 */
	public void doAssignPurview(String loginId, String type) {
		if (!actors.containsKey(type)) {
			Pe.raise("应用领域对应的Actor 未注册:" + type);
		}
		IWxlPluginActor<? extends Object> actor = actors.get(type);
		String id = actor.getActorId(loginId);
		if (id == null) {
			Pe.raise("未知的Actor编号:" + loginId);
		}
		this.actorPurviewDao.doAssignAll(id, type);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wxlplatform.setting.SettingMgr#doApplyActorPurview(java.lang.String)
	 */
	public void doApplyActorPurview(String actorId) {
		SysRole ar = sysRoleDao.findById(actorId);
		String type = ar.getAppField().getNo();
		if (!actors.containsKey(type)) {
			Pe.raise("应用领域对应的Actor 未注册:" + type);
		}
		// get ns
		IWxlPluginActor<? extends IWxlActor> actor = actors.get(type);

		List<? extends IWxlActor> acts = actor.getActorsByRole(ar.getNo());
		for (IWxlActor a : acts) {
			actorPurviewDao.doCopy(ar.getId(), a.getActorId());
		}
	}
}
