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

import puerta.PuertaWeb;
import puerta.support.AttachmentFile;
import puerta.support.PaginationSupport;
import puerta.support.Pe;
import puerta.support.ViewResult;
import puerta.support.annotation.Barrier;
import puerta.support.annotation.ObjectParam;
import puerta.support.utils.JacksonUtil;
import puerta.support.utils.MD5;
import puerta.support.utils.WxlSugar;
import puerta.support.vo.SelectOptionList;
import puerta.system.keeper.ParameterKeeper;
import puerta.system.service.SessionService;
import puerta.system.vo.JsonResult;
import puerta.system.vo.YuiResult;
import puerta.system.vo.YuiResultMapper;
import pula.sys.BhzqConstants;
import pula.sys.PurviewConstants;
import pula.sys.conditions.TimeCourseWorkCondition;
import pula.sys.daos.BranchDao;
import pula.sys.daos.FileAttachmentDao;
import pula.sys.daos.StudentDao;
import pula.sys.daos.TimeCourseDao;
import pula.sys.daos.TimeCourseWorkDao;
import pula.sys.domains.FileAttachment;
import pula.sys.domains.Student;
import pula.sys.domains.TimeCourse;
import pula.sys.domains.TimeCourseOrderUsage;
import pula.sys.domains.TimeCourseWork;
import pula.sys.forms.FileAttachmentForm;
import pula.sys.forms.TimeCourseWorkForm;
import pula.sys.services.SessionUserService;
import pula.sys.util.FileUtil;

/**
 * @author Liangfei
 *
 */
@Controller
public class TimeCourseWorkController {

    private static final Logger logger = Logger.getLogger(TimeCourseOrderUsage.class);
    private static final YuiResultMapper<TimeCourseWork> MAPPING = new YuiResultMapper<TimeCourseWork>() {
        @Override
        public Map<String, Object> toMap(TimeCourseWork obj) {
            Map<String, Object> m = WxlSugar.newHashMap();
            m.put("no", obj.getNo());
            m.put("id", obj.getId());
            m.put("createTime", obj.getCreateTime());
            m.put("updateTime", obj.getUpdateTime());
            m.put("enabled", obj.isEnabled());
            m.put("comments", obj.getComments());
            m.put("courseNo", obj.getCourseNo());
            m.put("studentNo", obj.getStudentNo());
            m.put("imagePath", obj.getImagePath());
            m.put("workEffectDate", obj.getWorkEffectDate());
            m.put("branchNo", obj.getBranchNo());
            m.put("updator", obj.getUpdator());
            m.put("attachmentKey", obj.getAttachmentKey());
            m.put("rate", obj.getRate());
            return m;
        }
    };

    @Resource
    TimeCourseWorkDao workDao;
    @Resource
    TimeCourseDao courseDao;
    @Resource
    StudentDao studentDao;
    @Resource
    SessionService sessionService;
    @Resource
    SessionUserService sessionUserService;
    @Resource
    BranchDao branchDao;
    @Resource
    ParameterKeeper parameterKeeper;
    @Resource
    FileAttachmentDao fileAttachmentDao;

    @RequestMapping
    @Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
    @Barrier(PurviewConstants.COURSE_WORK)
    public ModelAndView entry(@ObjectParam("condition") TimeCourseWorkCondition condition) {
        if (condition == null) {
            condition = new TimeCourseWorkCondition();
        }

        List<TimeCourse> courses = courseDao.loadAll();

        SelectOptionList statusList = PuertaWeb.getYesNoList(PuertaWeb.YES, new String[] { "有效", "无效" });
        return new ModelAndView()
                .addObject("condition", condition)
                .addObject("statusList", statusList)
                .addObject("courses", courses);
    }

    @RequestMapping
    @Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
    @Barrier(PurviewConstants.COURSE_WORK)
    @ResponseBody
    public YuiResult list(@ObjectParam("condition") TimeCourseWorkCondition condition,
            @RequestParam(value = "pageIndex", required = false, defaultValue = "0") int pageIndex) {

        if (condition == null) {
            condition = new TimeCourseWorkCondition();
        }
        PaginationSupport<TimeCourseWork> works = workDao.search(condition, pageIndex);

        YuiResult result = YuiResult.create(works, MAPPING);
        result.getRecords().clear();
        for (TimeCourseWork u : works.getItems()) {
            Map<String, Object> m = MAPPING.toMap(u);
            
            FileUtil.addIconToJson(fileAttachmentDao, u, m);

            result.getRecords().add(m);
        }

        return result;
    }

    @RequestMapping
    @Transactional
    @Barrier(PurviewConstants.COURSE_WORK)
    public ModelAndView create() {
        List<TimeCourse> courses = courseDao.loadAll();
        return new ModelAndView().addObject("updateMode", false).addObject("courses", courses);
    }

    @RequestMapping
    @Transactional
    @Barrier(PurviewConstants.COURSE_WORK)
    public String _create(@ObjectParam("work") TimeCourseWorkForm cli,
            @RequestParam("jsonAttachment") String jsonAttachment) {

        TimeCourseWork work = cli.toWork();
        long branchId = sessionUserService.getBranch().getIdLong();
        String branchNo = branchDao.getPrefix(branchId);
        work.setBranchNo(branchNo);
        if (StringUtils.isEmpty(work.getNo())) {
            work.setNo(MD5.GetMD5String("timework@" + System.currentTimeMillis()));
        }
        // for create, do simple validate
        validateWorkCreate(work);
        
        // attachment
        prepareData(cli, jsonAttachment);
        if (cli.getAttachmentForms() == null) {
            Pe.raise("没有作品上传！");
        }

        String filePath = parameterKeeper.getFilePath(BhzqConstants.FILE_STUDENT_WORK_DIR);
        List<FileAttachment> attachments = FileUtil.processFile(filePath, parameterKeeper, Arrays.asList(cli.getAttachmentForms()));
        for (FileAttachment fileAttachment : attachments) {
            fileAttachment.setType(FileAttachment.TYPE_STUENDT_TIME_COURSE_WORK);
        }

        // save work
        work.setUpdator(sessionService.get().getName());
        TimeCourseWork savedWork = workDao.save(work);
        // save attachment
        fileAttachmentDao.save(savedWork, attachments, false);

        return ViewResult.JSON_SUCCESS;
    }
    

    private void validateWorkCreate(TimeCourseWork work) {
        TimeCourse course = courseDao.findByNo(work.getCourseNo());
        if (course == null) {
            Pe.raise(String.format("课程号{0}找不到对应的课程！", work.getCourseNo()));
        }
        Student s = studentDao.findByNo(work.getStudentNo());
        if (s == null) {
            Pe.raise(String.format("学号{0}找不到对应的学员！", work.getStudentNo()));
        }
    }

    private void prepareData(TimeCourseWorkForm cli, String jsonAttachment) {
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

    @RequestMapping
    @Transactional
    @Barrier(PurviewConstants.COURSE_WORK)
    public String _update(@ObjectParam("work") TimeCourseWorkForm cli, @RequestParam("jsonAttachment") String jsonAttachment) {
        
        TimeCourseWork work = cli.toWork();
        long branchId = sessionUserService.getBranch().getIdLong();
        String branchNo = branchDao.getPrefix(branchId);
        work.setBranchNo(branchNo);
        
        // attachment
        prepareData(cli, jsonAttachment);
        if (cli.getAttachmentForms() == null) {
            Pe.raise("没有作品上传！");
        }

        String filePath = parameterKeeper.getFilePath(BhzqConstants.FILE_STUDENT_WORK_DIR);
        List<FileAttachment> attachments = FileUtil.processFile(filePath, parameterKeeper, Arrays.asList(cli.getAttachmentForms()));
        for (FileAttachment fileAttachment : attachments) {
            fileAttachment.setType(FileAttachment.TYPE_STUENDT_TIME_COURSE_WORK);
        }

        // save work
        work.setUpdator(sessionService.get().getName());
        TimeCourseWork savedWork = workDao.update(work);
        // save attachment 
        fileAttachmentDao.save(savedWork, attachments, true);

        return ViewResult.JSON_SUCCESS;
    }

    @RequestMapping
    @Transactional
    @Barrier(PurviewConstants.COURSE_WORK)
    public ModelAndView update(@RequestParam("id") Long id) {
        TimeCourseWork work = workDao.findById(id);
        if (work == null) {
            return new ModelAndView("error");
        }

        List<FileAttachment> attachments = fileAttachmentDao.loadByRefId(work.toRefId(), FileAttachment.TYPE_STUENDT_TIME_COURSE_WORK);

        FileAttachment icon = null;
        for (FileAttachment a : attachments) {
            if (!a.isRemoved() && a.getType() == FileAttachment.TYPE_STUENDT_TIME_COURSE_WORK) {
                icon = a;
                break;
            }
        }

        List<TimeCourse> courses = courseDao.loadAll();
        return new ModelAndView().addObject("work", work)
                .addObject("updateMode", true)
                .addObject("icon", icon)
                .addObject("attachments", attachments)
                .addObject("courses", courses);
    }

    @RequestMapping
    @Transactional
    @Barrier(PurviewConstants.COURSE_WORK)
    public ModelAndView view(@RequestParam("id") Long id) {
        TimeCourseWork work = workDao.findById(id);
        if (work == null) {
            return new ModelAndView("error");
        }
        return new ModelAndView().addObject("work", work);
    }

    @RequestMapping
    @Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
    @ResponseBody
    @Barrier()
    public JsonResult get(@RequestParam(value = "id", required = false) Long id,
            @RequestParam(value = "no", required = false) String no) {
        TimeCourseWork u = null;
        if (id != null) {
            u = workDao.findById(id);
        } else if (!StringUtils.isEmpty(no)) {
            u = workDao.findByNo(no);
        }

        if (u == null) {
            return JsonResult.e(String.format("找不到对应的作品, id:{1}, no :{2}", id, no));
        }

        Map<String, Object> m = MAPPING.toMap(u);
        FileUtil.addIconToJson(fileAttachmentDao, u, m);

        return JsonResult.s(m);
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
                    .getFilePath(BhzqConstants.FILE_STUDENT_WORK_DIR);
        }
        String fullPath = srcPath + File.separatorChar + fp;

        return AttachmentFile.forShow(new File(fullPath));
    }

    @RequestMapping
    public ModelAndView appshow(
            @RequestParam(value = "id", required = false) Long id,
            @RequestParam(value = "no", required = false) String no) {
        
        TimeCourseWork u = null;
        if (id != null) {
            u = workDao.findById(id);
        } else if (!StringUtils.isEmpty(no)) {
            u = workDao.findByNo(no);
        }

        if (u == null) {
        	ModelAndView view = new ModelAndView("jsonError");
        	Exception e = new Exception(String.format("找不到指定的通知:！" + id + no));
            view.addObject("exception", e);
            return view;
        }
        
        Student student = studentDao.findByNo(u.getStudentNo());
        if (student != null) {
        	u.setStudentName(student.getName());
        }
        TimeCourse tc = courseDao.findByNo(u.getCourseNo());
        if (tc != null) {
        	u.setCourseName(tc.getName());
        }
        
        ModelAndView view = new ModelAndView();
        
        List<FileAttachment> attachments = fileAttachmentDao.loadByRefId(u.toRefId(), FileAttachment.TYPE_STUENDT_TIME_COURSE_WORK);
        if (attachments != null && attachments.size() > 0) {
            view.addObject("af", attachments.get(0));
        }
        view.addObject("work", u);
        return view;
    }

    @Transactional
    @RequestMapping
    @Barrier(PurviewConstants.COURSE_WORK)
    public String remove(
            @RequestParam(value = "objId", required = false) Long[] id) {
        workDao.deleteById(id);
        return ViewResult.JSON_SUCCESS;
    }

}
