package com.hoccer.filecache.transfer;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hoccer.filecache.model.CacheFile;

/**
 * Active transfer to or from the cache
 * 
 * @author ingo
 */
public abstract class CacheTransfer {

	/**
	 * Common logger for subclasses
	 */
	protected static Logger log
		= Logger.getLogger(CacheTransfer.class.getSimpleName());
	
	/**
	 * Atomic counter for id assignment
	 */
	protected static AtomicInteger transferIdCounter=
		new AtomicInteger(0);

	/**
	 * Unique id for this transfer
	 */
	protected int transferId;
	
	/**
	 * Cache file being operated on
	 */
	protected CacheFile cacheFile;
	
	/**
	 * Http request causing this transfer
	 */
	protected HttpServletRequest  httpRequest;
	
	/**
	 * Http response for this transfer
	 */
	protected HttpServletResponse httpResponse;
	
	private long rateTimestamp;
	private long rateAccumulator;
	private double lastRate;
	
	/**
	 * Primary constructor
	 * 
	 * @param file
	 * @param req
	 * @param resp
	 */
	protected CacheTransfer(CacheFile file, HttpServletRequest req, HttpServletResponse resp) {
		transferId = transferIdCounter.incrementAndGet();
		cacheFile = file;
		httpRequest = req;
		httpResponse = resp;
	}
	
	public String getRemoteAddr() {
		return httpRequest.getRemoteAddr();
	}
	
	public double getRate() {
		return lastRate;
	}
	
	protected void rateStart() {
		rateTimestamp = System.currentTimeMillis();
		lastRate = 0.0;
	}
	
	protected void rateProgress(int bytesTransfered) {
		long now = System.currentTimeMillis();
		long passed = now - rateTimestamp;
		
		rateAccumulator += bytesTransfered;
		
		if(passed > 250) {
			double rate = rateAccumulator / (passed / 1000.0);
						
			rateTimestamp = now;
			rateAccumulator = 0;
			
			lastRate = rate;
		}
	}
	
	protected void rateFinish() {
		
	}
	
}
