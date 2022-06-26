package com.fornary4.exercise.util;

import android.content.Context;

import com.fornary4.exercise.entity.Choose;
import com.fornary4.exercise.entity.Judge;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class FileUtil {
    static Gson gson = new Gson();
    public static String readJson(Context context, String name) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            InputStreamReader inputStreamReader = new InputStreamReader(context.getAssets().open(name), StandardCharsets.UTF_8);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
            bufferedReader.close();
            inputStreamReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    public static List<Choose> getSingleList(Context context) {
        String json = readJson(context, "single.json");
        return gson.fromJson(json, new TypeToken<List<Choose>>() {
        }.getType());
    }

    public static List<Choose> getMultipleList(Context context) {
        String json = readJson(context, "multiple.json");
        return gson.fromJson(json, new TypeToken<List<Choose>>() {
        }.getType());
    }

    public static List<Judge> getJudgeList(Context context) {
        String json = readJson(context, "judge.json");
        return gson.fromJson(json, new TypeToken<List<Judge>>() {
        }.getType());
    }

}
