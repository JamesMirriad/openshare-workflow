package com.openshare.service.base.rpc.impl.handler.process.definition;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.apache.log4j.Logger;

import com.openshare.service.base.rpc.MethodHandler;
import com.openshare.service.base.rpc.OpenShareResponse;
import com.openshare.service.base.rpc.impl.palyoad.process.definition.WorkFlowDefinitionPayload;
import com.openshare.workflow.conf.ActivitiHelper;
/**
 * A simple pin handler common to everything
 * @author james.mcilroy
 *
 */
public class WorkflowDefinitionUploadHandler extends MethodHandler<WorkFlowDefinitionPayload> {

	private static final Logger logger = Logger.getLogger(WorkflowDefinitionUploadHandler.class);

	@Override
	protected OpenShareResponse executeWithConvertedPayload(
			WorkFlowDefinitionPayload convertedPayload) {
		//get hold of the execution engine
		ActivitiHelper activityHelper = ActivitiHelper.getInstance();
		ProcessEngine engine = activityHelper.getProcessEngine();
		logger.info("creating new deployment:");
		logger.info(convertedPayload.getData());
		//build the deployment
		DeploymentBuilder builder = engine.getRepositoryService().createDeployment();
		
		builder.addString(convertedPayload.getName()+".bpmn20.xml", convertedPayload.getData()).
				name(convertedPayload.getId());
		
		
		Deployment deployment = builder.deploy();
		logger.info("created new deploymet with id:" + deployment.getId() + " name: " + deployment.getName());
		OpenShareResponse response = new OpenShareResponse();
		response.setTxid(this.getTransactionId());
		response.setPayload(deployment.getId());
		return response;
	}

}
