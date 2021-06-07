package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.storage.Storage;
import ru.javawebinar.topjava.storage.impl.MealStorage;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;
import java.util.Optional;

import static org.slf4j.LoggerFactory.getLogger;
import static ru.javawebinar.topjava.util.Action.*;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(MealServlet.class);
    private Storage<Meal> storage;

    @Override
    public void init() {
        storage = new MealStorage();
        MealsUtil.getSampleData()
                .forEach(storage::add);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        log.debug("Get request received with action {}", action);

        if (Objects.isNull(action)) {
            request.setAttribute("allMeals", MealsUtil.filteredByStreams(storage.getAll(), LocalTime.MIN, LocalTime.MAX, MealsUtil.CALORIES_PER_DAY));
            request.getRequestDispatcher("/meals.jsp").forward(request, response);
        } else if (ADD.getAction().equals(action)) {
            request.getRequestDispatcher("/add_edit.jsp").forward(request, response);
        } else if (EDIT.getAction().equals(action)) {
            int id = getAsInt(request, "id");
            Optional<Meal> meal = storage.get(id);
            if (meal.isPresent()) {
                request.setAttribute("meal", meal.get());
                request.getRequestDispatcher("/add_edit.jsp").forward(request, response);
            } else {
                log.error("Meal with id:{} doesn't exist", id);
                response.sendRedirect("meals");
            }
        } else if (DELETE.getAction().equals(action)) {
            storage.delete(getAsInt(request, "id"));
            response.sendRedirect("meals");
        } else {
            log.error("Unexpected action value: {}", action);
            throw new IllegalStateException("Unexpected action value: " + action);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");
        log.debug("Post request received with action {}", action);

        if (ADD.getAction().equals(action)) {
            storage.add(MealsUtil.createMeal(getDateTime(request), request.getParameter("description"), getAsInt(request, "calories")));
        } else if (EDIT.getAction().equals(action)) {
            storage.get(getAsInt(request, "id"))
                    .ifPresent(meal -> storage.update(new Meal(meal.getId(), getDateTime(request), request.getParameter("description"), getAsInt(request, "calories"))));
        } else {
            log.error("Unexpected action value: {}", action);
            throw new IllegalStateException("Unexpected action value: " + action);
        }
        response.sendRedirect("meals");
    }

    private int getAsInt(HttpServletRequest request, String intParam) {
        return Integer.parseInt(request.getParameter(intParam));
    }

    private LocalDateTime getDateTime(HttpServletRequest request) {
        return LocalDateTime.parse(request.getParameter("dateTime"));
    }
}
