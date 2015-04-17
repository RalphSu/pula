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
import puerta.support.dao.HibernateTool;
import puerta.system.base.HibernateGenericDao;
import puerta.system.vo.MapBean;
import puerta.system.vo.MapList;
import pula.sys.conditions.CourseTaskResultStudentCondition;
import pula.sys.daos.CourseTaskResultStudentDao;
import pula.sys.domains.CourseTaskResult;
import pula.sys.domains.CourseTaskResultStudent;
import pula.sys.domains.Student;

@Repository
public class CourseTaskResultStudentDaoImpl extends
		HibernateGenericDao<CourseTaskResultStudent, Long> implements
		CourseTaskResultStudentDao {

	@Override
	public CourseTaskResultStudent save(CourseTaskResultStudent s) {
		s.setCreatedTime(Calendar.getInstance());
		_save(s);
		return s;

	}

	@Override
	public MapList mapList(Long id) {
		String sql = "select u.id as id ,u.student.no as studentNo,u.student.name as studentName"
				+ " from CourseTaskResultStudent u where u.courseTaskResult.id=? and removed=?  ";

		return mapList(sql, id, false);
	}

	@Override
	public boolean exists(Long courseTaskResultId, long studentId) {
		String sql = "select u.id from CourseTaskResultStudent u where u.courseTaskResult.id=? and u.student.id=? and removed=?";

		return exists(sql, courseTaskResultId, studentId, false);
	}

	@Override
	public MapBean meta(Long courseTaskResultStudentId) {
		String sql = "select u.startTime as startTime,u.endTime as endTime ,u.branch.id as branchId,uu.student.id as studentId,"
				+ "uu.courseTaskResult.id as courseTaskResultId from"
				+ " CourseTaskResultStudent uu left join uu.courseTaskResult u where uu.id=? and uu.removed=? ";
		return mapBean(sql, courseTaskResultStudentId, false);
	}

	@Override
	public MapBean meta4work(Long courseTaskResultStudentId) {

		String sql = "select u.startTime as startTime,u.endTime as endTime ,u.branch.id as branchId,uu.student.id as studentId"
				+ ",u.id as courseTaskResultId from"
				+ " CourseTaskResultStudent uu left join uu.courseTaskResult u where uu.id=? and uu.removed=? ";
		return mapBean(sql, courseTaskResultStudentId, false);
	}

	private static final String[] SINGLE_MAPPING_RESULT = new String[] { "id",
			"studentCount", "startTime", "endTime", "submitType" };

	private static final String[] ALIAS_MAPPING = new String[] { "b.name",
			"branchName", "b.id", "branchId", "m.name", "masterName", "m.no",
			"masterNo", "a1.name", "assistant1Name", "a1.no", "assistant1No",
			"a2.name", "assistant2Name", "a2.no", "assistant2No", "cc.name",
			"courseCategoryName", "c.name", "courseName", "clsr.name",
			"classroomName", "clsr.id", "classroomId", "cc.id",
			"courseCategoryId", "c.id", "courseId", "s.no", "studentNo",
			"s.name", "studentName", "ctrw.id", "workId" };

	private static final String[] SINGLE_MAPPING_WORK = new String[] {
			"score1", "score2", "score3", "score4", "score5", "scoreTime" };

	@Override
	public PaginationSupport<MapBean> search(
			CourseTaskResultStudentCondition condition, int pageIndex) {
		DetachedCriteria dc = makeDetachedCriteria(condition);
		ProjectionList proList = Projections.projectionList();// 设置投影集合

		proList = HibernateTool.injectSingle(proList, SINGLE_MAPPING_RESULT,
				"uu");
		proList = HibernateTool.injectSingle(proList, SINGLE_MAPPING_WORK,
				"ctrw");
		proList = HibernateTool.injectAlias(proList, ALIAS_MAPPING);
		dc.setProjection(proList);
		dc.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);

		PaginationSupport<Map<String, Object>> es = super.findPageByCriteria(
				dc, new PageInfo(pageIndex), Order.asc("uu.startTime"));
		return MapList.createPage(es);
	}

	private DetachedCriteria makeDetachedCriteria(
			CourseTaskResultStudentCondition condition) {
		DetachedCriteria dc = DetachedCriteria.forClass(
				CourseTaskResultStudent.class, "ctrs");

		dc.createAlias("ctrs.courseTaskResultWork", "ctrw",
				DetachedCriteria.LEFT_JOIN);
		dc.createAlias("ctrs.courseTaskResult", "uu",
				DetachedCriteria.LEFT_JOIN);
		dc.createAlias("ctrs.student", "s", DetachedCriteria.LEFT_JOIN);
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

		if (condition.getTeacherId() != 0) {
			dc.add(HibernateTool.or(
					Restrictions.eq("m.id", condition.getTeacherId()),
					Restrictions.eq("a1.id", condition.getTeacherId()),
					Restrictions.eq("a2.id", condition.getTeacherId())));

		}

		HibernateTool.eqIfNotEmpty(dc, "s.no", condition.getStudentNo());

		HibernateTool.betweenIfNotNull(dc, "uu.startTime",
				condition.getBeginDate(), condition.getEndDate(), -1);

		return dc;
	}

	@Override
	public MapList list4hits(Long actorId, String type_no) {
		String sql = "select u.id as id,u.gamePlayed as gamePlayed,c.id as courseId from CourseTaskResultStudent u left join u.student as s"
				+ " left join u.courseTaskResult as ctr left join ctr.course as c where u.removed=? and c.category.no=? and ctr.removed=? and s.id=? ";

		return mapList(sql, false, type_no, false, actorId);

	}

	@Override
	public MapBean allowPlayGame(Long actorId, long courseTaskResultStudentId) {
		String sql = "select u.id as id,c.no as courseNo,c.name as courseName"
				+ " from CourseTaskResultStudent u left join u.student as s"
				+ " left join u.courseTaskResult as ctr left join ctr.course as c where u.removed=? and u.id=? and ctr.removed=? and s.id=? and u.gamePlayed=?";

		return mapBean(sql, false, courseTaskResultStudentId, false, actorId,
				false);
	}

	@Override
	public void gamePlayed(long courseTaskResultStudentId) {
		String sql = "update CourseTaskResultStudent set gamePlayed=?,gamePlayAt=? where id=? and gamePlayed=?";
		updateBatch(sql, true, courseTaskResultStudentId,
				Calendar.getInstance(), false);

	}

	@Override
	public List<MapBean> save(List<Student> results, CourseTaskResult object1,
			Boolean update) {
		if (update) {
			// 旧的都删掉
			String sql = "update CourseTaskResultStudent set removed = ? where courseTaskResult.id=?";
			updateBatch(sql, true, object1.getId());
		}
		MapList ml = new MapList();

		for (Student st : results) {
			CourseTaskResultStudent s = new CourseTaskResultStudent();
			s.setCourseTaskResult(object1);
			s.setCreatedTime(Calendar.getInstance());
			s.setStudent(st);
			s.setSubmitType(CourseTaskResult.ST_CLIENT);
			getHibernateTemplate().save(s);
			ml.add(MapBean.map("no", s.getStudent().getNo())
					.add("id", s.getId()).add("studentId", st.getId()));
		}

		return ml;

	}

}
