package com.hoccer.filecache.transfer;

import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hoccer.filecache.model.CacheFile;

/**
 * Active download from the cache
 * 
 * @author ingo
 */
public class CacheDownload extends CacheTransfer {

	OutputStream outStream;
	
	public CacheDownload(CacheFile file,
						 HttpServletRequest req,
						 HttpServletResponse resp)
	{
		super(file, req, resp);
	}
	
	public void perform() throws IOException {
		byte[] buffer = new byte[64*1024];
		
		int fileState = cacheFile.getState();
		if(fileState == CacheFile.STATE_EXPIRED
			|| fileState == CacheFile.STATE_ABANDONED) {
			httpResponse.sendError(httpResponse.SC_NOT_FOUND);
			return;
		}
		
		cacheFile.downloadStarts(this);
		rateStart();
		
		try {
			OutputStream outStream = httpResponse.getOutputStream();
			RandomAccessFile inFile = cacheFile.openForRandomAccess("r");
			
			inFile.seek(0);
			
			int bytesSent = 0;
			int bytesLimit = 0;
			do {
				bytesLimit = cacheFile.getLimit();
				
				if(bytesSent >= bytesLimit) {
					if(cacheFile.waitForData(bytesLimit)) {
						continue;
					} else {
						break;
					}
				}
				
				int bytesToTransfer = bytesLimit - bytesSent;
				do {
					int currentBunch = Math.min(bytesToTransfer, buffer.length);
					int bytesRead = inFile.read(buffer, 0, currentBunch);
					
					if(bytesRead == -1) {
						throw new IOException("Synchronization fault");
					}
					
					outStream.write(buffer, 0, bytesRead);
					
					bytesSent += bytesRead;
					bytesToTransfer -= bytesRead;
					
					rateProgress(bytesRead);
				} while(bytesToTransfer > 0);
			} while(true);
			
			inFile.close();
			
		} catch (IOException e) {
			cacheFile.downloadAborted(this);
			throw e;
		}
		
		cacheFile.downloadFinished(this);
	}
}
