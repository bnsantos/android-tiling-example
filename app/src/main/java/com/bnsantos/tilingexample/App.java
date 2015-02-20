package com.bnsantos.tilingexample;

import android.app.Application;
import android.util.Log;

import com.bnsantos.tilingexample.service.TileService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

/**
 * Created by bruno on 26/01/15.
 */
public class App extends Application {
    public static final String ISO_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    public static final String END_POINT = "SERVER_LOCATION_HERE";

    private TileService mService;

    private static App instance;

    @Override
    public void onCreate() {
        super.onCreate();

        RestAdapter restAdapter = new RestAdapter.Builder().setConverter(buildGsonConverter()).setEndpoint(END_POINT).build();
        mService = restAdapter.create(TileService.class);

        instance=this;
    }

    public static TileService getService() {
        return App.getInstance().mService;
    }

    public static App getInstance(){
        return instance;
    }

    public static GsonConverter buildGsonConverter() {
        Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, dateJsonDeserializer).registerTypeAdapter(Date.class, dateJsonSerializer).create();
        return new GsonConverter(gson);
    }

    private static JsonDeserializer<Date> dateJsonDeserializer = new JsonDeserializer<Date>() {
        @Override
        public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            SimpleDateFormat df = new SimpleDateFormat(ISO_DATE_FORMAT);
            df.setTimeZone(TimeZone.getTimeZone("UTC"));
            try {
                return json == null ? null : df.parse(json.getAsString());
            } catch (ParseException e) {
                Log.e(App.class.getSimpleName(), "Error while parsing date from json " + json.getAsString(), e);
                return null;
            }
        }
    };

    private static JsonSerializer<Date> dateJsonSerializer = new JsonSerializer<Date>() {
        @Override
        public JsonElement serialize(Date date, Type type, JsonSerializationContext jsonSerializationContext) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(ISO_DATE_FORMAT);
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            return new JsonPrimitive(simpleDateFormat.format(date));
        }
    };
}
