/**
 * 
 */
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
import puerta.system.dao.LoggerDao;
import puerta.system.keeper.ParameterKeeper;
import puerta.system.service.SessionService;
import puerta.system.vo.JsonResult;
import puerta.system.vo.YuiResult;
import puerta.system.vo.YuiResultMapper;
import pula.sys.PurviewConstants;
import pula.sys.conditions.CourseCondition;
import pula.sys.daos.TimeCourseDao;
import pula.sys.domains.Course;
import pula.sys.domains.TimeCourse;
import pula.sys.forms.TimeCourseForm;
import pula.sys.services.SessionUserService;

/**
 * @author Liangfei
 *
 */
@Controller
public class TimeCourseController {
    
    @SuppressWarnings("unused")
    private static final Logger logger = Logger.getLogger(Course.class);
    private static final YuiResultMapper<TimeCourse> MAPPING = new YuiResultMapper<TimeCourse>() {
        @Override
        public Map<String, Object> toMap(TimeCourse obj) {

            Map<String, Object> m = WxlSugar.newHashMap();
            m.put("no", obj.getNo());
            m.put("id", obj.getId());
            m.put("name", obj.getName());
            m.put("startTime", obj.getStartTime());
            m.put("endTime", obj.getEndTime());
            m.put("enabled", obj.isEnabled());
            m.put("branchName", obj.getBranchName());
            m.put("classRoomName", obj.getClassRoomName());
            m.put("startHour", obj.getStartHour());
            m.put("startMinute", obj.getStartMinute());
            m.put("startWeekDay", obj.getStartWeekDay());
            m.put("durationMinute", obj.getDurationMinute());
            m.put("price", obj.getPrice());
            m.put("maxStudentNum", obj.getMaxStudentNum());

            return m;
        }
    };
    private static final YuiResultMapper<TimeCourse> MAPPING_FULL = new YuiResultMapper<TimeCourse>() {
        @Override
        public Map<String, Object> toMap(TimeCourse obj) {

            Map<String, Object> m = MAPPING.toMap(obj);
            m.put("comments", obj.getComments());
            return m;
        }
    };
    
    @Resource
    private TimeCourseDao courseDao;
    @Resource
    private LoggerDao logDao;
    @Resource
    private ParameterKeeper parameterKeeper;
    @Resource
    private SessionService sessionService;
    @Resource
    private SessionUserService sessionUserService;

    @RequestMapping
    @Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
    @Barrier(PurviewConstants.COURSE)
    public ModelAndView entry(
            @ObjectParam("condition") CourseCondition condition) {
        if (condition == null) {
            condition = new CourseCondition();
        }
        
        return new ModelAndView().addObject("condition", condition);
    }

    @RequestMapping
    @Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
    @ResponseBody
    @Barrier(PurviewConstants.COURSE)
    public YuiResult list(
            @ObjectParam("condition") CourseCondition condition,
            @RequestParam(value = "pageIndex", required = false, defaultValue = "0") int pageIndex) {
        if (condition == null) {
            condition = new CourseCondition();
        }
        PaginationSupport<TimeCourse> results = null;
        results = courseDao.search(condition, pageIndex);
        return YuiResult.create(results, MAPPING);
    }

    
    @RequestMapping
    @Barrier(PurviewConstants.COURSE)
    public String create() {
        return null;
    }

    @RequestMapping
    @Transactional
    @Barrier(PurviewConstants.COURSE)
    public String _create(@ObjectParam("course") TimeCourseForm cli) {

        TimeCourse cc = cli.toCourse();
        cc.setCreator(sessionService.getActorId());
        cc.setUpdator(sessionService.getActorId());
        cc.setEnabled(true);

        courseDao.save(cc);
        
        return ViewResult.JSON_SUCCESS;
    }

    @RequestMapping
    @Transactional()
    @Barrier(PurviewConstants.COURSE)
    public String _update(@ObjectParam("course") TimeCourseForm cli) {

        TimeCourse cc = cli.toCourse();
        cc.setUpdator(sessionService.getActorId());

        courseDao.update(cc);

        return ViewResult.JSON_SUCCESS;
    }

    @Transactional
    @RequestMapping
    @Barrier(PurviewConstants.COURSE)
    public String remove(
            @RequestParam(value = "objId", required = false) Long[] id) {
        courseDao.deleteById(id);
        return ViewResult.JSON_SUCCESS;
    }

    @RequestMapping
    @Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
    @ResponseBody
    @Barrier()
    public JsonResult get(@RequestParam("id") Long id) {
        TimeCourse u = courseDao.findById(id);

        Map<String, Object> m = MAPPING_FULL.toMap(u);

        return JsonResult.s(m);
    }

    @RequestMapping
    @Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
    @ResponseBody
    @Barrier()
    public JsonResult getByNo(@RequestParam("no") String no) {
        TimeCourse u = courseDao.findByNo(no);
        if (u == null) {
            Pe.raise("找不到指定的编号:" + no);
        }
        return JsonResult.s(MAPPING_FULL.toMap(u));
    }

    @Transactional
    @RequestMapping
    @Barrier(PurviewConstants.COURSE)
    public String enable(
            @RequestParam(value = "objId", required = false) Long[] id,
            @RequestParam(value = "enable", required = false) boolean enable) {
        courseDao.doEnable(id, enable);
        return ViewResult.JSON_SUCCESS;
    }
}
