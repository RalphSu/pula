/**
 * 
 */
package pula.sys.forms;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pula.sys.domains.TimeCourse;

/**
 * @author Liangfei
 *
 */
public class TimeCourseForm extends TimeCourse {

    private String startTimeText;
    private String endTimeText;
    

    // the given picture
    private FileAttachmentForm attachmentForms;


    public TimeCourse toCourse() {
        TimeCourse tc = new TimeCourse(this);
//        tc.setCreateTime(new Date());
        tc.setUpdateTime(new Date());
        tc.setStartTime(getDate(startTimeText));
        tc.setEndTime(getDate(endTimeText));
        return tc;
    }

    private static final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private static final Logger logger = LoggerFactory.getLogger(TimeCourseForm.class);

    private int parseInt(String intText) {
        if (StringUtils.isEmpty(intText)) {
            return 0;
        }

        try {
            return Integer.parseInt(intText);
        } catch (Exception e) {
            logger.error("parse integer failed!", e);
            return 0;
        }
    }

    public FileAttachmentForm getAttachmentForms() {
        return attachmentForms;
    }

    public void setAttachmentForms(FileAttachmentForm attachmentForms) {
        this.attachmentForms = attachmentForms;
    }

    public static Date getDate(String dateText) {
        if (StringUtils.isEmpty(dateText)) {
            return null;
        }

        try {
            return dateFormat.parse(dateText);
        } catch (Exception e) {
            logger.error("parse date failed!", e);
            return null;
        }
    }

    public String getStartTimeText() {
        return startTimeText;
    }

    public void setStartTimeText(String startTimeText) {
        this.startTimeText = startTimeText;
    }

    public String getEndTimeText() {
        return endTimeText;
    }

    public void setEndTimeText(String endTimeText) {
        this.endTimeText = endTimeText;
    }
}
