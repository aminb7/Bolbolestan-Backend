package com.IE_CA.CA5.model;

public class WaitingListUpdateJob implements Runnable {
	@Override
	public void run() {
		BolbolestanApplication.getInstance().updateWaitingLists();
	}
}
