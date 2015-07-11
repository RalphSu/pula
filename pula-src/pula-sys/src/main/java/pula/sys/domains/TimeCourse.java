/**
 * 
 */
package pula.sys.domains;

import java.util.Date;

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

    private String no;
    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    private Long id;
    private int courseType;
    private String name;
    private String branchName;
    private String classRoomName;
    private double price;
    private int maxStudentNum;
    private Date startTime;
    private Date endTime;
    private int startHour;
    private int startMinute;
    private int startWeekDay;
    private int durationMinute;
    private Date createTime;
    private Date updateTime;
    private String comments;
    private boolean removed;
    private boolean enabled = true;
    
    private String creator;
    private String updator;
    
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
        this.enabled = other.enabled;
        this.creator = other.creator;
        this.updator = other.updator;
    }
    

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

}
