/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ort.arqsoft.entities.utils;

import java.util.List;
import java.util.Map;
import javax.ejb.Local;
import javax.persistence.LockModeType;
import com.ort.arqsoft.entities.interfaces.EntityInterface;

@Local
public interface JPAServiceLocal {

    <T extends EntityInterface> List<T> findWithNamedQuery(Class<T> type, String namedQueryName, Map<String, Object> parameters, int resultLimit, LockModeType... lockType);

    <T extends EntityInterface> List<T> findWithNamedQuery(Class<T> type, String namedQueryName, Map<String, Object> parameters);
    
    <T extends EntityInterface> List<T> findWithNamedQuery(Class<T> type, String namedQueryName, int resultLimit,LockModeType... lockType);

    <T extends EntityInterface> List<T> findWithNamedQuery(Class<T> type, String namedQueryName);
    
    <T extends EntityInterface> T findOneWithNamedQuery(Class<T> type, String namedQueryName, Map<String, Object> parameters);

    <T extends EntityInterface> T findOneWithNamedQuery(Class<T> type, String namedQueryName);
    
    <T extends EntityInterface> boolean existWithNamedQuery(Class<T> type, String namedQueryName, Map<String, Object> parameters);
      
    <T extends EntityInterface> boolean existWithNamedQuery(Class<T> type, String namedQueryName);
    
    <T extends EntityInterface, PK> void delete(Class<T> type, PK id);

    <T extends EntityInterface> T update(T t);

    <T extends EntityInterface> T findForConverter(Class<T> type, String id);

    <T extends EntityInterface, PK> T find(Class<T> type, PK id);
    
    <T extends EntityInterface, PK> boolean exist(Class<T> type, PK id);

    <T extends EntityInterface> T create(T t);

    <T extends EntityInterface> List<T> findAll(Class<T> type);

    <T extends EntityInterface> void delete(T entity);
    
    <T extends EntityInterface> void deleteAll(Class<T> clazz);

    <T extends EntityInterface> T detach(T t);

    <T extends EntityInterface> void unlockEntity(T t);

    <T extends EntityInterface> long countElements(Class<T> clazz); 
  
}
