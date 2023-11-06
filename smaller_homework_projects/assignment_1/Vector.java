import java.util.Arrays;

public class Vector {
	private static final int BLOCK_LENGTH = 10;
	private int[] vector;

	public Vector(int[] vector) {
		this.vector = Arrays.copyOf(vector, vector.length);
	}

	// For testing.
	public Vector sum_sequentially(Vector other) {
		if (vector.length != other.vector.length) {
			throw new IllegalArgumentException("Different lengths of dotted vectors.");
		}

		for (int i = 0; i < vector.length; i++) {
			vector[i] += other.vector[i];
		}

		return this;
	}

	public Vector sum_concurrently(Vector other) {
		if (vector.length != other.vector.length) {
			throw new IllegalArgumentException("Different lengths of dotted vectors.");
		}

		// Each thread takes at most 10 elements to sum and thus
		// we firstly find the needed number of threads to do
		// this work.
		int number_of_threads = vector.length / BLOCK_LENGTH;

		// If for example vector.length is 33 then the last thread
		// has to deal only with 3 elements.
		int last_block_length = vector.length - BLOCK_LENGTH * number_of_threads;

		if (last_block_length > 0) {
			number_of_threads++;
		}

		// To correctly return the answer we need to wait until
		// all threads will finish their job. To do so we save all threads
		// in array and the main thread will wait on their finish.
		Thread[] all_threads = new Thread[number_of_threads];

		// We additionally define answer[] for safety even when
		// we know that in that problem all threads are "independent".
		int[] answer = new int[vector.length];

		for (int i = 0; i < number_of_threads; i++) {
			Runnable r;

			if (i < number_of_threads - 1) {
				r = new Sum(BLOCK_LENGTH * i,
							BLOCK_LENGTH * i + BLOCK_LENGTH - 1,
							vector,
							other.vector,
							answer);
			}
			else { // Deal with last thread.
				if (last_block_length > 0) {
					r = new Sum(BLOCK_LENGTH * i,
								BLOCK_LENGTH * i + last_block_length - 1,
								vector,
								other.vector,
								answer);
				}
				else {
					r = new Sum(BLOCK_LENGTH * i,
								BLOCK_LENGTH * i + BLOCK_LENGTH - 1,
								vector,
								other.vector,
								answer);
				}
			}

			Thread t = new Thread(r);

			// Save thread.
			all_threads[i] = t;

			t.start();
		}

		// Here we wait on all created threads.
		// The join() method could throw an exception
		// and thus we say what to do in the catch block.
		for (int i = 0; i < number_of_threads; i++) {
			try {
				all_threads[i].join();
			}
			catch(InterruptedException e) {
				System.out.println("Thread was interrupted during computations");
				
				System.exit(1);
			}
		}

		// Rewrite our vector.
		this.vector = answer;

		return this;
	}

	private static class Sum implements Runnable {
		// This are indexes of start / end of the new block to sum.
		int start_index;
		int end_index;
		int[] first_vector; // This will hold answer.
		int[] second_vector;
		int[] answer;

		// Constructor.
		public Sum(int start_index,
				   int end_index,
				   int[] first_vector,
				   int[] second_vector,
				   int[] answer) {
			this.start_index = start_index;
			this.end_index = end_index;
			this.first_vector = first_vector;
			this.second_vector = second_vector;
			this.answer = answer;
		}

		@Override
		public void run() {
			for (int i = start_index; i <= end_index; i++) {
				answer[i] = first_vector[i] + second_vector[i];
			}
		}
	}


	public static void main(String[] args) {
		// One small test. You can try to generate more
		// with Random vectors.
		int[] first_arr = new int[33];
		int[] second_arr = new int[33];

		for (int i = 0; i < 33; i++) {
			first_arr[i] = i;
			second_arr[i] = i + 33;
		}

		Vector p1 = new Vector(first_arr);
		Vector p2 = new Vector(second_arr);
		Vector p3 = new Vector(first_arr);

		p1.sum_sequentially(p2);
		p3.sum_concurrently(p2);

		System.out.println(Arrays.equals(p1.vector, p3.vector));
	}
}