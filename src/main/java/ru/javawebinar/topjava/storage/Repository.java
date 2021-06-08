package ru.javawebinar.topjava.storage;

import java.util.List;
import java.util.Optional;

public interface Repository<T> {

    T add(T t);

    T update(T t);

    Optional<T> get(int id);

    List<T> getAll();

    void delete(int id);
}
