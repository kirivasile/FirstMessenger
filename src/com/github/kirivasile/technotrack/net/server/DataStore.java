package com.github.kirivasile.technotrack.net.server;

import com.github.kirivasile.technotrack.authorization.FileUserStore;
import com.github.kirivasile.technotrack.history.FileMessageStore;

/**
 * Created by Kirill on 09.11.2015.
 */
public class DataStore {
    private FileUserStore fileUserStore;
    private FileMessageStore fileMessageStore;

    public DataStore(FileUserStore fileUserStore, FileMessageStore fileMessageStore) {
        this.fileUserStore = fileUserStore;
        this.fileMessageStore = fileMessageStore;
    }

    public FileUserStore getFileUserStore() {
        return fileUserStore;
    }

    public FileMessageStore getFileMessageStore() {
        return fileMessageStore;
    }

    public void close() throws Exception {
        fileUserStore.close();
        fileMessageStore.close();
    }
}
