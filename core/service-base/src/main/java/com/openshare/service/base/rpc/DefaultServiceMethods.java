package com.openshare.service.base.rpc;

import com.openshare.service.base.rpc.impl.PingHandler;
/**
 * default service methods, if no service method is defined to override this in the factory, then these are the ones used.
 * @author james.mcilroy
 *
 */
public enum DefaultServiceMethods implements IServiceMethod {
	
	PING			("ping",	String.class,		PingHandler.class);

	private final String methodName;
	private final Class<?> payloadClass;
	private final Class<? extends MethodHandler> methodHandlerClass;
	
	private DefaultServiceMethods(String methodName,Class<?> payloadClass, Class<? extends MethodHandler> methodHandlerClass){
		this.methodName = methodName;
		this.payloadClass = payloadClass;
		this.methodHandlerClass = methodHandlerClass;
	}
	
	
	public String getMethodName() {
		// TODO Auto-generated method stub
		return methodName;
	}

	
	public Class<?> getPayloadClass() {
		// TODO Auto-generated method stub
		return payloadClass;
	}

	
	public Class<? extends MethodHandler> getMethodHandlerClass() {
		// TODO Auto-generated method stub
		return methodHandlerClass;
	}

	
	public static IServiceMethod getServiceMethodImpl(String methodName) {
		// TODO Auto-generated method stub
		return null;
	}

}
