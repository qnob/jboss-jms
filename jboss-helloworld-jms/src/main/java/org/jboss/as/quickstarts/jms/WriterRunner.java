package org.jboss.as.quickstarts.jms;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class WriterRunner {
	private static final int INTERVAL_IN_MILLISECONDS = 1000;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ScheduledExecutorService scheduledExecutorService = Executors
				.newScheduledThreadPool(1, new WorkerThreadFactory("Writer"));

		for (int i = 0; i < 3; i++) {

			try {

				scheduledExecutorService.schedule(new WriterWorker(i), i
						* INTERVAL_IN_MILLISECONDS, TimeUnit.MILLISECONDS);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		scheduledExecutorService.shutdown();
		while (!scheduledExecutorService.isTerminated()) {
		}
		System.out.println("Finished all threads");
	}
}
