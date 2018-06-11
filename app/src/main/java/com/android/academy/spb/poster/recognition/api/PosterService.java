package com.android.academy.spb.poster.recognition.api;

import com.android.academy.spb.poster.recognition.api.entities.Film;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface PosterService {
    @GET("movies.json")
    Call<List<Film>> getFilms();

    @GET
    Call<ResponseBody> downloadFile(@Url String fileUrl);
}
