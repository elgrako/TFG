package com.example.tfg.backup;

import android.content.Context;

public interface BackupDataProvider {
    String exportToJson(Context context) throws Exception;

    void importFromJson(Context context, String json) throws Exception;
    //default int backupVersion(){return 1;}
}
