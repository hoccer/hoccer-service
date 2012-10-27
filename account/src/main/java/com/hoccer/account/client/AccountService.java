package com.hoccer.account.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("../account")
public interface AccountService extends RemoteService {
	
	String loginDeviceToken();
	
	void loginPlain(String username, String password);
	
	void logout();
	
}
