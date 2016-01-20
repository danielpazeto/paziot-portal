package com.pazeto.iot.server.entities;

import java.io.Serializable;
import java.util.Date;

public class Device implements Serializable {
	
	long chipId;
	Date createDate;
	long userId;
	public long getChipId() {
		return chipId;
	}
	public void setChipId(long chipId) {
		this.chipId = chipId;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public long getUser() {
		return userId;
	}
	public void setUser(long id) {
		this.userId = id;
	}

	
	
}
