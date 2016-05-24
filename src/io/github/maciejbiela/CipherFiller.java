package io.github.maciejbiela;

import javax.swing.*;
import javax.swing.text.AbstractDocument;
import javax.swing.text.DocumentFilter;
import java.awt.*;

class CipherFiller extends Grid {

    private int boardSize;
    private JTextField[][] matrix;
    private boolean[][] fieldStatusMatrix;

    CipherFiller(int boardSize) {
        this.boardSize = boardSize;
        setLayout();
    }

    public void setLayout() {
        this.matrix = new JTextField[this.boardSize][this.boardSize];
        removeAll();
        setLayout(new GridLayout(this.boardSize, this.boardSize));
        for (int row = 0; row < this.boardSize; row++) {
            for (int column = 0; column < this.boardSize; column++) {
                initializeField(row, column);
            }
        }
        revalidate();
    }

    private void initializeField(int row, int column) {
        matrix[row][column] = new JTextField("");
        JTextField textField = matrix[row][column];
        textField.setFont(new Font("SansSerif", Font.BOLD, 30));
        textField.setEditable(false);
        textField.setHorizontalAlignment(SwingConstants.CENTER);
        DocumentFilter oneCharacterFilter = new LengthDocumentFilter(1);
        ((AbstractDocument) textField.getDocument()).setDocumentFilter(oneCharacterFilter);
        add(textField);
    }

    @Override
    void setFieldStatusMatrix(boolean[][] matrix) {
        this.fieldStatusMatrix = matrix;
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
                    textField.setEditable(this.fieldStatusMatrix[i][j]);
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

    private boolean isSizeOdd() {
        return boardSize % 2 != 0;
    }

    @Override
    int getBoardSize() {
        return boardSize;
    }

    @Override
    void rotate() {
        boolean[][] matrixAfterRotation = new boolean[boardSize][boardSize];
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                matrixAfterRotation[i][j] = fieldStatusMatrix[boardSize - 1 - j][i];
            }
        }
        setFieldStatusMatrix(matrixAfterRotation);
    }

    @Override
    public boolean isFull() {
        for (int row = 0; row < boardSize; row++) {
            for (int column = 0; column < boardSize; column++) {
                if (matrix[row][column].getText().equals("")) {
                    if (isSizeOdd()) {
                        if (row != boardSize / 2 && column != boardSize / 2) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    String getCipher() {
        StringBuilder cipher = new StringBuilder("");
        cipher.append(boardSize);
        cipher.append("\n");
        for (int row = 0; row < boardSize; row++) {
            for (int column = 0; column < boardSize - 1; column++) {
                cipher.append(matrix[row][column].getText());
                cipher.append(" ");
            }
            cipher.append(matrix[row][boardSize - 1].getText());
            cipher.append("\n");
        }
        return cipher.toString();
    }
}
