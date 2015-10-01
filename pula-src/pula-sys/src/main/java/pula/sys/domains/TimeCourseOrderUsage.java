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
@WxlDomain("消次课消费")
public class TimeCourseOrderUsage implements LoggablePo {

    @JsonProperty
    private Long id;
    @JsonProperty
    private String courseNo;
    @JsonProperty
    private String orderNo;
    @JsonProperty
    private String no;
    @JsonProperty
    private String studentNo;
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
    private int usedGongfangCount;
    @JsonProperty
    private int usedHuodongCount;
    @JsonProperty
    private int usedSpecialCourseCount;

    @Override
    public String toLogString() {
        StringBuilder sb = new StringBuilder("【");
        sb.append("id=").append(id).append("courseId=").append(courseNo).append("orderId=").append(orderNo).append("studentId=").append(studentNo)
                .append("usedCount=").append(usedCount).append("usedCost=").append(usedCost).append("comments=")
                .append(comments).append("updateTime=").append(updateTime);
        sb.append("】");
        return sb.toString();
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

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getStudentNo() {
        return studentNo;
    }

    public void setStudentNo(String studentNo) {
        this.studentNo = studentNo;
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

    public int getUsedGongfangCount() {
        return usedGongfangCount;
    }

    public void setUsedGongfangCount(int usedGongfangCount) {
        this.usedGongfangCount = usedGongfangCount;
    }

    public int getUsedHuodongCount() {
        return usedHuodongCount;
    }

    public void setUsedHuodongCount(int usedHuodongCount) {
        this.usedHuodongCount = usedHuodongCount;
    }

    public int getUsedSpecialCourseCount() {
        return usedSpecialCourseCount;
    }

    public void setUsedSpecialCourseCount(int usedSpecialCourseCount) {
        this.usedSpecialCourseCount = usedSpecialCourseCount;
    }

}
