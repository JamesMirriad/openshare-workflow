package com.openshare.service.web.workflow;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import com.openshare.service.base.exception.OpenshareException;
import com.openshare.service.base.rpc.OpenShareRequest;
import com.openshare.service.base.rpc.OpenShareResponse;
import com.openshare.service.base.rpc.StatusEnum;
import com.openshare.service.workflow.WorkflowService;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
/**
 * Service for orchestration allowing calls to the orchestration API via rest
 * @author james.mcilroy
 *
 */
@Api(value = "/api", description = "Workflow REST Api")
@Path("/api")
@Produces({MediaType.APPLICATION_JSON})
public class WorkflowWebService{
	
	private static final Logger logger = Logger.getLogger(WorkflowWebService.class);

	//handle on orchestration service
	WorkflowService workflowService;
	
	public WorkflowWebService(){
		//orchestration service is self configuring, so just need to instantiate it.
		workflowService = new WorkflowService();
	}
	
	@POST
	@Path("/run/")
	@Produces({MediaType.APPLICATION_JSON})
	public OpenShareResponse runCommand(OpenShareRequest request){
		try {
			return workflowService.runCommand(request);
		} catch (OpenshareException e) {
			OpenShareResponse response = new OpenShareResponse();
			response.setStatus(StatusEnum.ERROR);
			response.setPayload(e);
			return response;
		}
	}
	/*
	 * (non-Javadoc)
	 * @see com.openshare.service.base.web.MirriadWebServiceBase#getStatus()
	 */
	@GET
	@Path("/workflow/show/latest/{name}")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	@ApiOperation(value = "Get Workflow By Name", response = String.class)
	@Deprecated
	public OpenShareResponse getWorkflowByName(@PathParam("name") String name) {
		OpenShareResponse response = new OpenShareResponse();
		String model;
		try {
			model = workflowService.getProcessodel(name);
			response.setPayload(model);
			return response;
		} catch (Exception e) {
			throw new WebApplicationException(e,Response.Status.INTERNAL_SERVER_ERROR);
		}
	}
}
