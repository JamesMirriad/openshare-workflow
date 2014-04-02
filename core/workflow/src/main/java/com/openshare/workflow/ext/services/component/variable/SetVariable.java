package com.openshare.workflow.ext.services.component.variable;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.Expression;
import org.apache.log4j.Logger;

import com.openshare.service.base.exception.OpenshareException;
import com.openshare.workflow.ext.services.component.AbstractJavaDelegateService;
/**
 * Component which just sets a variable
 * @author james.mcilroy
 *
 */
public class SetVariable extends AbstractJavaDelegateService{
	private static final Logger logger = Logger.getLogger(SetVariable.class);

	private Expression variableName;
	private Expression value;
	
	@Override
	protected void handleExecution(DelegateExecution execution)
			throws OpenshareException {
		String variableNameVal = parseExpression(execution, (String) variableName.getValue(execution));
		String valueVar = parseExpression(execution, (String) value.getValue(execution));
		execution.setVariable(variableNameVal, valueVar);		
	}

	@Override
	public String getComponentXMLConfig() {
		return "<serviceTask id=\"${component-id}\" name=\"Set Variable\" activiti:class=\""+ this.getClass().getName()+ "\">" +
				"<extensionElements>" +
				"<activiti:field name=\"variableName\">" +
				"<activiti:string>${value}</activiti:string>" +
				"</activiti:field>" +
				"<activiti:field name=\"value\">" +
				"<activiti:string>${value}</activiti:string>" +
				"</activiti:field>" +
				"</extensionElements>" +
				"</serviceTask>";
	}

	@Override
	public String getExecutorDisplayName() {
		return "Set Variable";
	}
}