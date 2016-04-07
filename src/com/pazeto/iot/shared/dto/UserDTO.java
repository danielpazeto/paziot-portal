package com.pazeto.iot.shared.dto;

import java.io.Serializable;
import java.util.List;

import com.pazeto.iot.shared.vo.User;

/**
 * User Class to persist in database
 * 
 * @author Daniel
 * 
 */
public class UserDTO implements Serializable {

	private static final long serialVersionUID = 6851402511184345741L;
	private Long id;
	private String name, lastName;
	private String email;
	private String pwd;

	List<DeviceDTO> devices;

	public UserDTO(User u) {
		setId(u.getId());
		setName(u.getName());
		setLastName(u.getLastName());
		setEmail(u.getEmail());
		setPwd(u.getPwd());
	}
	public UserDTO() {
		// TODO Auto-generated constructor stub
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastname) {
		this.lastName = lastname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<DeviceDTO> getDevices() {
		return devices;
	}

	public void setDevices(List<DeviceDTO> devices) {
		this.devices = devices;
	}

}
