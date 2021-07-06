package ru.javawebinar.topjava.repository.datajpa;

import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Repository
@Profile(Profiles.DATAJPA)
@Transactional(readOnly = true)
public class DataJpaMealRepository implements MealRepository {

    private static final Sort SORT_DATE = Sort.by(Sort.Direction.DESC, "dateTime");

    private final CrudMealRepository crudRepository;

    private final DataJpaUserRepository userRepository;

    public DataJpaMealRepository(CrudMealRepository crudRepository, DataJpaUserRepository userRepository) {
        this.crudRepository = crudRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public Meal save(Meal meal, int userId) {
        meal.setUser(userRepository.get(userId));
        if (meal.isNew() || Objects.nonNull(crudRepository.findByIdAndUserId(meal.id(), userId))) {
            return crudRepository.save(meal);
        } else {
            return null;
        }
    }

    @Override
    @Transactional
    public boolean delete(int id, int userId) {
        return crudRepository.deleteByIdAndUserId(id, userId) != 0;
    }

    @Override
    public Meal get(int id, int userId) {
        return crudRepository.findByIdAndUserId(id, userId);
    }

    @Override
    public List<Meal> getAll(int userId) {
        return crudRepository.findAllByUserId(userId, SORT_DATE);
    }

    @Override
    public List<Meal> getBetweenHalfOpen(LocalDateTime startDateTime, LocalDateTime endDateTime, int userId) {
        return crudRepository.findAllByDateTimeGreaterThanEqualAndDateTimeLessThanAndUserId(startDateTime,
                endDateTime,
                userId,
                SORT_DATE);
    }
}
