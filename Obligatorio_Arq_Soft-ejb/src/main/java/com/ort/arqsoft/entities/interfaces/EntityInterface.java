/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ort.arqsoft.entities.interfaces;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class EntityInterface {
    
    private final static Logger logger = LoggerFactory.getLogger(EntityInterface.class); 
    
    
        public String generateConverterId(){
           logger.error("The method getConverterId must be implemented");
           return "";
        }
	
	public Object makeConverterObject(String id){
            logger.error("The method getConverterObject must be implemented");
            return null;
        }
}
