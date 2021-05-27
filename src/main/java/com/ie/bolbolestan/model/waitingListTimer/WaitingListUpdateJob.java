package com.ie.bolbolestan.model.waitingListTimer;

import com.ie.bolbolestan.model.BolbolestanApplication;

public class WaitingListUpdateJob implements Runnable {
	@Override
	public void run() {
		BolbolestanApplication.getInstance().updateWaitingLists();
	}
}
