package com.openshare.service.registry.service.persistence.db.dao;

import java.util.List;

import com.openshare.service.registry.model.persist.ServiceEntry;
/**
 * Resource DAOinterface
 * @author james.mcilroy
 *
 */
public interface ServiceEntryDao extends GenericDao<ServiceEntry> {

	public List<ServiceEntry> findByOwner(String owner);
	
	public List<ServiceEntry> findByType(String type);
	
	public ServiceEntry findByOwnerAndLocation(String owner,String location);
}
