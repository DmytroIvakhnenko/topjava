package ru.javawebinar.topjava.storage;

import java.util.List;

public interface Storage<T> {

    void add(T t);

    void update(T t);

    T get(int id);

    List<T> getAll();

    void delete(int id);

    void deleteAll();

    int getSize();
}
