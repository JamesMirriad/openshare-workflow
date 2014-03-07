package com.openshare.service.base.response;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;
/**
 * generalized response object
 * @author james.mcilroy
 *
 */
@ApiModel(value="MirriadResponse", description="Mirriad General Operation Response")
@XmlRootElement(name = "MirriadResponse")
@XmlAccessorType(XmlAccessType.FIELD)
public class OpenshareResponse{

	//name of originating service
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
}
