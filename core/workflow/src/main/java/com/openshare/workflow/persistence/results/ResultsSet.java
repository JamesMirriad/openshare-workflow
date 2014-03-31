package com.openshare.workflow.persistence.results;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import com.openshare.workflow.domain.PersistableObject;
import com.wordnik.swagger.annotations.ApiModel;
/**
 * class to hold results set and full count. Results may be paginated,
 * so the full count may not match the size of the results list
 * @author james.mcilroy
 *
 * @param <T>
 */
@ApiModel(value="ResultsSet", description="A results set operation")
@XmlRootElement
public class ResultsSet <T extends PersistableObject>{
	
	private Long numberOfResults;
	
	private Integer page;
	
	private Integer startIndex;
	
	private Integer perPage;
	
	private List<T> results;

	public Long getNumberOfResults() {
		return numberOfResults;
	}

	public void setNumberOfResults(Long numberOfResults) {
		this.numberOfResults = numberOfResults;
	}

	public List<T> getResults() {
		return results;
	}

	public void setResults(List<T> results) {
		this.results = results;
	}

	public Integer getStartIndex() {
		return startIndex;
	}

	public void setStartIndex(Integer startIndex) {
		this.startIndex = startIndex;
	}

	public Integer getPerPage() {
		return perPage;
	}

	public void setPerPage(Integer perPage) {
		this.perPage = perPage;
	}

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}
	
}
