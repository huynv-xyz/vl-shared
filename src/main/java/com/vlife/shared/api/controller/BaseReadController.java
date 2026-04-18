package com.vlife.shared.api.controller;

import com.vlife.shared.api.dto.ApiResponse;
import com.vlife.shared.api.dto.PageRequest;
import com.vlife.shared.api.dto.PageResult;
import io.micronaut.http.HttpResponse;

import java.util.List;
import java.util.Map;

public abstract class BaseReadController<E, ID, Q> {

    protected abstract ReadService<E, ID, Q> service();
    protected abstract ItemBuilder<E> itemBuilder();

    public HttpResponse<?> getById(ID id) {
        E entity = service().getById(id);
        return HttpResponse.ok(ApiResponse.success(itemBuilder().buildItem(entity)));
    }

    public HttpResponse<?> list(Q query, PageRequest page) {
        PageResult<E> result = service().search(query, page);

        List<Map<String, Object>> rows = result.items().stream()
                .map(itemBuilder()::buildItem)
                .toList();

        return HttpResponse.ok(ApiResponse.success(rows));
    }

    public interface ReadService<E, ID, Q> {
        E getById(ID id);
        PageResult<E> search(Q query, PageRequest page);
    }

    public interface ItemBuilder<E> {
        Map<String, Object> buildItem(E entity);
    }
}
