package org.jboss.as.quickstarts.jms;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import org.jboss.as.quickstarts.jms.core.JmsSession;
import org.jboss.as.quickstarts.jms.core.MessageBrowser;
import org.jboss.as.quickstarts.jms.worker.ReaderWorker;
import org.jboss.as.quickstarts.jms.worker.WorkerThreadFactory;

public class ReaderRunner {

	private static final int NB_OF_READER_THREADS = 3;
	private static final Logger log = Logger.getLogger(ReaderRunner.class
			.getName());

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ExecutorService executorService = Executors.newFixedThreadPool(5,
				new WorkerThreadFactory("Reader"));

		int totalMessageCount = ConfigurationReader.getTotalMessageCount();
		int threadMessageCount = totalMessageCount / NB_OF_READER_THREADS;

		for (int i = 0; i < NB_OF_READER_THREADS; i++) {

			try {
				executorService.execute(new ReaderWorker(threadMessageCount));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		executorService.shutdown();
		try {
			executorService.awaitTermination(5, TimeUnit.MINUTES);
		} catch (InterruptedException e1) {
			log.severe("Reader threads interrupted after timeout.");
		}
		log.info("Finished all threads");

		JmsSession jmsSession = new JmsSession();
		try {
			jmsSession.setup();
			MessageBrowser browser = new MessageBrowser(jmsSession);
			int remainingMessagesCount = browser.getMessageCount();

			if (remainingMessagesCount > 0) {
				log.severe("There are '" + remainingMessagesCount
						+ "' remaining messages in the queue.");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
