package com.allbib.utils.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.time.LocalDate;

public class GsonUtil {

    private static final Gson gson;

    static {
        // Create Gson with the LocalDate adapter
        gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .create();
    }

    public static Gson getGson() {
        return gson;
    }
}

