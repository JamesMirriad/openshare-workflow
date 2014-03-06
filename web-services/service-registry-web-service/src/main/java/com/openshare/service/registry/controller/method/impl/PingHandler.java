package com.openshare.service.registry.controller.method.impl;

import com.openshare.service.registry.controller.method.MethodHandler;
import com.openshare.service.registry.model.Response;

public class PingHandler extends MethodHandler {

	@Override
	public Response handleExecution() {
		Response response = new Response();
		response.setType("response");
		response.setPayload("ping received payload:" + payload);
		return response;
	}

}
