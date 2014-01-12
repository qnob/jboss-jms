package org.jboss.as.quickstarts.jms;

import java.util.Properties;
import java.util.logging.Logger;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.QueueBrowser;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class JmsSession {

	private static final Logger log = Logger
			.getLogger(HelloWorldJMSClient.class.getName());

	// Set up all the default values
	private static final String DEFAULT_CONNECTION_FACTORY = "jms/RemoteConnectionFactory";
	private static final String DEFAULT_DESTINATION = "jms/queue/test";
	private static final String DEFAULT_USERNAME = "quickstartUser";
	private static final String DEFAULT_PASSWORD = "quickstartPwd1!";
	private static final String INITIAL_CONTEXT_FACTORY = "org.jboss.naming.remote.client.InitialContextFactory";
	private static final String PROVIDER_URL = "remote://localhost:4447";

	private ConnectionFactory connectionFactory = null;
	private Connection connection = null;
	private Session session = null;
	private Destination destination = null;
	private Context context = null;

	public void setup() throws NamingException, JMSException {
		if (context == null) {
			log.severe("JMS Session already initialized. Ignoring further setup method calls.");
		}
		// Set up the context for the JNDI lookup
		final Properties env = new Properties();
		env.put(Context.INITIAL_CONTEXT_FACTORY, INITIAL_CONTEXT_FACTORY);
		env.put(Context.PROVIDER_URL,
				System.getProperty(Context.PROVIDER_URL, PROVIDER_URL));
		env.put(Context.SECURITY_PRINCIPAL,
				System.getProperty("username", DEFAULT_USERNAME));
		env.put(Context.SECURITY_CREDENTIALS,
				System.getProperty("password", DEFAULT_PASSWORD));
		context = new InitialContext(env);

		// Perform the JNDI lookups
		String connectionFactoryString = System.getProperty(
				"connection.factory", DEFAULT_CONNECTION_FACTORY);
		log.info("Attempting to acquire connection factory \""
				+ connectionFactoryString + "\"");
		connectionFactory = (ConnectionFactory) context
				.lookup(connectionFactoryString);
		log.info("Found connection factory \"" + connectionFactoryString
				+ "\" in JNDI");

		String destinationString = System.getProperty("destination",
				DEFAULT_DESTINATION);
		log.info("Attempting to acquire destination \"" + destinationString
				+ "\"");
		destination = (Destination) context.lookup(destinationString);
		log.info("Found destination \"" + destinationString + "\" in JNDI");

		// Create the JMS connection, session, producer, and consumer
		connection = connectionFactory.createConnection(
				System.getProperty("username", DEFAULT_USERNAME),
				System.getProperty("password", DEFAULT_PASSWORD));
		session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		connection.start();

	}

	public MessageProducer createProducer() throws JMSException {
		return session.createProducer(destination);
	}

	public MessageConsumer createConsumer() throws JMSException {
		return session.createConsumer(destination);
	}

	public QueueBrowser createQueueBrowser() throws JMSException {
		if (Queue.class.isInstance(destination)) {
			Queue queue = (Queue) destination;
			return session.createBrowser(queue);
		} else {
			log.severe("Destination doens't implement Queue interface.");
			return null;
		}

	}

	public TextMessage createTextMessage(String content) throws JMSException {
		TextMessage message = session.createTextMessage(content);
		return message;
	}

	public void close() throws NamingException, JMSException {
		if (context != null) {
			context.close();
		}

		// closing the connection takes care of the session, producer, and
		// consumer
		if (connection != null) {
			connection.close();
		}
	}

}
