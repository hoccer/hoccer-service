package com.hoccer.account.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("../Account")
public interface AccountService extends RemoteService {
	
	void loginPlain(String username, String password);
	
	void logout();
	
}
