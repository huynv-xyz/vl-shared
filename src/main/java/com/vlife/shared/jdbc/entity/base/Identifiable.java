package com.vlife.shared.jdbc.entity.base;

public interface Identifiable<ID> {
    ID getId();
    void setId(ID id);
}
