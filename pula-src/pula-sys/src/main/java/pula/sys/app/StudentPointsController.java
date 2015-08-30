package pula.sys.app;

import java.util.Calendar;
import java.util.List;
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
import puerta.support.annotation.Barrier;
import puerta.support.annotation.ObjectParam;
import puerta.support.utils.DateExTool;
import puerta.support.utils.DateHelper;
import puerta.support.utils.WxlSugar;
import puerta.support.vo.Mix;
import puerta.support.vo.SelectOptionList;
import puerta.system.vo.JsonResult;
import puerta.system.vo.MapBean;
import puerta.system.vo.MapList;
import puerta.system.vo.YuiResult;
import puerta.system.vo.YuiResultMapper;
import pula.sys.PurviewConstants;
import pula.sys.conditions.StudentPointsCondition;
import pula.sys.daos.BranchDao;
import pula.sys.daos.GiftDao;
import pula.sys.daos.GiftStockDao;
import pula.sys.daos.GiftStockLogDao;
import pula.sys.daos.StudentDao;
import pula.sys.daos.StudentPointsDao;
import pula.sys.domains.Branch;
import pula.sys.domains.Gift;
import pula.sys.domains.GiftStockLog;
import pula.sys.domains.StudentPoints;
import pula.sys.helpers.GiftHelper;
import pula.sys.helpers.StudentPointsHelper;
import pula.sys.services.SessionUserService;
import pula.sys.services.StudentPointsService;
import pula.sys.vo.ExchangePointsVo;

@Controller
public class StudentPointsController {

	@SuppressWarnings("unused")
	private static final Logger logger = Logger
			.getLogger(StudentPointsController.class);

	public static final String SPLIT = ",";

	public static final String SPLIT_ROW = ";";
	private static final YuiResultMapper<StudentPoints> MAPPING = new YuiResultMapper<StudentPoints>() {
		@Override
		public Map<String, Object> toMap(StudentPoints obj) {
			Map<String, Object> m = WxlSugar.newHashMap();
			m.put("createdTime",
					DateExTool.dateTime2String(obj.getCreatedTime()));
			m.put("id", obj.getId());
			if (obj.getAdmin() != null) {
				m.put("admin", obj.getAdmin().getName());
			}
			m.put("type", obj.getFromName());
			m.put("points", obj.getPoints());
			m.put("ownerName", obj.getOwner().getName());
			m.put("ownerNo", obj.getOwner().getNo());
			m.put("comments", obj.getComments());
			return m;
		}
	};
	@Resource
	private StudentPointsDao studentPointsDao;
	@Resource
	private StudentDao studentDao;
	@Resource
	private StudentPointsService studentPointsService;
	@Resource
	BranchDao branchDao;
	@Resource
	SessionUserService sessionUserService;
	@Resource
	GiftDao giftDao;
	@Resource
	GiftStockDao giftStockDao;
	@Resource
	GiftStockLogDao giftStockLogDao;

	@RequestMapping
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	@Barrier(PurviewConstants.STUDENT_POINTS)
	public ModelAndView entry(
			@ObjectParam("condition") StudentPointsCondition condition) {

		return _entry(condition);
	}

	@RequestMapping
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	@Barrier(PurviewConstants.STUDENT_POINTS_SEARCH)
	public ModelAndView search(
			@ObjectParam("condition") StudentPointsCondition condition) {

		return _entry(condition);
	}

	private ModelAndView _entry(StudentPointsCondition condition) {
		if (condition == null) {
			condition = new StudentPointsCondition();
		}

		SelectOptionList typeList = StudentPointsHelper.getType(0);
		MapList branchList = branchDao.loadMetaWithoutHeadquarter();
		return new ModelAndView().addObject("condition", condition)
				.addObject("typeList", typeList)
				.addObject("branches", branchList)
				.addObject("headquarter", sessionUserService.isHeadquarter());
	}

	@RequestMapping
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	@ResponseBody
	@Barrier(PurviewConstants.STUDENT_POINTS)
	public YuiResult list(
			@ObjectParam("condition") StudentPointsCondition condition,
			@RequestParam(value = "pageIndex", required = false, defaultValue = "0") int pageIndex) {
		if (condition == null) {
			condition = new StudentPointsCondition();
		}
		PaginationSupport<StudentPoints> results = null;
		results = studentPointsDao.search(condition, pageIndex);
		return YuiResult.create(results, MAPPING);
	}

	@RequestMapping
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	@ResponseBody
	@Barrier(PurviewConstants.STUDENT_POINTS_SEARCH)
	public YuiResult list4Search(
			@ObjectParam("condition") StudentPointsCondition condition,
			@RequestParam(value = "pageIndex", required = false, defaultValue = "0") int pageIndex) {
		if (condition == null) {
			condition = new StudentPointsCondition();
		}
		PaginationSupport<StudentPoints> results = null;
		results = studentPointsDao.search(condition, pageIndex);
		return YuiResult.create(results, MAPPING);
	}

	@ResponseBody
	@Transactional
	@RequestMapping
	@Barrier(PurviewConstants.STUDENT_POINTS)
	public JsonResult _consume(
			@RequestParam(value = "studentId") String loginId,
			@RequestParam(value = "comments") String comments,
			@RequestParam(value = "giftNo") String giftNo,
			@RequestParam("quantity") int qty) {

		if (qty <= 0) {
			Pe.raise("无效的数量");
		}

		MapBean mb = studentDao.meta4exhange(loginId);

		if (mb == null) {
			Pe.raise("无效的学员编号");
		}
		Long wu_id = mb.asLong("id");
		// 允许访问学生吗?

		long branch_id = sessionUserService.getBranch().getIdLong();

		// 非总部,必须检查
		if (!sessionUserService.isHeadquarter()) {
			// studentDao.checkOwner(new Long[] { wu_id }, branch_id);
			if (branch_id != mb.asLong("branchId")) {
				Pe.raise("越权访问");
			}
		}

		MapBean gift = giftDao.meta4exchange(giftNo);

		if (gift == null) {
			Pe.raise("指定的礼品编号不存在:" + giftNo);
		}

		int points = gift.asInteger("points");
		if (points <= 0) {
			Pe.raise("无效的积分，必须为正整数");
		}

		points *= qty; // 数量翻倍

		if (mb.asInteger("points") < points) {
			Pe.raise("指定的学员积分不足:" + points + " 当前:" + mb.asInteger("points"));
		}

		if (!giftStockDao.isEnough(gift.asLong("id"), qty, branch_id)) {
			Pe.raise("本部礼品库存不足:" + gift.string("name") + " " + qty + " "
					+ gift.string("unit"));
		}

		Gift t_gift = Gift.create(gift.asLong("id"));

		// 扣除库存
		GiftStockLog gsl = new GiftStockLog();
		gsl.setBranch(Branch.create(branch_id));
		gsl.setComments("积分兑换:" + comments);
		gsl.setCreator(sessionUserService.getUser());
		gsl.setEventTime(Calendar.getInstance());
		gsl.setQuantity(qty);
		gsl.setType(GiftStockLog.OUT);
		gsl.setGift(t_gift);
		giftStockLogDao.save(gsl);
		giftStockDao.update(gsl);

		comments = gift.string("no") + " " + gift.string("name") + " :"
				+ comments;

		studentPointsService.save(wu_id, StudentPoints.FROM_EXCHANGE, points * -1,
				comments, StudentPoints.MT_NONE, t_gift,
				GiftHelper.buildRefId(t_gift.getId()));

		return JsonResult.s();
	}

	@RequestMapping
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	@Barrier(PurviewConstants.STUDENT_POINTS)
	public ModelAndView report(
			@ObjectParam("condition") StudentPointsCondition condition,
			@RequestParam(value = "search", required = false) boolean search) {

		if (condition == null) {
			condition = new StudentPointsCondition();
			// date span
			condition.setBeginDate(DateHelper.getThisMonthBegin());
			condition.setEndDate(DateHelper.getThisMonthEnd());

		}

		Mix<List<ExchangePointsVo>, ExchangePointsVo> results = null;
		if (search) {
			results = studentPointsDao.list(condition);
		}

		return new ModelAndView().addObject("results", results)
				.addObject("search", search).addObject("condition", condition);

	}
}
