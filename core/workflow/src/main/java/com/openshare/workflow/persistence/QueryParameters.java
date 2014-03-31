package com.openshare.workflow.persistence;

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;
/**
 * parameters common to any query, such as date range, pagination etc
 * @author james.mcilroy
 *
 */
@ApiModel(value="QueryParameters", description="Common Query Request Parameters")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class QueryParameters {

	@ApiModelProperty(required = false, value = "page required within the results set")
	private Integer page;
	
	@ApiModelProperty(required = false, value = "pagination amount")
	private Integer perPage;
	
	@ApiModelProperty(required = false, value = "start of date range")
	private Date from;
	
	@ApiModelProperty(required = false, value = "end of date range")
	private Date to;
	
	@ApiModelProperty(required = false, value = "sort order if sort is required, true = ascending, false = descending")
	private boolean sortAscending;
	
	@ApiModelProperty(required = false, value = "JSON name of the field we want to sort by, if null or emptysort is ignored")
	private String sortPropertyName;
	
	public Integer getPage() {
		return page;
	}
	public void setPage(Integer page) {
		this.page = page;
	}
	public Integer getPerPage() {
		return perPage;
	}
	public void setPerPage(Integer perPage) {
		this.perPage = perPage;
	}
	public Date getFrom() {
		return from;
	}
	public void setFrom(Date from) {
		this.from = from;
	}
	public Date getTo() {
		return to;
	}
	public void setTo(Date to) {
		this.to = to;
	}
	public boolean isSortAscending() {
		return sortAscending;
	}
	public void setSortAscending(boolean sortAscending) {
		this.sortAscending = sortAscending;
	}
	public String getSortPropertyName() {
		return sortPropertyName;
	}
	public void setSortPropertyName(String sortPropertyName) {
		this.sortPropertyName = sortPropertyName;
	}	
}
