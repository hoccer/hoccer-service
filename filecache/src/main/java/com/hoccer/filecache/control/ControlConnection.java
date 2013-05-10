package com.hoccer.filecache.control;

import better.jsonrpc.core.JsonRpcConnection;
import com.hoccer.filecache.control.ICacheControl;

public class ControlConnection implements ICacheControl {

    JsonRpcConnection mRpcConnection;

    public ControlConnection(JsonRpcConnection rpcConnection) {
        mRpcConnection = rpcConnection;
    }

}
