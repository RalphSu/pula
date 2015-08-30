/**
 * 
 */
package pula.sys.domains;

import java.util.Date;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

import puerta.support.annotation.WxlDomain;
import puerta.support.dao.LoggablePo;

/**
 * 消次课模型
 * 
 * @author Liangfei
 *
 */
@WxlDomain("消次课")
public class TimeCourse implements LoggablePo {

    @JsonProperty
    private String no;

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }
    
    @JsonProperty
    private Long id;
    @JsonProperty
    private int courseType;
    @JsonProperty
    private String name;
    @JsonProperty
    private String branchName;
    @JsonProperty
    private String classRoomName;
    @JsonProperty
    private double price;
    @JsonProperty
    private int maxStudentNum;
    @JsonProperty
    private Date startTime;
    @JsonProperty
    private Date endTime;
    @JsonProperty
    private int startHour;
    @JsonProperty
    private int startMinute;
    @JsonProperty
    private int startWeekDay;
    @JsonProperty
    private int durationMinute;
    @JsonProperty
    private Date createTime;
    @JsonProperty
    private Date updateTime;
    @JsonProperty
    private String comments;
    @JsonProperty
    private boolean removed;
    @JsonProperty
    private boolean enabled = true;
    @JsonProperty
    private String creator;
    @JsonProperty
    private String updator;
    
    @JsonProperty
    private String applicableAges;
    
    public TimeCourse()
    {
        
    }
    
    // copy constructor
    public TimeCourse(TimeCourse other)
    {
        this.id = other.id;
        this.no = other.no;
        this.courseType = other.courseType;
        this.name = other.name;
        this.branchName = other.branchName;
        this.classRoomName = other.classRoomName;
        this.price = other.price;
        this.maxStudentNum = other.maxStudentNum;
        this.startTime = other.startTime;
        this.endTime = other.endTime;
        this.startHour = other.startHour;
        this.startMinute = other.startMinute;
        this.startWeekDay = other.startWeekDay;
        this.durationMinute = other.durationMinute;
        this.createTime = other.createTime;
        this.updateTime = other.updateTime;
        this.comments = other.comments;
        this.removed = other.removed;
        this.applicableAges = other.applicableAges;
        this.enabled = other.enabled;
        this.creator = other.creator;
        this.updator = other.updator;
    }
    
    @JsonIgnore
    @Override
    public String toLogString() {
        StringBuilder sb = new StringBuilder();
        sb.append(id).append(",").append(name).append(",");
        return sb.toString();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getCourseType() {
        return courseType;
    }

    public void setCourseType(int courseType) {
        this.courseType = courseType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getClassRoomName() {
        return classRoomName;
    }

    public void setClassRoomName(String classRoomName) {
        this.classRoomName = classRoomName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getMaxStudentNum() {
        return maxStudentNum;
    }

    public void setMaxStudentNum(int maxStudentNum) {
        this.maxStudentNum = maxStudentNum;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public int getStartHour() {
        return startHour;
    }

    public void setStartHour(int startHour) {
        this.startHour = startHour;
    }

    public int getStartMinute() {
        return startMinute;
    }

    public void setStartMinute(int startMinute) {
        this.startMinute = startMinute;
    }

    public int getStartWeekDay() {
        return startWeekDay;
    }

    public void setStartWeekDay(int startWeekDay) {
        this.startWeekDay = startWeekDay;
    }

    public int getDurationMinute() {
        return durationMinute;
    }

    public void setDurationMinute(int durationMinute) {
        this.durationMinute = durationMinute;
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

    public String getApplicableAges() {
        return applicableAges;
    }

    public void setApplicableAges(String applicableAges) {
        this.applicableAges = applicableAges;
    }

}
