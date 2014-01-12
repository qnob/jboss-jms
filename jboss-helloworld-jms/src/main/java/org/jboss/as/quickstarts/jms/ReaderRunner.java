package org.jboss.as.quickstarts.jms;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ReaderRunner {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ExecutorService executorService = Executors
				.newFixedThreadPool(5, new WorkerThreadFactory("Reader"));

		for (int i = 0; i < 3; i++) {

			try {
				executorService.execute(new ReaderWorker());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		executorService.shutdown();
		while (!executorService.isTerminated()) {
		}
		System.out.println("Finished all threads");
	}
}
