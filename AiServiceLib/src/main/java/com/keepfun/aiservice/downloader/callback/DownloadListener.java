package com.keepfun.aiservice.downloader.callback;


import com.keepfun.aiservice.downloader.exception.DownloadException;

/**
 * Created by ixuea(http://a.ixuea.com/3) on 17/2/23.
 */

public interface DownloadListener {

    void onStart();

    void onWaited();

    void onPaused();

    void onDownloading(long progress, long size);

    void onRemoved();

    void onDownloadSuccess();

    void onDownloadFailed(DownloadException e);
}
