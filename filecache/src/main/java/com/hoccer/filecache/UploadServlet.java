package com.hoccer.filecache;

import com.hoccer.filecache.model.CacheFile;
import com.hoccer.filecache.transfer.CacheUpload;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Logger;

@WebServlet(urlPatterns = "/upload/*")
public class UploadServlet extends HttpServlet {

    static Logger log = Logger.getLogger(UploadServlet.class.getSimpleName());

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        log.info("upload starts: " + req.getPathInfo());

        CacheBackend backend = getCacheBackend();

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

    private CacheBackend getCacheBackend() {
        ServletContext ctx = getServletContext();
        CacheBackend backend = (CacheBackend)ctx.getAttribute("backend");
        return backend;
    }

}
