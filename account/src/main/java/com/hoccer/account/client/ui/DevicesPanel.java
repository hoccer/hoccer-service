package com.hoccer.account.client.ui;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class DevicesPanel extends Composite {

	public DevicesPanel() {
		initWidget(initialize());
	}
	
	private Widget initialize() {
		FlowPanel p = new FlowPanel();
		
		p.add(new Label("Your Devices"));
		
		return p;
	}
	
}
