package puerta.system.base;

import java.io.Serializable;

import puerta.support.Pe;
import puerta.support.dao.TreePathPo;
import puerta.support.utils.StringTool;

public class TreePathTool {

	@SuppressWarnings("unchecked")
	public static <T, ID extends Serializable> TreePathPo<T, ID> setup(
			HibernateGenericDao<T, ID> dao, TreePathPo<T, ID> rt) {
		TreePathPo<T, ID> p = rt.getParent();
		int level = 0;
		if (p != null) {
			p = (TreePathPo<T, ID>) dao.findById(p.getIdentify());
			level = p.getLevel() + 1;
		}
		rt.setLevel(level);
		rt.setParent((T) p);
		setupTreePath(rt, p);

		return rt;
	}

	private static <T, ID extends Serializable> void setupTreePath(
			TreePathPo<T, ID> rt, TreePathPo<T, ID> p) {
		StringBuilder sb = new StringBuilder(100);
		if (p != null) {
			sb.append(p.getTreePath()).append("-");
		} else {
			sb.append("-");
		}
		sb.append(StringTool.fillZero(rt.getIndexNo(), 3 + rt.getLevel()));
		rt.setTreePath(sb.toString());
	}

	@SuppressWarnings("unchecked")
	public static <T, ID extends Serializable> TreePathPo<T, ID> setup(
			HibernateGenericDao<T, ID> dao, TreePathPo<T, ID> rt,
			TreePathPo<T, ID> po) {
		int oldIndexNo = po.getIndexNo();
		po.setIndexNo(rt.getIndexNo());
		String oldTree = po.getTreePath();
		boolean diffParent = false;
		if (rt.getParent() != null && po.getParent() != null) {
			diffParent = (rt.getParent().getIdentify() != po.getParent()
					.getIdentify());
		} else if (rt.getParent() != po.getParent()) {
			diffParent = true;
		}

		if (oldIndexNo != rt.getIndexNo() || diffParent) {

			TreePathPo<T, ID> parentOfTree = null;
			if (rt.getParent() != null) {
				T objP = dao.findById(rt.getParent().getIdentify());
				parentOfTree = (TreePathPo<T, ID>) objP;

				if (parentOfTree.getIdentify().equals(po.getIdentify())) {
					Pe.raise("父级不可选择自身");
				}

				po.setParent(objP);
				po.setLevel(parentOfTree.getLevel() + 1);
			} else {
				po.setLevel(0);
				po.setParent(null);
			}
			setupTreePath(po, parentOfTree);
		}

		// 更新所有下级的treePath
		if (oldIndexNo != rt.getIndexNo()) {
			String hql = "update "
					+ dao.getPojoClassName()
					+ " set treePath=replace(treePath,?,?) WHERE treePath LIKE ?";
			dao.updateBatch(hql, oldTree + "-", po.getTreePath() + "-", oldTree
					+ "-%");
		}

		return po;
	}
}
