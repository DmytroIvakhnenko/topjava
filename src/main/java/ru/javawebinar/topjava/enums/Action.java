package ru.javawebinar.topjava.enums;

import java.util.Arrays;

public enum Action {
    ADD("add"),
    EDIT("edit"),
    DELETE("delete"),
    VIEW("view");

    private final String action;

    Action(String action) {
        this.action = action;
    }

    public String getAction() {
        return action;
    }

    public static Action from(String action) {
        return Arrays.stream(Action.values())
                .filter(a -> a.getAction().equals(action))
                .findFirst()
                .orElse(VIEW);
    }
}
