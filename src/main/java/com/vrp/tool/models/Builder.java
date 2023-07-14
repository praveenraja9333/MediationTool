package com.vrp.tool.models;

public interface Builder<T> {

    Object build(T t);
    Object build();
}
