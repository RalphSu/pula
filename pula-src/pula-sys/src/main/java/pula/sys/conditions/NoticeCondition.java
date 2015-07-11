/**
 * 
 */
package pula.sys.conditions;

import puerta.support.dao.CommonCondition;

/**
 * @author Liangfei
 *
 */
public class NoticeCondition extends CommonCondition {

    private int enabledStatus;

    private String keywords;

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public int getEnabledStatus() {
        return enabledStatus;
    }

    public void setEnabledStatus(int enabledStatus) {
        this.enabledStatus = enabledStatus;
    }

}
