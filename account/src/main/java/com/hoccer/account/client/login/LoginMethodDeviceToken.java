package com.hoccer.account.client.login;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.hoccer.account.client.AccountManager;

public class LoginMethodDeviceToken extends LoginMethodBase {

	public static final int QRCODE_REFRESH_INTERVAL = 30 * 1000;
	
	private static final Logger LOG = Logger.getLogger(LoginMethodDeviceToken.class.getName());
	
	Image mTokenImage;
	
	Timer mTokenRefreshTimer;
	
	AsyncCallback<String> mTokenRefreshCallback;
	
	public LoginMethodDeviceToken(AccountManager pApp) {
		super(pApp);
	}
	
	@Override
	public Widget initialize() {
		VerticalPanel v = new VerticalPanel();
		
		mTokenImage = new Image();
		v.add(mTokenImage);

		mTokenRefreshCallback = new AsyncCallback<String>() {
			public void onSuccess(String result) {
				LOG.info("Device login token: " + result);
				mTokenImage.setUrl(GWT.getModuleBaseURL() + "../qrcode/" + result);
			}
			public void onFailure(Throwable caught) {
				// XXX display error
				LOG.log(Level.SEVERE, "Could not fetch device login token", caught);
			}
		};
		
		mTokenRefreshTimer = new Timer() {
			@Override
			public void run() {
				updateQRCode();
			}
		};
		mTokenRefreshTimer.scheduleRepeating(QRCODE_REFRESH_INTERVAL);
		
		updateQRCode();
		
		return v;
	}
	
	private void updateQRCode() {
		mApp.getService().loginDeviceToken(mTokenRefreshCallback);
	}

	
	
}
