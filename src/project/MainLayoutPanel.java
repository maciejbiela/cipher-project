package project;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.swing.*;

abstract class MainLayoutPanel extends JPanel{
    abstract void setLayout(int n);

    abstract int getBoardSize();

    abstract boolean isFull();

    boolean[][] getFieldStatusMatrix() {
        throw new NotImplementedException();
    }

    void setFieldStatusMatrix(boolean[][] fieldStatusMatrix) {
        throw new NotImplementedException();
    }
}
