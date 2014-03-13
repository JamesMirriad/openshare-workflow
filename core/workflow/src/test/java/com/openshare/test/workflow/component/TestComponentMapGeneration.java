package com.openshare.test.workflow.component;

import static org.junit.Assert.fail;

import java.util.Map;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.internal.runners.statements.Fail;

import com.openshare.workflow.ext.services.AvailableWorkflowComponents;
import com.openshare.workflow.ext.services.component.impl.demo.DemoConsoleDumper;

public class TestComponentMapGeneration {
	
	@Test
	public void test() {
		try{
			Map<String,String> components = AvailableWorkflowComponents.getAvailableWorkflowComponents();
			for(Map.Entry<String, String> entry : components.entrySet()){
				System.out.println("name: " + entry.getKey() + " XML config:\n" + entry.getValue() + " \n\n");
			}
		}
		catch(Throwable t){
			fail("failed!!!!"+t);
		}
	}

}
