package pula.sys.helpers;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import puerta.support.vo.TreeNodeDTO;
import puerta.system.po.Module;
import puerta.system.po.Purview;
import pula.sys.BhzqConstants;

public class PurviewHelper {

	public static void transferCheckedPurviews(List<Object[]> checkedPurviews,
			Map<String, Object> map) {

		for (Object[] objs : checkedPurviews) {
			Purview p = (Purview) objs[0];
			Integer dataFrom = (Integer) objs[1];

			String key = p.getId();

			TreeNodeDTO dto = (TreeNodeDTO) map.get(key);

			if (dto != null) {
				dto.setChecked(true);
			}
			if (BhzqConstants.AP_GROUP.equals(dataFrom)) {
				dto.setRemark("disabled");
			}

		}
	}

	public static void transferPurviews(List<Purview> purviews,
			Map<String, Object> map) {
		for (Iterator<Purview> iter = purviews.iterator(); iter.hasNext();) {
			Purview p = (Purview) iter.next();
			// logger.debug("p.name=" + p.getName());
			addToTree(p, map);
		}

	}

	public static void addToTree(Purview p, Map<String, Object> map) {
		TreeNodeDTO thisDto = getDTO(p);
		if (p.getParentPurview() != null) {
			if (map.containsKey(p.getParentPurview().getId())) {
				TreeNodeDTO dto = (TreeNodeDTO) map.get(p.getParentPurview()
						.getId());

				thisDto.setRemark(p.getParentPurview().getId());
				dto.addChildNode(thisDto);
				// return;
			} else {
				// logger.debug("p.name=" + p.getName());
				// logger.debug(";p.getParent=" + p.getParentPurview().getId());
				// logger.debug(";p.getParentName="
				// + p.getParentPurview().getName());
			}

		} else {
			// roots.add(thisDto);

			String key = p.getModule().getId();
			thisDto.setRemark(key);
			TreeNodeDTO dto = (TreeNodeDTO) map.get(key);
			if (dto != null)
				dto.getNodes().add(thisDto);
		}

		map.put(p.getId(), thisDto);

	}

	public static void transferModules(List<Module> modules,
			List<TreeNodeDTO> roots, Map<String, Object> map) {
		for (Iterator<Module> iter = modules.iterator(); iter.hasNext();) {
			Module m = (Module) iter.next();
			TreeNodeDTO d = getDTO(m);
			roots.add(d);
			map.put(d.getId(), d);
		}
	}

	public static TreeNodeDTO getDTO(Module p) {
		TreeNodeDTO d = new TreeNodeDTO();
		d.setId(p.getId());
		d.setNo(p.getNo());
		d.setName(p.getName());
		return d;
	}

	public static TreeNodeDTO getDTO(Purview p) {
		TreeNodeDTO d = new TreeNodeDTO();
		d.setId(p.getId());
		d.setNo(p.getNo());
		d.setName(p.getName());
		d.setRemark(p.getDefaultURL());
		d.setLevel(p.getLevel());
		return d;
	}
}
