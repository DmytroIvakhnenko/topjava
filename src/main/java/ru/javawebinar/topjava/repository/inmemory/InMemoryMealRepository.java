package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static ru.javawebinar.topjava.util.MealsUtil.ADMIN_ID;
import static ru.javawebinar.topjava.util.MealsUtil.USER_ID;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private static final Logger log = LoggerFactory.getLogger(InMemoryMealRepository.class);
    // Map <userId, Map <mealId, meal>>
    private final Map<Integer, Map<Integer, Meal>> repository = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.userMeals.forEach(meal -> save(meal, USER_ID));
        MealsUtil.adminMeals.forEach(meal -> save(meal, ADMIN_ID));
    }

    @Override
    public Meal save(Meal meal, int userId) {
        return meal.isNew() ? create(meal, userId) : update(meal, userId);
    }

    private Meal update(Meal meal, int userId) {
        // handle case: update, but not present in storage
        log.info("update {}", meal);
        return Optional.ofNullable(this.get(meal.getId(), userId))
                .map(m -> {
                    repository.get(userId).put(meal.getId(), meal);
                    return meal;
                }).orElse(null);
    }

    private Meal create(Meal meal, int userId) {
        log.info("create {}", meal);
        meal.setId(counter.incrementAndGet());
        meal.setUserId(userId);

        Optional.ofNullable(repository.get(userId))
                .orElseGet(() -> {
                    Map<Integer, Meal> map = new ConcurrentHashMap<>();
                    repository.put(userId, map);
                    return map;
                }).put(meal.getId(), meal);
        return meal;
    }

    @Override
    public boolean delete(int id, int userId) {
        log.info("delete meal with id:{}", id);
        return Optional.ofNullable(repository.get(userId))
                .map(m -> m.remove(id) != null)
                .orElse(false);
    }

    @Override
    public Meal get(int id, int userId) {
        log.info("get meal with id:{}", id);
        return Optional.ofNullable(repository.get(userId))
                .map(m -> m.get(id))
                .orElse(null);
    }

    @Override
    public List<Meal> getAll(int userId) {
        log.info("get all meal for user:{}", userId);
        return getAllFiltered(userId, m -> true);
    }

    @Override
    public List<Meal> getAllFilterByDate(int userId, LocalDate startDate, LocalDate endDate) {
        log.info("get all meal for user:{} filtered by dates", userId);
        return getAllFiltered(userId, m -> DateTimeUtil.isBetweenHalfOpen(m.getDate(), startDate, endDate));
    }

    private List<Meal> getAllFiltered(int userId, Predicate<Meal> filter) {
        return Optional.ofNullable(repository.get(userId))
                .map(m -> m.values().stream()
                        .filter(filter)
                        .collect(Collectors.toList()))
                .orElseGet(ArrayList::new);
    }
}

