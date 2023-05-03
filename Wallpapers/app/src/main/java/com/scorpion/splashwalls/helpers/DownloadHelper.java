package com.scorpion.splashwalls.helpers;

import android.app.DownloadManager;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.ArrayMap;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.scorpion.splashwalls.activities.ImageDetailActivity;

import java.io.File;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class DownloadHelper {
//    public static final String DOWNLOAD_PATH = "/Pictures/";

    @IntDef({
            DownloadStatus.SUCCESS,
            DownloadStatus.FAILED,
            DownloadStatus.DOWNLOADING})
    @Retention(RetentionPolicy.SOURCE)
    public @interface DownloadStatus {
        int SUCCESS = 1;
        int FAILED = -1;
        int DOWNLOADING = 0;
    }

    @IntDef({
            DownloadType.DOWNLOAD,
            DownloadType.WALLPAPER})
    @Retention(RetentionPolicy.SOURCE)
    public @interface DownloadType {
        int DOWNLOAD = 0;
        int WALLPAPER = 1;
    }

    private static DownloadHelper instance;

    public static DownloadHelper getInstance(Context context) {
        if (instance == null) {
            synchronized (DownloadHelper.class) {
                if (instance == null) {
                    instance = new DownloadHelper(context);
                }
            }
        }
        return instance;
    }

    @Nullable
    private DownloadManager mDownloadManager;

    private ArrayMap<Long, String> mDownloads = new ArrayMap<>();

    private DownloadHelper(Context context) {
        mDownloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
    }

    public long addDownloadRequest(Context context, @DownloadType int downloadType, String downloadUrl, String fileName) {
        if (mDownloadManager == null) return -1;
        DownloadManager.Request request;
        String relativePath = Environment.getExternalStorageDirectory().getPath() + "/Splash Walls/";
        if (Build.VERSION.SDK_INT < 29) {
            request = new DownloadManager.Request(Uri.parse(downloadUrl))
                    .setTitle(fileName)
                    .setDestinationInExternalPublicDir(ImageDetailActivity.DOWNLOAD_PATH, fileName)
                    .setVisibleInDownloadsUi(true)
                    .setNotificationVisibility(downloadType == DownloadType.DOWNLOAD ? DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED : DownloadManager.Request.VISIBILITY_VISIBLE);

        } else {

            request = new DownloadManager.Request(Uri.parse(downloadUrl))
                    .setTitle(fileName)
                    .setDestinationInExternalFilesDir(context,relativePath, fileName)
                    .setVisibleInDownloadsUi(true)
                    .setNotificationVisibility(downloadType == DownloadType.DOWNLOAD ? DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED : DownloadManager.Request.VISIBILITY_VISIBLE);
        }

        request.allowScanningByMediaScanner();

        long downloadId = mDownloadManager.enqueue(request);
        mDownloads.put(downloadId, fileName);

        return downloadId;
    }

    public void removeDownloadRequest(long id) {
        if (mDownloadManager == null) return;

        mDownloadManager.remove(id);
        mDownloads.remove(id);
    }

    @Nullable
    public Cursor getDownloadCursor(long id) {
        if (mDownloadManager == null) return null;

        Cursor cursor = mDownloadManager.query(new DownloadManager.Query().setFilterById(id));
        if (cursor == null) {
            return null;
        } else if (cursor.getCount() > 0 && cursor.moveToFirst()) {
            return cursor;
        } else {
            cursor.close();
            return null;
        }
    }

    public int getDownloadStatus(@NonNull Cursor cursor) {
        switch (cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))) {
            case DownloadManager.STATUS_SUCCESSFUL:
                return DownloadStatus.SUCCESS;
            case DownloadManager.STATUS_FAILED:
            case DownloadManager.STATUS_PAUSED:
                return DownloadStatus.FAILED;
            default:
                return DownloadStatus.DOWNLOADING;
        }
    }

    public float getDownloadProgress(@NonNull Cursor cursor) {
        long soFar = cursor.getInt(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
        long total = cursor.getInt(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
        int result = (int) (100.0 * soFar / total);
        result = Math.max(0, result);
        result = Math.min(100, result);
        return result;
    }

    public boolean fileExists(String name) {
        return new File(Environment.getExternalStorageDirectory() + ImageDetailActivity.DOWNLOAD_PATH + name).exists();
    }

    public String getFileName(long id) {
        return mDownloads.get(id);
    }

    public String getFilePath(long id) {
        return Environment.getExternalStorageDirectory() + ImageDetailActivity.DOWNLOAD_PATH + getFileName(id);
    }

}
