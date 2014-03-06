package com.openshare.workflow.ext.services;

import java.io.Serializable;
import java.util.Map.Entry;

import org.activiti.engine.delegate.BpmnError;
import org.activiti.engine.impl.bpmn.behavior.TaskActivityBehavior;
import org.activiti.engine.impl.pvm.delegate.ActivityExecution;
import org.apache.log4j.Logger;

import com.openshare.service.base.exception.OpenshareException;
import com.openshare.workflow.ext.constants.WorkflowConstants;
import com.openshare.workflow.ext.constants.WorkflowErrorConstants;
import com.openshare.workflow.ext.exception.WorkflowException;
/**
 * this is a abstract template for operations within the system. Please extend this class 
 * with custom operations, using te worker threads to hand off to long running processes
 * that can be completed within the orchestration layer.
 * 
 *  For process that are external to the orchestration (web service calls etc) use this, and then 
 *  let the external service do a call back to the orchestration REST service to tell orchestration 
 *  that it is finished.
 *  
 *  THe calling sequence in this case would simply be to call the service, and get the response, and
 *  then wait for the call back via rest which will trigger the system into moving again. To do this, 
 *  observe the following:
 * 
 *  - The callback ID is the EXECUTION ID this must be saved in the process
 *  
 *  - The callback ID is saved as a LOCAL VARIABLE in all implementing classes that can be accessed as
 * 		this calls so it can call back and signal he node to contine execution, as per below.
 * 
 *  - If using an external service, pass off the callBack Id to that service which will then be responsible 
 *  	for keeping it safe. this also allows us to query the service for progress as it will be a unique id
 *  	specific to this process
 *  
 *   - use the doPostExecution() method (required in subclasses) to get out any information in a call back 
 *   	and to store this for later use in the process. This will need to be done so that later modules can 
 *   	use the data for their own processing. Not all operations will return anything, but some will.
 *  
 * // Retrieve the execution and cause it to trigger end state and leave the node via
 *  using the callBack Id saved previously which should be passed to the service we are targetting
 *	this.processEngine.getRuntimeService().signal(previouslySavedCallBackId);
 *
 * @author james.mcilroy
 *
 */
public abstract class AbstractAsyncJavaDelegateService extends TaskActivityBehavior
{
	private static final long serialVersionUID = -3579467266051396216L;
	
	private static final Logger logger = Logger.getLogger(AbstractAsyncJavaDelegateService.class);
	
	/**
	 * store a variable in local parameter context
	 * @param execution
	 * @param name
	 * @param object
	 */
	protected void saveVariable(ActivityExecution execution,String name, Serializable object){
		execution.setVariableLocal(generateKeyForInstance(execution,name),object);
	}
	
	/**
	 * retrieve variable from local parameter context
	 * @param execution
	 * @param name
	 * @return
	 */
	protected Serializable getSavedVariable(ActivityExecution execution,String name){
		return (Serializable) execution.getVariableLocal(generateKeyForInstance(execution,name));
	}
	
	/**
	 * generate unique key for local context
	 * @param execution
	 * @param name
	 * @return
	 */
	private String generateKeyForInstance(ActivityExecution execution,String name){
		String id = execution.getId();
		return name+"-"+id;
	}
	
    @Override
	public final void execute(ActivityExecution execution) throws Exception
	{
    	logger.info("(execution id: "+ execution.getId() +") - Executing: " + getExecutorDisplayName());
		logger.trace("(execution id: "+ execution.getId() +") - Pre Execute - Dumping out variable map, " + execution.getVariables().size() + " variables present");
		for(Entry<String, Object> entry : execution.getVariables().entrySet()){
			logger.trace("(execution id: "+ execution.getId() +") - Key: ["+entry.getKey()+"] - Value: [" +entry.getValue() + "]");
		}
		//  Save this id somewhere, or pass it to the outgoing asynchronous task, 
	    // as this will be needed to signal this activity later when the long running external task is complete	
    	String callBackId = execution.getId();
    	execution.setVariableLocal(WorkflowConstants.CALL_BACK_ID_LOCAL_VARIABLE, callBackId);
    	try{
    		//do data setup:
    		setupExecutionData(execution);
			//go and do everything we want to.
    		handleExecution(execution);
    	}
    	catch(Throwable t){
    		logger.error("Failed to execute " + this.getExecutorDisplayName() + " " + execution.getId() + " cause: "+ t,t);
    		throw new BpmnError(WorkflowErrorConstants.PROCESS_ERROR,"Failed to execute " + this.getExecutorDisplayName()+ " " + execution.getId() + " cause: "+ t);
    	}
	}
 
    @Override
	public final void signal(ActivityExecution execution, String signalName, Object signalData) throws Exception
	{
		//do all operations we want to do now that we've signalled a return
		//from the async operation
		try{
			//if there was an error, pick it up here. If the extending class needs to do something, then it must implement the following 
			//method:
			if(execution.hasVariableLocal(WorkflowConstants.ERROR_IN_EXTERNAL_EXECUTION)){
				logger.error("handling error call back from external process failure: "+ execution.getId());
				doPostExecutionErrorHandling(execution);
			}
			logger.info("(execution id: "+ execution.getId() +") - Performing Post Execution for - " + getExecutorDisplayName());
			doPostExecution(execution);
		}
    	catch(OpenshareException me){
    		logger.error("Failed to execute " + this.getExecutorDisplayName() + " " + execution.getId() + " cause: "+ me,me);
    		throw new BpmnError(WorkflowErrorConstants.PROCESS_ERROR,"Failed to execute " + this.getExecutorDisplayName()+ " " + execution.getId() + " cause: "+ me);
    	}
		//remove local calback id variable
		execution.removeVariableLocal(WorkflowConstants.CALL_BACK_ID_LOCAL_VARIABLE);
		//leave the execution
		logger.trace("(execution id: "+ execution.getId() +") - Post Execute - Dumping out variable map, " + execution.getVariables().size() + " variables present");
		for(Entry<String, Object> entry : execution.getVariables().entrySet()){
			logger.trace("(execution id: "+ execution.getId() +") - Key: ["+entry.getKey()+"] - Value: [" +entry.getValue() + "]");
		}
		
		logger.info("(execution id: "+ execution.getId() +") - Exiting - " + getExecutorDisplayName());
		leave(execution);
	}
	
    /**
     * do error handling on post execute. this is a generalised method, to do specific operations, 
     * override this in subclass.
     * @param errorObject
     * @throws WorkflowException
     */
	public void doPostExecutionErrorHandling(ActivityExecution execution) throws WorkflowException{
		logger.error("Post execution error handling activated, external process failed");
		throw new WorkflowException("post execute error detected, cannot continue");
	}
	
	/**
	 * do all the operations we'd want to do when the call returns.
	 * @throws WorkflowException 
	 */
	public abstract void doPostExecution(ActivityExecution execution) throws WorkflowException;
	
	/**
	 * Do any variable setup, pulling things from parameter map and other. 
	 * Convenience method added so that all config can be handled in one place for the command to run.
	 * @param arg0
	 * @throws WorkflowException
	 */
	public abstract void setupExecutionData(ActivityExecution arg0) throws WorkflowException;
	
	/**
	 * hand off to implementing subclass
	 * @param arg0
	 * @throws OpenshareException 
	 * @throws Exception
	 */
	protected abstract void handleExecution(ActivityExecution execution) throws OpenshareException;
	
	/**
	 * Convenience for naming the executor type
	 * @return
	 */
	protected abstract String getExecutorDisplayName();
}

