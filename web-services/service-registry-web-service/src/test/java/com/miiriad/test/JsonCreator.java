package com.miiriad.test;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.openshare.service.registry.model.Request;
import com.openshare.service.registry.model.dto.ServiceDto;
import com.openshare.service.registry.model.persist.ServiceEntry;

public class JsonCreator {

	
	public static void main(String[] args) throws JsonGenerationException, JsonMappingException, IOException{
		Request request = new Request();
		request.setMethod("list");
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(request);
		System.out.print(jsonString);
		System.out.print("\n\n");
		request = new Request();
		request.setMethod("add");
		ServiceDto entry = new ServiceDto();
		entry.setOwner("usr://mirriad.com/james.mcilroy/");
		entry.setResourceLocation("gpu://www.mirriad.com/services/gpu/cluster/1/");
		entry.setName("Service Name");
		
		mapper = new ObjectMapper();
		jsonString = mapper.writeValueAsString(entry);
		request.setPayload(jsonString);
		jsonString = mapper.writeValueAsString(request);
		System.out.print(jsonString);
		System.out.print("\n\n");
		request = new Request();
		request.setMethod("update");
		entry = new ServiceDto();
		entry.setOwner("usr://mirriad.com/james.mcilroy/");
		entry.setResourceLocation("gpu://www.mirriad.com/services/gpu/cluster/1/");
		entry.setName("Service Name");
		entry.setId("uuid-goes-here");
		entry.setOwner("usr://mirriad.com/james.mcilroy/");
		entry.setResourceLocation("gpu://www.mirriad.com/services/gpu/cluster/1/");
		
		mapper = new ObjectMapper();
		jsonString = mapper.writeValueAsString(entry);
		request.setPayload(jsonString);
		jsonString = mapper.writeValueAsString(request);
		System.out.print(jsonString);
		System.out.print("\n\n");
	}
}
