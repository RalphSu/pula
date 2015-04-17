package puerta.system.keeper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import puerta.support.ParameterProvider;
import puerta.support.utils.WxlSugar;
import puerta.system.dao.ParameterDao;
import puerta.system.intfs.IWxlPluginKeeper;
import puerta.system.po.Parameter;

@Service
public class ParameterKeeper implements IWxlPluginKeeper, ParameterProvider {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger
			.getLogger(ParameterKeeper.class);

	public Map<String, Parameter> parameters = null;

	public Map<String, Object> cacheValue = new HashMap<String, Object>();
	public Map<String, Object> complied = new HashMap<String, Object>();

	@Resource
	private ParameterDao parameterDao;
	@Resource
	private ProjectPath projectPath;

	public void reload() {
		// read config file from local
		try {
			parameters = LocalParameter.readLocal();
		} catch (IOException e) {
			logger.debug(e);
			parameters = WxlSugar.newHashMap();
		}
		// read from database
		parameters.putAll(parameterDao.loadParameters());
		cacheValue.clear();
		complied.clear();
		build();
	}

	private void build() {
		for (Map.Entry<String, Parameter> e : this.parameters.entrySet()) {
			Object value = null;
			switch (e.getValue().getParamType()) {
			case Parameter.TYPE_FILE_PATH:
				value = getFilePath(e.getKey());
				break;
			case Parameter.TYPE_INT:
				value = getInt(e.getKey());
				break;
			case Parameter.TYPE_DOUBLE:
				value = getDouble(e.getKey());
				break;
			case Parameter.TYPE_BOOL:
				value = getBoolean(e.getKey(), false);
				break;
			default:
				value = e.getValue().getValue();
			}
			this.complied.put(e.getKey(), value);
		}

	}

	public void save(HashMap<String, Parameter> hm) {
		parameterDao.saveParameters(hm);
		reload();

		// Config.reload();
	}

	public String getString(String no) {
		return getString(no, "");
	}

	public int getInt(String no) {
		return getInt(no, 0);
	}

	public int[] getHourMinute(String no) {
		String v = getString(no);

		String[] s = v.split(":");

		int[] i = new int[2];
		i[0] = Integer.parseInt(s[0]);
		i[1] = Integer.parseInt(s[1]);

		return i;
	}

	/**
	 * @param no
	 * @return
	 */
	public double getDouble(String no) {
		Parameter p = (Parameter) parameters.get(no);
		return Double.parseDouble(p.getValue());
	}

	public double getDouble(String no, double defaultValue) {
		Parameter p = (Parameter) parameters.get(no);
		if (p == null)
			return defaultValue;
		return Double.parseDouble(p.getValue());
	}

	public String getFilePath(String no) {
		if (cacheValue.containsKey(no)) {
			return (String) cacheValue.get(no);
		}
		String str = this.projectPath.parsePath(getString(no));
		cacheValue.put(no, str);
		return str;
	}

	public String getWebpath(String no, HttpServletRequest request) {
		if (cacheValue.containsKey(no)) {
			return (String) cacheValue.get(no);
		}
		String str = this.projectPath.parsePath(getString(no), request);
		cacheValue.put(no, str);
		return str;
	}

	public boolean getBoolean(String no, boolean defaultValue) {
		Parameter p = (Parameter) parameters.get(no);
		if (p == null)
			return defaultValue;
		return BooleanUtils.toBoolean(p.getValue());
	}

	/**
	 * @param no
	 * @return
	 */
	public String[] getAsList(String no) {
		String str = getString(no);
		return StringUtils.split(str, ';');
	}

	/**
	 * @param commonQueryPageSize
	 * @param i
	 * @return
	 */
	public int getInt(String no, int i) {
		Parameter p = (Parameter) parameters.get(no);
		if (p == null)
			return i;
		try {
			return Integer.parseInt(p.getValue());
		} catch (RuntimeException ex) {
			logger.debug("no=" + no);
			throw ex;
		}
	}

	/**
	 * @param remoteJdbcUrl
	 * @param defaultValue
	 * @return
	 */
	public String getString(String no, String defaultValue) {
		Parameter p = (Parameter) parameters.get(no);
		if (p == null)
			return defaultValue;
		return p.getValue();
	}

	/**
	 * @param params
	 * @param values
	 */
	public void updateParams(String[] params, String[] values) {
		parameterDao.updateParams(params, values);

		// update self
		parameters.clear();
		parameters = null;
		reload();
	}

}
