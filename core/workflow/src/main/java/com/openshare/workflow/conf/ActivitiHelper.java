package com.openshare.workflow.conf;

import org.activiti.engine.ProcessEngine;
import org.apache.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;
/**
 * class to make sure we have an instance of the process engine available for our custom extensions
 * spring loads up shared config into a singleton so we only need one place to point at the correct DB 
 * which is built in via spring injection.
 * @author james.mcilroy
 *
 */
public class ActivitiHelper {
	private static final Logger logger = Logger.getLogger(ActivitiHelper.class);
	
	private ProcessEngine processEngine;

	private ActivitiHelper() {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
				"/com/openshare/workflow/conf/activiti-standalone-context.xml");
		ProcessEngine processEngineLoad = ((ProcessEngine) context.getBean("processEngine"));
		setProcessEngine(processEngineLoad);
	}

	private static class SingletonHolder { 
        public static final ActivitiHelper INSTANCE = new ActivitiHelper();
	}

	/**
	 * singleton access
	 * @return
	 */
	public static ActivitiHelper getInstance() {
	        return SingletonHolder.INSTANCE;
	}
	
	public ProcessEngine getProcessEngine(){
		return processEngine;
	}
	
	public void setProcessEngine(ProcessEngine processEngine){
		this.processEngine = processEngine;
	}
}
