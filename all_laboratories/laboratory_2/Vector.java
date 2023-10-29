import java.util.Arrays;
import java.util.Random;

public class Vector {

    private static final int SUM_CHUNK_LENGTH = 10;
    private static final int DOT_CHUNK_LENGTH = 10;

    private final int[] elements;

    public Vector(int length) {
        this.elements = new int[length];
    }

    public Vector(int[] elements) {
        this.elements = Arrays.copyOf(elements, elements.length);
    }

    public static class Summer implements Runnable {
        int[] array1;
        int[] array2;
        int start, end;
        int[] ans;

        
        public Summer(int[] array1, int[] array2, int start, int end, int[] ans) {
            this.array1 = array1;
            this.array2 = array2;
            this.start = start;
            this.end = end;
            this.ans = ans;
        }

        @Override
        public void run() {
            for (int i = start; i <= end; i++) {
                ans[i] = array1[i] + array2[i];
            }

        }
    }


    public Vector sum(Vector other) throws InterruptedException {
        if (this.elements.length != other.elements.length) {
            throw new IllegalArgumentException("different lengths of summed vectors");
        }
        Vector result = new Vector(this.elements.length);

        // Find number of 10 elements fragments.
        int number_of_baches = this.elements.length / SUM_CHUNK_LENGTH;

        // Stores the remainder when divided by 10.
        int remainder = this.elements.length - SUM_CHUNK_LENGTH * number_of_baches;
        boolean change = false;

        if (remainder != 0) {
            change = true;
            number_of_baches += 1;
        }

        // Dynamic allocation of Threads.
        Thread[] all_Thread = new Thread[number_of_baches];

        // start and end indexes of each thread.
        int start, end = 0;

        for (int j = 0; j < number_of_baches; j++) {

            // This code handels the boundary case
            // when batch length is not 10.
            if (j == (number_of_baches - 1) && change) {
                start = end + 1;
                end = this.elements.length - 1;
            }
            else { // here fragments are of lenght 10.
                start = SUM_CHUNK_LENGTH * j;
                end = start + (SUM_CHUNK_LENGTH - 1);
            }

            // Create thread.
            Runnable r = new Summer(this.elements, other.elements, start, end, result.elements);
            all_Thread[j] = new Thread(r);
            all_Thread[j].start();
        }

        // Wait till all threads are finished.
        for (Thread j : all_Thread) {
            try{
                j.join();
            }
            catch(InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("Interrupted");
            }
        }

        return result;
    }


    public int dot(Vector other) throws InterruptedException {
        if (this.elements.length != other.elements.length) {
            throw new IllegalArgumentException("different lengths of dotted vectors");
        }

        int result = 0;

        // Find number of 10 elements fragments.
        int number_of_baches = this.elements.length / SUM_CHUNK_LENGTH;

        // Stores the remainder when divided by 10.
        int remainder = this.elements.length - SUM_CHUNK_LENGTH * number_of_baches;
        boolean change = false;

        if (remainder != 0) {
            change = true;
            number_of_baches += 1;
        }

        // Dynamic allocation of Threads.
        Thread[] all_Thread = new Thread[number_of_baches];

        // Results of different threads are stored in partial_result.
        int partial_result[] = new int[number_of_baches];

        // start and end indexes of each thread.
        int start, end = 0;

        for (int j = 0; j < number_of_baches; j++) {

            // This code handels the boundary case
            // when batch length is not 10.
            if (j == (number_of_baches - 1) && change) {
                start = end + 1;
                end = this.elements.length - 1;
            } else { // here fragments are of lenght 10.
                start = SUM_CHUNK_LENGTH * j;
                end = start + (SUM_CHUNK_LENGTH - 1);
            }

            // Create thread.
            Runnable r = new Dotter(this, other, start, end, partial_result, j);
            all_Thread[j] = new Thread(r);
            all_Thread[j].start();
        }

        // Wait till all threads are finished.
        for (Thread j : all_Thread) {
            try{
                j.join();
            }
            catch(InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("Thread interrupted");
                //main().interrupt();     
            }
        }

        // Sum partial results.
        for (int j : partial_result) {
            result += j;
        }

        return result;
    }

    private static class Dotter implements Runnable {

        private final Vector leftVec;
        private final Vector rightVec;
        private final int startPosInc;
        private final int len;
        private final int[] resVec;
        private final int resPos;

        public Dotter(Vector leftVec, Vector rightVect, int startPosInc, int len, int[] resVec, int resPos) {
            this.leftVec = leftVec;
            this.rightVec = rightVect;
            this.startPosInc = startPosInc;
            this.len = len;
            this.resVec = resVec;
            this.resPos = resPos;
        }

        @Override
        public void run() {
            for (int j = startPosInc; j < len; j++) {
                resVec[resPos] += leftVec.elements[j] * rightVec.elements[j];
            }
        }

    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Vector)) {
            return false;
        }
        Vector other = (Vector) obj;
        return Arrays.equals(this.elements, other.elements);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(this.elements);
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder("[");
        for (int i = 0; i < this.elements.length; ++i) {
            if (i > 0) {
                s.append(", ");
            }
            s.append(this.elements[i]);
        }
        s.append("]");
        return s.toString();
    }

    // ----------------------- TESTS -----------------------

    private static final Random RANDOM = new Random();

    private static Vector generateRandomVector(int length) {
        int[] a = new int[length];
        for (int i = 0; i < length; ++i) {
            a[i] = RANDOM.nextInt(10);
        }
        return new Vector(a);
    }

    private final Vector sumSequential(Vector other) {
        if (this.elements.length != other.elements.length) {
            throw new IllegalArgumentException("different lengths of summed vectors");
        }

        Vector result = new Vector(this.elements.length);
        for (int i = 0; i < result.elements.length; ++i) {
            result.elements[i] = this.elements[i] + other.elements[i];
        }
        return result;
    }

    private final int dotSequential(Vector other) {
        if (this.elements.length != other.elements.length) {
            throw new IllegalArgumentException("different lengths of dotted vectors");
        }
        int result = 0;
        for (int i = 0; i < this.elements.length; ++i) {
            result += this.elements[i] * other.elements[i];
        }
        return result;
    }

    public static void main(String[] args) {
        try {
            Vector a = generateRandomVector(33);
            System.out.println(a);
            Vector b = generateRandomVector(33);
            System.out.println(b);
            Vector c = a.sum(b);
            System.out.println(c);
            assert (c.equals(a.sumSequential(b)));
            int d = a.dot(b);
            System.out.println(d);
            assert (d == a.dotSequential(b));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("computations interrupted");
        }
    }

}