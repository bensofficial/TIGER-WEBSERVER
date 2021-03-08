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

import java.util.Objects;

/**
 * Class for an HTTPResponse.
 * 
 * @author Benjamin Schmitz
 * @version 2021-02-19
 */
public final class HttpResponse {

	private final HttpStatus status;
	private final String body;
	private final String location;

	/**
	 * Creates a HTTP response with no headers and the given body. This is for
	 * example for {@link HttpStatus#OK} where the body is the HTML of the page.
	 *
	 * @param status the HTTP status, must not be <code>null</code>
	 * @param body   the response body, may be <code>null</code> or empty (in case
	 *               of <code>null</code>, an empty string is used)
	 */
	public HttpResponse(HttpStatus status, String body) {
		this.status = Objects.requireNonNull(status);
		this.body = Objects.requireNonNullElse(body, "");
		this.location = null;
	}

	/**
	 * Creates a HTTP response that includes a location in the response header. This
	 * is used for example for {@link org.benjaminschmitz.tiger.HttpStatus} which
	 * then redirects to the location.
	 *
	 * @param status      the HTTP status, must not be <code>null</code>
	 * @param body        the response body, may be <code>null</code> or empty (in
	 *                    case of <code>null</code>, an empty string is used)
	 * @param locationUrl must not be null or empty
	 */
	public HttpResponse(HttpStatus status, String body, String locationUrl) {
		this.status = Objects.requireNonNull(status);
		this.body = Objects.requireNonNullElse(body, "");
		this.location = Objects.requireNonNull(locationUrl);
		if (location.isBlank())
			throw new IllegalArgumentException("location must not be blank");
	}

	/**
	 * Getter for the HttpStatus
	 * 
	 * @return HttpStatus of the response.
	 */
	public HttpStatus getStatus() {
		return status;
	}

	/**
	 * Getter for the body.
	 * 
	 * @return String representation of the called file.
	 */
	public String getBody() {
		return body;
	}

	/**
	 * Getter for the location to be redirected.
	 * 
	 * @return String of the URL to be redirected.
	 */
	public String getLocation() {
		return location;
	}

	/**
	 * Making a string representation of the response. Adding meta data information
	 * to the request as "HTTP/1.1" and other relevant protocol information.
	 * 
	 * @return String of the HttpResponse.
	 */
	@Override
	public String toString() {
		StringBuilder response = new StringBuilder();
		response.append("HTTP/1.1 ");
		response.append(status.errorCode());
		response.append(" ");
		response.append(status.toString());
		if (location != null) {
			response.append("\r\nLocation: ");
			response.append(location);
		}
		response.append("\r\n\r\n");
		response.append(body);
		return response.toString();
	}
}
