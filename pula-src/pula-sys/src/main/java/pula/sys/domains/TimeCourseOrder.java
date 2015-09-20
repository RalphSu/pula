/**
 * 
 */
package pula.sys.domains;

import java.util.Date;

import org.codehaus.jackson.annotate.JsonProperty;

import puerta.support.annotation.WxlDomain;
import puerta.support.dao.LoggablePo;

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
    private String creator;
    @JsonProperty
    private String updator;

    @JsonProperty
    private int gongfangCount;
    @JsonProperty
    private int usedGongFangCount;
    @JsonProperty
    private int huodongCount;
    @JsonProperty
    private int usedHuodongCount;
    
    @JsonProperty
    private String courseTime;
//    @JsonProperty
//    private String effectTime;
    
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

}
