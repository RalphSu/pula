/**
 * 
 */
package pula.sys.domains;

import java.util.Date;

import puerta.support.annotation.WxlDomain;
import puerta.support.dao.LoggablePo;

/**
 * @author Liangfei
 *
 */
@WxlDomain("消次课消费")
public class TimeCourseOrderUsage implements LoggablePo {

    private Long id;
    private String courseNo;
    private String orderNo;
    private String no;
    private String studentNo;
    private int usedCount;
    private int usedCost;
    private Date createTime;
    private Date updateTime;
    private String comments;
    private boolean removed;
    private boolean enabled;
    private String creator;
    private String updator;

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

}
