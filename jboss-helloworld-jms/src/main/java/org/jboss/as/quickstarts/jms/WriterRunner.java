package org.jboss.as.quickstarts.jms;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class WriterRunner {
	private static final Logger log = Logger.getLogger(WriterRunner.class
			.getName());
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int totalMessageCount = ConfigurationReader.getTotalMessageCount();
		ExecutorService executorService = Executors
				.newSingleThreadExecutor(new WorkerThreadFactory("Writer"));

		try {
			executorService.execute(new WriterWorker(totalMessageCount));
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			executorService.awaitTermination(5, TimeUnit.MINUTES);
		} catch (InterruptedException e1) {
			log.severe("Reader threads interrupted after timeout.");
		}
		
		log.info("Finished all threads");
	}
}
