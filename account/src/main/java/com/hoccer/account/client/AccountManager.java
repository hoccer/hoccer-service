package com.hoccer.account.client;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.hoccer.account.client.ui.DevicesPanel;
import com.hoccer.account.client.ui.LoginPanel;
import com.hoccer.account.client.ui.RegistrationPanel;
import com.hoccer.account.client.ui.WelcomePanel;

/**
 */
public class AccountManager implements EntryPoint {
	public static final String SCREEN_LOGIN = "accountLogin";
	public static final String SCREEN_REGISTRATION = "accountRegister";
	public static final String SCREEN_WELCOME = "accountWelcome";
	public static final String SCREEN_DEVICES = "accountDevices";

	/**
	 * The message displayed to the user when the server cannot be reached or
	 * returns an error.
	 */
	private static final String SERVER_ERROR = "An error occurred while "
			+ "attempting to contact the server. Please check your network "
			+ "connection and try again.";

	private static final Logger LOG
		= Logger.getLogger(AccountManager.class.getName());
	
	private final AccountServiceAsync mService
		= GWT.create(AccountService.class);

	private final Messages mMessages
		= GWT.create(Messages.class);
	
	private DockLayoutPanel mDock;

	private Panel mHeader;

	private Panel mNavigation;

	private DeckPanel mContent;
	
	public void onModuleLoad() {
		LOG.info("onModuleLoad()");
		
		// handle uncaught exceptions
		GWT.setUncaughtExceptionHandler(
			new GWT.UncaughtExceptionHandler() {
				public void onUncaughtException(Throwable e) {
					LOG.log(Level.SEVERE, "Uncaught exception", e);
				}
		});
		
		// initialize in a deferred command so the above
		// exception handler can work during initialization
		Scheduler.get().scheduleDeferred(
			new Scheduler.ScheduledCommand() {
				public void execute() {
					initialize();
				}
		});
	}
	
	public AccountServiceAsync getService() {
		return mService;
	}
	
	private void initialize() {
		LOG.info("initialize()");

		// set initial history state
		String initToken = History.getToken();
		if(initToken.length() == 0) {
			History.newItem(SCREEN_LOGIN);
		}

		// handle history changes
		History.addValueChangeHandler(new ValueChangeHandler<String>() {
			public void onValueChange(ValueChangeEvent<String> event) {
				historyChange(event.getValue());
			}
		});

		// create and attach views
		initializeUi();
		
		// trigger initial history event
		History.fireCurrentHistoryState();
	}
	
	private void initializeUi() {
		FlowPanel f = new FlowPanel();
		
		mHeader = new FlowPanel();
		mHeader.setStylePrimaryName("header");
		Image headerLogo = new Image(GWT.getModuleBaseURL() + "../logo.png");
		headerLogo.setStylePrimaryName("headerLogo");
		mHeader.add(headerLogo);
		Label headerTitle = new Label("Account Management");
		headerTitle.setStylePrimaryName("headerTitle");
		mHeader.add(headerTitle);
		f.add(mHeader);
		
		mNavigation = new VerticalPanel();
		mNavigation.setStylePrimaryName("navigation");
		mNavigation.add(new Hyperlink("Welcome", AccountManager.SCREEN_WELCOME));
		mNavigation.add(new Hyperlink("Devices", AccountManager.SCREEN_DEVICES));
		f.add(mNavigation);

		mContent = new DeckPanel();
		mContent.setStylePrimaryName("content");
		mContent.add(new LoginPanel(this));
		mContent.add(new WelcomePanel());
		mContent.add(new DevicesPanel());
		mContent.add(new RegistrationPanel());
		mContent.showWidget(0);
		f.add(mContent);
		
		RootPanel.get("app").add(f);
	}
	
	public void switchTo(String token) {
		History.newItem(token, true);
	}
	
	private void historyChange(String token) {
		LOG.info("historyChange(" + token + ")");
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
