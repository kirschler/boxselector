package com.peterkisch.boxselector.beans;

public class Item {
    private int width;
    private int itemNumber;
    
    public Item() {
    }

    public Item(int itemNumber, int width) {
    	this.itemNumber = itemNumber;
        this.width = width;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }
    
    public int getItemNumber() {
        return itemNumber;
    }

    public void setItemNumber(int itemNumber) {
        this.itemNumber = itemNumber;
    }    
    
}
