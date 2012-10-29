package com.hoccer.account.client.login;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;
import com.hoccer.account.client.AccountManager;

public abstract class LoginMethodBase extends Composite {

	protected AccountManager mApp;
	
	Panel mPanel;
	Label mTitle;
	
	public LoginMethodBase(AccountManager pApp) {
		mApp = pApp;

		mPanel = new FlowPanel();
		mPanel.setStylePrimaryName("loginMethod");
		mTitle = new Label(getTitle());
		mTitle.setStylePrimaryName("loginMethodTitle");
		mPanel.add(mTitle);
		
		Widget w = initialize();
		
		mPanel.add(w);
		
		initWidget(mPanel);
	}
	
	public abstract String getTitle();
	
	public abstract Widget initialize();
	
}
