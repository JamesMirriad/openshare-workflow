package com.openshare.workflow.persistence.dao;

import java.util.List;

import com.openshare.workflow.domain.TriggerWorkflowMapping;
import com.openshare.workflow.persistence.QueryParameters;
import com.openshare.workflow.persistence.results.ResultsSet;
/**
 * Resource DAOinterface
 * @author james.mcilroy
 *
 */
public interface TriggerWorkflowMappingDao extends GenericDao<TriggerWorkflowMapping> {

	/**
	 * find file trigger info from data:
	 * @param commonQueryParameters
	 * @param contentOwners
	 * @param fileNames
	 * @param hubs
	 * @param fileTypes
	 * @param relativePaths
	 * @return
	 */
	public ResultsSet<TriggerWorkflowMapping> findByTriggerNames(QueryParameters queryParameters,List<String> triggerNames);

}
