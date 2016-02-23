package com.pazeto.iot.shared.vo;

import com.googlecode.objectify.annotation.Subclass;

@Subclass(index=true)
public class SettedValue extends Value {
	
	long sentDate;
	
	

	public void setSentDate(long sentDate) {
		this.sentDate = sentDate;
	}
	
	public long getSentDate() {
		return sentDate;
	}
	
	
}
