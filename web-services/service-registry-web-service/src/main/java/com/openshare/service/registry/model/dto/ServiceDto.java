package com.openshare.service.registry.model.dto;

import com.openshare.service.registry.model.ServiceStatusEnum;

public class ServiceDto {

	private String owner;
	private String resourceLocation;
	private String id;
	private String name;
	private ServiceStatusEnum status;
	
	public ServiceStatusEnum getStatus() {
		return status;
	}
	public void setStatus(ServiceStatusEnum status) {
		this.status = status;
	}
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
	public String getResourceLocation() {
		return resourceLocation;
	}
	public void setResourceLocation(String resourceLocation) {
		this.resourceLocation = resourceLocation;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}	
}
