package pula.sys.daos.impl;

import java.util.Calendar;
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
import puerta.support.dao.QueryJedi;
import puerta.support.utils.RandomTool;
import puerta.support.utils.WxlSugar;
import puerta.system.base.HibernateGenericDao;
import puerta.system.vo.MapBean;
import puerta.system.vo.MapList;
import pula.sys.conditions.StudentCondition;
import pula.sys.daos.StudentDao;
import pula.sys.domains.Student;
import pula.sys.helpers.StudentHelper;

@Repository
public class StudentDaoImpl extends HibernateGenericDao<Student, Long>
		implements StudentDao {

	@Override
	public void updatePoints(int points, long id) {
		String sql = "update Student set points =? where id=?";
		updateBatch(sql, points, id);

	}

	private DetachedCriteria makeDetachedCriteria(StudentCondition condition) {
		DetachedCriteria dc = super.makeDetachedCriteria(condition);

		// dc.createAlias("uu.assignments", "ass", DetachedCriteria.LEFT_JOIN,
		// Restrictions.eq("ass.current", true));

		dc.createAlias("uu.branch", "b", DetachedCriteria.LEFT_JOIN);

		// HibernateTool.eqIfNotZero(dc, "status", condition.getStatus());
		HibernateTool.eqIfNotZero(dc, "gender", condition.getGender());
		HibernateTool.eqIfNotZero(dc, "id", condition.getId());

		// HibernateTool.likeIfNotEmpty(dc, "identity",
		// condition.getIdentity());

		HibernateTool.eqIfNotZero(dc, "b.id", condition.getBranchId());
		// HibernateTool.eqIfNotZero(dc, "level", condition.getLevel());
		HibernateTool.likeIfNotEmpty(dc, "barcode", condition.getBarcode());

		HibernateTool.eqIfHas(dc, "enabled", condition.getEnabledStatus());
		return dc;
	}

	private static final String[] SINGLE_MAPPING = new String[] { "id", "no",
			"name", "birthday", "enabled", "gender", "loginId", "parentName",
			"barcode" };

	private static final String[] ALIAS_MAPPING = new String[] { "b.name",
			"branchName", "b.id", "branchId" };

	private static final String[] SINGLE_MAPPING_FULL = new String[] { "id",
			"no", "name", "birthday", "parentName", "gender", "barcode",
			"loginId", "mobile", "phone", "email", "parentCaption" };

	@Override
	public PaginationSupport<MapBean> search(StudentCondition condition,
			int pageIndex) {
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

	@Override
	public Student update(Student rt, boolean changePassword) {
		Student po = this.findById(rt.getId());
		// po.setAttachmentKey(rt.getAttachmentKey());
		po.setBarcode(rt.getBarcode());
		po.setBirthday(rt.getBirthday());
		// po.setBranch(rt.getBranch());
		po.setCreator(rt.getCreator());
		po.setEmail(rt.getEmail());
		po.setGender(rt.getGender());
		// po.setId(rt.getId());
		po.setLoginId(rt.getLoginId());
		po.setMobile(rt.getMobile());
		po.setName(rt.getName());
		// po.setNo(rt.getNo());
		po.setParentCaption(rt.getParentCaption());
		po.setParentName(rt.getParentName());
		po.setAddress(rt.getAddress());
		po.setZip(rt.getZip());
		po.setComments(rt.getComments());

		if (changePassword) {

			po.setPassword(StudentHelper.makePassword(rt.getPassword()));

		}
		po.setPhone(rt.getPhone());
		po.setUpdater(rt.getUpdater());
		_update(po);
		return po;

	}

	@Override
	public MapBean unique(Long id) {
		StudentCondition condition = new StudentCondition();
		condition.setId(id);

		DetachedCriteria dc = makeDetachedCriteria(condition);
		dc.createAlias("a.office", "o", DetachedCriteria.LEFT_JOIN);
		ProjectionList proList = Projections.projectionList();// 设置投影集合

		proList = HibernateTool
				.injectSingle(proList, SINGLE_MAPPING_FULL, "uu");
		proList = HibernateTool.injectAlias(proList, ALIAS_MAPPING);
		dc.setProjection(proList);
		dc.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);

		Map<String, Object> map = super.uniqueResult(dc);

		return MapBean.map(map);
	}

	@Override
	public Student save(Student cc) {

		// if (existsNo(cc.getNo())) {
		// Pe.raise("编号已经存在:" + cc.getNo());
		// }

		cc.setCreatedTime(Calendar.getInstance());
		cc.setUpdatedTime(cc.getCreatedTime());
		cc.setUpdater(cc.getCreator());
		cc.setAttachmentKey(RandomTool.getRandomString(10));

		_save(cc);
		return cc;
	}

	@Override
	public MapList loadByKeywords(String no, long branchId) {

		String hql = "select no as no , name as name FROM Student "
				+ "WHERE (no LIKE ? or name LIKE ?  ) AND removed=?";

		String v = "%" + StringUtils.defaultIfEmpty(no, "") + "%";
		QueryJedi qj = new QueryJedi(hql, v, v, false);

		qj.eqIfNotZero("branch.id", branchId);
		qj.append(" ORDER BY no");

		return mapListLimit(qj.hql(), 40, qj.parameters());
	}

	@Override
	public void checkOwner(Long[] id, long branchId) {
		QueryJedi qj = new QueryJedi(
				"select count(id) from Student where branch.id=? and ",
				branchId);

		qj.eqOr("id", WxlSugar.asObjects(id));

		int c = getInt(qj.hql(), qj.parameters());
		if (c != id.length) {
			Pe.raise("包含越权访问的数据");
		}

	}

	@Override
	public Long getIdByNoWithBranchId(String studentNo, long branch_id) {
		String sql = "select id from Student where no=? and branch.id=? and removed=? ";
		return findSingle(sql, studentNo, branch_id, false);
	}

	@Override
	public MapBean metaByNo(String no, long branch_id) {
		String sql = "select s.id as id ,s.birthday as birthday ,s.gender as gender,s.attachmentKey as attachmentKey from Student s where s.no=? and s.branch.id=? and s.removed=?";
		return mapBean(sql, no, branch_id, false);
	}

	@Override
	public MapBean meta4exhange(String no) {
		String sql = "select s.id as id ,s.points as points,s.branch.id as branchId from Student s where s.no=? and s.removed=?";
		return mapBean(sql, no, false);
	}

	@Override
	public void checkAllowView(long id, long branch_id) {
		String sql = "select id from Student where id=? and branch.id=? and removed=? ";
		if (!exists(sql, id, branch_id, false)) {
			Pe.raise("越权访问");
		}

	}

	@Override
	public MapBean meta(Long id) {
		String sql = "select no as no,name as name from Student where id=?";
		return mapBean(sql, id);
	}

	@Override
	public MapBean meta4login(String loginId, String password) {
		String sql = "select u.name as name, u.id  as id,u.no as loginId,u.password as password,b.id as branchId,b.name as branchName, b.no as branchNo"
				+ " from Student u left join u.branch as b where u.no=? and u.enabled=? and u.removed=?";
		MapBean mb = mapBean(sql, loginId, true, false);

		if (mb == null)
			return null;

		String pwd = mb.string("password");
		String sentPwd = StudentHelper.makePassword(password);
		if (StringUtils.equals(pwd, sentPwd)) {
			mb.remove("password");
		}
		return mb;
	}

	@Override
	public Long getBranchId(Long actorId) {
		String sql = "select b.id as branchId "
				+ " from Student u left join u.branch as b where u.id=? and u.enabled=? and u.removed=?";

		return findSingle(sql, actorId, true, false);
	}

	@Override
	public Long updatePassword(Long actorId, String oldPassword,
			String newPassword) {

		// 旧密码对吗?

		String op = StudentHelper.makePassword(oldPassword);
		String np = StudentHelper.makePassword(newPassword);

		// 更新一下密码!

		String sql = "select b.id as branchId,u.password as password "
				+ " from Student u left join u.branch as b where u.id=? and u.enabled=? and u.removed=?";

		MapBean mb = mapBean(sql, actorId, true, false);

		String nowPassword = mb.string("password");
		if (!StringUtils.equals(nowPassword, op)) {
			Pe.raise("当前密码错误");
		}

		// 改成新密码

		sql = "update Student set password=? where id=?";
		updateBatch(sql, np, actorId);

		return mb.asLong("branchId");

	}

	@Override
	public int getTotal(long actorId) {
		String sql = "select points from Student where id=?";
		return getInt(sql, actorId);
	}
}
