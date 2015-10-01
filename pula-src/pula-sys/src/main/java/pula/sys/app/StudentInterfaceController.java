package pula.sys.app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import puerta.support.PaginationSupport;
import puerta.support.annotation.Barrier;
import puerta.support.utils.MD5;
import puerta.system.keeper.ParameterKeeper;
import puerta.system.vo.JsonResult;
import puerta.system.vo.MapBean;
import puerta.system.vo.MapList;
import pula.sys.BhzqConstants;
import pula.sys.conditions.TimeCourseOrderCondition;
import pula.sys.conditions.TimeCourseOrderUsageCondition;
import pula.sys.daos.CourseDao;
import pula.sys.daos.CourseTaskResultStudentDao;
import pula.sys.daos.CourseTaskResultWorkDao;
import pula.sys.daos.StudentDao;
import pula.sys.daos.StudentLogDao;
import pula.sys.daos.StudentPointsDao;
import pula.sys.daos.TimeCourseDao;
import pula.sys.daos.TimeCourseOrderDao;
import pula.sys.daos.TimeCourseUsageDao;
import pula.sys.domains.Student;
import pula.sys.domains.StudentCourse;
import pula.sys.domains.StudentPoints;
import pula.sys.domains.TimeCourse;
import pula.sys.domains.TimeCourseOrder;
import pula.sys.domains.TimeCourseOrderUsage;
import pula.sys.helpers.CourseHelper;
import pula.sys.helpers.StudentHelper;
import pula.sys.helpers.StudentPointsHelper;
import pula.sys.miscs.MD5Checker;
import pula.sys.services.StudentPointsService;
import pula.sys.util.SmsUtil;
import pula.sys.util.SmsUtil.SendResult;

@Controller
@Barrier(ignore = true)
public class StudentInterfaceController {
    
    private static final Logger logger = LoggerFactory.getLogger(StudentInterfaceController.class);

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
    private TimeCourseOrderDao timeCourseOrderDao;
    @Resource
    private TimeCourseDao timeCourseDao;
    @Resource
    private TimeCourseUsageDao timeCourseUsageDao; 

	@Resource
	CourseTaskResultWorkDao courseTaskResultWorkDao;

	@ResponseBody
	@RequestMapping
	@Transactional
	public JsonResult login(@RequestParam("loginId") String loginId,
			@RequestParam("password") String password,
			@RequestParam(value="ip",required=false) String ip, @RequestParam(value="md5", required =false) String md5) {

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
    public JsonResult info(@RequestParam("loginId") String loginId,
            @RequestParam(value = "ip", required = false) String ip,
            @RequestParam(value = "md5", required = false) String md5) {

        Long id = studentDao.getIdByNo(loginId);
        if (id == null) {
            return JsonResult.e("无效的账号或密码");
        }
        Student student = studentDao.findById(id);
        return JsonResult.s(student);
    }
    
    @ResponseBody
    @RequestMapping
    @Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
    public JsonResult timeCourses(@RequestParam("type") int type, @RequestParam("actorId") Long actorId,
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
    public JsonResult listTimeCourses(@RequestParam("studentNo") String studentNo) {

//        MD5Checker.check(parameterKeeper, md5, type, actorId, ip);

        Student student = studentDao.findByNo(studentNo);
        if (student == null){
            return JsonResult.e("no student found!");
        }

        // course no -> student course
        Map<String, StudentCourse> result = new HashMap<String, StudentCourse>();

        // from orders
        getOrderCourses(studentNo, result);
        
        // from detail order usage
        getOrderUsageCourses(studentNo, result);

        return JsonResult.s(new ArrayList<StudentCourse>(result.values()));
    }

    private void getOrderUsageCourses(String studentNo, Map<String, StudentCourse> result) {
        TimeCourseOrderUsageCondition usageCondition = new TimeCourseOrderUsageCondition();
        usageCondition.setStudentNo(studentNo);
        PaginationSupport<TimeCourseOrderUsage> usages = timeCourseUsageDao.search(usageCondition, 0);
        List<String> nos = new ArrayList<String>();
        for (int i = 0; i < usages.getItems().size(); i++) {
            String course = usages.getItems().get(i).getCourseNo();
            if (!StringUtils.isEmpty(course)) {
                nos.add(course);
            }
        }
        if (nos.size() > 0) {
            Map<String, TimeCourse> courses = new HashMap<String, TimeCourse>();
            List<TimeCourse> usageCourses = timeCourseDao.search(nos);
            for (int i = 0; i < usageCourses.size(); i++) {
                courses.put(usageCourses.get(i).getNo(), usageCourses.get(i));
            }
            for (TimeCourseOrderUsage usage : usages.getItems()) {
                if (StringUtils.isEmpty(usage.getCourseNo())) {
                    continue;
                }

                if (courses.containsKey(usage.getCourseNo())) {
                    // get or add
                    StudentCourse sc = null;
                    if (result.containsKey(usage.getCourseNo())) {
                        sc = result.get(usage.getCourseNo());
                    } else {
                        sc = new StudentCourse();
                        sc.setCourse(courses.get(usage.getCourseNo()));
                        result.put(usage.getCourseNo(), sc);
                    }

                    sc.addOrderUsage(usage);
                    if (sc.getSourceType() < 0) {
                        sc.setSourceType(1);
                    }
                }
            }
        }
    }

    private void getOrderCourses(String studentNo, Map<String, StudentCourse> result) {
        TimeCourseOrderCondition condition = new TimeCourseOrderCondition();
        condition.setStudentNo(studentNo);
        PaginationSupport<TimeCourseOrder> orders = timeCourseOrderDao.search(condition, 0);
        List<String> nos = new ArrayList<String>();
        for (int i = 0; i < orders.getItems().size(); i++) {
            String course = orders.getItems().get(i).getCourseNo();
            if (!StringUtils.isEmpty(course)) {
                nos.add(course);
            }
        }
        if (nos.size() > 0) {
            Map<String, TimeCourse> courses = new HashMap<String, TimeCourse>();
            List<TimeCourse> orderCourses = timeCourseDao.search(nos);
            for (int i = 0; i < orderCourses.size(); i++) {
                courses.put(orderCourses.get(i).getNo(), orderCourses.get(i));
            }
            for (TimeCourseOrder order : orders.getItems()) {
                if (StringUtils.isEmpty(order.getCourseNo())) {
                    continue;
                }

                if (courses.containsKey(order.getCourseNo())) {
                    // get or add
                    StudentCourse sc = null;
                    if (result.containsKey(order.getCourseNo())) {
                        sc = result.get(order.getCourseNo());
                    } else {
                        sc = new StudentCourse();
                        sc.setCourse(courses.get(order.getCourseNo()));
                        result.put(order.getCourseNo(), sc);
                    }
                    sc.addOrder(order);
                    sc.setSourceType(0);
                }
            }
        }
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
	@RequestMapping(method={RequestMethod.POST})
	@Transactional
	public JsonResult updatePassword(@RequestParam(value = "studentNo") String studentNo,
			@RequestParam("oldPassword") String oldPassword,
			@RequestParam("newPassword") String newPassword,
			@RequestParam("ip") String ip, 
			@RequestParam("md5") String md5) {
	    Student student = studentDao.findByNo(studentNo);
	    if (student == null) {
            return JsonResult.e("找不到对应的注册用户!");
        }

        Long branchId = studentDao.updatePassword(student.getId(), oldPassword, newPassword);

		studentLogDao.save(student.getId(), branchId, ip, "修改密码");

		return JsonResult.s();

	}

    @ResponseBody
    @RequestMapping(method={RequestMethod.POST})
    @Transactional
    public JsonResult resetPassword(@RequestParam(value = "studentNo", required = false) String studentNo,
            @RequestParam("mobile") String mobile, 
            @RequestParam(value = "ip", required = false) String ip,
            @RequestParam(value = "md5", required = false) String md5) {
        // validate user input
        Student student = studentDao.findByNo(studentNo);
        if (student == null) {
            List<Student> students = studentDao.findByProperty("mobile", mobile);
            if (students.size() > 1) {
                return JsonResult.e("此手机对应多个用户，请提供学生号码！");
            }
            if (students.size() > 0) {
                student = students.get(0);
            }
        }

        if (student == null) {
            return JsonResult.e("找不到对应的注册用户!");
        }

        if (!student.getMobile().equals(mobile)) {
            return JsonResult.e("给定的学生号和给定的手机号不一致，请提供正确的手机号码和学生号！");
        }

        String newPassword = makeNewPassword(student);
        student.setPassword(StudentHelper.makePassword(newPassword));
        studentDao.update(student, true);
        // bad log password....
        logger.info(String.format("学生 编号%s, 用户名 %s, 密码重置为 %s",student.getNo(), student.getName(), newPassword));

        studentLogDao.save(student.getId(), student.getBranch().getId(), ip, "重置密码");
        // SMS send
        SendSMS(student.getName(), mobile, newPassword);

        return JsonResult.s();
    }

	private void SendSMS(String name, String mobile, String newPassword) {
	    SendResult result = SmsUtil.sendResetPassword(name, mobile, newPassword);
	    if (result.succeed) {
            logger.info(String.format("发送短信成功: %s: %s !", name, mobile));
        } else {
            logger.error(String.format("发送短信失败! %s: %s: %s !", name, mobile, result.message));
        }
    }

	// Reset password algorithm
    private String makeNewPassword(Student student) {
        Random r = new Random(System.currentTimeMillis());
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 5; i++) {
            sb.append(r.nextInt());
        }
        String md5 = MD5.GetMD5String(sb.toString());
        if (md5.length() > 6) {
            return md5.substring(0, 6);
        } else {
            return String.format("%a6s", md5);
        }
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
