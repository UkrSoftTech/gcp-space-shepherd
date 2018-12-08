package com.ukrsofttech.gcp.orchestration.service;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@Service
public class GcsAccessService {

    private static final String GS_PREFIX = "gs://";

    public boolean copyFileFromCloudStorage(String cloudStoragePath) {
        // The name of the bucket to access
        // String bucketName = "my-bucket";

        // The name of the remote file to download
        // String srcFilename = "file.txt";

        // Instantiate a Google Cloud Storage client
        Storage storage = StorageOptions.getDefaultInstance().getService();

        Pair<String, String> gcsMetadata = getBucketAndFileNames(cloudStoragePath);
        String fileName = gcsMetadata.getRight();
        Blob blob = storage.get(BlobId.of(gcsMetadata.getLeft(), fileName));

        // Download file to specified path, like Paths.get("/local/path/to/file.txt");
        Path destFilePath = Paths.get(fileName);
        blob.downloadTo(destFilePath);


        return true;
    }

    /**
     * Extract bucket name and file name from given GCS path.
     *
     * @param cloudStoragePath Path to a file in bucket in format gs://my-test-bucket/templates/MyTemplateFile
     * @return pair of values bucket + file names
     */
    Pair<String, String> getBucketAndFileNames(String cloudStoragePath) {
        if (StringUtils.isEmpty(cloudStoragePath)
                || !cloudStoragePath.startsWith(GS_PREFIX)
                || cloudStoragePath.endsWith("/")) {
            throw new IllegalStateException("Given path is not applicable for GCS: " + cloudStoragePath);
        }

        String subPath = cloudStoragePath.replace(GS_PREFIX, "");
        String bucketName = subPath.substring(0, subPath.indexOf("/"));
        String fileName = subPath.substring(subPath.lastIndexOf("/") + 1);

        return Pair.of(bucketName, fileName);
    }

}
