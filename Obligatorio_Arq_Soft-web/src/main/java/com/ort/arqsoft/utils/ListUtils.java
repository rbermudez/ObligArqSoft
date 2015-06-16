/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ort.arqsoft.utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author HP
 */
public class ListUtils {
    
    public static <T> List<T> union(List<T> list1, List<T> list2) {
        Set<T> set = new HashSet<T>();

        set.addAll(list1);
        set.addAll(list2);

        return new ArrayList<T>(set);
    }

    public static <T> List<T> intersection(List<T> list1, List<T> list2) {
        List<T> list = new ArrayList<T>();
        for (T t : list1) {
            if(list2.contains(t)) {
                list.add(t);
            }
        }
        return list;
    }
    
    public static <T> List<T> diferenceFirstList(List<T> list1, List<T> list2) {
        List<T> list = new ArrayList<T>();
        for (T t : list1) {
            if(!list2.contains(t)) {
                list.add(t);
            }
        }
        return list;
    }
    
    public static <T> List<T> diferenceOfSecondList(List<T> list1, List<T> list2) {
        List<T> list = new ArrayList<T>();
        for (T t : list2) {
            if(!list1.contains(t)) {
                list.add(t);
            }
        }
        return list;
    }
}
