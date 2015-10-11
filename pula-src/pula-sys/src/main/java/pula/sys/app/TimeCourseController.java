/**
 * 
 */
package pula.sys.app;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import puerta.support.AttachmentFile;
import puerta.support.PaginationSupport;
import puerta.support.Pe;
import puerta.support.ViewResult;
import puerta.support.annotation.Barrier;
import puerta.support.annotation.ObjectParam;
import puerta.support.utils.JacksonUtil;
import puerta.support.utils.MD5;
import puerta.support.utils.WxlSugar;
import puerta.system.dao.LoggerDao;
import puerta.system.keeper.ParameterKeeper;
import puerta.system.service.SessionService;
import puerta.system.vo.JsonResult;
import puerta.system.vo.YuiResult;
import puerta.system.vo.YuiResultMapper;
import pula.sys.BhzqConstants;
import pula.sys.PurviewConstants;
import pula.sys.conditions.CourseCondition;
import pula.sys.daos.FileAttachmentDao;
import pula.sys.daos.TimeCourseDao;
import pula.sys.domains.Course;
import pula.sys.domains.FileAttachment;
import pula.sys.domains.TimeCourse;
import pula.sys.forms.FileAttachmentForm;
import pula.sys.forms.TimeCourseForm;
import pula.sys.services.SessionUserService;
import pula.sys.util.FileUtil;

/**
 * @author Liangfei
 *
 */
@Controller
public class TimeCourseController {
    
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
            m.put("applicableAges", obj.getApplicableAges());
            m.put("comments", obj.getComments());
            return m;
        }
    };
    private static final YuiResultMapper<TimeCourse> MAPPING_FULL = new YuiResultMapper<TimeCourse>() {
        @Override
        public Map<String, Object> toMap(TimeCourse obj) {
            Map<String, Object> m = MAPPING.toMap(obj);
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
        
        YuiResult result = YuiResult.create(results, MAPPING);
        result.getRecords().clear();
        for (TimeCourse u : results.getItems()) {
            Map<String, Object> m = MAPPING.toMap(u);

            FileUtil.addIconToJson(fileAttachmentDao, u, m);

            result.getRecords().add(m);
        }

        return result;
    }

    
    @RequestMapping
    @Barrier(PurviewConstants.COURSE)
    public ModelAndView create() {
        return new ModelAndView().addObject("updateMode", false);
    }


    @RequestMapping
    @Transactional
    @Barrier(PurviewConstants.COURSE)
    public String _create(@ObjectParam("course") TimeCourseForm cli,
            @RequestParam("jsonAttachment") String jsonAttachment) {

        TimeCourse course = cli.toCourse();
        if (StringUtils.isEmpty(course.getNo())) {
            course.setNo(MD5.GetMD5String("tc@" + System.currentTimeMillis()));
        }
        
        // attachment
        prepareData(cli, jsonAttachment);

        String filePath = parameterKeeper.getFilePath(BhzqConstants.FILE_TIMECOURSE_ICON_DIR);
        List<FileAttachment> attachments = FileUtil.processFile(filePath, parameterKeeper, Arrays.asList(cli.getAttachmentForms()));
        for (FileAttachment fileAttachment : attachments) {
            fileAttachment.setType(FileAttachment.TYPE_TIME_COURSE_ICON);
        }

        // save work
        course.setUpdator(sessionService.get().getName());
        TimeCourse savedCourse = courseDao.save(course);
        // save attachment
        fileAttachmentDao.save(savedCourse, attachments, false);

        return ViewResult.JSON_SUCCESS;
    }
    
    @RequestMapping
    @Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
    @ResponseBody
    @Barrier(ignore = true)
    public AttachmentFile icon(
            @RequestParam(value = "fp", required = false) String fp,
            @RequestParam(value = "id", required = false) Long id,
            HttpServletResponse res) {

        String srcPath = null;
        if (id == null || id == 0) {
            srcPath = parameterKeeper
                    .getFilePath(BhzqConstants.FILE_UPLOAD_DIR);
        } else {
            srcPath = parameterKeeper
                    .getFilePath(BhzqConstants.FILE_TIMECOURSE_ICON_DIR);
        }
        String fullPath = srcPath + File.separatorChar + fp;

        return AttachmentFile.forShow(new File(fullPath));
    }
    
    @Resource
    private FileAttachmentDao fileAttachmentDao;
    
    @RequestMapping
    @Transactional
    @Barrier(PurviewConstants.COURSE)
    public ModelAndView update(@RequestParam("id") Long id) {
        TimeCourse course = courseDao.findById(id);
        if (course == null) {
            return new ModelAndView("error");
        }

        List<FileAttachment> attachments = fileAttachmentDao.loadByRefId(course.toRefId(), course.getTypeRange());

        FileAttachment icon = null;
        for (FileAttachment a : attachments) {
            if (!a.isRemoved() && a.getType() == FileAttachment.TYPE_TIME_COURSE_ICON) {
                icon = a;
                break;
            }
        }

        return new ModelAndView().addObject("course", course)
                .addObject("updateMode", true)
                .addObject("icon", icon)
                .addObject("attachments", attachments);
    }
    
    @RequestMapping
    @Transactional
    @Barrier(PurviewConstants.COURSE)
    public String _update(@ObjectParam("course") TimeCourseForm cli, @RequestParam("jsonAttachment") String jsonAttachment) {
        
        TimeCourse course = cli.toCourse();
        
        // attachment
        prepareData(cli, jsonAttachment);

        String filePath = parameterKeeper.getFilePath(BhzqConstants.FILE_TIMECOURSE_ICON_DIR);
        List<FileAttachment> attachments = FileUtil.processFile(filePath, parameterKeeper, Arrays.asList(cli.getAttachmentForms()));
        for (FileAttachment fileAttachment : attachments) {
            fileAttachment.setType(FileAttachment.TYPE_TIME_COURSE_ICON);
        }

        // save work
        course.setUpdator(sessionService.get().getName());
        TimeCourse savedCourse = courseDao.update(course);
        // save attachment 
        fileAttachmentDao.save(savedCourse, attachments, true);

        return ViewResult.JSON_SUCCESS;
    }

    private void prepareData(TimeCourseForm cli, String jsonAttachment) {
        List<FileAttachmentForm> items = null;
        try {
            items = JacksonUtil.getList(jsonAttachment, FileAttachmentForm.class);
        } catch (Exception e) {
            logger.error("jsonAttachment=" + jsonAttachment);
            Pe.raise(e.getMessage());
        }
        if (items != null && items.size() > 0) {
            cli.setAttachmentForms(items.get(0));
        }
    }

    
    
//    @RequestMapping
//    @Transactional
//    @Barrier(PurviewConstants.COURSE)
//    public String _create(@ObjectParam("course") TimeCourseForm cli) {
//
//        TimeCourse cc = cli.toCourse();
//        cc.setCreator(sessionService.get().getName());
//        cc.setUpdator(sessionService.get().getName());
//        cc.setEnabled(true);
//
//        courseDao.save(cc);
//        
//        return ViewResult.JSON_SUCCESS;
//    }
//
//    @RequestMapping
//    @Transactional()
//    @Barrier(PurviewConstants.COURSE)
//    public String _update(@ObjectParam("course") TimeCourseForm cli) {
//
//        TimeCourse cc = cli.toCourse();
//        cc.setUpdator(sessionService.get().getName());
//
//        courseDao.update(cc);
//
//        return ViewResult.JSON_SUCCESS;
//    }

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
        FileUtil.addIconToJson(fileAttachmentDao, u, m);
        return JsonResult.s(m);
    }

    @RequestMapping
    public ModelAndView appshow(@RequestParam(value = "id", required = false) Long id,
            @RequestParam(value = "no", required = false) String no) {
        ModelAndView view = new ModelAndView();

        TimeCourse u = null;
        if (id != null) {
            u = courseDao.findById(id);
        } else if (!StringUtils.isEmpty(no)) {
            u = courseDao.findByNo(no);
        }
        if (u == null) {
            view.setViewName("error");
            Exception e = new Exception(String.format("课程没找到！: " + id + no));
            view.addObject("exception", e);
            return view;
        }
        view.addObject("course", u);
        return view;
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
        Map<String, Object> m = MAPPING_FULL.toMap(u);
        FileUtil.addIconToJson(fileAttachmentDao, u, m);
        return JsonResult.s(m);
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
