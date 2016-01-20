package com.pazeto.iot.shared.vo;

import java.io.Serializable;

import java.util.Date;

import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

@Entity
public class Device implements Serializable {

	@Index
	@Id long chipId;
	Date createDate;
	@Index
	private Ref<User> user;

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

	public User getUser() {
		return user.get();
	}

	public void setUser(User user) {
		this.user =  Ref.create(user);
	}

}
