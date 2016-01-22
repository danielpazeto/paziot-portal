package com.pazeto.iot.shared.vo;

import java.io.Serializable;

import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

@Entity
@Index
public class IoPort implements Serializable {

	@Id private  Long id;
	private String iONumber;
	private Long deviceId;
	private String type;
	
	

}
