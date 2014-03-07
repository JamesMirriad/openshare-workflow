package com.openshare.service.base.rpc;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class OpenShareResponse implements Serializable{
	
	private static final long serialVersionUID = -5128741549243211534L;

	private String txid;
	private StatusEnum status;
	private String version="jRPC0.3";
	private String type = "response";
	private Object payload;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}


	public String getTxid() {
		return txid;
	}

	public void setTxid(String txid) {
		this.txid = txid;
	}

	public StatusEnum getStatus() {
		return status;
	}

	public void setStatus(StatusEnum status) {
		this.status = status;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public Object getPayload() {
		return payload;
	}

	public void setPayload(Object payload) {
		this.payload = payload;
	}
}
