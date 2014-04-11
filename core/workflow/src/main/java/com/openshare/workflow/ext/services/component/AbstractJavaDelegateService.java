package com.openshare.workflow.ext.services.component;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.activiti.engine.delegate.BpmnError;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.apache.log4j.Logger;

import com.openshare.service.base.exception.OpenshareException;
import com.openshare.workflow.ext.constants.WorkflowErrorConstants;
import com.openshare.workflow.util.VariableParser;

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
public abstract class AbstractJavaDelegateService implements JavaDelegate, IWorkflowComponent {

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
    		logger.error("Failed to execute " + this.getExecutorDisplayName() + " " + execution.getId() + " cause: "+t,t);
    		throw new BpmnError(WorkflowErrorConstants.PROCESS_ERROR, "Failed to execute " + this.getExecutorDisplayName() + " " + execution.getId() + " cause: "+ t);
    	}
		
		logger.trace("(execution id: "+ execution.getId() +") - Post Execute - Dumping out variable map, " + execution.getVariables().size() + " variables present");
		for(Entry<String, Object> entry : execution.getVariables().entrySet()){
			logger.trace("(execution id: "+ execution.getId() +") - Key: ["+entry.getKey()+"] - Value: [" +entry.getValue() + "]");
		}
		
		logger.info("(execution id: "+ execution.getId() +") - Exiting - " + getExecutorDisplayName());
	}

	/**
	 * parse the expression with the variables in the execution map and replace where necessary
	 * @param execution
	 * @param expression
	 * @return
	 */
	protected String parseExpression(DelegateExecution execution, String expression){
		//copy the command
		String parsedCommand = expression;
		//we need to find "variables" delimited by ${} for replacements and get them.
		Set<String> variables = VariableParser.parseExpression(expression);
		Map<String,Object> variableInstances = new HashMap<String,Object>();
		//try and find the required variables in the execution variable map
		for(String var : variables){
			if(execution.hasVariable(var)){
				if(execution.getVariable(var)!=null){
					variableInstances.put(var, execution.getVariable(var));
				}
			}
		}
		//now, cycle through them, call toString on the object, and do a replace for each one
		parsedCommand = VariableParser.replaceVariablesInExpression(expression, variableInstances);
		return parsedCommand;
	}
	
	/**
	 * hand off to implementing subclass
	 * @param arg0
	 * @throws Exception
	 */
	protected abstract void handleExecution(DelegateExecution arg0) throws OpenshareException;
	
	public abstract String getComponentXMLConfig();
	
	public abstract String getExecutorDisplayName();
}
