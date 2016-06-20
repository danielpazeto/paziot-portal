package com.pazeto.iot.shared.dto;

import java.util.Date;

import com.pazeto.iot.shared.vo.Event;

public class EventDTO {

	private static final long serialVersionUID = -3387891800736936513L;

	public EventDTO() {
	}

	public EventDTO(Event event) {
		setRequester(event.getRequester().name());
		setOwnId(event.getOwnId());
		setType(event.getType().name());
		setValue(event.getValue());
		setDate(event.getDate());
		setToEntitiy(event.getToEntitiy().name());
		setToId(event.getToId());
	}

	private long id;
	private String requester;
	private String ownId;
	private String type;
	private Date date;
	private String value;
	private String toEntitiy;
	private String toId;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getRequester() {
		return requester;
	}

	public void setRequester(String requester) {
		this.requester = requester;
	}

	public String getOwnId() {
		return ownId;
	}

	public void setOwnId(String ownId) {
		this.ownId = ownId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getToEntitiy() {
		return toEntitiy;
	}

	public void setToEntitiy(String toEntitiy) {
		this.toEntitiy = toEntitiy;
	}

	public String getToId() {
		return toId;
	}

	public void setToId(String toId) {
		this.toId = toId;
	}

}
