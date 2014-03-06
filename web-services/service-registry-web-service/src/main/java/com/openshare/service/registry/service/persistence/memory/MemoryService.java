package com.openshare.service.registry.service.persistence.memory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.openshare.service.registry.controller.exception.OpenShareException;
import com.openshare.service.registry.model.persist.ServiceEntry;


public class MemoryService {

	private static HashMap<String,ServiceEntry> serviceEntries= new HashMap<String,ServiceEntry>();
	
	private static class SingletonHolder { 
        public static final MemoryService INSTANCE = new MemoryService();
	}

	/**
	 * singleton access
	 * @return
	 */
	public static MemoryService getInstance() {
		return SingletonHolder.INSTANCE;
	}
	
	public List<ServiceEntry> list(){
		List<ServiceEntry> entries = new ArrayList<ServiceEntry>();
		for(Map.Entry<String, ServiceEntry> entry : serviceEntries.entrySet()){
			ServiceEntry service = entry.getValue();
			if(service!=null){
				entries.add(service);
			}
		}
		return entries;
	}
	
	public ServiceEntry add(ServiceEntry entry){
		//create a UUID
		String uuid = UUID.randomUUID().toString();
		entry.setId(uuid);
		serviceEntries.put(uuid, entry);
		return entry;
	}
	
	public ServiceEntry update(ServiceEntry entry) throws OpenShareException{
		String uuid = entry.getId();
		if(uuid==null || uuid.isEmpty() || serviceEntries.get(uuid) == null){
			throw new OpenShareException();
		}
		serviceEntries.put(uuid, entry);
		return entry;
	}
	
	public void delete(ServiceEntry entry) throws OpenShareException{
		String uuid = entry.getId();
		if(uuid==null || uuid.isEmpty() || serviceEntries.get(uuid) == null){
			throw new OpenShareException();
		}
		serviceEntries.remove(uuid);
	}
	
	public ServiceEntry read(String uuid) throws OpenShareException{
		if(uuid==null || uuid.isEmpty() || serviceEntries.get(uuid) == null){
			throw new OpenShareException();
		}
		ServiceEntry entry = serviceEntries.get(uuid);
		return entry;
	}
}
