package com.vrp.tool.flow.publishers;

import java.util.List;

public interface Publisher<T> {
    void published(T t);
    void removed(T t);
    void removed(List<T> T);
    void published(List<T> T);
}
