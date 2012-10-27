package com.hoccer.account.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.hoccer.account.client.AccountManager;

public class MainPanel extends Composite {

	private AccountManager mIndex;
	
	private DockPanel mDock;

	private Panel mHeader;

	private Panel mNavigation;

	private DeckPanel mContent;
	
	public MainPanel(AccountManager pIndex) {
		mIndex = pIndex;
		initWidget(initialize());
	}
	
	private Panel initialize() {
		mDock = new DockPanel();
		mDock.setWidth("100%");

		mHeader = new HorizontalPanel();
		mHeader.add(new Image(GWT.getModuleBaseURL() + "../logo.png"));
		mHeader.add(new HTML("<h1>Hoccer Account Management</h1>"));
		mDock.add(mHeader, DockPanel.NORTH);

		mNavigation = new VerticalPanel();
		mNavigation.add(new Hyperlink("Welcome", AccountManager.SCREEN_WELCOME));
		mNavigation.add(new Hyperlink("Devices", AccountManager.SCREEN_DEVICES));
		mDock.add(mNavigation, DockPanel.WEST);

		mContent = new DeckPanel();
		mDock.add(mContent, DockPanel.CENTER);
		
		mContent.add(new LoginPanel(mIndex));
		mContent.add(new WelcomePanel());
		mContent.add(new DevicesPanel());
		mContent.add(new RegistrationPanel());
		
		mContent.showWidget(0);

		return mDock;
	}
	
	public void onHistoryChange(String token) {
		if(token.equals(AccountManager.SCREEN_LOGIN)
				|| token.equals(AccountManager.SCREEN_REGISTRATION)) {
			mNavigation.setVisible(false);
		} else {
			mNavigation.setVisible(true);
		}
		if(token.equals(AccountManager.SCREEN_LOGIN)) {
			mContent.showWidget(0);
		} else if(token.equals(AccountManager.SCREEN_WELCOME)) {
			mContent.showWidget(1);
		} else if(token.equals(AccountManager.SCREEN_REGISTRATION)) {
			mContent.showWidget(3);
		} else if(token.equals(AccountManager.SCREEN_DEVICES)) {
			mContent.showWidget(2);
		}
	}
	
}
