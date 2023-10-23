public class ManyThreads {
	public static class Helper implements Runnable {
		private final int MAX_ID = 1000;

		@Override 
		public void run() {
			if (Thread.currentThread().getId() < MAX_ID) {
				Thread t = new Thread(new Helper());
				t.start();
				double how_long = Math.random();

				try {
					Thread.sleep((long)(how_long * 100));
				}
				catch(InterruptedException e) {
					System.err.println("Thread was interrupted: " + e.getMessage());
				}

				System.out.println(Thread.currentThread().getId());
			}
		}
	}

	public static void main(String[] args) {
		Thread t1 = new Thread(new Helper());
		t1.start();

		
		
		while (Thread.activeCount() > 1) {
			// Wait
		}
	}
}