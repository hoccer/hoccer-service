package com.hoccer.filecache.transfer;

import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hoccer.filecache.model.CacheFile;

public class CacheUpload extends CacheTransfer {
	
	public CacheUpload(CacheFile file,
					   HttpServletRequest req,
					   HttpServletResponse resp) {
		super(file, req, resp);
	}
	
	public void perform() throws IOException {
		byte[] buffer = new byte[64*1024];
		
		cacheFile.uploadStarts(this);
		
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
