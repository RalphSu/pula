package pula.sys.domains;

import java.util.Calendar;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

import puerta.support.annotation.WxlDomain;
import puerta.support.dao.LoggablePo;

@WxlDomain("课程")
public class Course implements LoggablePo {

    @JsonProperty
	private long id;
    @JsonProperty
	private String no;
    @JsonProperty
    private String name;
	// 失效时间，上线时间（publishTime）
    @JsonProperty
	private Calendar expiredTime;
    @JsonProperty
    private Calendar createdTime;
    @JsonProperty
    private Calendar publishTime;
    @JsonProperty
    private Calendar updatedTime;
    @JsonProperty
	private boolean removed;
    @JsonProperty
    private boolean enabled;
    @JsonProperty
	private boolean showInWeb;
    @JsonIgnore
	private SysCategory category;
    @JsonIgnore
    private SysCategory subCategory; // 课程类别
    @JsonProperty
	private int indexNo; // 排序序号，从小到大
    @JsonProperty
	private String comments; // 备注,显示在网页上
    @JsonIgnore
	private SysUser creator;
    @JsonIgnore
    private SysUser updater;
    @JsonProperty
	private String key; // 播放key
    @JsonProperty
	private int minutes; // 课程长度

	public SysCategory getSubCategory() {
		return subCategory;
	}

	public void setSubCategory(SysCategory subCategory) {
		this.subCategory = subCategory;
	}

	public int getMinutes() {
		return minutes;
	}

	public void setMinutes(int minutes) {
		this.minutes = minutes;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public SysUser getCreator() {
		return creator;
	}

	public void setCreator(SysUser creator) {
		this.creator = creator;
	}

	public SysUser getUpdater() {
		return updater;
	}

	public void setUpdater(SysUser updater) {
		this.updater = updater;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Calendar getExpiredTime() {
		return expiredTime;
	}

	public void setExpiredTime(Calendar expiredTime) {
		this.expiredTime = expiredTime;
	}

	public Calendar getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Calendar createdTime) {
		this.createdTime = createdTime;
	}

	public Calendar getPublishTime() {
		return publishTime;
	}

	public void setPublishTime(Calendar publishTime) {
		this.publishTime = publishTime;
	}

	public Calendar getUpdatedTime() {
		return updatedTime;
	}

	public void setUpdatedTime(Calendar updatedTime) {
		this.updatedTime = updatedTime;
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

	public boolean isShowInWeb() {
		return showInWeb;
	}

	public void setShowInWeb(boolean showInWeb) {
		this.showInWeb = showInWeb;
	}

	public SysCategory getCategory() {
		return category;
	}

	public void setCategory(SysCategory category) {
		this.category = category;
	}

	public int getIndexNo() {
		return indexNo;
	}

	public void setIndexNo(int indexNo) {
		this.indexNo = indexNo;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	@Override
	public String toLogString() {
		return this.no + "(" + this.name + ")";
	}

	public static Course create(Long id2) {
		if (id2 == null || id2 == 0) {
			return null;
		}
		Course c = new Course();
		c.id = id2;
		return c;
	}

	// 网页相关信息，例如 图标？

}
