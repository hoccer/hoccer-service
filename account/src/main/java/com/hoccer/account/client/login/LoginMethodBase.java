package com.hoccer.account.client.login;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.hoccer.account.client.AccountManager;

public abstract class LoginMethodBase extends Composite {

	protected AccountManager mApp;
	
	public LoginMethodBase(AccountManager pApp) {
		mApp = pApp;
		initWidget(initialize());
	}
	
	public abstract Widget initialize();
	
}
