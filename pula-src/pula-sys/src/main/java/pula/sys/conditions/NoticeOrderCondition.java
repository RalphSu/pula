/**
 * 
 */
package pula.sys.conditions;

import puerta.support.dao.CommonCondition;

/**
 * @author Liangfei
 *
 */
public class NoticeOrderCondition extends CommonCondition {

    private String noticeNo;

    private String studentNo;

    private String studentName;

    private long prepayId;

    private String wxOrderId;


    public String getNoticeNo() {
        return noticeNo;
    }

    public void setNoticeNo(String noticeNo) {
        this.noticeNo = noticeNo;
    }

    public String getStudentNo() {
        return studentNo;
    }

    public void setStudentNo(String studentNo) {
        this.studentNo = studentNo;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public long getPrepayId() {
        return prepayId;
    }

    public void setPrepayId(long prepayId) {
        this.prepayId = prepayId;
    }

    public String getWxOrderId() {
        return wxOrderId;
    }

    public void setWxOrderId(String wxOrderId) {
        this.wxOrderId = wxOrderId;
    }

}
