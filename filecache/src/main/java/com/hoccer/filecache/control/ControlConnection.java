package com.hoccer.filecache.control;

import better.jsonrpc.core.JsonRpcConnection;
import com.hoccer.filecache.CacheBackend;
import com.hoccer.filecache.control.ICacheControl;

import javax.servlet.ServletContext;

public class ControlConnection implements ICacheControl {

    ControlServlet mServlet;

    JsonRpcConnection mRpcConnection;

    CacheBackend mBackend;

    public ControlConnection(ControlServlet servlet, JsonRpcConnection rpcConnection) {
        mServlet = servlet;
        mRpcConnection = rpcConnection;
        mBackend = getCacheBackend();
    }

    private CacheBackend getCacheBackend() {
        ServletContext ctx = mServlet.getServletContext();
        CacheBackend backend = (CacheBackend)ctx.getAttribute("backend");
        return backend;
    }

    @Override
    public FileHandles createFileForStorage(String accountId, int fileSize) {
        return null;
    }

    @Override
    public FileHandles createFileForTransfer(String accountId, int fileSize) {
        return null;
    }

    @Override
    public void deleteFile(String fileId) {
    }

    @Override
    public void deleteAccount(String accountId) {
    }

}
