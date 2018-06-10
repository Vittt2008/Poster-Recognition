package com.android.academy.spb.poster.recognition.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.android.academy.spb.poster.recognition.api.entities.Film;
import com.android.academy.spb.poster.recognition.api.entities.Films;
import com.google.gson.Gson;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Preferences {

    private static final String FILMS_KEY = "films_key";

    private SharedPreferences sharedPreferences;
    private Gson gson = new Gson();

    public Preferences(Context context) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public List<Film> getFilms() {
        String value = sharedPreferences.getString(FILMS_KEY, null);
        if (value == null) {
            return new ArrayList<>();
        } else {
            Films films = gson.fromJson(value, Films.class);
            return films.getFilms();
        }
    }

    public void saveFilms(List<Film> list) {
        Films films = new Films(list);
        String value = gson.toJson(films);
        sharedPreferences.edit().putString(FILMS_KEY, value).apply();
    }

}
