/*
 * TIGER WEBSERVER. A simple HTTP webserver.
 * Copyright (C) 2021  Benjamin Schmitz
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package org.benjaminschmitz.tiger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.benjaminschmitz.tools.PropertyReader;

/**
 * Starting the Webserver
 * 
 * @author Benjamin Schmitz
 * @see org.benjaminschmitz.tiger.WebServer
 * @version 2021-02-19
 */
public class Run {
	public static final String CONFIGFILE = "tiger.config";
	private final WebServer server;

	/**
	 * Constructor for setting up the {@link WebServer} with the parameters from the
	 * configuration file {@code tiger.config}.
	 * 
	 * @throws InvalidConfigException Exception for an invalid configuration file.
	 *                                More information in the message.
	 */
	public Run() throws InvalidConfigException {
		String version;
		int port;
		String folder;
		int threads;
		int logLevel;
		String host;

		PropertyReader properties;
		try {
			properties = new PropertyReader(CONFIGFILE);
		} catch (IOException e) {
			throw new InvalidConfigException("Invalid config file.");
		}

		version = properties.getProperty("tiger.version");
		if (version == null)
			throw new InvalidConfigException("No version declared.");

		try {
			port = Integer.parseInt(properties.getProperty("tiger.port"));
		} catch (NumberFormatException e) {
			throw new InvalidConfigException("Couldn't parse port.");
		}

		host = properties.getProperty("tiger.host");

		folder = properties.getProperty("tiger.folder");
		if (folder == null || !Files.isDirectory(Path.of(folder)))
			throw new InvalidConfigException("No folder declared or incorrect folder.");
		try {
			threads = Integer.parseInt(properties.getProperty("tiger.threads"));
		} catch (NumberFormatException e) {
			throw new InvalidConfigException("Couldn't parse threads.");
		}
		try {
			logLevel = Integer.parseInt(properties.getProperty("tiger.log"));
		} catch (NumberFormatException e) {
			throw new InvalidConfigException("Couldn't parse log level.");
		}

		server = new WebServer(new Logger(version, logLevel), port, folder, threads,
				new ErrorPageGenerator(version, port, host));
	}

	/**
	 * Running the WebServer. Catching the {@code RuntimeException} and aborting the
	 * execution.
	 */
	public void run() {
		try {
			server.run();
		} catch (RuntimeException e) {
			System.err.println("ERROR: " + e.toString());
			System.err.println("ABORTED");
		}
	}

	/**
	 * Starting the server. Catching the {@code InvalidConfigException} and aborting
	 * the execution.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			new Run().run();
		} catch (InvalidConfigException e) {
			System.err.println("ERROR: " + e.toString());
			System.err.println("ABORTED");
		}
	}

}
