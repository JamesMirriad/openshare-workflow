package com.openshare.service.registry.controller.method;

import java.io.IOException;

import org.codehaus.jackson.map.ObjectMapper;

import com.openshare.service.registry.controller.exception.OpenShareException;

public class MethodFactory {

	private static class SingletonHolder { 
        public static final MethodFactory INSTANCE = new MethodFactory();
	}

	/**
	 * singleton access
	 * @return
	 */
	public static MethodFactory getInstance() {
	        return SingletonHolder.INSTANCE;
	}
	
	private MethodFactory(){
		
	}
	
	public MethodHandler getMethodHandler(String transactionId, String method, Object payload) throws OpenShareException{
		ServiceMethodEnum sme = ServiceMethodEnum.getByMethod(method);
		Class <? extends MethodHandler> handlerClass = sme.getMethodHandlerClass();
		try {
			MethodHandler handler = handlerClass.newInstance();
			//check payload is correct at this point...
			ObjectMapper mapper = new ObjectMapper();
			if(sme.getPayloadClass()!=null){
//				Object value = mapper.readValue(payload, sme.getPayloadClass());
				Object value = mapper.convertValue(payload, sme.getPayloadClass());
			}
			handler.setTransactionId(transactionId);
			handler.setPayload(payload);
			return handler;
		} 
		catch (Exception e) {
			throw new OpenShareException("Cannot create handler for method: " + method);
		}
	}
	
}
