package com.samcymbaluk.ultimateguns.config;

import com.google.common.reflect.TypeToken;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import com.samcymbaluk.ultimateguns.config.util.RuntimeTypeAdapterFactory;
import com.samcymbaluk.ultimateguns.features.FeatureConfig;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.nio.file.Files;
/**
 * Gson Config Loader
 *
 * @author uksspy
 */
public class UltimateGunsConfigLoader {

    private final Type typeList = new TypeToken<FeatureConfig>() {}.getType();

    private JsonParser parser = new JsonParser();
    private RuntimeTypeAdapterFactory<FeatureConfig> featureConfigAdapter;
    private Gson gson;


    public UltimateGunsConfigLoader() {
        featureConfigAdapter = RuntimeTypeAdapterFactory.of(FeatureConfig.class, "__type");

    }

    public void registerFeatureConfig(FeatureConfig featureConfig) {
        System.out.println(featureConfig.getClass());
        featureConfigAdapter.registerSubtype(featureConfig.getClass());
    }

    public void build() {
        gson = new GsonBuilder()
                .setPrettyPrinting()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .registerTypeAdapterFactory(featureConfigAdapter)
                .create();
    }

    /**
     * Loads a provided config object from a given JSON file.
     * If the file does not exist it also creates the file using the given object defaults
     *
     * @param clazz The object type you wish to load, also dictates the class of the returned object
     * @param file   The file that is to be created/read from
     * @return The object loaded from file
     * @throws IOException
     */
    public <T> T loadConfig(Class<T> clazz, File file, T fallback) throws IOException {
        if (file.createNewFile()) { //File does not exist, save fallback to file
            String json = gson.toJson(fallback);
            try (PrintWriter out = new PrintWriter(file)) {
                out.println(json);
            }
            return fallback;
        } else { //File exists, load from file
            return gson.fromJson(new String(Files.readAllBytes(file.toPath())), typeList);
        }
    }

    /**
     * Saves a config object to the specified file in JSON format
     *
     * @param config The object to be saved
     * @param file   The file to which the object is saved
     * @throws IOException
     */
    public void saveConfig(Object config, File file) throws IOException {
        file.createNewFile();
        String json = gson.toJson(parser.parse(gson.toJson(config)));
        try (PrintWriter out = new PrintWriter(file)) {
            out.println(json);
        }
    }
}