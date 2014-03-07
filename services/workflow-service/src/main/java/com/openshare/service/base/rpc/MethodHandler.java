package com.openshare.service.base.rpc;


/**
 * a method handler class.
 * @author james.mcilroy
 *
 */
public abstract class MethodHandler {

	protected Object payload;
	protected String transactionId;
	
	public abstract OpenShareResponse handleExecution();

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

	protected OpenShareResponse generateErrorResponse(String error){
		OpenShareResponse response = new OpenShareResponse();
		response.setTxid(transactionId);
		response.setStatus(StatusEnum.ERROR);
		response.setPayload(error);
		return response;
	}
}
