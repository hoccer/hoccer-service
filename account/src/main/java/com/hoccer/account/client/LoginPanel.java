package com.hoccer.account.client;

import java.util.logging.Logger;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class LoginPanel extends Composite {

	private static final Logger LOG = Logger.getLogger(LoginPanel.class.getName());
	
	TextBox mUsernameText;
	
	PasswordTextBox mPasswordText;
	
	Button mSignInButton;
	
	public LoginPanel() {
		initWidget(initialize());
	}

	private Panel initialize() {
		LOG.info("initialize()");
		
		VerticalPanel v = new VerticalPanel();
		
		v.add(new Label("Username"));
		
		mUsernameText = new TextBox();
		v.add(mUsernameText);
		
		v.add(new Label("Password"));
		
		mPasswordText = new PasswordTextBox();
		v.add(mPasswordText);
		
		mSignInButton = new Button("Sign in");
		v.add(mSignInButton);
		
		return v;
	}
	
}
