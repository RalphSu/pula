package pula.sys.app;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import puerta.support.Pe;
import puerta.support.annotation.Barrier;
import puerta.support.annotation.ObjectParam;
import puerta.support.utils.PewUtils;
import puerta.support.utils.RandomTool;
import puerta.support.utils.WxlSugar;
import puerta.support.vo.Mix;
import puerta.system.dao.LoggerDao;
import puerta.system.keeper.ParameterKeeper;
import puerta.system.service.SessionService;
import puerta.system.vo.JsonResult;
import puerta.system.vo.MapBean;
import puerta.system.vo.MapList;
import pula.sys.BhzqConstants;
import pula.sys.daos.CardDao;
import pula.sys.daos.CourseClientDao;
import pula.sys.daos.CourseDao;
import pula.sys.daos.CourseDeploymentDao;
import pula.sys.daos.CourseTaskResultDao;
import pula.sys.daos.CourseTaskResultStudentDao;
import pula.sys.daos.CourseTaskResultWorkDao;
import pula.sys.daos.FileAttachmentDao;
import pula.sys.daos.StudentCardDao;
import pula.sys.daos.StudentDao;
import pula.sys.daos.SysUserDao;
import pula.sys.daos.TeacherCardDao;
import pula.sys.daos.TeacherDao;
import pula.sys.domains.Branch;
import pula.sys.domains.Classroom;
import pula.sys.domains.Course;
import pula.sys.domains.CourseClient;
import pula.sys.domains.CourseTaskResult;
import pula.sys.domains.CourseTaskResultReportComments;
import pula.sys.domains.CourseTaskResultStudent;
import pula.sys.domains.CourseTaskResultWork;
import pula.sys.domains.FileAttachment;
import pula.sys.domains.Student;
import pula.sys.domains.SysUser;
import pula.sys.domains.Teacher;
import pula.sys.services.SessionUserService;
import pula.sys.vo.ReportBean;

@Controller
@Barrier(ignore = true)
public class CourseClientServiceController {

	private static final Logger logger = Logger
			.getLogger(CourseClientServiceController.class);
	private static final JsonResult NOT_ACTIVED = JsonResult.e("尚未注册!请先激活!")
			.withNo("999");
	@Resource
	CourseClientDao courseClientDao;
	@Resource
	CourseDeploymentDao courseDeploymentDao;
	@Resource
	StudentDao studentDao;
	@Resource
	TeacherDao teacherDao;
	@Resource
	SysUserDao sysUserDao;
	@Resource
	StudentCardDao studentCardDao;
	@Resource
	CardDao cardDao;
	@Resource
	TeacherCardDao teacherCardDao;
	@Resource
	CourseTaskResultDao courseTaskResultDao;
	@Resource
	SessionUserService sessionUserService;
	@Resource
	SessionService sessionService;
	@Resource
	CourseTaskResultStudentDao courseTaskResultStudentDao;

	@Resource
	CourseDao courseDao;
	@Resource
	ParameterKeeper parameterKeeper;
	@Resource
	CourseTaskResultWorkDao courseTaskResultWorkDao;
	@Resource
	FileAttachmentDao fileAttachmentDao;
	@Resource
	LoggerDao loggerDao;

	@ResponseBody
	@Transactional
	@RequestMapping
	public JsonResult requestActive(
			@RequestParam("code") String code,
			@RequestParam(value = "comments", required = false) String comments,
			HttpServletRequest req) {
		MapBean requestInfo = courseClientDao.hasRequest(code);

		if (requestInfo == null) {
			courseClientDao.request(code, comments, req.getRemoteAddr());
		} else {
			int status = requestInfo.asInteger("status");
			if (status == CourseClient.STATUS_LOCKED) {
				return JsonResult.e("当前机器码已被锁定,请与总部联系");
			} else if (status == CourseClient.STATUS_NEW) {
				return JsonResult.e("该机器码已经申请过激活,请耐心等待处理或与总部联系");
			} else if (status == CourseClient.STATUS_NORMAL) {
				return JsonResult.e("该机器码已激活,请使用同步功能激活");
			}

		}
		// System.out.println("code=" + code);

		return JsonResult.s(String.format("机器码%s已经申请激活，请等待处理或者与总部联系！", code));
	}

	// 获得激活信息
	@ResponseBody
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	@RequestMapping
	public JsonResult syncActive(@RequestParam("code") String code) {

		MapBean requestInfo = courseClientDao.hasRequest(code);
		if (requestInfo == null) {
			return JsonResult.e("尚未申请激活");
		} else {
			int status = requestInfo.asInteger("status");
			if (status == CourseClient.STATUS_LOCKED) {
				return JsonResult.e("当前机器码已被锁定,请与总部联系");
			} else if (status == CourseClient.STATUS_NEW) {
				return JsonResult.e("该机器码已经申请过激活尚未处理,请耐心等待或与总部联系");
			} else if (status == CourseClient.STATUS_NORMAL) {
				// return JsonResult.e("该机器码已激活,请使用同步功能激活");

				// 把当前的激活码返回
				// 节点id,节点名称
				return JsonResult.s(requestInfo);

			}

		}

		return JsonResult.e("尚未有匹配的状态");
	}

	/**
	 * 同步课程,加载
	 * 
	 * @param code
	 * @param activeCode
	 * @return
	 */
	@ResponseBody
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	@RequestMapping
	public JsonResult syncCourse(@RequestParam("code") String code,
			@RequestParam("activeCode") String activeCode) {
		Long classroomId = courseClientDao.getClassroomId(code, activeCode);
		if (classroomId == null) {
			return NOT_ACTIVED;
		}

		MapList courseList = courseDeploymentDao.mapList(classroomId);
		// 获得所有课程了.
		return JsonResult.s(courseList);

	}

	/**
	 * 上报数据
	 * 
	 * @param code
	 * @param activeCode
	 * @return
	 */
	@ResponseBody
	@Transactional()
	@RequestMapping
	public JsonResult report(@RequestParam("code") String code,
			@RequestParam("activeCode") String activeCode,
			@ObjectParam("bean") ReportBean bean) {

		// 拿到身份
		Object[] classroomId = courseClientDao.getClassroomIdAndBranchId(code,
				activeCode);
		if (classroomId[0] == null) {
			return NOT_ACTIVED;
		}

		long classroom_id = (Long) classroomId[0];
		long branch_id = (Long) classroomId[1];

		// 保存这个任务内容

		// toCourseTaskResult
		CourseTaskResult ctr;
		try {
			ctr = bean.toCourseTaskResult();
		} catch (ParseException e) {
			return Pe.raise("时间格式错误");
		}

		List<CourseTaskResultReportComments> results = prepare(bean, ctr,
				branch_id, classroom_id);

		List<Student> students = prepareStudents(bean, results, branch_id);

		// 数量
		ctr.setStudentCount(students.size());
		Mix<CourseTaskResult, Boolean> res = courseTaskResultDao.store(ctr);

		// 对应关系要有,一个是学生编号一个是id
		List<MapBean> receipts = courseTaskResultStudentDao.save(students,
				res.getObject1(), res.getObject2());

		// IP 记录一下
		results.add(CourseTaskResultReportComments.create(sessionService.env()
				.getIp()));

		courseTaskResultDao.storeComments(results, res.getObject1());

		// 凭证,用以上传文件

		// 最后一个的id 是result id
		receipts.add(0, MapBean.map("id", res.getObject1().getId()));
		return JsonResult.s(receipts);

	}

	private List<Student> prepareStudents(ReportBean bean,
			List<CourseTaskResultReportComments> results, long branch_id) {

		if (bean.getStudentRfid() == null || bean.getStudentNo() == null
				|| bean.getStudentName() == null) {
			results.add(CourseTaskResultReportComments.create("学生数据无效(NULL)"));
			return Collections.emptyList();
		}

		if (bean.getStudentRfid().length != bean.getStudentName().length
				|| bean.getStudentRfid().length != bean.getStudentName().length) {
			results.add(CourseTaskResultReportComments
					.create("学生数据无效(数据长度不匹配)"));
			return Collections.emptyList();

		}

		List<Student> ret = WxlSugar.newArrayList();

		// 去重
		List<String> exists_no = WxlSugar.newArrayList();
		int arrayLen = bean.getStudentRfid().length;
		for (int i = 0; i < arrayLen; i++) {
			String rfid = bean.getStudentRfid()[i];
			String name = bean.getStudentName()[i];
			String studentNo = bean.getStudentNo()[i];

			if (exists_no.contains(studentNo)) {
				results.add(CourseTaskResultReportComments
						.create("学生编号已经存在[同次提交]: " + studentNo + " " + name
								+ " " + rfid));
				continue;
			}

			// 查找学员
			MapBean studentMeta = studentDao.meta4exhange(studentNo);

			if (studentMeta == null) {
				// Pe.raise("指定的学员编号不存在:" + studentNo);

				results.add(CourseTaskResultReportComments.create("学生编号不存在: "
						+ studentNo + " " + name + " " + rfid));
				continue;
			}

			long this_branch_id = studentMeta.asLong("branchId");
			if (this_branch_id != branch_id) {
				// Pe.raise("指定的学员并非从属于当前分支机构");
				results.add(CourseTaskResultReportComments.create("学生数据不匹配: "
						+ studentNo + " " + name + " " + rfid));
				continue;
			}

			long studentId = studentMeta.asLong("id");
			Student s = Student.create(studentId);
			s.setNo(studentNo);
			ret.add(s);
			exists_no.add(studentNo);
		}

		return ret;
	}

	private List<CourseTaskResultReportComments> prepare(ReportBean bean,
			CourseTaskResult cc, long branch_id, long classroom_id) {
		List<CourseTaskResultReportComments> comments = WxlSugar.newArrayList();

		Long courseId = courseDao.getIdByNo(bean.getCourseNo());

		if (courseId == null) {
			comments.add(CourseTaskResultReportComments.create("课程信息不匹配:"
					+ bean.getCourseNo()));
		}

		MapBean m_meta = getTeacherMeta(bean.getMasterNo(),
				bean.getMasterName(), bean.getMasterRfid(), branch_id, true);
		MapBean a1_meta = getTeacherMeta(bean.getAssistant1No(),
				bean.getAssistant1Name(), bean.getAssistant1Rfid(), branch_id,
				true);
		MapBean a2_meta = getTeacherMeta(bean.getAssistant2No(),
				bean.getAssistant2Name(), bean.getAssistant2Rfid(), branch_id,
				false);

		if (m_meta != null && m_meta.asBoolean("notMatch")) {
			comments.add(CourseTaskResultReportComments.create("主教信息不匹配:"
					+ buildTeacher(m_meta)));
		} else if (m_meta != null) {
			cc.setMaster(Teacher.create(m_meta.asLong("id")));
		}

		if (a1_meta != null && a1_meta.asBoolean("notMatch")) {
			comments.add(CourseTaskResultReportComments.create("助教1信息不匹配:"
					+ buildTeacher(a1_meta)));
		} else if (a1_meta != null) {
			cc.setAssistant1(Teacher.create(a1_meta.asLong("id")));
		}

		if (a2_meta != null && a2_meta.asBoolean("notMatch")) {
			comments.add(CourseTaskResultReportComments.create("助教2信息不匹配:"
					+ buildTeacher(a2_meta)));
		} else if (a2_meta != null) {
			cc.setAssistant2(Teacher.create(a2_meta.asLong("id")));
		}

		cc.setBranch(Branch.create(branch_id));
		cc.setClassroom(Classroom.create(classroom_id));
		cc.setCourse(Course.create(courseId));

		return comments;

	}

	private String buildTeacher(MapBean m_meta) {
		return m_meta.string("no") + " " + m_meta.string("name") + " "
				+ m_meta.string("rfid");
	}

	private MapBean getTeacherMeta(String no, String name, String rfid,
			long branch_id, boolean ness) {
		MapBean mb = null;
		if (StringUtils.isEmpty(no)) {
			mb = null;

		} else {
			mb = teacherDao.meta4plan(no, branch_id);

		}

		if (mb == null && !StringUtils.isEmpty(no)) {
			mb = new MapBean();
			mb.add("notMatch", true).add("no", no).add("name", name)
					.add("rfid", rfid);
		}

		return mb;
	}

	/**
	 * 上报数据
	 * 
	 * @param code
	 * @param activeCode
	 * @return
	 */
	@ResponseBody
	@Transactional()
	@RequestMapping
	public JsonResult reportWork(@RequestParam("file") MultipartFile file,
			@RequestParam("id") long id,
			@RequestParam("studentId") long studentId) {

		// 第一个是 ctrs_id , 第二个是student_id

		try {
			PewUtils.checkUploadPic(file, file.getOriginalFilename(), false);
		} catch (RuntimeException ex) {
			// 文件格式不对?只是扩展名,不过拒绝了再说!
			ex.printStackTrace();
			return JsonResult.e(ex.getMessage());

		}

		MapBean workMeta = courseTaskResultWorkDao.meta4upload(id);

		CourseTaskResultWork w = null;
		if (workMeta == null) {
			// 新增作品

			w = new CourseTaskResultWork();
			w.setCourseTaskResultStudent(CourseTaskResultStudent
					.create(studentId));

			w = courseTaskResultWorkDao.save(w);

		} else {
			// 修改作品
			String newKey = RandomTool.getRandomString(10);
			courseTaskResultWorkDao.updateKey(newKey, workMeta.asLong("id"));
			w = new CourseTaskResultWork();
			w.setId(workMeta.asLong("id"));
			w.setAttachmentKey(newKey);
		}

		FileAttachment fa = null;
		try {
			fa = processFile(file);

		} catch (Exception ex) {
			ex.printStackTrace();
			return JsonResult.e(ex.getMessage());
		}

		// 保存文件信息
		fileAttachmentDao.save(w, WxlSugar.asList(fa), true);// 旧的都删掉了

		return JsonResult.s();

	}

	private FileAttachment processFile(MultipartFile file)
			throws IllegalStateException, IOException {
		String filePath = parameterKeeper
				.getFilePath(BhzqConstants.FILE_STUDENT_WORK_DIR);

		FileAttachment fa = new FileAttachment();
		fa.setExtName(FilenameUtils.getExtension(file.getOriginalFilename()));
		fa.setName(file.getOriginalFilename());
		fa.setType(FileAttachment.TYPE_STUENDT_WORK);
		fa.setFileId(RandomTool.getRandomString(10) + "."
				+ Calendar.getInstance().getTimeInMillis());

		// if (a.getId() == 0) {
		// build
		String dest = filePath + File.separatorChar + fa.getFileId();

		logger.debug("file=" + dest);
		file.transferTo(new File(dest));

		// } else {
		// fa.setId(a.getId());
		// }

		return fa;
	}

	// 登录
	@ResponseBody
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	@RequestMapping
	public JsonResult checkLogin(@RequestParam("username") String username,
			@RequestParam("password") String password) {

		MapBean usrMap = sysUserDao.meta4Login(username, password);
		if (usrMap == null) {
			return JsonResult.e("登录身份无效!");
		}

		return JsonResult.s();

	}

	// 发卡
	@ResponseBody
	@Transactional()
	@RequestMapping
	public JsonResult getInfo(@RequestParam("rfid") String rfid,
			@RequestParam("username") String username,
			@RequestParam("password") String password) {

		MapBean usrMap = sysUserDao.meta4Login(username, password);
		if (usrMap == null) {
			return JsonResult.e("登录身份无效!");

		}

		// 根据卡rfid 去加载学生或老师信息
		String logInfo = "";
		MapBean mb = studentCardDao.meta(rfid, usrMap.asLong("branchId"));
		if (mb == null) {
			mb = teacherCardDao.meta(rfid, usrMap.asLong("branchId"));

			if (mb == null) {
				return JsonResult.e("查无发卡记录,请检查该卡是否发放；或教师是否从属于该校");
			} else {
				// 教师标记
				mb.add("type", 2);
				logInfo = "教师";

			}
		} else {
			mb.add("type", 1); // 学生
			logInfo = "学生";
		}

		logInfo += ":" + mb.string("no") + "," + mb.string("name");
		SysUser su = SysUser.create(usrMap.string("id"));

		loggerDao.doLog("写卡", logInfo, su);

		return JsonResult.s(mb);

		// 信息返回

		//
		// MapBean mb = studentDao.meta4Card(studentNo,
		// usrMap.asLong("branchId"));
		// if (mb == null) {
		// return JsonResult.e("无效的学员编号:" + studentNo);
		// }
		//
		// return JsonResult.s(mb);
	}
	// 写卡
	// @ResponseBody
	// @Transactional()
	// @RequestMapping
	// public JsonResult writeStudent(@RequestParam("no") String studentNo,
	// @RequestParam("rfid") String rfid,
	// @RequestParam("user") String username,
	// @RequestParam("password") String password) {
	//
	// MapBean usrMap = sysUserDao.meta4Log(username, password);
	//
	// MapBean mb = studentDao.meta4Card(studentNo, usrMap.asLong("branchId"));
	// if (mb == null) {
	// return JsonResult.e("无效的学员编号:" + studentNo);
	// }
	//
	// Long cardId = cardDao.getIdByRfid(rfid);
	//
	// if (cardId == null) {
	// return JsonResult.e("卡无法识别,并未在系统内登记");
	// }
	//
	// //使用了卡
	// studentCardDao.saveIfNeed(Student.create(mb.asLong("id")), cardId,
	// false);
	//
	// return JsonResult.s(mb);
	// }
}
