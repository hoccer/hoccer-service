package com.hoccer.filecache.control;

import better.jsonrpc.core.JsonRpcConnection;
import com.hoccer.filecache.control.ICacheControl;

public class ControlConnection implements ICacheControl {

    ControlServlet mServlet;

    JsonRpcConnection mRpcConnection;

    public ControlConnection(ControlServlet servlet, JsonRpcConnection rpcConnection) {
        mServlet = servlet;
        mRpcConnection = rpcConnection;
    }

}
