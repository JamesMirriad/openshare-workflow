package com.openshare.service.registry.service.persistence.db.dao.impl.jpa;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.openshare.service.registry.model.persist.ServiceEntry;
import com.openshare.service.registry.service.persistence.db.dao.ServiceEntryDao;
import com.openshare.service.registry.service.persistence.db.dao.impl.GenericJpaDaoImpl;
/**
 * implementation of file DAO
 * @author james.mcilroy
 *
 */
public class ServiceEntryDaoJpaImpl extends GenericJpaDaoImpl<ServiceEntry> implements
		ServiceEntryDao {

	public List<ServiceEntry> findByOwner(String owner) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<ServiceEntry> findByType(String type) {
		// TODO Auto-generated method stub
		return null;
	}

	public ServiceEntry findByOwnerAndLocation(String owner, String location) {
		// TODO Auto-generated method stub
		return null;
	}

	

//	@Override
//	public List<ServiceEntry> findByUser(String user) {
//		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
//
//		CriteriaQuery<ServiceEntry> q = criteriaBuilder.createQuery(ServiceEntry.class);
//		Root<ServiceEntry> c = q.from(ServiceEntry.class);
//		
//		List<Predicate> predicates = new ArrayList<Predicate>();
//		if(user!=null){
//			List<String> users = new ArrayList<String>();
//			Expression<String> exp = c.get("user");
//			Predicate predicate = exp.in(mediaTypes);
//			predicates.add(predicate);
//		}
//		if(assetUuids!=null && assetUuids.size() > 0){
//			Expression<String> exp = c.get("mediaId");
//			Predicate predicate = exp.in(assetUuids);
//			predicates.add(predicate);
//		}
//		
//		if(predicates.size() > 0){
//			Predicate andPredicate = criteriaBuilder.and(predicates.toArray(new Predicate [predicates.size()]));
//			q.where(andPredicate);
//		}
//		
//		q.select(c);
//		TypedQuery<ServiceEntry> query = em.createQuery(q);
//		List<ServiceEntry> results = query.getResultList();
//		return results;
//	}
//
//	@Override
//	public List<ServiceEntry> findByType(String type) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public ServiceEntry findByOwnerAndLocation(String owner, String location) {
//		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
//
//		CriteriaQuery<ServiceEntry> q = criteriaBuilder.createQuery(ServiceEntry.class);
//		Root<ServiceEntry> c = q.from(ServiceEntry.class);
//		
//		List<Boolean> conditions = new ArrayList<Boolean>();
//		
//		Expression<String> exp = c.get("owner");
//		Boolean ownerTest = exp.equals(owner);
//
//	
//	
//		Expression<String> exp2 = c.get("resourceLocation");
//		Boolean locationTest = exp2.equals(location);
//	
//		
//		if(conditions.size() > 1){
//			criteriaBuilder.and(ownerTest,locationTest);
//			q.where(c.get("owner").equals(owner));
//		}
//		
//		q.select(c);
//		TypedQuery<ServiceEntry> query = em.createQuery(q);
//		ServiceEntry result = query.getSingleResult();
//		return result;
//	}
//
//	@Override
//	public List<ServiceEntry> findByOwner(String owner) {
//		// TODO Auto-generated method stub
//		return null;
//	}

}
