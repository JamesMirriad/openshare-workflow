package com.openshare.service.registry.controller.method.impl;

import java.util.List;

import com.openshare.service.registry.controller.method.MethodHandler;
import com.openshare.service.registry.model.Response;
import com.openshare.service.registry.model.StatusEnum;
import com.openshare.service.registry.model.persist.ServiceEntry;
import com.openshare.service.registry.service.persistence.memory.MemoryService;

public class ListAllServicesHandler extends MethodHandler {

	@Override
	public Response handleExecution() {
		MemoryService service = MemoryService.getInstance();
		List<ServiceEntry> allEntries = service.list();
		return createResponse(allEntries);
	}

	protected Response createResponse(List<ServiceEntry> entry){
		Response response = new Response();
		response.setTxid(transactionId);
		try{
			if(entry!=null && entry.size() > 0){
				response.setPayload(entry);
			}
			else{
				response.setPayload(null);
			}
			response.setStatus(StatusEnum.OK);
		} 
		catch (Exception e) {
			response.setStatus(StatusEnum.ERROR);
			response.setPayload("Internal server error");
		}
		return response;
	}
}
