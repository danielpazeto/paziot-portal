package com.pazeto.iot.shared.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.pazeto.iot.shared.vo.Device;

/**
 * Device class to persist in database
 * 
 * @author Daniel
 * 
 */
public class DeviceDTO implements Serializable {

	private String chipId;
	private String name;
	private Date createDate;
	private Long userId;

	List<IoPortDTO> ports;

	public DeviceDTO(Device dev) {
		setChipId(dev.getChipId());
		System.out.println("createDate : "+dev.getCreateDate());
		setCreateDate(dev.getCreateDate());
		setUserId(dev.getUserId());
		setName(dev.getName());
	}

	public DeviceDTO() {
		// TODO Auto-generated constructor stub
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

	public List<IoPortDTO> getPorts() {
		return ports;
	}

	public void setPorts(List<IoPortDTO> ports) {
		this.ports = ports;
	}

}
