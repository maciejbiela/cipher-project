package project;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.swing.*;

abstract class Grid extends JPanel{
    abstract void setLayout();

    abstract int getBoardSize();

    abstract boolean isFull();

    abstract void rotate();

    abstract void highlightFields();

    boolean[][] getFieldStatusMatrix() {
        throw new NotImplementedException();
    }

    void setFieldStatusMatrix(boolean[][] fieldStatusMatrix) {
        throw new NotImplementedException();
    }
}
