package com.vlife.shared.api.builder;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public abstract class ItemBuilder<T> {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Inject
    protected ObjectMapper objectMapper;


    public Map<String, Object> buildItem(T item) {
        return autoBuild(item);
    }

    public Map<String, Object> buildItemFull(T item) {
        return buildItem(item);
    }

    public List<Map<String, Object>> buildList(List<T> items) {
        if (items == null || items.isEmpty()) return Collections.emptyList();

        List<Map<String, Object>> list = new ArrayList<>(items.size());
        for (T item : items) list.add(buildItem(item));
        return list;
    }

    protected Map<String, Object> autoBuild(T item) {
        if (item == null) return Collections.emptyMap();
        return objectMapper.convertValue(item, new TypeReference<Map<String, Object>>() {});
    }

    protected Map<String, Object> autoBuildAny(Object item) {
        if (item == null) return Collections.emptyMap();
        return objectMapper.convertValue(item, new TypeReference<Map<String, Object>>() {});
    }
}
