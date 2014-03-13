package com.openshare.test.workflow.component;

import static org.junit.Assert.fail;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import com.openshare.workflow.ext.services.AvailableWorkflowComponents;
/**
 * Tests the generation of component config ad makes sure 
 * that no newly added config fails this step
 * @author james.mcilroy
 *
 */
public class TestComponentMapGeneration {
	
	@Test
	/**
	 * All comonents MUST be able to be instantiated
	 */
	public void testInstantiaton() {
		try{
			Map<String,String> components = AvailableWorkflowComponents.getAvailableWorkflowComponents();
			for(Map.Entry<String, String> entry : components.entrySet()){
				System.out.println("name: " + entry.getKey() + "\nXML config:\n" + entry.getValue() + " \n\n");
			}
		}
		catch(Throwable t){
			fail("failed!!!!"+t);
		}
	}
	
	@Test
	/**
	 * Names of all components MUST be unique
	 */
	public void testUniqueNames() {
		Set<String> names = new HashSet<String>();
		try{
			for(AvailableWorkflowComponents comp : AvailableWorkflowComponents.values()){
				String name = comp.getComponentName();
				if(names.contains(name)){
					fail("Name " + name + " already exists for another component!");
				}
				else{
					names.add(name);
				}
			}
		}
		catch(Throwable t){
			fail("failed!!!!"+t);
		}
		
	}

}
