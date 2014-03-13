package com.openshare.workflow.ext.services;

import java.util.HashMap;
import java.util.Map;

import com.openshare.service.base.exception.OpenshareException;
import com.openshare.workflow.ext.services.component.IWorkflowComponent;
import com.openshare.workflow.ext.services.component.impl.demo.DemoConsoleDumper;
/**
 * Enumerative class that contains all the workflow components that are accessible via the 
 * config call. All implement workflow components should be listed here so they can be used to create 
 * workflows by external systems.
 * @author james.mcilroy
 *
 */
public enum AvailableWorkflowComponents {

	DEMO_CONSOLE_DUMPER(DemoConsoleDumper.class);
	
	private final Class<? extends IWorkflowComponent> workflowComponentClass;
	
	private AvailableWorkflowComponents(Class<? extends IWorkflowComponent> workflowComponentClass){
		this.workflowComponentClass = workflowComponentClass;
	}

	public Class<? extends IWorkflowComponent> getWorkflowComponentClass() {
		return workflowComponentClass;
	}
	
	public String getComponentName() throws OpenshareException{
		try{
			IWorkflowComponent component = workflowComponentClass.newInstance();
			return component.getExecutorDisplayName();
		}
		catch(Throwable t){
			throw new OpenshareException("cannot instantiate class to get name");
		}
	}
	
	public static Map<String,String> getAvailableWorkflowComponents() throws OpenshareException{
		Map<String,String> componentConfigMap = new HashMap<String,String>();
		try{
			for(AvailableWorkflowComponents availableComponent : AvailableWorkflowComponents.values()){
				Class<? extends IWorkflowComponent> componentClass = availableComponent.getWorkflowComponentClass();
				IWorkflowComponent component = componentClass.newInstance();
				componentConfigMap.put(component.getExecutorDisplayName(), component.getComponentXMLConfig());
			}
			return componentConfigMap;
		}
		catch(Throwable t){
			throw new OpenshareException("Cannot list available components, cause:",t);
		}
	}
}
