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
import pula.sys.conditions.NoticeCondition;
import pula.sys.daos.FileAttachmentDao;
import pula.sys.daos.NoticeDao;
import pula.sys.daos.TimeCourseDao;
import pula.sys.domains.Course;
import pula.sys.domains.FileAttachment;
import pula.sys.domains.Notice;
import pula.sys.domains.TimeCourse;
import pula.sys.forms.FileAttachmentForm;
import pula.sys.forms.NoticeForm;
import pula.sys.services.SessionUserService;
import pula.sys.util.FileUtil;

/**
 * @author Liangfei
 *
 * TODO: 权限未和其余的domain object分开
 */
@Controller
public class NoticeController {

    private static final Logger logger = Logger.getLogger(Course.class);
    public static final YuiResultMapper<Notice> MAPPING = new YuiResultMapper<Notice>() {
        @Override
        public Map<String, Object> toMap(Notice obj) {
            Map<String, Object> m = WxlSugar.newHashMap();
            m.put("no", obj.getNo());
            m.put("id", obj.getId());
            m.put("title", obj.getTitle());
            m.put("formattedTitle", obj.getFormattedTitle());
            m.put("content", obj.getContent());
            m.put("enabled", obj.isEnabled());
            m.put("updateTime", obj.getUpdateTime());
            m.put("updator", obj.getUpdator());
            m.put("imgPath", obj.getImgPath());
            m.put("suffix", obj.getSuffix());
            m.put("comment", obj.getComment());
            m.put("noticeCount", obj.getNoticeCount());
            m.put("noticeCourseNo", obj.getNoticeCourseNo());
            m.put("noticePrice", obj.getNoticePrice());
            m.put("noticeCourseName", obj.getNoticeCourseName());
            return m;
        }
    };

    @Resource
    private NoticeDao noticeDao;
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
    public ModelAndView entry(@ObjectParam("condition") NoticeCondition condition) {
        if (condition == null) {
            condition = new NoticeCondition();
        }

        return new ModelAndView().addObject("condition", condition);
    }

    @RequestMapping
    @Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
    @ResponseBody
    @Barrier(PurviewConstants.COURSE)
    public YuiResult list(@ObjectParam("condition") NoticeCondition condition,
            @RequestParam(value = "pageIndex", required = false, defaultValue = "0") int pageIndex) {
        if (condition == null) {
            condition = new NoticeCondition();
        }
        PaginationSupport<Notice> results = null;
        results = noticeDao.search(condition, pageIndex);

        YuiResult result = YuiResult.create(results, MAPPING);
        result.getRecords().clear();
        for (Notice u : results.getItems()) {
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
    @Barrier(PurviewConstants.COURSE_WORK)
    public String _create(@ObjectParam("notice") NoticeForm cli,
            @RequestParam("jsonAttachment") String jsonAttachment) {

        Notice notice = cli.toNotice();
        if (StringUtils.isEmpty(notice.getNo())) {
            notice.setNo(MD5.GetMD5String("notice@" + System.currentTimeMillis()));
        }
        
        // attachment
        prepareData(cli, jsonAttachment);

        String filePath = parameterKeeper.getFilePath(BhzqConstants.FILE_NOTICE_ICON_DIR);
        List<FileAttachment> attachments = FileUtil.processFile(filePath, parameterKeeper, Arrays.asList(cli.getAttachmentForms()));
        for (FileAttachment fileAttachment : attachments) {
            fileAttachment.setType(FileAttachment.TYPE_NOTICE_ICON);
        }

        // save work
        notice.setUpdator(sessionService.get().getName());
        Notice savedNotice = noticeDao.save(notice);
        // save attachment
        fileAttachmentDao.save(savedNotice, attachments, false);

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
                    .getFilePath(BhzqConstants.FILE_NOTICE_ICON_DIR);
        }
        String fullPath = srcPath + File.separatorChar + fp;

        return AttachmentFile.forShow(new File(fullPath));
    }
    
    @Resource
    private FileAttachmentDao fileAttachmentDao;
    
    @RequestMapping
    @Transactional
    @Barrier(PurviewConstants.COURSE_WORK)
    public ModelAndView update(@RequestParam("id") Long id) {
        Notice notice = noticeDao.findById(id);
        if (notice == null) {
            return new ModelAndView("error");
        }

        List<FileAttachment> attachments = fileAttachmentDao.loadByRefId(notice.toRefId(), notice.getTypeRange());

        FileAttachment icon = null;
        for (FileAttachment a : attachments) {
            if (!a.isRemoved() && a.getType() == FileAttachment.TYPE_NOTICE_ICON) {
                icon = a;
                break;
            }
        }

        return new ModelAndView().addObject("notice", notice)
                .addObject("updateMode", true)
                .addObject("icon", icon)
                .addObject("attachments", attachments);
    }
    
    @RequestMapping
    @Transactional
    @Barrier(PurviewConstants.COURSE_WORK)
    public String _update(@ObjectParam("notice") NoticeForm cli, @RequestParam("jsonAttachment") String jsonAttachment) {
        
        Notice notice = cli.toNotice();
        
        // attachment
        prepareData(cli, jsonAttachment);

        String filePath = parameterKeeper.getFilePath(BhzqConstants.FILE_NOTICE_ICON_DIR);
        List<FileAttachment> attachments = FileUtil.processFile(filePath, parameterKeeper, Arrays.asList(cli.getAttachmentForms()));
        for (FileAttachment fileAttachment : attachments) {
            fileAttachment.setType(FileAttachment.TYPE_NOTICE_ICON);
        }

        // save work
        notice.setUpdator(sessionService.get().getName());
        Notice savedNotice = noticeDao.update(notice);
        // save attachment 
        fileAttachmentDao.save(savedNotice, attachments, true);

        return ViewResult.JSON_SUCCESS;
    }
    

    private void prepareData(NoticeForm cli, String jsonAttachment) {
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


    @Transactional
    @RequestMapping
    @Barrier(PurviewConstants.COURSE)
    public String remove(@RequestParam(value = "objId", required = false) Long[] id) {
        noticeDao.deleteById(id);
        logDao.doLog("deleteTimeCourse", StringUtils.join(id));
        return ViewResult.JSON_SUCCESS;
    }

    @RequestMapping
    @Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
    @ResponseBody
    @Barrier()
    public JsonResult get(@RequestParam("id") Long id) {
        Notice u = noticeDao.findById(id);

        Map<String, Object> m = MAPPING.toMap(u);
        FileUtil.addIconToJson(fileAttachmentDao, u, m);
        return JsonResult.s(m);
    }

    @Resource
    private TimeCourseDao courseDao;

    /**
     * show the contents of a given notice in web page.
     * @param id
     * @return
     */
    @RequestMapping
    public ModelAndView appshow(@RequestParam(value = "id", required = false) Long id,
            @RequestParam(value = "no", required = false) String no) {
        Notice u = null;
        if (id != null) {
            u = noticeDao.findById(id);
        } else if (!StringUtils.isAlpha(no)) {
            u = noticeDao.findByNo(no);
        }
        
        ModelAndView view = new ModelAndView();
        if (u == null) {
            view.setViewName("error");
            Exception e = new Exception(String.format("找不到指定的通知:！" + id + no));
            view.addObject("exception", e);
            return view;
        }

        if (!StringUtils.isEmpty(u.getNoticeCourseNo())) {
            u.setNoticeCourseName("..."); // default name placement
            TimeCourse tc = courseDao.findByNo(u.getNoticeCourseNo());
            if (tc != null) {
                u.setNoticeCourseName(tc.getName());
            }
        }
        
        List<FileAttachment> attachments = fileAttachmentDao.loadByRefId(u.toRefId(), u.getTypeRange());
        if (attachments != null && attachments.size() > 0) {
            view.addObject("af", attachments.get(0));
        }

        return view.addObject("notice", u);
    }
    
    /**
     * show the contents of a given notice in web page.
     * @param id
     * @return
     */
    @RequestMapping
    public ModelAndView show(@RequestParam("id") Long id)
    {
        Notice u = noticeDao.findById(id);
        
        return new ModelAndView().addObject("notice", u);
    }
    

    @RequestMapping
    @Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
    @ResponseBody
    @Barrier()
    public JsonResult getByNo(@RequestParam("no") String no) {
        Notice u = noticeDao.findByNo(no);
        if (u == null) {
            Pe.raise("找不到指定的编号:" + no);
        }
        Map<String, Object> m = MAPPING.toMap(u);
        FileUtil.addIconToJson(fileAttachmentDao, u, m);
        return JsonResult.s();
    }

    @Transactional
    @RequestMapping
    @Barrier(PurviewConstants.COURSE)
    public String enable(@RequestParam(value = "objId", required = false) Long[] id,
            @RequestParam(value = "enable", required = false) boolean enable) {
        noticeDao.doEnable(id, enable);
        return ViewResult.JSON_SUCCESS;
    }

}
