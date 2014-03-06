package com.openshare.service.registry.model.persist;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.openshare.service.registry.model.ServiceStatusEnum;

@Entity
@Table(name = "service_entry")
public class ServiceEntry {//extends OpenSharePersistableObject{

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	private String id;

	@Column(name="owner", nullable = false)
	private String owner;
	
	@Column(name="resourceLocation", nullable = false)
	private String resourceLocation;

	@Column(name="resourceType", nullable = false)
	private String resourceType;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ServiceStatusEnum getStatus() {
		return status;
	}

	public void setStatus(ServiceStatusEnum status) {
		this.status = status;
	}

	@Column(name="name", nullable = false)
	private String name;
	
	@Column(name="status", nullable = false)
	@Enumerated(EnumType.STRING)
	ServiceStatusEnum status = ServiceStatusEnum.SUSPENDED;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public String getResourceType() {
		return resourceType;
	}

	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}
}
