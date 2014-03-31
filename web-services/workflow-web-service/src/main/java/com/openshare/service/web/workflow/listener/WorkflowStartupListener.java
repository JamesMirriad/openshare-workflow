package com.openshare.service.web.workflow.listener;

import javax.persistence.EntityManager;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;

import com.openshare.workflow.conf.ActivitiHelper;
import com.openshare.workflow.persistence.PersistenceService;

public class WorkflowStartupListener implements ServletContextListener{
/**
 * workflow start up listener, to initialize singletons at startup
 */
private static final Logger logger = Logger.getLogger(WorkflowStartupListener.class);

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		//initialise both FMS and the service registry on start up.
		logger.info("orchestration service starting:");
		@SuppressWarnings("unused")
		PersistenceService service = PersistenceService.getInstance();
		ActivitiHelper activiti = ActivitiHelper.getInstance();
		logger.info("orchestration service initialised:");	
	}
	
	@Override
	public void contextDestroyed(ServletContextEvent event) {
		PersistenceService.getInstance().close();
	}
	
	/**
	 * get an entity manager
	 * @return
	 */
	public static EntityManager createEntityManager() {
		return PersistenceService.getInstance().createEntityManager();
	}
}
