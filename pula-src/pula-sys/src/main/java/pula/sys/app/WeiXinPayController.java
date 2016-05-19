package pula.sys.app;

import java.text.MessageFormat;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import puerta.support.annotation.Barrier;
import puerta.support.utils.MD5;
import pula.sys.daos.NoticeDao;
import pula.sys.daos.NoticeOrderDao;
import pula.sys.daos.TimeCourseDao;
import pula.sys.daos.TimeCourseOrderDao;
import pula.sys.domains.Notice;
import pula.sys.domains.NoticeOrder;
import pula.sys.domains.TimeCourse;
import pula.sys.domains.TimeCourseOrder;
import pula.sys.vo.WechatNotifyEntity;
import pula.sys.wechat.pay.ResponseHandler;
import pula.sys.wechat.pay.util.ConstantUtil;
import pula.sys.wechat.pay.util.ResponsePage;

import com.alibaba.fastjson.JSON;

/**
 * 微信支付服务端简单示例
 * 
 * @author seven_cm
 * @dateTime 2014-11-29
 */
@Controller
@Barrier(ignore=true)
public class WeiXinPayController extends ResponsePage {

	private Logger log = Logger.getLogger(WeiXinPayController.class);

	@Resource
	private TimeCourseOrderDao tcOrderDao;
	@Resource
	private NoticeOrderDao noticeOrderDao;
	@Resource
	private NoticeDao noticeDao;
	@Resource
	private TimeCourseDao tcDao;

	@RequestMapping(value = "wechatPayNotify", consumes = {
			MediaType.TEXT_XML_VALUE, MediaType.APPLICATION_XML_VALUE }, produces = { MediaType.TEXT_XML_VALUE })
	@Transactional
	@ResponseBody
	public String wechatPayNotify(@RequestBody String xml,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		log.info("Receive message: " + xml);
		log.info("request parameter map: " + request.getParameterMap());
		log.info("request encoding: " + request.getCharacterEncoding());
		log.info("response encoding: " + response.getCharacterEncoding());

		String code = "SUCCESS";
		String message = "OK";

		// parse request
		ResponseHandler resHandler = new ResponseHandler(xml, request, response);
		resHandler.setKey(ConstantUtil.PARTNER_KEY);// set key for below sign
													// check
		// 判断签名
		// TODO: need check ten pay sign, otherwise might be hacked attacked
//		if (resHandler.isTenpaySign()) {
			WechatNotifyEntity notifyEntity = readNotifyRequest(request,
					resHandler);
			if (notifyEntity.getReturn_code().toUpperCase().contains("SUCCESS")) {
				StringBuilder msgBuilder = new StringBuilder();
				StringBuilder codeBuilder = new StringBuilder();

				// key action
				checkOrCreateOrder(notifyEntity, msgBuilder, codeBuilder);
				// key action

				code = codeBuilder.toString();
				message = msgBuilder.toString();
			} else {
				log.error(String.format(
						"支付结果通知提示失败 return_code: %s, request:%s",
						notifyEntity.getReturn_code(),
						JSON.toJSONString(notifyEntity)));
				message = String.format("支付结果通知提示失败 return_code: %s　- 拒绝服务",
						notifyEntity.getReturn_code());
				code = "FAIL";
			}
//		} else {
//			log.error(String.format("通知签名验证失败: %s",
//					printNotifyContent(resHandler)));
//			message = "通知签名验证失败 - 拒绝服务";
//			code = "FAIL";
//		}

		// write log
		if (code.equalsIgnoreCase("FAIL")) {
			log.error(message);
		} else {
			log.info(message);
		}

		return wrapResp(code, message);
	}

	private String wrapResp(String code, String message) {
		String template = "<xml> "
				+ " <return_code><![CDATA[%s]]></return_code> "
				+ " <return_msg><![CDATA[%s]]></return_msg> " + " </xml>";
		return String.format(template, code, message);
	}

	private void checkOrCreateOrder(WechatNotifyEntity notifyEntity, StringBuilder msgBuilder, StringBuilder codeBuilder) {
		String attach = notifyEntity.getAttach();
		//		.....@nt@.....@cnt@
		//		.....@tc@.....@cnt@
		if (StringUtils.isNotEmpty(notifyEntity.getAttach())) {
			if (attach.contains("@nt@")) {
				String[] ids = attach.split("@nt@");
				String studentNo = ids[0];
				String noticeNo = ids[1];
				int count =1;
				if (ids.length > 2) {
					count = parseCount(ids[2]);
				}
				createNoticeOrder(noticeNo, studentNo, count, notifyEntity, msgBuilder, codeBuilder);

			} else if (attach.contains("@tc@")) {
				String[] ids = attach.split("@tc@");
				String studentNo = ids[0];
				String tcNo = ids[1];
				int count =1;
				if (ids.length > 2) {
					count = parseCount(ids[2]);
				}
				createTimeCourseOrder(tcNo, studentNo, count, notifyEntity, msgBuilder, codeBuilder);
			} else {
				codeBuilder.append("FAIL");
				msgBuilder.append("Attach 格式错误,不能服务! NotifyEntity: " + JSON.toJSONString(notifyEntity));
			}
		} else {
			codeBuilder.append("FAIL");
			msgBuilder.append("Attach 属性为空,不能服务! NotifyEntity: " + JSON.toJSONString(notifyEntity));
		}
	}

	private int parseCount(String string) {
		try {
			return Integer.parseInt(string);
		} catch (Exception e) {
			log.error(MessageFormat.format(
					"Parse weixin order count failed, count string : {0}!",
					string), e);
			return 1;
		}
	}

	public void createTimeCourseOrder(String tcNo, String studentNo,
			int count, WechatNotifyEntity notifyEntity, StringBuilder msgBuilder,
			StringBuilder codeBuilder) {
		try {
			TimeCourse tc = tcDao.findByNo(tcNo);
			
			String hql = "SELECT a FROM TimeCourseOrder as a WHERE a.transaction_id=? AND a.enabled=?";

			List<TimeCourseOrder> orders = tcOrderDao.find(hql, new Object[] {notifyEntity.getTransaction_id(), true});
			if (orders.size() > 0) {
				codeBuilder.append("SUCCESS");
				msgBuilder.append(MessageFormat.format("支付已处理transaction id {0}, orderid{1}, orderno{2}",
						notifyEntity.getTransaction_id(),
						orders.get(0).getId(), orders.get(0).getNo()));
			} else {
				TimeCourseOrder cc = new TimeCourseOrder();
				cc.setNo("TCO:"
						+ MD5.GetMD5String("tco@" + System.currentTimeMillis()));
				cc.setBuyType(1);
				cc.setPaied(notifyEntity.getTotal_fee());
				cc.setPaiedCount(count);
				cc.setStudentNo(studentNo);
				cc.setCourseNo(tcNo);
				cc.setCourseTime("");
				cc.setCreator("byApp");
				cc.setEnabled(true);
				cc.setOrderStatus(1);

				cc.setNotify(notifyEntity);

				cc = tcOrderDao.save(cc);
				codeBuilder.append("SUCCESS");
				msgBuilder.append(MessageFormat.format("课程订单处理成功, transaction_id {0}, orderno {1}, orderid {1}",
					notifyEntity.getTransaction_id(),
					cc.getNo(), cc.getId()));
			}
		} catch (Exception e) {
			codeBuilder.append("FAIL");
			msgBuilder.append(MessageFormat.format(
					"支付课程订单处理失败,transaction id {0}, 原因:{1}",
					notifyEntity.getTransaction_id(), e.getMessage()));
		}
	}

	public void createNoticeOrder(String noticeNo, String studentNo,
			int count, WechatNotifyEntity notifyEntity, StringBuilder msgBuilder,
			StringBuilder codeBuilder) {
		try {
			Notice tc = noticeDao.findByNo(noticeNo);
			
			String hql = "SELECT a FROM NoticeOrder as a WHERE a.transaction_id=? AND a.enabled=?";

			List<NoticeOrder> orders = noticeOrderDao.find(hql, new Object[] {
					notifyEntity.getTransaction_id(), true });
			if (orders.size() > 0) {
				codeBuilder.append("SUCCESS");
				msgBuilder.append(MessageFormat.format("支付活动已处理transaction id {0}, orderid{1}, orderno{2}",
						notifyEntity.getTransaction_id(),
						orders.get(0).getId(), orders.get(0).getNo()));
			} else {
				NoticeOrder cc = new NoticeOrder();
				cc.setNo("TCO:"
						+ MD5.GetMD5String("tco@" + System.currentTimeMillis()));
				cc.setStudentNo(studentNo);
				cc.setNoticeNo(noticeNo);
				cc.setCount(count);
				cc.setPaied(notifyEntity.getTotal_fee());
				cc.setEnabled(true);
				if (tc != null) {
					cc.setNoticeId(tc.getId());
					cc.setNoticeName(tc.getTitle());
				}
				cc.setOrderPayStatus(1);

				cc.setNotify(notifyEntity);

				cc = noticeOrderDao.save(cc);
				codeBuilder.append("SUCCESS");
				msgBuilder.append(MessageFormat.format("活动订单处理成功, transaction_id {0}, orderno {1}, orderid {1}",
					notifyEntity.getTransaction_id(),
					cc.getNo(), cc.getId()));
			}
		} catch (Exception e) {
			codeBuilder.append("FAIL");
			msgBuilder.append(MessageFormat.format(
					"支付活动订单处理失败,transaction id {0}, 原因:{1}",
					notifyEntity.getTransaction_id(), e.getMessage()));
		}
	}

	private WechatNotifyEntity readNotifyRequest(HttpServletRequest request,
			ResponseHandler resHandler) {
		WechatNotifyEntity notifyEntity = new WechatNotifyEntity(resHandler.getAllParameters());
		return notifyEntity;
	}

	private String printNotifyContent(ResponseHandler request) {
		return JSON.toJSONString(request.getAllParameters());
	}

	@RequestMapping
	@ResponseBody
	public String getPayStatus(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam("orderId") String orderId,
			@RequestParam("orderType") int type)
	{
		// TODO.. 
		return "";
	}
	
}