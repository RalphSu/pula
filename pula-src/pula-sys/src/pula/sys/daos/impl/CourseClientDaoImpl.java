package pula.sys.daos.impl;

import java.util.Calendar;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;
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
import puerta.support.utils.MD5;
import puerta.support.utils.RandomTool;
import puerta.system.base.HibernateGenericDao;
import puerta.system.vo.MapBean;
import puerta.system.vo.MapList;
import pula.sys.conditions.CourseClientCondition;
import pula.sys.daos.CourseClientDao;
import pula.sys.domains.CourseClient;
import pula.sys.helpers.LicenseKeyHelper;

@Repository
public class CourseClientDaoImpl extends
		HibernateGenericDao<CourseClient, Long> implements CourseClientDao {

	private static final String[] SINGLE_MAPPING = new String[] { "id", "name",
			"machineNo", "status", "enabled", "createdTime", "applyTime",
			"expiredTime", "ip" };

	private static final String[] ALIAS_MAPPING = new String[] { "b.name",
			"branchName", "b.id", "branchId", "clr.id", "classroomId",
			"clr.name", "classroomName", "app.name", "applierName" };

	private static final String[] SINGLE_MAPPING_EXT = new String[] {
			"comments", "applyComments", "licenseKey" };

	private static final String PREFIX = "IGRANT";

	@Override
	public PaginationSupport<MapBean> search(CourseClientCondition condition,
			int pageIndex) {
		DetachedCriteria dc = makeDetachedCriteria(condition);
		ProjectionList proList = Projections.projectionList();// 设置投影集合

		proList = HibernateTool.injectSingle(proList, SINGLE_MAPPING, "uu");
		proList = HibernateTool.injectAlias(proList, ALIAS_MAPPING);
		dc.setProjection(proList);
		dc.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		String dateField = "uu.applyTime";
		if (condition.isForApply()) {
			dateField = "uu.createdTime";
		}
		PaginationSupport<Map<String, Object>> es = super.findPageByCriteria(
				dc, new PageInfo(pageIndex), Order.desc(dateField));
		return MapList.createPage(es);
	}

	private DetachedCriteria makeDetachedCriteria(
			CourseClientCondition condition) {
		DetachedCriteria dc = super.makeDetachedCriteria(condition);

		dc.createAlias("uu.branch", "b", DetachedCriteria.LEFT_JOIN);
		dc.createAlias("uu.classroom", "clr", DetachedCriteria.LEFT_JOIN);
		dc.createAlias("uu.applier", "app", DetachedCriteria.LEFT_JOIN);

		HibernateTool.eqIfNotZero(dc, "status", condition.getStatus());
		HibernateTool.eqIfNotZero(dc, "id", condition.getId());

		HibernateTool.eqIfNotZero(dc, "b.id", condition.getBranchId());
		HibernateTool.eqIfNotZero(dc, "clr.id", condition.getClassroomId());

		HibernateTool.likeIfNotEmpty(dc, "uu.machineNo",
				condition.getMachineNo());

		String dateField = "uu.applyTime";
		if (condition.isForApply()) {
			dateField = "uu.createdTime";
		}
		HibernateTool.betweenIfNotNull(dc, dateField, condition.getBeginDate(),
				condition.getEndDate(), -1);

		return dc;
	}

	@Override
	public MapBean unique(long id) {
		CourseClientCondition condition = new CourseClientCondition();
		condition.setId(id);

		DetachedCriteria dc = makeDetachedCriteria(condition);

		ProjectionList proList = Projections.projectionList();// 设置投影集合

		proList = HibernateTool.injectSingle(proList, SINGLE_MAPPING, "uu");
		proList = HibernateTool.injectSingle(proList, SINGLE_MAPPING_EXT, "uu");
		proList = HibernateTool.injectAlias(proList, ALIAS_MAPPING);
		dc.setProjection(proList);
		dc.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);

		Map<String, Object> map = super.uniqueResult(dc);

		return MapBean.map(map);
	}

	@Override
	public void apply(CourseClient courseClient) {
		// 受理！

		int[] array = new int[] { CourseClient.STATUS_NORMAL,
				CourseClient.STATUS_INGORE, CourseClient.STATUS_LOCKED

		};

		// 什么情况？是受理了还是？
		if (!ArrayUtils.contains(array, courseClient.getStatus())) {
			Pe.raise("状态位错误");
		}

		CourseClient po = this.findById(courseClient.getId());
		if (po.getStatus() != CourseClient.STATUS_NEW) {
			Pe.raise("原状态位错误，不允许受理");
		}

		// 开始处理
		if (courseClient.getStatus() == CourseClient.STATUS_NORMAL) {

			// 当前同机是否已经有激活的？
			// if( existsNormal(po.getMachineNo()) ){
			//
			// }
			// 强制取消
			forceNormalToCancel(po.getMachineNo());
			po.setExpiredTime(courseClient.getExpiredTime());
			po.setLicenseKey(LicenseKeyHelper.generate(po.getMachineNo(),
					po.getExpiredTime()));
			po.setEnabled(true);

		}

		po.setApplier(courseClient.getApplier());
		po.setApplyTime(Calendar.getInstance());
		po.setApplyComments(courseClient.getApplyComments());

		// location
		po.setBranch(courseClient.getBranch());
		po.setClassroom(courseClient.getClassroom());
		po.setName(courseClient.getName());
		po.setStatus(courseClient.getStatus());

		// 激活码
		po.setLicenseKey(MD5.GetMD5String(PREFIX + po.getMachineNo()
				+ RandomTool.getRandomString(4)));

		loggerDao.doLog("受理", po);

	}

	private void forceNormalToCancel(String machineNo) {
		String sql = "update CourseClient set enabled=?,status=? where machineNo=? and status=?";
		updateBatch(sql, false, CourseClient.STATUS_RENEW, machineNo,
				CourseClient.STATUS_NORMAL);

	}

	@Override
	public void update(CourseClient cc) {

		int[] array = new int[] { CourseClient.STATUS_NORMAL,
				CourseClient.STATUS_INGORE, CourseClient.STATUS_LOCKED

		};

		// 什么情况？是受理了还是？
		if (!ArrayUtils.contains(array, cc.getStatus())) {
			Pe.raise("状态位错误");
		}

		CourseClient po = this.findById(cc.getId());
		// if (po.getStatus() != CourseClient.STATUS_NEW) {
		// Pe.raise("原状态位错误，不允许受理");
		// }

		// 开始处理
		if (cc.getStatus() == CourseClient.STATUS_NORMAL) {

			// 当前同机是否已经有激活的？

			// 强制取消
			forceNormalToCancel(po.getMachineNo());
			if (diff(po.getExpiredTime(), cc.getExpiredTime())) {
				po.setExpiredTime(cc.getExpiredTime());
				po.setLicenseKey(LicenseKeyHelper.generate(po.getMachineNo(),
						po.getExpiredTime()));
			}
			po.setEnabled(true);

		}

		// po.setApplier(cc.getApplier());
		// po.setApplyTime(Calendar.getInstance());
		po.setApplyComments(cc.getApplyComments());

		// location
		if (cc.getBranch() != null)
			po.setBranch(cc.getBranch());
		if (cc.getClassroom() != null)
			po.setClassroom(cc.getClassroom());
		if (!StringUtils.isEmpty(cc.getName())) {
			po.setName(cc.getName());
		}
		po.setStatus(cc.getStatus());

		_update(po);

	}

	private boolean diff(Calendar expiredTime, Calendar expiredTime2) {
		if (expiredTime == expiredTime2) {
			return false;
		}
		if (expiredTime == null && expiredTime2 != null) {
			return true;
		}
		if (expiredTime != null && expiredTime2 == null) {
			return true;
		}

		if (expiredTime.equals(expiredTime2)) {
			return false;
		}
		return true;
	}

	@Override
	public void request(String code, String comments, String ip) {
		CourseClient cc = new CourseClient();
		cc.setCreatedTime(Calendar.getInstance());
		cc.setComments(comments);
		cc.setRemoved(false);
		cc.setStatus(CourseClient.STATUS_NEW);
		cc.setIp(ip);
		cc.setEnabled(true);
		cc.setMachineNo(code);

		getHibernateTemplate().save(cc);
	}

	@Override
	public MapBean hasRequest(String code) {
		String sql = "select u.licenseKey as licenseKey,u.expiredTime as expiredTime,"
				+ "u.classroom.id as classroomId,u.name as name,u.status as status,expiredTime as expiredTime from CourseClient u where u.machineNo=?"
				+ " and u.removed=? and u.enabled=? ";

		return mapBean(sql, code, false, true);

	}

	@Override
	public Long getClassroomId(String code,String acode) {
		String sql = "select u.classroom.id as classroomId from CourseClient u where u.machineNo=?"
				+ " and u.removed=? and u.enabled=? and u.status=? and u.licenseKey=?";
		return findSingle(sql, code, false, true, CourseClient.STATUS_NORMAL,acode);
	}

	@Override
	public Object[] getClassroomIdAndBranchId(String code, String activeCode) {
		String sql = "select u.classroom.id as classroomId,u.branch.id as branchId from CourseClient u where u.machineNo=?"
			+ " and u.removed=? and u.enabled=? and u.status=? and u.licenseKey=?";
	return findSingle(sql, code, false, true, CourseClient.STATUS_NORMAL,activeCode);
	}
}
