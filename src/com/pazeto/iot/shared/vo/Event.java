package com.pazeto.iot.shared.vo;

import java.util.Date;

import com.pazeto.iot.shared.dto.EventDTO;

public class Event {

	public enum ENTITIES { // WHO
		USER, DEVICE, IO_PORT;
		public String toString() {
			return this.name();
		};
	}

	public enum TYPE_EVENT { // WHAT
		CHANGE_PINTOUT_VALUE(0), CONNECTED(1), DISCONNECTED(2);

		TYPE_EVENT(int code) {
			this.code = code;
		}

		int code;

		public int getCode() {
			return code;
		}
		public void setCode(int code) {
			this.code = code;
		}
	}

	private static final long serialVersionUID = -3387891800736936513L;

	public Event(EventDTO eventDTO) {
		setRequester(ENTITIES.valueOf(eventDTO.getRequester()));
		setOwnId(eventDTO.getOwnId());
		setType(TYPE_EVENT.valueOf(eventDTO.getType()));
		setValue(eventDTO.getValue());
		setDate(eventDTO.getDate());
		setToEntitiy(ENTITIES.valueOf(eventDTO.getToEntitiy()));
		setToId(eventDTO.getToId());
	}

	private long id;
	private ENTITIES requester;
	private String ownId;
	private TYPE_EVENT type;
	private Date date;
	private String value;
	private String toId;
	private ENTITIES toEntitiy;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public ENTITIES getRequester() {
		return requester;
	}

	public void setRequester(ENTITIES requester) {
		this.requester = requester;
	}

	public String getOwnId() {
		return ownId;
	}

	public void setOwnId(String ownId) {
		this.ownId = ownId;
	}

	public TYPE_EVENT getType() {
		return type;
	}

	public void setType(TYPE_EVENT type) {
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

	public void setToEntitiy(ENTITIES toEntitiy) {
		this.toEntitiy = toEntitiy;
	}

	public ENTITIES getToEntitiy() {
		return toEntitiy;
	}

	public String getToId() {
		return toId;
	}

	public void setToId(String toId) {
		this.toId = toId;
	}

}
