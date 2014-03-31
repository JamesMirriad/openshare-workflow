package com.openshare.service.base.rpc.impl.palyoad.process.instance;
/**
 * Payload to start a workflow
 * @author james.mcilroy
 *
 */
public class WorkflowInstanceTriggerPayload {

	private String triggerName;
	
	private String fileUri;

	public String getTriggerName() {
		return triggerName;
	}

	public void setTriggerName(String triggerName) {
		this.triggerName = triggerName;
	}

	public String getFileUri() {
		return fileUri;
	}

	public void setFileUri(String fileUri) {
		this.fileUri = fileUri;
	}
}
