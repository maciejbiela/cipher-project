package io.github.maciejbiela;

class FieldStatusMatrix {

    private int marked;
    private int maxMarked;
    private boolean[][] markMatrix;

    FieldStatusMatrix(int n) {
        marked = 0;
        maxMarked = n * n / 4;
        this.markMatrix = new boolean[n][n];
    }

    boolean mark(int row, int column) {
        boolean previous = markMatrix[row][column];
        if (!previous) {
            if (isFull()) {
                return false;
            }
            marked++;
            markMatrix[row][column] = true;
            return true;
        }
        marked--;
        markMatrix[row][column] = false;
        return true;
    }

    boolean[][] getMarkMatrix() {
        return markMatrix;
    }

    void setMarkMatrix(boolean[][] markMatrix) {
        this.markMatrix = markMatrix;
    }

    boolean isFull() {
        return marked == maxMarked;
    }

    boolean isMarked(int i, int j) {
        return markMatrix[i][j];
    }
}
