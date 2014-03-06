package com.openshare.service.registry.service.persistence.db.dao;


/**
 * generic DAO class.
 * @author james.mcilroy
 *
 * @param <T>
 */
public interface GenericDao <T> {

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
