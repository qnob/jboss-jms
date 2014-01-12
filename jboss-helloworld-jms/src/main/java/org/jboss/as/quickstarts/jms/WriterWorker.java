/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.as.quickstarts.jms;

import java.util.Random;
import java.util.logging.Logger;

import javax.jms.JMSException;
import javax.naming.NamingException;

public class WriterWorker implements Runnable {
	private static final int MAX_RANDOM_WAIT_TIME = 100;

	private static final Logger log = Logger.getLogger(WriterWorker.class
			.getName());

	// Set up all the default values
	private static final String DEFAULT_MESSAGE = "Hello, World!";
	private static final String DEFAULT_MESSAGE_COUNT = "1";

	private MessageSender sender = null;

	private JmsSession jmsSession = new JmsSession();

	private int messageCounter;
	
	private Random random=new Random(); 

	public WriterWorker(int messageCounter) throws JMSException, NamingException {
		this.jmsSession = new JmsSession();
		jmsSession.setup();
		this.sender = new MessageSender(jmsSession);
		this.messageCounter = messageCounter;
	}

	@Override
	public void run() {
		try {
			
			Thread.sleep(random.nextInt(MAX_RANDOM_WAIT_TIME));
		} catch (Exception e) {
			handleException(e);
		}

		try {
			sendMessage();
		} catch (Exception e) {
			handleException(e);
		} finally {
			try {
				jmsSession.close();
			} catch (Exception e) {
				handleException(e);
			}
		}

	}

	private void sendMessage() throws Exception, NamingException,
			JMSException {

		int count = Integer.parseInt(System.getProperty("message.count",
				DEFAULT_MESSAGE_COUNT));
		String content = System.getProperty("message.content", DEFAULT_MESSAGE)
				+ "(" + messageCounter + ")";

		sender.sendMessage(count, content);
	}

	private void handleException(Exception e) {
		log.severe(e.getMessage());
	}

}
