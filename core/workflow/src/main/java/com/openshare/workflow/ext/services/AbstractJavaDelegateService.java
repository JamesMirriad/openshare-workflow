package com.openshare.workflow.ext.services;

import java.util.Map.Entry;

import org.activiti.engine.delegate.BpmnError;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.apache.log4j.Logger;

import com.openshare.workflow.ext.constants.WorkflowErrorConstants;

/**
 * Class to execute a synchronous command. Use this if the operation
 * will return immediately, and when no external services are called.
 * 
 * If an operation that can hang is called in this, it will hang the UI 
 * on the front end whilst it is waiting. 
 * 
 * In fact, for pretty much all cases, it is better to use 
 * AbstractMirriadAsyncJavaDelegateService
 * and extend that.
 * @author james.mcilroy
 *
 */
public abstract class AbstractJavaDelegateService implements JavaDelegate {

	private static final Logger logger = Logger.getLogger(AbstractJavaDelegateService.class);

	public void execute(DelegateExecution execution) throws Exception {
		logger.info("(execution id: "+ execution.getId() +") - Executing: " + getExecutorDisplayName());
		logger.trace("(execution id: "+ execution.getId() +") - Pre Execute - Dumping out variable map, " + execution.getVariables().size() + " variables present");
		for(Entry<String, Object> entry : execution.getVariables().entrySet()){
			logger.trace("(execution id: "+ execution.getId() +") - Key: ["+entry.getKey()+"] - Value: [" +entry.getValue() + "]");
		}
		try{
			//go and do everything we want to.
    		handleExecution(execution);
    	}
    	catch(Throwable t){
    		logger.error("Failed to execute " + this.getExecutorDisplayName() + " " + execution.getId() + " cause: "+ t);
    		throw new BpmnError(WorkflowErrorConstants.PROCESS_ERROR, "Failed to execute " + this.getExecutorDisplayName() + " " + execution.getId() + " cause: "+ t);
    	}
		
		logger.trace("(execution id: "+ execution.getId() +") - Post Execute - Dumping out variable map, " + execution.getVariables().size() + " variables present");
		for(Entry<String, Object> entry : execution.getVariables().entrySet()){
			logger.trace("(execution id: "+ execution.getId() +") - Key: ["+entry.getKey()+"] - Value: [" +entry.getValue() + "]");
		}
		
		logger.info("(execution id: "+ execution.getId() +") - Exiting - " + getExecutorDisplayName());
	}
	
	/**
	 * hand off to implementing subclass
	 * @param arg0
	 * @throws Exception
	 */
	protected abstract void handleExecution(DelegateExecution arg0) throws OpenshareException;
	
	/**
	 * COnvenience for naming the executor type
	 * @return
	 */
	protected abstract String getExecutorDisplayName();
	
}
