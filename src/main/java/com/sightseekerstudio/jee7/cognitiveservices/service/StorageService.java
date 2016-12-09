/*
 * (C) Sony Network Communications Inc. All Rights reserved.
 */
package com.sightseekerstudio.jee7.cognitiveservices.service;

import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.BlobContainerPermissions;
import com.microsoft.azure.storage.blob.BlobContainerPublicAccessType;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.util.logging.Level;
import javax.annotation.PostConstruct;
import javax.faces.bean.ApplicationScoped;
import lombok.extern.java.Log;

/**
 *
 * @author sightseeker
 */
@Log
@ApplicationScoped
public class StorageService {

    private static final String ACCOUNT_NAME = "AccountName=********;";
    private static final String ACCOUNT_KEY = "AccountKey=***************************************************************************************==;";
    private static final String DEFAULT_ENDPOINT_PROTOCOL = "DefaultEndpointsProtocol=https;";
    private static final String CONTAINER_NAME = "cognitiveservices";
    private static final String STORAGE_CONNECTION_STRING = DEFAULT_ENDPOINT_PROTOCOL + ACCOUNT_NAME + ACCOUNT_KEY;

    private CloudBlobClient blobClient;

    @PostConstruct
    public void init() {
        try {
            CloudStorageAccount storageAccount = CloudStorageAccount.parse(STORAGE_CONNECTION_STRING);
            blobClient = storageAccount.createCloudBlobClient();
            createContainer(CONTAINER_NAME);
        } catch (URISyntaxException | InvalidKeyException ex) {
            log.log(Level.SEVERE, "Cannot create CloudBlobClient : " + STORAGE_CONNECTION_STRING, ex);
            throw new IllegalArgumentException(ex);
        }
    }

    /**
     * ファイルをBLOBにアップロード
     *
     * @param data
     * @param fileName
     */
    public void uploadFile(byte[] data, String fileName) {
        CloudBlobContainer container;
        try {
            container = blobClient.getContainerReference(CONTAINER_NAME);
            CloudBlockBlob blob = container.getBlockBlobReference(fileName);
            blob.upload(new ByteArrayInputStream(data), data.length);
        } catch (URISyntaxException | StorageException ex) {
            log.log(Level.SEVERE, "file upload failed : " + fileName, ex);
            throw new IllegalArgumentException(ex);
        } catch (IOException ex) {
            throw new IllegalStateException(ex);
        }

    }

    /**
     * コンテナ生成
     *
     * @param containerName
     */
    private void createContainer(String containerName) {
        try {
            CloudBlobContainer container = blobClient.getContainerReference(containerName);
            if (!container.exists()) {
                BlobContainerPermissions permissions = new BlobContainerPermissions();
                permissions.setPublicAccess(BlobContainerPublicAccessType.CONTAINER);
                container.uploadPermissions(permissions);
            }
        } catch (URISyntaxException | StorageException ex) {
            log.log(Level.SEVERE, "Failed to create container : " + containerName, ex);
            throw new IllegalArgumentException(ex);
        }
    }

}
