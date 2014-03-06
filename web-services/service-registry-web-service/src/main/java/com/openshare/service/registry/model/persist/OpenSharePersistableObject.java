package com.openshare.service.registry.model.persist;

import java.util.Date;

public class OpenSharePersistableObject {

//	@Id
//	@GeneratedValue(generator = "system-uuid")
//	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	private String id;
	
//	@Column(nullable = false)
//	@Temporal(TemporalType.TIMESTAMP)
	private Date created;

//	@Column(nullable = false)
//	@Temporal(TemporalType.TIMESTAMP)
	private Date lastModified;
	
	//auto prepersist to automatically update the timestamp, and if not set, 
	//the created time
//	@PrePersist
	protected void updateDates() {
		if (created == null) {
			created = new Date();
		}
		lastModified = new Date();
	}

//	@PreUpdate
	protected void updateLastModified(){
		lastModified = new Date();
	}
	
	// GETTERS AND SETTERS
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Date getLastModified() {
		return lastModified;
	}

	public void setLastModified(Date lastModified) {
		this.lastModified = lastModified;
	}

}
