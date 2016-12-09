/*
 * (C) Sony Network Communications Inc. All Rights reserved.
 */
package com.sightseekerstudio.jee7.cognitiveservices.controller;

import com.sightseekerstudio.jee7.cognitiveservices.entity.FaceViewEntity;
import com.sightseekerstudio.jee7.cognitiveservices.service.StorageService;
import java.util.UUID;
import javax.enterprise.inject.Model;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import org.primefaces.event.CaptureEvent;

/**
 *
 * @author sightseeker
 */
@Model
public class FaceViewController {

    @Inject
    private StorageService storageService;

    @Inject
    private FaceViewEntity entity;

    private String IMAGE_RESOURCE_DIRECTORY = FacesContext.getCurrentInstance().getExternalContext().getRealPath("") + "/images";

    private String getRandomImageName() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString() + ".jpg";
    }

    public void oncapture(CaptureEvent captureEvent) {
        entity.setFileName(getRandomImageName());
        byte[] imageData = captureEvent.getData();

        storageService.uploadFile(imageData, entity.getFileName());

//        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
//        String imageFilePath = IMAGE_RESOURCE_DIRECTORY + File.separator + filename;
//        FileImageOutputStream imageOutput;
//        try {
//            imageOutput = new FileImageOutputStream(new File(imageFilePath));
//            imageOutput.write(data, 0, data.length);
//            imageOutput.close();
//        } catch (IOException e) {
//            throw new FacesException("Error in writing captured image.", e);
//        }
    }
}
