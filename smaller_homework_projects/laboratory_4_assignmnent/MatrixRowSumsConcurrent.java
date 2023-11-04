import java.util.ArrayList;
import java.util.List;
import java.util.function.IntBinaryOperator;
import java.util.concurrent.atomic.AtomicInteger;

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
            int[] rowSums = new int[numRows];
            List<Thread> threads = new ArrayList<>();

            // Define Atomic row_sum and set their initial values on 0.
            AtomicInteger[] vector_sum = new AtomicInteger[NUM_ROWS];

            for (int i = 0; i < NUM_ROWS; i++) {
                vector_sum[i] = new AtomicInteger(0);
            }

            // Adding all threads to the list.
            for (int columnNo = 0; columnNo < numColumns; columnNo++) {
                Thread t = new Thread(new PerColumnDefinitionApplier(columnNo, 
                                      vector_sum, 
                                      Thread.currentThread(),
                                      definition));
                threads.add(t);
            }
            for (Thread t : threads) {
                t.start();
            }

            for (Thread t : threads) {
                try {
                    t.join();
                }
                catch (InterruptedException e) {
                    System.out.println("Thread interrupted");
                    System.exit(1);
                }
            }

            for (int i = 0; i < NUM_ROWS; i++) {
                rowSums[i] = vector_sum[i].get();
            }

            return rowSums;
        }

        private class PerColumnDefinitionApplier implements Runnable {
            private final int myColumnNo;
            private final AtomicInteger[] vector_sum;
            private final Thread mainThread;
            IntBinaryOperator definition;

            private PerColumnDefinitionApplier(
                    int myColumnNo,
                    AtomicInteger[] vector_sum,
                    Thread mainThread,
                    IntBinaryOperator definition) {
                        this.myColumnNo = myColumnNo;
                        this.mainThread = mainThread;
                        this.vector_sum = vector_sum;
                        this.definition = definition;
            }

            @Override
            public void run() {
                int add_to_row;

                for (int i = 0; i < NUM_ROWS; i++) {
                    add_to_row = definition.applyAsInt(i, myColumnNo);
                    vector_sum[i].addAndGet(add_to_row);
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

        } catch (InterruptedException e) {
            System.err.println("Obliczenie przerwane");
            return;
        }
    }

}