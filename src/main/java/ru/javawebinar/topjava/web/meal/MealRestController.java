package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static ru.javawebinar.topjava.util.ValidationUtil.assureIdConsistent;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNew;

@Controller
public class MealRestController {
    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private MealService service;

    public List<MealTo> getAll() {
        log.info("getAll");
        return MealsUtil.getTos(service.getAll(SecurityUtil.authUserId()), SecurityUtil.authUserCaloriesPerDay());
    }

    public List<MealTo> getAllFilteredByDateTime(LocalDate startDate, LocalDate endDate, LocalTime startTime, LocalTime endTime) {
        log.info("getAllFilteredByDateTime");
        List<Meal> allFilterMealsByDate = service.getAllFilterByDate(SecurityUtil.authUserId(),
                Optional.ofNullable(startDate).orElse(LocalDate.MIN),
                Optional.ofNullable(endDate).map(d -> d.plusDays(1)).orElse(LocalDate.MAX));
        return MealsUtil.getFilteredTos(allFilterMealsByDate,
                SecurityUtil.authUserCaloriesPerDay(),
                Optional.ofNullable(startTime).orElse(LocalTime.MIN),
                Optional.ofNullable(endTime).orElse(LocalTime.MAX));
    }

    public Meal get(int id) {
        log.info("get {}", id);
        return service.get(id, SecurityUtil.authUserId());
    }

    public Meal create(Meal meal) {
        log.info("create {}", meal);
        checkNew(meal);
        return service.create(meal, SecurityUtil.authUserId());
    }

    public void delete(int id) {
        log.info("delete {}", id);
        service.delete(id, SecurityUtil.authUserId());
    }

    public void update(Meal meal, int mealId) {
        log.info("update {} with id={}", meal, meal.getId());
        assureIdConsistent(meal, mealId);
        service.update(meal, SecurityUtil.authUserId());
    }
}