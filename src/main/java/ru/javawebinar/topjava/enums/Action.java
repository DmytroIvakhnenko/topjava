package ru.javawebinar.topjava.enums;

public enum Action {
    ADD("add"),
    EDIT("edit"),
    DELETE("delete");

    private final String action;

    Action(String action) {
        this.action = action;
    }

    public String getAction() {
        return action;
    }
}
