package com.hoccer.filecache;

import com.hoccer.filecache.model.CacheFile;

import java.io.File;
import java.util.HashMap;
import java.util.Vector;

public class CacheBackend {

    private File mDataDirectory = null;

    private HashMap<String, CacheFile> mFiles
            = new HashMap<String, CacheFile>();

    public CacheBackend(File dataDirectory) {
        mDataDirectory = dataDirectory;
    }

    public CacheFile forPathInfo(String pathInfo, boolean create) {
        if(pathInfo.length() == 1) {
            return null;
        }

        CacheFile res = null;

        synchronized (mFiles) {
            String rest = pathInfo.substring(1);

            if(mFiles.containsKey(rest)) {
                res = mFiles.get(rest);
            } else {
                if(create) {
                    res = new CacheFile(rest);
                    res.setBackend(this);
                    mFiles.put(rest, res);
                }
            }
        }

        return res;
    }

    public void remove(CacheFile f) {
        if(mFiles.containsKey(f.getUUID())) {
            mFiles.remove(f.getUUID());
        }
    }

    public Vector<CacheFile> getAll() {
        return new Vector<CacheFile>(mFiles.values());
    }

    public File getDataDirectory() {
        return mDataDirectory;
    }

}
