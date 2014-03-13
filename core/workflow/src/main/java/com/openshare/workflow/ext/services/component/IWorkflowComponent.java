package com.openshare.workflow.ext.services.component;
/**
 * Interface all components must implement, to allow for config to
 * be generated externally.
 * @author james.mcilroy
 *
 */
public interface IWorkflowComponent {

	public String getComponentXMLConfig();
	
	public String getExecutorDisplayName();
}
