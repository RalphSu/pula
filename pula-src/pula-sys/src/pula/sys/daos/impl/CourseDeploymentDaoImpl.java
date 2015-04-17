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
import puerta.support.dao.HibernateTool;
import puerta.system.base.HibernateGenericDao;
import puerta.system.vo.MapBean;
import puerta.system.vo.MapList;
import pula.sys.conditions.CourseDeploymentCondition;
import pula.sys.daos.CourseDeploymentDao;
import pula.sys.domains.Classroom;
import pula.sys.domains.Course;
import pula.sys.domains.CourseDeployment;
import pula.sys.domains.SysUser;

@Repository
public class CourseDeploymentDaoImpl extends
		HibernateGenericDao<CourseDeployment, Long> implements
		CourseDeploymentDao {

	@Override
	public void save(Long[] objId, Long classroomId, String actorId) {
		// String sql =
		// "select u.id from CourseDeployment u where u.branch.id=? and u.removed=? ";
		//
		// QueryJedi qj = new QueryJedi(sql, branchId);
		//
		// qj.eqOr("u.course.id", WxlSugar.asObjects(objId));
		//
		// if (exists(qj.hql(), qj.parameters())) {
		// Pe.raise("已经分配过了");
		// }

		Classroom c = Classroom.create(classroomId);
		SysUser creator = SysUser.create(actorId);
		for (Long id : objId) {
			if (exists(id, classroomId)) {
				continue;
			}
			// 另存一个
			CourseDeployment cd = new CourseDeployment();
			cd.setClassroom(c);
			cd.setCourse(Course.create(id));
			cd.setCreatedTime(Calendar.getInstance());
			cd.setCreator(creator);
			getHibernateTemplate().save(cd);
		}

	}

	private boolean exists(Long id, Long classroomId) {
		String sql = "select id from CourseDeployment where course.id=? and classroom.id=? and removed=?";
		return exists(sql, id, classroomId, false);
	}

	private static final String[] SINGLE_MAPPING = new String[] { "id",
			"createdTime" };

	private static final String[] ALIAS_MAPPING = new String[] { "b.name",
			"branchName", "b.id", "branchId", "c.id", "courseId", "c.no",
			"courseNo", "c.name", "courseName", "cc.name", "categoryName",
			"cr.name", "creatorName", "clsr.name", "classroomName" };

	@Override
	public PaginationSupport<MapBean> searchMapBean(
			CourseDeploymentCondition condition, int pageIndex) {

		DetachedCriteria dc = makeDetachedCriteria(condition);
		ProjectionList proList = Projections.projectionList();// 设置投影集合

		proList = HibernateTool.injectSingle(proList, SINGLE_MAPPING, "uu");
		proList = HibernateTool.injectAlias(proList, ALIAS_MAPPING);
		dc.setProjection(proList);
		dc.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);

		PaginationSupport<Map<String, Object>> es = super.findPageByCriteria(
				dc, new PageInfo(pageIndex), Order.asc("c.no"));
		return MapList.createPage(es);

	}

	private DetachedCriteria makeDetachedCriteria(
			CourseDeploymentCondition condition) {
		DetachedCriteria dc = DetachedCriteria.forClass(CourseDeployment.class,
				"uu");

		dc.createAlias("uu.classroom", "clsr", DetachedCriteria.LEFT_JOIN);
		dc.createAlias("clsr.branch", "b", DetachedCriteria.LEFT_JOIN);
		dc.createAlias("uu.course", "c", DetachedCriteria.LEFT_JOIN);
		dc.createAlias("c.category", "cc", DetachedCriteria.LEFT_JOIN);
		dc.createAlias("c.creator", "cr", DetachedCriteria.LEFT_JOIN);

		HibernateTool.eq(dc, "removed", false);

		HibernateTool.eqIfNotZero(dc, "b.id", condition.getBranchId());
		HibernateTool.eqIfNotZero(dc, "clsr.id", condition.getClassroomId());
		HibernateTool.eqIfNotZero(dc, "c.id", condition.getCourseId());
		HibernateTool.eqIfNotEmpty(dc, "c.no", condition.getCourseNo());

		HibernateTool.likeIfNotEmpty(dc, "c.no", "c.name",
				condition.getKeywords());

		return dc;
	}

	@Override
	public boolean hasCourse(long classroomId, long courseId) {
		String sql = "select u.id from CourseDeployment u where u.classroom.id=? and u.course.id=?";
		return exists(sql, classroomId, courseId);
	}

	@Override
	public MapList list(long classroomId, String categoryId) {
		String sql = "select u.id as id,u.name as name ,u.no as no from CourseDeployment cd LEFT join cd.course as u"
				+ " where u.removed=? and cd.classroom.id=? and u.category.id=? order by u.no";
		return mapList(sql, false, classroomId, categoryId);
	}

	@Override
	public MapList listByBranch(long branch_id, String categoryId) {
		String sql = "select u.id as id,u.name as name ,u.no as no from CourseDeployment cd LEFT join cd.course as u"
				+ " where u.removed=? and cd.classroom.branch.id=? and u.category.id=? order by u.no";
		return mapList(sql, false, branch_id, categoryId);
	}

	@Override
	public MapList mapList(Long classroomId) {
		String sql = "select c.id as courseId,c.no as courseNo,c.name as courseName," +
				"c.key as courseKey"
				+ ",cc.name as categoryName,cc.no as categoryNo,subc.name as subCategoryName"
				+ " from CourseDeployment cd LEFT JOIN"
				+ " cd.course as c left join c.category as cc left join c.subCategory as subc"
				+ " where cd.classroom.id=? and cd.removed=? and c.removed=? and c.enabled=?";

		return mapList(sql, classroomId, false,false,true);

	}
}
