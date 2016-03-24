package com.pazeto.iot.shared.vo;

import java.io.Serializable;
import java.util.Date;

import com.pazeto.iot.shared.dto.DeviceDTO;

public class Device implements Serializable {

	private String chipId;
	private String name;
	private Date createDate;
	private Long userId;

	public Device(User userLogged) {
		setUserId(userLogged.getId());
	}

	public Device() {
		// TODO Auto-generated constructor stub
	}

	public Device(DeviceDTO devDto) {
		setChipId(devDto.getChipId());
		setName(devDto.getName());
		setCreateDate(devDto.getCreateDate());
		setUserId(devDto.getUserId());
	}

	public String getChipId() {
		return chipId;
	}

	public void setChipId(String chipId) {
		this.chipId = chipId;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

}
