package pula.sys.conditions;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import puerta.support.dao.CommonCondition;

public class TimeCourseWorkCondition extends CommonCondition {

    private static final Logger logger = LoggerFactory.getLogger(TimeCourseWorkCondition.class);

    private static final DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
    private String studentNo;
    private String courseNo;
    private String workEffectDate;
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

    public Date getLimitedWorkEffectDate() {
        try {
            return dateFormatter.parse(workEffectDate);
        } catch (Exception e) {
            logger.error("TimeCourseWorkCondition date format parse error!", e);
            return null;
        }
    }

    public String getWorkEffectDate() {
        return workEffectDate;
    }

    public void setWorkEffectDate(String workEffectDate) {
        this.workEffectDate = workEffectDate;
    }

}
