// To create another thread we need to 
// create run method in the class "Helper" which implements 
// Runnable class. After that in main method we create
// an object of the class Helper (lets say we name it r)
// and the Thread object. In thread object we give as an argument.
public class TwoThreads {
	public static void printName() {
		System.out.println(Thread.currentThread());
	}

	private static class Helper implements Runnable {

		@Override
		public void run() {
			printName();
		}
	}

	public static void main(String[] args) {
		Thread.currentThread().setName("MAIN_THREAD");
		Runnable r = new Helper();
		Thread t = new Thread(r, "auxillary_thread");

		t.start();
		printName();
	}
}