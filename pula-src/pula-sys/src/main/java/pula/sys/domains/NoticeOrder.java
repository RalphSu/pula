/**
 * 
 */
package pula.sys.domains;

import java.util.Date;

import org.codehaus.jackson.annotate.JsonProperty;

import puerta.support.dao.LoggablePo;

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
    }

    @Override
    public String toLogString() {
        StringBuilder sb = new StringBuilder();
        sb.append("id=").append(id).append("no=").append(no).append("noticeId=").append(noticeId).append("paied=")
                .append(paied).append("orderStatus=").append(orderPayStatus).append("wxPayStatus=").append(wxPayStatus)
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

}
