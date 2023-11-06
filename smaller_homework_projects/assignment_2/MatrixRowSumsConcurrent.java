import java.util.concurrent.CyclicBarrier;
import java.util.function.IntBinaryOperator;
import java.util.concurrent.BrokenBarrierException;

public class MatrixRowSumsConcurrent {
    private static final int NUM_ROWS = 10;
    private static final int NUM_COLUMNS = 100;

    private static class Matrix {
        private final int numRows;
        private final int numColumns;
        private final IntBinaryOperator definition;

        public Matrix(int numRows, int numColumns, IntBinaryOperator definition) {
            this.numRows = numRows;
            this.numColumns = numColumns;
            this.definition = definition;
        }

        public int[] rowSums() throws InterruptedException {
            // This constant keeps the current 
            // row index. The reason why it is 
            // an one element array is that 
            // we update it in barrierAction inner class
            // which needs "final" type of variable.
            final int[] ROW_NUMBER = new int[]{-1};

            // row_sum[] holds the final answer.
            int[] row_sum = new int[numRows];

            // This array stores the current processing
            // row.
            int[] single_row_sum = new int[numRows];

            // Create method for barrier to do when 
            // all threads hitted it.
            Runnable barrierAction = new Runnable() {
                @Override
                public void run() {
                    // Update the current row index.
                    ROW_NUMBER[0] = ROW_NUMBER[0] + 1;

                    // Technical value for the sum.
                    int x = 0;

                    // Sum elements in the single_row_sum array.
                    for (int i = 0; i < row_sum.length; i++) {
                        x += single_row_sum[i];
                    }

                    // Save the result.
                    row_sum[ROW_NUMBER[0]] = x;
                }
            };

            // Initiate cyclic barrier which waits on numRows threads
            // and will do barrierAction at the end.
            CyclicBarrier barrier = new CyclicBarrier(numRows, barrierAction);

            // Dynamically create threads.
            for (int i = 0; i < numColumns; i++) {
                Runnable r = new RowSummer(numRows,
                                           i,
                                           definition,
                                           barrier,
                                           single_row_sum);
                Thread t = new Thread(r);
                t.start();
            }

            return row_sum;
        }

        private class RowSummer implements Runnable {
            int numRows;
            int my_column_number;
            IntBinaryOperator definition;
            CyclicBarrier barrier;
            int[] single_row_sum;

            public RowSummer(int numRows,
                             int my_column_number,
                             IntBinaryOperator definition,
                             CyclicBarrier barrier,
                             int[] single_row_sum) {
                this.numRows = numRows;
                this.my_column_number = my_column_number;
                this.definition = definition;
                this.barrier = barrier;
                this.single_row_sum = single_row_sum;
            }
            
            @Override
            public void run() {    
                // Will keep the current matrix
                // value given in "definition" object.
                int matrix_value;

                for (int i = 0; i < numRows; i++) {
                    matrix_value = definition.applyAsInt(i, my_column_number);
                    single_row_sum[my_column_number] = matrix_value;

                    try{
                        barrier.await();
                    }
                    catch(InterruptedException | BrokenBarrierException e) {
                        Thread t = Thread.currentThread();

                        t.interrupt();
                        System.err.println(t.getName() + " interrupted");
                    }
                }
            }
        }
    }


    // Simple test in main method.
    public static void main(String[] args) {
        Matrix matrix = new Matrix(NUM_ROWS, NUM_COLUMNS, (row, column) -> {
            int a = 2 * column + 1;

            return (row + 1) * (a % 4 - 2) * a;
        });
        try {
            int[] rowSums = matrix.rowSums();

            for (int i = 0; i < rowSums.length; i++) {
                System.out.println(i + " -> " + rowSums[i]);
            }

        }
        catch (InterruptedException e) {
            System.err.println("Computations are interrupted");

            return;
        }
    }
}