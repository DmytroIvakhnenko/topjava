package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.enums.Action;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.storage.Repository;
import ru.javawebinar.topjava.storage.InMemoryMealRepository;
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
        storage = new InMemoryMealRepository();
        MealsUtil.getSampleData()
                .forEach(storage::add);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        log.debug("Get request received with action {}", action);

        switch (Action.from(action)) {
            case ADD:
                request.setAttribute("meal", MealsUtil.getDefaultMeal());
                request.getRequestDispatcher("/addEditMeal.jsp").forward(request, response);
                break;
            case EDIT:
                int id = getAsInt(request, "id");
                Optional<Meal> meal = storage.get(id);
                if (meal.isPresent()) {
                    request.setAttribute("meal", meal.get());
                    request.getRequestDispatcher("/addEditMeal.jsp").forward(request, response);
                }
                break;
            case DELETE:
                storage.delete(getAsInt(request, "id"));
                response.sendRedirect("meals");
                break;
            default:
                request.setAttribute("allMeals",
                        MealsUtil.filteredByStreams(storage.getAll(),
                                LocalTime.MIN,
                                LocalTime.MAX,
                                MealsUtil.CALORIES_PER_DAY));
                request.getRequestDispatcher("/meals.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");
        log.debug("Post request received with action {}", action);

        if (ADD.getAction().equals(action)) {
            storage.add(new Meal(LocalDateTime.parse(request.getParameter("dateTime")),
                    request.getParameter("description"),
                    getAsInt(request, "calories")));
        } else if (EDIT.getAction().equals(action)) {
            storage.update(new Meal(getAsInt(request, "id"),
                    LocalDateTime.parse(request.getParameter("dateTime")),
                    request.getParameter("description"),
                    getAsInt(request, "calories")));
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
