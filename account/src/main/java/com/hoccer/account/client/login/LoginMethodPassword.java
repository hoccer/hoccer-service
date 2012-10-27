package com.hoccer.account.client.login;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.hoccer.account.client.AccountManager;

public class LoginMethodPassword extends LoginMethodBase {

	TextBox mUsernameText;
	
	PasswordTextBox mPasswordText;
	
	Button mSignInButton;
	
	public LoginMethodPassword(AccountManager pApp) {
		super(pApp);
	}
	
	public Widget initialize() {
		VerticalPanel v = new VerticalPanel();
		
		ClickHandler clickSubmit = new ClickHandler() {
			public void onClick(ClickEvent event) {
				mApp.switchTo(AccountManager.SCREEN_WELCOME);
			}
		};
		KeyDownHandler keyDownSubmit = new KeyDownHandler() {
			public void onKeyDown(KeyDownEvent event) {
				if(event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					mApp.switchTo(AccountManager.SCREEN_WELCOME);
				}
			}
		};
		
		v.add(new Label("Username"));
		
		mUsernameText = new TextBox();
		mUsernameText.addKeyDownHandler(keyDownSubmit);
		v.add(mUsernameText);
		
		v.add(new Label("Password"));
		
		mPasswordText = new PasswordTextBox();
		mPasswordText.addKeyDownHandler(keyDownSubmit);
		v.add(mPasswordText);
		
		mSignInButton = new Button("Sign in");
		mSignInButton.addClickHandler(clickSubmit);
		v.add(mSignInButton);
		
		return v;
	}
	
}
