package org.jboss.as.quickstarts.jms;

public class PropertyReader {

	private static final String DEFAULT_MESSAGE = "Hello, World!";
	private static final int DEFAULT_MESSAGE_COUNT = 3;

	public static int get(String key, int defaultValue) {

		String systemProp = System.getProperty(key);
		if (systemProp == null || systemProp.trim().isEmpty()) {
			return defaultValue;
		} else {
			return Integer.parseInt(systemProp);
		}
	}

	public static String get(String key, String defaultValue) {

		String systemProp = System.getProperty(key);
		if (systemProp == null || systemProp.trim().isEmpty()) {
			return defaultValue;
		} else {
			return systemProp;
		}
	}

	public static String getMessageContent() {
		return get("message.content", DEFAULT_MESSAGE);
	}

	public static int getTotalMessageCount() {
		return get("message.count", DEFAULT_MESSAGE_COUNT);
	}

}
