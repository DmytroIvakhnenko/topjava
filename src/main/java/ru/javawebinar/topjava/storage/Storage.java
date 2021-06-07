package ru.javawebinar.topjava.storage;

import java.util.List;
import java.util.Optional;

public interface Storage<T> {

    void add(T t);

    void update(T t);

    Optional<T> get(int id);

    List<T> getAll();

    void delete(int id);

    void deleteAll();

    int getSize();
}
