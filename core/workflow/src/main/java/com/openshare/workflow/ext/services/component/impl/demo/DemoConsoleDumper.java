package com.openshare.workflow.ext.services.component.impl.demo;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.Expression;
import org.apache.log4j.Logger;

import com.openshare.service.base.exception.OpenshareException;
import com.openshare.workflow.ext.services.component.AbstractJavaDelegateService;

public class DemoConsoleDumper extends AbstractJavaDelegateService {

	private static final Logger logger = Logger.getLogger(DemoConsoleDumper.class);
	private Expression variableOne;
	private Expression variableTwo;
			
	@Override
	protected void handleExecution(DelegateExecution execution)
			throws OpenshareException {
		String variableOneString = (String) variableOne.getValue(execution);
		String variableTwoString = (String) variableTwo.getValue(execution);
		logger.info("***********************************************");
		logger.info("***********************************************");
		logger.info("CONSOLE DEMO DUMPER");
		logger.info("VARIABLE ONE: " + variableOneString);
		logger.info("VARIABLE TWO: " + variableTwoString);
		logger.info("***********************************************");
		logger.info("***********************************************");
		
	}

	@Override
	public String getExecutorDisplayName() {
		// TODO Auto-generated method stub
		return "Console Demo Dumper";
	}

	@Override
	public String getComponentXMLConfig() {
		return 
	  "<serviceTask id=\"${component-id}\" name=\"Console Demo Dumper\" activiti:class=\""+this.getClass().getName()+"\">"+
      "<extensionElements>"+
      "<activiti:field name=\"variableOne\">"+
      "<activiti:string>${value}</activiti:string>"+
      "</activiti:field>"+
      "<activiti:field name=\"variableTwo\">"+
      "<activiti:string>${value}</activiti:string>"+
      "</activiti:field>"+
      "</extensionElements>"+
      "</serviceTask>";
	}

}
