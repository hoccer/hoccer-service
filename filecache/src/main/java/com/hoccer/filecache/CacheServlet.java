package com.hoccer.filecache;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hoccer.filecache.model.CacheFile;
import com.hoccer.filecache.transfer.CacheDownload;
import com.hoccer.filecache.transfer.CacheUpload;

@WebServlet(urlPatterns="/v3/*",asyncSupported=true)
public class CacheServlet extends HttpServlet {

	static Logger log = Logger.getLogger(CacheServlet.class.getSimpleName());
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		log.info("GET " + req.getPathInfo());
		
		CacheFile file = CacheFile.forPathInfo(req.getPathInfo());
		if(file == null) {
			resp.sendError(HttpServletResponse.SC_NOT_FOUND,
							"File can't exist in cache");
			return;
		}
		
		CacheDownload download = new CacheDownload(file, req, resp);
		
		download.perform();
	}

	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		log.info("PUT " + req.getPathInfo());
		
		CacheFile file = CacheFile.forPathInfo(req.getPathInfo());
		if(file == null) {
			resp.sendError(HttpServletResponse.SC_NOT_FOUND,
							"File can't exist in cache");
			return;
		}
		
		CacheUpload upload = new CacheUpload(file, req, resp);
		
		upload.perform();
	}

}
