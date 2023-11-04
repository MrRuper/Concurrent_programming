import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.function.IntBinaryOperator;

public class MatrixRowSumsConcurrent {

    private static final int NUM_ROWS = 10;
    private static final int NUM_COLUMNS = 100;

    private static class Matrix {
        public int[] all_sum = new int[NUM_ROWS];
        private final int numRows;
        private final int numColumns;
        public final IntBinaryOperator definition;

        public Matrix(int numRows, int numColumns, IntBinaryOperator definition) {
            this.numRows = numRows;
            this.numColumns = numColumns;
            this.definition = definition;
        }

        public int[] rowSums() throws InterruptedException {
            CyclicBarrier barrier =  new CyclicBarrier(NUM_COLUMNS + 1);
            CyclicBarrier barrier2 = new CyclicBarrier(NUM_COLUMNS + 1);
            int[] row_save = new int [NUM_COLUMNS];
            Thread[] all_threads = new Thread[NUM_COLUMNS];

            for (int counter = 0; counter < NUM_COLUMNS; counter++){
                Runnable r = new PerColumnDefinitionApplier(counter, barrier, barrier2, definition, row_save);

                all_threads[counter] = new Thread(r);
                all_threads[counter].start();
            }


            for (int i = 0; i < NUM_ROWS; i++) {
                int sum = 0;

                try{
                    barrier.await();
                    for (int j = 0; j < NUM_COLUMNS; j++) {
                        sum += row_save[j];
                    }

                    all_sum[i] = sum;
                    barrier2.await();
                }
                catch(InterruptedException | BrokenBarrierException e) {
                    System.out.println("some message");
                    System.exit(1);
                }


            }


            for (int j = 0; j < NUM_COLUMNS; j++) {
                try {
                    all_threads[j].join();
                }
                catch(InterruptedException e) {
                    System.err.println("Interrupted");
                    System.exit(1);
                }
            }



            return all_sum;
        }


        private class PerColumnDefinitionApplier implements Runnable {
            private final int myColumnNo;
            private final CyclicBarrier barrier;
            private final CyclicBarrier barrier2;
            private IntBinaryOperator definition;
            public int[] row_sum;


            private PerColumnDefinitionApplier(
                    int myColumnNo,
                    CyclicBarrier barrier,
                    CyclicBarrier barrier2,
                    IntBinaryOperator definition,
                    int[] row_sum) {
                this.myColumnNo = myColumnNo;
                this.barrier = barrier;
                this.barrier2 = barrier2;
                this.definition = definition;
                this.row_sum = row_sum;
            }

            @Override
            public void run() {
                for (int row_number = 0; row_number < NUM_ROWS; row_number++) {
                    // Summing.
                    row_sum[myColumnNo] = definition.applyAsInt(row_number, myColumnNo);

                    // Synchronized.
                    try{
                        barrier.await();
                        barrier2.await();
                    }
                    catch (InterruptedException | BrokenBarrierException e){
                        System.err.println("Interrupted");
                        System.exit(1);
                    }
                }


            }

        }
    }

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
            System.err.println("Obliczenie przerwane");

            return;
        }
    }
}