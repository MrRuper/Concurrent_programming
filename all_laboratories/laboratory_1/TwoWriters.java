// Here two Threads are presented. 
// The first Thread is printing 5000 times letters 
// from 'a' to 'z' the second thread is printing 
// concurrently numbers from '0' to '9'.
// After they finish we end the program.

public class TwoWriters {

    // Here we define run() method and all needed 
    // stuff for Threads.
    public static class Writer implements Runnable {

        // This constant describes how much times
        // some char will be written.
        static final int CHAR_NUMBER = 5000;

        // This two chars describe the range of 
        // printed chars.
        private char first_char;
        private char last_char;
        private char c;

        public Writer(char first_char, char last_char) {
            this.first_char = first_char;
            this.last_char = last_char;
            this.c = first_char;
        }

        @Override
        public void run() {
            for (int i = 0; i < CHAR_NUMBER; i++) {
                System.out.print(c);
                c++;

                if (c > last_char) {
                    c = first_char;
                }
            }
        }
    }

    public static void printLine() {
            System.out.println();
            System.out.println("END");
    }

    public static void main(String[] args) {
        Thread t1 = new Thread(new Writer('a', 'z'));
        Thread t2 = new Thread(new Writer('0', '9'));
        t1.start();
        t2.start();

        // This is not afficient because 
        // isAlive() is costly.
        // It is better to use 
        // t1.join();
        // t2.join();
        while (t1.isAlive() || t2.isAlive()) {
            // Nothing to do.
        }

        printLine();
    }
}
