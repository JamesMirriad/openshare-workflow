package com.openshare.workflow.persistence;

import java.io.IOException;
import java.util.Properties;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.apache.log4j.Logger;

import com.openshare.util.properties.PropertiesLoader;
/**
 * persistence service.
 * @author james.mcilroy
 *
 */
public class PersistenceService {
	
	private static final Logger logger = Logger.getLogger(PersistenceService.class);
	
	private static final String PERSISTENCE_PROP_FILE_NAME = "persistence.properties";

	private static EntityManagerFactory emf = null;
	
	private static class SingletonHolder { 
        public static final PersistenceService INSTANCE = new PersistenceService();
	}

	/**
	 * singleton access
	 * @return
	 */
	public static PersistenceService getInstance() {
	        return SingletonHolder.INSTANCE;
	}
	
	/**
	 * constructor that sets up the emf.
	 */
	private PersistenceService() {
		if(emf==null){
			Properties props = null;
			try {
				PropertiesLoader loader = new PropertiesLoader(PERSISTENCE_PROP_FILE_NAME);
				props = loader.getProps();
			} 
			catch (IOException e) {
				logger.error("failed to load config file: " + PERSISTENCE_PROP_FILE_NAME);
			}
			if(props!=null){
				emf = Persistence.createEntityManagerFactory("com.openshare.persistence.jpa",props);
			}
			else{
				emf = Persistence.createEntityManagerFactory("com.openshare.persistence.jpa");
			}
		}
	}
	
	/**
	 * get an entity manager
	 * @return
	 */
	public EntityManager createEntityManager() {
		if (emf == null) {
			throw new IllegalStateException("Context is not initialized yet.");
		}
		return emf.createEntityManager();
	}
	
	/**
	 * closes the Persistence Service
	 */
	public void close(){
		emf.close();
		emf = null;
	}
}
