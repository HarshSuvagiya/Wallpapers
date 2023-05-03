package com.scorpion.splashwalls.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WallpaperApi {

    private static Retrofit retrofit = null;
    private static Retrofit retrofit2 = null;

    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl("https://api.unsplash.com")
                    .build();
        }
        return retrofit;
    }

    public static Retrofit getClientVideoWallpaper() {
        if (retrofit2 == null) {
            retrofit2 = new Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl("https://api.pexels.com")
                    .build();
        }
        return retrofit2;
    }

}
