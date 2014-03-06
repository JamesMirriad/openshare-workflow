package com.openshare.service.base.response;

import javax.xml.bind.annotation.XmlRootElement;

import com.wordnik.swagger.annotations.ApiModel;

/**
 * status object to signify the status of the request 
 * processing in a response object
 * @author james.mcilroy
 *
 */
@ApiModel(value="RequestStatus", description="Request Status values")
@XmlRootElement
public enum RequestStatus {

	OK,
	ERROR,
	INVALID;
}
