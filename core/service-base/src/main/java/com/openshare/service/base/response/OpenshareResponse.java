package com.openshare.service.base.response;

import java.util.ArrayList;
import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;
/**
 * generalized response object
 * @author james.mcilroy
 *
 */
@ApiModel(value="MirriadResponse", description="Mirriad General Operation Response")
@XmlRootElement(name = "MirriadResponse")
@XmlAccessorType(XmlAccessType.FIELD)
public class OpenshareResponse{

	//name of originating service
	@ApiModelProperty(required = true, value = "calling service name")
	private String serviceOriginName;

	@ApiModelProperty(required = true, value = "status of the request processing")
	private RequestStatus requestStatus = RequestStatus.OK;
	
	@ApiModelProperty(required=true, value = "date and time of response")
	private Date responseTime;
	
	@ApiModelProperty(required = false,value = "Errors if all the required fields are missing")
	private ArrayList<String> errorList;
	
	
	public String getServiceOriginName() {
		return serviceOriginName;
	}

	public void setServiceOriginName(String serviceOriginName) {
		this.serviceOriginName = serviceOriginName;
	}

	public RequestStatus getRequestStatus() {
		return requestStatus;
	}

	public void setRequestStatus(RequestStatus requestStatus) {
		this.requestStatus = requestStatus;
	}

	/**
	 * convenience method to set the time to now
	 */
	public void setResponseTime(){
		//set to now
		this.responseTime = new Date();
	}
	
	public Date getResponseTime() {
		return responseTime;
	}

	public void setResponseTime(Date responseTime) {
		this.responseTime = responseTime;
	}

	public ArrayList<String> getErrorList() {
		return errorList;
	}

	public void setErrorList(ArrayList<String> errorList) {
		this.errorList = errorList;
	}
	
	/**
	 * add an error
	 * @param error
	 */
	public void addError(String error){
		if(errorList==null){
			errorList = new ArrayList<String>();
		}
		errorList.add(error);
	}
}
