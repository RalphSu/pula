/**
 * Created on 2008-12-21 08:13:34
 *
 * NILVAR 2008
 * $Id$
 */
package puerta.system.dao.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
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
import puerta.support.dao.TreePathPo;
import puerta.support.mls.Mls;
import puerta.support.utils.StringTool;
import puerta.support.vo.TreeNodeDTO;
import puerta.system.base.HibernateGenericDao;
import puerta.system.base.TreePathTool;
import puerta.system.condition.DictLimbCondition;
import puerta.system.dao.DictLimbDao;
import puerta.system.dao.LoggerDao;
import puerta.system.intfs.JDOMTransfer;
import puerta.system.po.DictLimb;
import puerta.system.transfer.DictLimbTransfer;
import puerta.system.vo.EntityWithIndex;

/**
 * 
 * @author tiyi
 * 
 */
@Repository("dictLimbDao")
public class DictLimbDaoImpl extends HibernateGenericDao<DictLimb, String>
		implements DictLimbDao {

	@Resource
	private LoggerDao loggerDao;
	@Resource
	private Mls mls;

	/*
	 * (non-Javadoc)
	 * 
	 * @see wxldiscount.system.dictlimb.DictLimbDao#search(wxl
	 * discount.system.dictlimb.DictLimbCondition, int)
	 */
	public PaginationSupport<DictLimb> search(DictLimbCondition condition,
			int pageNo) {
		DetachedCriteria dc = makeDetachedCriteria(condition);

		// dc.add(criterion)
		if (!StringUtils.isEmpty(condition.getParentId())) {
			// dc.createAlias("parent", "p");

			dc.add(Restrictions.eq("parent.id", condition.getParentId()));
		} else {
			dc.add(Restrictions.eq("level", 0));
		}

		return super.findPageByCriteria(dc, new PageInfo(pageNo),
				Order.asc("treePath"));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wxldiscount.system.dictlimb.DictLimbDao#save(wxl.web
	 * .discount.system.po.DictLimb)
	 */
	public DictLimb save(DictLimb dictLimb) {
		// Map<String, Object> v = new HashMap<String, Object>();
		// v.put("no", dictLimb.getNo());

		if (existsNo(dictLimb.getNo())) {
			Mls.raise("platform.dictlimb.noExists", dictLimb.getNo());
		}

		// int level = 0;
		// String treePath = StringTool.fillZero(dictLimb.getIndexNo(), 5);
		//
		// if (dictLimb.getParent() != null) {
		//
		// DictLimb parent = this.findById(dictLimb.getParent().getId());
		// dictLimb.setParent(parent);
		// dictLimb.setParentNo(parent.getNo());
		//
		// level = parent.getLevel() + 1;
		//
		// treePath = parent.getTreePath() + ","
		// + StringTool.fillZero(dictLimb.getIndexNo(), 5);
		// }
		//
		// dictLimb.setLevel(level);
		// dictLimb.setTreePath(treePath);

		TreePathTool.setup(this, dictLimb);

		getHibernateTemplate().save(dictLimb);
		loggerDao.doLog(mls.t("platform.dictlimb.add"),
				StringTool.getBean(dictLimb));
		return dictLimb;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wxldiscount.system.dictlimb.DictLimbDao#beforeEdit
	 * (java.lang.String)
	 */
	public DictLimb beforeEdit(String id) {
		DictLimb pc = this.findById(id);
		if (pc.getParent() != null) {
			getHibernateTemplate().initialize(pc.getParent());

		}
		return pc;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wxldiscount.system.dictlimb.DictLimbDao#update(wxl
	 * discount.system.po.DictLimb)
	 */
	public DictLimb update(DictLimb m) {
		// Map<String, Object> v = new HashMap<String, Object>();
		// v.put("no", m.getNo());
		//
		// Map<String, Object> filter = new HashMap<String, Object>();
		// filter.put("id", m.getId());

		if (existsNo(m.getNo(), m.getIdentify()))
			Mls.raise("platform.dictlimb.noExists", m.getNo());

		DictLimb po = findById(m.getIdentify());

		// po.setIndexNo(m.getIndexNo());
		po.setNo(m.getNo());
		po.setName(m.getName());
		// if (m.getParent() != null
		// && !StringUtils.isEmpty(m.getParent().getId())) {
		// DictLimb p = findById(m.getParent().getId());
		// po.setParent(p);
		// logger.debug(p.getNo());
		// po.setParentNo(p.getNo());
		// } else {
		// po.setParent(null);
		// }
		// // po.setLevel(m.getLevel());
		// // po.setIndexNo(indexNo)
		//
		// // 更新子类的parentNo;
		// String hql =
		// "UPDATE DictLimb SET parentNo=? WHERE parent.id=? AND removed=?";
		// super.updateBatch(hql, m.getNo(), m.getId(), false);

		// int level = 0;
		// String treePath = "";
		// if (po.getParent() != null) {
		// po.setParentNo(po.getParent().getNo());
		// level = po.getParent().getLevel() + 1;
		// treePath = po.getParent().getTreePath() + ","
		// + StringTool.fillZero(po.getIndexNo(), 5);
		// } else {
		// treePath = StringTool.fillZero(po.getIndexNo(), 5);
		// }
		//
		// po.setLevel(level);
		// po.setTreePath(treePath);

		TreePathTool.setup(this, m, po);

		loggerDao
				.doLog(mls.t("platform.dictlimb.edit"), StringTool.getBean(po));

		return po;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * wxl.basement.bonding.dao.DomainInspector#afterEnable(java.lang.Object,
	 * boolean)
	 */
	public void afterEnable(DictLimb obj, boolean enable) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * wxl.basement.bonding.dao.DomainInspector#afterRemove(java.lang.Object)
	 */
	public void afterRemove(DictLimb obj) {

		loggerDao.doLog(mls.t("platform.dictlimb.remove"),
				StringTool.getBean(obj));

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * wxl.basement.bonding.dao.DomainInspector#afterVisible(java.lang.Object,
	 * boolean)
	 */
	public void afterVisible(DictLimb obj, boolean visible) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seewxldiscount.system.dictlimb.DictLimbDao#
	 * loadCategoryAndMaxIndex(java.lang.String)
	 */
	public EntityWithIndex<DictLimb> loadCategoryAndMaxIndex(String id) {
		DictLimb pc = null;
		if (!StringUtils.isEmpty(id)) {
			pc = this.findById(id);
		}

		return new EntityWithIndex<DictLimb>(pc, getMaxIndexNo(pc));

	}

	/**
	 * @param pc
	 * @return
	 */
	private int getMaxIndexNo(DictLimb pc) {
		String hql = null;
		Integer maxN = null;
		if (pc == null) {
			hql = "SELECT MAX(indexNo) FROM DictLimb WHERE removed=? AND level=0";
			maxN = findSingle(hql, false);
		} else {
			hql = "SELECT MAX(indexNo) FROM DictLimb WHERE removed=? AND parent.id=?";
			maxN = findSingle(hql, false, pc.getIdentify());
		}

		if (maxN == null) {
			return 10;
		} else {
			return maxN + 10;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seewxldiscount.system.dictlimb.DictLimbDao# loadByIndexNoAndLevel(int)
	 */
	public List<DictLimb> loadByIndexNoAndLevel(int i) {
		String hql = "FROM DictLimb WHERE removed=? AND level=?";
		return find(hql, false, i);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wxldiscount.system.dictlimb.DictLimbDao#loadByParent
	 * (java.lang.String)
	 */
	public List<DictLimb> loadByParent(String id) {
		String hql = "FROM DictLimb WHERE removed=? AND parent.id=? ORDER BY indexNo";
		return find(hql, false, id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wxlplatform.dictlimb.DictLimbDao#doClear()
	 */
	public void doClear() {

		String hql = "UPDATE DictLimb SET parent=null";
		super.updateBatch(hql);
		hql = "DELETE FROM DictLimb ";
		super.delete(hql);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wxlplatform.dictlimb.DictLimbDao#doImport(java.util.List)
	 */
	public void doImport(List<DictLimb> limbs) {
		if (limbs == null || limbs.size() == 0)
			return;
		for (DictLimb dl : limbs) {
			if (!StringUtils.isEmpty(dl.getParentNo())) {
				String pno = dl.getParentNo();
				dl.setParent(this.findByNo(pno));

			} else {
				dl.setParent(null);
			}

			this.save(dl);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wxlplatform.dictlimb.DictLimbDao#loadAllByTree()
	 */
	public List<DictLimb> loadAllByTree() {
		String hql = "FROM DictLimb WHERE removed=? ORDER BY treePath";
		return find(hql, false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wxlplatform.dictlimb.DictLimbDao#loadByParentNo(java.lang.String)
	 */
	public List<DictLimb> loadByParentNo(String dictArea) {
		String hql = "FROM DictLimb WHERE removed=? AND parent.no=? AND parent.removed=? ORDER BY indexNo";
		return find(hql, false, dictArea, false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wxlplatform.setting.IWxlPluginSetting#doExports()
	 */
	public List<DictLimb> doExports(String afNo) {
		return loadAllByTree();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wxlplatform.setting.IWxlPluginSetting#doImport(java.util.List,
	 * boolean)
	 */
	public void doImport(List<DictLimb> objs, boolean update) {
		if (update) {
			this.doAppend(objs);
		} else {
			this.doImport(objs);
		}

	}

	private void doAppend(List<DictLimb> limbs) {

		if (limbs == null || limbs.size() == 0)
			return;
		for (DictLimb dl : limbs) {
			if (!StringUtils.isEmpty(dl.getParentNo())) {
				String pno = dl.getParentNo();
				dl.setParent(this.findByNo(pno));
			} else {
				dl.setParent(null);
			}

			DictLimb p = this.findByNo(dl.getNo());
			if (p == null) {
				this.save(dl);
			} else {
				dl.setId(p.getIdentify());
				this.update(dl);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wxlplatform.setting.IWxlPluginSetting#getName()
	 */
	public String getPluginName() {
		return getPojoClassName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wxlplatform.setting.IWxlPluginSetting#getTransfer()
	 */
	public JDOMTransfer<DictLimb> getTransfer() {
		return new DictLimbTransfer();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wxlplatform.dictlimb.DictLimbDao#makeTree(java.lang.StringBuilder,
	 * java.lang.String, java.lang.String)
	 */
	public void makeTree(StringBuilder sb, String no, String underNo) {
		String hql = "select u FROM DictLimb u WHERE u.no=? AND u.removed=?";
		DictLimb d = findSingle(hql, no, false);

		makeTree(sb, d, true);

	}

	public void makeTree(StringBuilder sb, DictLimb dict, boolean sub) {
		DictLimb padre = dict.getParent();

		if (padre != null && padre.getLevel() != 0) {
			makeTree(sb, padre, false);
		}

		sb.append(dict.getName());
		if (!sub)
			sb.append(" - ");

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wxlplatform.dictlimb.DictLimbDao#loadDictsAsTree(java.lang.String)
	 */
	public List<TreeNodeDTO> loadDictsAsTree(String no) {
		List<DictLimb> l = loadAllByParent(no);

		List<TreeNodeDTO> roots = new ArrayList<TreeNodeDTO>();
		Map<String, TreeNodeDTO> treeNodes = new LinkedHashMap<String, TreeNodeDTO>();
		for (DictLimb dict : l) {

			TreeNodeDTO dto = new TreeNodeDTO();
			dto.setNo(dict.getNo());
			dto.setName(dict.getName());
			treeNodes.put(dict.getNo(), dto);
			if (!StringUtils.isEmpty(dict.getParentNo())) {
				String parentId = dict.getParentNo();
				logger.debug("parentId=" + parentId + " ;dict.getName="
						+ dict.getName());
				if (treeNodes.containsKey(parentId)) {
					treeNodes.get(parentId).addChildNode(dto);
					continue;
				}

			}

			// 没有找到parent
			// 没有parent
			roots.add(dto);
		}
		return roots;
	}

	private List<DictLimb> loadAllByParent(String no) {
		String hql = "select u FROM DictLimb u WHERE u.treePath like ? and u.removed=? order by u.treePath,u.no ";
		return find(hql, "%," + no + "%", false);
	}

	@Override
	public void fixTreePath() {
		String hql = "select u from DictLimb u where u.level=?";
		String update = "update DictLimb set treePath=? where id=?";
		for (int i = 0; i < 100; i++) {
			logger.debug("level=" + i);
			List<DictLimb> dicts = find(hql, i);
			if (dicts.size() == 0)
				return;

			for (DictLimb d : dicts) {
				// if (StringUtils.equals("C01750", d.getNo())) {
				// //int dds = 0;
				// }

				String treePath = null;
				if (d.getParent() != null) {

					treePath = d.getParent().getTreePath() + ","
							+ StringTool.fillZero(d.getIndexNo(), 5);
				} else {

					treePath = StringTool.fillZero(d.getIndexNo(), 5);
				}

				updateBatch(update, treePath, d.getIdentify());
			}
		}

	}

	@Override
	public DictLimb fetch(DictLimb area, String area2) {
		// find first
		String hql = "select u FROM DictLimb u where u.name=? and u.parent.no=?";
		DictLimb find = findSingle(hql, area.getName(), area2);
		if (find == null) {

			DictLimb p = this.findByNo(area2);
			area.setParent(p);
			save(area);
			return area;
		}

		return find;
	}

	@Override
	public void organize() {
		String hql = "select u from DictLimb u where u.removed=? order by indexNo,level";
		List<DictLimb> list = find(hql, false);

		//
		for (DictLimb dl : list) {
			TreePathTool.setup(this, dl);
		}
	}
}
