/**
 * 
 */
package pula.sys.conditions;

import puerta.support.dao.CommonCondition;

/**
 * @author Liangfei
 *
 */
public class TimeCourseOrderCondition extends CommonCondition {

    private String courseNo;

    private String courseName;

    private String studentNo;

    private String studentName;

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

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

}
