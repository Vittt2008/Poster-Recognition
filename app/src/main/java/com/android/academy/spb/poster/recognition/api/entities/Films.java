package com.android.academy.spb.poster.recognition.api.entities;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Films {

    @SerializedName("films")
    private List<Film> films;

    public Films(List<Film> films) {
        this.films = films;
    }

    public List<Film> getFilms() {
        return films;
    }
}
