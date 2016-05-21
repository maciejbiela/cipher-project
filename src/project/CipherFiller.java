package project;

import javax.swing.*;
import javax.swing.text.AbstractDocument;
import javax.swing.text.DocumentFilter;
import java.awt.*;

class CipherFiller extends MainLayoutPanel {

    private int boardSize;
    private JTextField[][] matrix;
    private boolean[][] fieldStatusMatrix;

    CipherFiller(int boardSize) {
        this.boardSize = boardSize;
        setLayout(boardSize);
    }

    CipherFiller(String cipher) {
        final String[] lines = cipher.split("\n");
        this.boardSize = Integer.valueOf(lines[0]);
        setLayout(this.boardSize);
        for (int row = 0; row < this.boardSize; row++) {
            final String[] characters = lines[row + 1].split(" ");
            for (int column = 0; column < this.boardSize; column++) {
                this.matrix[row][column].setText(characters[column]);
            }
        }
    }

    public void setLayout(int n) {
        this.boardSize = n;
        this.matrix = new JTextField[n][n];
        removeAll();
        setLayout(new GridLayout(n, n));
        for (int row = 0; row < n; row++) {
            for (int column = 0; column < n; column++) {
                initializeField(row, column, "");
            }
        }
        revalidate();
    }

    private void initializeField(int row, int column, String character) {
        matrix[row][column] = new JTextField(character);
        JTextField textField = matrix[row][column];
        textField.setFont(new Font("SansSerif", Font.BOLD, 30));
        textField.setEnabled(false);
        textField.setHorizontalAlignment(SwingConstants.CENTER);
        DocumentFilter oneCharacterFilter = new LengthDocumentFilter(1);
        ((AbstractDocument) textField.getDocument()).setDocumentFilter(oneCharacterFilter);
        add(textField);
    }

    @Override
    void setFieldStatusMatrix(boolean[][] matrix) {
        this.fieldStatusMatrix = matrix;
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                final JTextField textField = this.matrix[i][j];
                textField.setEnabled(this.fieldStatusMatrix[i][j]);
                if (textField.isEnabled()) {
                    textField.setBackground(Color.PINK);
                    textField.setForeground(Color.RED);
                } else {
                    textField.setBackground(Color.WHITE);
                    textField.setForeground(Color.BLACK);
                }
            }
        }
        if (boardSize % 2 != 0) {
            this.matrix[boardSize / 2][boardSize / 2].setBackground(Color.GRAY);
        }
    }

    @Override
    int getBoardSize() {
        return boardSize;
    }

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
                    if (boardSize % 2 != 0) {
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
