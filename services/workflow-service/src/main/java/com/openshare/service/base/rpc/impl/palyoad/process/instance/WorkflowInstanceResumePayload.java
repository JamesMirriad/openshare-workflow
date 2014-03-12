package com.openshare.service.base.rpc.impl.palyoad.process.instance;
/**
 * for a resume of a workflow from external source.
 * The payload object inside this canbe anything and will 
 * be tailored to wat is being resumed.
 * @author james.mcilroy
 *
 */
public class WorkflowInstanceResumePayload {

	String processId;
	
	Object payload;

	public String getProcessId() {
		return processId;
	}

	public void setProcessId(String processId) {
		this.processId = processId;
	}

	public Object getPayload() {
		return payload;
	}

	public void setPayload(Object payload) {
		this.payload = payload;
	}
}
