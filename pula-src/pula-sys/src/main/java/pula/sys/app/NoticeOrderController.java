/**
 * 
 */
package pula.sys.app;

import java.text.MessageFormat;
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
import puerta.system.vo.JsonResult;
import puerta.system.vo.YuiResult;
import puerta.system.vo.YuiResultMapper;
import pula.sys.PurviewConstants;
import pula.sys.conditions.NoticeOrderCondition;
import pula.sys.daos.NoticeOrderDao;
import pula.sys.domains.NoticeOrder;
import pula.sys.forms.NoticeOrderForm;

@Controller
@Barrier(ignore = true)
public class NoticeOrderController {

    @SuppressWarnings("unused")
    private static final Logger logger = Logger.getLogger(NoticeOrder.class);

    private final YuiResultMapper<NoticeOrder> MAPPING = new YuiResultMapper<NoticeOrder>() {
        @Override
        public Map<String, Object> toMap(NoticeOrder obj) {

            Map<String, Object> m = WxlSugar.newHashMap();
            m.put("no", obj.getNo());
            m.put("id", obj.getId());
            m.put("createTime", obj.getCreateTime());
            m.put("updateTime", obj.getUpdateTime());
            m.put("enabled", obj.isEnabled());
            m.put("comments", obj.getComments());
            m.put("studentNo", obj.getStudentNo());
            m.put("paied", obj.getPaied());
            //
            m.put("studentId", obj.getStudentId());
            m.put("noticeId", obj.getNoticeId());
            m.put("noticeName", obj.getNoticeName());
            m.put("noticeNo", obj.getNoticeNo());
            m.put("noticePrice", obj.getNoticePrice());
            m.put("orderPayStatus", obj.getOrderPayStatus());
            m.put("count", obj.getCount());
            //
            m.put("paied", obj.getPaied());
            m.put("prepayId", obj.getPrepayId());
            m.put("accessToken", obj.getAccessToken());
            m.put("wxOrderId", obj.getWxOrderId());
            m.put("wxPayStatus", obj.getWxPayStatus());
            m.put("wxOrderDetail", obj.getWxOrderDetail());
            return m;
        }
    };

    @Resource
    NoticeOrderDao noticeOrderDao;

    @RequestMapping
    @Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
    @Barrier(PurviewConstants.COURSE)
    public String create() {
        return null;
    }
    
    @RequestMapping
    @Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
    @Barrier(PurviewConstants.COURSE)
    public ModelAndView entry(@ObjectParam("condition") NoticeOrderCondition condition) {
        if (condition == null) {
            condition = new NoticeOrderCondition();
        }

        return new ModelAndView().addObject("condition", condition);
    }

    

    @RequestMapping
    @Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
    @ResponseBody
    @Barrier(PurviewConstants.COURSE)
    public YuiResult list(@ObjectParam("condition") NoticeOrderCondition condition,
            @RequestParam(value = "pageIndex", required = false, defaultValue = "0") int pageIndex) {
        if (condition == null) {
            condition = new NoticeOrderCondition();
        }
        PaginationSupport<NoticeOrder> results = noticeOrderDao.search(condition, pageIndex);
        return YuiResult.create(results, MAPPING);
    }

    @RequestMapping
    @Transactional()
    @Barrier(PurviewConstants.COURSE)
    public String _update(@ObjectParam("course") NoticeOrderForm cli) {
        if (StringUtils.isEmpty(cli.getStudentNo()) || noticeOrderDao.findByNo(cli.getStudentNo()) == null) {
            Pe.raise(MessageFormat.format("找不到学生编号{0}", cli.getStudentNo()));
        }

        NoticeOrder cc = cli;
        noticeOrderDao.update(cc);

        return ViewResult.JSON_SUCCESS;
    }
    
    @RequestMapping
    @Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
    @ResponseBody
    @Barrier()
    public JsonResult get(@RequestParam("id") Long id) {
        NoticeOrder u = noticeOrderDao.findById(id);

        Map<String, Object> m = MAPPING.toMap(u);

        return JsonResult.s(m);
    }


    @RequestMapping
    @Transactional()
    @Barrier(PurviewConstants.COURSE)
    @ResponseBody
    public String prepay() {
        return "";
    }

    @Transactional
    @RequestMapping
    @Barrier(PurviewConstants.COURSE)
    public String remove(@RequestParam(value = "objId", required = false) Long[] id) {
        noticeOrderDao.deleteById(id);
        return ViewResult.JSON_SUCCESS;
    }

//    public String submit() {
//
//    }

}
