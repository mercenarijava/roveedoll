package com.android.gjprojection.roveedoll.utils;


import android.support.annotation.NonNull;

import com.android.gjprojection.roveedoll.services.bluetooth.BleSendMessageL;
import com.android.gjprojection.roveedoll.services.bluetooth.BleSendMessageV;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class JacksonUtils {
    static final ObjectMapper objectMapper;

    static {
        objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    public static <T> T read(
            @NonNull final Class<T> target,
            @NonNull final String jsonString) {

        try {
            return objectMapper.readValue(jsonString, target);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }



    public static String write(
            @NonNull final BleSendMessageV object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String write(
            @NonNull final BleSendMessageL object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }
}