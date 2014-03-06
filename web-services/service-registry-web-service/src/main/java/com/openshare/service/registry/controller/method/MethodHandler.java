package com.openshare.service.registry.controller.method;

import com.openshare.service.registry.model.Response;
import com.openshare.service.registry.model.StatusEnum;

/**
 * a method handler class.
 * @author james.mcilroy
 *
 */
public abstract class MethodHandler {

	protected Object payload;
	protected String transactionId;
	
	public abstract Response handleExecution();

	public Object getPayload() {
		return payload;
	}

	public void setPayload(Object payload) {
		this.payload = payload;
	}
	
	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	protected Response generateErrorResponse(String error){
		Response response = new Response();
		response.setTxid(transactionId);
		response.setStatus(StatusEnum.ERROR);
		response.setPayload(error);
		return response;
	}
}
