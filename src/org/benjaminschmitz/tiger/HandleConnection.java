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

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Handling the connection.
 * 
 * @author Benjamin Schmitz
 * @see org.benjaminschmitz.tiger.WebServer
 * @version 2021-02-19
 */
public class HandleConnection {

	/**
	 * Handling the connection.
	 * 
	 * @param LOGGER    Logger to write toe logs.
	 * @param s         Socket for the connection.
	 * @param generator ErrorPageGenerator for the error page.
	 * @param FOLDER    Folder to see where the files should be.
	 */
	public static void handleConnection(Logger LOGGER, Socket s, ErrorPageGenerator generator, String FOLDER) {
		LOGGER.info("Incomming connection from " + s.getInetAddress() + " port " + s.getPort());

		// In-/Outputstream
		BufferedReader in = null;
		PrintWriter out = null;
		try {
			in = new BufferedReader(new InputStreamReader(s.getInputStream(), StandardCharsets.UTF_8));
			out = new PrintWriter(s.getOutputStream(), true, StandardCharsets.UTF_8);
		} catch (IOException e) {
			LOGGER.warning("Couldn't make In-/Outputstream with " + s.getInetAddress());
			return;
		}

		// first line of the request
		String line;
		try {
			line = in.readLine();
		} catch (IOException e) {
			LOGGER.warning("Error getting input from " + s.getInetAddress());
			return;
		}

		final int indexOfFirstSpace;
		final int indexOfSecondSpace;
		try {
			indexOfFirstSpace = line.indexOf(" ");
			indexOfSecondSpace = line.substring(indexOfFirstSpace + 1, line.length()).indexOf(" ") + 1
					+ indexOfFirstSpace;
		} catch (IndexOutOfBoundsException e) {
			LOGGER.warning("Invalid format from " + s.getInetAddress() + ": " + line);
			out.println(generator.errorGenerator(HttpStatus.FORBIDDEN));
			try {
				in.close();
			} catch (IOException e1) {
				LOGGER.warning("Couldn't close input stream with " + s.getInetAddress());
			}
			out.close();
			return;
		}
		String p;
		try {
			p = line.substring(indexOfFirstSpace + 2, indexOfSecondSpace);
		} catch (IndexOutOfBoundsException e) {
			LOGGER.warning("Invalid format from " + s.getInetAddress() + ": " + line);
			out.println(generator.errorGenerator(HttpStatus.FORBIDDEN));
			try {
				in.close();
			} catch (IOException e1) {
				LOGGER.warning("Couldn't close input stream with " + s.getInetAddress());
			}
			out.close();
			return;
		}

		if (p.endsWith(File.separator))
			p = p + "index.html";

		Path path;
		try {
			path = relativeUriToPath(Path.of(FOLDER), p);
		} catch (IllegalArgumentException e) {
			LOGGER.warning("Invalid format from " + s.getInetAddress() + ": " + line);
			out.println(generator.errorGenerator(HttpStatus.FORBIDDEN));
			try {
				in.close();
			} catch (IOException e1) {
				LOGGER.warning("Couldn't close input stream with " + s.getInetAddress());
			}
			out.close();
			return;
		}

		if (Files.isDirectory(path))
			path = Path.of(path.toString() + File.separator + "index.html");

		try {
			HttpResponse response = new HttpResponse(HttpStatus.OK, Files.readString(path));
			out.println(response);
		} catch (Exception e) {
			LOGGER.warning("Error getting path " + path + " with " + s.getInetAddress());
			HttpResponse response = new HttpResponse(HttpStatus.OK, generator.errorGenerator(HttpStatus.NOT_FOUND, p));
			out.println(response);
		}

		try {
			in.close();
		} catch (IOException e1) {
			LOGGER.warning("Couldn't close input stream with " + s.getInetAddress());
		}
		out.close();
	}

	/**
	 * Converts relative URI String using the given folder into the file path.
	 *
	 * @param folder                  Path of the 'root' folder.
	 * @param fileInFolderRelativeUri Relative path from the 'root' folder.
	 * @return Absolute path of the path.
	 */
	public static Path relativeUriToPath(Path folder, String fileInFolderRelativeUri) {
		Path folderAbs = folder.toAbsolutePath();
		Path newPath = Path.of(folderAbs.toUri().resolve(URI.create(fileInFolderRelativeUri)));
		if (!newPath.startsWith(folderAbs))
			throw new IllegalArgumentException(
					"file " + fileInFolderRelativeUri + " is not localed in the folder " + folderAbs);
		return newPath;
	}
}
