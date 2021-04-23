package com.IE_CA.CA5.model.waitingListTimer;

import com.IE_CA.CA5.model.BolbolestanApplication;

public class WaitingListUpdateJob implements Runnable {
	@Override
	public void run() {
		BolbolestanApplication.getInstance().updateWaitingLists();
	}
}
