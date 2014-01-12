package org.jboss.as.quickstarts.jms;

import java.util.logging.Logger;

import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.TextMessage;

public class MessageSender {
	private static final Logger log = Logger.getLogger(MessageSender.class
			.getName());

	private MessageProducer producer = null;

	private JmsSession jmsSession;

	public MessageSender(JmsSession jmsSession) throws JMSException {
		this.jmsSession = jmsSession;
		this.producer = jmsSession.createProducer();
	}

	public void sendMessage(String content) throws JMSException {
		String currentThreadName = Thread.currentThread().getName();
		log.info(currentThreadName + " : Sending message with content: "
				+ content);

		TextMessage message = jmsSession.createTextMessage(content);
		producer.send(message);
	}

}
