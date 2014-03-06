package com.openshare.service.registry.controller.method;

import com.openshare.service.registry.controller.exception.OpenShareException;
import com.openshare.service.registry.controller.method.impl.AddServiceEntryHandler;
import com.openshare.service.registry.controller.method.impl.DeleteServiceEntryHandler;
import com.openshare.service.registry.controller.method.impl.ListAllServicesHandler;
import com.openshare.service.registry.controller.method.impl.PingHandler;
import com.openshare.service.registry.controller.method.impl.ReadServiceEntryHandler;
import com.openshare.service.registry.controller.method.impl.UpdateServiceEntryHandler;
import com.openshare.service.registry.model.dto.ServiceDto;

public enum ServiceMethodEnum {
	
	PING			("ping",	String.class,		PingHandler.class),
	LIST			("list",	null,				ListAllServicesHandler.class),
	ADD_SERVICE 	("create",	ServiceDto.class,	AddServiceEntryHandler.class),
	READ_SERVICE 	("read",	String.class,		ReadServiceEntryHandler.class),
	UPDATE_SERVICE 	("update",	ServiceDto.class,	UpdateServiceEntryHandler.class),
	REMOVE_SERVICE 	("delete",	String.class,		DeleteServiceEntryHandler.class);
	
	private final String methodName;
	private final Class<?> payloadClass;
	private final Class<? extends MethodHandler> methodHandlerClass;
	
	private ServiceMethodEnum(String methodName,Class<?> payloadClass,Class<? extends MethodHandler> methodHandlerClass){
		this.methodName = methodName;
		this.methodHandlerClass = methodHandlerClass;
		this.payloadClass = payloadClass;
	}

	/**
	 * 
	 * @return
	 */
	public String getMethodName() {
		return methodName;
	}

	/**
	 * 
	 * @return
	 */
	public Class<?> getPayloadClass() {
		return payloadClass;
	}

	/**
	 * 
	 * @return
	 */
	public Class<? extends MethodHandler> getMethodHandlerClass() {
		return methodHandlerClass;
	}
	
	/**
	 * 
	 * @param method
	 * @return
	 */
	public static ServiceMethodEnum getByMethod(String method) throws OpenShareException{
		for(ServiceMethodEnum sme : ServiceMethodEnum.values()){
			if(sme.getMethodName().equals(method)){
				return sme;
			}
		}
		throw new OpenShareException("method: " + method + " not supported by this service");
	}
}
