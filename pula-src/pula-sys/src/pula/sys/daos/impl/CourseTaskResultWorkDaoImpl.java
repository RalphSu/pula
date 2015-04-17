package pula.sys.daos.impl;

import java.util.Calendar;
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
import puerta.support.dao.QueryJedi;
import puerta.support.utils.DateJedi;
import puerta.support.utils.RandomTool;
import puerta.support.utils.WxlSugar;
import puerta.system.base.HibernateGenericDao;
import puerta.system.vo.MapBean;
import puerta.system.vo.MapList;
import pula.sys.conditions.CourseTaskResultWorkCondition;
import pula.sys.daos.CourseTaskResultWorkDao;
import pula.sys.domains.CourseTaskResultWork;

@Repository
public class CourseTaskResultWorkDaoImpl extends
		HibernateGenericDao<CourseTaskResultWork, Long> implements
		CourseTaskResultWorkDao {

	@Override
	public CourseTaskResultWork save(CourseTaskResultWork w) {
		w.setAttachmentKey(RandomTool.getRandomString(10));
		getHibernateTemplate().save(w);
		return w;
	}

	@Override
	public Map<String, MapBean> map(Long id) {
		String sql = "select s.no as no ,u.score1 as score1, u.score2 as score2, "
				+ "u.score3 as score3, u.score4 as score4, u.score5 as score5,u.attachmentKey as attachmentKey,"
				+ "u.scoreTime as scoreTime,u.id as workId from CourseTaskResultWork u LEFT JOIN u.student s where u.courseTaskResult.id=? ";
		MapList ml = mapList(sql, id);
		Map<String, MapBean> r = WxlSugar.newHashMap();
		for (MapBean mb : ml) {
			String key = mb.string("no");
			r.put(key, mb);
		}
		return r;
	}

	@Override
	public void updateKey(String ak, Long id) {
		String sql = "update CourseTaskResultWork set attachmentKey=? where id=?";
		updateBatch(sql, ak, id);

	}

	@Override
	public Long getId(Long resultId, Long studentId) {
		String sql = "select u.id from CourseTaskResultWork u where u.courseTaskResult.id=? and u.student.id=?";
		return findSingle(sql, resultId, studentId);

	}

	@Override
	public MapBean meta4upload(Long resultStudentId) {

		String sql = "select u.id as id ,u.attachmentKey as attachmentKey from CourseTaskResultWork"
				+ " u where u.courseTaskResultStudent.id=?";
		return mapBean(sql, resultStudentId);
	}

	@Override
	public MapBean meta4view(Long id) {
		String sql = "select b.id as branchId,u.attachmentKey as attachmentKey ,u.id as id"
				+ " from CourseTaskResultWork u left join u.courseTaskResultStudent s left join s.courseTaskResult as r left join r.branch as b where u.id=?";

		MapBean mb = mapBean(sql, id);
		if (mb == null) {
			return null;
		}

		return mb;
	}

	// 为了学生管理界面,id 还是student 的id
	@Override
	public MapList mapList(long id) {
		String sql = "select rs.id as id ,s.no as studentNo ,s.name as studentName,u.score1 as score1, u.score2 as score2, "
				+ "u.score3 as score3, u.score4 as score4, u.score5 as score5,u.attachmentKey as attachmentKey,"
				+ "u.scoreTime as scoreTime,u.id as workId from CourseTaskResultStudent rs left join rs.courseTaskResultWork as u left join rs.student s where rs.courseTaskResult.id=? ";
		MapList ml = mapList(sql, id);
		return ml;
	}

	private static final String[] SINGLE_MAPPING = new String[] { "id" };
	private static final String[] SINGLE_MAPPING_EX = new String[] {
			"attachmentKey", "scoreTime", "score1", "score2", "score3",
			"score4", "score5" };

	private static final String[] ALIAS_MAPPING = new String[] { "s.name",
			"studentName", "ctr.startTime", "startTime", "clsr.name",
			"classroomName", "c.name", "courseName" };

	private static final String[] ALIAS_MAPPING_EX = new String[] {
			"ctr.endTime", "endTime", "cc.name", "categoryName" };

	@Override
	public PaginationSupport<MapBean> list4score(Long actorId, int pageIndex) {
		CourseTaskResultWorkCondition c = new CourseTaskResultWorkCondition();
		c.setActorId(actorId);
		DetachedCriteria dc = makeDetachedCriteria(c);

		ProjectionList proList = Projections.projectionList();// 设置投影集合
		proList = HibernateTool.injectSingle(proList, SINGLE_MAPPING, "uu");
		proList = HibernateTool.injectAlias(proList, ALIAS_MAPPING);
		dc.setProjection(proList);
		dc.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);

		PaginationSupport<Map<String, Object>> es = super.findPageByCriteria(
				dc, new PageInfo(pageIndex,5), Order.desc("uu.id"));
		return MapList.createPage(es);
	}

	private DetachedCriteria makeDetachedCriteria(
			CourseTaskResultWorkCondition c) {
		DetachedCriteria dc = DetachedCriteria.forClass(
				CourseTaskResultWork.class, "uu");

		dc.createAlias("uu.courseTaskResultStudent", "ctrs",
				DetachedCriteria.LEFT_JOIN);
		dc.createAlias("ctrs.student", "s", DetachedCriteria.LEFT_JOIN);
		dc.createAlias("ctrs.courseTaskResult", "ctr",
				DetachedCriteria.LEFT_JOIN);
		dc.createAlias("ctr.master", "m", DetachedCriteria.LEFT_JOIN);
		dc.createAlias("ctr.classroom", "clsr", DetachedCriteria.LEFT_JOIN);
		dc.createAlias("ctr.course", "c", DetachedCriteria.LEFT_JOIN);

		// 教师信息锁定
		dc.add(Restrictions.eq("m.id", c.getActorId()));
		HibernateTool.eqIfNotZero(dc, "uu.id", c.getId());
		// 删掉的任务不要
		dc.add(Restrictions.eq("ctr.removed", false));
		dc.add(Restrictions.eq("ctrs.removed", false));
		// 评分信息锁定
		Calendar three_hrs_before = DateJedi.createNow().moveHour(-3).to();
		dc.add(Restrictions.or(Restrictions.isNull("uu.scoreTime"),
				Restrictions.gt("scoreTime", three_hrs_before)));
		return dc;
	}

	@Override
	public MapBean meta4score(long courseTaskResultWorkId, Long actorId) {
		CourseTaskResultWorkCondition condition = new CourseTaskResultWorkCondition();
		condition.setId(courseTaskResultWorkId);
		condition.setActorId(actorId);

		DetachedCriteria dc = makeDetachedCriteria(condition);

		dc.createAlias("c.category", "cc", DetachedCriteria.LEFT_JOIN);
		ProjectionList proList = Projections.projectionList();// 设置投影集合

		proList = HibernateTool.injectSingle(proList, SINGLE_MAPPING, "uu");
		proList = HibernateTool.injectSingle(proList, SINGLE_MAPPING_EX, "uu");
		proList = HibernateTool.injectAlias(proList, ALIAS_MAPPING);
		proList = HibernateTool.injectAlias(proList, ALIAS_MAPPING_EX);
		dc.setProjection(proList);
		dc.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);

		Map<String, Object> map = super.uniqueResult(dc);

		return MapBean.map(map);
	}

	@Override
	public MapBean getFile(long courseTaskResultWorkId, Long actorId) {
		String sql = "select ctrw.attachmentKey as attachmentKey from CourseTaskResultWork ctrw"
				+ " left join ctrw.courseTaskResultStudent ctrs left join ctrs.courseTaskResult"
				+ " ctr where ctrw.id=? and ctr.master.id=? and ctrs.removed=? and ctr.removed=? ";

		return mapBean(sql, courseTaskResultWorkId, actorId, false, false);

	}

	@Override
	public void makeScore(long id, int s1, int s2, int s3, int s4, int s5,
			boolean updateScoreTime) {
		String sql = "update CourseTaskResultWork set score1=?,score2=?,score3=?,score4=?,score5=?";

		QueryJedi qj = new QueryJedi(sql, s1, s2, s3, s4, s5);
		if (updateScoreTime) {
			qj.append(",scoreTime=?", Calendar.getInstance());
		}

		qj.append(" where id=?", id);

		updateBatch(qj.hql(), qj.parameters());

	}

	@Override
	public MapBean stat4radar(Long actorId) {
		String sql = "select avg(u.score1) as s1,avg(u.score2) as s2,avg(u.score3) as s3 ,avg(u.score4) as s4,avg(u.score5) as s5"
				+ " from CourseTaskResultWork u left join u.courseTaskResultStudent as ctrs"
				+ " left join ctrs.student as s where s.id=? and ctrs.removed=? and ctrs.courseTaskResult.removed=? ";

		MapBean mb = mapBean(sql, actorId, false, false);

		// Double s1= mb.asDouble("s1");
		// Double s2= mb.asDouble("s2");
		// Double s3= mb.asDouble("s3");
		// Double s4= mb.asDouble("s4");
		// Double s5= mb.asDouble("s5");

		return mb;

	}
}
