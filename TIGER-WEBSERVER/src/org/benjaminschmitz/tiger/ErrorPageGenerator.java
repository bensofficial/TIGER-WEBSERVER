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

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Generating the error page for tiger webserver.
 * 
 * @author Benjamin Schmitz
 * @see org.benjaminschmitz.tiger.WebServer
 * @version 2021-02-19
 */
public class ErrorPageGenerator {
	private final String version;
	private final int port;
	private final String os;
	private final String host;

	/**
	 * Setting all the needed values.
	 * 
	 * @param version Version of the webserver.
	 * @param port    Port of the application.
	 * @param host    Host name on which the server runs.
	 */
	public ErrorPageGenerator(String version, int port, String host) {
		this.version = version;
		this.port = port;
		if (host == null) {
			String h;
			try {
				h = InetAddress.getLocalHost().getHostName();
			} catch (UnknownHostException e) {
				h = "localhost";
			}
			this.host = h;
		} else {
			this.host = host;
		}
		os = System.getProperty("os.name");
	}

	/**
	 * Generating the HTML error page for the message.
	 * 
	 * @param message Message to be printed on the error page.
	 * @return HTML error page
	 */
	public String errorGenerator(String message) {
		return "<html><body><h1>Error: " + message + "</h1><hr><p>tiger/" + version + " (" + os + ") Server at " + host
				+ " Port " + port + "<p></body></html>";
	}

	/**
	 * Generating the HTML error page for the HttpStatus.
	 * 
	 * @param status HttpStatus of the failed connection.
	 * @return HTML error page
	 */
	public String errorGenerator(HttpStatus status) {
		return "<html><body><h1>" + status.code + ": " + status.toString() + "</h1><hr><p>tiger/" + version + " (" + os
				+ ") Server at " + host + " Port " + port + "<p></body></html>";
	}

	/**
	 * Generating the HTML error page for the HttpStatus and the called path.
	 * 
	 * @param status HttpStatus of the failed connection.
	 * @param path   Path of the called file.
	 * @return HTML error page
	 */
	public String errorGenerator(HttpStatus status, String path) {
		return "<html><body><h1>" + status.code + ": " + status.toString() + " " + path + "</h1><hr><p>tiger/" + version
				+ " (" + os + ") Server at " + host + " Port " + port + "<p></body></html>";
	}
}
