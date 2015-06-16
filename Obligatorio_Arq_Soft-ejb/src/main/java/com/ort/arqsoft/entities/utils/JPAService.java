/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ort.arqsoft.entities.utils;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import com.ort.arqsoft.entities.interfaces.EntityInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Stateless
public class JPAService implements JPAServiceLocal {

    private final static Logger logger = LoggerFactory.getLogger(JPAService.class);
    @PersistenceContext(unitName = "ARQSOFT_DATA")
    private EntityManager em;

    @Override
    public <T extends EntityInterface> T create(T t) {
        this.em.persist(t);
        this.em.flush();
        this.em.refresh(t);
        return t;
    }

    @Override
    public <T extends EntityInterface, PK> T find(Class<T> type, PK id) {
        return this.em.find(type, id);
    }

    @Override
    public <T extends EntityInterface> T findForConverter(Class<T> type, String id) {
        T ins = null;
        try {
            ins = type.newInstance();
            return this.em.find(type, ins.makeConverterObject(id));
        } catch (Exception ex) {
            logger.error("Imposible to create the class from converter String", ex);
        }
        return null;
    }

    @Override
    public <T extends EntityInterface> T update(T t) {
        return this.em.merge(t);
    }

    @Override
    public <T extends EntityInterface, PK> void delete(Class<T> type, PK id) {
        T object = this.find(type, id);
        if (object != null) {
            this.em.remove(object);
        }
    }

    @Override
    public <T extends EntityInterface> List<T> findWithNamedQuery(Class<T> type, String namedQueryName) {
        return this.em.createNamedQuery(namedQueryName, type).getResultList();
    }

    @Override
    public <T extends EntityInterface> List<T> findWithNamedQuery(Class<T> type, String namedQueryName, int resultLimit, LockModeType... lockType) {
        TypedQuery<T> query = em.createNamedQuery(namedQueryName, type);
        query.setMaxResults(resultLimit);
        if (lockType.length > 0) {
            query.setLockMode(lockType[0]);
        }
        return query.getResultList();
    }

    @Override
    public <T extends EntityInterface> List<T> findWithNamedQuery(Class<T> type, String namedQueryName, Map<String, Object> parameters) {
        return findWithNamedQuery(type, namedQueryName, parameters, 0);
    }

    @Override
    public <T extends EntityInterface> List<T> findWithNamedQuery(Class<T> type, String namedQueryName, Map<String, Object> parameters, int resultLimit, LockModeType... lockType) {
        Set<Entry<String, Object>> rawParameters = parameters.entrySet();
        TypedQuery<T> query = this.em.createNamedQuery(namedQueryName, type);
        if (resultLimit > 0) {
            query.setMaxResults(resultLimit);
        }
        for (Entry<String, Object> entry : rawParameters) {
            query.setParameter(entry.getKey(), entry.getValue());
        }
        if (lockType.length > 0) {
            query.setLockMode(lockType[0]);
        }
        return query.getResultList();
    }

    @Override
    public <T extends EntityInterface> T findOneWithNamedQuery(Class<T> type, String namedQueryName, Map<String, Object> parameters) {
        List<T> res = findWithNamedQuery(type, namedQueryName, parameters);
        if (res.size() > 0) {
            return res.get(0);
        } else {
            return null;
        }
    }

    @Override
    public <T extends EntityInterface> T findOneWithNamedQuery(Class<T> type, String namedQueryName) {
        List<T> res = findWithNamedQuery(type, namedQueryName);
        if (res.size() > 0) {
            return res.get(0);
        } else {
            return null;
        }
    }

    @Override
    public <T extends EntityInterface> boolean existWithNamedQuery(Class<T> type, String namedQueryName, Map<String, Object> parameters) {
        List<T> res = findWithNamedQuery(type, namedQueryName, parameters);
        if (res.size() > 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public <T extends EntityInterface> boolean existWithNamedQuery(Class<T> type, String namedQueryName) {
        List<T> res = findWithNamedQuery(type, namedQueryName);
        if (res.size() > 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public <T extends EntityInterface> List<T> findAll(Class<T> type) {
        javax.persistence.criteria.CriteriaQuery cq = this.em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(type));
        return this.em.createQuery(cq).getResultList();
    }

    @Override
    public <T extends EntityInterface> void delete(T entity) {
        this.em.remove(this.em.merge(entity));
    }

    @Override
    public <T extends EntityInterface> void deleteAll(Class<T> clazz) {
        List<T> entidades = this.findAll(clazz);
        for (T entidad : entidades) {
            this.delete(entidad);
        }
    }

    @Override
    public <T extends EntityInterface> T detach(T t) {
        this.em.detach(t);
        return t;
    }

    @Override
    public <T extends EntityInterface, PK> boolean exist(Class<T> type, PK id) {
        return this.em.find(type, id) != null;
    }

    @Override
    public <T extends EntityInterface> void unlockEntity(T t) {
        em.lock(t, LockModeType.NONE);
    }

    @Override
    public <T extends EntityInterface> long countElements(Class<T> clazz) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Long> cq = builder.createQuery(Long.class);
        cq.select(builder.count(cq.from(clazz)));
        return em.createQuery(cq).getSingleResult();
    }
}
