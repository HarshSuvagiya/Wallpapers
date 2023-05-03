package com.scorpion.splashwalls.models;

public class AutoWallpaperHistoryModel {

    ImageDetailModel imageDetailModel;
    long timeStamp;

    public AutoWallpaperHistoryModel() {
    }

    public AutoWallpaperHistoryModel(ImageDetailModel imageDetailModel, long timeStamp) {
        this.imageDetailModel = imageDetailModel;
        this.timeStamp = timeStamp;
    }

    public ImageDetailModel getImageDetailModel() {
        return imageDetailModel;
    }

    public void setImageDetailModel(ImageDetailModel imageDetailModel) {
        this.imageDetailModel = imageDetailModel;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }
}
