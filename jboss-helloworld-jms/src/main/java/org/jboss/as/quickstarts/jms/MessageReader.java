package org.jboss.as.quickstarts.jms;

import java.util.logging.Logger;

import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.TextMessage;

public class MessageReader {
	private static final Logger log = Logger
			.getLogger(MessageReader.class.getName());

	private MessageConsumer consumer = null;

	public MessageReader(JmsSession jmsSession) throws JMSException {
		this.consumer = jmsSession.createConsumer();
	}
	
	public void readMessage(int count) throws JMSException {
		// Then receive the same number of messages that were sent
		for (int i = 0; i < count; i++) {
			TextMessage message = (TextMessage) consumer.receive(5000);
			log.info("Received message with content " + message.getText());
		}
	}	

}
