/**
 * Created on 2008-7-24 02:27:22
 *
 * NILVAR 2008
 * $Id$
 */
package puerta.system.keeper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import puerta.system.dao.DictLimbDao;
import puerta.system.intfs.IWxlPluginKeeper;
import puerta.system.po.DictLimb;

/**
 * 
 * @author tiyi
 * 
 */
@Service
public class DictLimbKeeper implements IWxlPluginKeeper {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(DictLimbKeeper.class);

	public Map<String, List<DictLimb>> map = new HashMap<String, List<DictLimb>>();
	private Map<String, String> noNameMap = new LinkedHashMap<String, String>();
	private Map<String, DictLimb> noMap = new HashMap<String, DictLimb>();
	@Resource
	public DictLimbDao dictLimbDao;

	public void reload() {

		map.clear();
		List<DictLimb> l = dictLimbDao.loadAllByTree();

		for (DictLimb d : l) {
			String t = d.getParentNo();
			if (StringUtils.isEmpty(t))
				continue;
			List<DictLimb> ds = null;
			if (map.containsKey(t)) {
				ds = map.get(t);
			} else {
				ds = new ArrayList<DictLimb>();
				map.put(t, ds);
			}

			ds.add(d);

			noMap.put(d.getNo(), d);

			noNameMap.put(d.getNo(), d.getName());
		}

		logger.debug("dict count=" + l.size());
	}

	public List<DictLimb> getDicts(String type) {
		List<DictLimb> l = map.get(type);
		if (l == null)
			l = Collections.emptyList();
		return l;
	}

	public String getName(String typeNo) {
		return noNameMap.get(typeNo);
	}

	public List<String> getNames(String typeNos) {
		String[] strs = StringUtils.split(typeNos, ",");
		List<String> list = new ArrayList<String>();
		for (String s : strs) {
			if (StringUtils.isEmpty(s.trim())) {
				continue;
			}
			list.add(getName(s.trim()));
		}
		return list;
	}

	public DictLimbDao getDictLimbMgr() {
		return dictLimbDao;
	}

	public void setDictLimbMgr(DictLimbDao dictLimbMgr) {
		this.dictLimbDao = dictLimbMgr;
	}

	public DictLimb getByNo(String no) {
		return noMap.get(no);
	}

}
