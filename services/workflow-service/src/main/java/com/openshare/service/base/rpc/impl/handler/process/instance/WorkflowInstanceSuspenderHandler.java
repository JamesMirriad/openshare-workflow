package com.openshare.service.base.rpc.impl.handler.process.instance;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.IdentityLinkType;
import org.apache.log4j.Logger;

import com.openshare.service.base.constants.OpenShareConstants;
import com.openshare.service.base.rpc.MethodHandler;
import com.openshare.service.base.rpc.OpenShareResponse;
import com.openshare.workflow.conf.ActivitiHelper;

public class WorkflowInstanceSuspenderHandler extends MethodHandler<String> {

	private static final Logger logger = Logger.getLogger(WorkflowInstanceSuspenderHandler.class);

	@Override
	protected OpenShareResponse executeWithConvertedPayload(
			String convertedPayload) {
		//get hold of the execution engine
		ActivitiHelper activityHelper = ActivitiHelper.getInstance();
		ProcessEngine engine = activityHelper.getProcessEngine();
		logger.info("deleteing deployment with id:" + convertedPayload);
		//find definition from deployment id
		RuntimeService runtimeService = engine.getRuntimeService();
		runtimeService.suspendProcessInstanceById(convertedPayload);
		
		OpenShareResponse response = new OpenShareResponse();
		response.setTxid(this.getTransactionId());
		response.setPayload(true);
		return response;
	}

}
