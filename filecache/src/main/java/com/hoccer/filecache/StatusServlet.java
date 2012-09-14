package com.hoccer.filecache;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hoccer.filecache.model.CacheFile;
import com.hoccer.filecache.transfer.CacheDownload;
import com.hoccer.filecache.transfer.CacheUpload;

@WebServlet(urlPatterns={"/status"})
public class StatusServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		resp.setContentType("text/plain; charset=UTF-8");
		
		OutputStream s = resp.getOutputStream();
		OutputStreamWriter w = new OutputStreamWriter(s, "UTF-8");
		
		w.write(">>>>>>>>>>>>>>> Hoccer Filecache <<<<<<<<<<<<<<<\n\n");
		
		Vector<CacheFile> allFiles = CacheFile.getAll();
		w.write("Active files (" + allFiles.size() + "):\n");
		for (CacheFile f : allFiles) {			
			w.write(" File " + f.getUUID()
					+ " type " + f.getContentType()
					+ "\n");
			
			w.write("  State " + f.getStateString()
					+ " limit " + f.getLimit()
					+ "\n");
			w.write("  Expires " + f.getExpiryTime() + "\n");
			
			CacheUpload upload = f.getUpload();
			if(upload != null) {
				w.write("  Upload"
						+ " from " + upload.getRemoteAddr()
						+ " rate " + Math.round(upload.getRate()) / 1000.0 + " kB/s"
						+ "\n");
			}
			
			Vector<CacheDownload> downloads = f.getDownloads();
			for (CacheDownload d : downloads) {
				w.write("  Download"
						+ " from " + d.getRemoteAddr()
						+ " rate " + Math.round(d.getRate()) / 1000.0 + " kB/s"
						+ "\n");
			}
		}
		
		w.close();
	}

}
