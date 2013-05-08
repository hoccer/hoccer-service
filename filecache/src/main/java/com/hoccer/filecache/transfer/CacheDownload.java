package com.hoccer.filecache.transfer;

import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.blobstore.ByteRange;
import com.hoccer.filecache.model.CacheFile;

/**
 * Active download from the cache
 * 
 * @author ingo
 */
public class CacheDownload extends CacheTransfer {

	private static final int BUFFER_SIZE = 64 * 1024;
	
	OutputStream outStream;
    ByteRange byteRange;
	
	public CacheDownload(CacheFile file, ByteRange range,
						 HttpServletRequest req,
						 HttpServletResponse resp)
	{
		super(file, req, resp);
        byteRange = range;
	}
	
	public void perform() throws IOException {
		byte[] buffer = new byte[BUFFER_SIZE];
		
		// set content type
		httpResponse.setContentType(cacheFile.getContentType());

        // start the download
		cacheFile.downloadStarts(this);
		rateStart();
		
		try {
            // open/get streams
			OutputStream outStream = httpResponse.getOutputStream();
			RandomAccessFile inFile = cacheFile.openForRandomAccess("r");

            // determine amount of data to send
            int totalRequested = ((int)byteRange.getEnd()) - ((int)byteRange.getStart()) + 1;

            // seek forward to the requested range
			inFile.seek(byteRange.getStart());

            // loop until done
            int totalTransferred = 0;
            while(totalTransferred < totalRequested) {
                // determine how much to transfer
                int bytesWanted = Math.min(totalRequested - totalTransferred, buffer.length);

                // read data from file
                int bytesRead = inFile.read(buffer, 0, bytesWanted);
                if(bytesRead == -1) {
                    break;
                }

                // write to http output stream
                outStream.write(buffer, 0, bytesRead);

                // account for what we did
                totalTransferred += bytesRead;
                rateProgress(totalTransferred);
            }

            // close file stream
			inFile.close();
			
		} catch (IOException e) {
            // notify the file of the abort
			cacheFile.downloadAborted(this);
            // rethrow to finish the http request
			throw e;
		} finally {
            // always finish the rate estimator
            rateFinish();
        }

        // we are done, tell everybody
		cacheFile.downloadFinished(this);
	}
}
