package com.openshare.service.base.rpc;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

@ApiModel(value="OpenShareResponse", description="Openshare General Operation Response")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class OpenShareResponse implements Serializable{
	
	private static final long serialVersionUID = -5128741549243211534L;

	@ApiModelProperty(required = true, value = "reference txid")
	private String txid;

	@ApiModelProperty(required = true, value = "status")
	StatusEnum status;

	@ApiModelProperty(required = true, value = "rpc version")
	String version="jRPC0.3";

	@ApiModelProperty(required = true, value = "type response")
	String type = "response";

	@ApiModelProperty(required = true, value = "payload object")
	Object payload;

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

	/**
	 * convenience method to turn an exception into 
	 * a string payload
	 * @param t
	 */
	public void setPayload(Throwable t) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PrintStream ps = new PrintStream(baos);
		String content = null;
		if(t!=null){
			t.printStackTrace(ps);
			content = baos.toString();
		}
		this.payload = content;
	}
}
