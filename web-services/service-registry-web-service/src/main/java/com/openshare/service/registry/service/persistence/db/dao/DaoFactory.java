package com.openshare.service.registry.service.persistence.db.dao;

import com.openshare.service.registry.service.persistence.db.dao.impl.jpa.ServiceEntryDaoJpaImpl;


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
	public ServiceEntryDao getServiceEntryDao(){
		return new ServiceEntryDaoJpaImpl();
	}
}
