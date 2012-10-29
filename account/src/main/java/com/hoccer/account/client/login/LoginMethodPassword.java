package com.hoccer.account.client.login;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.hoccer.account.client.AccountManager;

public class LoginMethodPassword extends LoginMethodBase {

	TextBox mUsernameText;
	
	PasswordTextBox mPasswordText;
	
	Button mSignInButton;
	Hyperlink mRegisterLink;
	
	public LoginMethodPassword(AccountManager pApp) {
		super(pApp);
	}
	
	public Widget initialize() {
		FlowPanel p = new FlowPanel();
		
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
		
		p.add(new Label("Username"));
		
		mUsernameText = new TextBox();
		mUsernameText.addKeyDownHandler(keyDownSubmit);
		p.add(mUsernameText);
		
		p.add(new Label("Password"));
		
		mPasswordText = new PasswordTextBox();
		mPasswordText.addKeyDownHandler(keyDownSubmit);
		p.add(mPasswordText);
		
		FlowPanel finishLine = new FlowPanel();
		
		mSignInButton = new Button("Sign in");
		mSignInButton.addClickHandler(clickSubmit);
		finishLine.add(mSignInButton);
		
		mRegisterLink = new Hyperlink("Register", AccountManager.SCREEN_REGISTRATION);
		finishLine.add(mRegisterLink);
		
		p.add(finishLine);
		
		return p;
	}

	@Override
	public String getTitle() {
		return "Web Account";
	}
	
}
