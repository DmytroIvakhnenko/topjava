package ru.javawebinar.topjava.storage.impl;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.storage.Storage;

import java.util.ArrayList;
import java.util.List;

public class ListMealStorage implements Storage<Meal> {
    private final List<Meal> storage = new ArrayList<>();

    @Override
    public void add(Meal meal) {
        if (!storage.contains(meal)) {
            storage.add(meal);
        }
    }

    @Override
    public void update(Meal meal) {
        //TODO
    }

    @Override
    public Meal get(int id) {
        //TODO
        return null;
    }

    @Override
    public List<Meal> getAll() {
        return new ArrayList<>(storage);
    }

    @Override
    public void delete(int id) {
        //TODO
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
