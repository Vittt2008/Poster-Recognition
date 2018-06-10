package com.android.academy.spb.poster.recognition.model;

import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.ResponseBody;

public class PosterSaver {
    public static void savePoster(ResponseBody body, File file) {
        try {
            try (InputStream inputStream = body.byteStream()) {
                try (OutputStream output = new FileOutputStream(file)) {
                    byte[] buffer = new byte[4 * 1024]; // or other buffer size
                    int read;
                    while ((read = inputStream.read(buffer)) != -1) {
                        output.write(buffer, 0, read);
                    }
                    output.flush();
                }
            }
        } catch (Exception e) {
            Log.e("=== ERROR ===", "=== ERROR ===", e);
        }
    }
}
