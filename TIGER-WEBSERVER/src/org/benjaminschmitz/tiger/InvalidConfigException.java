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
 * Exception for wring config file configuration.
 * 
 * @author Benjamin Schmitz
 * @see org.benjaminschmitz.tiger.Main
 * @version 2021-02-19
 */
public class InvalidConfigException extends Exception {
	private static final long serialVersionUID = 1L;

	/**
	 * Making a exception with the message s
	 * 
	 * @param s
	 */
	public InvalidConfigException(String s) {
		super(s);
	}
}
