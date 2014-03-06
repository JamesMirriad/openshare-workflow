package com.openshare.service.registry.service.persistence.db.dao.impl;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import javax.persistence.EntityManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.openshare.service.registry.service.persistence.db.PersistenceService;
import com.openshare.service.registry.service.persistence.db.dao.GenericDao;
/**
 * implementation of the generic DAO, inherit to provide functionality specific
 * to a class
 * @author james.mcilroy
 *
 * @param <T>
 */
public abstract class GenericJpaDaoImpl<T> implements GenericDao<T> {

    protected EntityManager em;
	
	private static final Logger logger = LoggerFactory.getLogger(GenericJpaDaoImpl.class);

    private Class<T> type;
    
    @SuppressWarnings("unchecked")
	public GenericJpaDaoImpl() {
    	em = PersistenceService.getInstance().createEntityManager();
        Type t = getClass().getGenericSuperclass();
        ParameterizedType pt = (ParameterizedType) t;
        type = (Class<T>) pt.getActualTypeArguments()[0];
    }

    public T persist(final T t) {
		em.getTransaction().begin();
        this.em.persist(t);
		em.getTransaction().commit();
        return t;
    }

    public T load(final Object id) {
        return (T) this.em.find(type, id);
    }

    public T update(final T t) {
		em.getTransaction().begin();
		//if object doesn't exist, create it.
//		if(t.getId() == null){
//			this.em.persist(t);
//			em.getTransaction().commit();
//			return t;
//		}
//		else{
			em.merge(t);
			em.getTransaction().commit();
			return t;
//		}    
    }

	public void delete(T o) {
		em.getTransaction().begin();
		this.em.remove(o);
		em.getTransaction().commit();
	}
	
    public void deleteById(final Object id) {
        T t = load(id);
        delete(t);
    }

	public EntityManager getEm() {
		return em;
	}

	public void setEm(EntityManager em) {
		this.em = em;
	}
    
    
}