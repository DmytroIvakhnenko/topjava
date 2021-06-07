package ru.javawebinar.topjava.storage.impl;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.storage.Storage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class MealStorage implements Storage<Meal> {
    private final Map<Integer, Meal> storage = new ConcurrentHashMap<>();

    @Override
    public void add(Meal meal) {
        storage.putIfAbsent(meal.getId(), meal);
    }

    @Override
    public void update(Meal meal) {
        storage.replace(meal.getId(), meal);
    }

    @Override
    public Optional<Meal> get(int id) {
        return Optional.ofNullable(storage.getOrDefault(id, null));
    }

    @Override
    public List<Meal> getAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public void delete(int id) {
        storage.remove(id);
    }

    @Override
    public void deleteAll() {
        storage.clear();
    }

    @Override
    public int getSize() {
        return storage.size();
    }
}
