package com.yuhj.ontheway.bean;

import java.io.Serializable;

/**
 * @name UserInfo
 * @Descripation 这是人物的实体类<br>
 *		1、<br>
 *		2、<br>
 *      3、<br>
 * @author 禹慧军
 * @date 2014-10-22
 * @version 1.0
 */
public class UserInfo implements Serializable{
	private static final long serialVersionUID = 642225199212241050L;
	/**
	 * 姓名
	 */
	private String nickname;
	
	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getUserId() {
		return UserId;
	}

	public void setUserId(String userId) {
		UserId = userId;
	}

	private String avatar;
	
	private String UserId;
	
	private String username;

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUsername() {
		return username;
	}



}
