package com.peterkisch.boxselector.beans;

public class Box {
    private int rows;
    private int cols;
    private int boxNumber;

    public Box(int boxNumber, int rows, int cols) {
        this.boxNumber = boxNumber;
        this.rows = rows;
        this.cols = cols;
    }

    public int getHeight() {
        return rows;
    }

    public int getWidth() {
        return cols;
    }

    public int getBoxNumber() {
        return boxNumber;
    }
}
