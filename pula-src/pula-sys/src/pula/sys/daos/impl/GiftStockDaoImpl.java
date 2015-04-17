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
import pula.sys.conditions.GiftStockCondition;
import pula.sys.daos.GiftStockDao;
import pula.sys.domains.Branch;
import pula.sys.domains.Gift;
import pula.sys.domains.GiftStock;
import pula.sys.domains.GiftStockLog;
import pula.sys.helpers.GiftStockHelper;

@Repository
public class GiftStockDaoImpl extends HibernateGenericDao<GiftStock, Long>
		implements GiftStockDao {

	// @Override
	// public void rollback(GiftStockIn old) {
	// // 根据入库单明细恢复库存
	//
	// // 载入入库单明细
	// for (GiftStockInItem item : old.getItems()) {
	// // 逆向
	// GiftStock s = new GiftStock();
	// s.setGift(item.getGift());
	// s.setQuantity(item.getQuantity() * -1);
	// s.setSpace(item.getSpace());
	// // s.setWarehouse(item.getSpace())
	//
	// getHibernateTemplate().save(s);
	// }
	//
	// }

	// private static final String[] ALIAS_MAPPING_AVAILABLE = new String[] {
	// "m.no", "giftNo", "m.name", "giftName", "m.spec", "spec",
	// "m.graphNo", "graphNo" };

	// @Override
	// public void update(GiftStockLog out) {
	//
	// Map<String, GiftStock> map = GiftStockHelper.merge(stockIn, out);
	// // 区域预检（看是否锁定）
	// List<Long> spacePreCheck = WxlSugar.newArrayList();
	// for (GiftStock s : map.values()) {
	// // precheckWarehouseSpace(spacePreCheck, s);
	// updateGiftStock(s);
	// }
	//
	// }

	private void updateGiftStock(GiftStock item) {

		GiftStock stock = findGiftStockRef(item.getGift(), item.getBranch());
		if (stock == null) {
			stock = new GiftStock();
			stock.setGift(item.getGift());
			stock.setQuantity(item.getQuantity());
			stock.setBranch(item.getBranch());
			getHibernateTemplate().save(stock);
		} else {
			// 追加数量即可
			String sql = "update GiftStock set quantity=quantity+? where id=?";
			updateBatch(sql, item.getQuantity(), stock.getId());
		}

	}

	private GiftStock findGiftStockRef(Gift gift, Branch branch) {
		String sql = "select u.id from GiftStock u where u.removed=? and u.branch.id=? and u.gift.id=?";
		Long s = findSingle(sql, false, branch.getId(), gift.getId());
		if (s == null) {
			return null;
		}
		return GiftStock.create(s);
	}

	@Override
	public MapList listBySpaceId(long id) {
		String sql = "select m.no as no,m.name as name,m.graphNo as picNo,m.spec as spec,u.quantity as quantity"
				+ " from GiftStock u,Material m where u.gift.id=m.id and u.space.id=? and u.removed=? and u.quantity!=0";

		return super.mapList(sql, id, false);
	}

	@Override
	public Map<String, Integer> mapByBranchId(long id) {
		String sql = "select m.no ,u.quantity as quantity"
				+ " from GiftStock u,Material m where u.gift.id=m.id and u.space.id=? and u.removed=?";

		return HibernateTool.asMap(find(sql, id, false));

	}

	// @Override
	// public void update(GiftStockOut stockOut, GiftStockOut out) {
	// Map<String, GiftStock> map = GiftStockHelper.merge(stockOut, out);
	// List<Long> spacePreCheck = WxlSugar.newArrayList();
	// for (GiftStock s : map.values()) {
	// precheckWarehouseSpace(spacePreCheck, s);
	// updateGiftStock(s);
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
	// GiftStock s = new GiftStock();
	// s.setGift(item.getGift());
	// s.setQuantity(want);
	// s.setWarehouse(note.getSpace().getWarehouse());
	// s.setSpace(note.getSpace());
	// // 盘点单结转，不需要检查锁定！
	// // precheckWarehouseSpace(spacePreCheck, s);
	//
	// updateGiftStock(s);
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
	// GiftStock s = new GiftStock();
	// s.setGift(item.getGift());
	// s.setQuantity(want * -1);
	// s.setWarehouse(note.getSpace().getWarehouse());
	// s.setSpace(note.getSpace());
	//
	// precheckWarehouseSpace(spacePreCheck, s);
	//
	// updateGiftStock(s);
	// }
	// }

	// @Override
	// public void update(TransferOrder si, TransferOrder old) {
	// Map<String, GiftStock> map = GiftStockHelper.merge(si, old);
	//
	// List<Long> spacePreCheck = WxlSugar.newArrayList();
	// for (GiftStock s : map.values()) {
	// precheckWarehouseSpace(spacePreCheck, s);
	// updateGiftStock(s);
	// }
	//
	// }

	@Override
	public PaginationSupport<GiftStock> search(GiftStockCondition condition,
			int pageNo) {
		DetachedCriteria criteria = makeDetachedCriteria(condition);
		return super.findPageByCriteria(criteria, new PageInfo(pageNo),
				Order.desc("m.no"));
	}

	private DetachedCriteria makeDetachedCriteria(GiftStockCondition condition) {
		DetachedCriteria dc = DetachedCriteria.forClass(GiftStock.class, "uu");

		dc.createAlias("uu.gift", "m", DetachedCriteria.LEFT_JOIN);
		dc.createAlias("uu.branch", "b", DetachedCriteria.LEFT_JOIN);

		HibernateTool.eqIfNotEmpty(dc, "m.no", condition.getGiftNo());
		HibernateTool.eqIfNotZero(dc, "b.id", condition.getBranchId());

		dc.add(Restrictions.eq("uu.removed", false));

		dc.add(Restrictions.ne("uu.quantity", 0));

		return dc;
	}

	@Override
	public MapList export(GiftStockCondition condition) {
		DetachedCriteria dc = makeDetachedCriteria(condition);
		ProjectionList proList = Projections.projectionList();// 设置投影集合

		proList = HibernateTool.injectAlias(proList, ALIAS_MAPPING);
		dc.setProjection(proList);
		dc.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		dc.addOrder(Order.desc("m.no"));

		List<Map<String, Object>> es = super.findByCriteria(dc);
		return MapList.create(es);
	}

	// stockQty,stockMax,stockMin,giftId
	// @Override
	// public MapBean getAlert(Long m) {
	// String sql =
	// "select m.id as id ,m.stockMin as stockMin,m.stockMax as stockMax,"
	// + "s.quantity as stockQuantity from GiftStock s,"
	// + "Material m where m.id=s.gift.id and m.id=? "
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
	// "select m.id as giftId,u.quantity as quantity from GiftStock u,Material m where u.removed=? and m.id=u.gift.id"
	// +
	// " and (exists (select p.id from Part p where p.gift.id=m.id and p.productVersion.id=?)"
	// +
	// " or exists(select p.id from StandardPart p where p.gift.id=m.id and p.productVersion.id=?))";
	//
	// return mapList(sql, false, id, id);
	//
	// }
	//
	// @Override
	// public PaginationSupport<MapBean> searchAvailable(GiftStockCondition
	// condition,
	// int pageNo) {
	//
	// DetachedCriteria dc = makeDetachedCriteria(condition, true);
	//
	// ProjectionList proList = Projections.projectionList();// 设置投影集合
	//
	// // proList = HibernateTool.injectAlias(proList, ALIAS_MAPPING);
	//
	// proList.add(Projections.groupProperty("m.id").as("giftId"));
	// proList.add(Projections.groupProperty("m.no").as("giftNo"));
	// proList.add(Projections.groupProperty("m.name").as("giftName"));
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
	public void restore(GiftStockLog stockLog) {
		// TODO Auto-generated method stub

	}

	private static final String[] ALIAS_MAPPING = new String[] { "m.no", "no",
			"m.name", "name", "uu.quantity", "quantity", "b.name",
			"branchName", "m.unit", "giftUnit" };

	// private static final String[] SINGLE_MAPPING = new String[] { "id", "no",
	// "name", "birthday", "status", "gender", "level", "enabled" };
	//
	// private static final String[] ALIAS_MAPPING = new String[] { "b.name",
	// "branchName", "b.id", "branchId" };

	// private static final String[] SINGLE_MAPPING_FULL = new String[] { "id",
	// "no", "name", "birthday", "status", "gender", "hjAddress" };

	@Override
	public PaginationSupport<MapBean> searchMapBean(
			GiftStockCondition condition, int pageIndex) {
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
	public void update(GiftStockLog... stockLog) {

		// 处理所有stockLog
		// 该扣扣，该加加

		Map<String, GiftStock> stocksMap = GiftStockHelper.merge(stockLog);

		for (GiftStock s : stocksMap.values()) {
			this.updateGiftStock(s);
		}

	}

	@Override
	public boolean isEnough(Long giftId, int qty, long branch_id) {
		String sql = "select u.quantity from GiftStock u where u.removed=? and u.branch.id=? and u.gift.id=?";
		Integer s = findSingle(sql, false, branch_id, giftId);
		if (s == null) {
			return false;
		}
		if (s < qty) {
			return false;
		}
		return true;
	}

}
