package puerta.system.po;

import java.util.Calendar;

/**
 * 请求历史
 * 
 * @author tiyi
 * 
 */
public class RequestHistory {

	private long id;
	private String url; // 请求的url
	private Calendar createdTime;
	private String actorId; // 当时的用户
	private String extras;// 数据部分
	private String ip;
	private Purview purview;

	public Purview getPurview() {
		return purview;
	}

	public void setPurview(Purview purview) {
		this.purview = purview;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Calendar getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Calendar createdTime) {
		this.createdTime = createdTime;
	}

	public String getActorId() {
		return actorId;
	}

	public void setActorId(String actorId) {
		this.actorId = actorId;
	}

	public String getExtras() {
		return extras;
	}

	public void setExtras(String extras) {
		this.extras = extras;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

}
