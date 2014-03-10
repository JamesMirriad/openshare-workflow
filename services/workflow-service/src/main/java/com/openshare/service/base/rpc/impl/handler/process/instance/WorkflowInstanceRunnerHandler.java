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
/**
 * executes a run time instance given a deployment id of the associated
 * process definition
 * @author james.mcilroy
 *
 */
public class WorkflowInstanceRunnerHandler  extends MethodHandler<String> {

	private static final Logger logger = Logger.getLogger(WorkflowInstanceRunnerHandler.class);

	@Override
	protected OpenShareResponse executeWithConvertedPayload(
			String convertedPayload) {
		//get hold of the execution engine
		ActivitiHelper activityHelper = ActivitiHelper.getInstance();
		ProcessEngine engine = activityHelper.getProcessEngine();
		logger.info("deleteing deployment with id:" + convertedPayload);
		//find definition from deployment id
		ProcessDefinition definition = engine.getRepositoryService().createProcessDefinitionQuery().deploymentId(convertedPayload).singleResult();
		RuntimeService runtimeService = engine.getRuntimeService();
		final ProcessInstance process = runtimeService.startProcessInstanceById(definition.getId());
		//assign sysadmin as default owner
		runtimeService.addUserIdentityLink(process.getId(), OpenShareConstants.DEFAULT_USER, IdentityLinkType.OWNER);
		runtimeService.addUserIdentityLink(process.getId(), OpenShareConstants.DEFAULT_USER, IdentityLinkType.STARTER);
		//return whatever we need to here...
		OpenShareResponse response = new OpenShareResponse();
		response.setTxid(this.getTransactionId());
		response.setPayload(process.getId());
		return response;
	}

}
