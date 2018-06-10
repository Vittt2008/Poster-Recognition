package com.android.academy.spb.poster.recognition;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.android.academy.spb.poster.recognition.api.PosterService;
import com.android.academy.spb.poster.recognition.api.entities.Film;
import com.android.academy.spb.poster.recognition.api.entities.ImageInfo;
import com.android.academy.spb.poster.recognition.api.entities.ImageInfos;
import com.android.academy.spb.poster.recognition.model.PosterSaver;
import com.android.academy.spb.poster.recognition.model.PosterUrlParser;
import com.google.gson.Gson;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author Vitaliy Markus
 */

public class StartActivity extends AppCompatActivity {

    private PosterUrlParser posterUrlParser;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StartActivity.this, MainActivity.class));
            }
        });

        posterUrlParser = new PosterUrlParser(this);
        prepare();
    }

    private void prepare() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://spb.subscity.ru/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        PosterService service = retrofit.create(PosterService.class);
        service.getFilms().enqueue(new Callback<List<Film>>() {
            @Override
            public void onResponse(Call<List<Film>> call, Response<List<Film>> response) {
                List<Film> films = response.body();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        ArrayList<ImageInfo> infos = new ArrayList<>();
                        for (Film film : films) {
                            try {
                                ResponseBody body = service.downloadFile(film.getPoster()).execute().body();
                                File file = posterUrlParser.parsePosterUrl(film.getPoster());
                                PosterSaver.savePoster(body, file);
                                infos.add(new ImageInfo(file.getAbsolutePath(), file.getName(), file.getName().split("\\.")[0]));
                            } catch (Exception e) {
                                Log.e("=== ERROR ===", "=== ERROR ===", e);
                            }
                        }
                        ImageInfos imageInfos = new ImageInfos(infos);
                        String gson = new Gson().toJson(imageInfos);
                        PosterSaver.savePoterInfo(gson, posterUrlParser.getPosterInfoFile());
                        int i = 0;
                    }
                }).start();
            }


            @Override
            public void onFailure(Call<List<Film>> call, Throwable t) {

            }
        });
    }
}
