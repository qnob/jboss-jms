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

import java.util.logging.Logger;

import javax.jms.JMSException;
import javax.naming.NamingException;

public class HelloWorldJMSClient {
	private static final Logger log = Logger
			.getLogger(HelloWorldJMSClient.class.getName());

	private MessageSender sender = null;
	private MessageReader reader = null;

	private JmsSession jmsSession = new JmsSession();

	public static void main(String[] args) throws Exception {

		HelloWorldJMSClient client = new HelloWorldJMSClient();
		client.sendAndReceiveMessage();
	}

	public void sendAndReceiveMessage() throws Exception, NamingException,
			JMSException {

		try {
			String content = PropertyReader.getMessageContent();

			jmsSession.setup();
			sender = new MessageSender(jmsSession);
			reader = new MessageReader(jmsSession);

			sender.sendMessage(content);
			reader.readMessage();
		} catch (Exception e) {
			handleException(e);
		} finally {
			jmsSession.close();
		}
	}

	private void handleException(Exception e) throws Exception {
		log.severe(e.getMessage());
		throw e;
	}

}
