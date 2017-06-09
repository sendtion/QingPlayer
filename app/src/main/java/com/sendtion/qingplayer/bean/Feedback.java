package com.sendtion.qingplayer.bean;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobRelation;

public class Feedback extends BmobObject {
	private static final long serialVersionUID = 1L;

//	private MyUser user;//反馈的用户
	private String deviceInfo;//设备信息
	private String userContact;//联系方式
	private String userFeedback;//反馈内容
	private Integer commentCount;
	private Integer favourCount;
	private Integer unfavourCount;
//	private List<String> commentUser;
//	private List<String> favourUser;
//	private List<String> unfavourUser;
	private BmobRelation favours;//所有支持的用户
	private BmobRelation unfavours;//所有反对的用户
	private Integer isClose;//是否关闭

	public String getDeviceInfo() {
		return deviceInfo;
	}

	public void setDeviceInfo(String deviceInfo) {
		this.deviceInfo = deviceInfo;
	}

	public String getUserContact() {
		return userContact;
	}

	public void setUserContact(String userContact) {
		this.userContact = userContact;
	}

	public String getUserFeedback() {
		return userFeedback;
	}

	public void setUserFeedback(String userFeedback) {
		this.userFeedback = userFeedback;
	}

	public Integer getCommentCount() {
		return commentCount;
	}

	public void setCommentCount(Integer commentCount) {
		this.commentCount = commentCount;
	}

	public Integer getFavourCount() {
		return favourCount;
	}

	public void setFavourCount(Integer favourCount) {
		this.favourCount = favourCount;
	}

	public Integer getUnfavourCount() {
		return unfavourCount;
	}

	public void setUnfavourCount(Integer unfavourCount) {
		this.unfavourCount = unfavourCount;
	}


//	public List<String> getUnfavourUser() {
//		return unfavourUser;
//	}
//
//	public void setUnfavourUser(List<String> unfavourUser) {
//		this.unfavourUser = unfavourUser;
//	}
//
//	public List<String> getFavourUser() {
//		return favourUser;
//	}
//
//	public void setFavourUser(List<String> favourUser) {
//		this.favourUser = favourUser;
//	}
//
//	public List<String> getCommentUser() {
//		return commentUser;
//	}
//
//	public void setCommentUser(List<String> commentUser) {
//		this.commentUser = commentUser;
//	}

	public BmobRelation getUnfavours() {
		return unfavours;
	}

	public void setUnfavours(BmobRelation unfavours) {
		this.unfavours = unfavours;
	}

	public BmobRelation getFavours() {
		return favours;
	}

	public void setFavours(BmobRelation favours) {
		this.favours = favours;
	}

	public Integer getIsClose() {
		return isClose;
	}

	public void setIsClose(Integer isClose) {
		this.isClose = isClose;
	}
}
