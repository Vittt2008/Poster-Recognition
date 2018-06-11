package com.android.academy.spb.poster.recognition.model;

import android.content.Context;
import android.util.Log;
import android.widget.AutoCompleteTextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

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

    public static void savePoterInfo(String posterInfo, File file){
        try {
            try(FileOutputStream fileOutputStream = new FileOutputStream(file)) {
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);
                outputStreamWriter.write(posterInfo);
                outputStreamWriter.close();
            }
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }

    }
}
