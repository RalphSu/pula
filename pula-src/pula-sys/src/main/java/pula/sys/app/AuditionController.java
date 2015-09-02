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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import puerta.PuertaWeb;
import puerta.support.PaginationSupport;
import puerta.support.Pe;
import puerta.support.annotation.Barrier;
import puerta.support.annotation.ObjectParam;
import puerta.support.utils.DateHelper;
import puerta.support.utils.JacksonUtil;
import puerta.support.vo.SelectOptionList;
import puerta.system.vo.JsonResult;
import puerta.system.vo.MapBean;
import puerta.system.vo.MapList;
import puerta.system.vo.YuiResult;
import puerta.system.vo.YuiResultMapper;
import pula.sys.BhzqConstants;
import pula.sys.PurviewConstants;
import pula.sys.conditions.AuditionCondition;
import pula.sys.daos.AuditionDao;
import pula.sys.daos.BranchDao;
import pula.sys.daos.SysCategoryDao;
import pula.sys.domains.Audition;
import pula.sys.domains.Branch;
import pula.sys.domains.SysCategory;
import pula.sys.forms.AuditionForm;
import pula.sys.services.SessionUserService;
import pula.sys.util.SmsUtil;
import pula.sys.util.SmsUtil.SendResult;

@Controller
public class AuditionController {
	private static final Logger logger = Logger
			.getLogger(AuditionController.class);
	/*
	 * @RequestMapping
	 * 
	 * @Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	 * public ModelAndView my(HttpServletResponse res) {
	 * 
	 * try { res.sendRedirect("/pula-sys/html/audition.html");
	 * 
	 * } catch (IOException e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); }
	 * 
	 * return null; }
	 */

	private static final YuiResultMapper<Audition> MAPPING = new YuiResultMapper<Audition>() {

		@Override
		public Map<String, Object> toMap(Audition obj) {
			MapBean mb = MapBean.map("id", obj.getId())
					.add("age", obj.getAge()).add("student", obj.getStudent())
					.add("phone", obj.getPhone())
					.add("parent", obj.getParent())
					.add("content", obj.getContent())
					.add("plan1", obj.getPlan1()).add("plan2", obj.getPlan2())
					.add("plan3", obj.getPlan3()).add("plan4", obj.getPlan4())
					.add("plan5", obj.getPlan5())
					.add("comments", obj.getComments());

			if (obj.getResult() != null) {
				mb.add("resultId", obj.getResult().getId());
			}

			return mb;
		}
	};

	private static final YuiResultMapper<Audition> MAPPING_LIST = new YuiResultMapper<Audition>() {

		@Override
		public Map<String, Object> toMap(Audition obj) {
			Map<String, Object> m = MAPPING.toMap(obj);

			if (obj.getResult() != null) {
				m.put("resultName", obj.getResult().getName());
			} else {
				m.put("resultName", "尚未结束");
			}
            m.put("branchNo", obj.getBranch().getNo());
			m.put("ownerName", obj.getOwner().getName());
			m.put("branchName", obj.getBranch().getName());
			m.put("createdTime",obj.getCreatedTime());
			return m;
		}
	};

	@Resource
	AuditionDao auditionDao;
	@Resource
	SessionUserService sessionUserService;
	@Resource
	BranchDao branchDao;
	@Resource
	SysCategoryDao sysCategoryDao;

	@RequestMapping
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	@Barrier(PurviewConstants.AUDITION_SIGNIN)
	public ModelAndView my() {

		// load my

		List<Audition> auditions = auditionDao.loadMy(sessionUserService
				.getActorId());

		boolean more = false;
		if (auditions.size() > 100) {
			auditions = auditions.subList(0, 99);
			more = true;
		}

		List<SysCategory> cates = sysCategoryDao
				.getUnder(BhzqConstants.SC_AUDITION_RESULT);

		List<Map<String, Object>> rets = JsonResult.l(auditions, MAPPING);

		return new ModelAndView().addObject("results", auditions)
				.addObject("items_json", JacksonUtil.toString(rets))
				.addObject("more", more).addObject("categories", cates);
	}

	@RequestMapping
	@Transactional
	@ResponseBody
	@Barrier(PurviewConstants.AUDITION_SIGNIN)
	public JsonResult _update(@RequestParam("json") String json) {
		List<AuditionForm> items = prepareData(json);

		// 更新自己未关闭的

		Map<Long, Long> myIds = auditionDao.loadMyIds(sessionUserService
				.getActorId());

		Branch branch = Branch.create(sessionUserService.getBranch()
				.getIdLong());

		for (AuditionForm item : items) {

			if (item.isEmpty()) {
				continue;
			}

			item.setBranch(branch);
			auditionDao.update(item.toAudition(),
					sessionUserService.getActorId());

			if (myIds.containsKey(item.getId())) {
				myIds.remove(item.getId());
			}

		}

		// myIds left to remove
		if (myIds.size() != 0)
			auditionDao.remove(myIds.values());

		return JsonResult.s();

	}

	private List<AuditionForm> prepareData(String json) {
		List<AuditionForm> items = null;
		try {
			items = JacksonUtil.getList(json, AuditionForm.class);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("jsonDetail=" + json);
			Pe.raise(e.getMessage());
		}

		return items;
	}

	// 查询用的
	@RequestMapping
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	@Barrier(PurviewConstants.AUDITION)
	public ModelAndView entry(AuditionCondition condition) {
		if (condition == null) {
			condition = new AuditionCondition();
			condition.setBeginDate(DateHelper.getThisMonthBegin());
			condition.setEndDate(DateHelper.getThisMonthEnd());
		}

		MapList branchList = branchDao.loadMeta();

		SelectOptionList statusList = PuertaWeb.getYesNoList(0, new String[] {
				"关闭", "开启" });

		List<SysCategory> cates = sysCategoryDao
				.getUnder(BhzqConstants.SC_AUDITION_RESULT);

		return new ModelAndView().addObject("condition", condition)
				.addObject("categories", cates)
				.addObject("branches", branchList)
				.addObject("headquarter", sessionUserService.isHeadquarter())
				// .addObject("levels", levelList)
				.addObject("statusList", statusList);
	}

	@RequestMapping(method={RequestMethod.POST, RequestMethod.GET})
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	@ResponseBody
	@Barrier(PurviewConstants.AUDITION)
	public YuiResult list(
			@ObjectParam("condition") AuditionCondition condition,
			@RequestParam(value = "pageIndex", required = false, defaultValue = "0") int pageIndex) {
		if (condition == null) {
			condition = new AuditionCondition();
		}
		PaginationSupport<Audition> results = null;
		results = auditionDao.search(condition, pageIndex);
		return YuiResult.create(results, MAPPING_LIST);
    }

    @RequestMapping
    @Transactional(isolation = Isolation.READ_COMMITTED)
    @ResponseBody
    @Barrier(ignore = true)
    public JsonResult create(@ObjectParam("audition") Audition audition) {
        if (StringUtils.isEmpty(audition.getPhone())) {
            return JsonResult.e("电话是必填的！");
        }

        Branch b = branchDao.findByNo(audition.getBranchNo());
        if (b == null) {
            List<Branch> branches = branchDao.findByProperty("name", audition.getBranchName());
            if (branches.isEmpty()) {
                return JsonResult.e("未找到指定的分支结构: no : " + audition.getBranchNo() + " name : "
                        + audition.getBranchName());
            } else {
                b = branches.get(0);
            }
        }
        audition.setBranch(b);
        audition.setBranchNo(b.getNo());
        Long id = auditionDao._save(audition);
        return JsonResult.create(MessageFormat.format("预订已提交，Id:{0}", id), null);
    }

    @RequestMapping(method = RequestMethod.POST)
    @Transactional(isolation = Isolation.READ_COMMITTED)
    @ResponseBody
    @Barrier(ignore = true)
    public JsonResult update(@ObjectParam("audition") Audition audition) {
        Audition existAudition = auditionDao.findById(audition.getId());
        if (existAudition == null) {
            return JsonResult.e(MessageFormat.format("找不到已有的预约Id:{0}！", audition.getId()));
        }
        existAudition = new Audition(existAudition);

        try {
            Branch b = branchDao.findByNo(audition.getBranchNo());
            if (b == null) {
                List<Branch> branches = branchDao.findByProperty("name", audition.getBranchName());
                if (branches.isEmpty()) {
                    return JsonResult.e("未找到指定的分支结构: no : " + audition.getBranchNo() + " name : "
                            + audition.getBranchName());
                } else {
                    b = branches.get(0);
                }
            }
            audition.setBranch(b);
            auditionDao.update(audition, sessionUserService.getActorId());
            // SMS when changed the time
            Audition newAudition = auditionDao.findById(audition.getId());
            if (!existAudition.getResult().getName().equals("尚未结束") && newAudition.getResult().getName().equals("成功")
                    || (!existAudition.getPlan1().equals(newAudition.getPlan1()))) {
                // 从尚未结束变成成功，或者时间变化，发个短信
                SendResult result = SmsUtil.SendBookingMessage(audition);
                if (result.succeed) {
                    logger.info("发送短信成功!");
                } else {
                    logger.error("发送短信失败! :: " + result.message);
                }
            }
            return JsonResult.create("预约已更新!", null);
        } catch (Exception e) {
            return JsonResult.e("预约修改为成功，报错了！", e);
        }
    }

    @RequestMapping(method = RequestMethod.POST)
    @Transactional(isolation = Isolation.READ_COMMITTED)
    @ResponseBody
    @Barrier(ignore = true)
    public JsonResult cancel(@RequestParam(value = "auditionid", required = false) Long id,
            @RequestParam(value = "auditionid", required = false) String no) {
        if (id != null) {
            auditionDao.deleteById(id);
            return JsonResult.create("预约已取消!", null);
        } else if (!StringUtils.isEmpty(no)) {
            Audition a = auditionDao.findByNo(no);
            if (a != null) {
                auditionDao.deleteById(id);
                return JsonResult.create("预约已取消!", null);
            }
        }
        return JsonResult.create("没找到给定的预约，取消操作无效!", null);
    }

}
