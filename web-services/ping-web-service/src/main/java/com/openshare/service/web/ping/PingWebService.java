package com.openshare.service.web.ping;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.openshare.service.web.ping.dto.Request;
import com.openshare.service.web.ping.dto.Response;
/**
 * Service for ingest and for ingest related functionality 
 * @author james.mcilroy
 *
 */
@Produces({MediaType.APPLICATION_JSON})
@Path("")
public class PingWebService{

	private static final Logger logger = LoggerFactory.getLogger(PingWebService.class);
	
	/**
	 * signal an execution that it can continue (ie external process has finished)
	 * @param executionId
	 */
	@POST
	@Path("/")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public Response ping(Request request){
		Response response = new Response();
		response.setPayload("received message: " + request.getPayload() + " ,method: " + request.getMethod());
		return response;
	}
	
	/**
	 * signal an execution that it can continue (ie external process has finished)
	 * @param executionId
	 */
	@GET
	@Path("/hello/")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public Response hello(){
		Response response = new Response();
		response.setPayload("received message: " + "no message, get test" + " ,method: " + "none");
		return response;
	}
}
