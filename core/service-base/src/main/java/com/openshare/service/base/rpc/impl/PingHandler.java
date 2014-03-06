package com.openshare.service.base.rpc.impl;

import com.openshare.service.base.rpc.MethodHandler;
import com.openshare.service.base.rpc.Response;
/**
 * A simple pin handler common to everything
 * @author james.mcilroy
 *
 */
public class PingHandler extends MethodHandler {

	@Override
	public Response handleExecution() {
		Response response = new Response();
		response.setType("response");
		response.setPayload("ping received payload:" + payload);
		return response;
	}

}
