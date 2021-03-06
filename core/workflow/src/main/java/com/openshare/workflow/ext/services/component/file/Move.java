package com.openshare.workflow.ext.services.component.file;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.Expression;
import org.apache.log4j.Logger;

import com.openshare.file.utils.FileFactory;
import com.openshare.file.utils.FileSystemObject;
import com.openshare.file.utils.exception.FileUtilException;
import com.openshare.service.base.exception.OpenshareException;
import com.openshare.workflow.ext.constants.WorkflowConstants;
import com.openshare.workflow.ext.services.component.AbstractJavaDelegateService;
/**
 * Move a file
 * @author james.mcilroy
 *
 */
public class Move extends AbstractJavaDelegateService {

	private static final Logger logger = Logger.getLogger(Move.class);

	private Expression source;
	private Expression target;
	private Expression targetVariableName;
	
	@Override
	protected void handleExecution(DelegateExecution execution)
			throws OpenshareException {
		String sourceFileUri = parseExpression(execution, (String) source.getValue(execution));
		//if there was no source file set as a variable, then assume that we are working on the main source file in 
		//this workflow
		if(sourceFileUri==null || sourceFileUri.isEmpty() || sourceFileUri.equals(".")){
			sourceFileUri = (String)execution.getVariable(WorkflowConstants.SOURCE_FILE);
		}
		String targetFileUri = parseExpression(execution, (String) target.getValue(execution));
		String targetFileVariable = parseExpression(execution, (String) targetVariableName.getValue(execution));
		logger.info("source: " + sourceFileUri);
		logger.info("target: " + targetFileUri);
		try {
			FileSystemObject sourceFile = FileFactory.GetInstance().create(sourceFileUri);
			FileSystemObject targetFile = FileFactory.GetInstance().create(targetFileUri);
			if(sourceFile.exists() && sourceFile.canRead()){
				sourceFile.moveTo(targetFile);
			}
			else{
				throw new OpenshareException("file " + sourceFileUri + " does not exist or we don't have read permissions");
			}
			logger.info("move completed");
			//set target variable name
			if(targetFileVariable!=null && !targetFileVariable.isEmpty() && !sourceFileUri.equals(".")){
				execution.setVariable(targetFileVariable, targetFile.getFullPathName());
			}
			else{
				execution.setVariable(WorkflowConstants.SOURCE_FILE, targetFile.getFullPathName());
			}
		} 
		catch (FileUtilException e) {
			throw new OpenshareException("Failed to move file, cause:",e);
		}
	}

	@Override
	public String getComponentXMLConfig() {
		return "<serviceTask id=\"${component-id}\" name=\"Move\" activiti:class=\""+ this.getClass().getName()+ "\">" +
				"<extensionElements>" +
				"<activiti:field name=\"source\">" +
				"<activiti:string>${value}</activiti:string>" +
				"</activiti:field>" +
				"<activiti:field name=\"target\">" +
				"<activiti:string>${value}</activiti:string>" +
				"</activiti:field>" +
				"<activiti:field name=\"targetVariableName\">" +
				"<activiti:string>${value}</activiti:string>" +
				"</activiti:field>" +
				"</extensionElements>" +
				"</serviceTask>";
	}

	@Override
	public String getExecutorDisplayName() {
		return "Move";
	}

}
