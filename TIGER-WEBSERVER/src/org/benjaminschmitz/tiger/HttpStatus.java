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

/**
 * Enum for all the different statuses.
 * <p>
 * "NOT_FOUND" for errors in the requested URL.
 * <p>
 * "FORBIDDEN" for errors while reading the file(wrong permissions, image, ...).
 * <p>
 * "OK" for an normal request and everything went well.
 * 
 * @author Benjamin Schmitz
 * @version 2021-02-19
 */
public enum HttpStatus {
	NOT_FOUND("NOT_FOUND", 404), FORBIDDEN("FORBIDDEN", 403), OK("OK", 200);

	/**
	 * Constructor for setting up the status types.
	 * 
	 * @param message String representation of the enum.
	 * @param code    HTTP code from the status.
	 */
	private HttpStatus(String message, int code) {
		this.message = message;
		this.code = code;
	}

	String message;
	int code;

	/**
	 * Getter for the string of status type.
	 * 
	 * @return String of the status type.
	 */
	@Override
	public String toString() {
		return message;
	}

	/**
	 * Getter for the code.
	 * 
	 * @return HTTP code from the status.
	 */
	public int errorCode() {
		return code;
	}
}
