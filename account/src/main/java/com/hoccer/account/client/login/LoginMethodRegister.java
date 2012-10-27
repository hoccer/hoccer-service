package com.hoccer.account.client.login;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.hoccer.account.client.AccountManager;

public class LoginMethodRegister extends LoginMethodBase {

	Button mRegisterButton;
	
	public LoginMethodRegister(AccountManager pApp) {
		super(pApp);
	}

	@Override
	public Widget initialize() {
		VerticalPanel v = new VerticalPanel();
		
		v.add(new Label("Join now for free"));
		
		mRegisterButton = new Button("Register Account");
		mRegisterButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				mApp.switchTo(AccountManager.SCREEN_REGISTRATION);
			}
		});
		v.add(mRegisterButton);
		
		return v;
	}

}
