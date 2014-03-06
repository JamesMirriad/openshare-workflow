package com.openshare.service.web.workflow.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;

import com.openshare.workflow.conf.ActivitiHelper;

public class WorkflowStartupListener implements ServletContextListener{
/**
 * workflow start up listener, to initialize singletons at startup
 */
private static final Logger logger = Logger.getLogger(WorkflowStartupListener.class);
	
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		// Notification that the servlet context is about to be shut down.
		//TODO ----- shutdown hook here --------------
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		//initialise both FMS and the service registry on start up.
		logger.info("orchestration service starting:");
		@SuppressWarnings("unused")
		ActivitiHelper activiti = ActivitiHelper.getInstance();
		logger.info("orchestration service initialised:");	
	}
}
