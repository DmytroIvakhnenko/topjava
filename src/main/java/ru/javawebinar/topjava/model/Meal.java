package ru.javawebinar.topjava.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;

public class Meal {
    private final Integer id;
    private final LocalDateTime dateTime;
    private final String description;
    private final int calories;

    private Meal(Integer id, LocalDateTime dateTime, String description, int calories) {
        this.id = id;
        this.dateTime = dateTime;
        this.description = description;
        this.calories = calories;
    }

    public static Meal from(Meal meal, int id){
        return new Meal(id, meal.getDateTime(), meal.getDescription(), meal.getCalories());
    }

    public static Meal of(LocalDateTime dateTime, String description, int calories){
        return new Meal(null, dateTime, description, calories);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Meal meal = (Meal) o;

        if (calories != meal.calories) return false;
        if (!Objects.equals(id, meal.id)) return false;
        if (!description.equals(meal.description)) return false;
        return dateTime.equals(meal.dateTime);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + description.hashCode();
        result = 31 * result + dateTime.hashCode();
        result = 31 * result + calories;
        return result;
    }

    public Integer getId() {
        return id;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public String getDescription() {
        return description;
    }

    public int getCalories() {
        return calories;
    }

    public LocalDate getDate() {
        return dateTime.toLocalDate();
    }

    public LocalTime getTime() {
        return dateTime.toLocalTime();
    }
}
