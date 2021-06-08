package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.storage.Repository;
import ru.javawebinar.topjava.storage.InMemoryRepository;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

import static org.slf4j.LoggerFactory.getLogger;
import static ru.javawebinar.topjava.enums.Action.*;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(MealServlet.class);
    private Repository<Meal> storage;

    @Override
    public void init() {
        storage = new InMemoryRepository();
        MealsUtil.getSampleData()
                .forEach(storage::add);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        log.debug("Get request received with action {}", action);

        if (ADD.getAction().equals(action)) {
            request.getRequestDispatcher("/addEditMeal.jsp").forward(request, response);
        } else if (EDIT.getAction().equals(action)) {
            int id = getAsInt(request, "id");
            Optional<Meal> meal = storage.get(id);
            if (meal.isPresent()) {
                request.setAttribute("meal", meal.get());
                request.getRequestDispatcher("/addEditMeal.jsp").forward(request, response);
            }
        } else if (DELETE.getAction().equals(action)) {
            storage.delete(getAsInt(request, "id"));
            response.sendRedirect("meals");
        } else {
            request.setAttribute("allMeals", MealsUtil.filteredByStreams(storage.getAll(), LocalTime.MIN, LocalTime.MAX, MealsUtil.CALORIES_PER_DAY));
            request.getRequestDispatcher("/meals.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");
        log.debug("Post request received with action {}", action);

        if (ADD.getAction().equals(action)) {
            storage.add(Meal.of(LocalDateTime.parse(request.getParameter("dateTime")),
                    request.getParameter("description"),
                    getAsInt(request, "calories")));
        } else if (EDIT.getAction().equals(action)) {
            storage.update(Meal.of(
                    LocalDateTime.parse(request.getParameter("dateTime")),
                    request.getParameter("description"),
                    getAsInt(request, "calories"),
                    getAsInt(request, "id")));
        } else {
            log.error("Unexpected action value: {}", action);
            throw new IllegalStateException("Unexpected action value: " + action);
        }
        response.sendRedirect("meals");
    }

    private int getAsInt(HttpServletRequest request, String intParam) {
        return Integer.parseInt(request.getParameter(intParam));
    }
}
