package com.hoccer.filecache.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Vector;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Logger;

import com.hoccer.filecache.transfer.CacheDownload;
import com.hoccer.filecache.transfer.CacheUpload;

public class CacheFile {

	public static final int STATE_NEW = 1;
	public static final int STATE_WAITING = 2;
	public static final int STATE_UPLOADING = 3;
	public static final int STATE_COMPLETE = 4;
	public static final int STATE_ABANDONED = 5;
	public static final int STATE_EXPIRED = 6;
	
	private static String[] stateNames = {
		"UNKNOWN",
		"NEW",
		"WAITING",
		"UPLOADING",
		"COMPLETE",
		"ABANDONED",
		"EXPIRED"
	};
	
	private static ScheduledExecutorService expiryExecutor
		= Executors.newSingleThreadScheduledExecutor();
			
	protected static Logger log
		= Logger.getLogger(CacheFile.class.getSimpleName());
	
	private ReentrantLock mStateLock;
	private Condition mStateChanged;
	private int mState;
	private int mLimit;
	
	private String mUUID;
	
	private Date mExpiryTime;
	
	private CacheUpload mUpload = null;
	
	private Vector<CacheDownload> mDownloads
		= new Vector<CacheDownload>();
	
	private CacheFile(String pUUID) {
		mStateLock = new ReentrantLock();
		mStateChanged = mStateLock.newCondition();
		
		mState = STATE_NEW;
		mLimit = 0;
		
		mUUID = pUUID;
	}

	public String getStateString() {
		return stateNames[mState];
	}
	
	public int getLimit() {
		return mLimit;
	}
	
	public String getUUID() {
		return mUUID;
	}
	
	public Date getExpiryTime() {
		return mExpiryTime;
	}
	
	public CacheUpload getUpload() {
		return mUpload;
	}
	
	public Vector<CacheDownload> getDownloads() {
		return new Vector<CacheDownload>(mDownloads);
	}
	
	private String getPath() {
		return "/tmp/" + mUUID;
	}
	
	private File getFile() {
		return new File(getPath());
	}
	
	private void switchState(int newState, String cause) {
		log.info("file " + mUUID + " state " + stateNames[mState]
					+ " -> " + stateNames[newState] + ": " + cause);
		mState = newState;
	}
	
	private void considerRemoval() {
		if(mState == STATE_EXPIRED) {
			if(mDownloads.size() == 0) {
				CacheFile.remove(this);
			}
		}
		if(mState == STATE_ABANDONED) {
			if(mDownloads.size() == 0) {
				CacheFile.remove(this);
			}
		}
	}
	
	
	public void setupExpiry(int secondsFromNow) {
		Date now = new Date();
		Calendar cal = new GregorianCalendar();
		cal.setTime(now);
		cal.add(Calendar.SECOND, secondsFromNow);
		mExpiryTime = cal.getTime();
		log.info("file " + mUUID + " expires " + mExpiryTime.toString());
	}
	
	private void scheduleExpiry() {
		Runnable expiryAction = new Runnable() {
			@Override
			public void run() {
				CacheFile.this.expire();
			}
		};
		expiryExecutor.schedule(
				expiryAction,
				mExpiryTime.getTime() - System.currentTimeMillis(),
				TimeUnit.MILLISECONDS);
	}
	
	private void expire() {
		mStateLock.lock();
		try {
			switchState(STATE_EXPIRED, "expiry time reached");
			this.getFile().delete();
			considerRemoval();
		} finally {
			mStateLock.unlock();
		}
	}
	
	public void uploadStarts(CacheUpload upload) {
		mStateLock.lock();
		try {
			if(mState == STATE_NEW || mState == STATE_WAITING) {
				switchState(STATE_UPLOADING, "new upload");
			} else {
				// XXX error or reupload
			}

			mUpload = upload;

			mStateChanged.signalAll();
		} finally {
			mStateLock.unlock();
		}
	}
	
	public void uploadAborted(CacheUpload upload) {
		mStateLock.lock();
		try {
			switchState(STATE_ABANDONED, "upload aborted");
			
			mUpload = null;
			
			mStateChanged.signalAll();
			
			considerRemoval();
		} finally {
			mStateLock.unlock();
		}
	}
	
	public void uploadFinished(CacheUpload upload) {
		mStateLock.lock();
		try {
			if(mState == STATE_UPLOADING) {
				switchState(STATE_COMPLETE, "upload finished");
			} else {
				// XXX error
			}
			
			scheduleExpiry();
			
			mUpload = null;
			
			mStateChanged.signalAll();
			
			considerRemoval();
		} finally {
			mStateLock.unlock();
		}
	}
	
	public void downloadStarts(CacheDownload download) {
		mStateLock.lock();
		try {
			if(mState == STATE_NEW) {
				switchState(STATE_WAITING, "new download");
			}
			
			mDownloads.add(download);
			
			mStateChanged.signalAll();
		} finally {
			mStateLock.unlock();
		}
	}
	
	public void downloadAborted(CacheDownload download) {
		mDownloads.remove(download);
		considerRemoval();
	}
	
	public void downloadFinished(CacheDownload download) {
		mDownloads.remove(download);
		considerRemoval();
	}
	
	public void updateLimit(int newLimit) {
		mStateLock.lock();
		try {			
			mLimit = newLimit;
			
			mStateChanged.signalAll();
		} finally {
			mStateLock.unlock();
		}
	}
	
	public boolean waitForData(int lastLimit) {
		
		mStateLock.lock();
		
		try {
			// cases where progress has been
			// made already or will never be made
			if(mState == STATE_COMPLETE || mState == STATE_EXPIRED) {
				if(mLimit > lastLimit) {
					return true;
				} else {
					return false;
				}
			}	
			if(mState == STATE_UPLOADING) {
				if(mLimit > lastLimit) {
					return true;
				}
			}
			if(mState == STATE_ABANDONED) {
				return false;
			}
			
			// wait for state change
			try {
				mStateChanged.await();
			} catch (InterruptedException e) {
			}

			// cases where progress may have
			// been made while waiting
			if(mState == STATE_COMPLETE) {
				return true;
			}
			if(mState == STATE_UPLOADING) {
				return true;
			}
			if(mState == STATE_NEW) {
				return true;
			}
			if(mState == STATE_WAITING) {
				return true;
			}
			
			// no progression possible
			return false;
		} finally {
			mStateLock.unlock();
		}
	}
	
	private void ensureExists() throws IOException {
		File f = new File(getPath());
		f.createNewFile();
	}
	
	public RandomAccessFile openForRandomAccess(String mode) throws IOException {
		ensureExists();
		
		RandomAccessFile r = null;
		
		try {
			r = new RandomAccessFile(new File(getPath()), mode);
		} catch (FileNotFoundException e) {
			// XXX does not happen
		}
		
		return r;
	}

	private static HashMap<String, CacheFile> sFiles
			= new HashMap<String, CacheFile>();
	
	public static CacheFile forPathInfo(String pathInfo) {
		if(pathInfo.length() == 1) {
			return null;
		}
		
		synchronized (sFiles) {
			String rest = pathInfo.substring(1);
			
			if(sFiles.containsKey(rest)) {
				return sFiles.get(rest);
			} else {
				CacheFile res = new CacheFile(rest);
				sFiles.put(rest, res);
				return res;
			}
		}
	}
	
	public static void remove(CacheFile f) {
		if(sFiles.containsKey(f.getUUID())) {
			sFiles.remove(f.getUUID());
		}
	}
	
	public static Vector<CacheFile> getAll() {
		return new Vector<CacheFile>(sFiles.values());
	}
	
}
