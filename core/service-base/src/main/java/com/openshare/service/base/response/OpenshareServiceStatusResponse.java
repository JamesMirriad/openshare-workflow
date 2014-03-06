package com.openshare.service.base.response;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import com.openshare.service.base.ServiceStatus;
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;
/**
 * response object to return details about the service (common details and stats)
 * @author james.mcilroy
 *
 */
@ApiModel(value="MirriadServiceStatusResponse", description="Mirriad General Service Status Response")
@XmlRootElement(name = "MirriadServiceStatusResponse")
@XmlAccessorType(XmlAccessType.FIELD)
public class OpenshareServiceStatusResponse extends OpenshareResponse{

	@ApiModelProperty(required = true, notes = "status of the service")
	private ServiceStatus status = ServiceStatus.AVAILABLE;

	public ServiceStatus getStatus() {
		return status;
	}

	public void setStatus(ServiceStatus status) {
		this.status = status;
	}
}
