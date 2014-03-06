package com.openshare.service.base.request;

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 * basic base class for an object to call a service. has the service name in it
 * @author james.mcilroy
 *
 */
@ApiModel(value="MirriadRequest", description="Mirriad Generic Request")
@XmlRootElement(name = "MirriadRequest")
@XmlAccessorType(XmlAccessType.FIELD)
public class OpenshareRequest {
	
	//name of originating service
	@ApiModelProperty(required = true, value = "calling service name")
	private String serviceOriginName;
	
	@ApiModelProperty(required = true, value = "end point of service to return to")
	private String callBackEndPoint;
	
	@ApiModelProperty(required = true, value = "call back reference id")
	private String callBackReferenceId;
	
	@ApiModelProperty(required=true, value = "date and time of request")
	private Date requestTime = new Date();
	
	
	public String getServiceOriginName() {
		return serviceOriginName;
	}
	public void setServiceOriginName(String serviceOriginName) {
		this.serviceOriginName = serviceOriginName;
	}
	public String getCallBackEndPoint() {
		return callBackEndPoint;
	}
	public void setCallBackEndPoint(String callBackEndPoint) {
		this.callBackEndPoint = callBackEndPoint;
	}
	public String getCallBackReferenceId() {
		return callBackReferenceId;
	}
	public void setCallBackReferenceId(String callBackReferenceId) {
		this.callBackReferenceId = callBackReferenceId;
	}
	/**
	 * convenience method to set the time to now
	 */
	public void setRequestTime(){
		//set to now
		this.requestTime = new Date();
	}
	public Date getRequestTime() {
		return requestTime;
	}
	public void setRequestTime(Date requestTime) {
		this.requestTime = requestTime;
	}	
}
