package com.hoccer.account.client;

import java.util.logging.Logger;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class IndexPage extends Composite {

	private static final Logger LOG = Logger.getLogger(IndexPage.class.getName());
	
	private DockPanel mDock;

	private Panel mHeader;

	private Panel mNavigation;

	private DeckPanel mContent;
	
	public IndexPage() {
		initWidget(initialize());
	}
	
	private Panel initialize() {
		LOG.info("initialize()");
		
		mDock = new DockPanel();
		mDock.setWidth("100%");

		mHeader = new HorizontalPanel();
		mHeader.add(new HTML("<h1>Hoccer Account Management</h1>"));
		mDock.add(mHeader, DockPanel.NORTH);

		mNavigation = new VerticalPanel();
		mNavigation.add(new Hyperlink("Welcome", Index.HISTORY_WELCOME));
		mNavigation.add(new Hyperlink("Devices", Index.HISTORY_DEVICES));
		mDock.add(mNavigation, DockPanel.WEST);

		mContent = new DeckPanel();
		mDock.add(mContent, DockPanel.CENTER);
		
		mContent.add(new LoginPanel());
		mContent.showWidget(0);

		return mDock;
	}
	
	public void onHistoryChange(String token) {
		if(token.equals(Index.HISTORY_LOGIN)) {
			//mNavigation.setVisible(false);
		} else {
			mNavigation.setVisible(true);
		}
		if(token.equals(Index.HISTORY_WELCOME)) {
		} else if(token.equals(Index.HISTORY_DEVICES)) {
		}
	}
	
}
