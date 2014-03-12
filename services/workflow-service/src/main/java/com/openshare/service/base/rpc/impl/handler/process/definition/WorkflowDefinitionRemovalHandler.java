package com.openshare.service.base.rpc.impl.handler.process.definition;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.apache.log4j.Logger;

import com.openshare.service.base.rpc.MethodHandler;
import com.openshare.service.base.rpc.OpenShareResponse;
import com.openshare.service.base.rpc.ServiceMethodMapper;
import com.openshare.workflow.conf.ActivitiHelper;
/**
 * A simple pin handler common to everything
 * @author james.mcilroy
 *
 */
public class WorkflowDefinitionRemovalHandler extends MethodHandler<String> {

	private static final Logger logger = Logger.getLogger(WorkflowDefinitionRemovalHandler.class);

	@Override
	protected OpenShareResponse executeWithConvertedPayload(
			String convertedPayload) {
		//get hold of the execution engine
		ActivitiHelper activityHelper = ActivitiHelper.getInstance();
		ProcessEngine engine = activityHelper.getProcessEngine();
		logger.info("deleteing deployment with id:" + convertedPayload);
		//delete the deployment
		engine.getRepositoryService().deleteDeployment(convertedPayload, true);

		OpenShareResponse response = new OpenShareResponse();
		response.setTxid(this.getTransactionId());
		response.setPayload(true);
		return response;
	}

}
