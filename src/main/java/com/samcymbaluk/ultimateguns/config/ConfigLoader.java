/*
 * Author: uksspy
 * <p>
 * MIT License
 * <p>
 * Copyright (c) 2017 uksspy
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.samcymbaluk.ultimateguns.config;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import com.samcymbaluk.ultimateguns.config.util.PostProcessingEnabler;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

/**
 * Gson Config Loader
 *
 * @author uksspy
 */
public class ConfigLoader {

    private static JsonParser parser = new JsonParser();
    private static Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .disableHtmlEscaping()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .registerTypeAdapterFactory(new PostProcessingEnabler())
            .create();

    public static Gson getParser() {
        return gson;
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
    public static <T> T loadConfig(Class<T> clazz, File file, T fallback) throws IOException {
        if (file.createNewFile()) { //File does not exist, save fallback to file
            String json = gson.toJson(fallback);
            try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8))) {
                writer.write(json);
            }
            return fallback;
        } else { //File exists, load from file
            StringBuilder stringBuilder = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line);
                }
            }
            return gson.fromJson(stringBuilder.toString(), clazz);
        }
    }

    /**
     * Saves a config object to the specified file in JSON format
     *
     * @param config The object to be saved
     * @param file   The file to which the object is saved
     * @throws IOException
     */
    public static void saveConfig(Object config, File file) throws IOException {
        file.createNewFile();
        String json = gson.toJson(parser.parse(gson.toJson(config)));
        try (PrintWriter out = new PrintWriter(file)) {
            out.println(json);
        }
    }
}