package org.jboss.as.quickstarts.jms.core;

import java.util.logging.Logger;

import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.TextMessage;

public class MessageReader {
	private static final Logger log = Logger.getLogger(MessageReader.class
			.getName());

	private MessageConsumer consumer = null;

	public MessageReader(JmsSession jmsSession) throws JMSException {
		this.consumer = jmsSession.createConsumer();
	}

	public void readMessage() throws JMSException {
		// Then receive the same number of messages that were sent
			TextMessage message = (TextMessage) consumer.receive(1000);
			String currentThreadName = Thread.currentThread().getName();
			if (message == null) {
				log.info(currentThreadName + " : No message received within given timeout. Giving up!");
			} else {
				log.info(currentThreadName + " : Received message with content " + message.getText());
			}
	}

}
