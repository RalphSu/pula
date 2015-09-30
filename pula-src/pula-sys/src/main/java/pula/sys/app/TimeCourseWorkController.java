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
import org.springframework.web.servlet.ModelAndView;

import puerta.support.PaginationSupport;
import puerta.support.annotation.Barrier;
import puerta.support.annotation.ObjectParam;
import puerta.support.utils.WxlSugar;
import puerta.system.vo.JsonResult;
import puerta.system.vo.YuiResult;
import puerta.system.vo.YuiResultMapper;
import pula.sys.PurviewConstants;
import pula.sys.conditions.TimeCourseWorkCondition;
import pula.sys.daos.TimeCourseWorkDao;
import pula.sys.domains.TimeCourseOrderUsage;
import pula.sys.domains.TimeCourseWork;
import pula.sys.forms.TimeCourseWorkFormLine;

/**
 * @author Liangfei
 *
 */
@Controller
public class TimeCourseWorkController {

    @SuppressWarnings("unused")
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
            m.put("rate", obj.getRate());
            return m;
        }
    };

    @Resource
    TimeCourseWorkDao workDao;

    @RequestMapping
    @Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
    @Barrier(PurviewConstants.COURSE_WORK)
    public ModelAndView entry(@ObjectParam("condition") TimeCourseWorkCondition condition) {
        if (condition == null) {
            condition = new TimeCourseWorkCondition();
        }

        return new ModelAndView().addObject("condition", condition);
    }

    @RequestMapping
    @Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
    @Barrier(PurviewConstants.COURSE_WORK)
    public YuiResult listCourseWork(@ObjectParam("condition") TimeCourseWorkCondition condition,
            @RequestParam(value = "pageIndex", required = false, defaultValue = "0") int pageIndex) {

        PaginationSupport<TimeCourseWork> works = workDao.search(condition, 0);

        return YuiResult.create(works, MAPPING);
    }

    @RequestMapping
    public JsonResult appshowListCourseWork(String courseNo, boolean starOnly) {
        return null;
    }

    @RequestMapping
    public JsonResult upload(TimeCourseWorkFormLine form) {
        return null;
    }

}
