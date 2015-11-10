/**
 * 
 */
package pula.sys.domains;

import java.util.Date;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

import puerta.support.dao.LoggablePo;
import pula.sys.vo.WechatNotifyEntity;

/**
 * @author Liangfei
 *
 */
public class NoticeOrder implements LoggablePo {

	@JsonProperty
	private long id;
	@JsonProperty
	private String no;
	@JsonProperty
	private long noticeId;
	@JsonProperty
	private String noticeNo;
	@JsonProperty
	private String noticeName;
	@JsonProperty
	private int count;
	@JsonProperty
	private long noticePrice;
	@JsonProperty
	private Date createTime;
	@JsonProperty
	private Date updateTime;
	@JsonProperty
	private String studentNo;
	@JsonProperty
	private long studentId;
	@JsonProperty
	private boolean enabled;
	@JsonProperty
	private boolean removed;

	/**
	 * 当前订单的状态
	 */
	@JsonProperty
	private int orderPayStatus;

	/**
	 * Wechat Payment fields
	 */
	@JsonProperty
	private double paied;
	@JsonProperty
	private String prepayId;
	@JsonProperty
	private String accessToken;
	@JsonProperty
	private String wxOrderId;
	@JsonProperty
	private String wxOrderDetail;

	@JsonProperty
	private String return_code;
	@JsonProperty
	private String return_msg;
	@JsonProperty
	private String appid;
	@JsonProperty
	private String mch_id;
	@JsonProperty
	private String device_info;
	@JsonProperty
	private String nonce_str;
	@JsonProperty
	private String sign;
	@JsonProperty
	private String result_code;
	@JsonProperty
	private String err_code;
	@JsonProperty
	private String err_code_des;
	@JsonProperty
	private String openid;
	@JsonProperty
	private String is_subscribe;
	@JsonProperty
	private String trade_type;
	@JsonProperty
	private String bank_type;
	@JsonProperty
	private int total_fee;
	@JsonProperty
	private String fee_type;
	@JsonProperty
	private int cash_fee;
	@JsonProperty
	private String cash_fee_type;
	@JsonProperty
	private int coupon_fee;
	@JsonProperty
	private int coupon_count;
	/*
	 * 代金券或立减优惠ID coupon_id_$n @JsonProperty否 String(20) 10000
	 * 代金券或立减优惠ID,$n为下标，从0开始编号 单个代金券或立减优惠支付金额 coupon_fee_$n 否 Int 100
	 * 单个代金券或立减优惠支付金额,$n为下标，从0开始编号
	 */
	@JsonProperty
	private String transaction_id;
	@JsonProperty
	private String out_trade_no;
	@JsonProperty
	private String attach;
	@JsonProperty
	private String time_end;

	/**
	 * 微信支付的状态
	 */
	@JsonProperty
	private int wxPayStatus;
	@JsonProperty
	private String comments;

	public NoticeOrder() {
	}

	public NoticeOrder(NoticeOrder o) {
		this.id = o.id;
		this.no = o.no;
		this.accessToken = o.accessToken;
		this.comments = o.comments;
		this.count = o.count;
		this.createTime = o.createTime;
		this.updateTime = o.updateTime;
		this.enabled = o.enabled;
		this.removed = o.removed;
		this.noticeId = o.noticeId;
		this.noticeName = o.noticeName;
		this.noticeNo = o.noticeNo;
		this.noticePrice = o.noticePrice;
		this.orderPayStatus = o.orderPayStatus;
		this.paied = o.paied;
		this.prepayId = o.prepayId;
		this.studentId = o.studentId;
		this.studentNo = o.studentNo;
		this.wxOrderDetail = o.wxOrderDetail;
		this.wxOrderId = o.wxOrderId;
		this.wxPayStatus = o.wxPayStatus;
		
		// notify fields
		this.appid = o.getAppid();
		this.attach = o.getAttach();
		this.bank_type = o.getBank_type();
		this.cash_fee = o.getCash_fee();
		this.cash_fee_type = o.getCash_fee_type();
		this.coupon_count = o.getCoupon_count();
		this.coupon_fee = o.getCoupon_fee();
		this.device_info = o.getDevice_info();
		this.err_code = o.getErr_code();
		this.err_code_des = o.getErr_code_des();
		this.fee_type = o.getFee_type();
		this.is_subscribe = o.getIs_subscribe();
		this.mch_id = o.getMch_id();
		this.nonce_str = o.getNonce_str();
		this.openid = o.getOpenid();
		this.out_trade_no = o.getOut_trade_no();
		this.result_code = o.getResult_code();
		this.return_code = o.getReturn_code();
		this.return_msg = o.getReturn_msg();
		this.sign = o.getSign();
		this.time_end = o.getTime_end();
		this.total_fee = o.getTotal_fee();
		this.trade_type = o.getTrade_type();
		this.transaction_id = o.getTransaction_id();
	}

	@Override
	public String toLogString() {
		StringBuilder sb = new StringBuilder();
		sb.append("id=").append(id).append("no=").append(no)
				.append("noticeId=").append(noticeId).append("paied=")
				.append(paied).append("orderStatus=").append(orderPayStatus)
				.append("wxPayStatus=").append(wxPayStatus)
				.append("wxOrderId=").append(wxOrderId);
		return sb.toString();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}

	public long getNoticeId() {
		return noticeId;
	}

	public void setNoticeId(long noticeId) {
		this.noticeId = noticeId;
	}

	public String getNoticeName() {
		return noticeName;
	}

	public void setNoticeName(String noticeName) {
		this.noticeName = noticeName;
	}

	public long getNoticePrice() {
		return noticePrice;
	}

	public void setNoticePrice(long noticePrice) {
		this.noticePrice = noticePrice;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public int getOrderPayStatus() {
		return orderPayStatus;
	}

	public void setOrderPayStatus(int orderPayStatus) {
		this.orderPayStatus = orderPayStatus;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getWxOrderId() {
		return wxOrderId;
	}

	public void setWxOrderId(String wxOrderId) {
		this.wxOrderId = wxOrderId;
	}

	public String getWxOrderDetail() {
		return wxOrderDetail;
	}

	public void setWxOrderDetail(String wxOrderDetail) {
		this.wxOrderDetail = wxOrderDetail;
	}

	public int getWxPayStatus() {
		return wxPayStatus;
	}

	public void setWxPayStatus(int wxPayStatus) {
		this.wxPayStatus = wxPayStatus;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getNoticeNo() {
		return noticeNo;
	}

	public void setNoticeNo(String noticeNo) {
		this.noticeNo = noticeNo;
	}

	public String getStudentNo() {
		return studentNo;
	}

	public void setStudentNo(String studentNo) {
		this.studentNo = studentNo;
	}

	public long getStudentId() {
		return studentId;
	}

	public void setStudentId(long studentId) {
		this.studentId = studentId;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public boolean isRemoved() {
		return removed;
	}

	public void setRemoved(boolean removed) {
		this.removed = removed;
	}

	public double getPaied() {
		return paied;
	}

	public void setPaied(double paied) {
		this.paied = paied;
	}

	public String getPrepayId() {
		return prepayId;
	}

	public void setPrepayId(String prepayId) {
		this.prepayId = prepayId;
	}

	@JsonIgnore
	public void setNotify(WechatNotifyEntity notifyEntity) {
		this.appid = notifyEntity.getAppid();
		this.attach = notifyEntity.getAttach();
		this.bank_type = notifyEntity.getBank_type();
		this.cash_fee = notifyEntity.getCash_fee();
		this.cash_fee_type = notifyEntity.getCash_fee_type();
		this.coupon_count = notifyEntity.getCoupon_count();
		this.coupon_fee = notifyEntity.getCoupon_fee();
		this.device_info = notifyEntity.getDevice_info();
		this.err_code = notifyEntity.getErr_code();
		this.err_code_des = notifyEntity.getErr_code_des();
		this.fee_type = notifyEntity.getFee_type();
		this.is_subscribe = notifyEntity.getIs_subscribe();
		this.mch_id = notifyEntity.getMch_id();
		this.nonce_str = notifyEntity.getNonce_str();
		this.openid = notifyEntity.getOpenid();
		this.out_trade_no = notifyEntity.getOut_trade_no();
		this.result_code = notifyEntity.getResult_code();
		this.return_code = notifyEntity.getReturn_code();
		this.return_msg = notifyEntity.getReturn_msg();
		this.sign = notifyEntity.getSign();
		this.time_end = notifyEntity.getTime_end();
		this.total_fee = notifyEntity.getTotal_fee();
		this.trade_type = notifyEntity.getTrade_type();
		this.transaction_id = notifyEntity.getTransaction_id();
	}

	public String getReturn_code() {
		return return_code;
	}

	public void setReturn_code(String return_code) {
		this.return_code = return_code;
	}

	public String getReturn_msg() {
		return return_msg;
	}

	public void setReturn_msg(String return_msg) {
		this.return_msg = return_msg;
	}

	public String getAppid() {
		return appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}

	public String getMch_id() {
		return mch_id;
	}

	public void setMch_id(String mch_id) {
		this.mch_id = mch_id;
	}

	public String getDevice_info() {
		return device_info;
	}

	public void setDevice_info(String device_info) {
		this.device_info = device_info;
	}

	public String getNonce_str() {
		return nonce_str;
	}

	public void setNonce_str(String nonce_str) {
		this.nonce_str = nonce_str;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getResult_code() {
		return result_code;
	}

	public void setResult_code(String result_code) {
		this.result_code = result_code;
	}

	public String getErr_code() {
		return err_code;
	}

	public void setErr_code(String err_code) {
		this.err_code = err_code;
	}

	public String getErr_code_des() {
		return err_code_des;
	}

	public void setErr_code_des(String err_code_des) {
		this.err_code_des = err_code_des;
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public String getIs_subscribe() {
		return is_subscribe;
	}

	public void setIs_subscribe(String is_subscribe) {
		this.is_subscribe = is_subscribe;
	}

	public String getTrade_type() {
		return trade_type;
	}

	public void setTrade_type(String trade_type) {
		this.trade_type = trade_type;
	}

	public String getBank_type() {
		return bank_type;
	}

	public void setBank_type(String bank_type) {
		this.bank_type = bank_type;
	}

	public int getTotal_fee() {
		return total_fee;
	}

	public void setTotal_fee(int total_fee) {
		this.total_fee = total_fee;
	}

	public String getFee_type() {
		return fee_type;
	}

	public void setFee_type(String fee_type) {
		this.fee_type = fee_type;
	}

	public int getCash_fee() {
		return cash_fee;
	}

	public void setCash_fee(int cash_fee) {
		this.cash_fee = cash_fee;
	}

	public String getCash_fee_type() {
		return cash_fee_type;
	}

	public void setCash_fee_type(String cash_fee_type) {
		this.cash_fee_type = cash_fee_type;
	}

	public int getCoupon_fee() {
		return coupon_fee;
	}

	public void setCoupon_fee(int coupon_fee) {
		this.coupon_fee = coupon_fee;
	}

	public int getCoupon_count() {
		return coupon_count;
	}

	public void setCoupon_count(int coupon_count) {
		this.coupon_count = coupon_count;
	}

	public String getTransaction_id() {
		return transaction_id;
	}

	public void setTransaction_id(String transaction_id) {
		this.transaction_id = transaction_id;
	}

	public String getOut_trade_no() {
		return out_trade_no;
	}

	public void setOut_trade_no(String out_trade_no) {
		this.out_trade_no = out_trade_no;
	}

	public String getAttach() {
		return attach;
	}

	public void setAttach(String attach) {
		this.attach = attach;
	}

	public String getTime_end() {
		return time_end;
	}

	public void setTime_end(String time_end) {
		this.time_end = time_end;
	}
	
}
