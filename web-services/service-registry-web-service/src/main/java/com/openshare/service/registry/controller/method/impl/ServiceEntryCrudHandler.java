package com.openshare.service.registry.controller.method.impl;

import org.codehaus.jackson.map.ObjectMapper;

import com.openshare.service.registry.controller.exception.OpenShareException;
import com.openshare.service.registry.controller.method.MethodHandler;
import com.openshare.service.registry.model.Response;
import com.openshare.service.registry.model.StatusEnum;
import com.openshare.service.registry.model.dto.ServiceDto;
import com.openshare.service.registry.model.persist.ServiceEntry;

public abstract class ServiceEntryCrudHandler extends MethodHandler{

	protected ServiceDto getEntryFromPayload() throws OpenShareException{
		try{
			ObjectMapper mapper = new ObjectMapper();
			ServiceDto entry = mapper.convertValue(payload, ServiceDto.class);
			return entry;
		}
		catch(Exception e){
			throw new OpenShareException("failed to convert payload: " + payload);
		}
	}
	
	protected ServiceEntry convertDtoToEntry(ServiceDto dto){
		ServiceEntry entry = new ServiceEntry();
		entry.setId(dto.getId());
		entry.setResourceLocation(dto.getResourceLocation());
		String type = dto.getResourceLocation().split("://")[0];
		entry.setResourceType(type);
		entry.setName(dto.getName());
		entry.setOwner(dto.getOwner());
		//only assign if set
		if(dto.getStatus()!=null){
			entry.setStatus(dto.getStatus());
		}
		return entry;
	}
	
	protected ServiceEntry updateDtoToEntry(ServiceDto dto,ServiceEntry entry){
		entry.setResourceLocation(dto.getResourceLocation());
		String type = dto.getResourceLocation().split("://")[0];
		entry.setResourceType(type);
		entry.setName(dto.getName());
		entry.setOwner(dto.getOwner());
		//only assign if set
		if(dto.getStatus()!=null){
			entry.setStatus(dto.getStatus());
		}
		return entry;
	}
	
	protected Response createResponse(ServiceEntry entry){
		Response response = new Response();
		response.setPayload(entry);
		response.setTxid(transactionId);
		response.setStatus(StatusEnum.OK);
		
		return response;
	}
}
