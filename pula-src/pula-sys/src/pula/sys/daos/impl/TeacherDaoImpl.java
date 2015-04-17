package pula.sys.daos.impl;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import puerta.support.PageInfo;
import puerta.support.PaginationSupport;
import puerta.support.Pe;
import puerta.support.dao.HibernateTool;
import puerta.support.dao.QueryJedi;
import puerta.support.utils.RandomTool;
import puerta.system.base.HibernateGenericDao;
import puerta.system.vo.MapBean;
import puerta.system.vo.MapList;
import pula.sys.conditions.TeacherCondition;
import pula.sys.daos.TeacherDao;
import pula.sys.domains.Teacher;
import pula.sys.helpers.TeacherHelper;

@Repository
public class TeacherDaoImpl extends HibernateGenericDao<Teacher, Long>
		implements TeacherDao {

	@Override
	public PaginationSupport<Teacher> search(TeacherCondition condition,
			int pageNo) {
		DetachedCriteria criteria = makeDetachedCriteria(condition);
		return super.findPageByCriteria(criteria, new PageInfo(pageNo),
				Order.desc("no"));
	}

	private DetachedCriteria makeDetachedCriteria(TeacherCondition condition) {
		DetachedCriteria dc = super.makeDetachedCriteria(condition);

		dc.createAlias("uu.assignments", "ass", DetachedCriteria.LEFT_JOIN,
				Restrictions.eq("ass.current", true));

		dc.createAlias("ass.branch", "b", DetachedCriteria.LEFT_JOIN);
		dc.createAlias("uu.cards", "c", DetachedCriteria.LEFT_JOIN,
				Restrictions.eq("c.current", true));

		HibernateTool.eqIfNotZero(dc, "status", condition.getStatus());
		HibernateTool.eqIfNotZero(dc, "gender", condition.getGender());
		HibernateTool.eqIfNotZero(dc, "id", condition.getId());

		HibernateTool.likeIfNotEmpty(dc, "identity", condition.getIdentity());

		HibernateTool.eqIfNotZero(dc, "b.id", condition.getBranchId());
		HibernateTool.eqIfNotZero(dc, "level", condition.getLevel());
		HibernateTool.likeIfNotEmpty(dc, "barcode", condition.getBarcode());
		HibernateTool.eqIfHas(dc, "enabled", condition.getEnabledStatus());

		return dc;
	}

	@Override
	public Teacher save(Teacher cc) {

		if (existsNo(cc.getNo())) {
			Pe.raise("编号已经存在:" + cc.getNo());
		}

		cc.setCreatedTime(Calendar.getInstance());
		cc.setUpdatedTime(cc.getCreatedTime());
		cc.setUpdater(cc.getCreator());
		cc.setAttachmentKey(RandomTool.getRandomString(10));
		_save(cc);
		return cc;
	}

	@Override
	public Teacher update(Teacher rt, boolean changePassword) {

		if (existsNo(rt.getNo(), rt.getId())) {
			Pe.raise("编号已经存在:" + rt.getNo());
		}

		Teacher po = this.findById(rt.getId());
		po.setBirthday(rt.getBirthday());
		po.setComments(rt.getComments());
		// po.setEmployee(rt.getEmployee());
		// po.setFamily(rt.getFamily());
		po.setGender(rt.getGender());
		po.setHjAddress(rt.getHjAddress());
		po.setHomeplace(rt.getHomeplace());
		po.setIdentity(rt.getIdentity());
		po.setJoinTime(rt.getJoinTime());
		po.setLeaveTime(rt.getLeaveTime());
		po.setLiveAddress(rt.getLiveAddress());
		po.setName(rt.getName());
		po.setNo(rt.getNo());
		po.setSchool(rt.getSchool());
		po.setStatus(rt.getStatus());
		po.setSpeciality(rt.getSpeciality());
		po.setZip(rt.getZip());
		po.setEmail(rt.getEmail());
		po.setPhone(rt.getPhone());
		po.setMobile(rt.getMobile());
		po.setBarcode(rt.getBarcode());
		po.setUpdater(rt.getUpdater());
		po.setUpdatedTime(Calendar.getInstance());
		po.setLevel(rt.getLevel());

		po.setLinkman(rt.getLinkman());
		po.setLinkmanCaption(rt.getLinkmanCaption());
		po.setLinkmanTel(rt.getLinkmanTel());

		if (changePassword) {
			String pwd = TeacherHelper.makePassword(rt.getPassword());
			po.setPassword(pwd);

		}

		_update(po);
		return po;

	}

	private static final String[] SINGLE_MAPPING = new String[] { "id", "no",
			"name", "birthday", "status", "gender", "level", "enabled",
			"barcode" };

	private static final String[] ALIAS_MAPPING = new String[] { "b.name",
			"branchName", "b.id", "branchId" };

	private static final String[] SINGLE_MAPPING_FULL = new String[] { "id",
			"no", "name", "birthday", "status", "gender", "hjAddress" };

	@Override
	public PaginationSupport<MapBean> searchMapBean(TeacherCondition condition,
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
	public MapBean unique(Long id) {
		TeacherCondition condition = new TeacherCondition();
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
	public Teacher findRefByAttendanceNo(String no) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MapBean meta(Long id) {
		String sql = "select no as no ,name as name from Teacher where removed=? and id=?";
		return mapBean(sql, false, id);
	}

	@Override
	public MapList listMetaEnabledIn(long idLong) {
		String sql = "select u.id as teacherId, u.no as no ,u.name as name from Teacher u LEFT JOIN u.assignments as ua"
				+ " where u.removed=? and u.enabled=? and u.status=? and ua.branch.id=? and ua.current=?";
		return mapList(sql, false, true, Teacher.ON_DUTY, idLong, true);
	}

	@Override
	public MapList loadByKeywords(String no, long branchId) {

		String hql = "select t.no as no , t.name as name FROM Teacher t "
				+ "WHERE (t.no LIKE ? or t.name LIKE ? ) AND t.removed=? and t.status=? ";

		String v = "%" + StringUtils.defaultIfEmpty(no, "") + "%";
		QueryJedi qj = new QueryJedi(hql, v, v, false, Teacher.ON_DUTY);

		if (branchId != 0) {
			// append to parameter
			qj.append(
					" and exists(select ta.id from TeacherAssignment ta where ta.teacher.id=t.id and ta.branch.id=? )",
					branchId);// and current =true, ?　当下不在也可以引用
		}

		// qj.eqIfNotZero("branch.id", branchId);
		qj.append(" ORDER BY no");

		return mapListLimit(qj.hql(), 40, qj.parameters());
	}

	@Override
	public Long getIdByNoWithBranchId(String teacherNo, long branch_id) {

		String sql = "select t.id from Teacher t where t.no=? and t.removed=? and t.status=? and exists(select "
				+ " ta.id from TeacherAssignment ta where ta.branch.id=? and ta.teacher.id=t.id)";
		return findSingle(sql, teacherNo, false, Teacher.ON_DUTY, branch_id);
	}

	@Override
	public Map<Long, String> allIn(List<Long> teacherIds, long branchId) {
		QueryJedi qj = new QueryJedi(
				"select u.id,u.no from Teacher u where u.removed=? and exists(select "
						+ " ta.id from TeacherAssignment ta where ta.branch.id=? and ta.teacher.id=u.id) and ",
				false, branchId);

		qj.eqOr("u.id", teacherIds.toArray());

		Map<Long, String> m = HibernateTool.asMap(find(qj.hql(),
				qj.parameters()));

		if (m.size() != teacherIds.size()) {
			Pe.raise("包含不属于本分支机构的教师数据");
		}

		return m;

	}

	// @Override
	// public Employee getRefCheckIfExistsFile(String employeeNo, long id) {
	// String sql = "select e.id from Employee e where e.no=? "
	// + " and e.enabled=? and e.removed=? "
	// + " and not exists(select ef.id from Teacher ef where "
	// + "ef.employee.id=e.id and ef.id<>? and ef.removed=?)";
	//
	// Long eid = findSingle(sql, employeeNo, true, false, id, false);
	//
	// if (eid == null) {
	// Pe.raise("无法找到指定的职员编号:" + employeeNo
	// + " \n或该编号被禁用或删除\n 或该编号已关联人员档案");
	// }
	// return Employee.create(eid);
	//
	// }
	//
	// @Override
	// public List<Teacher> loadTimeout() {
	// String sql =
	// "select u from Teacher u where u.removed=? and u.employee.enabled=? and u.employee.removed=?"
	// + " and u.status=? and u.joinTime<=?";
	//
	// Calendar cal = DateJedi.createNow().resetToZero().moveMonth(-3).to();
	//
	// return find(sql, false, true, false, Teacher.OBSERVATION, cal);
	//
	// }

	@Override
	public MapBean meta4plan(String teacherNo, long branch_id) {

		String sql = "select t.id as id,t.name as name ,t.no as no from Teacher t where t.no=? and t.removed=? and t.status=? and exists(select "
				+ " ta.id from TeacherAssignment ta where ta.branch.id=? and ta.teacher.id=t.id)";
		return mapBean(sql, teacherNo, false, Teacher.ON_DUTY, branch_id);
	}

	@Override
	public void checkAllowView(Long id, long branchId) {
		String sql = "select u.id,u.no from Teacher u where u.removed=? and exists(select "
				+ " ta.id from TeacherAssignment ta where ta.branch.id=? and ta.teacher.id=u.id) and id=?";

		if (!exists(sql, false, branchId, id)) {
			Pe.raise("越权访问");
		}
	}

	@Override
	public MapBean meta4login(String loginId, String password) {
		String sql = "select u.id  as id,u.no as loginId,u.name as name ,u.password as password,b.id as branchId,b.name as branchName, b.no as branchNo"
				+ " from Teacher u left join u.assignments ta with ta.current=? left join ta.branch as b where u.no=? and u.enabled=? and u.removed=?";
		MapBean mb = mapBean(sql, true, loginId, true, false);

		if (mb == null)
			return null;

		String pwd = mb.string("password");
		String sentPwd = TeacherHelper.makePassword(password);
		if (StringUtils.equals(pwd, sentPwd)) {
			mb.remove("password");
			return mb;
		}

		return null;

	}

	@Override
	public Long getBranchId(Long actorId) {
		String sql = "select b.id as branchId "
				+ " from Teacher u left join u.assignments ta with ta.current=? left join ta.branch as b where u.id=? and u.enabled=? and u.removed=?";

		return findSingle(sql, true, actorId, true, false);
	}

	@Override
	public Long updatePassword(Long actorId, String oldPassword,
			String newPassword) {

		// 旧密码对吗?

		String op = TeacherHelper.makePassword(oldPassword);
		String np = TeacherHelper.makePassword(newPassword);

		// 更新一下密码!

		String sql = "select b.id as branchId,u.password as password "
				+ " from Teacher u left join u.assignments ta with ta.current=? left join ta.branch as b where u.id=? and u.enabled=? and u.removed=?";

		MapBean mb = mapBean(sql, true, actorId, true, false);

		String nowPassword = mb.string("password");
		if (!StringUtils.equals(nowPassword, op)) {
			Pe.raise("当前密码错误");
		}

		// 改成新密码

		sql = "update Teacher set password=? where id=?";
		updateBatch(sql, np, actorId);

		return mb.asLong("branchId");

	}
}
