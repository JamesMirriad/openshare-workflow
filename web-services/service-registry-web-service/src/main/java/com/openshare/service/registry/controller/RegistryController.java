package com.openshare.service.registry.controller;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.openshare.service.registry.controller.exception.OpenShareException;
import com.openshare.service.registry.controller.method.MethodFactory;
import com.openshare.service.registry.controller.method.MethodHandler;
import com.openshare.service.registry.model.Request;
import com.openshare.service.registry.model.Response;
import com.openshare.service.registry.model.StatusEnum;

@Produces({MediaType.APPLICATION_JSON})
@Path("")
public class RegistryController {
	
	/**
	 * serve a request
	 * @param request
	 * @return
	 */
	@POST
	@Path("/")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public Response serve(Request request){
		MethodHandler handler;
		try {
			handler = MethodFactory.getInstance().getMethodHandler(request.getTxid(), request.getMethod(), request.getPayload());
			return handler.handleExecution();
		} catch (OpenShareException e) {
			return generateErrorResponse(e);
		}
	}
	
	protected Response generateErrorResponse(Exception e){
		Response response = new Response();
		response.setStatus(StatusEnum.ERROR);
		response.setPayload(e.getLocalizedMessage());
		return response;
	}
}