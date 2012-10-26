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
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 */
public class Index implements EntryPoint {
	public static final String HISTORY_LOGIN = "accountLogin";
	public static final String HISTORY_WELCOME = "accountWelcome";
	public static final String HISTORY_DEVICES = "accountDevices";

	/**
	 * The message displayed to the user when the server cannot be reached or
	 * returns an error.
	 */
	private static final String SERVER_ERROR = "An error occurred while "
			+ "attempting to contact the server. Please check your network "
			+ "connection and try again.";

	private static final Logger LOG
		= Logger.getLogger(Index.class.getName());
	
	private final AccountServiceAsync mService
		= GWT.create(AccountService.class);

	private final Messages mMessages
		= GWT.create(Messages.class);
	
	private IndexPage mPage;

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
	
	private void initialize() {
		LOG.info("initialize()");

		// set initial history state
		String initToken = History.getToken();
		if(initToken.length() == 0) {
			History.newItem(HISTORY_LOGIN);
		}

		// handle history changes
		History.addValueChangeHandler(new ValueChangeHandler<String>() {
			public void onValueChange(ValueChangeEvent<String> event) {
				historyChange(event.getValue());
			}
		});
		
		// create and attach views
		mPage = new IndexPage();
		RootPanel.get("app").add(mPage);

		// trigger initial history event
		History.fireCurrentHistoryState();
	}
	
	private void historyChange(String token) {
		LOG.info("historyChange(" + token + ")");
		mPage.onHistoryChange(token);
	}

}
