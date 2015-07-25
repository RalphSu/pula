package pula.sys.app;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import puerta.support.PaginationSupport;
import puerta.support.annotation.Barrier;
import puerta.system.keeper.ParameterKeeper;
import puerta.system.vo.JsonResult;
import puerta.system.vo.MapBean;
import puerta.system.vo.MapList;
import pula.sys.BhzqConstants;
import pula.sys.daos.CourseDao;
import pula.sys.daos.CourseTaskResultStudentDao;
import pula.sys.daos.CourseTaskResultWorkDao;
import pula.sys.daos.StudentDao;
import pula.sys.daos.StudentLogDao;
import pula.sys.daos.StudentPointsDao;
import pula.sys.domains.StudentPoints;
import pula.sys.helpers.CourseHelper;
import pula.sys.helpers.StudentPointsHelper;
import pula.sys.miscs.MD5Checker;
import pula.sys.services.StudentPointsService;

@Controller
@Barrier(ignore = true)
public class StudentInterfaceController {

	@Resource
	ParameterKeeper parameterKeeper;

	@Resource
	StudentDao studentDao;
	@Resource
	StudentLogDao studentLogDao;
	@Resource
	StudentPointsDao studentPointsDao;
	@Resource
	CourseDao courseDao;
	@Resource
	CourseTaskResultStudentDao courseTaskResultStudentDao;
	@Resource
	StudentPointsService studentPointsService;

	@Resource
	CourseTaskResultWorkDao courseTaskResultWorkDao;

	@ResponseBody
	@RequestMapping
	@Transactional
	public JsonResult login(@RequestParam("loginId") String loginId,
			@RequestParam("password") String password,
			@RequestParam("ip") String ip, @RequestParam("md5") String md5) {

	    if (!StringUtils.isEmpty(md5)){
	        MD5Checker.check(parameterKeeper, md5, loginId, password, ip);
	    }

		MapBean mb = studentDao.meta4login(loginId, password);
		if (mb == null) {
			return JsonResult.e("无效的账号或密码");
		}

		studentLogDao.save(mb.asLong("id"), mb.asLong("branchId"), ip, "登录");

		return JsonResult.s(mb);

	}

	@ResponseBody
	@RequestMapping
	@Transactional
	public JsonResult logout(@RequestParam("actorId") Long actorId,
			@RequestParam("ip") String ip, @RequestParam("md5") String md5) {

		MD5Checker.check(parameterKeeper, md5, actorId, ip);

		Long branchId = studentDao.getBranchId(actorId);

		if (branchId == null) {
			return JsonResult.s();
		}

		studentLogDao.save(actorId, branchId, ip, "退出");

		return JsonResult.s();

	}

	@ResponseBody
	@RequestMapping
	@Transactional
	public JsonResult updatePassword(@RequestParam("actorId") Long actorId,
			@RequestParam("oldPassword") String oldPassword,
			@RequestParam("newPassword") String newPassword,
			@RequestParam("ip") String ip, @RequestParam("md5") String md5) {

		MD5Checker.check(parameterKeeper, md5, actorId, oldPassword,
				newPassword, ip);

		Long branchId = studentDao.updatePassword(actorId, oldPassword,
				newPassword);

		studentLogDao.save(actorId, branchId, ip, "修改密码");

		return JsonResult.s();

	}

	@ResponseBody
	@RequestMapping
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	public JsonResult getPointsLog(@RequestParam("pageIndex") int pageIndex,
			@RequestParam("actorId") Long actorId,
			@RequestParam("ip") String ip, @RequestParam("md5") String md5) {

		MD5Checker.check(parameterKeeper, md5, pageIndex, actorId, ip);

		// Long branchId = studentDao.updatePassword(actorId, );

		PaginationSupport<MapBean> results = studentPointsDao.search4front(
				actorId, pageIndex);
		long totalPoints = studentDao.getTotal(actorId);

		for (MapBean mb : results.getItems()) {
			mb.put("fromTypeName",
					StudentPointsHelper.getTypeName(mb.asInteger("fromType")));
		}

		// studentLogDao.save(actorId, branchId, ip, "修改密码");

		return JsonResult.s(MapBean.map("data", results).add("totalPoints",
				totalPoints));

	}

	@ResponseBody
	@RequestMapping
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	public JsonResult getCourses(@RequestParam("type") int type,
			@RequestParam("actorId") Long actorId,
			@RequestParam("ip") String ip, @RequestParam("md5") String md5) {

		MD5Checker.check(parameterKeeper, md5, type, actorId, ip);

		String type_no = CourseHelper.getNoFromFront(type);

		// no,id,name ,comments
		MapList courses = courseDao.mapList4web(type_no);

		// id ,gamePlayed,courseId
		MapList hits = courseTaskResultStudentDao.list4hits(actorId, type_no);

		return JsonResult.s(MapBean.map("data", courses).add("hits", hits));

	}

	@ResponseBody
	@RequestMapping
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	public JsonResult getCoursesGame(
			@RequestParam("courseTaskResultStudentId") long courseTaskResultStudentId,
			@RequestParam("actorId") Long actorId,
			@RequestParam("ip") String ip, @RequestParam("md5") String md5) {

		MD5Checker.check(parameterKeeper, md5, courseTaskResultStudentId,
				actorId, ip);

		// id ,gamePlayed
		MapBean result = courseTaskResultStudentDao.allowPlayGame(actorId,
				courseTaskResultStudentId);

		if (result == null) {
			return JsonResult.e("尚未开课或已经玩过该游戏");
		}

		return JsonResult.s(result);

	}

	@ResponseBody
	@RequestMapping
	@Transactional()
	public JsonResult takeGamePoints(
			@RequestParam("courseTaskResultStudentId") long courseTaskResultStudentId,
			@RequestParam("actorId") Long actorId,
			@RequestParam("ip") String ip, @RequestParam("md5") String md5) {

		MD5Checker.check(parameterKeeper, md5, courseTaskResultStudentId,
				actorId, ip);

		// id ,gamePlayed
		MapBean result = courseTaskResultStudentDao.allowPlayGame(actorId,
				courseTaskResultStudentId);

		Long branchId = studentDao.getBranchId(actorId);

		if (branchId == null) {
			return JsonResult.s();
		}

		if (result == null) {
			return JsonResult.e("尚未开课或已经玩过该游戏");
		}

		// 更新,加分
		courseTaskResultStudentDao.gamePlayed(courseTaskResultStudentId);

		int ptz = parameterKeeper.getInt(BhzqConstants.POINTS_TAKE_GAME, 10);

		String refId = "CTRS:" + result.asLong("id");
		studentPointsService.save(actorId, StudentPoints.FROM_COUSE_GAME, ptz,
				result.string("courseNo") + " " + result.string("courseName"),
				0, null, refId);

		studentLogDao.save(actorId, branchId, ip, "游戏得分:" + ptz);

		return JsonResult.s(MapBean.map("points", ptz));

	}

	@ResponseBody
	@RequestMapping
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	public JsonResult getRadar(@RequestParam("actorId") Long actorId,
			@RequestParam("ip") String ip, @RequestParam("md5") String md5) {

		MD5Checker.check(parameterKeeper, md5, actorId, ip);

		// id ,gamePlayed
		MapBean result = courseTaskResultWorkDao.stat4radar(actorId);

		if (result == null) {
			return JsonResult.e("尚未开课或已经玩过该游戏");
		}

		return JsonResult.s(result);

	}
}
