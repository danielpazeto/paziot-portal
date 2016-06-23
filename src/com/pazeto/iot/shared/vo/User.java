package com.pazeto.iot.shared.vo;

import java.io.Serializable;

import com.pazeto.iot.shared.dto.UserDTO;

public class User implements Serializable {

	private Long id;
	private String name, lastName;
	private String email;
	private String pwd;
	private String sessionId;
	private boolean loggedIn;
	private boolean isAdmin;

	public User(UserDTO userDTO) {
		setId(userDTO.getId());
		setName(userDTO.getName());
		setLastName(userDTO.getLastName());
		setEmail(userDTO.getEmail());
		setPwd(userDTO.getPwd());
		setIsAdmin(userDTO.getIsAdmin());
	}
	
	public User() {
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

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public boolean getLoggedIn() {
		return loggedIn;
	}

	public void setLoggedIn(boolean logged) {
		loggedIn = logged;
	}

	public boolean isAdmin() {
		return isAdmin;
	}
	
	public void setIsAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}
}
