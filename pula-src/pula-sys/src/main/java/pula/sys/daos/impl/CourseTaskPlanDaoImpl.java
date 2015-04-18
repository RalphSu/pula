package pula.sys.daos.impl;

import java.util.Calendar;

import org.springframework.stereotype.Repository;

import puerta.support.Pe;
import puerta.support.dao.QueryJedi;
import puerta.support.utils.DateJedi;
import puerta.system.base.HibernateGenericDao;
import puerta.system.vo.MapBean;
import puerta.system.vo.MapList;
import pula.sys.daos.CourseTaskPlanDao;
import pula.sys.domains.CourseTaskPlan;
import pula.sys.helpers.CourseTaskPlanHelper;

@Repository
public class CourseTaskPlanDaoImpl extends
		HibernateGenericDao<CourseTaskPlan, Long> implements CourseTaskPlanDao {

	@Override
	public void checkAllowEdit(long id, long branch_id) {
		String sql = "select startTime from  CourseTaskPlan where id=? and branch.id=? and removed=?";
		Calendar obj = findSingle(sql, id, branch_id, false);
		if (obj == null) {
			Pe.raise("指定的计划不存在或不允许修改");
		}

		if (!CourseTaskPlanHelper.allowEdit(obj.get(Calendar.YEAR),
				obj.get(Calendar.MONTH) + 1)) {
			Pe.raise("数据已经超过可修改期限");
		}

	}

	@Override
	public CourseTaskPlan save(CourseTaskPlan plan) {
		plan.setCreatedTime(Calendar.getInstance());
		plan.setUpdater(plan.getCreator());
		plan.setUpdatedTime(plan.getCreatedTime());
		_save(plan);
		return plan;
	}

	@Override
	public CourseTaskPlan update(CourseTaskPlan rt) {

		CourseTaskPlan po = this.findById(rt.getId());
		po.setAssistant1(rt.getAssistant1());
		po.setAssistant2(rt.getAssistant2());
		po.setClassroom(rt.getClassroom());
		po.setComments(rt.getComments());
		po.setCourse(rt.getCourse());
		po.setEndTime(rt.getEndTime());
		po.setMaster(rt.getMaster());
		po.setStartTime(rt.getStartTime());
		po.setUpdater(rt.getCreator());
		po.setUpdatedTime(rt.getUpdatedTime());
		_update(po);
		return po;

	}

	@Override
	public MapBean unique(Long id) {
		QueryJedi qj = new QueryJedi("select ");
		qj.select("u", new String[] { "id", "startTime", "comments" })
				.selectAs("m.id", "masterId", "m.no", "masterNo", "m.name",
						"masterName", "a.Id", "assistantId", "a.no",
						"assistantNo", "a.name", "assistantName", "clsr.id",
						"classroomId", "clsr.name", "classroomName", "c.id",
						"courseId", "c.name", "courseName", "c.no", "courseNo",
						"cc.id", "courseCategoryId", "cc.name",
						"courseCategoryName")
				.append(" from CourseTaskPlan u LEFT join u.master as m,u.assistant as a")
				.append(" LEFT JOIN u.course as c LEFT JOIN c.category as cc LEFT JOIN u.classroom as clsr ")
				.append(" where u.id=?", id);

		return mapBean(qj.hql(), qj.parameters());

	}

	@Override
	public MapList mapList(int year, int month, long branchId) {

		Calendar bd = DateJedi.create(year, month - 1).firstDayOfMonth()
				.yesterday().to();
		Calendar ed = DateJedi.create(year, month - 1).lastDayOfMonth()
				.tomorrow().to();
		String sql = "select u.id as id ,u.startTime as startTime ,t.name as teacherName"
				+ " from CourseTaskPlan u left join u.master as t where u.startTime>?"
				+ " and u.startTime<? and u.branch.id=? and u.removed=? order by u.startTime";
		return mapList(sql, bd, ed, branchId, false);
	}

	@Override
	public MapList planList(Calendar sDate, long branchId) {
		Calendar bd = DateJedi.create(sDate).yesterday().to();
		Calendar ed = DateJedi.create(sDate).tomorrow().to();

		QueryJedi qj = new QueryJedi("select ");
		qj.select("u", new String[] { "id", "startTime", "comments" })
				.selectAs("m.id", "masterId", "m.no", "masterNo", "m.name",
						"masterName", "a1.id", "assistant1Id", "a1.no",
						"assistant1No", "a1.name", "assistant1Name", "a2.id",
						"assistant2Id", "a2.no", "assistant2No", "a2.name",
						"assistant2Name", "clsr.id", "classroomId",
						"clsr.name", "classroomName", "c.id", "courseId",
						"c.name", "courseName", "c.no", "courseNo", "cc.id",
						"courseCategoryId", "cc.name", "courseCategoryName")
				.append(" from CourseTaskPlan u LEFT join u.master as m left join u.assistant1 as a1 left join u.assistant2 as a2")
				.append(" LEFT JOIN u.course as c LEFT JOIN c.category as cc LEFT JOIN u.classroom as clsr ")
				.append(" where u.startTime>? and u.startTime<? and u.branch.id=? and u.removed=? order by u.startTime ",
						bd, ed, branchId, false);

		return mapList(qj.hql(), qj.parameters());
	}

	@Override
	public MapList listByTeacher(int year, int month, Long actorId) {

		Calendar bd = DateJedi.create(year, month ).firstDayOfMonth()
				.yesterday().to();
		Calendar ed = DateJedi.create(year, month ).lastDayOfMonth()
				.tomorrow().to();
		String sql = "select u.id as id ,u.startTime as startTime ,c.name as courseName,cc.name as courseCategoryName"
				+ " from CourseTaskPlan u left join u.master as t left join u.assistant1 as a1 left join u.assistant2 as a2"
				+ " left join u.course as c left join c.category as cc where u.startTime>?"
				+ " and u.startTime<? and ( t.id=? or a1.id=? or a2.id=?) and u.removed=? order by u.startTime";
		return mapList(sql, bd, ed, actorId, actorId, actorId, false);
	}
}
