/**
 * 
 */
package pula.sys.domains;

import java.util.Date;

import org.codehaus.jackson.annotate.JsonProperty;

import puerta.support.annotation.WxlDomain;
import puerta.support.dao.LoggablePo;
import pula.sys.intfs.RefIdSupport;

/**
 * @author Liangfei
 *
 */

@WxlDomain("次课作品")
public class TimeCourseWork implements LoggablePo, RefIdSupport {

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
    @JsonProperty
    private String branchNo;
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
    
    @JsonProperty
    private boolean removed;
    
    @JsonProperty
    private String updator;
    @JsonProperty
    private String attachmentKey;
    
    private String studentName;
    private String courseName;

    public TimeCourseWork() {
    }
    
    public TimeCourseWork(TimeCourseWork other) {
        this.id = other.id;
        this.no = other.no;
        this.courseNo = other.courseNo;
        this.studentNo = other.studentNo;
        this.imagePath = other.imagePath;
        this.workEffectDate = other.workEffectDate;
        this.comments = other.comments;
        this.rate = other.rate;
        this.enabled = other.enabled;
        this.createTime = other.createTime;
        this.updateTime = other.updateTime;
        this.removed = other.removed;
    }

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

    public boolean isRemoved() {
        return removed;
    }

    public void setRemoved(boolean removed) {
        this.removed = removed;
    }

    public String getBranchNo() {
        return branchNo;
    }

    public void setBranchNo(String branchNo) {
        this.branchNo = branchNo;
    }

    public String getUpdator() {
        return updator;
    }

    public void setUpdator(String updator) {
        this.updator = updator;
    }

    public String getAttachmentKey() {
        return attachmentKey;
    }

    public void setAttachmentKey(String attachmentKey) {
        this.attachmentKey = attachmentKey;
    }

    @Override
    public String toLogString() {
        StringBuilder sb = new StringBuilder();
        sb.append("TimeCourseWork: ").append("  courseNo:").append(courseNo).append(" studentNo:").append(studentNo)
                .append("   imagePath:").append(imagePath).append("    comments:").append(comments);
        return sb.toString();
    }

    public String getStudentName() {
		return studentName;
	}

	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	@Override
    public String toRefId() {
        return CourseTaskResultWork.buildRefId(id, attachmentKey);
    }

    @Override
    public int getTypeRange() {
        return FileAttachment.TYPE_STUENDT_TIME_COURSE_WORK;
    }

}
