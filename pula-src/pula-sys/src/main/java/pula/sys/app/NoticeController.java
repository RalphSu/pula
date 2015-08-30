/**
 * 
 */
package pula.sys.app;

import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
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
import pula.sys.conditions.NoticeCondition;
import pula.sys.daos.NoticeDao;
import pula.sys.daos.TimeCourseDao;
import pula.sys.domains.Course;
import pula.sys.domains.Notice;
import pula.sys.domains.TimeCourse;
import pula.sys.services.SessionUserService;

/**
 * @author Liangfei
 *
 * TODO: 权限未和其余的domain object分开
 */
@Controller
public class NoticeController {

    @SuppressWarnings("unused")
    private static final Logger logger = Logger.getLogger(Course.class);
    private static final YuiResultMapper<Notice> MAPPING = new YuiResultMapper<Notice>() {
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
    private static final YuiResultMapper<Notice> MAPPING_FULL = new YuiResultMapper<Notice>() {
        @Override
        public Map<String, Object> toMap(Notice obj) { 

            Map<String, Object> m = MAPPING.toMap(obj);
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
    public String _create(@ObjectParam("course") Notice cli) {
        Notice cc = cli;
        cc.setCreator(sessionService.get().getName());
        cc.setUpdator(sessionService.get().getName());
        cc.setEnabled(true);

        noticeDao.save(cc);

        return ViewResult.JSON_SUCCESS;
    }

    @RequestMapping
    @Transactional()
    @Barrier(PurviewConstants.COURSE)
    public String _update(@ObjectParam("course") Notice cli) {
        Notice cc = cli;
        cc.setUpdator(sessionService.get().getName());

        noticeDao.update(cc);

        return ViewResult.JSON_SUCCESS;
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

        Map<String, Object> m = MAPPING_FULL.toMap(u);

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
        return JsonResult.s(MAPPING_FULL.toMap(u));
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
