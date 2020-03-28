package com.example.prohieu.map;

import com.example.prohieu.map.Remote.IGoogleAPIService;
import com.example.prohieu.map.Remote.RetrofitClient;

public class Common {
    private static final  String GOOGLE_API_URL = "https://maps.googleapis.com/";
    public static IGoogleAPIService getGoogleAPIService(){
        return RetrofitClient.getClient(GOOGLE_API_URL).create(IGoogleAPIService.class);
    }
}
