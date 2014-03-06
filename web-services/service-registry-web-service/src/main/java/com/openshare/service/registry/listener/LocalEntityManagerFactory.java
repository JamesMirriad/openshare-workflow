package com.openshare.service.registry.listener;

/**
 * servlet context listener that starts up the persistence hook in the persistence service
 * and also shuts it down when server context is destroyed (shutdown)
 * @author james.mcilroy
 *
 */
public class LocalEntityManagerFactory{// implements ServletContextListener {
	
//	private static final Logger logger = LoggerFactory.getLogger(LocalEntityManagerFactory.class);
//	
////	@Override
////	public void contextInitialized(ServletContextEvent event) {
////		//this causes the initialization of the persistence service on startup, even if the service
////		//variable is not used.
////		PersistenceService service = PersistenceService.getInstance();
////	}
////
////	@Override
////	public void contextDestroyed(ServletContextEvent event) {
////		PersistenceService.getInstance().close();
////	}
//
//	/**
//	 * get an entity manager
//	 * @return
//	 */
//	public static EntityManager createEntityManager() {
//		return PersistenceService.getInstance().createEntityManager();
//	}
//
//	public void contextInitialized(ServletContextEvent sce) {
//		// TODO Auto-generated method stub
//		
//	}
//	
//	public void contextDestroyed(ServletContextEvent sce) {
//		// TODO Auto-generated method stub
//		
//	}
}