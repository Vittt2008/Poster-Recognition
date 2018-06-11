package com.android.academy.spb.poster.recognition.api.entities;

import com.google.gson.annotations.SerializedName;

public class Rating {

    @SerializedName("id")
    private String id;
    @SerializedName("rating")
    private float rating;
    @SerializedName("votes")
    private int votes;

    public String getId() {
        return id;
    }

    public float getRating() {
        return rating;
    }

    public int getVotes() {
        return votes;
    }
}
