public class TwoThreads {

	public static class Helper implements Runnable {

		@Override
		public void run() {
			System.out.println(Thread.currentThread());
		}

	}

	public static void main(String[] args) {
		Runnable r = new Helper();
		Thread t = new Thread(r, "Second thread");
		t.start();
	}
}