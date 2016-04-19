/**
 * 
 */
package pula.sys.app;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
import puerta.system.vo.MapBean;
import puerta.system.vo.YuiResult;
import puerta.system.vo.YuiResultMapper;
import pula.sys.PurviewConstants;
import pula.sys.conditions.NoticeOrderCondition;
import pula.sys.conditions.StudentCondition;
import pula.sys.daos.FileAttachmentDao;
import pula.sys.daos.NoticeDao;
import pula.sys.daos.NoticeOrderDao;
import pula.sys.daos.StudentDao;
import pula.sys.domains.Notice;
import pula.sys.domains.NoticeOrder;
import pula.sys.forms.NoticeOrderForm;
import pula.sys.services.SessionUserService;
import pula.sys.util.FileUtil;

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
    @Resource
    StudentDao studentDao;
    @Resource
    NoticeDao noticeDao;
    @Resource
    SessionUserService sessionUserService;

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

        List<Notice> notices = noticeDao.loadAll();

        StudentCondition studentCondition = new StudentCondition();
        if (!sessionUserService.isHeadquarter()) {
            studentCondition.setBranchId(sessionUserService.getBranch().getIdLong());
        }
        PaginationSupport<MapBean> results = studentDao.search(studentCondition, 0, 500);
        List<ThinStudent> students = new ArrayList<ThinStudent>();
        for (MapBean stu : results.getItems()) {
            ThinStudent ts = new ThinStudent();
            ts.id = stu.asLong("id");
            ts.no = stu.string("no");
            ts.name = stu.string("name");

            students.add(ts);
        }

        return new ModelAndView().addObject("condition", condition).addObject("notices", notices)
                .addObject("students", students);
    }

    public static class ThinStudent {
        public long id;
        public String no;
        public String name;
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
        // add icon
        YuiResult result = YuiResult.create(results, MAPPING);
        result.getRecords().clear();
        for (NoticeOrder u : results.getItems()) {
            Map<String, Object> m = MAPPING.toMap(u);
            Notice notice = noticeDao.findByNo(u.getNoticeNo());
            if (notice != null) {
                // icon
                FileUtil.addIconToJson(fileAttachmentDao, notice, m);

                Map<String, Object> mNotice = NoticeController.MAPPING.toMap(notice);
                // icon again
                FileUtil.addIconToJson(fileAttachmentDao, notice, mNotice);
                m.put("notice", mNotice);

            }
            result.getRecords().add(m);
        }

        return result;
    }

    @RequestMapping
    @Transactional()
    @Barrier(PurviewConstants.COURSE)
    public String _create(@ObjectParam("course") NoticeOrderForm cli) {
        NoticeOrder order = new NoticeOrder(cli);

        if (StringUtils.isEmpty(cli.getStudentNo()) || studentDao.findByNo(cli.getStudentNo()) == null) {
            Pe.raise(MessageFormat.format("找不到学生编号{0}", cli.getStudentNo()));
        }

        order.setCreateTime(new Date());
        order.setUpdateTime(new Date());

        NoticeOrder savedOrder = noticeOrderDao.save(order);

        return ViewResult.JSON_SUCCESS;
    }

    @RequestMapping
    @Transactional()
    @Barrier(PurviewConstants.COURSE)
    public String _update(@ObjectParam("course") NoticeOrderForm cli) {
        if (StringUtils.isEmpty(cli.getStudentNo()) || studentDao.findByNo(cli.getStudentNo()) == null) {
            Pe.raise(MessageFormat.format("找不到学生编号{0}", cli.getStudentNo()));
        }

        NoticeOrder cc = cli;
        noticeOrderDao.update(cc);

        return ViewResult.JSON_SUCCESS;
    }
    
    @Resource
    private FileAttachmentDao fileAttachmentDao;

    @RequestMapping
    @Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
    @ResponseBody
    @Barrier()
    public JsonResult get(@RequestParam("id") Long id) {
        NoticeOrder u = noticeOrderDao.findById(id);

        Map<String, Object> m = MAPPING.toMap(u);

        Notice notice = noticeDao.findByNo(u.getNoticeNo());
        if (notice != null) {

//        YuiResult result = YuiResult.create(results, MAPPING);
//        result.getRecords().clear();
//        for (Notice u : results.getItems()) {
//            Map<String, Object> m = MAPPING.toMap(u);

            FileUtil.addIconToJson(fileAttachmentDao, notice, m);


            Map<String, Object> mNotice = NoticeController.MAPPING.toMap(notice);
            // icon again
            FileUtil.addIconToJson(fileAttachmentDao, notice, mNotice);
            m.put("notice", mNotice);

            
//            result.getRecords().add(m);
//        }
        }

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


    // public String submit() {
    //
    // }


    /**
     * show the contents of a given notice in web page.
     * 
     * @param id
     * @return
     */
    @RequestMapping
    public ModelAndView appshow(@RequestParam(value = "id", required = false) Long id,
            @RequestParam(value = "no", required = false) String no) {
        NoticeOrder u = null;
        if (id != null) {
            u = noticeOrderDao.findById(id);
        } else if (!StringUtils.isAlpha(no)) {
            u = noticeOrderDao.findByNo(no);
        }

        ModelAndView view = new ModelAndView();
        if (u == null) {
            view.setViewName("error");
            Exception e = new Exception(String.format("找不到指定的Order:！" + id + no));
            view.addObject("exception", e);
            return view;
        }

        Notice notice = noticeDao.findByNo(u.getNoticeNo());

        return view.addObject("noticeOrder", u).addObject("notice", notice);
    }

}
