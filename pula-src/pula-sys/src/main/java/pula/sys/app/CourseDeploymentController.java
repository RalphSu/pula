package pula.sys.app;

import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import puerta.support.PaginationSupport;
import puerta.support.Pe;
import puerta.support.ViewResult;
import puerta.support.annotation.Barrier;
import puerta.support.annotation.ObjectParam;
import puerta.system.keeper.ParameterKeeper;
import puerta.system.service.SessionService;
import puerta.system.vo.JsonResult;
import puerta.system.vo.MapBean;
import puerta.system.vo.MapList;
import puerta.system.vo.YuiResult;
import puerta.system.vo.YuiResultMapper;
import pula.sys.PurviewConstants;
import pula.sys.conditions.CourseDeploymentCondition;
import pula.sys.daos.BranchDao;
import pula.sys.daos.ClassroomDao;
import pula.sys.daos.CourseDeploymentDao;
import pula.sys.services.SessionUserService;

@Controller
public class CourseDeploymentController {

	/**
	 * Logger for this class
	 */
	@SuppressWarnings("unused")
	private static final Logger logger = Logger
			.getLogger(CourseDeploymentController.class);
	private static final YuiResultMapper<MapBean> MAPPING_FIX = new YuiResultMapper<MapBean>() {
		@Override
		public Map<String, Object> toMap(MapBean obj) {

			return obj;
		}
	};

	@Resource
	CourseDeploymentDao courseDeploymentDao;
	@Resource
	ParameterKeeper parameterKeeper;
	@Resource
	SessionService sessionService;
	@Resource
	SessionUserService sessionUserService;
	@Resource
	BranchDao branchDao;
	@Resource
	ClassroomDao classroomDao;

	@RequestMapping
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	@Barrier(PurviewConstants.COURSE_DEPLOYMENT)
	public ModelAndView entry(
			@ObjectParam("condition") CourseDeploymentCondition condition) {
		if (condition == null) {
			condition = new CourseDeploymentCondition();
		}

		MapList branchList = branchDao.loadMetaWithoutHeadquarter();

		return new ModelAndView().addObject("condition", condition).addObject(
				"branches", branchList);

	}

	@RequestMapping
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	@ResponseBody
	@Barrier(PurviewConstants.COURSE_DEPLOYMENT)
	public YuiResult list(
			@ObjectParam("condition") CourseDeploymentCondition condition,
			@RequestParam(value = "pageIndex", required = false, defaultValue = "0") int pageIndex) {
		if (condition == null) {
			condition = new CourseDeploymentCondition();
		}

		PaginationSupport<MapBean> results = courseDeploymentDao.searchMapBean(
				condition, pageIndex);

		return YuiResult.create(results, MAPPING_FIX);
	}

	@Transactional
	@RequestMapping
	@Barrier(PurviewConstants.COURSE_DEPLOYMENT)
	public String remove(
			@RequestParam(value = "objId", required = false) Long[] id) {
		courseDeploymentDao.deleteById(id);
		return ViewResult.JSON_SUCCESS;
	}

	@RequestMapping
	@Transactional
	@Barrier(value = PurviewConstants.COURSE_DEPLOY, check = false)
	@ResponseBody
	public JsonResult _deploy(@RequestParam("objId") Long[] objId,
			@RequestParam("classroomId") Long classroomId) {

		courseDeploymentDao.save(objId, classroomId,
				sessionService.getActorId());

		return JsonResult.s();
	}

	@RequestMapping
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	@ResponseBody
	@Barrier()
	public JsonResult listByClassroom(
			@RequestParam("classroomId") long classroomId,
			@RequestParam("categoryId") String categoryId) {

		if (!sessionUserService.isHeadquarter()) {
			if (!classroomDao.isBelongsTo(classroomId, sessionUserService
					.getBranch().getIdLong())) {
				Pe.raise("越权访问");
			}
		}

		MapList courses = courseDeploymentDao.list(classroomId, categoryId);

		return JsonResult.s(courses);
	}
}
