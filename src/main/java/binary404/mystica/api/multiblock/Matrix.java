package binary404.mystica.api.multiblock;

public class Matrix {

    int rows;
    int cols;
    MultiBlockComponent[][] matrix;

    public Matrix(MultiBlockComponent[][] matrix) {
        this.rows = matrix.length;
        this.cols = matrix[0].length;
        this.matrix = new MultiBlockComponent[this.rows][this.cols];
        for (int i = 0; i < this.rows; i++) {

            for (int j = 0; j < this.cols; j++) {
                this.matrix[i][j] = matrix[i][j];
            }
        }
    }

    public void Rotate90DegRight(int times) {
        for (int a = 0; a < times; a++) {


            MultiBlockComponent[][] newMatrix = new MultiBlockComponent[this.cols][this.rows];

            for (int i = 0; i < this.rows; i++) {

                for (int j = 0; j < this.cols; j++) {
                    newMatrix[j][this.rows - i - 1] = this.matrix[i][j];
                }
            }

            this.matrix = newMatrix;


            int tmp = this.rows;
            this.rows = this.cols;
            this.cols = tmp;
        }
    }


    public MultiBlockComponent[][] getMatrix() {
        return this.matrix;
    }

}
