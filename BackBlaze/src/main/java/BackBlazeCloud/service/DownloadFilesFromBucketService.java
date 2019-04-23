package BackBlazeCloud.service;

import BackBlazeCloud.model.DownloadFileModel;

public interface DownloadFilesFromBucketService {

    void downloadFilesFromBucket(DownloadFileModel downloadFileModel);
}
