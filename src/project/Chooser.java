package project;

import javax.swing.*;
import javax.swing.text.AbstractDocument;
import javax.swing.text.DocumentFilter;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

class Chooser extends MainLayoutPanel {

    private int n;
    private FieldStatusMatrix model;
    private JTextField[][] matrix;

    Chooser(int n) {
        setLayout(n);
    }

    public void setLayout(int n) {
        this.n = n;
        this.model = new FieldStatusMatrix(n);
        this.matrix = new JTextField[n][n];
        removeAll();
        setLayout(new GridLayout(n, n));
        for (int row = 0; row < n; row++) {
            for (int column = 0; column < n; column++) {
                matrix[row][column] = new JTextField();
                JTextField textField = matrix[row][column];
                textField.setFont(new Font("SansSerif", Font.BOLD, 30));
                textField.setHorizontalAlignment(SwingConstants.CENTER);
                DocumentFilter oneCharacterFilter = new LengthDocumentFilter(1);
                ((AbstractDocument) textField.getDocument()).setDocumentFilter(oneCharacterFilter);
                textField.setEnabled(true);
                final int i = row;
                final int j = column;
                textField.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if (mark(i, j)) {
                            if (isMarked(i, j)) {
                                textField.setBackground(Color.PINK);
                                textField.setForeground(Color.RED);
                                changeRotations(i, j, true);
                            } else {
                                textField.setBackground(Color.WHITE);
                                textField.setForeground(Color.BLACK);
                                changeRotations(i, j, false);
                            }
                        }
                    }
                });
                add(textField);
            }
        }
        if (n % 2 != 0) {
            matrix[n / 2][n / 2].setEnabled(false);
        }
        revalidate();
    }

    private boolean isMarked(int i, int j) {
        return model.isMarked(i, j);
    }

    private void changeRotations(int row, int column, boolean select) {
        for (int i = 0; i < 3; i++) {
            int tmp = row;
            row = n - 1 - column;
            column = tmp;
            final JTextField textField = matrix[row][column];
            if (select) {
                textField.setEnabled(false);
                textField.setBackground(Color.GRAY);
                textField.setForeground(Color.BLACK);
            } else {
                textField.setEnabled(true);
                textField.setBackground(Color.WHITE);
                textField.setForeground(Color.BLACK);
            }
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
