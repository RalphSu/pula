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

@WxlDomain("次课作品")
public class TimeCourseWork implements LoggablePo {

    @JsonProperty
    private long id;
    @JsonProperty
    private String no;
    @JsonProperty
    private String courseNo;
    @JsonProperty
    private String studentNo;
    @JsonProperty
    private String imagePath;
    @JsonProperty
    private Date workEffectDate;
    @JsonProperty
    private String comments;
    /**
     * 0 - normal 1 - start
     */
    @JsonProperty
    private int rate;

    @JsonProperty
    private Date createTime;
    @JsonProperty
    private Date updateTime;
    @JsonProperty
    private boolean enabled;

    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    public String getImagePath() {
        return imagePath;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public Date getWorkEffectDate() {
        return workEffectDate;
    }

    public void setWorkEffectDate(Date workEffectDate) {
        this.workEffectDate = workEffectDate;
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

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public String toLogString() {
        StringBuilder sb = new StringBuilder();
        sb.append("TimeCourseWork: ").append("  courseNo:").append(courseNo).append(" studentNo:").append(studentNo)
                .append("   imagePath:").append(imagePath).append("    comments:").append(comments);
        return sb.toString();
    }

}
