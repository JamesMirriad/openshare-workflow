package com.openshare.workflow.ext.constants;


/**
 * Constants file for use across the app.
 * @author james.mcilroy
 *
 */
public class WorkflowConstants {
	
	//the default user
	public static final String DEFAULT_USER 						= "sysadmin";

	public static final String ERROR_IN_EXTERNAL_EXECUTION 			= "ErrorInExternalExecution";
	
	//definitions of constants in workflows
	public static final String SOURCE_FILE							= "sourceFile";
	public static final String SOURCE_FILE_NAME						= "sourceFileName";
	public static final String SOURCE_FILE_EXT						= "sourceFileExt";
	public static final String SOURCE_FILE_NAME_NO_EXT				= "sourceFileNameOnly";
	
	
	//Callback variable for async process
	public static final String CALLBACK_OBJECT			 			= "CallBackObjectVariable";

	//User variable
	public static final String PROCESS_OWNER 						= "processOwner";
	public static final String DEFAULT_PROCESS_OWNER 				= "ORCHESTRATION SYSTEM";
	
	//call back constant
	public static final String CALL_BACK_ID_LOCAL_VARIABLE 			= "callBackIdLocalVariable";
	
	//call back constant
	public static final String CALL_BACK_ID_PARENT_PROCESS_VARIABLE = "callBackIdParentProcessVariable";

}
