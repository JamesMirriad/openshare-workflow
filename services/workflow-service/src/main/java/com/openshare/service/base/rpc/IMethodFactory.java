package com.openshare.service.base.rpc;

import com.openshare.service.base.exception.OpenshareException;

/**
 * Method factory interface, creates the correct method handler given the incoming parameters. 
 * @author james.mcilroy
 *
 */
public interface IMethodFactory {

	/**
	 * create a method handler. If the payload does not matchg the requirements of the method, 
	 * or if the method does not exist, an exception is thrown
	 * @param transactionId
	 * @param method
	 * @param payload
	 * @return
	 * @throws OpenshareException
	 */
	public MethodHandler getMethodHandler(String transactionId, String method, Object payload) throws OpenshareException;
	
}
