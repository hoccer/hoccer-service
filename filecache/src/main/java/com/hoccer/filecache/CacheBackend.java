package com.hoccer.filecache;

import com.hoccer.filecache.model.CacheFile;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

public abstract class CacheBackend {

    private File mDataDirectory = null;

    public CacheBackend(File dataDirectory) {
        mDataDirectory = dataDirectory;
    }

    public File getDataDirectory() {
        return mDataDirectory;
    }

    public CacheFile forPathInfo(String pathInfo, boolean create) {
        if(pathInfo.length() == 1) {
            return null;
        }

        return forId(pathInfo.substring(1), create);
    }

    public abstract List<CacheFile> getAll();

    public abstract CacheFile forId(String id, boolean create);

    public abstract void checkpoint(CacheFile file);

    public abstract void remove(CacheFile file);

}
