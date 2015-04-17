/**
 * Created on 2008-12-21 08:11:55
 *
 * NILVAR 2008
 * $Id$
 */
package puerta.system.dao;

import java.util.List;

import puerta.support.PaginationSupport;
import puerta.support.dao.BaseDao;
import puerta.support.vo.TreeNodeDTO;
import puerta.system.condition.DictLimbCondition;
import puerta.system.intfs.IWxlPluginSetting;
import puerta.system.po.DictLimb;
import puerta.system.vo.EntityWithIndex;

/**
 * 
 * @author tiyi
 * 
 */
public interface DictLimbDao extends BaseDao<DictLimb, String>,
		IWxlPluginSetting<DictLimb> {

	/**
	 * @param condition
	 * @param pageNo
	 * @return
	 */
	PaginationSupport<DictLimb> search(DictLimbCondition condition, int pageNo);

	/**
	 * @param dictLimb
	 * @return
	 */
	DictLimb save(DictLimb dictLimb);

	/**
	 * @param dictLimb
	 * @return
	 */
	DictLimb update(DictLimb dictLimb);

	/**
	 * @param id
	 * @return
	 */
	DictLimb beforeEdit(String id);

	/**
	 * @param id
	 * @return
	 */
	EntityWithIndex<DictLimb> loadCategoryAndMaxIndex(String id);

	/**
	 * @param i
	 * @return
	 */
	List<DictLimb> loadByIndexNoAndLevel(int i);

	/**
	 * @param id
	 * @return
	 */
	List<DictLimb> loadByParent(String id);

	/**
	 * @return
	 */
	List<DictLimb> loadAllByTree();

	/**
	 * 
	 */
	void doClear();

	/**
	 * @param limbs
	 */
	void doImport(List<DictLimb> limbs);

	/**
	 * @param dictArea
	 * @return
	 */
	List<DictLimb> loadByParentNo(String dictArea);

	/**
	 * @param sb
	 * @param type
	 * @param dictWorkformType
	 */
	void makeTree(StringBuilder sb, String no, String underNo);

	/**
	 * @param dictWorkformType
	 * @return
	 */
	List<TreeNodeDTO> loadDictsAsTree(String no);

	void fixTreePath();

	DictLimb fetch(DictLimb area, String area2);

	void organize();

}
