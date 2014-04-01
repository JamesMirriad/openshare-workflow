package com.openshare.service.base.rpc.impl.palyoad.mapping;

import com.openshare.service.base.rpc.impl.palyoad.OperationType;
/**
 * payload for a workflow trigger mapping
 * @author james.mcilroy
 *
 */
public class TriggerWorkflowMappingPayload {

	private String triggerName;
	
	private String workflow;
	
	private String id;
	
	private OperationType operation;

	public String getTriggerName() {
		return triggerName;
	}

	public void setTriggerName(String triggerName) {
		this.triggerName = triggerName;
	}

	public String getWorkflow() {
		return workflow;
	}

	public void setWorkflow(String workflow) {
		this.workflow = workflow;
	}

	public OperationType getOperation() {
		return operation;
	}

	public void setOperation(OperationType operation) {
		this.operation = operation;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
