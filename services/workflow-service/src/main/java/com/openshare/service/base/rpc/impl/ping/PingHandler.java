package com.openshare.service.base.rpc.impl.ping;

import com.openshare.service.base.rpc.MethodHandler;
import com.openshare.service.base.rpc.OpenShareResponse;
/**
 * A simple pin handler common to everything
 * @author james.mcilroy
 *
 */
public class PingHandler extends MethodHandler {

	@Override
	public OpenShareResponse handleExecution() {
		OpenShareResponse response = new OpenShareResponse();
		response.setType("response");
		response.setPayload("ping received payload:" + payload);
		return response;
	}

}
