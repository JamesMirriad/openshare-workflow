package com.openshare.service.base.web;

import com.openshare.service.base.exception.OpenshareException;
import com.openshare.service.base.request.OpenshareRequest;
import com.openshare.service.base.response.OpenshareResponse;
/**
 * General interface so we have a contract of basic functionality for all services
 * that support a resume and error, ie, will want to call external services and 
 * wait forthem to complete before resuming task.
 * @author james.mcilroy
 *
 */
public interface OpenshareResumableWebServiceBase extends OpenshareWebServiceBase{
	
	/**
	 * service resume request. Implement as POST
	 * @param callBack
	 * @return
	 */
	public OpenshareResponse resume(OpenshareException callBack);
	
	/**
	 * Service resume error notification, to be used when an 
	 * external service was called, but encountered an error.
	 * THis is to be able to allow this service to deal with 
	 * the consequences of external failure
	 * @param callBack
	 * @return
	 */
	public OpenshareResponse error(OpenshareRequest callBack);
		
}
