package com.android.academy.spb.poster.recognition.api.entities;

import com.google.gson.annotations.SerializedName;

public class Titles {

    @SerializedName("original")
    private String original;

    @SerializedName("russian")
    private String russian;


    public String getOriginal() {
        return original;
    }

    public String getRussian() {
        return russian;
    }
}
