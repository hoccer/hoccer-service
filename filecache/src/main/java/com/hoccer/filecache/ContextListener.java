package com.hoccer.filecache;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import com.hoccer.filecache.model.CacheFile;

@WebListener
public class ContextListener implements ServletContextListener {
	
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		ServletContext ctx = sce.getServletContext();
		File tmpDir = (File)ctx.getAttribute(ServletContext.TEMPDIR);
		CacheFile.setDataDirectory(tmpDir);
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
	}

}
