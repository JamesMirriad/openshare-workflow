package com.openshare.workflow.persistence;

import com.openshare.workflow.persistence.dao.TriggerWorkflowMappingDao;
import com.openshare.workflow.persistence.dao.impl.TriggerWorkflowMappingDaoJpaImpl;


/**
 * simple DAO factory
 * @author james.mcilroy
 *
 */
public class DaoFactory {

	private static class SingletonHolder { 
        public static final DaoFactory INSTANCE = new DaoFactory();
	}

	/**
	 * singleton access
	 * @return
	 */
	public static DaoFactory getInstance() {
	        return SingletonHolder.INSTANCE;
	}
	
	/**
	 * private constructor for singleton
	 */
	private DaoFactory(){}
	
	/**
	 * gets a file DAO
	 * @return
	 */
	public TriggerWorkflowMappingDao getTriggerWorkflowMappingDao(){
		return new TriggerWorkflowMappingDaoJpaImpl();
	}
}
