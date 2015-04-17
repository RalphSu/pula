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
import org.hibernate.criterion.Subqueries;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import puerta.support.PageInfo;
import puerta.support.PaginationSupport;
import puerta.support.Pe;
import puerta.support.dao.HibernateTool;
import puerta.support.dao.QueryJedi;
import puerta.support.utils.DateJedi;
import puerta.support.utils.WxlSugar;
import puerta.support.vo.Mix;
import puerta.system.base.HibernateGenericDao;
import puerta.system.vo.MapBean;
import puerta.system.vo.MapList;
import pula.sys.conditions.CourseTaskResultCondition;
import pula.sys.daos.CourseTaskResultDao;
import pula.sys.domains.CourseTaskResult;
import pula.sys.domains.CourseTaskResultReportComments;
import pula.sys.domains.CourseTaskResultStudent;
import pula.sys.helpers.CourseTaskResultHelper;

@Repository
public class CourseTaskResultDaoImpl extends
		HibernateGenericDao<CourseTaskResult, Long> implements
		CourseTaskResultDao {

	@Override
	public CourseTaskResult save(CourseTaskResult ctr) {
		ctr.setCreatedTime(Calendar.getInstance());
		ctr.setUpdatedTime(ctr.getCreatedTime());
		ctr.setUpdater(ctr.getCreator());
		_save(ctr);
		return ctr;
	}

	private DetachedCriteria makeDetachedCriteria(
			CourseTaskResultCondition condition) {
		DetachedCriteria dc = super.makeDetachedCriteria(condition);

		// dc.createAlias("uu.students", "stu", DetachedCriteria.LEFT_JOIN);
		dc.createAlias("uu.master", "m", DetachedCriteria.LEFT_JOIN);
		dc.createAlias("uu.assistant1", "a1", DetachedCriteria.LEFT_JOIN);
		dc.createAlias("uu.assistant2", "a2", DetachedCriteria.LEFT_JOIN);

		dc.createAlias("uu.branch", "b", DetachedCriteria.LEFT_JOIN);
		dc.createAlias("uu.classroom", "clsr", DetachedCriteria.LEFT_JOIN);
		dc.createAlias("uu.course", "c", DetachedCriteria.LEFT_JOIN);
		dc.createAlias("c.category", "cc", DetachedCriteria.LEFT_JOIN);

		HibernateTool.eqIfNotZero(dc, "id", condition.getId());

		HibernateTool.eqIfNotZero(dc, "b.id", condition.getBranchId());
		HibernateTool.eqIfNotZero(dc, "clsr.id", condition.getClassroomId());
		HibernateTool.eqIfNotZero(dc, "c.id", condition.getCourseId());
		HibernateTool
				.eqIfNotEmpty(dc, "cc.id", condition.getCourseCategoryId());

		if (!StringUtils.isEmpty(condition.getTeacherNo())) {
			dc.add(HibernateTool.or(
					Restrictions.eq("m.no", condition.getTeacherNo()),
					Restrictions.eq("a1.no", condition.getTeacherNo()),
					Restrictions.eq("a2.no", condition.getTeacherNo())));

		}
		if (!StringUtils.isEmpty(condition.getStudentNo())) {
			DetachedCriteria sub_dc = DetachedCriteria.forClass(
					CourseTaskResultStudent.class, "ctrs");
			sub_dc.createAlias("ctrs.student", "s");
			sub_dc.add(Restrictions.eqProperty("ctrs.courseTaskResult.id",
					"uu.id"));

			HibernateTool
					.eqIfNotEmpty(sub_dc, "s.no", condition.getStudentNo());
			sub_dc.setProjection(Projections.id());

			dc.add(Subqueries.exists(sub_dc));
		}

		HibernateTool.betweenIfNotNull(dc, "uu.startTime",
				condition.getBeginDate(), condition.getEndDate(), -1);

		return dc;
	}

	private static final String[] SINGLE_MAPPING = new String[] { "id",
			"studentCount", "startTime", "endTime", "submitType" };

	private static final String[] ALIAS_MAPPING = new String[] { "b.name",
			"branchName", "b.id", "branchId", "m.name", "masterName", "m.no",
			"masterNo", "a1.name", "assistant1Name", "a1.no", "assistant1No",
			"a2.name", "assistant2Name", "a2.no", "assistant2No", "cc.name",
			"courseCategoryName", "c.name", "courseName", "clsr.name",
			"classroomName", "clsr.id", "classroomId", "cc.id",
			"courseCategoryId", "c.id", "courseId" };

	private static final String[] SINGLE_MAPPING_FULL = new String[] { "id",
			"studentCount", "startTime", "submitType", "createdTime",
			"updatedTime", "endTime", };

	private static final String[] ALIAS_MAPPING_FULL = new String[] {
			"creator.name", "creatorName", "updater.name", "updaterName" };

	@Override
	public PaginationSupport<MapBean> search(
			CourseTaskResultCondition condition, int pageIndex) {
		DetachedCriteria dc = makeDetachedCriteria(condition);
		ProjectionList proList = Projections.projectionList();// 设置投影集合

		proList = HibernateTool.injectSingle(proList, SINGLE_MAPPING, "uu");
		proList = HibernateTool.injectAlias(proList, ALIAS_MAPPING);
		dc.setProjection(proList);
		dc.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);

		PaginationSupport<Map<String, Object>> es = super.findPageByCriteria(
				dc, new PageInfo(pageIndex), Order.asc("uu.startTime"));
		return MapList.createPage(es);
	}

	@Override
	public MapBean unique(Long id) {
		CourseTaskResultCondition condition = new CourseTaskResultCondition();
		condition.setId(id);

		DetachedCriteria dc = makeDetachedCriteria(condition);
		dc.createAlias("uu.creator", "creator", DetachedCriteria.LEFT_JOIN);
		dc.createAlias("uu.updater", "updater", DetachedCriteria.LEFT_JOIN);
		ProjectionList proList = Projections.projectionList();// 设置投影集合

		proList = HibernateTool
				.injectSingle(proList, SINGLE_MAPPING_FULL, "uu");
		proList = HibernateTool.injectAlias(proList, ALIAS_MAPPING);
		proList = HibernateTool.injectAlias(proList, ALIAS_MAPPING_FULL);
		dc.setProjection(proList);
		dc.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);

		Map<String, Object> map = super.uniqueResult(dc);

		return MapBean.map(map);
	}

	@Override
	public void checkAllowView(long id, long branch_id) {
		String sql = "select id from CourseTaskResult where id=? and branch.id=?";
		if (!exists(sql, id, branch_id)) {
			Pe.raise("越权访问");
		}

	}

	@Override
	public void checkAllowEdit(long id, long branch_id) {
		checkAllowView(id, branch_id);

	}

	@Override
	public CourseTaskResult update(CourseTaskResult rt) {
		CourseTaskResult po = this.findById(rt.getId());
		po.setAssistant1(rt.getAssistant1());
		po.setAssistant2(rt.getAssistant2());
		po.setBranch(rt.getBranch());
		po.setClassroom(rt.getClassroom());
		po.setComments(rt.getComments());
		po.setCourse(rt.getCourse());
		po.setEndTime(rt.getEndTime());

		po.setMaster(rt.getMaster());
		po.setStartTime(rt.getStartTime());

		po.setSubmitType(rt.getSubmitType());
		po.setUpdater(rt.getUpdater());
		po.setUpdatedTime(Calendar.getInstance());
		_update(po);
		return po;
	}

	@Override
	public void checkAllowRemove(Long[] id, long branch_id) {
		String sql = "select u.id from CourseTaskResult where branch.id=? and ";

		QueryJedi qj = new QueryJedi(sql, branch_id);
		qj.eqOr("id", WxlSugar.asObjects(id));
		int c = getInt(qj.hql(), qj.parameters());
		if (c != id.length) {
			Pe.raise("越权访问");
		}

	}

	@Override
	public MapBean meta(long id) {
		String sql = "select u.id as id ,u.startTime as startTime,u.endTime as endTime,u.branch.id as branchId"
				+ " from CourseTaskResult u where u.id=? and removed=?";
		return mapBean(sql, id, false);

	}

	@Override
	public Map<String, MapBean> stat4Teacher(int year, int java_month,
			long branchId) {

		Calendar begin = DateJedi.create(year, java_month).firstDayOfMonth()
				.yesterday().to();
		Calendar end = DateJedi.create(year, java_month).lastDayOfMonth().to();

		String sql = "select count(ctr.id) as courseCount,m.id as masterId,m.no as masterNo,m.name as masterName,"
				+ "a1.id as assistant1Id,a1.no as assistant1No,a1.name as assistant1Name,"
				+ "a2.id as assistant2Id,a2.no as assistant2No,a2.name as assistant2Name "
				+ " from CourseTaskResult ctr left join ctr.master as m"
				+ " left join ctr.assistant1 as a1 left join ctr.assistant2 as a2 where ctr.startTime>? and ctr.startTime<=?"
				+ " and ctr.removed=? and ctr.branch.id=? group by m.id,m.no,m.name,a1.id,a1.no,a1.name,a2.id,a2.no,a2.name";

		MapList ml = mapList(sql, begin, end, false, branchId);

		Map<String, MapBean> m = WxlSugar.newHashMap();
		for (MapBean mb : ml) {

			MapList mbs = CourseTaskResultHelper.splitToTeacher(mb);
			for (MapBean mbb : mbs) {
				String key = mbb.string("teacherNo");
				if (m.containsKey(key)) {
					// 一个教师可能出现在不同的任务中,单靠group无法实现合计课程数量,只能拿到解开的结果,再根据教师来合计
					MapBean old = m.get(key);
					old.add("courseCount",
							old.asLong("courseCount")
									+ mbb.asLong("courseCount"));
				} else {
					m.put(key, mbb);
				}
			}
		}
		return m;
	}

	@Override
	public void incStudent(Long courseTaskResultId, int seed) {
		String sql = "update CourseTaskResult set studentCount=studentCount+? where id=?";
		updateBatch(sql, seed, courseTaskResultId);

	}

	@Override
	public Mix<CourseTaskResult, Boolean> store(CourseTaskResult rt) {
		String sql = "select id from CourseTaskResult where classroom.id=? and localId=?";
		Long id = findSingle(sql, rt.getClassroom().getId(), rt.getLocalId());
		boolean create = false;

		rt.setSubmitType(CourseTaskResult.ST_CLIENT);
		if (id == null) {
			create = true;
			getHibernateTemplate().save(rt);
		} else {
			//ctr.setId(id);
			//getHibernateTemplate().update(ctr);
			
			CourseTaskResult po = this.findById(rt.getId());
			po.setAssistant1( rt.getAssistant1() );
			po.setAssistant2( rt.getAssistant2() );
			po.setBranch( rt.getBranch() );
			po.setComments( rt.getComments() );
			po.setCourse( rt.getCourse() );
			po.setEndTime( rt.getEndTime() );
			po.setMaster( rt.getMaster() );
			po.setStartTime( rt.getStartTime() );
			po.setStudentCount( rt.getStudentCount() );
			po.setUpdater( rt.getUpdater() );
			_update(po);
			rt = po ;
			
		}

		return Mix.create(rt, create);
	}

	@Override
	public void storeComments(List<CourseTaskResultReportComments> results,
			CourseTaskResult object1) {
		for (CourseTaskResultReportComments c : results) {
			c.setResult(object1);
			c.setCreatedTime(Calendar.getInstance());
			getHibernateTemplate().save(c);
		}

	}

}
