package com.openshare.service.base.rpc.impl.handler.ping;

import com.openshare.service.base.rpc.MethodHandler;
import com.openshare.service.base.rpc.OpenShareResponse;
import com.openshare.service.base.rpc.StatusEnum;
/**
 * A simple pin handler common to everything
 * @author james.mcilroy
 *
 */
public class PingHandler extends MethodHandler<String> {

	@Override
	protected OpenShareResponse executeWithConvertedPayload(String convertedPayload) {
		OpenShareResponse response = new OpenShareResponse();
		response.setType("response");
		response.setPayload("ping received payload:" + convertedPayload);
		response.setStatus(StatusEnum.OK);
		return response;
	}

}
