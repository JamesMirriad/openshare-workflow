package com.openshare.service.base.rpc.impl.handler.config;

import java.util.Map;

import com.openshare.service.base.rpc.MethodHandler;
import com.openshare.service.base.rpc.OpenShareResponse;
import com.openshare.service.base.rpc.StatusEnum;
import com.openshare.workflow.ext.services.AvailableWorkflowComponents;

public class WorkflowConfigRetriever extends MethodHandler<String> {

	@Override
	protected OpenShareResponse executeWithConvertedPayload(
			String convertedPayload) {
		OpenShareResponse response = new OpenShareResponse();
		try{
			Map<String,String> components = AvailableWorkflowComponents.getAvailableWorkflowComponents();
			response.setPayload(components);
			response.setStatus(StatusEnum.OK);
		}
		catch(Throwable t){
			response.setStatus(StatusEnum.ERROR);
			response.setPayload(t);
		}
		return response;
	}

}
