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
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * WebServer class for the main server application.
 * 
 * @author Benjamin Schmitz
 * @see org.benjaminschmitz.tiger
 * @version 2021-02-19
 */
public class WebServer {
	private final int PORT;
	private final String FOLDER;
	private final int THREADS;
	private final ErrorPageGenerator GENERATOR;
	private final Logger LOGGER;

	/**
	 * Initialising the server.
	 * 
	 * @param logger    {@link Logger} instance.
	 * @param port      Port for the application.
	 * @param folder    Root folder for files.
	 * @param threads   Number of threads handling incoming connections.
	 * @param generator {@link ErrorPageGenerator} for error pages.
	 */
	public WebServer(Logger logger, int port, String folder, int threads, ErrorPageGenerator generator) {
		LOGGER = logger;
		PORT = port;
		FOLDER = folder;
		THREADS = threads;
		GENERATOR = generator;

	}

	/**
	 * Running the server.
	 */
	public void run() {
		LOGGER.info("Server started");
		LOGGER.resetStart();

		Thread runtime = new Thread(new Runnable() {
			@Override
			public void run() {
				while (!Thread.currentThread().isInterrupted()) {
					LOGGER.runtime();
					try {
						Thread.sleep(5 * 60 * 1000);
					} catch (InterruptedException e) {
					}
				}
			}
		});
		runtime.start();

		final ServerSocket socket;
		try {
			socket = new ServerSocket(PORT);
		} catch (IOException e) {
			LOGGER.severe("Couldn't bind on port " + PORT);
			throw new RuntimeException("Couldn't bind on port " + PORT);
		}

		ExecutorService pool = Executors.newFixedThreadPool(THREADS);
		while (!Thread.currentThread().isInterrupted()) {
			Socket sock;
			try {
				sock = socket.accept();
			} catch (IOException e) {
				sock = null;
				LOGGER.severe("Error getting connection.");
			}
			final Socket s = sock;

			if (s != null) {
				pool.submit(() -> HandleConnection.handleConnection(LOGGER, s, GENERATOR, FOLDER));

			}
		}

		LOGGER.severe("Server stopped!");
		runtime.interrupt();
		pool.shutdownNow();

		try {
			socket.close();
		} catch (IOException e) {
			LOGGER.severe("Couldn't close server socket.");
		}
	}
}
