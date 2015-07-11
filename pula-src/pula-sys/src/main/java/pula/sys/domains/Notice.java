/**
 * 
 */
package pula.sys.domains;

import java.util.Date;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.ObjectMapper;

import puerta.support.annotation.WxlDomain;
import puerta.support.dao.LoggablePo;

/**
 * @author Liangfei
 *
 */
@WxlDomain("活动通知")
public class Notice implements LoggablePo {

    private static final ObjectMapper om = new ObjectMapper();

    @Override
    public String toLogString() {
        try {
            return om.writeValueAsString(this);
        } catch (Exception e) {
            StringBuilder sb = new StringBuilder("[Notice: ");
            sb.append("noticeId=").append(id).append("title=").append(title);
            sb.append("]");
            return sb.toString();
        }
    }

    @JsonProperty
    private Long id;
    @JsonProperty
    private String no;
    @JsonProperty
    private String title;
    @JsonProperty
    private String formattedTitle;
    @JsonProperty
    private String content;
    @JsonProperty
    private String imgPath;
    @JsonProperty
    private String suffix;
    @JsonProperty
    private Date createTime;
    @JsonProperty
    private Date updateTime;
    @JsonProperty
    private String comment;
    @JsonProperty
    private boolean removed;
    @JsonProperty
    private boolean enabled;

    private String creator;
    private String updator;

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFormattedTitle() {
        return formattedTitle;
    }

    public void setFormattedTitle(String formattedTitle) {
        this.formattedTitle = formattedTitle;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
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

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
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

}
