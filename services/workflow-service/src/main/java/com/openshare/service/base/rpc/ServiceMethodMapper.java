package com.openshare.service.base.rpc;

import com.openshare.service.base.exception.OpenshareException;
import com.openshare.service.base.rpc.impl.handler.ping.PingHandler;
import com.openshare.service.base.rpc.impl.handler.process.definition.WorkflowDefinitionRemovalHandler;
import com.openshare.service.base.rpc.impl.handler.process.definition.WorkflowDefinitionUploadHandler;
import com.openshare.service.base.rpc.impl.handler.process.instance.WorkflowInstanceResumeHandler;
import com.openshare.service.base.rpc.impl.handler.process.instance.WorkflowInstanceRunnerHandler;
import com.openshare.service.base.rpc.impl.handler.process.instance.WorkflowInstanceSuspenderHandler;
import com.openshare.service.base.rpc.impl.palyoad.process.definition.WorkFlowDefinitionPayload;
import com.openshare.service.base.rpc.impl.palyoad.process.instance.WorkflowInstanceResumePayload;
/**
 * default service methods, if no service method is defined to override this in the factory, then these are the ones used.
 * @author james.mcilroy
 *
 */
public enum ServiceMethodMapper {
	
	PING			("ping",	String.class,						PingHandler.class),
	WORKFLOW_ADD	("add",		WorkFlowDefinitionPayload.class,	WorkflowDefinitionUploadHandler.class),
	WORKFLOW_DELETE	("remove",	String.class,						WorkflowDefinitionRemovalHandler.class),
	WORKFLOW_RUN	("run",		String.class,						WorkflowInstanceRunnerHandler.class),
	WORKFLOW_SUSPEND("stop",	String.class,						WorkflowInstanceSuspenderHandler.class),
	WORKFLOW_RESUME ("resume",	WorkflowInstanceResumePayload.class,WorkflowInstanceResumeHandler.class);	
	
	private final String methodName;
	private final Class<?> payloadClass;
	private final Class<? extends MethodHandler<?>> methodHandlerClass;
	
	private ServiceMethodMapper(String methodName,Class<?> payloadClass, Class<? extends MethodHandler<?>> methodHandlerClass){
		this.methodName = methodName;
		this.payloadClass = payloadClass;
		this.methodHandlerClass = methodHandlerClass;
	}
	
	public String getMethodName() {
		return methodName;
	}

	public Class<?> getPayloadClass() {
		return payloadClass;
	}

	
	public Class<? extends MethodHandler<?>> getMethodHandlerClass() {
		return methodHandlerClass;
	}
	
	/**
	 * create a method handler
	 * @param transactionId
	 * @param method
	 * @param payload
	 * @return
	 * @throws OpenshareException
	 */
	@SuppressWarnings("unchecked")
	public static MethodHandler<?> getMethodHandler(String transactionId, String method, Object payload) throws OpenshareException{
		try{
			for(ServiceMethodMapper smm : ServiceMethodMapper.values()){
				if(smm.getMethodName().equals(method)){
					Class<? extends MethodHandler<?>> handlerClass = smm.getMethodHandlerClass();
					MethodHandler<?> handler = handlerClass.newInstance();
					//check payload is correct at this point...
					handler.setTransactionId(transactionId);
					handler.setPayload(payload);
					return handler;
				}
			}
			throw new OpenshareException("method: "+ method + " is not a recognised method");
		}
		catch(Throwable t){
			throw new OpenshareException("failed to execute requested method " + method + " for txid " + transactionId + " failed, cause:",t);
		}
	}

}
