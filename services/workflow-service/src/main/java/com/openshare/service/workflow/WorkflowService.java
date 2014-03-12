package com.openshare.service.workflow;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.IdentityLinkType;
import org.apache.log4j.Logger;

import com.openshare.service.base.exception.OpenshareException;
import com.openshare.service.base.request.OpenshareRequest;
import com.openshare.service.base.rpc.MethodHandler;
import com.openshare.service.base.rpc.OpenShareResponse;
import com.openshare.service.base.rpc.ServiceMethodMapper;
import com.openshare.workflow.conf.ActivitiHelper;
import com.openshare.workflow.ext.constants.WorkflowConstants;
/**
 * Orchestration service
 * @author james.mcilroy
 *
 */
public class WorkflowService {
	
	private static final Logger logger = Logger.getLogger(WorkflowService.class);

	private ProcessEngine engine;
	
	public WorkflowService(){
		//set up the process engine.
		ActivitiHelper activityHelper = ActivitiHelper.getInstance();
		engine = activityHelper.getProcessEngine();
	}
	
	/**
	 * run a method
	 * @param request
	 * @return
	 * @throws OpenshareException
	 */
	public OpenShareResponse runCommand(OpenshareRequest request) throws OpenshareException{
		logger.info("executing request " + request.getMethod() + " txid " + request.getTxid());
		MethodHandler<?> handler = ServiceMethodMapper.getMethodHandler(request.getTxid(), request.getMethod(), request.getPayload());
		return handler.handleExecution();
	}
	
	/**
     * start an orchestration with the business key name (name of workflow)
     * @param orchestrationName
	 * @param map 
     * @return
     */
	@Deprecated
	public String startOrchestrationFlow(String orchestrationName, Map<String, Object> map, String parentProcessId){
		RuntimeService runtimeService = engine.getRuntimeService();
		//start a workflow with the given name;
		if(parentProcessId!=null && !parentProcessId.isEmpty() && processExistsAndIsActive(parentProcessId)){
			//we have a parent process id specified, add to map, or create map if not present, then continue as before.
			if(map==null){
				map = new HashMap<String,Object>();
			}
//			map.put(MirriadOrchestrationConstants.CALL_BACK_ID_PARENT_PROCESS_VARIABLE, parentProcessId);
		}
		//depending on whether we have a map, call the appropriate method.
		if(map==null){
			final ProcessInstance process = runtimeService.startProcessInstanceByKey(orchestrationName);
			//assign sysadmin as default owner
			runtimeService.addUserIdentityLink(process.getId(), WorkflowConstants.DEFAULT_USER, IdentityLinkType.OWNER);
			runtimeService.addUserIdentityLink(process.getId(), WorkflowConstants.DEFAULT_USER, IdentityLinkType.STARTER);
			return process.getId();
		}
		else{
			final ProcessInstance process = runtimeService.startProcessInstanceByKey(orchestrationName, map);
			//assign sysadmin as default owner
			runtimeService.addUserIdentityLink(process.getId(), WorkflowConstants.DEFAULT_USER, IdentityLinkType.OWNER);
			runtimeService.addUserIdentityLink(process.getId(), WorkflowConstants.DEFAULT_USER, IdentityLinkType.STARTER);
			return process.getId();
		}
		
	}
	
	/**
	 * is there an active process for this process id
	 * @param processId
	 * @return
	 */
	@Deprecated
	public boolean processExistsAndIsActive(String processId){
		RuntimeService runtimeService = engine.getRuntimeService();
		long result = runtimeService.createProcessInstanceQuery().active().processInstanceId(processId).count();
		return (result == 1);
	}

	/**
	 * does this execution node exist?
	 * @param executionId
	 * @return
	 */
	@Deprecated
	public boolean executionExists(String executionId){
		RuntimeService runtimeService = engine.getRuntimeService();
		//if we don't want to add in retries, just perform the query.
		long result = runtimeService.createExecutionQuery().executionId(executionId).count();
		return (result == 1);
	}

	
	
	/**
	 * signal an execution that it can continue (ie external process has finished)
	 * @param executionId
	 */
	@Deprecated
	public void signal(String callBackId,Map<String,Object> globalProcessVariables,Map<String,Object> localProcessVariables){
		RuntimeService runtimeService = engine.getRuntimeService();
		logger.error("signalling resume for process with id: " + callBackId);
		//set local and global process variables that have been returned
		runtimeService.setVariablesLocal(callBackId, localProcessVariables);
		runtimeService.setVariables(callBackId, globalProcessVariables);
		//trigger signal, indicating a resume.Need to do retries as access to the process instance is 
		runtimeService.signal(callBackId);
	}
	
	/**
	 * Signal an error for the call back ref id (execution id)
	 * @param callBackReferenceId
	 * @throws Exception
	 */
	@Deprecated
	public void signalError(String callBackReferenceId) throws Exception {
		//lookup process from execution and suspend.
		logger.error("signallng error for process with id " + callBackReferenceId + " suspending process");
		RuntimeService runtimeService = engine.getRuntimeService();
		try{
			//try to signal the executig process that external part has failed
			Map<String,Object> errorMap = new HashMap<String,Object>();
			errorMap.put(WorkflowConstants.ERROR_IN_EXTERNAL_EXECUTION, "External execution failed");
			runtimeService.setVariablesLocal(callBackReferenceId, errorMap);
			runtimeService.signal(callBackReferenceId);
		}
		catch(Exception e){
			logger.error("failed to signal error for process with id " + callBackReferenceId);
		}
		Execution exec = runtimeService.createExecutionQuery().executionId(callBackReferenceId).singleResult();
		if(exec!=null){
			String processInstanceId = exec.getProcessInstanceId();
			runtimeService.suspendProcessInstanceById(processInstanceId);
		}
	}

	/**
	 * cancel a process instance
	 * @param id
	 * @throws OpenshareException
	 */
	@Deprecated
	public void cancel(String id) throws OpenshareException {
		logger.error("cancelling process with id " + id + " suspending process");
		RuntimeService runtimeService = engine.getRuntimeService();
		try{
			Execution exec = runtimeService.createExecutionQuery().executionId(id).singleResult();
			if(exec!=null){
				String processInstanceId = exec.getProcessInstanceId();
				runtimeService.suspendProcessInstanceById(processInstanceId);
			}
		}
		catch(Exception e){
			logger.error("failed to cancel process with id " + id);
			throw new OpenshareException("failed to cancel process with id: " + id,e);
		}
	}

	/**
	 * delete a process instance
	 * @param id
	 * @throws OpenshareException
	 */
	@Deprecated
	public void delete(String id) throws OpenshareException {
		logger.error("deleting process with id " + id + " suspending process");
		RuntimeService runtimeService = engine.getRuntimeService();
		String processInstanceId = null;
		try{
			Execution exec = runtimeService.createExecutionQuery().executionId(id).singleResult();
			if(exec!=null){
				processInstanceId = exec.getProcessInstanceId();
				runtimeService.suspendProcessInstanceById(processInstanceId);
				logger.info("deleting process instance: " + id);
				runtimeService.deleteProcessInstance(processInstanceId, "External Deletion Request");
			}
		}
		catch(Exception e){
			logger.error("failed to delete process with id " + id);
			throw new OpenshareException("failed to delete process with id: " + id,e);
		}
	}
	
	/**
	 * list all available workflows 
	 * @return
	 */
	@Deprecated
	public Map<String,String> listAvailableWorkflows(){
		//get repository service
		RepositoryService repositoryService = engine.getRepositoryService();
		//query it for ALL LATEST process definitions
		List<ProcessDefinition> processDefinitions = repositoryService.createProcessDefinitionQuery().latestVersion().list();
		Map<String,String> availableWorkFlows = new HashMap<String,String>();
		for(ProcessDefinition pd : processDefinitions){
			availableWorkFlows.put(pd.getId(), pd.getName());
		}
		return availableWorkFlows;
	}
	
	
	/**
	 * list all available workflows 
	 * @return
	 */
	@Deprecated
	public String getProcessodel(String processName) throws Exception{
		//get repository service
		RepositoryService repositoryService = engine.getRepositoryService();
		
		//query it for ALL LATEST process definitions
		ProcessDefinition pd = repositoryService.createProcessDefinitionQuery().processDefinitionKey(processName).latestVersion().singleResult();
		InputStream is = repositoryService.getProcessModel(pd.getId());
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
	    byte[] buffer = new byte[1024];
	    int length = 0;
	    while ((length = is.read(buffer)) != -1) {
	        baos.write(buffer, 0, length);
	    }
	    byte [] byteArray =  baos.toByteArray();
	    
		return new String(byteArray);
	}
	
	/**
	 * @TODO this should throroughly test wether all services are available, what is running etc.
	 * @return
	 */
	@Deprecated
	public boolean testService(){
		if(engine!=null 
				&& engine.getFormService()!=null
				&& engine.getRepositoryService()!=null
				&& engine.getRuntimeService()!=null
				&& engine.getHistoryService()!=null
				&& engine.getIdentityService()!=null
				&& engine.getManagementService()!=null
				&& engine.getTaskService()!=null){
			return true;
		}
		return false;
	}
}
