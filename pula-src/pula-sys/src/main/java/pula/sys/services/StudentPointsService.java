package pula.sys.services;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import puerta.system.service.SessionService;
import pula.sys.daos.StudentDao;
import pula.sys.daos.StudentPointsDao;
import pula.sys.domains.Gift;
import pula.sys.domains.Student;
import pula.sys.domains.StudentPoints;

@Service
public class StudentPointsService {

	@Resource
	StudentPointsDao studentPointsDao;
	@Resource
	StudentDao studentDao;
	@Resource
	SessionService sessionService;
	@Resource
	SessionUserService sessionUserService;

	public void save(StudentPoints wup) {
		wup.setAdmin(sessionUserService.getUser());
		studentPointsDao.save(wup);
		int points = studentPointsDao.getSummary(wup.getOwner().getId(), 0);
		studentDao.updatePoints(points, wup.getOwner().getId());
	}

	// 手动奖分
	// public void saveBonus(String loginId, int points, String comments,
	// SysUser admin) {
	// Student wu = studentDao.findByLoginId(loginId);
	// if (wu == null) {
	// Pe.raise("无效的会员账号");
	// }
	// StudentPoints wup = new StudentPoints();
	//
	// // wup.setManual(true);
	// wup.setAdmin(admin);
	// wup.setComments(comments);
	// wup.setFromType(StudentPoints.FROM_MANUAL);
	// wup.setOwner(wu);
	// wup.setPoints(points);
	// studentPointsDao.save(wup);
	//
	// // update
	// int pointsssss = studentPointsDao.getSummary(wup.getOwner().getId(), 0);
	// studentDao.updatePoints(pointsssss, wup.getOwner().getId());
	//
	// }

	public boolean hasPoints(String targetId, String ownerId) {
		return studentPointsDao.hasPoints(targetId, ownerId);
	}

	public void save(long id, int type, int i, String comments, int mt, Gift g,
			String refId) {

		// id
		StudentPoints wup = new StudentPoints();
		wup.setFromType(type);
		wup.setOwner(Student.create(id));

		wup.setPoints(i);
		wup.setComments(comments);
		wup.setMinorType(mt);
		wup.setGift(g);
		wup.setRefId(refId);

		this.save(wup);

	}

	public void erase(String buildRefId, String string) {
		// 只有课程结果 是不修改的,所以可以这么做.(也就是积分里面只有一条记录)
		StudentPoints po = studentPointsDao.findByRefId(buildRefId);

		// 反向
		StudentPoints ne = StudentPoints.create(po.getOwner(),
				po.getFromType(), po.getPoints() * -1, string, buildRefId);
		
		this.save(ne);

	}
}
