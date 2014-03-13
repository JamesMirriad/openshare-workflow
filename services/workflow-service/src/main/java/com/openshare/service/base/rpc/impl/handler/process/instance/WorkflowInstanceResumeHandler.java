package com.openshare.service.base.rpc.impl.handler.process.instance;

import java.util.HashMap;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RuntimeService;
import org.apache.log4j.Logger;

import com.openshare.service.base.rpc.MethodHandler;
import com.openshare.service.base.rpc.OpenShareResponse;
import com.openshare.service.base.rpc.impl.palyoad.process.instance.WorkflowInstanceResumePayload;
import com.openshare.workflow.conf.ActivitiHelper;
import com.openshare.workflow.ext.constants.WorkflowConstants;
/**
 * executes a run time instance given a deployment id of the associated
 * process definition
 * @author james.mcilroy
 *
 */
public class WorkflowInstanceResumeHandler  extends MethodHandler<WorkflowInstanceResumePayload> {

	private static final Logger logger = Logger.getLogger(WorkflowInstanceResumeHandler.class);

	@Override
	protected OpenShareResponse executeWithConvertedPayload(
			WorkflowInstanceResumePayload convertedPayload) {
		//get hold of the execution engine
		ActivitiHelper activityHelper = ActivitiHelper.getInstance();
		ProcessEngine engine = activityHelper.getProcessEngine();
		RuntimeService runtimeService = engine.getRuntimeService();
		logger.error("signalling resume for process with id: " + convertedPayload.getProcessId());
		
		//set local and global process variables that have been returned
		//TODO: need to put the call back variables in the map.
		HashMap<String,Object> localProcessVariables = new HashMap<String,Object>();
		HashMap<String,Object> globalProcessVariables = new HashMap<String,Object>();
		//add in the callback object in the local map ready to be used by the process component that is waiting for signal command

		Object payload = convertedPayload.getPayload();
		localProcessVariables.put(WorkflowConstants.CALLBACK_OBJECT, payload);
		
		//set variables into execution context
		runtimeService.setVariablesLocal(convertedPayload.getProcessId(), localProcessVariables);
		runtimeService.setVariables(convertedPayload.getProcessId(), globalProcessVariables);
		//trigger signal, indicating a resume.Need to do retries as access to the process instance is 
		runtimeService.signal(convertedPayload.getProcessId());
		
		OpenShareResponse response = new OpenShareResponse();
		response.setTxid(this.getTransactionId());
		response.setPayload(convertedPayload.getProcessId());
		return response;
	}

}
