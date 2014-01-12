package org.jboss.as.quickstarts.jms;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class ConcurrentWriterAndReaderRunner {
	private static final int NB_OF_READER_THREADS = 3;
	private static final Logger log = Logger.getLogger(ReaderRunner.class
			.getName());

	private ExecutorService readerThreadExecutorService;
	private ExecutorService writerThreadExecutorService;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ConcurrentWriterAndReaderRunner runner = new ConcurrentWriterAndReaderRunner();
		runner.initReaderThreads();
		runner.initWriterThreads();
		runner.awaitWriterThreadTermination();
		runner.awaitReaderThreadTermination();

	}

	public void initReaderThreads() {
		readerThreadExecutorService = Executors.newFixedThreadPool(5,
				new WorkerThreadFactory("Reader"));

		int totalMessageCount = ConfigurationReader.getTotalMessageCount();
		int threadMessageCount = totalMessageCount / NB_OF_READER_THREADS;

		for (int i = 0; i < NB_OF_READER_THREADS; i++) {

			try {
				readerThreadExecutorService.execute(new ReaderWorker(
						threadMessageCount));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	public void initWriterThreads() {
		int totalMessageCount = ConfigurationReader.getTotalMessageCount();
		writerThreadExecutorService = Executors
				.newSingleThreadExecutor(new WorkerThreadFactory("Writer"));

		try {
			writerThreadExecutorService.execute(new WriterWorker(
					totalMessageCount));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void awaitReaderThreadTermination() {
		readerThreadExecutorService.shutdown();

		try {
			readerThreadExecutorService.awaitTermination(5, TimeUnit.MINUTES);
		} catch (InterruptedException e1) {
			log.severe("Reader threads interrupted after timeout.");
		}
		log.info("Finished all reader threads");

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

	public void awaitWriterThreadTermination() {
		writerThreadExecutorService.shutdown();

		try {
			writerThreadExecutorService.awaitTermination(5, TimeUnit.MINUTES);
		} catch (InterruptedException e1) {
			log.severe("Writer threads interrupted after timeout.");
		}

		log.info("Finished all writer threads");
	}
}
