package pula.sys.daos.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import puerta.support.PageInfo;
import puerta.support.PaginationSupport;
import puerta.support.Pe;
import puerta.support.dao.HibernateTool;
import puerta.system.base.HibernateGenericDao;
import puerta.system.vo.MapBean;
import puerta.system.vo.MapList;
import pula.sys.conditions.MaterialCondition;
import pula.sys.daos.MaterialDao;
import pula.sys.domains.Material;

@Repository
public class MaterialDaoImpl extends HibernateGenericDao<Material, Long>
		implements MaterialDao {

	@Override
	public Material save(Material rt) {

		if (super.existsNo(rt.getNo())) {
			Pe.raise("编号已经存在:" + rt.getNo());
		}

		_save(rt);

		return rt;
	}

	@Override
	public PaginationSupport<Material> search(MaterialCondition condition,
			int pageNo) {
		DetachedCriteria criteria = makeDetachedCriteria(condition);
		return super.findPageByCriteria(criteria, new PageInfo(pageNo),
				Order.asc("no"));
	}

	private DetachedCriteria makeDetachedCriteria(MaterialCondition condition) {
		DetachedCriteria dc = super.makeDetachedCriteria(condition);

		dc.createAlias("uu.category", "c", DetachedCriteria.LEFT_JOIN);

		HibernateTool.eqIfNotEmpty(dc, "c.id", condition.getCategoryId());
		HibernateTool.likeIfNotEmpty(dc, "name", "no", "raw", "unit", "pinyin",
				"brand", condition.getKeywords());
		HibernateTool.eqIfNotZero(dc, "id", condition.getId());
		HibernateTool.eqIfHas(dc, "enabled", condition.getEnabledStatus());
		return dc;
	}

	@Override
	public Material update(Material rt) {
		if (super.existsNo(rt.getNo(), rt.getId())) {
			Pe.raise("编号已经存在:" + rt.getNo());
		}
		Material po = this.findById(rt.getId());
		po.setComments(rt.getComments());
		po.setId(rt.getId());
		po.setCategory(rt.getCategory());
		po.setName(rt.getName());
		po.setNo(rt.getNo());
		po.setRaw(rt.getRaw());
		po.setBrand(rt.getBrand());
		po.setSuperficialArea(rt.getSuperficialArea());
		po.setWeight(rt.getWeight());
		po.setPinyin(rt.getPinyin());
		po.setUnit(rt.getUnit());

		_update(po);
		return po;

	}

	@Override
	public List<Material> loadByKeywords(String no) {
		String hql = "FROM Material WHERE (no LIKE ? or name LIKE ? ) AND removed=? and enabled=? ORDER BY no";
		String v = "%" + StringUtils.defaultIfEmpty(no, "") + "%";
		return findLimitByQuery(hql, 40, v, v, false, true);
	}

	@Override
	public Material fetchRef(String name, String spec) {

		String hql = "Select u.id from Material u where u.name=? and u.spec=? and u.removed=? and u.enabled=?";
		return Material.create((Long) findSingle(hql, name, spec, false, true));
	}

	@Override
	public List<Material> loadSuggest(String q, int i) {
		String sql = "select u from Material u where (u.name LIKE ? or u.pinyin LIKE ? or u.no LIKE ? ) and u.removed=? and u.enabled=? ORDER BY u.no ";
		String qs = "%" + q + "%";
		return findLimitByQuery(sql, i, qs, qs, qs, false, true);
	}

	// @Override
	// public Material fetch(PartImport pi) {
	// String sql = "select u from Material u where u.no=? and u.removed=?";
	// Material m = findSingle(sql, pi.getMaterialNo(), false);
	// if (m != null) {
	// return m;
	// }
	//
	// m = pi.toMaterial();
	//
	// getHibernateTemplate().save(m);
	// return m;
	// }

	@Override
	public MapBean getMetaData(Long key) {
		String sql = "select u.id as id ,u.no as materialNo,u.name as materialName,u.graphNo as graphNo,"
				+ "u	.spec as spec from Material u where u.id=?";

		return mapBean(sql, key);

	}

	@Override
	public MapBean mapBeanByNo(String no) {

		String sql = "select u.id as id ,u.no as materialNo,u.name as materialName,u.brand as brand,"
				+ "u.unit as unitl from Material u where u.no=? and removed=?";

		return mapBean(sql, no, false);
	}

	@Override
	public void saveOrUpdate(Material material) {
		Long id = getIdByNo(material.getNo());

		if (id == null) {
			getHibernateTemplate().save(material);
		} else {
			material.setId(id);
			getHibernateTemplate().update(material);
		}

	}

	@Override
	public MapBean unique(Long id) {
		MaterialCondition condition = new MaterialCondition();
		condition.setId(id);

		DetachedCriteria dc = makeDetachedCriteria(condition);
		ProjectionList proList = Projections.projectionList();// 设置投影集合

		proList = HibernateTool
				.injectSingle(proList, SINGLE_MAPPING_FULL, "uu");
		proList = HibernateTool.injectAlias(proList, ALIAS_MAPPING);
		dc.setProjection(proList);
		dc.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);

		Map<String, Object> map = super.uniqueResult(dc);

		return MapBean.map(map);
	}

	private static final String[] SINGLE_MAPPING = new String[] { "id", "no",
			"name", "pinyin", "raw", "brand", "unit", "enabled" };

	private static final String[] ALIAS_MAPPING = new String[] { "c.name",
			"categoryName", "c.id", "categoryId" };

	// private static final String[] SINGLE_MAPPING_FULL = new String[] { "id",
	// "no", "name", "birthday", "status", "gender", "hjAddress" };

	private static final String[] SINGLE_MAPPING_FULL = new String[] { "id",
			"no", "name", "pinyin", "raw", "brand", "unit", "comments" };

	@Override
	public PaginationSupport<MapBean> searchMapBean(
			MaterialCondition condition, int pageIndex) {
		DetachedCriteria dc = makeDetachedCriteria(condition);
		ProjectionList proList = Projections.projectionList();// 设置投影集合

		proList = HibernateTool.injectSingle(proList, SINGLE_MAPPING, "uu");
		proList = HibernateTool.injectAlias(proList, ALIAS_MAPPING);
		dc.setProjection(proList);
		dc.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);

		PaginationSupport<Map<String, Object>> es = super.findPageByCriteria(
				dc, new PageInfo(pageIndex), Order.asc("uu.no"));
		return MapList.createPage(es);
	}

	// @Override
	// public Material saveStandard(PartForm af) {
	// Material m = new Material();
	// m.setName(af.getName());
	// m.setNo(String.valueOf(Calendar.getInstance().getTimeInMillis())
	// + RandomTool.getRandomString(5));
	// m.setRaw(af.getRaw());
	// m.setSpec(af.getSpec());
	//
	// return this.save(m);
	// }

}
