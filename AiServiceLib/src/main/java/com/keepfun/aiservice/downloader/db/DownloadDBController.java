package com.keepfun.aiservice.downloader.db;

import com.keepfun.aiservice.downloader.domain.DownloadInfo;
import com.keepfun.aiservice.downloader.domain.DownloadThreadInfo;

import java.util.List;

/**
 * Created by ixuea(http://a.ixuea.com/3) on 17/1/23.
 */

public interface DownloadDBController {

    List<DownloadInfo> findAllDownloading();

    List<DownloadInfo> findAllDownloaded();

    DownloadInfo findDownloadedInfoById(String id);

    void pauseAllDownloading();

    void createOrUpdate(DownloadInfo downloadInfo);

    void createOrUpdate(DownloadThreadInfo downloadThreadInfo);

    void delete(DownloadInfo downloadInfo);

    void delete(DownloadThreadInfo download);
}
