package io.github.maciejbiela;

import javax.swing.*;
import javax.swing.text.AbstractDocument;
import javax.swing.text.DocumentFilter;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

class Chooser extends Grid {

    private int boardSize;
    private JTextField[][] matrix;
    private FieldStatusMatrix fieldStatusMatrix;

    Chooser(int boardSize) {
        this.boardSize = boardSize;
        setLayout();
    }

    Chooser(String cipher) {
        final String[] lines = cipher.split("\n");
        this.boardSize = Integer.valueOf(lines[0]);
        setLayout();
        for (int row = 0; row < this.boardSize; row++) {
            final String[] characters = lines[row + 1].split(" ");
            for (int column = 0; column < this.boardSize; column++) {
                this.matrix[row][column].setText(characters[column]);
            }
        }
    }

    public void setLayout() {
        this.fieldStatusMatrix = new FieldStatusMatrix(this.boardSize);
        this.matrix = new JTextField[this.boardSize][this.boardSize];
        removeAll();
        setLayout(new GridLayout(this.boardSize, this.boardSize));
        for (int row = 0; row < this.boardSize; row++) {
            for (int column = 0; column < this.boardSize; column++) {
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
                        if (isSizeOdd()) {
                            if (i == boardSize / 2 && j == boardSize / 2) {
                                return;
                            }
                        }
                        if (!mark(i, j)) {
                            return;
                        }
                        if (isMarked(i, j)) {
                            SwingUtilities.invokeLater(() -> {
                                textField.setBackground(Color.PINK);
                                textField.setForeground(Color.RED);
                                changeRotations(i, j, true);
                            });
                        } else {
                            SwingUtilities.invokeLater(() -> {
                                textField.setBackground(Color.WHITE);
                                textField.setForeground(Color.BLACK);
                                changeRotations(i, j, false);
                            });
                        }
                    }
                });
                add(textField);
            }
        }
        if (isSizeOdd()) {
            SwingUtilities.invokeLater(() -> {
                final JTextField textField = matrix[this.boardSize / 2][this.boardSize / 2];
                textField.setEditable(false);
                textField.setBackground(Color.GRAY);
                textField.setForeground(Color.GRAY);
            });
        }
        revalidate();
    }

    private boolean isSizeOdd() {
        return this.boardSize % 2 != 0;
    }

    private boolean isMarked(int i, int j) {
        return fieldStatusMatrix.isMarked(i, j);
    }

    private void changeRotations(int row, int column, boolean select) {
        for (int i = 0; i < 3; i++) {
            int tmp = row;
            row = boardSize - 1 - column;
            column = tmp;
            final JTextField textField = matrix[row][column];
            if (select) {
                textField.setEditable(false);
                textField.setBackground(Color.GRAY);
                textField.setForeground(Color.BLACK);
            } else {
                textField.setEditable(true);
                textField.setBackground(Color.WHITE);
                textField.setForeground(Color.BLACK);
            }
        }
    }

    private boolean mark(int row, int column) {
        return this.fieldStatusMatrix.mark(row, column);
    }

    public int getBoardSize() {
        return boardSize;
    }

    boolean[][] getFieldStatusMatrix() {
        return fieldStatusMatrix.getMarkMatrix();
    }

    @Override
    public boolean isFull() {
        return fieldStatusMatrix.isFull();
    }

    @Override
    void setFieldStatusMatrix(boolean[][] matrix) {
        this.fieldStatusMatrix.setMarkMatrix(matrix);
        highlightFields();
    }

    @Override
    void highlightFields() {
        for (int row = 0; row < boardSize; row++) {
            for (int column = 0; column < boardSize; column++) {
                final JTextField textField = this.matrix[row][column];
                final int i = row;
                final int j = column;
                SwingUtilities.invokeLater(() -> {
                    textField.setEditable(isMarked(i, j));
                    if (textField.isEditable()) {
                        textField.setBackground(Color.PINK);
                        textField.setForeground(Color.RED);
                    } else {
                        textField.setBackground(Color.WHITE);
                        textField.setForeground(Color.BLACK);
                    }
                });
            }
        }
        if (isSizeOdd()) {
            SwingUtilities.invokeLater(() -> this.matrix[boardSize / 2][boardSize / 2].setBackground(Color.GRAY));
        }
    }

    @Override
    void rotate() {
        boolean[][] matrixAfterRotation = new boolean[boardSize][boardSize];
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                matrixAfterRotation[i][j] = isMarked(boardSize - 1 - j, i);
            }
        }
        setFieldStatusMatrix(matrixAfterRotation);
    }
}
