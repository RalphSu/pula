/**
 * 
 */
package pula.sys.app;

import java.text.MessageFormat;
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
import puerta.system.dao.LoggerDao;
import puerta.system.keeper.ParameterKeeper;
import puerta.system.service.SessionService;
import puerta.system.vo.JsonResult;
import puerta.system.vo.YuiResult;
import puerta.system.vo.YuiResultMapper;
import pula.sys.PurviewConstants;
import pula.sys.conditions.CourseCondition;
import pula.sys.conditions.TimeCourseOrderCondition;
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
            m.put("orderNo", obj.getOrderNo());
            m.put("creator", obj.getCreator());
            m.put("updator", obj.getUpdator());
            m.put("studentNo", obj.getStudentNo());
            m.put("usedCost", obj.getUsedCost());
            m.put("usedCount", obj.getUsedCount());
            m.put("usedGongfangCount", obj.getUsedGongfangCount());
            m.put("usedHuodongCount", obj.getUsedHuodongCount());

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

        PaginationSupport<TimeCourseOrderUsage> results = usageDao.search(condition, pageIndex);
        return YuiResult.create(results, MAPPING);
    }

    @RequestMapping
    @Barrier(PurviewConstants.COURSE)
    public String create() {
        return null;
    }

    @RequestMapping
    @Transactional(isolation = Isolation.READ_COMMITTED)
    @Barrier(PurviewConstants.COURSE)
    public String _create(@ObjectParam("course") TimeCourseOrderUsage cli) {
        // pre - check
        preCheckExisting(cli);

        // cost check
        TimeCourseOrder order = orderCheck(cli);
        usageCreateCountCheck(cli, order.getNo(), order);
        
        // update usage
        TimeCourseOrderUsage cc = cli;
        cc.setCreator(sessionService.get().getName());
        cc.setUpdator(sessionService.get().getName());
        cc.setEnabled(true);
        usageDao.save(cc);

        // update order
        orderDao.update(order);

        return ViewResult.JSON_SUCCESS;
    }

    private TimeCourseOrder orderCheck(TimeCourseOrderUsage cli) {
        String orderNo  = cli.getOrderNo();
        
        TimeCourseOrder order = null;
        if (StringUtils.isEmpty(orderNo)) {
            TimeCourseOrderCondition condition = new TimeCourseOrderCondition();
            condition.setStudentNo(cli.getStudentNo());
            List<TimeCourseOrder> orders = orderDao.search(condition, 0).getItems();
            if (orders.size() > 1) {
                order = orders.get(0);// update order
            }
        } else {
            order = orderDao.findByNo(orderNo);
        }
        // order 必须存在
        if (order == null) {
            Pe.raise(MessageFormat.format("找不到用户的订单，请确认用户有支付的订单存在: {0}!", orderNo));
        }
        orderNo = order.getNo();
        order.setUpdator(sessionService.get().getName());

        return order;
    }

    private void usageCreateCountCheck(TimeCourseOrderUsage cli, String orderNo, TimeCourseOrder order) {
        int usedCount = cli.getUsedCount();
        if (order.getUsedCount() + usedCount > order.getPaiedCount()) {
            Pe.raise(MessageFormat.format("订单号{0}剩余上课次数已经不足，买了次数{1}，已使用次数{1}，本次需扣除次数{2}！", orderNo,
                    order.getPaiedCount(), order.getUsedCount(), usedCount));

        }
        order.setUsedCount(order.getUsedCost() + usedCount);

        int usedGongfangCount = cli.getUsedGongfangCount();
        if (order.getUsedGongFangCount() + usedGongfangCount > order.getGongfangCount()) {
            Pe.raise(MessageFormat.format("订单号{0}剩余工坊上课次数已经不足，买了次数{1}，已使用次数{1}，本次需扣除次数{2}！", orderNo,
                    order.getGongfangCount(), order.getUsedGongFangCount(), usedGongfangCount));

        }
        order.setUsedGongFangCount(order.getUsedGongFangCount() + usedGongfangCount);

        int usedHuodongCount = cli.getUsedHuodongCount();
        if (order.getUsedHuodongCount() + usedHuodongCount > order.getHuodongCount()) {
            Pe.raise(MessageFormat.format("订单号{0}剩余活动上课次数已经不足，买了次数{1}，已使用次数{1}，本次需扣除次数{2}！", orderNo,
                    order.getHuodongCount(), order.getUsedHuodongCount(), usedHuodongCount));
        }
        order.setUsedHuodongCount(order.getUsedHuodongCount() + usedHuodongCount);
    }

    private void preCheckExisting(TimeCourseOrderUsage cli) {
        String courseNo = cli.getCourseNo();
        if (!StringUtils.isEmpty(courseNo) && courseDao.findByNo(courseNo) == null) {
            Pe.raise(MessageFormat.format("课程{0}不存在！请填写正确的课程id！", courseNo));
        }

        String studentNo = cli.getStudentNo();
        if (studentNo == null || studentDao.findByNo(studentNo) == null) {
            Pe.raise(MessageFormat.format("学生{0}不存在！请填写正确的学生id！", studentNo));
        }
        String orderNo = cli.getOrderNo();
        if (!StringUtils.isEmpty(orderNo) &&  orderDao.findByNo(orderNo) == null) {
            Pe.raise(MessageFormat.format("订单号{0}不存在！请填写正确的订单号！", orderNo));
        }
    }

    @RequestMapping
    @Transactional(isolation = Isolation.READ_COMMITTED)
    @Barrier(PurviewConstants.COURSE)
    public String _update(@ObjectParam("course") TimeCourseOrderUsage cli) {
        TimeCourseOrderUsage existing = usageDao.findById(cli.getId());
        if (existing == null) {
            Pe.raise(MessageFormat.format("消费记录{0}不存在!", cli.getId()));
        }

        // pre-check
        preCheckExisting(cli);
        
        if (StringUtils.isEmpty(cli.getOrderNo())) {
            Pe.raise("更新时必须要有订单号！");
        }
        // order-check
        TimeCourseOrder order = orderCheck(cli);
        usageUpdateCountCheck(cli, existing, order);

        TimeCourseOrderUsage cc = cli;
        cc.setUpdator(sessionService.get().getName());
        usageDao.update(cc);

        // update order
        orderDao.update(order);

        return ViewResult.JSON_SUCCESS;
    }

    private void usageUpdateCountCheck(TimeCourseOrderUsage cli, TimeCourseOrderUsage existing, TimeCourseOrder order) {
        {
            int deltaCount = cli.getUsedCount() - existing.getUsedCount();
            int newUsedCount = order.getUsedCount() + deltaCount;
            if (newUsedCount > order.getPaiedCount()) {
                Pe.raise(MessageFormat.format("订单号{0}剩余上课次数已经不足，购买次数{1}，更新前已使用次数{2},本次尝试新设置消费次数{3}！", 
                        order.getNo(), 
                        order.getPaiedCount(),
                        order.getUsedCount(),
                        deltaCount));
            }
            order.setUsedCount(newUsedCount);
        }
        {   
            int deltaGongfangCount = cli.getUsedGongfangCount() - existing.getUsedGongfangCount();
            int newUsedGongfangCount = order.getUsedGongFangCount() + deltaGongfangCount;
            if (newUsedGongfangCount > order.getGongfangCount()) {
                Pe.raise(MessageFormat.format("订单号{0}剩余''工坊''上课次数已经不足，购买次数{1}，更新前已使用次数{2},本次尝试新设置消费次数{3}！", 
                        order.getNo(), 
                        order.getGongfangCount(),
                        order.getUsedGongFangCount(),
                        deltaGongfangCount));
            }
            order.setUsedGongFangCount(newUsedGongfangCount);
        }
        {   
            int deltaHuodongCount = cli.getUsedHuodongCount() - existing.getUsedHuodongCount();
            int newUsedHuodongCount = order.getUsedHuodongCount() + deltaHuodongCount;
            if (newUsedHuodongCount > order.getHuodongCount()) {
                Pe.raise(MessageFormat.format("订单号{0}剩余''活动''上课次数已经不足，购买次数{1}，更新前已使用次数{2},本次尝试新设置消费次数{3}！", 
                        order.getNo(), 
                        order.getHuodongCount(),
                        order.getUsedHuodongCount(),
                        deltaHuodongCount));
            }
            order.setUsedHuodongCount(newUsedHuodongCount);
        }
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
