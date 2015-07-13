/**
 * 
 */
package com.yuhj.ontheway.bean;

import java.io.Serializable;

/**
 * @name JingxuanDetailData
 * @Descripation <br>
 *		1、精选内容实体的设计<br>
 *		2、<br>
 *      3、<br>
 * @author 禹慧军
 * @date 2014-10-23
 * @version 1.0
 */
public class JingxuanDetailData implements Serializable {
	private static final long serialVersionUID = 642225199212241050L;
	private String image;
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getPoi() {
		return poi;
	}
	public void setPoi(String poi) {
		this.poi = poi;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	private String poi;
	private String text;

}
