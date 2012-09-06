package com.hoccer.filecache.transfer;

import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hoccer.filecache.model.CacheFile;

public abstract class CacheTransfer {

	protected static Logger log
		= Logger.getLogger(CacheTransfer.class.getSimpleName());

	protected CacheFile cacheFile;
	
	protected HttpServletRequest  httpRequest;
	protected HttpServletResponse httpResponse;
		
	public CacheTransfer(CacheFile file, HttpServletRequest req, HttpServletResponse resp) {
		cacheFile = file;
		httpRequest = req;
		httpResponse = resp;
	}
	
}
