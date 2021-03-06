package com.android.academy.spb.poster.recognition.model;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.net.URL;

public class PosterUrlParser {

    private static final String POSTER_INFO_NAME = "poster_info.txt";
    private Context context;

    public PosterUrlParser(Context context) {
        this.context = context;
    }

    public File parsePosterUrl(String string) {
        try {
            URL url = new URL(string);
            String[] pathes = url.getPath().split("/");
            String lastPath = pathes[pathes.length - 1];
            return new File(context.getCacheDir(), lastPath);
        } catch (Exception e) {
            Log.e("=== ERROR ===", "=== ERROR ===", e);
        }
        return null;
    }

    public File getPosterInfoFile() {
        return new File(context.getCacheDir(), POSTER_INFO_NAME);
    }
}
