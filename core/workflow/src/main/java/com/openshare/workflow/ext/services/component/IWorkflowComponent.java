package com.openshare.workflow.ext.services.component;
/**
 * Interface all components must implement, to allow for config to
 * be generated externally.
 * @author james.mcilroy
 *
 */
public interface IWorkflowComponent {

	/**
	 * retrieve the configuration XML for this component
	 * @return
	 */
	public String getComponentXMLConfig();
	
	/**
	 * gets the executor display name for this component
	 * @return
	 */
	public String getExecutorDisplayName();
}
