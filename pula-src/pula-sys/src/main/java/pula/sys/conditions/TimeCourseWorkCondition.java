package pula.sys.conditions;

import puerta.support.dao.CommonCondition;

public class TimeCourseWorkCondition extends CommonCondition {

    private String studentNo;
    private String courseNo;
    private int rate = -1;

    public String getStudentNo() {
        return studentNo;
    }

    public void setStudentNo(String studentNo) {
        this.studentNo = studentNo;
    }

    public String getCourseNo() {
        return courseNo;
    }

    public void setCourseNo(String courseNo) {
        this.courseNo = courseNo;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

}
