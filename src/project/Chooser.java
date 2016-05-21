package project;

import javax.swing.*;
import java.awt.*;

class Chooser extends MainLayoutPanel {

    private int n;
    private FieldStatusMatrix model;
    private JCheckBox[][] matrix;

    Chooser(int n) {
        setLayout(n);
    }

    public void setLayout(int n) {
        this.n = n;
        this.model = new FieldStatusMatrix(n);
        this.matrix = new JCheckBox[n][n];
        removeAll();
        setLayout(new GridLayout(n, n));
        for (int row = 0; row < n; row++) {
            for (int column = 0; column < n; column++) {
                Icon icon = new CheckBoxIcon();
                matrix[row][column] = new JCheckBox(icon);
                JCheckBox checkBox = matrix[row][column];
                checkBox.setEnabled(true);
                final int i = row;
                final int j = column;
                checkBox.addActionListener(e -> {
                    if (!mark(i, j)) {
                        checkBox.setSelected(!checkBox.isSelected());
                    } else {
                        changeRotations(i, j, !checkBox.isSelected());
                    }
                });
                add(checkBox);
            }
        }
        if (n % 2 != 0) {
            matrix[n / 2][n / 2].setEnabled(false);
        }
        revalidate();
    }

    private void changeRotations(int row, int column, boolean select) {
        for (int i = 0; i < 3; i++) {
            int tmp = row;
            row = n - 1 - column;
            column = tmp;
            matrix[row][column].setEnabled(select);
        }
    }

    private boolean mark(int row, int column) {
        return this.model.mark(row, column);
    }

    public int getBoardSize() {
        return n;
    }

    boolean[][] getFieldStatusMatrix() {
        return model.getMarkMatrix();
    }

    @Override
    public boolean isFull() {
        return model.isFull();
    }
}
