package com.openshare.workflow.ext.services.component.exec;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.Expression;
import org.apache.log4j.Logger;

import com.openshare.service.base.exception.OpenshareException;
import com.openshare.workflow.ext.services.component.AbstractJavaDelegateService;

/**
 * @author james.mcilroy
 * Command Line Executor
 */
public class CommandLineExecutor extends AbstractJavaDelegateService {

	private static final Logger logger = Logger.getLogger(CommandLineExecutor.class);
	
	private Expression commandLine;
	
	/* (non-Javadoc)
	 * @see com.openshare.workflow.ext.services.component.AbstractJavaDelegateService#handleExecution(org.activiti.engine.delegate.DelegateExecution)
	 */
	@Override
	protected void handleExecution(DelegateExecution execution)
			throws OpenshareException {
		String commandLineValue = (String) commandLine.getValue(execution);
		logger.info("initial unparsed command: " + commandLineValue);
		//need to parse the command line.
		String executionCommandParsed = parseExpression(execution, commandLineValue);
		
		//now we've parsed it, we need to go off and run it.
		try {
			logger.info("running command: " + executionCommandParsed);
			//run the command
			Process p = Runtime.getRuntime().exec(executionCommandParsed);
		    BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
		    BufferedReader error = new BufferedReader(new InputStreamReader(p.getErrorStream()));
			//wait for command to complete
			p.waitFor();
			 
		    
		    int exitValue = p.exitValue();
		    //log output of operation
		    logger.info("Exit code of operation: " + exitValue);
		    String line = "";
		    while ((line = input.readLine())!= null) {
		    	logger.info(line);
			}
		    while ((line = error.readLine())!= null) {
		    	logger.info(line);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/* (non-Javadoc)
	 * @see com.openshare.workflow.ext.services.component.IWorkflowComponent#getComponentXMLConfig()
	 */
	@Override
	public String getComponentXMLConfig() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.openshare.workflow.ext.services.component.IWorkflowComponent#getExecutorDisplayName()
	 */
	@Override
	public String getExecutorDisplayName() {
		// TODO Auto-generated method stub
		return "Command Line Executor";
	}

}
