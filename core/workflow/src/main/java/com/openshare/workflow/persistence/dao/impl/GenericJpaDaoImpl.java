package com.openshare.workflow.persistence.dao.impl;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.EntityManager;
import javax.persistence.JoinColumn;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.log4j.Logger;

import com.openshare.workflow.domain.PersistableObject;
import com.openshare.workflow.persistence.PersistenceService;
import com.openshare.workflow.persistence.QueryParameters;
import com.openshare.workflow.persistence.dao.GenericDao;
import com.openshare.workflow.persistence.results.ResultsSet;
/**
 * implementation of the generic DAO, inherit to provide functionality specific
 * to a class
 * @author james.mcilroy
 *
 * @param <T>
 */
public abstract class GenericJpaDaoImpl<T extends PersistableObject> implements GenericDao<T> {

    protected EntityManager em;
	
	private static final Logger logger = Logger.getLogger(GenericJpaDaoImpl.class);

    protected Class<T> type;
    
    /**
     * Reflective code that looks for the correct field of an JPA annotated type within this query, 
     * and then gets the corresponding DB field name for the member variable name.
     * This method has been made static so it can be unit tested, but need not be
     * @param memberVariableName
     * @return
     */
    public static String getColumnNameForMemberVariable(String memberVariableName,Class<?> type){
    	try {
    		//get the fields for ths level of the object (super class fields will be got via recursion, 
    		//as they are not accessible unless invoked on the supertype) 
    		Field [] fields = type.getDeclaredFields();
			Field fieldToUse = null;
			for(Field field:fields){
				if(field.getName().equals(memberVariableName)){
					fieldToUse = field;
					break;
				}
			}
			//need to examine superclass (so we can see the private fields in that) as we haven't found the field (droid) 
			//we are looking for
			if(fieldToUse == null && type.getSuperclass()!=Object.class && type.getSuperclass()!=null){
				return getColumnNameForMemberVariable(memberVariableName,type.getSuperclass());
			}
			//we have reached the top level of class hierarchy, so this field does not exist, return null
			else if(fieldToUse == null && (type.getSuperclass()==Object.class || type.getSuperclass()==null)){
				return null;
			}
			//have found the field, now examine the annotations on it, see if we can extract the column name
			else{
				Annotation [] annotations = fieldToUse.getAnnotations();
				//examine annotation type. We support ordering by Column and JoinColumn, so 
				//only consider these. If an id is to be ordered by it MUST be annotated with @Column as
				//well as with @Id as @Id does not support name variable
				for(Annotation annotation : annotations){
					if(annotation instanceof Column){
				        Column myAnnotation = (Column) annotation;
				        return myAnnotation.name();
				    }
					else if(annotation instanceof JoinColumn){
						JoinColumn myAnnotation = (JoinColumn) annotation;
				        return myAnnotation.name();
				    }
				}
			}
		} catch (SecurityException e) {
			return null;
		}
    	return null;
    }
    /**
     * does the query, adds in common pagination etc
     * @param criteriaBuilder
     * @param q
     * @param c
     * @param predicates
     * @param params
     * @return
     */
    protected ResultsSet<T> doQuery(CriteriaBuilder criteriaBuilder,CriteriaQuery<T> q,Root<T> c,Collection<Predicate> predicates,QueryParameters params){
    	
    	Predicate andPredicate=null;
		if(predicates!=null && predicates.size() > 0){
			andPredicate = criteriaBuilder.and(predicates.toArray(new Predicate [predicates.size()]));
			q.where(andPredicate);
		}
		q.select(c);
		if(params!=null && params.getSortPropertyName()!=null && !params.getSortPropertyName().isEmpty()){
			String columnName = getColumnNameForMemberVariable(params.getSortPropertyName(),type);
			if(columnName!=null){
				if(params.isSortAscending()){
					q.orderBy(criteriaBuilder.asc(c.get(columnName)));
				}
				else{
					q.orderBy(criteriaBuilder.desc(c.get(columnName)));
				}
			}
		}
		TypedQuery<T> query = em.createQuery(q);
		//set maximum per page
		query = setPagination(query, params);
		
		List<T> results = query.getResultList();
		//deliver back as a count + map thing.
		ResultsSet<T> resultsSet = new ResultsSet<T>();
		resultsSet.setResults(results);
		long total = getQueryCount(criteriaBuilder,andPredicate);
		resultsSet.setNumberOfResults(total);
		//set the per page if available and non zero.
		if(params!=null && ((params.getPerPage()!=null && params.getPerPage() > 0) && 
					(params.getPage()!=null && params.getPage() > 0))){
				resultsSet.setPerPage(params.getPerPage());
				resultsSet.setStartIndex((params.getPage() - 1) * params.getPerPage());
				resultsSet.setPage(params.getPage());
		}
		else{
			//set default, with 
			resultsSet.setPerPage((int)total);
			resultsSet.setStartIndex(0);
			resultsSet.setPage(1);
		}
		return resultsSet;
    }
    /**
     * ads in all predicates for the query
     * @param cb
     * @param c
     * @param params
     * @return
     */
    protected List<Predicate> getCommonPredicatesForQuery(CriteriaBuilder cb,
    		Root<?> c,QueryParameters params){
    	List<Predicate> predicates = new ArrayList<Predicate>();
    	if(params!=null){
	    	//date range
	    	//less than this date
	    	if(params.getTo()!=null){
	    		predicates.add(cb.lessThanOrEqualTo(c.<Date>get("created"), params.getTo()));
	    	}
	    	//more than this date
	    	if(params.getFrom()!=null){
	    		predicates.add(cb.greaterThanOrEqualTo(c.<Date>get("created"), params.getFrom()));
	    	}
    	}
    	return predicates;
    }
    
    /**
     * generalised pagination method
     * @param query
     * @param params
     * @return
     */
    protected TypedQuery<T> setPagination(TypedQuery<T> query,QueryParameters params){
    	if(params!=null && (params.getPerPage()!=null && params.getPerPage() > 0) && 
				(params.getPage()!=null && params.getPage() > 0)){
			Integer startIndex = (params.getPage() - 1) * params.getPerPage();
			query.setMaxResults(params.getPerPage());
			query.setFirstResult(startIndex);
		}
    	return query;
    }
    
    /**
     * does a count given the predicates for the query
     * @param cb
     * @param andPredicate
     * @return
     */
    protected Long getQueryCount(CriteriaBuilder cb,Predicate andPredicate){
    	CriteriaQuery<Long> cq = cb.createQuery(Long.class);
		cq.select(cb.count(cq.from(type)));
		em.createQuery(cq);
		if(andPredicate!=null){
			cq.where(andPredicate);
		}
		Long count = em.createQuery(cq).getSingleResult();
		return count;
    }
    
    @SuppressWarnings("unchecked")
	public GenericJpaDaoImpl() {
    	em = PersistenceService.getInstance().createEntityManager();
        Type t = getClass().getGenericSuperclass();
        ParameterizedType pt = (ParameterizedType) t;
        type = (Class<T>) pt.getActualTypeArguments()[0];
    }

    /**
     * save an object
     */
    public T persist(final T t) {
    	try{
			em.getTransaction().begin();
	        this.em.persist(t);
			em.getTransaction().commit();
			return t;
    	}
    	finally{
    		if(em!=null && em.isOpen()){
    			em.close();
    		}
    	}
    }

    public T load(final Object id) {
    	try{
    		return this.em.find(type, id);
    	}
    	finally{
    		if(em!=null && em.isOpen()){
    			em.close();
    		}
    	}
    }

    /**
     * update an object
     */
    public T update(final T t) {
		try{
			//if object doesn't exist, create it.
			if(t.getId() == null){
				em.getTransaction().begin();
		        this.em.persist(t);
				em.getTransaction().commit();
				return t;
			}
			else{
				em.getTransaction().begin();
				em.merge(t);
				em.getTransaction().commit();
				return t;
			}    

    	}
    	finally{
    		if(em!=null && em.isOpen()){
    			em.close();
    		}
    	}
    }

    /**
     * delete an object
     */
	public void delete(T o) {
		try{
			em.getTransaction().begin();
			this.em.remove(o);
			em.getTransaction().commit();
    	}
    	finally{
    		if(em!=null && em.isOpen()){
    			em.close();
    		}
    	}
	}
	
	/**
	 * delete by id
	 * @param id
	 */
    public void deleteById(final Object id) {
        T t = this.em.find(type, id);
        if(t!=null){
        	delete(t);
        }
    }

	public EntityManager getEm() {
		return em;
	}

	public void setEm(EntityManager em) {
		this.em = em;
	}  
}