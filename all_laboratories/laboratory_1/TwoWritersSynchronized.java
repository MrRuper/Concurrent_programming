// Here the volatile variable is used to 
// show how we can force program to control              
// threads. In next notes the semaphor will be
// introduced.
public class TwoWritersSynchronized {

    private static volatile boolean firstDone = false;
    private static volatile boolean secondDone = false;

    private static volatile int currentId;

    private static class Writer implements Runnable {

        private static final int LINES_COUNT = 100;
        private static final int LINE_LENGTH = 50;

        private final char first;
        private final char last;
        private final int id;

        public Writer(char first, char last, int id) {
            this.first = first;
            this.last = last;
            this.id = id;
        }

        @Override
        public void run() {
            char c = first;

            // Main thread loop.
            for (int i = 0; i < LINES_COUNT; i++) {
                
                // Wait untill another process
                // will change our id.
                while (currentId != id) {
                    // empty
                }

                // Line printing.
                for (int j = 0; j < LINE_LENGTH; j++) {
                    System.out.print(c);
                    c++;

                    if (c > last) {
                        c = first;
                    }
                }

                System.out.println();

                // Change currentId from 0 to 1
                // or from 1 to 0.
                currentId = 1 - currentId;
            }

            if (id == 0) {
                firstDone = true;
            }
             else {
                secondDone = true;
            }
        }

    }

    public static void main(String args[]) {
        Thread first = new Thread(new Writer('a', 'z', 0));
        Thread second = new Thread(new Writer('0', '9', 1));

        currentId = 0;

        System.out.println("Początek");
        first.start();
        second.start();

        // Active waiting on the end of 
        // threads. This is quite bad approach
        // because of the processor time wasting.
        while (!firstDone) {
            // pusta
        }
        while (!secondDone) {
            // pusta
        }


        System.out.println("Koniec");
    }

}