package com.openshare.service.base.rpc;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;

import com.openshare.service.base.exception.OpenshareException;


/**
 * a method handler class.
 * @author james.mcilroy
 *
 */
public abstract class MethodHandler<T> {

	private static final Logger logger = Logger.getLogger(MethodHandler.class);
	
	private Object payload;
	protected String transactionId;
	protected Class<T> type;
	
	/**
	 * Default constructor
	 */
	public MethodHandler(){
		Type t = getClass().getGenericSuperclass();
		ParameterizedType pt = (ParameterizedType) t;
		type = (Class<T>) pt.getActualTypeArguments()[0];
	}
	
	/**
	 * Handles the method execution
	 * @return
	 * @throws OpenshareException
	 */
	public final OpenShareResponse handleExecution() throws OpenshareException{
		try{
			T payloadConverted = getEntryFromPayload();
			return executeWithConvertedPayload(payloadConverted);
		}
		catch(Throwable t){
			throw new OpenshareException("Failed to execute method handler, cause:",t);
		}
	}
	
	protected abstract OpenShareResponse executeWithConvertedPayload(T convertedPayload);
	
	protected T getEntryFromPayload() throws OpenshareException{
		try{
			ObjectMapper mapper = new ObjectMapper();
			T entry = mapper.convertValue(payload, type);
			return entry;
		}
		catch(Throwable t){
			throw new OpenshareException("failed to convert payload: " + payload);
		}
	}
	
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
