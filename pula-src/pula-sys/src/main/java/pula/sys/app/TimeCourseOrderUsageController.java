/**
 * 
 */
package pula.sys.app;

import java.text.MessageFormat;
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
import pula.sys.conditions.TimeCourseOrderUsageCondition;
import pula.sys.daos.StudentDao;
import pula.sys.daos.TimeCourseDao;
import pula.sys.daos.TimeCourseOrderDao;
import pula.sys.daos.TimeCourseUsageDao;
import pula.sys.domains.TimeCourseOrder;
import pula.sys.domains.TimeCourseOrderUsage;
import pula.sys.services.SessionUserService;

/**
 * @author Liangfei
 *
 */
@Controller
public class TimeCourseOrderUsageController {

    @SuppressWarnings("unused")
    private static final Logger logger = Logger.getLogger(TimeCourseOrderUsage.class);
    private static final YuiResultMapper<TimeCourseOrderUsage> MAPPING = new YuiResultMapper<TimeCourseOrderUsage>() {
        @Override
        public Map<String, Object> toMap(TimeCourseOrderUsage obj) {

            Map<String, Object> m = WxlSugar.newHashMap();
            m.put("no", obj.getNo());
            m.put("id", obj.getId());
            m.put("createTime", obj.getCreateTime());
            m.put("updateTime", obj.getUpdateTime());
            m.put("enabled", obj.isEnabled());
            m.put("comments", obj.getComments());
            m.put("courseNo", obj.getCourseNo());
            m.put("creator", obj.getCreator());
            m.put("updator", obj.getUpdator());
            m.put("studentNo", obj.getStudentNo());
            m.put("usedCost", obj.getUsedCost());
            m.put("usedCount", obj.getUsedCount());

            return m;
        }
    };
    private static final YuiResultMapper<TimeCourseOrderUsage> MAPPING_FULL = new YuiResultMapper<TimeCourseOrderUsage>() {
        @Override
        public Map<String, Object> toMap(TimeCourseOrderUsage obj) {

            Map<String, Object> m = MAPPING.toMap(obj);
            return m;
        }
    };

    @Resource
    private TimeCourseDao courseDao;
    @Resource
    private StudentDao studentDao;
    @Resource
    private TimeCourseOrderDao orderDao;
    @Resource
    private TimeCourseUsageDao usageDao;
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
    public ModelAndView entry(@ObjectParam("condition") CourseCondition condition) {
        if (condition == null) {
            condition = new CourseCondition();
        }

        return new ModelAndView().addObject("condition", condition);
    }

    @RequestMapping
    @Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
    @ResponseBody
    @Barrier(PurviewConstants.COURSE)
    public YuiResult list(@ObjectParam("condition") TimeCourseOrderUsageCondition condition,
            @RequestParam(value = "pageIndex", required = false, defaultValue = "0") int pageIndex) {
        if (condition == null) {
            condition = new TimeCourseOrderUsageCondition();
        }

        // setup search condition
//        if (condition.getCourseId() == 0 && !StringUtils.isEmpty(condition.getCourseName())) {
//            Long cid = courseDao.getIdByName(condition.getCourseName());
//            if (cid != null) {
//                condition.setCourseId(cid);
//            }
//        }
//        if (condition.getStudentId() > 0 && !StringUtils.isEmpty(condition.getStudentName())) {
//            Long sid = studentDao.getIdByName(condition.getStudentName());
//            if (sid != null) {
//                condition.setStudentId(sid);
//            }
//        }

        PaginationSupport<TimeCourseOrderUsage> results = usageDao.search(condition, pageIndex);
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
    public String _create(@ObjectParam("course") TimeCourseOrderUsage cli) {
        // pre - check
        preCheckExisting(cli);

        // cost check
        TimeCourseOrder order = orderCheck(cli);

        // update usage
        TimeCourseOrderUsage cc = cli;
        cc.setCreator(sessionService.getActorId());
        cc.setUpdator(sessionService.getActorId());
        cc.setEnabled(true);
        usageDao.save(cc);

        // update order
        orderDao.update(order);

        return ViewResult.JSON_SUCCESS;
    }

    private TimeCourseOrder orderCheck(TimeCourseOrderUsage cli) {
        String orderNo  = cli.getOrderNo();
        TimeCourseOrder order = orderDao.findByNo(orderNo);
        order.setUpdator(sessionService.getActorId());
        int remainCost = order.getRemainCost();
        int remainCount = order.getRemainCount();
        if (order.getBuyType() == 0) {
            remainCost = remainCost - cli.getUsedCost();
            if (remainCost < 0) {
                Pe.raise(MessageFormat.format("订单号{0}余额已经不足，剩余{1}，需扣费用{1}！", orderNo, order.getRemainCost(),
                        cli.getUsedCost()));
            }
            order.setRemainCost(remainCost);
        } else {
            remainCount = remainCount - cli.getUsedCount();
            if (remainCount < 0) {
                Pe.raise(MessageFormat.format("订单号{0}剩余上课次数已经不足，剩余次数{1}，需扣次数{1}！", orderNo, order.getRemainCount(),
                        cli.getUsedCount()));
            }
            order.setRemainCount(remainCount);
        }
        return order;
    }

    private void preCheckExisting(TimeCourseOrderUsage cli) {
        String courseNo = cli.getCourseNo();
        if (courseNo == null || courseDao.findByNo(courseNo) == null) {
            Pe.raise(MessageFormat.format("课程{0}不存在！请填写正确的课程id！", courseNo));
        }

        String studentNo = cli.getStudentNo();
        if (studentNo == null || studentDao.findByNo(studentNo) == null) {
            Pe.raise(MessageFormat.format("学生{0}不存在！请填写正确的学生id！", studentNo));
        }
        String orderNo = cli.getOrderNo();
        if (orderNo == null || orderDao.findByNo(orderNo) == null) {
            Pe.raise(MessageFormat.format("订单号{0}不存在！请填写正确的订单号！", orderNo));
        }
    }

    @RequestMapping
    @Transactional()
    @Barrier(PurviewConstants.COURSE)
    public String _update(@ObjectParam("course") TimeCourseOrderUsage cli) {
        TimeCourseOrderUsage existing = usageDao.findById(cli.getId());
        if (existing == null) {
            Pe.raise(MessageFormat.format("消费记录{0}不存在!", cli.getId()));
        }

        // pre-check
        preCheckExisting(cli);
        
        // order-check
        TimeCourseOrder order = orderDao.findByNo(cli.getOrderNo());
        order.setUpdator(sessionService.getActorId());
        
        if (order.getBuyType() == 0) {
            int deltaCost = cli.getUsedCost() - existing.getUsedCost();
            int newCost = order.getRemainCost() - deltaCost;
            if (newCost < 0) {
                Pe.raise(MessageFormat.format("订单号{0}余额已经不足，剩余{1}，需扣费用{1}！", order.getId(), order.getRemainCost(),
                        deltaCost));
            }
            order.setRemainCost(newCost);
        } else {
            int deltaCount = cli.getUsedCount() - existing.getUsedCount();
            int newCount = order.getRemainCount() - deltaCount;
            if (newCount < 0) {
                Pe.raise(MessageFormat.format("订单号{0}剩余上课次数已经不足，剩余次数{1}，需扣次数{1}！", order.getId(), order.getRemainCount(),
                        deltaCount));
            }
            order.setRemainCount(newCount);
        }

        TimeCourseOrderUsage cc = cli;
        cc.setUpdator(sessionService.getActorId());
        usageDao.update(cc);

        // update order
        orderDao.update(order);

        return ViewResult.JSON_SUCCESS;
    }

    @Transactional
    @RequestMapping
    @Barrier(PurviewConstants.COURSE)
    public String remove(@RequestParam(value = "objId", required = false) Long[] id) {
        orderDao.deleteById(id);
        return ViewResult.JSON_SUCCESS;
    }

    @RequestMapping
    @Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
    @ResponseBody
    @Barrier()
    public JsonResult get(@RequestParam("id") Long id) {
        TimeCourseOrderUsage u = usageDao.findById(id);

        Map<String, Object> m = MAPPING_FULL.toMap(u);

        return JsonResult.s(m);
    }

    @RequestMapping
    @Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
    @ResponseBody
    @Barrier()
    public JsonResult getByNo(@RequestParam("no") String no) {
        TimeCourseOrderUsage u = usageDao.findByNo(no);
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
        usageDao.doEnable(id, enable);
        return ViewResult.JSON_SUCCESS;
    }
}
