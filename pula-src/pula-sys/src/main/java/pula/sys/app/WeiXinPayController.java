package pula.sys.app;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
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
import pula.sys.wechat.pay.AccessTokenRequestHandler;
import pula.sys.wechat.pay.ClientRequestHandler;
import pula.sys.wechat.pay.PackageRequestHandler;
import pula.sys.wechat.pay.PrepayIdRequestHandler;
import pula.sys.wechat.pay.ResponseHandler;
import pula.sys.wechat.pay.util.ConstantUtil;
import pula.sys.wechat.pay.util.ResponsePage;
import pula.sys.wechat.pay.util.TenpayUtil;
import pula.sys.wechat.pay.util.WXUtil;

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

	@RequestMapping("weixin.do")
	@ResponseBody
	public String doWeinXinRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Map<Object, Object> resInfo = new HashMap<Object, Object>();
		// 接收财付通通知的URL
		String notify_url = "http://127.0.0.1:8180/tenpay_api_b2c/payNotifyUrl.jsp";

		// ---------------生成订单号 开始------------------------
		// 当前时间 yyyyMMddHHmmss
		String currTime = TenpayUtil.getCurrTime();
		// 8位日期
		String strTime = currTime.substring(8, currTime.length());
		// 四位随机数
		String strRandom = TenpayUtil.buildRandom(4) + "";
		// 10位序列号,可以自行调整。
		String strReq = strTime + strRandom;
		// 订单号，此处用时间加随机数生成，商户根据自己情况调整，只要保持全局唯一就行
		String out_trade_no = strReq;
		// ---------------生成订单号 结束------------------------

		PackageRequestHandler packageReqHandler = new PackageRequestHandler(
				request, response);// 生成package的请求类
		PrepayIdRequestHandler prepayReqHandler = new PrepayIdRequestHandler(
				request, response);// 获取prepayid的请求类
		ClientRequestHandler clientHandler = new ClientRequestHandler(request,
				response);// 返回客户端支付参数的请求类
		packageReqHandler.setKey(ConstantUtil.PARTNER_KEY);

		int retcode;
		String retmsg = "";
		String xml_body = "";
		// 获取token值

		String token = AccessTokenRequestHandler.getAccessToken();

		log.info("获取token------值 " + token);

		if (!"".equals(token)) {
			// 设置package订单参数
			packageReqHandler.setParameter("bank_type", "WX");// 银行渠道
			packageReqHandler.setParameter("body", "测试"); // 商品描述
			packageReqHandler.setParameter("notify_url", notify_url); // 接收财付通通知的URL
			packageReqHandler.setParameter("partner", ConstantUtil.PARTNER); // 商户号
			packageReqHandler.setParameter("out_trade_no", out_trade_no); // 商家订单号
			packageReqHandler.setParameter("total_fee", "1"); // 商品金额,以分为单位
			packageReqHandler.setParameter("spbill_create_ip",
					request.getRemoteAddr()); // 订单生成的机器IP，指用户浏览器端IP
			packageReqHandler.setParameter("fee_type", "1"); // 币种，1人民币 66
			packageReqHandler.setParameter("input_charset", "GBK"); // 字符编码

			// 获取package包
			String packageValue = packageReqHandler.getRequestURL();
			resInfo.put("package", packageValue);

			log.info("获取package------值 " + packageValue);

			String noncestr = WXUtil.getNonceStr();
			String timestamp = WXUtil.getTimeStamp();
			String traceid = "";
			// //设置获取prepayid支付参数
			prepayReqHandler.setParameter("appid", ConstantUtil.APP_ID);
			prepayReqHandler.setParameter("appkey", ConstantUtil.APP_KEY);
			prepayReqHandler.setParameter("noncestr", noncestr);
			prepayReqHandler.setParameter("package", packageValue);
			prepayReqHandler.setParameter("timestamp", timestamp);
			prepayReqHandler.setParameter("traceid", traceid);

			// 生成获取预支付签名
			String sign = prepayReqHandler.createSHA1Sign();
			// 增加非参与签名的额外参数
			prepayReqHandler.setParameter("app_signature", sign);
			prepayReqHandler.setParameter("sign_method",
					ConstantUtil.SIGN_METHOD);
			String gateUrl = ConstantUtil.GATEURL + token;
			prepayReqHandler.setGateUrl(gateUrl);

			// 获取prepayId
			String prepayid = prepayReqHandler.sendPrepay();

			log.info("获取prepayid------值 " + prepayid);

			// 吐回给客户端的参数
			if (null != prepayid && !"".equals(prepayid)) {
				// 输出参数列表
				clientHandler.setParameter("appid", ConstantUtil.APP_ID);
				clientHandler.setParameter("appkey", ConstantUtil.APP_KEY);
				clientHandler.setParameter("noncestr", noncestr);
				// clientHandler.setParameter("package", "Sign=" +
				// packageValue);
				clientHandler.setParameter("package", "Sign=WXPay");
				clientHandler.setParameter("partnerid", ConstantUtil.PARTNER);
				clientHandler.setParameter("prepayid", prepayid);
				clientHandler.setParameter("timestamp", timestamp);
				// 生成签名
				sign = clientHandler.createSHA1Sign();
				clientHandler.setParameter("sign", sign);

				xml_body = clientHandler.getXmlBody();
				resInfo.put("entity", xml_body);
				retcode = 0;
				retmsg = "OK";
			} else {
				retcode = -2;
				retmsg = "错误：获取prepayId失败";
			}
		} else {
			retcode = -1;
			retmsg = "错误：获取不到Token";
		}

		resInfo.put("retcode", retcode);
		resInfo.put("retmsg", retmsg);
		String strJson = JSON.toJSONString(resInfo);
		return responseAjax(request, strJson);
	}

	@Resource
	private TimeCourseOrderDao tcOrderDao;
	@Resource
	private NoticeOrderDao noticeOrderDao;
	@Resource
	private NoticeDao noticeDao;
	@Resource
	private TimeCourseDao tcDao;

	@RequestMapping(value="wechatPayNotify", headers={"content-type=text/*"})
	@Transactional
	@ResponseBody
	public String wechatPayNotify(@RequestBody String xml,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String code = "SUCCESS";
		String message = "OK";

		// parse request
		ResponseHandler resHandler = new ResponseHandler(xml, request, response);
		resHandler.setKey(ConstantUtil.PARTNER_KEY);// set key for below sign
													// check
		// 判断签名
		if (resHandler.isTenpaySign()) {
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
		} else {
			log.error(String.format("通知签名验证失败: %s",
					printNotifyContent(resHandler)));
			message = "通知签名验证失败 - 拒绝服务";
			code = "FAIL";
		}

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
				+ " <return_code><![CDATA[{0}]]></return_code> "
				+ " <return_msg><![CDATA[{1}]]></return_msg> " + " </xml>";
		return MessageFormat.format(template, code, message);
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
				createNoticeOrder(noticeNo, studentNo, notifyEntity, msgBuilder, codeBuilder);

			} else if (attach.contains("@tc@")) {
				String[] ids = attach.split("@tc@");
				String studentNo = ids[0];
				String tcNo = ids[1];
				createTimeCourseOrder(tcNo, studentNo, notifyEntity, msgBuilder, codeBuilder);
			} else {
				codeBuilder.append("FAIL");
				msgBuilder.append("Attach 格式错误,不能服务! NotifyEntity: " + JSON.toJSONString(notifyEntity));
			}
		} else {
			codeBuilder.append("FAIL");
			msgBuilder.append("Attach 属性为空,不能服务! NotifyEntity: " + JSON.toJSONString(notifyEntity));
		}
	}

	private void createTimeCourseOrder(String tcNo, String studentNo,
			WechatNotifyEntity notifyEntity, StringBuilder msgBuilder,
			StringBuilder codeBuilder) {
		try {
			TimeCourse tc = tcDao.findByNo(tcNo);
			
			String hql = "SELECT a FROM timecourseorder as a WHERE a.transaction_id=? AND a.enabled=?";

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
//				cc.setPaiedCount(paiedCount);// TODO
				cc.setStudentNo(studentNo);
				cc.setCourseNo(tcNo);
				cc.setCourseTime("");
				cc.setCreator("byApp");
				cc.setEnabled(true);
				cc.setOrderStatus(1);;

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

	private void createNoticeOrder(String noticeNo, String studentNo,
			WechatNotifyEntity notifyEntity, StringBuilder msgBuilder,
			StringBuilder codeBuilder) {
		try {
			Notice tc = noticeDao.findByNo(noticeNo);
			
			String hql = "SELECT a FROM noticeorder as a WHERE a.transaction_id=? AND a.enabled=?";

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