package com.openshare.service.base.comms;

import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;

import com.openshare.service.base.exception.OpenshareServiceException;
import com.openshare.service.base.request.OpenshareRequest;
import com.openshare.service.base.response.OpenshareResponse;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
/**
 * Utility client for rest calls.
 * @author james.mcilroy
 *
 */
public class RestClient {
	
	private static final Logger logger = Logger.getLogger(RestClient.class);
	
	private static final int MILLISECONDS = 1000;
	public static final int DEFAULT_READ_TIMEOUT = 30;
	public static final int DEFAULT_CONNECT_TIMEOUT = 30;
	/**
	 * Do a post request on a service.
	 * @param request
	 * @param responseClass
	 * @param resource
	 * @param path
	 * @return
	 * @throws OpenshareServiceException 
	 */
	public static OpenshareResponse doPost(OpenshareRequest request,Class<? extends OpenshareResponse> responseClass,WebResource resource,String path) throws OpenshareServiceException{
		//split path to programmatically construct the path.
		if(path!=null && !path.isEmpty()){
			resource = resource.path(path);
			logger.debug("POST: uri for call: " + resource.getURI().getRawPath());
		}
		ClientResponse response = resource.type(MediaType.APPLICATION_JSON)
		.entity(request)
		.accept(MediaType.APPLICATION_JSON)
		.post(ClientResponse.class);
		//examine status code
		int statusCode = response.getStatus();
		logger.debug("request status code:" + statusCode + " for call: POST: " + resource.getURI().getRawPath());
		if(statusCode >= 400){
			String error = response.getEntity(String.class);
			logger.error("failed to perform request, code: " + statusCode + " cause: " + error);
			throw new OpenshareServiceException("failed to perform request, code: " + statusCode + " cause: " + error);
		}
		else if(statusCode==204){
			return null;
		}
		else{		
			// get history as JSON
			OpenshareResponse operationResponse = response.getEntity(responseClass);		
			return operationResponse;
		}
	}
	
	/**
	 * Do a post request on a service.
	 * @param request
	 * @param responseClass
	 * @param resource
	 * @param path
	 * @return
	 * @throws OpenshareServiceException 
	 */
	public static OpenshareResponse doPut(OpenshareRequest request,Class<? extends OpenshareResponse> responseClass,WebResource resource,String path) throws OpenshareServiceException{
		//split path to programmatically construct the path.
		if(path!=null && !path.isEmpty()){
			resource = resource.path(path);
			logger.debug("PUT: uri for call: " + resource.getURI().getRawPath());
		}

		ClientResponse response = resource.type(MediaType.APPLICATION_JSON)
		.entity(request)
		.accept(MediaType.APPLICATION_JSON)
		.put(ClientResponse.class);
		//examine status code
		int statusCode = response.getStatus();
		logger.debug("request status code:" + statusCode + " for call: PUT: " + resource.getURI().getRawPath());
		
		if(statusCode >= 400){
			String error = response.getEntity(String.class);
			logger.error("failed to perform request, cause: " + error);
			throw new OpenshareServiceException("failed to perform request, cause: " + error);
		}
		else if(statusCode==204){
			return null;
		}
		else{		
			// get history as JSON
			OpenshareResponse operationResponse = response.getEntity(responseClass);		
			return operationResponse;
		}
	}
	
	/**
	 * get request
	 * @param responseClass
	 * @param resource
	 * @param path
	 * @return
	 * @throws OpenshareServiceException 
	 */
	public static OpenshareResponse doGet(Class<? extends OpenshareResponse> responseClass,WebResource resource,String path) throws OpenshareServiceException{
		//split path to programmatically construct the path.
		if(path!=null && !path.isEmpty()){
			resource = resource.path(path);
			logger.debug("GET: uri for call: " + resource.getURI().getRawPath());
		}
		ClientResponse response = resource.type(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.get(ClientResponse.class);
		//examine status code
		int statusCode = response.getStatus();
		logger.debug("request status code:" + statusCode + " for call: GET: " + resource.getURI().getRawPath());
		if(statusCode >= 400){
			String error = response.getEntity(String.class);
			logger.error("failed to perform request, cause: " + error);
			throw new OpenshareServiceException("failed to perform request, cause: " + error);
		}
		else if(statusCode==204){
			return null;
		}
		else{		
			// get history as JSON
			OpenshareResponse responseEntity = response.getEntity(responseClass);
			return responseEntity;
		}
	}
	
	/**
	 * get request
	 * @param responseClass
	 * @param resource
	 * @param path
	 * @return
	 * @throws OpenshareServiceException 
	 */
	public static void doDelete(Class<? extends OpenshareResponse> responseClass,WebResource resource,String path) throws OpenshareServiceException{
		//split path to programmatically construct the path.
		if(path!=null && !path.isEmpty()){
			resource = resource.path(path);
			logger.debug("DELETE: uri for call: " + resource.getURI().getRawPath());
		}
		ClientResponse response = resource.type(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.delete(ClientResponse.class);
		//examine status code
		int statusCode = response.getStatus();
		logger.debug("request status code:" + statusCode + " for call: DELETE: " + resource.getURI().getRawPath());
		if(statusCode >= 400){
			String error = response.getEntity(String.class);
			logger.error("failed to perform request, cause: " + error);
			throw new OpenshareServiceException("failed to perform request, cause: " + error);
		}
	}
	
	/**
	 * get a web resource of a specified endpoint. will try to look up and compare the requested endpoint
	 * to exiting services, if not found then throws an error.
	 * @param serviceEndPoint
	 * @return
	 * @throws OpenshareServiceException
	 */
	public static WebResource getService(String serviceEndPoint) throws OpenshareServiceException{
		//we need to find out if this service is registered, so we need to do a scan to figure this out. 
		//Stops bad services being requested.
		Integer readTimeOut = DEFAULT_READ_TIMEOUT * MILLISECONDS;
		Integer connectTimeOut = DEFAULT_CONNECT_TIMEOUT * MILLISECONDS;
		
		ClientConfig config = new DefaultClientConfig();
		Client client = Client.create(config);
		client.setReadTimeout(readTimeOut);
		client.setConnectTimeout(connectTimeOut);
		client.setFollowRedirects(true);
		//obtain web resource
		WebResource webService = client.resource(serviceEndPoint);
		return webService;
		
	}
	
	/**
	 * check two end points to see if they are the same.
	 * if the end points to check are null or empty, returns false.
	 * strips any trailing "/" characters of the checks.
	 * Matches if uri's are the same, or if the endpoint check prefixed with http / https matches.
	 * @param serviceEndPoint
	 * @param serviceEndPointCheck
	 * @param secure
	 * @return
	 */
	private static boolean areEndPointsTheSame(String serviceEndPoint,String serviceEndPointCheck,boolean secure){
		if(serviceEndPoint==null || serviceEndPoint.isEmpty() || serviceEndPointCheck==null || serviceEndPointCheck.isEmpty()){
			//no data, just return false.
			return false;
		}
		while(serviceEndPoint.endsWith("/")){
			//strip trailing "/" characters
			serviceEndPoint = serviceEndPoint.substring(0, serviceEndPoint.length() - 1);
		}
		while(serviceEndPointCheck.endsWith("/")){
			//strip trailing "/" characters
			serviceEndPointCheck = serviceEndPointCheck.substring(0, serviceEndPoint.length() - 1);
		}
		//check as normal or with http / https in front.
		if(serviceEndPoint.equalsIgnoreCase(serviceEndPointCheck) || 
				serviceEndPoint.equalsIgnoreCase((secure ? "https://" : "http://") + serviceEndPointCheck)){
			return true;
		}
		return false;
	}
	

	/**
	 * Get a web resource for a service as defined by a uri, port, and whether we want a secure connection.
	 * @param uri
	 * @param port
	 * @param secure
	 * @return
	 */
	public static WebResource getService(String uri,Integer port,boolean secure){
		//create the client
		ClientConfig config = new DefaultClientConfig();
		Client client = Client.create(config);
		//build uri
		uri = ((secure) ? "https://" : "http://") + uri;
		if(port!=null){
			uri = uri.concat(":" + port);
		}
		//obtain web resource
		WebResource service = client.resource(uri);
		return service;
	}	
	
	/**
	 * Overloaded doGet to accept generalised parameters
	 * @param resource
	 * @param path
	 * @return
	 * @throws OpenshareServiceException
	 */
	public static ClientResponse doGet(WebResource resource,String path) throws OpenshareServiceException{
		//split path to programmatically construct the path.
		if(path!=null && !path.isEmpty()){
			resource = resource.path(path);
			logger.debug("GET: uri for call: " + resource.getURI().getRawPath()+path);
		}
		ClientResponse response = resource.type(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.get(ClientResponse.class);
		//examine status code
		int statusCode = response.getStatus();
		logger.debug("request status code:" + statusCode + " for call: GET: " + resource.getURI().getRawPath()+path);
		if(statusCode >= 400){
			String error = response.getEntity(String.class);
			logger.error("failed to perform request, cause: " + error);
			throw new OpenshareServiceException("failed to perform request, cause: " + error);
		}
		else if(statusCode==204){
			return null;
		}
		else{		
			return response;
		}
	}
	
	/**
	 * This method is generalised to accept all request objects and returns generic response
	 * @param request
	 * @param resource
	 * @param path
	 * @return
	 * @throws OpenshareServiceException
	 */
	public static ClientResponse doPost(Object request,WebResource resource,String path) throws OpenshareServiceException{
		//split path to programmatically construct the path.
		if(path!=null && !path.isEmpty()){
			resource = resource.path(path);
			logger.debug("POST: uri for call: " + resource.getURI().getRawPath());
		}
		ClientResponse response = resource.type(MediaType.APPLICATION_JSON)
		.accept(MediaType.APPLICATION_JSON)
		.post(ClientResponse.class,request);
		//examine status code
		int statusCode = response.getStatus();
		logger.debug("request status code:" + statusCode + " for call: POST: " + resource.getURI().getRawPath());
		if(statusCode >= 400){
			String error = response.getEntity(String.class);
			logger.error("failed to perform request, cause: " + error);
			throw new OpenshareServiceException("failed to perform request, cause: " + error);
		}
		else if(statusCode==204){
			return null;
		}
		else{		
			return response;
		}
	}
	
	/**
	 * Overloaded doPut to accept generalised parameters
	 * @param request
	 * @param resource
	 * @param path
	 * @return
	 * @throws OpenshareServiceException
	 */
	public static ClientResponse doPut(Object request,WebResource resource,String path) throws OpenshareServiceException{
		//split path to programmatically construct the path.
		if(path!=null && !path.isEmpty()){
			resource = resource.path(path);
			logger.debug("POST: uri for call: " + resource.getURI().getRawPath()+path);
		}
		ClientResponse response = resource.type(MediaType.APPLICATION_JSON)
		.accept(MediaType.APPLICATION_JSON)
		.put(ClientResponse.class,request);
		//examine status code
		int statusCode = response.getStatus();
		logger.debug("request status code:" + statusCode + " for call: POST: " + resource.getURI().getRawPath()+path);
		if(statusCode >= 400){
			String error = response.getEntity(String.class);
			logger.error("failed to perform request, cause: " + error);
			throw new OpenshareServiceException("failed to perform request, cause: " + error);
		}
		else if(statusCode==204){
			return null;
		}
		else{		
			return response;
		}
	}
	
}
