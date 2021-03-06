//================================================================================================================================
//
//  Copyright (c) 2015-2018 VisionStar Information Technology (Shanghai) Co., Ltd. All Rights Reserved.
//  EasyAR is the registered trademark or trademark of VisionStar Information Technology (Shanghai) Co., Ltd in China
//  and other countries for the augmented reality technology developed by VisionStar Information Technology (Shanghai) Co., Ltd.
//
//================================================================================================================================

package com.android.academy.spb.poster.recognition;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.customtabs.CustomTabsIntent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.android.academy.spb.poster.recognition.api.entities.Film;
import com.android.academy.spb.poster.recognition.model.Preferences;

import java.util.HashMap;
import java.util.List;

import cn.easyar.Engine;


public class MainActivity extends AppCompatActivity {
    /*
     * Steps to create the key for this sample:
     *  1. login www.easyar.com
     *  2. create app with
     *      Name: HelloAR
     *      Package Name: cn.easyar.samples.helloar
     *  3. find the created item in the list and show key
     *  4. set key string bellow
     */
    private static String key = "gM2WBkYHODDDPaQ7mQfWsyb4rwoFtBySJPexJNubmqqzkGshz2RNIbWlAE2qGmxfGY1VywUblRnrUMtx71cMdHbEKEEdbvSGS6tC69b61cEcMk1bGDKAco4Yc3EYovJ0esoKPJpSG60nyY5XCJ9RVq31xCnTbsmNKzUSC3OHAUGUMBfQuIXDpRr0ckwuqJloYlX0efU7";
    private GLView glView;
    private Preferences preferences;
    private String oldMeta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        if (!Engine.initialize(this, key)) {
            Log.e("HelloAR", "Initialization Failed.");
        }

        preferences = new Preferences(this);

        glView = new GLView(this);

        requestCameraPermission(new PermissionCallback() {
            @Override
            public void onSuccess() {
                ((ViewGroup) findViewById(R.id.preview)).addView(glView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            }

            @Override
            public void onFailure() {
            }
        });
    }

    private interface PermissionCallback {
        void onSuccess();

        void onFailure();
    }

    private HashMap<Integer, PermissionCallback> permissionCallbacks = new HashMap<Integer, PermissionCallback>();
    private int permissionRequestCodeSerial = 0;

    @TargetApi(23)
    private void requestCameraPermission(PermissionCallback callback) {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                int requestCode = permissionRequestCodeSerial;
                permissionRequestCodeSerial += 1;
                permissionCallbacks.put(requestCode, callback);
                requestPermissions(new String[]{Manifest.permission.CAMERA}, requestCode);
            } else {
                callback.onSuccess();
            }
        } else {
            callback.onSuccess();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (permissionCallbacks.containsKey(requestCode)) {
            PermissionCallback callback = permissionCallbacks.get(requestCode);
            permissionCallbacks.remove(requestCode);
            boolean executed = false;
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    executed = true;
                    callback.onFailure();
                }
            }
            if (!executed) {
                callback.onSuccess();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (glView != null) {
            glView.onResume();
        }
    }

    @Override
    protected void onPause() {
        if (glView != null) {
            glView.onPause();
        }
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onImageRecognized(String meta) {
        if (oldMeta == null || !oldMeta.equals(meta)) {
            oldMeta = meta;
            long filmId = Long.parseLong(meta);
            List<Film> films = preferences.getFilms();
            Film film = findFilm(films, filmId);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String name = film.getTitles().getRussian();
                    AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
                            .setTitle(name)
                            .setOnCancelListener(new DialogInterface.OnCancelListener() {
                                @Override
                                public void onCancel(DialogInterface dialog) {
                                    oldMeta = null;
                                }
                            })
                            .setOnDismissListener(new DialogInterface.OnDismissListener() {
                                @Override
                                public void onDismiss(DialogInterface dialog) {
                                    oldMeta = null;
                                }
                            })
                            .setItems(R.array.site_items, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (which == 0) {
                                        openKinopoisk(film);
                                        //oldMeta = null;
                                    } else {
                                        openImdb(film);
                                        //oldMeta = null;
                                    }
                                }
                            })
                            .show();
                }
            });
        }
    }

    private Film findFilm(List<Film> films, long filmId) {
        for (Film film : films) {
            if (film.getId() == filmId) {
                return film;
            }
        }
        return null;
    }

    private void openKinopoisk(Film film) {
        String url = "https://www.kinopoisk.ru/film/" + film.getRating().getKinopoisk().getId();
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.launchUrl(this, Uri.parse(url));
    }

    private void openImdb(Film film) {
        String url = "https://www.imdb.com/title/tt" + film.getRating().getImdb().getId();
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.launchUrl(this, Uri.parse(url));
    }
}
