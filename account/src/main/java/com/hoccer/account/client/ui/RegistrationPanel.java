package com.hoccer.account.client.ui;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class RegistrationPanel extends Composite {

	public RegistrationPanel() {
		initWidget(initialize());
	}
	
	private Widget initialize() {
		VerticalPanel v = new VerticalPanel();
		
		v.add(new Label("Registration"));
		
		return v;
	}
	
}
