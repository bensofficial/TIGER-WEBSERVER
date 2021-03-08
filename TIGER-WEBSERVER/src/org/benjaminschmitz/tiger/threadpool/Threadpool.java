package org.benjaminschmitz.tiger.threadpool;

import java.util.concurrent.LinkedBlockingQueue;

public class Threadpool {
	private Thread[] workers;
	private LinkedBlockingQueue<Task> queue;

	public Threadpool(int numWorkers) {
		if (numWorkers <= 0)
			throw new IllegalArgumentException("More than 0 workers needed!");

		queue = new LinkedBlockingQueue<>();

		workers = new Thread[numWorkers];
		Runnable worker = () -> {
			try {
				while (true) {
					Task t = queue.take();
					t.getRunnable().run();
				}
			} catch (InterruptedException e) {
				return;
			}
		};
		for (int i = 0; i < workers.length; i++) {
			workers[i] = new Thread(worker);
			workers[i].start();
		}
	}

	public void submit(Runnable job) throws InterruptedException {
		queue.put(new Task(job));
	}

	public void shutdownNow() {
		for (Thread t : workers)
			t.interrupt();
	}
}
