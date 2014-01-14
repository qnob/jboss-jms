package org.jboss.as.quickstarts.jms.core;

import java.util.Enumeration;
import java.util.logging.Logger;

import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.QueueBrowser;
import javax.jms.TextMessage;

public class MessageBrowser {
	private static final Logger log = Logger.getLogger(MessageBrowser.class
			.getName());

	private QueueBrowser browser = null;

	public MessageBrowser(JmsSession jmsSession) throws JMSException {
		this.browser = jmsSession.createQueueBrowser();
	}

	public int getMessageCount() throws JMSException {
		int remainingMessagesCount = 0;
		@SuppressWarnings({ "rawtypes" })
		Enumeration messagesInQueue = browser.getEnumeration();

		while (messagesInQueue.hasMoreElements()) {
			messagesInQueue.nextElement();
			remainingMessagesCount++;
		}

		return remainingMessagesCount;
	}

}
