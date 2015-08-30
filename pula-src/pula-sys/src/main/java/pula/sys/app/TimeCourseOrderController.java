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
import puerta.system.dao.LoggerDao;
import puerta.system.keeper.ParameterKeeper;
import puerta.system.service.SessionService;
import puerta.system.vo.JsonResult;
import puerta.system.vo.YuiResult;
import puerta.system.vo.YuiResultMapper;
import pula.sys.PurviewConstants;
import pula.sys.conditions.TimeCourseOrderCondition;
import pula.sys.daos.StudentDao;
import pula.sys.daos.TimeCourseDao;
import pula.sys.daos.TimeCourseOrderDao;
import pula.sys.domains.TimeCourseOrder;
import pula.sys.services.SessionUserService;

/**
 * @author Liangfei
 *
 */
@Controller
public class TimeCourseOrderController {

    @SuppressWarnings("unused")
    private static final Logger logger = Logger.getLogger(TimeCourseOrder.class);
    private final YuiResultMapper<TimeCourseOrder> MAPPING = new YuiResultMapper<TimeCourseOrder>() {
        @Override
        public Map<String, Object> toMap(TimeCourseOrder obj) {

            Map<String, Object> m = WxlSugar.newHashMap();
            m.put("no", obj.getNo());
            m.put("id", obj.getId());
            m.put("buyType", obj.getBuyType());
            m.put("createTime", obj.getCreateTime());
            m.put("updateTime", obj.getUpdateTime());
            m.put("enabled", obj.isEnabled());
            m.put("comments", obj.getComments());
            m.put("courseNo", obj.getCourseNo());
            m.put("creator", obj.getCreator());
            m.put("updator", obj.getUpdator());
            m.put("studentNo", obj.getStudentNo());
            m.put("paied", obj.getPaied());
            m.put("paiedCount", obj.getPaiedCount());
            m.put("usedCost", obj.getUsedCost());
            m.put("usedCount", obj.getUsedCount());
            m.put("gongfangCount", obj.getGongfangCount());
            m.put("usedGongFangCount", obj.getUsedGongFangCount());
            m.put("huodongCount", obj.getHuodongCount());
            m.put("usedHuodongCount", obj.getUsedHuodongCount());
            m.put("orderStatus", obj.getOrderStatus());
            return m;
        }
    };
    private final YuiResultMapper<TimeCourseOrder> MAPPING_FULL = new YuiResultMapper<TimeCourseOrder>() {
        @Override
        public Map<String, Object> toMap(TimeCourseOrder obj) {

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
    public ModelAndView entry(@ObjectParam("condition") TimeCourseOrderCondition condition) {
        if (condition == null) {
            condition = new TimeCourseOrderCondition();
        }

        return new ModelAndView().addObject("condition", condition);
    }

    @RequestMapping
    @Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
    @ResponseBody
    @Barrier(PurviewConstants.COURSE)
    public YuiResult list(@ObjectParam("condition") TimeCourseOrderCondition condition,
            @RequestParam(value = "pageIndex", required = false, defaultValue = "0") int pageIndex) {
        if (condition == null) {
            condition = new TimeCourseOrderCondition();
        }

        PaginationSupport<TimeCourseOrder> results = orderDao.search(condition, pageIndex);
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
    public String _create(@ObjectParam("course") TimeCourseOrder cli) {
        if (StringUtils.isEmpty(cli.getStudentNo()) || studentDao.findByNo(cli.getStudentNo()) == null) {
            Pe.raise(MessageFormat.format("找不到学生编号{0}", cli.getStudentNo()));
        }

        TimeCourseOrder cc = cli;
        cc.setCreator(sessionService.get().getName());
        cc.setUpdator(sessionService.get().getName());
        cc.setEnabled(true);

        orderDao.save(cc);

        return ViewResult.JSON_SUCCESS;
    }

    @RequestMapping
    @Transactional()
    @Barrier(PurviewConstants.COURSE)
    public String _update(@ObjectParam("course") TimeCourseOrder cli) {
        if (StringUtils.isEmpty(cli.getStudentNo()) || studentDao.findByNo(cli.getStudentNo()) == null) {
            Pe.raise(MessageFormat.format("找不到学生编号{0}", cli.getStudentNo()));
        }

        TimeCourseOrder cc = cli;
        cc.setUpdator(sessionService.get().getName());

        orderDao.update(cc);

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
        TimeCourseOrder u = orderDao.findById(id);

        Map<String, Object> m = MAPPING_FULL.toMap(u);

        return JsonResult.s(m);
    }

    @RequestMapping
    @Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
    @ResponseBody
    @Barrier()
    public JsonResult getByNo(@RequestParam("no") String no) {
        TimeCourseOrder u = orderDao.findByNo(no);
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
        orderDao.doEnable(id, enable);
        return ViewResult.JSON_SUCCESS;
    }
}
