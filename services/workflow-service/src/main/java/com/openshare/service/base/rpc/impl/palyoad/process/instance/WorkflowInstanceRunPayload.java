package com.openshare.service.base.rpc.impl.palyoad.process.instance;

public class WorkflowInstanceRunPayload {

	String workflow;
	
	String fileUri;

	public String getWorkflow() {
		return workflow;
	}

	public void setWorkflow(String workflow) {
		this.workflow = workflow;
	}

	public String getFileUri() {
		return fileUri;
	}

	public void setFileUri(String fileUri) {
		this.fileUri = fileUri;
	}
	
}
