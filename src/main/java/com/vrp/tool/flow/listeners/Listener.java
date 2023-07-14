package com.vrp.tool.flow.listeners;

import java.util.List;

public interface Listener<T> {
    void onPublish(T t);
    void onRemove(T t);
    void onPublish(List<T> T);
    void onRemove(List<T> T);
}
