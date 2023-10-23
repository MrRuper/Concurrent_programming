// One Thread example. Here the only one thread will be 
// created in the main procedure.
public class SingleThread {
	public static void main(String[] args) {
		// currentThread is returning the currently 
		// working thread. In our case this will be 
		// the main thread because nothing else is 
		// created.
		Thread t = Thread.currentThread();

		// By Thread.setName() command you 
		// can name your thread.
		t.setName("Main_Thread");

		// Print out all information about created thread.
		System.out.println("Thread " + t);
		System.out.println("Name of the thread is: " + t.getName());
		System.out.println("The thread priority: " + t.getPriority());
		System.out.println("Thread group is: " + t.getThreadGroup().getName());
	}
}