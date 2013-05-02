package com.hoccer.filecache;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Logger;

import javax.servlet.ServletContext;
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

    CacheBackend getBackendFromRequest(HttpServletRequest req) {
        ServletContext ctx = req.getServletContext();
        CacheBackend backend = (CacheBackend)ctx.getAttribute("backend");
        return backend;
    }
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		log.info("download starts: " + req.getPathInfo());

        CacheBackend backend = getBackendFromRequest(req);
		
		CacheFile file = backend.forPathInfo(req.getPathInfo(), false);
		if(file == null) {
			resp.sendError(HttpServletResponse.SC_NOT_FOUND,
					"File does not exist");
			return;
		}
		
		int fileState = file.getState();
		if(fileState == CacheFile.STATE_EXPIRED
			|| fileState == CacheFile.STATE_ABANDONED
			|| fileState == CacheFile.STATE_NEW) {
			resp.sendError(HttpServletResponse.SC_NOT_FOUND,
					"File does not exist");
			return;
		}
		
		CacheDownload download = new CacheDownload(file, req, resp);
		
		download.perform();
		
		log.info("download finished: " + req.getPathInfo());
	}

	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		log.info("upload starts: " + req.getPathInfo());

        CacheBackend backend = getBackendFromRequest(req);

        CacheFile file = backend.forPathInfo(req.getPathInfo(), true);
		if(file == null) {
			resp.sendError(HttpServletResponse.SC_NOT_FOUND,
					"File can not exist in cache");
			return;
		}
		
		CacheUpload upload = new CacheUpload(file, req, resp);
		
		upload.perform();
		
		log.info("upload finished: " + req.getPathInfo());
	}

}
