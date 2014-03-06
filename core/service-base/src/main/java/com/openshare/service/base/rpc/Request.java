package com.openshare.service.base.rpc;

import java.io.Serializable;

/**
 * A request class for the openshare RPC
 * @author james.mcilroy
 *
 */
public class Request implements Serializable{
	
	private static final long serialVersionUID = 5446389369891928912L;
	private String type = "request";
	private String method;	
	private Object payload;
	private String txid;

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
