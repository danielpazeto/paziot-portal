package com.pazeto.iot.shared.dto;

import java.io.Serializable;

import com.pazeto.iot.shared.vo.IoPort;


/**
 * Class to persist Io Ports in db
 * @author Daniel
 *
 */
public class IoPortDTO implements Serializable {

	private  Long id;
	private String iONumber;
	private String deviceId;
	private String type;
	
	
	public IoPortDTO() {
		// TODO Auto-generated constructor stub
	}
	
	public IoPortDTO(IoPort port){
		this.id = port.getId();
		this.iONumber = port.getiONumber();
		this.deviceId = port.getDeviceId();
		this.type= port.getType();
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getiONumber() {
		return iONumber;
	}
	public void setiONumber(String iONumber) {
		this.iONumber = iONumber;
	}
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	
	

}
