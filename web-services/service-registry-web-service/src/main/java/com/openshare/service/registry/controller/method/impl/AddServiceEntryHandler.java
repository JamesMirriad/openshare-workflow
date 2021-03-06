package com.openshare.service.registry.controller.method.impl;

import com.openshare.service.registry.controller.exception.OpenShareException;
import com.openshare.service.registry.model.Response;
import com.openshare.service.registry.model.dto.ServiceDto;
import com.openshare.service.registry.model.persist.ServiceEntry;
import com.openshare.service.registry.service.persistence.memory.MemoryService;

public class AddServiceEntryHandler extends ServiceEntryCrudHandler {

	@Override
	public Response handleExecution() {
		ServiceEntry entry;
		try {
			ServiceDto entryDto = getEntryFromPayload();
			//create actual entry object.
			MemoryService service = MemoryService.getInstance();
			entry = convertDtoToEntry(entryDto);
			entry = service.add(entry);
			Response response = createResponse(entry);
			return response;
		} catch (OpenShareException e) {
			return generateErrorResponse("Internal Service Error, failed to add service entry from payload: " + payload);
		}
	}

}
