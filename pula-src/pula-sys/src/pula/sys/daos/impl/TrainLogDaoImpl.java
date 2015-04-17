package pula.sys.daos.impl;

import java.util.Calendar;
import java.util.Map;

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
import puerta.support.dao.QueryJedi;
import puerta.support.utils.WxlSugar;
import puerta.system.base.HibernateGenericDao;
import puerta.system.vo.MapBean;
import puerta.system.vo.MapList;
import pula.sys.conditions.TrainLogCondition;
import pula.sys.daos.TrainLogDao;
import pula.sys.domains.TrainLog;
import pula.sys.helpers.TrainLogHelper;

@Repository
public class TrainLogDaoImpl extends HibernateGenericDao<TrainLog, Long>
		implements TrainLogDao {

	@Override
	public TrainLog save(TrainLog cc) {
		cc.setCreatedTime(Calendar.getInstance());
		cc.setUpdater(cc.getCreator());
		cc.setUpdatedTime(cc.getCreatedTime());

		_save(cc);
		return cc;
	}

	private static final String[] SINGLE_MAPPING = new String[] { "id",
			"location", "trainer", "content", "trainDate", "createdTime" };

	private static final String[] ALIAS_MAPPING = new String[] { "b.name",
			"branchName", "b.id", "branchId", "c.name", "creatorName" };

	private static final String[] SINGLE_MAPPING_FULL = new String[] { "id",
			"no", "name", "birthday", "parentName", "gender", "barcode",
			"loginId", "mobile", "phone", "email", "parentCaption" };

	@Override
	public PaginationSupport<MapBean> search(TrainLogCondition condition,
			int pageIndex) {
		DetachedCriteria dc = makeDetachedCriteria(condition);
		ProjectionList proList = Projections.projectionList();// 设置投影集合

		proList = HibernateTool.injectSingle(proList, SINGLE_MAPPING, "uu");
		proList = HibernateTool.injectAlias(proList, ALIAS_MAPPING);
		dc.setProjection(proList);
		dc.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);

		PaginationSupport<Map<String, Object>> es = super.findPageByCriteria(
				dc, new PageInfo(pageIndex), Order.desc("uu.trainDate"));
		return MapList.createPage(es);
	}

	private DetachedCriteria makeDetachedCriteria(TrainLogCondition condition) {
		DetachedCriteria dc = super.makeDetachedCriteria(condition);

		// dc.createAlias("uu.assignments", "ass", DetachedCriteria.LEFT_JOIN,
		// Restrictions.eq("ass.current", true));

		dc.createAlias("uu.branch", "b", DetachedCriteria.LEFT_JOIN);
		dc.createAlias("uu.creator", "c", DetachedCriteria.LEFT_JOIN);

		// HibernateTool.eqIfNotZero(dc, "status", condition.getStatus());

		HibernateTool.eqIfNotZero(dc, "id", condition.getId());

		// HibernateTool.likeIfNotEmpty(dc, "identity",
		// condition.getIdentity());

		HibernateTool.eqIfNotZero(dc, "b.id", condition.getBranchId());
		// HibernateTool.eqIfNotZero(dc, "level", condition.getLevel());
		HibernateTool.likeIfNotEmpty(dc, "location", "content", "trainer",
				"comments", condition.getKeywords());

		HibernateTool.betweenIfNotNull(dc, "trainDate",
				condition.getBeginDate(), condition.getEndDate(), -1);
		return dc;
	}

	@Override
	public TrainLog update(TrainLog rt) {

		TrainLog po = this.findById(rt.getId());
		po.setComments(rt.getComments());
		po.setContent(rt.getContent());
		po.setLocation(rt.getLocation());
		po.setTrainDate(rt.getTrainDate());
		po.setTrainer(rt.getTrainer());
		po.setUpdater(rt.getUpdater());

		po.setUpdatedTime(Calendar.getInstance());
		_update(po);
		return po;

	}

	@Override
	public void checkAllowEdit(long id, long branchId) {
		// 检查时间，及分部

		Calendar allowEditDate = TrainLogHelper.getAllowEditDate();
		String sql = "select id from TrainLog where id=? and branch.id=? and trainDate>=? and removed=?";

		if (!exists(sql, id, branchId, allowEditDate, false)) {
			Pe.raise("不允许修改数据或越权访问");
		}

	}

	@Override
	public void checkAllowView(long id, long branchId) {
		String sql = "select id from TrainLog where id=? and branch.id=? and removed=?";

		if (!exists(sql, id, branchId, false)) {
			Pe.raise("不允许查看数据或越权访问");
		}

	}

	@Override
	public void checkAllowRemove(Long[] id, long branch_id) {

		Calendar allowEditDate = TrainLogHelper.getAllowEditDate();
		// 时间也是关键哦！！
		QueryJedi qj = new QueryJedi(
				"select count(id) from Student where branch.id=? and trainDate>=? and ",
				branch_id, allowEditDate);

		qj.eqOr("id", WxlSugar.asObjects(id));

		int c = getInt(qj.hql(), qj.parameters());
		if (c != id.length) {
			Pe.raise("包含超出可修改时间或越权访问的数据");
		}

	}
}