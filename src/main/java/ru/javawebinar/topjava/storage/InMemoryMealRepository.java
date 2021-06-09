package ru.javawebinar.topjava.storage;

import ru.javawebinar.topjava.model.Meal;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class InMemoryMealRepository implements Repository<Meal> {
    private final AtomicInteger idGenerator = new AtomicInteger(0);
    private final Map<Integer, Meal> storage = new ConcurrentHashMap<>();

    @Override
    public Meal add(Meal meal) {
        Meal mealWithId = new Meal(idGenerator.getAndIncrement(), meal);
        return Objects.isNull(storage.put(mealWithId.getId(), mealWithId)) ? mealWithId : null;
    }

    @Override
    public Meal update(Meal meal) {
        return Objects.nonNull(storage.replace(meal.getId(), meal)) ? meal : null;
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
