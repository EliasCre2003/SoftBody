package eliascregard.math;

public class Matrix {

    private final Row[] rows;
    private final int numOfRows;
    private final int numOfColumns;

    public Matrix(int rows, int columns) {
        this.numOfRows = rows;
        this.numOfColumns = columns;
        this.rows = new Row[rows];
        for (int i = 0; i < rows; i++) {
            this.rows[i] = new Row(columns);
        }
    }

    public Matrix(double[][] matrix) {
        this.numOfRows = matrix.length;
        this.numOfColumns = matrix[0].length;
        this.rows = new Row[numOfRows];
        for (int i = 0; i < numOfRows; i++) {
            this.rows[i] = new Row(matrix[i]);
        }
    }

    public void set(int row, int column, double value) {
        if (row < 1 || row > numOfRows || column < 1 || column > numOfColumns) {
            throw new IllegalArgumentException("Row or column out of bounds");
        }
        rows[row - 1].set(column, value);
    }

    private void setRow(int row, Row newRow) {
        if (row < 1 || row > numOfRows) {
            throw new IllegalArgumentException("Row out of bounds");
        }
        rows[row - 1] = newRow;
    }

    public double get(int row, int column) {
        if (column < 1 || column > numOfColumns) {
            throw new IndexOutOfBoundsException("Column out of bounds");
        }
        return rowAt(row).get(column);
    }

    public double[] getRow(int row) {
        if (row < 1 || row > numOfRows) {
            throw new IndexOutOfBoundsException("Row out of bounds");
        }
        return rowAt(row).toArray();
    }

    private Row rowAt(int row) {
        if (row < 1 || row > numOfRows) {
            throw new IndexOutOfBoundsException("Row out of bounds");
        }
        return rows[row - 1];
    }

    public double[] getColumn(int column) {
        if (column < 1 || column > numOfColumns) {
            throw new IllegalArgumentException("Column index out of bounds");
        }
        double[] columnArray = new double[numOfRows];
        for (int i = 0; i < numOfRows; i++) {
            columnArray[i] = rows[i].get(column);
        }
        return columnArray;
    }

    public int getNumOfRows() {
        return numOfRows;
    }

    public int getNumOfColumns() {
        return numOfColumns;
    }

    public void multiplyRow(int row, double scalar) {
        if (row < 1 || row > numOfRows) {
            throw new IllegalArgumentException("Row index out of bounds");
        }
        row--;
        rows[row] = rows[row].multiplication(scalar);
    }

    public void switchRows(int row1, int row2) {
        if (row1 == row2) {
            return;
        }
        if (row1 < 1 || row1 > numOfRows || row2 < 1 || row2 > numOfRows) {
            throw new IllegalArgumentException("Row index out of bounds");
        }
        row1--;row2--;
        Row temp = rows[row1].copy();
        rows[row1] = rows[row2];
        rows[row2] = temp;
    }

    public Matrix transpose() {
        Matrix newMatrix = new Matrix(numOfColumns, numOfRows);
        for (int i = 1; i <= numOfRows; i++) {
            for (int j = 1; j <= numOfColumns; j++) {
                newMatrix.set(j, i, get(i, j));
            }
        }
        return newMatrix;
    }

    public Matrix scaled(double scalar) {
        Matrix newMatrix = new Matrix(numOfRows, numOfColumns);
        for (int i = 1; i <= numOfRows; i++) {
            for (int j = 1; j <= numOfColumns; j++) {
                newMatrix.set(i, j, get(i, j) * scalar);
            }
        }
        return newMatrix;
    }

    private int[][] getPermutations(int n) {
        int numberOfPermutations = 1;
        for (int i = 1; i <= n; i++) {
            numberOfPermutations *= i;
        }
        int[][] permutations = new int[numberOfPermutations][n];
        int[] numArray = new int[n];
        for (int i = 0; i < n; i++) {
            numArray[i] = i;
        }
        heapsAlgorithm(numArray, n, n, permutations, 0);
        return permutations;

    }

    private void heapsAlgorithm(int[] array, int size, int n, int[][] permutations, int currentIndex) {

        if (size == 1) {
            permutations[currentIndex] = array.clone();
            currentIndex++;
        }
        for (int i = 0; i < size; i++) {
            heapsAlgorithm(array, size - 1, n, permutations, currentIndex);
            if (size % 2 == 1) {
                int temp = array[0];
                array[0] = array[size - 1];
                array[size - 1] = temp;
            } else {
                int temp = array[i];
                array[i] = array[size - 1];
                array[size - 1] = temp;
            }
        }
    }

    public double determinant() {
        if (!isSquare()) {
            throw new IllegalArgumentException("Matrix must be square to calculate determinant.");
        }
        if (numOfRows == 1) {
            return get(0, 0);
        }
        if (numOfRows == 2) {
            return get(0, 0) * get(1, 1) - get(0, 1) * get(1, 0);
        }
        double size = numOfRows;
        double determinant = 0;
        // TODO: Implement the Leibniz formula for determinant calculation for matrices
        // bigger than 2.
        // for ()

        return determinant;
    }

    public Matrix gaussElimination() {
        if (!isSquare() && !(numOfRows == numOfColumns + 1)) {
            throw new IllegalArgumentException(
                    "Matrix must be square or have one more column than rows to perform Gauss elimination.");
        }
        Matrix newMatrix = new Matrix(numOfRows, numOfColumns);

        return newMatrix;
    }

    public boolean isSquare() {
        return numOfRows == numOfColumns;
    }

    public boolean isSymmetric() {
        if (!isSquare()) {
            return false;
        }
        return this == transpose();
    }

    public boolean isSkewSymmetric() {
        if (!isSquare()) {
            return false;
        }
        return this == transpose().scaled(-1);
    }

    public boolean isIdentity() {
        if (!isSquare()) {
            return false;
        }
        return this == Matrix.identity(numOfRows);
    }

    public static Matrix sum(Matrix A, Matrix B) {
        if (A.numOfRows != B.numOfRows || A.numOfColumns != B.numOfColumns) {
            throw new IllegalArgumentException("Matrices must have the same dimensions.");
        }
        Matrix C = new Matrix(A.numOfRows, A.numOfColumns);
        for (int i = 1; i <= A.numOfRows; i++) {
            for (int j = 1; j <= A.numOfColumns; j++) {
                C.set(i, j, A.get(i, j) + B.get(i, j));
            }
        }
        return C;
    }

    public static Matrix multiplication(Matrix A, Matrix B) {
        if (A.numOfColumns != B.numOfRows) {
            throw new IllegalArgumentException(
                    "The number of columns of the first matrix must be equal to the number of rows of the second matrix.");
        }
        Matrix C = new Matrix(A.numOfRows, B.numOfColumns);
        for (int i = 1; i <= A.numOfRows; i++) {
            for (int j = 1; j <= B.numOfColumns; j++) {
                double sum = 0;
                for (int k = 1; k <= A.numOfColumns; k++) {
                    sum += A.get(i, k) * B.get(k, j);
                }
                C.set(i, j, sum);
            }
        }
        return C;
    }

    public static Matrix submatrix(Matrix matrix, int row, int column) {
        if (row < 1 || column < 1 || row > matrix.numOfRows || column > matrix.numOfColumns) {
            throw new IllegalArgumentException("Row and column must be within the dimensions of the matrix.");
        }
        Matrix newMatrix = new Matrix(matrix.numOfRows - 1, matrix.numOfColumns - 1);
        for (int i = 1; i <= matrix.numOfRows; i++) {
            for (int j = 1; j <= matrix.numOfColumns; j++) {
                if (i != row && j != column) {
                    newMatrix.set(i, j, matrix.get(i, j));
                }
            }
        }
        return newMatrix;
    }

    public static Matrix identity(int size) {
        Matrix matrix = new Matrix(size, size);
        for (int i = 1; i <= size; i++) {
            matrix.set(i, i, 1);
        }
        return matrix;
    }

    public static Matrix random(int rows, int columns) {
        Matrix matrix = new Matrix(rows, columns);
        for (int i = 1; i <= rows; i++) {
            for (int j = 1; j <= columns; j++) {
                matrix.set(i, j, Math.random());
            }
        }
        return matrix;
    }

    public static Matrix gaussEliminated(Matrix matrix) {
        if (!(matrix.isSquare() || matrix.numOfRows == matrix.numOfColumns - 1)) {
            throw new IllegalArgumentException(
                    "Matrix must be square or have one more column than rows to perform Gauss elimination."
            );
        }
        Matrix result = matrix.copy();
        for (int i = 1; i <= result.numOfRows; i++) {
            if (result.get(i, i) == 0) continue;
            double factor = 1 / result.get(i, i);
            result.scaleRow(i, factor);
            if (i == result.numOfRows) break;
            for (int j = i + 1; j <= result.numOfRows; j++) {
                Row row = result.rowAt(i).copy();
                result.rows[j-1].add(row, -result.get(j, i));
            }
            if (result.rowAt(i).getFistNonZeroIndex() != i) {
                for (int j = i + 1; j <= result.numOfRows; j++) {
                    if (result.rowAt(j).getFistNonZeroIndex() == i) {
                        result.switchRows(i, j);
                        break;
                    }
                }
            }
        }
        for (int i = result.numOfRows; i > 0; i--) {
            for (int j = i - 1; j > 0; j--) {
                Row row = result.rowAt(i).copy();
                row.scale(-result.get(j, i));
                result.rows[j-1].add(row);
            }
        }
        return result;
    }

    private void scaleRow(int i, double factor) {
        if (i < 1 || i > numOfRows) {
            throw new IllegalArgumentException("Row must be within the dimensions of the matrix.");
        }
        rows[i-1].scale(factor);
    }

    public Matrix copy() {
        Matrix matrix = new Matrix(numOfRows, numOfColumns);
        for (int i = 1; i <= numOfRows; i++) {
            for (int j = 1; j <= numOfColumns; j++) {
                matrix.set(i, j, get(i, j));
            }
        }
        return matrix;
    }

    public String toString() {
        StringBuilder string = new StringBuilder();
        for (Row row : rows) {
            string.append(row.toString()).append("\n");
        }
        return string.toString();
    }

    public boolean equals(Object object) {
        if (!(object instanceof Matrix)) {
            return false;
        }
        Matrix matrix = (Matrix) object;
        if (numOfRows != matrix.numOfRows || numOfColumns != matrix.numOfColumns) {
            return false;
        }
        for (int i = 1; i <= numOfRows; i++) {
            for (int j = 1; j <= numOfColumns; j++) {
                if (get(i, j) != matrix.get(i, j)) {
                    return false;
                }
            }
        }
        return true;
    }

    public static void main(String[] args) {
        Matrix matrix = new Matrix(new double[][]{
                {1, 2, 3, 12},
                {4, 5, 6, 56},
                {7, 8, 9, 9 }
        });
        System.out.println(matrix);
        System.out.println(Matrix.gaussEliminated(matrix));
    }
}

class Row {
    private final double[] cells;
    private final int numberOfColumns;

    public Row(int columns) {
        this.numberOfColumns = columns;
        this.cells = new double[columns];
    }

    public Row(double[] cells) {
        this.cells = cells;
        this.numberOfColumns = cells.length;
    }

    void set(int column, double value) {
        if (column < 1 || column > numberOfColumns) {
            throw new IllegalArgumentException("Column must be between 1 and " + numberOfColumns + ".");
        }
        this.cells[column - 1] = value;
    }

    double get(int column) {
        if (column < 1 || column > numberOfColumns) {
            throw new IllegalArgumentException("Column must be between 1 and " + numberOfColumns + ".");
        }
        return this.cells[column - 1];
    }

    double[] toArray() {
        return this.cells;
    }

    void add(Row row, double scalar) {
        for (int i = 0; i < numberOfColumns; i++) {
            this.cells[i] += row.cells[i] * scalar;
        }
    }

    void add(Row row) {
        add(row, 1);
    }

    Row sum(Row otherMatrix) {
        if (numberOfColumns != otherMatrix.numberOfColumns) {
            throw new IllegalArgumentException("Rows must have the same dimensions.");
        }
        Row newMatrix = new Row(numberOfColumns);
        for (int i = 0; i < numberOfColumns; i++) {
            newMatrix.cells[i] = this.cells[i] + otherMatrix.cells[i];
        }
        return newMatrix;
    }

    Row multiplication(double scalar) {
        Row newRow = new Row(numberOfColumns);
        for (int i = 0; i < numberOfColumns; i++) {
            newRow.cells[i] = cells[i] * scalar;
        }
        return newRow;
    }

    void scale(double scalar) {
        for (int i = 0; i < numberOfColumns; i++) {
            cells[i] *= scalar;
        }

    }

    int getFistNonZeroIndex() {
        for (int i = 0; i < numberOfColumns; i++) {
            if (cells[i] != 0) {
                return i + 1;
            }
        }
        return -1;
    }

    Row copy() {
        Row newRow = new Row(numberOfColumns);
        for (int i = 0; i < numberOfColumns; i++) {
            newRow.cells[i] = cells[i];
        }
        return newRow;
    }

    public String toString() {
        StringBuilder string = new StringBuilder("| ");
        for (double value : cells) {
            if (value == -0.0) {
                value = 0.0;
            }
            string.append(value).append(" ");
        }
        return string + "|";
    }

    public boolean equals(Object object) {
        if (!(object instanceof Row)) {
            return false;
        }
        Row row = (Row) object;
        if (this.numberOfColumns != row.numberOfColumns) {
            return false;
        }
        for (int i = 0; i < this.numberOfColumns; i++) {
            if (this.cells[i] != row.cells[i]) {
                return false;
            }
        }
        return true;
    }
}
