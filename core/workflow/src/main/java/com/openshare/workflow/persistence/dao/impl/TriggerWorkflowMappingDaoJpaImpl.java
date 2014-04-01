package com.openshare.workflow.persistence.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.log4j.Logger;

import com.openshare.workflow.domain.TriggerWorkflowMapping;
import com.openshare.workflow.persistence.PersistenceService;
import com.openshare.workflow.persistence.QueryParameters;
import com.openshare.workflow.persistence.dao.TriggerWorkflowMappingDao;
import com.openshare.workflow.persistence.results.ResultsSet;
/**
 * implementation of file DAO
 * @author james.mcilroy
 *
 */
public class TriggerWorkflowMappingDaoJpaImpl extends GenericJpaDaoImpl<TriggerWorkflowMapping> implements
	TriggerWorkflowMappingDao {
	
	private static final Logger logger = Logger.getLogger(TriggerWorkflowMappingDaoJpaImpl.class);

	public ResultsSet<TriggerWorkflowMapping> findByTriggerNames(QueryParameters queryParameters,List<String> triggerNames) {
		try{
			CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
	
			CriteriaQuery<TriggerWorkflowMapping> q = (CriteriaQuery<TriggerWorkflowMapping>) criteriaBuilder.createQuery(TriggerWorkflowMapping.class);
			Root<TriggerWorkflowMapping> c = (Root<TriggerWorkflowMapping>) q.from(this.type);
			
			List<Predicate> predicates = new ArrayList<Predicate>();
			//get common predicates
			predicates.addAll(getCommonPredicatesForQuery(criteriaBuilder, c, queryParameters));

			if(triggerNames!=null && triggerNames.size() > 0){
				Expression<String> exp = c.get("triggerName");
				Predicate predicate = exp.in(triggerNames);
				predicates.add(predicate);
			}
			return doQuery(criteriaBuilder,q,c,predicates,queryParameters);
    	}
    	finally{
    		if(em!=null && em.isOpen()){
    			em.close();
    		}
    	}
	}

	public TriggerWorkflowMapping findByTriggerNameAndWorkflow(String triggerName, String workflow) {
		try{
			CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
		
			CriteriaQuery<TriggerWorkflowMapping> q = (CriteriaQuery<TriggerWorkflowMapping>) criteriaBuilder.createQuery(TriggerWorkflowMapping.class);
			Root<TriggerWorkflowMapping> c = (Root<TriggerWorkflowMapping>) q.from(this.type);
			
			List<Predicate> predicates = new ArrayList<Predicate>();
			//get common predicates
			predicates.addAll(getCommonPredicatesForQuery(criteriaBuilder, c, null));
			
			Expression<String> exp = c.get("triggerName");
			Predicate predicate = exp.in(triggerName);
			predicates.add(predicate);
			
			Expression<String> exp2 = c.get("workflowName");
			Predicate predicate2 = exp2.in(triggerName);
			predicates.add(predicate2);
			
			ResultsSet<TriggerWorkflowMapping> results = doQuery(criteriaBuilder,q,c,predicates,null);
			TriggerWorkflowMapping mapping = (results.getResults()!=null && results.getResults().size() > 0) ? results.getResults().get(0) : null; 
			
			//return
			return mapping;
		}
		finally{
			if(em!=null && em.isOpen()){
				em.close();
			}
		}
	}
}
