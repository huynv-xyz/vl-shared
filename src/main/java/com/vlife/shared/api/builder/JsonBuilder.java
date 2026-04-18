package com.vlife.shared.api.builder;

import java.time.OffsetDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

public class JsonBuilder {

    private final Map<String, Object> root = new LinkedHashMap<>();

    public static JsonBuilder create() {
        return new JsonBuilder();
    }

    private JsonBuilder() {
    }

    public static JsonBuilder from(Map<String, Object> map) {
        JsonBuilder jb = new JsonBuilder();
        if (map != null) {
            jb.root.putAll(map);
        }
        return jb;
    }

    public JsonBuilder add(String key, String value) {
        if (value != null) root.put(key, value);
        return this;
    }

    public JsonBuilder add(String key, Number value) {
        if (value != null) root.put(key, value);
        return this;
    }

    public JsonBuilder add(String key, Boolean value) {
        if (value != null) root.put(key, value);
        return this;
    }

    public JsonBuilder add(String key, OffsetDateTime value) {
        if (value != null) root.put(key, value.toEpochSecond());
        return this;
    }

    public JsonBuilder add(String key, Object value) {
        if (value != null) root.put(key, value);
        return this;
    }

    public JsonBuilder add(String key, JsonBuilder nested) {
        if (nested != null) root.put(key, nested.build());
        return this;
    }

    public Map<String, Object> build() {
        return root;
    }
}
