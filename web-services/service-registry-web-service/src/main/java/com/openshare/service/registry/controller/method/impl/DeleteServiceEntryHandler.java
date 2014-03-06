package com.openshare.service.registry.controller.method.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openshare.service.registry.controller.exception.OpenShareException;
import com.openshare.service.registry.model.Response;
import com.openshare.service.registry.model.persist.ServiceEntry;
import com.openshare.service.registry.service.persistence.memory.MemoryService;

public class DeleteServiceEntryHandler extends ServiceEntryCrudHandler {

	@Override
	public Response handleExecution() {
		ServiceEntry entry;
		try {
			ObjectMapper mapper = new ObjectMapper();
			String uuid = mapper.convertValue(payload, String.class);
			MemoryService service = MemoryService.getInstance();
			entry = service.read(uuid);
			service.delete(entry);
			Response response = createResponse(null);
			return response;
		} catch (OpenShareException e) {
			return generateErrorResponse("Internal Service Error, failed to add service entry from payload: " + payload);
		}
	}

}
