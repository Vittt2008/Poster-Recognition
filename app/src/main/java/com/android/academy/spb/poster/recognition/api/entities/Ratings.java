package com.android.academy.spb.poster.recognition.api.entities;

import com.google.gson.annotations.SerializedName;

public class Ratings {

    @SerializedName("imdb")
    private Rating imdb;

    @SerializedName("kinopoisk")
    private Rating kinopoisk;

    public Rating getImdb() {
        return imdb;
    }

    public Rating getKinopoisk() {
        return kinopoisk;
    }
}
