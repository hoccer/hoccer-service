package com.hoccer.filecache.transfer;

import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hoccer.filecache.model.CacheFile;

/**
 * Active upload to the cache
 * 
 * @author ingo
 */
public class CacheUpload extends CacheTransfer {
	
	private static final int BUFFER_SIZE = 64 * 1024;
	
	public static final int MIN_LIFETIME = 10;
	public static final int MAX_LIFETIME = 3 * 3600;
	
	public CacheUpload(CacheFile file,
					   HttpServletRequest req,
					   HttpServletResponse resp) {
		super(file, req, resp);
	}
	
	public void perform() throws IOException {
		byte[] buffer = new byte[BUFFER_SIZE];
		
		int expiresIn = MAX_LIFETIME;
		String expiresString = httpRequest.getParameter("expires_in");
		if(expiresString != null) {
			expiresIn = Integer.parseInt(expiresString);
		}
		if(expiresIn < MIN_LIFETIME) {
			expiresIn = MIN_LIFETIME;
		}
		if(expiresIn > MAX_LIFETIME) {
			expiresIn = MAX_LIFETIME;
		}
		cacheFile.setupExpiry(expiresIn);
		
		String cType = httpRequest.getContentType();
		if(cType == null) {
			cType = "application/octet-stream";
		}
		cacheFile.setContentType(cType);
		
		cacheFile.uploadStarts(this);
		rateStart();
		
		try {
			InputStream inStream = httpRequest.getInputStream();
			RandomAccessFile outFile = cacheFile.openForRandomAccess("rw");
			
			outFile.seek(0);
			outFile.setLength(0);
			
			int bytesTotal = 0;
			int bytesRead;
			do {
				bytesRead = inStream.read(buffer);
				
				if(bytesRead == -1) {
					break;
				}
				
				outFile.write(buffer, 0, bytesRead);
				
				bytesTotal += bytesRead;
				
				rateProgress(bytesRead);
				
				cacheFile.updateLimit(bytesTotal);
			} while(true);
			
			outFile.close();
		
		} catch (IOException e) {
			cacheFile.uploadAborted(this);
			throw e;
		}
		
		cacheFile.uploadFinished(this);
	}
}
