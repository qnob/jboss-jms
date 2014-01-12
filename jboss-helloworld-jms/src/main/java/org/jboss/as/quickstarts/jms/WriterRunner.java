package org.jboss.as.quickstarts.jms;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

public class WriterRunner {
	private static final Logger log = Logger.getLogger(WriterRunner.class
			.getName());
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int totalMessageCount = PropertyReader.getTotalMessageCount();
		ExecutorService executorService = Executors
				.newSingleThreadExecutor(new WorkerThreadFactory("Writer"));

		try {
			executorService.execute(new WriterWorker(totalMessageCount));
		} catch (Exception e) {
			e.printStackTrace();
		}
		executorService.shutdown();
		while (!executorService.isTerminated()) {
		}
		
		log.info("Finished all threads");
	}
}
