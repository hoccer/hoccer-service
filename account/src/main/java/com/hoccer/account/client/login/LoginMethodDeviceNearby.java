package com.hoccer.account.client.login;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;
import com.hoccer.account.client.AccountManager;

public class LoginMethodDeviceNearby extends LoginMethodBase {

	public LoginMethodDeviceNearby(AccountManager pApp) {
		super(pApp);
	}

	@Override
	public Widget initialize() {
		FlowPanel p = new FlowPanel();
		
		Button b = new Button("Enable search");
		p.add(b);
		
		return p;
	}

	@Override
	public String getTitle() {
		return "Nearby Devices";
	}

}
