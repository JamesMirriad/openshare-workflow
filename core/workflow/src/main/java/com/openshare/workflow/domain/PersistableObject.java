package com.openshare.workflow.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.GenericGenerator;

import com.wordnik.swagger.annotations.ApiModel;
@ApiModel(value="PersistableObject", description="Persistable object base class")
@XmlRootElement
@MappedSuperclass
public class PersistableObject {
	
	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name="id", unique=true)
	private String id;

	@Column(nullable = false, name="created")
	@Temporal(TemporalType.TIMESTAMP)
	private Date created;

	@Column(nullable = false,name="lastModified")
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastModified;
	//auto prepersist to automatically update the timestamp, and if not set, 
	//the created time
	@PrePersist
	protected void updateDates() {
		if (created == null) {
			created = new Date();
		}
		lastModified = new Date();
	}

	@PreUpdate
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
