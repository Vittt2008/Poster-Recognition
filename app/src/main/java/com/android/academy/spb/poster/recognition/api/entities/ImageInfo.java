package com.android.academy.spb.poster.recognition.api.entities;

import com.google.gson.annotations.SerializedName;

public class ImageInfo {

    @SerializedName("image")
    private String image;
    @SerializedName("name")
    private String name;
    @SerializedName("meta")
    private String meta;

    public ImageInfo(String image, String name, String meta) {
        this.image = image;
        this.name = name;
        this.meta = meta;
    }

    public String getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public String getMeta() {
        return meta;
    }
}
