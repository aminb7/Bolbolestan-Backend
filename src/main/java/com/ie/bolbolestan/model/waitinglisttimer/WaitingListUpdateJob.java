package com.ie.bolbolestan.model.waitinglisttimer;

import com.ie.bolbolestan.model.BolbolestanApplication;

public class WaitingListUpdateJob implements Runnable {
	@Override
	public void run() {
		BolbolestanApplication.getInstance().updateWaitingLists();
	}
}
