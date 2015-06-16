/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ort.arqsoft.ui.utils;

/**
 *
 * @author HP
 */
public class ItemMenu {
    
    private String name;
    private String pathImage;
    private Enum value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPathImage() {
        return pathImage;
    }

    public void setPathImage(String pathImage) {
        this.pathImage = pathImage;
    }

    public Enum getValue() {
        return value;
    }

    public void setValue(Enum value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemMenu that = (ItemMenu) o;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (pathImage != null ? !pathImage.equals(that.pathImage) : that.pathImage != null) return false;
        return value.equals(that.value);
    }

    @Override
    public int hashCode() {
        if (name==null){
            return 0;
        }
        int result = name.hashCode();
        result = 31 * result + (pathImage != null ? pathImage.hashCode() : 0);
        return result;
    }
    
    
           
    
    
    
}
