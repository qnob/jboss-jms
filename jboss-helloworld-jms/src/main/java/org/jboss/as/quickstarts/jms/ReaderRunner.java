package org.jboss.as.quickstarts.jms;

import java.util.Enumeration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

import javax.jms.QueueBrowser;

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

		int totalMessageCount = PropertyReader.getTotalMessageCount();
		int threadMessageCount = totalMessageCount / NB_OF_READER_THREADS;

		for (int i = 0; i < NB_OF_READER_THREADS; i++) {

			try {
				executorService.execute(new ReaderWorker(threadMessageCount));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		executorService.shutdown();
		while (!executorService.isTerminated()) {
		}
		log.info("Finished all threads");

		JmsSession jmsSession = new JmsSession();
		try {
			jmsSession.setup();
			QueueBrowser queueBrowser = jmsSession.createQueueBrowser();

			@SuppressWarnings({ "rawtypes" })
			Enumeration messagesInQueue = queueBrowser.getEnumeration();
			int remainingMessagesCount = 0;
			while (messagesInQueue.hasMoreElements()) {
				messagesInQueue.nextElement();
				remainingMessagesCount++;
			}

			if (remainingMessagesCount > 0) {
				log.severe("There are '" + remainingMessagesCount
						+ "' remaining messages in the queue.");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
