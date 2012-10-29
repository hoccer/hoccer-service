package com.hoccer.account.client.ui;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Panel;
import com.hoccer.account.client.AccountManager;
import com.hoccer.account.client.login.LoginMethodDeviceNearby;
import com.hoccer.account.client.login.LoginMethodDeviceToken;
import com.hoccer.account.client.login.LoginMethodPassword;

public class LoginPanel extends Composite {

	AccountManager mApp;
	
	public LoginPanel(AccountManager pIndex) {
		mApp = pIndex;
		initWidget(initialize());
	}

	private Panel initialize() {
		FlowPanel p = new FlowPanel();
		
		p.add(new LoginMethodPassword(mApp));
		p.add(new LoginMethodDeviceToken(mApp));
		p.add(new LoginMethodDeviceNearby(mApp));
		
		return p;
	}
	
}
