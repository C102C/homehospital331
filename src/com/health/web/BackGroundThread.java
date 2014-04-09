package com.health.web;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BackGroundThread implements Runnable {
	BackGroundTask task;

	private static ExecutorService exec = Executors.newFixedThreadPool(2);

	public BackGroundThread(BackGroundTask task) {
		this.task = task;
	}

	public interface BackGroundTask {
		public void process();
	}

	@Override
	public void run() {
		task.process();
	}

	public void start() {
		exec.execute(this);

	}

}
