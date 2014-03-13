package com.openshare.workflow.ext.services.component;
/**
 * Interface all components must implement
 * @author james.mcilroy
 *
 */
public interface IWorkflowComponent {

	public String getComponentXMLConfig();
	
	public String getExecutorDisplayName();
}
