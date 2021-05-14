package com.keepfun.aiservice.downloader.core;

import com.keepfun.aiservice.downloader.domain.DownloadInfo;
import com.keepfun.aiservice.downloader.exception.DownloadException;

/**
 * Created by ixuea(http://a.ixuea.com/3) on 17/1/22.
 */

public interface DownloadResponse {

    void onStatusChanged(DownloadInfo downloadInfo);

    void handleException(DownloadException exception);
}
