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

import static java.time.temporal.ChronoUnit.MILLIS;
import static java.time.Instant.now;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Logger-Class
 * 
 * @author Benjamin Schmitz
 * @version 2021-02-19
 */
public class Logger {
	private Instant start;
	private LogLevel level;
	private final DateTimeFormatter dtf;

	/**
	 * Initialising the logger variables.
	 * 
	 * @param version    Version of the server.
	 * @param logLevelId ID of the log level. 0 for INFO/RUNTIME and above. 1 for
	 *                   WARNING and above. 2 for SEVERE.
	 * @throws InvalidConfigException Invalid log level id.
	 */
	public Logger(String version, int logLevelId) throws InvalidConfigException {
		switch (logLevelId) {
		case 0 -> level = LogLevel.INFO;
		case 1 -> level = LogLevel.WARNING;
		case 2 -> level = LogLevel.SEVERE;
		default -> throw new InvalidConfigException("Unexpected value: " + logLevelId);
		}

		System.err.printf("""
				TIGER WEBSERVER %s Copyright (C) 2021  Benjamin Schmitz <dev@benjamin-schmitz.org>
				This program comes with ABSOLUTELY NO WARRANTY.
				This is free software, and you are welcome to redistribute it
				under certain conditions (GNU GENERAL PUBLIC LICENSE Version 3).
				Logging all above %s.

				""", version, level.toString());

		dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss:SSS");
		start = now();
	}

	/**
	 * Checks weather the log level is in the set log level.
	 * 
	 * @param l LogLevel of the log to be logged.
	 * @return Boolean value weather the log should be logged.
	 */
	private boolean isToLog(LogLevel l) {
		if (level.id <= l.id)
			return true;
		else
			return false;
	}

	/**
	 * Info-Log
	 * 
	 * @param msg Log message.
	 */
	public void info(String msg) {
		if (isToLog(LogLevel.INFO))
			err("[" + getDate() + "] [" + LogLevel.INFO.toString() + "] " + msg);
	}

	/**
	 * Warning-Log
	 * 
	 * @param msg Log message.
	 */
	public void warning(String msg) {
		if (isToLog(LogLevel.WARNING))
			err("[" + getDate() + "] [" + LogLevel.WARNING.toString() + "] " + msg);
	}

	/**
	 * Severe-Log
	 * 
	 * @param msg Log message.
	 */
	public void severe(String msg) {
		if (isToLog(LogLevel.SEVERE))
			err("[" + getDate() + "] [" + LogLevel.SEVERE.toString() + "] " + msg);
	}

	/**
	 * Runtime-Log
	 */
	public void runtime() {
		if (isToLog(LogLevel.RUNTIME))
			err("[" + getDate() + "] [" + LogLevel.RUNTIME.toString() + "] " + "Running " + start.until(now(), MILLIS)
					+ " ms");
	}

	/**
	 * Reset of the start time
	 */
	public void resetStart() {
		start = now();
	}

	/**
	 * Writing the log to System.err. Writing to the default error output.
	 * 
	 * @param str String to be printed.
	 */
	private static void err(String str) {
		System.err.println(str);
	}

	/**
	 * Returning the date in the format 'yyyy-MM-dd HH:mm:ss:SSS'.
	 * 
	 * @return String representation of the date.
	 */
	private String getDate() {
		LocalDateTime now = LocalDateTime.now();
		return dtf.format(now);
	}

	/**
	 * Enum for the diferent log levels.
	 * 
	 * @author Benjamin Schmitz
	 * @version 2021-02-19
	 */
	enum LogLevel {
		INFO(0, "INFORMATION"), RUNTIME(0, "RUNTIME"), WARNING(1, "WARNING"), SEVERE(2, "SEVERE");

		int id;
		String str;

		/**
		 * Enum for all the different log levels.
		 * <p>
		 * Recommended values:
		 * <p>
		 * "INFO" for info logs. ID is 0.
		 * <p>
		 * "RUNTIME" for runtime logs. ID is 0.
		 * <p>
		 * "WARNING" for warning logs. ID is 1.
		 * <p>
		 * "SEVERE" for severe logs. ID is 2.
		 * 
		 * @param id  id of the log level.
		 * @param str String of the log level.
		 */
		private LogLevel(int id, String str) {
			this.id = id;
			this.str = str;
		}

		/**
		 * Getter for the string of the log level.
		 * 
		 * @return String of the log level.
		 */
		@Override
		public String toString() {
			return str;
		}
	}
}