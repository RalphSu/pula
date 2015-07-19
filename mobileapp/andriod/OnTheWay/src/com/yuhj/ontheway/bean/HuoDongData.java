package com.yuhj.ontheway.bean;

public class HuoDongData {
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	
	public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public String getIamge() {
		return iamge;
	}
	public void setIamge(String iamge) {
		this.iamge = iamge;
	}
	
	public String getUpdateTime() {
        return updateTime;
    }
    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    private String updateTime;
	private String id;
	private String name;
	private String title;
	private String content;
	private String iamge;
	private String urlS;
	public String getUrlS() {
		return urlS;
	}
	public void setUrlS(String urlS) {
		this.urlS = urlS;
	}

}
