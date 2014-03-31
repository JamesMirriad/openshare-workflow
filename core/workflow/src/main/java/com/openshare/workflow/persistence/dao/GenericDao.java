package com.openshare.workflow.persistence.dao;

import com.openshare.workflow.domain.PersistableObject;


/**
 * generic DAO class.
 * @author james.mcilroy
 *
 * @param <T>
 */
public interface GenericDao <T extends PersistableObject> {

	/**
	 * persist an object
	 * @param t
	 * @return
	 */
	public T persist(T t);

	/**
	 *delete this object
	 * @param o
	 */
    public void delete(T o);
    
    /**
     * delete an object by Id
     * @param id
     */
    public void deleteById(final Object id);

    /**
     * load an object by an id
     * @param id
     * @return
     */
    public T load(Object id);

    /**
     * update an object
     * @param t
     * @return
     */
    public T update(T t); 
}
