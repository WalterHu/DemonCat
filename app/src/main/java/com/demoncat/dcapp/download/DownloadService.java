/**
 * Copyright 2018 hubohua
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.demoncat.dcapp.download;

import android.app.DownloadManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.demoncat.dcapp.common.Constants;

import java.io.File;

/**
 * @Class: DownloadService
 * @Description: 下载类，使用的Android原生下载类DownloadManager
 * @Author: hubohua
 * @CreateDate: 2018/4/12
 */
public class DownloadService extends Service {
    private DownloadManager mDownloadManager;
    private long mDownloadId;
    private String mUrl = "https://qd.myapp.com/myapp/qqteam/AndroidQQ/mobileqq_android.apk"; // test download url

    private String mParentDir = null;  // download file dir
    private String mFileName = null; // download file name, absolute file path
    private String mMimeType = null; // mimeType of the file

    private static final String EXTRA_URL = "url";
    private static final String EXTRA_PARENT_DIR = "dir";
    private static final String EXTRA_FILE_NAME = "file";
    private static final String EXTRA_MIME_TYPE = "mimeType";

    private static final String DOWNLOAD_ROOT_DIR =
            Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator +  "DemonCat" + File.separator;
    private static final String DEFAULT_DOWNLOAD_DIR = "Download";

    /**
     * Start download
     * @param url download url
     * @param dir current download file parent url
     * @param fileName download file name
     */
    public static void startDownload(Context context, String url, String dir, String fileName, String mimeType) {
        if (context == null ||
                TextUtils.isEmpty(url) || TextUtils.isEmpty(fileName)) {
            // download params invalid
            return;
        }
        Intent intent = new Intent(context, DownloadService.class);
        intent.putExtra(EXTRA_URL, url);
        intent.putExtra(EXTRA_PARENT_DIR, dir);
        intent.putExtra(EXTRA_FILE_NAME, fileName);
        intent.putExtra(EXTRA_MIME_TYPE, mimeType);
        context.startService(intent);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        download(intent);
        return START_STICKY;
    }

    private void download(Intent intent) {
        // check download information
        if (intent != null) {
            mUrl = intent.getStringExtra(EXTRA_URL);
            mParentDir = intent.getStringExtra(EXTRA_PARENT_DIR);
            mFileName = intent.getStringExtra(EXTRA_FILE_NAME);
            mMimeType = intent.getStringExtra(EXTRA_MIME_TYPE);
            Log.d(Constants.TAG_DEMONCAT, "download url: " + mUrl + ", parent dir: " + mParentDir +
                    ", file name: " + mFileName + ", mimeType: " + mMimeType);
            if (TextUtils.isEmpty(mUrl) || TextUtils.isEmpty(mFileName)) {
                // download params invalid
                stopSelf();
                return;
            }
            mParentDir = TextUtils.isEmpty(mParentDir) ? DEFAULT_DOWNLOAD_DIR : mParentDir;
            String downloadPath = DOWNLOAD_ROOT_DIR + mParentDir;
            File file = new File(downloadPath);
            if (file.exists() && file.isFile()) {
                file.delete();
            }
            if (!file.exists()) {
                file.mkdirs();
            }
            File downloadFile = new File(downloadPath, mFileName);
            // start system download manager
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(mUrl));
            request.setAllowedOverRoaming(true);
            request.setNotificationVisibility(
                    DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            request.setVisibleInDownloadsUi(true); // progress notification visible
            request.setAllowedNetworkTypes(
                    DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
            request.setTitle("DemonCat");
            // set the download file directory to local known one, such as Downloads/Music, etc.
            // request.setDestinationInExternalPublicDir(
            //         Environment.DIRECTORY_DOWNLOADS, mFileName);
            request.setDestinationUri(Uri.fromFile(downloadFile));
            // Set the MIME content type of this download.  This will override the content type declared
            // in the server's response.
            request.setMimeType(mMimeType);
            mDownloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
            mDownloadId = mDownloadManager.enqueue(request);
            Log.d(Constants.TAG_DEMONCAT, "download id: " + mDownloadId);
            registerReceiver(mReceiver,
                    new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        }
    }

    //广播监听下载的各个状态
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // download success
            Toast.makeText(context, "下载完成", Toast.LENGTH_SHORT).show();
        }
    };
}
