package com.android.academy.spb.poster.recognition.api.entities;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ImageInfos {

    @SerializedName("images")
    private List<ImageInfo> imageList;

    public ImageInfos(List<ImageInfo> imageList) {
        this.imageList = imageList;
    }

    public List<ImageInfo> getImageList() {
        return imageList;
    }
}
