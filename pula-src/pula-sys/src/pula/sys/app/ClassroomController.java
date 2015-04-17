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
import puerta.support.utils.WxlSugar;
import puerta.system.keeper.ParameterKeeper;
import puerta.system.service.SessionService;
import puerta.system.vo.JsonResult;
import puerta.system.vo.MapList;
import puerta.system.vo.YuiResult;
import puerta.system.vo.YuiResultMapper;
import pula.sys.PurviewConstants;
import pula.sys.conditions.ClassroomCondition;
import pula.sys.daos.BranchDao;
import pula.sys.daos.ClassroomDao;
import pula.sys.domains.Classroom;
import pula.sys.domains.SysUser;
import pula.sys.forms.ClassroomForm;

@Controller
public class ClassroomController {

	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(Classroom.class);
	private static final YuiResultMapper<Classroom> MAPPING = new YuiResultMapper<Classroom>() {
		@Override
		public Map<String, Object> toMap(Classroom obj) {

			Map<String, Object> m = WxlSugar.newHashMap();
			m.put("no", obj.getNo());
			m.put("id", obj.getId());
			m.put("name", obj.getName());
			m.put("branchName", obj.getBranch().getName());
			m.put("enabled", obj.isEnabled());

			return m;
		}
	};

	private static final YuiResultMapper<Classroom> MAPPING_FULL = new YuiResultMapper<Classroom>() {
		@Override
		public Map<String, Object> toMap(Classroom obj) {

			Map<String, Object> m = MAPPING.toMap(obj);

			m.put("branchId", obj.getBranch().getId());

			return m;
		}
	};

	@Resource
	ClassroomDao classroomDao;
	@Resource
	ParameterKeeper parameterKeeper;
	@Resource
	SessionService sessionService;
	@Resource
	BranchDao branchDao;

	@RequestMapping
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	@Barrier(PurviewConstants.CLASSROOM)
	public ModelAndView entry(
			@ObjectParam("condition") ClassroomCondition condition) {
		if (condition == null) {
			condition = new ClassroomCondition();
		}

		MapList branchList = branchDao.loadMeta();

		return new ModelAndView().addObject("condition", condition).addObject(
				"branches", branchList);
	}

	@RequestMapping
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	@ResponseBody
	@Barrier(PurviewConstants.CLASSROOM)
	public YuiResult list(
			@ObjectParam("condition") ClassroomCondition condition,
			@RequestParam(value = "pageIndex", required = false, defaultValue = "0") int pageIndex) {
		if (condition == null) {
			condition = new ClassroomCondition();
		}
		PaginationSupport<Classroom> results = null;
		results = classroomDao.search(condition, pageIndex);
		return YuiResult.create(results, MAPPING);
	}

	@RequestMapping
	@Barrier(PurviewConstants.CLASSROOM)
	public String create() {
		return null;
	}

	@RequestMapping
	@Transactional
	@Barrier(PurviewConstants.CLASSROOM)
	public String _create(@ObjectParam("classroom") ClassroomForm cli) {

		Classroom cc = cli.toClassroom();

		cc.setCreator(SysUser.create(sessionService.getActorId()));
		classroomDao.save(cc);

		return ViewResult.JSON_SUCCESS;
	}

	@RequestMapping
	@Transactional()
	@Barrier(PurviewConstants.CLASSROOM)
	public String _update(@ObjectParam("classroom") ClassroomForm cli) {

		Classroom cc = cli.toClassroom();
		cc.setUpdater(SysUser.create(sessionService.getActorId()));
		classroomDao.update(cc);

		return ViewResult.JSON_SUCCESS;
	}

	@Transactional
	@RequestMapping
	@Barrier(PurviewConstants.CLASSROOM)
	public String remove(
			@RequestParam(value = "objId", required = false) Long[] id) {
		classroomDao.deleteById(id);
		return ViewResult.JSON_SUCCESS;
	}

	@RequestMapping
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	@ResponseBody
	@Barrier()
	public JsonResult get(@RequestParam("id") Long id) {
		Classroom u = classroomDao.findById(id);

		Map<String, Object> m = MAPPING_FULL.toMap(u);

		return JsonResult.s(m);
	}

	@RequestMapping
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	@ResponseBody
	@Barrier()
	public JsonResult getByNo(@RequestParam("no") String no) {
		Classroom u = classroomDao.findByNo(no);
		if (u == null) {
			Pe.raise("找不到指定的编号:" + no);
		}
		return JsonResult.s(MAPPING_FULL.toMap(u));
	}

	@RequestMapping
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	@Barrier()
	public ModelAndView find(
			@RequestParam(value = "no", required = false) String no,

			@RequestParam(value = "t", required = false) String t,

			@RequestParam(value = "prefix", required = false) String prefix) {
		ModelAndView m = new ModelAndView(ViewResult.JSON_LIST);
		MapList list = classroomDao.loadByKeywords(no, t, prefix);
		logger.debug("list.size=" + list.size());
		m.addObject("list", list);
		return m;
	}

	@Transactional
	@RequestMapping
	@Barrier(PurviewConstants.CLASSROOM)
	public String enable(
			@RequestParam(value = "objId", required = false) Long[] id,
			@RequestParam(value = "enable", required = false) boolean enable) {
		classroomDao.doEnable(id, enable);
		return ViewResult.JSON_SUCCESS;
	}

	@RequestMapping
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	@ResponseBody
	@Barrier(value={PurviewConstants.CLASSROOM,PurviewConstants.COURSE_DEPLOY,PurviewConstants.COURSE_DEPLOYMENT},check=false)
	public JsonResult listByBranch(
			@RequestParam("branchId") long branchId) {
		MapList classroomList = classroomDao.list(branchId);

		return JsonResult.s(classroomList);
	}
}
