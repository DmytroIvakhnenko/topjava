package ru.javawebinar.topjava.storage;

import ru.javawebinar.topjava.model.Meal;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class InMemoryRepository implements Repository<Meal> {
    private final AtomicInteger idGenerator = new AtomicInteger(0);
    private final Map<Integer, Meal> storage = new ConcurrentHashMap<>();

    @Override
    public Meal add(Meal meal) {
        Meal mealWithId = new Meal(meal, idGenerator.getAndIncrement());
        return storage.put(mealWithId.getId(), mealWithId);
    }

    @Override
    public Meal update(Meal meal) {
        return storage.replace(meal.getId(), meal);
    }

    @Override
    public Optional<Meal> get(int id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public List<Meal> getAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public void delete(int id) {
        storage.remove(id);
    }
}
