package com.openshare.service.base.comms;

import org.apache.log4j.Logger;

import com.openshare.service.base.dto.callback.CallbackNotifier;
import com.openshare.service.base.request.OpenshareRequest;

/**
 * utility class for calling back orchestration or other services.
 * @author james.mcilroy
 *
 */
public class CallBackUtil {
	private static final Logger logger = Logger.getLogger(CallBackUtil.class);
	
	/**
	 * create a call back notifier.
	 * @param request
	 * @return
	 */
	public static CallbackNotifier generateCallBackNotifier(OpenshareRequest request){
		return CallBackUtil.generateCallBackNotifier(request.getCallBackReferenceId(),request.getCallBackEndPoint(),request.getServiceOriginName());
	}
	
	/**
	 * create a call back notifier.
	 * @param request
	 * @param serviceName
	 * @return
	 */
	public static CallbackNotifier generateCallBackNotifier(OpenshareRequest request, String serviceName){
		return CallBackUtil.generateCallBackNotifier(request.getCallBackReferenceId(),request.getCallBackEndPoint(),serviceName);
	}
	
	/**
	 * create a call back notifier.
	 * @param request
	 * @return
	 */
	public static CallbackNotifier generateCallBackNotifier(String callBackRefId,String callBackEndpoint,String serviceOriginName){
		//if no call back endpoint specified, assume it's going back to orchestration.
//		if(callBackEndpoint==null || callBackEndpoint.isEmpty()){
//			logger.info("creating default notifier for refId: " +callBackRefId);
//			OrchestrationRestCallBackNotifier notifier = new OrchestrationRestCallBackNotifier(callBackRefId, serviceOriginName);
//			return notifier;
//		}
//		else{
//			logger.info("creating notifier for: " + callBackEndpoint + "/api/resume/ for refId: " +callBackRefId);
//			RestCallBackNotifier notifier = new RestCallBackNotifier(callBackRefId,callBackEndpoint, serviceOriginName);
//			return notifier;
//		}
		return null;
	}
}
