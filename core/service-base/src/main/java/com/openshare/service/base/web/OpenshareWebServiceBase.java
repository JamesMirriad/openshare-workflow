package com.openshare.service.base.web;

import com.openshare.service.base.response.OpenshareServiceStatusResponse;
/**
 * General interface so we have a contract of basic functionality for all services
 * @author james.mcilroy
 *
 */
public interface OpenshareWebServiceBase {

	/**
	 * service status request. Implement as GET
	 * @return
	 */
	public OpenshareServiceStatusResponse getStatus();
}
