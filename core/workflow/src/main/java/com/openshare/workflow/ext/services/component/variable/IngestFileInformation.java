package com.openshare.workflow.ext.services.component.variable;

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
 * parses variables into map if the source operand is a file.
 * @author james.mcilroy
 *
 */
public class IngestFileInformation extends AbstractJavaDelegateService {

	private static final Logger logger = Logger.getLogger(IngestFileInformation.class);
	
	private Expression source;

	@Override
	protected void handleExecution(DelegateExecution execution)
			throws OpenshareException {
		String sourceFileUri = (String) source.getValue(execution);
		if(sourceFileUri==null || sourceFileUri.isEmpty() || sourceFileUri.equals(".")){
			sourceFileUri = (String)execution.getVariable(WorkflowConstants.SOURCE_FILE);
		}
		logger.info("source: " + sourceFileUri);
		try {
			FileSystemObject sourceFile = FileFactory.GetInstance().create(sourceFileUri); 
			if(!sourceFile.isFolder()){
				execution.setVariable(WorkflowConstants.SOURCE_FILE_EXT,sourceFile.getFileExtension());
				execution.setVariable(WorkflowConstants.SOURCE_FILE_NAME_NO_EXT,sourceFile.getFileNameWithoutExtension());
				execution.setVariable(WorkflowConstants.SOURCE_FILE_NAME,sourceFile.getFileName());
			}
		}
		catch (FileUtilException e) {
			throw new OpenshareException("Failed to move file, cause:",e);
		}
	}

	@Override
	public String getComponentXMLConfig() {
		return "<serviceTask id=\"${component-id}\" name=\"Ingest File Info\" activiti:class=\""+ this.getClass().getName()+ "\">" +
				"<extensionElements>" +
				"<activiti:field name=\"source\">" +
				"<activiti:string>${value}</activiti:string>" +
				"</activiti:field>" +
				"</extensionElements>" +
				"</serviceTask>";
	}

	@Override
	public String getExecutorDisplayName() {
		// TODO Auto-generated method stub
		return "Ingest File Information";
	}

}
