package com.openshare.service.base;

import javax.xml.bind.annotation.XmlRootElement;

import com.wordnik.swagger.annotations.ApiModel;

/**
 * list of statusses that a service can have when queried.
 * @TODO add possibles
 * @author james.mcilroy
 *
 */
@ApiModel(value="ServiceStatus", description="Mirriad Service Status")
@XmlRootElement
public enum ServiceStatus {

	ERROR,
	SUSPENDED,
	AVAILABLE;
}
