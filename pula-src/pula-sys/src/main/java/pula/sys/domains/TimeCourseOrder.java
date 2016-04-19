/**
 * 
 */
package pula.sys.domains;

import java.util.Date;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

import puerta.support.annotation.WxlDomain;
import puerta.support.dao.LoggablePo;
import pula.sys.vo.WechatNotifyEntity;

/**
 * @author Liangfei
 *
 */
@WxlDomain("消次课订单")
public class TimeCourseOrder implements LoggablePo {
    @JsonProperty
    private Long id;
    @JsonProperty
    private String courseNo;
    @JsonProperty
    private String no;
    @JsonProperty
    private String studentNo;
    /**
     * 0 - by cost 1 - by count
     */
    @JsonProperty
    private int buyType = 1;
    @JsonProperty
    private int paied;
    @JsonProperty
    private int paiedCount;
    @JsonProperty
    private int usedCount;
    @JsonProperty
    private int usedCost;
    @JsonProperty
    private Date createTime;
    @JsonProperty
    private Date updateTime;
    @JsonProperty
    private String comments;
    @JsonProperty
    private boolean removed;
    @JsonProperty
    private boolean enabled;
    @JsonProperty
    private String creator="";
    @JsonProperty
    private String updator="";

    @JsonProperty
    private int gongfangCount;
    @JsonProperty
    private int usedGongFangCount;
    @JsonProperty
    private int huodongCount;
    @JsonProperty
    private int usedHuodongCount;
    @JsonProperty
    private int specialCourseCount;
    @JsonProperty
    private int usedSpecialCourseCount;
    
    @JsonProperty
    private String courseTime="";
//    @JsonProperty
//    private String effectTime;
    @JsonProperty
    private String level="";
    
    // wechat order related
    @JsonProperty
	private String return_code="";
	@JsonProperty
	private String return_msg="";
	@JsonProperty
	private String appid="";
	@JsonProperty
	private String mch_id="";
	@JsonProperty
	private String device_info="";
	@JsonProperty
	private String nonce_str="";
	@JsonProperty
	private String sign="";
	@JsonProperty
	private String result_code="";
	@JsonProperty
	private String err_code="";
	@JsonProperty
	private String err_code_des="";
	@JsonProperty
	private String openid="";
	@JsonProperty
	private String is_subscribe="";
	@JsonProperty
	private String trade_type="";
	@JsonProperty
	private String bank_type="";
	@JsonProperty
	private int total_fee;
	@JsonProperty
	private String fee_type;
	@JsonProperty
	private int cash_fee;
	@JsonProperty
	private String cash_fee_type="";
	@JsonProperty
	private int coupon_fee;
	@JsonProperty
	private int coupon_count;
	/*
	 * 代金券或立减优惠ID	coupon_id_$n	@JsonProperty否	String(20)	10000	代金券或立减优惠ID,$n为下标，从0开始编号
单个代金券或立减优惠支付金额	coupon_fee_$n	否	Int	100	单个代金券或立减优惠支付金额,$n为下标，从0开始编号
	*/ 
	@JsonProperty
	private String transaction_id="";
	@JsonProperty
	private String out_trade_no="";
	@JsonProperty
	private String attach="";
	@JsonProperty
	private String time_end="";
    
    
    /**
     * 订单状态
     * 0 - 已提交
     * 1 - 已支付
     */
    @JsonProperty
    private int orderStatus;

    @Override
    public String toLogString() {
        StringBuilder sb = new StringBuilder();
        sb.append("TimeCourseOrder[").append("id=").append(id).append(",courseId=").append(courseNo)
                .append(",studentId=").append(studentNo).append(",paied=").append(paied).append(",paiedCount=")
                .append("remainCount=").append(usedCount).append(",remainCost=").append(usedCost)
                .append(paiedCount).append("]");
        return sb.toString();
    }

    public int getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(int orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCourseNo() {
        return courseNo;
    }

    public void setCourseNo(String courseNo) {
        this.courseNo = courseNo;
    }

    public String getStudentNo() {
        return studentNo;
    }

    public void setStudentNo(String studentNo) {
        this.studentNo = studentNo;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    /**
     * 0 - by cost 1 - by count
     */
    public int getBuyType() {
        return buyType;
    }

    public void setBuyType(int buyType) {
        this.buyType = buyType;
    }

    public int getPaied() {
        return paied;
    }

    public void setPaied(int paied) {
        this.paied = paied;
    }

    public int getPaiedCount() {
        return paiedCount;
    }

    public void setPaiedCount(int paiedCount) {
        this.paiedCount = paiedCount;
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

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public boolean isRemoved() {
        return removed;
    }

    public void setRemoved(boolean removed) {
        this.removed = removed;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getUpdator() {
        return updator;
    }

    public void setUpdator(String updator) {
        this.updator = updator;
    }

    public int getGongfangCount() {
        return gongfangCount;
    }

    public void setGongfangCount(int gongfangCount) {
        this.gongfangCount = gongfangCount;
    }

    public int getUsedGongFangCount() {
        return usedGongFangCount;
    }

    public void setUsedGongFangCount(int usedGongFangCount) {
        this.usedGongFangCount = usedGongFangCount;
    }

    public int getHuodongCount() {
        return huodongCount;
    }

    public void setHuodongCount(int huodongCount) {
        this.huodongCount = huodongCount;
    }

    public int getUsedHuodongCount() {
        return usedHuodongCount;
    }

    public void setUsedHuodongCount(int usedHuodongCount) {
        this.usedHuodongCount = usedHuodongCount;
    }

    public int getUsedCount() {
        return usedCount;
    }

    public void setUsedCount(int usedCount) {
        this.usedCount = usedCount;
    }

    public int getUsedCost() {
        return usedCost;
    }

    public void setUsedCost(int usedCost) {
        this.usedCost = usedCost;
    }

    public String getCourseTime() {
        return courseTime;
    }

    public void setCourseTime(String courseTime) {
        this.courseTime = courseTime;
    }

    public int getSpecialCourseCount() {
        return specialCourseCount;
    }

    public void setSpecialCourseCount(int specialCourseCount) {
        this.specialCourseCount = specialCourseCount;
    }

    public int getUsedSpecialCourseCount() {
        return usedSpecialCourseCount;
    }

    public void setUsedSpecialCourseCount(int usedSpecialCourseCount) {
        this.usedSpecialCourseCount = usedSpecialCourseCount;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
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
