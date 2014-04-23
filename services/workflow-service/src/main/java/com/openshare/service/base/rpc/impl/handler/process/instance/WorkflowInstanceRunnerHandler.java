package com.openshare.service.base.rpc.impl.handler.process.instance;

import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.IdentityLinkType;
import org.apache.log4j.Logger;

import com.openshare.service.base.constants.OpenShareConstants;
import com.openshare.service.base.exception.OpenshareException;
import com.openshare.service.base.rpc.MethodHandler;
import com.openshare.service.base.rpc.OpenShareResponse;
import com.openshare.service.base.rpc.StatusEnum;
import com.openshare.service.base.rpc.impl.palyoad.process.instance.WorkflowInstanceRunPayload;
import com.openshare.workflow.conf.ActivitiHelper;
import com.openshare.workflow.ext.constants.WorkflowConstants;
/**
 * executes a run time instance given a deployment id of the associated
 * process definition
 * @author james.mcilroy
 *
 */
public class WorkflowInstanceRunnerHandler  extends MethodHandler<WorkflowInstanceRunPayload> {

	private static final Logger logger = Logger.getLogger(WorkflowInstanceRunnerHandler.class);

	@Override
	protected OpenShareResponse executeWithConvertedPayload(
			WorkflowInstanceRunPayload convertedPayload) throws OpenshareException{
		OpenShareResponse response = new OpenShareResponse();
		//get hold of the execution engine
		ActivitiHelper activityHelper = ActivitiHelper.getInstance();
		ProcessEngine engine = activityHelper.getProcessEngine();
		logger.info("attempting to run workflow: " + convertedPayload.getWorkflow() + " on file " + convertedPayload.getFileUri());
		//find definition from deployment id
		ProcessDefinition definition = engine.getRepositoryService().createProcessDefinitionQuery()
				.processDefinitionKey(convertedPayload.getWorkflow()).latestVersion().singleResult();

		if(definition==null){
			response.setTxid(this.getTransactionId());
			response.setPayload("could not execute workflow: " + convertedPayload.getWorkflow() + " as it does not exist");
			response.setStatus(StatusEnum.INVALID);
			return response;
		}
		Map<String,Object> processVariables = new HashMap<String,Object>();
		processVariables.put(WorkflowConstants.SOURCE_FILE, convertedPayload.getFileUri());
		
		RuntimeService runtimeService = engine.getRuntimeService();
		final ProcessInstance process = runtimeService.startProcessInstanceById(definition.getId(),processVariables);
		//assign sysadmin as default owner -> this seems to crash, as it's only seemingly set after the process has finished executing
		try{
			runtimeService.addUserIdentityLink(process.getId(), OpenShareConstants.DEFAULT_USER, IdentityLinkType.OWNER);
			runtimeService.addUserIdentityLink(process.getId(), OpenShareConstants.DEFAULT_USER, IdentityLinkType.STARTER);
		}
		catch(Throwable t){
			logger.error("Non fatal error - could not set identity links on workflow");
		}
		//return whatever we need to here...
		response.setTxid(this.getTransactionId());
		response.setStatus(StatusEnum.OK);
		response.setPayload(process.getId());
		return response;
	}

}
