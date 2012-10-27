package com.hoccer.account.server;

import java.util.UUID;

import javax.servlet.http.HttpSession;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.hoccer.account.client.AccountService;

@SuppressWarnings("serial")
public class AccountServiceImpl extends RemoteServiceServlet implements
		AccountService {

	private HttpSession getSession(boolean create) {
		return getThreadLocalRequest().getSession(create);
	}
	
	public String loginDeviceToken() {
		return UUID.randomUUID().toString();
	}
	
	public void loginPlain(String username, String password) {
		HttpSession s = getSession(true);
		s.setAttribute("username", username);
	}
	
	public void logout() {
		HttpSession s = getSession(false);
		if(s != null) {
			s.invalidate();
		}
	}

}
