package edu.lu.uni.serval.mbertloc.utils;

import com.google.gson.Gson;

public class GsonHolder {

    public static Gson gson;

    public static Gson getGson() {
        if (gson == null) gson = new Gson();
        return gson;
    }

    private GsonHolder() {
        throw new IllegalAccessError("Utility class: No instance allowed, static access only.");
    }

}
