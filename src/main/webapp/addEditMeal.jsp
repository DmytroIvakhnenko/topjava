<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="ru.javawebinar.topjava.enums.Action" %>

<html lang="ru">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="css/style.css">
    <title>Meals</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>${Action.EDIT.action.equals(param.action) ? "Edit" : "Add"} meal</h2>
<br>
<form method="post" action="meals" enctype="application/x-www-form-urlencoded">
    <input type="hidden" name="action" value="${param.action}">
    <input type="hidden" name="id" value="${meal != null ? meal.id : null}">
    <dl>
        <dt>DateTime:</dt>
        <dd><input type="datetime-local" name="dateTime" size=40
                   value="${meal != null ? meal.dateTime : "2020-01-30T08:00"}" required></dd>
    </dl>
    <dl>
        <dt>Description:</dt>
        <dd><input type="text" name="description" size=40 value="${meal != null ? meal.description : "Default desc"}"
                   required></dd>
    </dl>
    <dl>
        <dt>Calories:</dt>
        <dd><input type="number" name="calories" size=40 value="${meal != null ? meal.calories : 100}" required></dd>
    </dl>
    <button type="submit">Save changes</button>
    <button type="button" onclick="window.history.back()">Cancel</button>
</form>
</body>
</html>