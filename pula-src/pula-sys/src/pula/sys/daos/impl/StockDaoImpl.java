package pula.sys.daos.impl;

import java.util.List;
import java.util.Map;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import puerta.support.PageInfo;
import puerta.support.PaginationSupport;
import puerta.support.dao.HibernateTool;
import puerta.system.base.HibernateGenericDao;
import puerta.system.vo.MapBean;
import puerta.system.vo.MapList;
import pula.sys.conditions.StockCondition;
import pula.sys.daos.StockDao;
import pula.sys.domains.Branch;
import pula.sys.domains.Material;
import pula.sys.domains.Stock;
import pula.sys.domains.StockLog;
import pula.sys.helpers.StockHelper;

@Repository
public class StockDaoImpl extends HibernateGenericDao<Stock, Long> implements
		StockDao {

	// @Override
	// public void rollback(StockIn old) {
	// // 根据入库单明细恢复库存
	//
	// // 载入入库单明细
	// for (StockInItem item : old.getItems()) {
	// // 逆向
	// Stock s = new Stock();
	// s.setMaterial(item.getMaterial());
	// s.setQuantity(item.getQuantity() * -1);
	// s.setSpace(item.getSpace());
	// // s.setWarehouse(item.getSpace())
	//
	// getHibernateTemplate().save(s);
	// }
	//
	// }

	// private static final String[] ALIAS_MAPPING_AVAILABLE = new String[] {
	// "m.no", "materialNo", "m.name", "materialName", "m.spec", "spec",
	// "m.graphNo", "graphNo" };

	// @Override
	// public void update(StockLog out) {
	//
	// Map<String, Stock> map = StockHelper.merge(stockIn, out);
	// // 区域预检（看是否锁定）
	// List<Long> spacePreCheck = WxlSugar.newArrayList();
	// for (Stock s : map.values()) {
	// // precheckWarehouseSpace(spacePreCheck, s);
	// updateStock(s);
	// }
	//
	// }

	private void updateStock(Stock item) {
		String sql = "update Stock set quantity=quantity+? where id=?";
		Stock stock = findStockRef(item.getMaterial(), item.getBranch());
		if (stock == null) {
			stock = new Stock();
			stock.setMaterial(item.getMaterial());
			stock.setQuantity(item.getQuantity());
			stock.setBranch(item.getBranch());
			getHibernateTemplate().save(stock);
		} else {
			// 追加数量即可
			updateBatch(sql, item.getQuantity(), stock.getId());
		}

	}

	private Stock findStockRef(Material material, Branch branch) {
		String sql = "select u.id from Stock u where u.removed=? and u.branch.id=? and u.material.id=?";
		Long s = findSingle(sql, false, branch.getId(), material.getId());
		if (s == null) {
			return null;
		}
		return Stock.create(s);
	}

	@Override
	public MapList listBySpaceId(long id) {
		String sql = "select m.no as no,m.name as name,m.graphNo as picNo,m.spec as spec,u.quantity as quantity"
				+ " from Stock u,Material m where u.material.id=m.id and u.space.id=? and u.removed=? and u.quantity!=0";

		return super.mapList(sql, id, false);
	}

	@Override
	public Map<String, Integer> mapByBranchId(long id) {
		String sql = "select m.no ,u.quantity as quantity"
				+ " from Stock u,Material m where u.material.id=m.id and u.space.id=? and u.removed=?";

		return HibernateTool.asMap(find(sql, id, false));

	}

	// @Override
	// public void update(StockOut stockOut, StockOut out) {
	// Map<String, Stock> map = StockHelper.merge(stockOut, out);
	// List<Long> spacePreCheck = WxlSugar.newArrayList();
	// for (Stock s : map.values()) {
	// precheckWarehouseSpace(spacePreCheck, s);
	// updateStock(s);
	// }
	// }

	// @Override
	// public void update(InventorySheet note) {
	// // 计算差值，进入库存
	//
	// // List<Long> spacePreCheck = WxlSugar.newArrayList();
	// for (InventorySheetItem item : note.getItems()) {
	// // 差值
	// int want = item.getChkQuantity() - item.getQuantity();
	// Stock s = new Stock();
	// s.setMaterial(item.getMaterial());
	// s.setQuantity(want);
	// s.setWarehouse(note.getSpace().getWarehouse());
	// s.setSpace(note.getSpace());
	// // 盘点单结转，不需要检查锁定！
	// // precheckWarehouseSpace(spacePreCheck, s);
	//
	// updateStock(s);
	// }
	//
	// }
	//
	// @Override
	// public void restore(InventorySheet note) {
	//
	// List<Long> spacePreCheck = WxlSugar.newArrayList();
	// // 计算差值，离开库存 ,所以用-
	// for (InventorySheetItem item : note.getItems()) {
	// // 差值
	// int want = item.getChkQuantity() - item.getQuantity();
	// Stock s = new Stock();
	// s.setMaterial(item.getMaterial());
	// s.setQuantity(want * -1);
	// s.setWarehouse(note.getSpace().getWarehouse());
	// s.setSpace(note.getSpace());
	//
	// precheckWarehouseSpace(spacePreCheck, s);
	//
	// updateStock(s);
	// }
	// }

	// @Override
	// public void update(TransferOrder si, TransferOrder old) {
	// Map<String, Stock> map = StockHelper.merge(si, old);
	//
	// List<Long> spacePreCheck = WxlSugar.newArrayList();
	// for (Stock s : map.values()) {
	// precheckWarehouseSpace(spacePreCheck, s);
	// updateStock(s);
	// }
	//
	// }

	@Override
	public PaginationSupport<Stock> search(StockCondition condition, int pageNo) {
		DetachedCriteria criteria = makeDetachedCriteria(condition);
		return super.findPageByCriteria(criteria, new PageInfo(pageNo),
				Order.desc("m.no"));
	}

	private DetachedCriteria makeDetachedCriteria(StockCondition condition) {
		DetachedCriteria dc = DetachedCriteria.forClass(Stock.class, "uu");

		dc.createAlias("uu.material", "m", DetachedCriteria.LEFT_JOIN);
		dc.createAlias("uu.branch", "b", DetachedCriteria.LEFT_JOIN);

		HibernateTool.eqIfNotEmpty(dc, "m.no", condition.getMaterialNo());
		HibernateTool.eqIfNotZero(dc, "b.id", condition.getBranchId());

		dc.add(Restrictions.eq("uu.removed", false));

		dc.add(Restrictions.ne("uu.quantity", 0));

		return dc;
	}

	@Override
	public MapList export(StockCondition condition) {
		DetachedCriteria dc = makeDetachedCriteria(condition);
		ProjectionList proList = Projections.projectionList();// 设置投影集合

		proList = HibernateTool.injectAlias(proList, ALIAS_MAPPING);
		dc.setProjection(proList);
		dc.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		dc.addOrder(Order.desc("m.no"));

		List<Map<String, Object>> es = super.findByCriteria(dc);
		return MapList.create(es);
	}

	// stockQty,stockMax,stockMin,materialId
	// @Override
	// public MapBean getAlert(Long m) {
	// String sql =
	// "select m.id as id ,m.stockMin as stockMin,m.stockMax as stockMax,"
	// + "s.quantity as stockQuantity from Stock s,"
	// + "Material m where m.id=s.material.id and m.id=? "
	// + " and ((m.stockMin>=0 AND m.stockMin>=s.quantity )"
	// + " OR ( m.stockMax>=0 AND m.stockMax<=s.quantity) )";
	//
	// MapList ml = mapList(sql, m);
	// if (ml.size() == 0) {
	// return null;
	// }
	// return ml.get(0);
	// }
	//
	// @Override
	// public MapList listByProductVersion(long id) {
	// // 通过 MapList -id
	// //
	// String sql =
	// "select m.id as materialId,u.quantity as quantity from Stock u,Material m where u.removed=? and m.id=u.material.id"
	// +
	// " and (exists (select p.id from Part p where p.material.id=m.id and p.productVersion.id=?)"
	// +
	// " or exists(select p.id from StandardPart p where p.material.id=m.id and p.productVersion.id=?))";
	//
	// return mapList(sql, false, id, id);
	//
	// }
	//
	// @Override
	// public PaginationSupport<MapBean> searchAvailable(StockCondition
	// condition,
	// int pageNo) {
	//
	// DetachedCriteria dc = makeDetachedCriteria(condition, true);
	//
	// ProjectionList proList = Projections.projectionList();// 设置投影集合
	//
	// // proList = HibernateTool.injectAlias(proList, ALIAS_MAPPING);
	//
	// proList.add(Projections.groupProperty("m.id").as("materialId"));
	// proList.add(Projections.groupProperty("m.no").as("materialNo"));
	// proList.add(Projections.groupProperty("m.name").as("materialName"));
	// proList.add(Projections.groupProperty("m.spec").as("spec"));
	// proList.add(Projections.groupProperty("m.graphNo").as("graphNo"));
	// proList.add(Projections.sum("uu.quantity").as("quantity"));
	//
	// dc.setProjection(proList);
	// dc.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
	//
	// PaginationSupport<Map<String, Object>> es = super.findPageByCriteria(
	// dc, new PageInfo(pageNo), Order.desc("m.no"));
	// return MapList.createPage(es);
	//
	// }

	@Override
	public void restore(StockLog stockLog) {
		// TODO Auto-generated method stub

	}

	private static final String[] ALIAS_MAPPING = new String[] { "m.no", "no",
			"m.name", "name", "uu.quantity", "quantity", "b.name", "branchName","m.unit","materialUnit" };

	// private static final String[] SINGLE_MAPPING = new String[] { "id", "no",
	// "name", "birthday", "status", "gender", "level", "enabled" };
	//
	// private static final String[] ALIAS_MAPPING = new String[] { "b.name",
	// "branchName", "b.id", "branchId" };

	// private static final String[] SINGLE_MAPPING_FULL = new String[] { "id",
	// "no", "name", "birthday", "status", "gender", "hjAddress" };

	@Override
	public PaginationSupport<MapBean> searchMapBean(StockCondition condition,
			int pageIndex) {
		DetachedCriteria dc = makeDetachedCriteria(condition);
		ProjectionList proList = Projections.projectionList();// 设置投影集合

		// proList = HibernateTool.injectSingle(proList, SINGLE_MAPPING, "uu");
		proList = HibernateTool.injectAlias(proList, ALIAS_MAPPING);
		dc.setProjection(proList);
		dc.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);

		PaginationSupport<Map<String, Object>> es = super.findPageByCriteria(
				dc, new PageInfo(pageIndex), Order.asc("m.no"));
		return MapList.createPage(es);
	}

	@Override
	public void update(StockLog... stockLog) {

		// 处理所有stockLog
		// 该扣扣，该加加

		Map<String, Stock> stocksMap = StockHelper.merge(stockLog);

		for (Stock s : stocksMap.values()) {
			this.updateStock(s);
		}

	}

}
