package org.benjaminschmitz.tools;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Easy reading of a config file.
 * 
 * @author Benjamin Schmitz
 * @see org.benjaminschmitz.tools
 * @version 09.02.2021
 *
 */
public class PropertyReader {
	private final Properties properties;

	/**
	 * Making the Properties-Object
	 * 
	 * @param file name of the config file
	 * @throws IOException
	 */
	public PropertyReader(String file) throws IOException {
		InputStream stream = null;
		stream = new FileInputStream(file);
		properties = new Properties();
		properties.load(stream);
		stream.close();
	}

	/**
	 * Returning the String of the property
	 * 
	 * @param property key of the property
	 * @return String value of the property
	 */
	public String getProperty(String property) {
		return properties.getProperty(property);
	}
}
