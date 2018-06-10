package com.android.academy.spb.poster.recognition.api.entities;

import com.google.gson.annotations.SerializedName;

public class Film {
    @SerializedName("id")
    private long id;

    @SerializedName("poster")
    private String poster;

    @SerializedName("title")
    private Titles titles;

    public long getId() {
        return id;
    }

    public String getPoster() {
        return poster;
    }

    public Titles getTitles() {
        return titles;
    }
}
