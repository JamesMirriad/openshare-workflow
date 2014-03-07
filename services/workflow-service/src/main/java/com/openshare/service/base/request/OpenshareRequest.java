package com.openshare.service.base.request;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 * basic base class for an object to call a service. has the service name in it
 * @author james.mcilroy
 *
 */
@ApiModel(value="OpenshareRequest", description="Openshare Generic Request")
@XmlRootElement(name = "OpenshareRequest")
@XmlAccessorType(XmlAccessType.FIELD)
public class OpenshareRequest {
	
	@ApiModelProperty(required = true, value = "type request")
	String type = "request";
	
	@ApiModelProperty(required = true, value = "requested method")
	String method;
	
	@ApiModelProperty(required = true, value = "payload object")
	Object payload;

	@ApiModelProperty(required = true, value = "reference txid")
	String txid;


	public String getTxid() {
		return txid;
	}

	public void setTxid(String txid) {
		this.txid = txid;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public Object getPayload() {
		return payload;
	}

	public void setPayload(Object payload) {
		this.payload = payload;
	}
}
