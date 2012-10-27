package com.hoccer.account.client.ui;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Panel;
import com.hoccer.account.client.AccountManager;
import com.hoccer.account.client.login.LoginMethodDeviceToken;
import com.hoccer.account.client.login.LoginMethodPassword;
import com.hoccer.account.client.login.LoginMethodRegister;

public class LoginPanel extends Composite {

	AccountManager mApp;
	
	public LoginPanel(AccountManager pIndex) {
		mApp = pIndex;
		initWidget(initialize());
	}

	private Panel initialize() {
		HorizontalPanel h = new HorizontalPanel();
		
		h.add(new LoginMethodPassword(mApp));
		h.add(new LoginMethodRegister(mApp));
		h.add(new LoginMethodDeviceToken(mApp));
		
		return h;
	}
	
}
