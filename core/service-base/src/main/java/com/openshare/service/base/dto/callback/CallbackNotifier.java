package com.openshare.service.base.dto.callback;

import com.openshare.service.base.dto.MirriadDtoObject;
import com.openshare.service.base.exception.OpenshareServiceException;
import com.openshare.service.base.exception.OpenshareException;

/**
 * this is a general object used for callback notifictions. 
 * For different ways of accessing a service we should extend this. 
 * It allows for a service to call a fixed way of notifying asynchronously 
 * that it is finished.
 * @author james.mcilroy
 *
 */
public abstract class CallbackNotifier {

	protected String callBackReferenceId;
	protected String callBackServiceName;
	
	/**
	 * constructor must take a callback referenceId, otherwise it 
	 * won't know how to call back.
	 * @param callBackReferenceId
	 */
	public CallbackNotifier(String callBackReferenceId, String callBackServiceName) {
		this.callBackReferenceId = callBackReferenceId;
		this.callBackServiceName = callBackServiceName;
	}

	/**
	 * tell the callback object it can notify whatever is listening,
	 * or however it is arranged, that the operation this was attached to is 
	 * complete and that we can call back.
	 * @throws OpenshareServiceException 
	 */
	public abstract void notifyComplete(MirriadDtoObject callBackResultObject) throws OpenshareServiceException;
	
	/**
	 * notify an error has occured.
	 * @param exception
	 * @throws OpenshareServiceException 
	 */
	public abstract void notifyOnError(OpenshareException exception) throws OpenshareServiceException;

	/**
	 * get the callback reference id
	 * @return
	 */
	public String getCallBackReferenceId() {
		return callBackReferenceId;
	}
	
	
}
