package org.benjaminschmitz.tiger.threadpool;

public class Task {
	private final Runnable runnable;

	public Task(Runnable runnable) {
		this.runnable = runnable;
	}

	public Runnable getRunnable() {
		return runnable;
	}
}
