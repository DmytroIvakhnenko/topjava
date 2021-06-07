<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="ru.javawebinar.topjava.util.Action" %>

<html lang="ru">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="css/style.css">
    <title>Meals</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>Meals</h2>
<br>
<table>
    <tr>
        <th></th>
        <th>Date/Time</th>
        <th>Meal</th>
        <th>Calories</th>
        <th></th>
        <th></th>
    </tr>
    <a href="meals?action=${Action.ADD.action}">Add meal</a>
    <c:forEach var="meal" items="${allMeals}">
        <tr style="color: ${meal.excess ? 'darkred' : 'darkgreen'}">
        <td></td>
        <td>
            <fmt:parseDate value="${meal.dateTime}" pattern="yyyy-MM-dd'T'HH:mm" var="parsedDateTime" type="both"/>
            <fmt:formatDate pattern="yyyy-MM-dd HH:mm" value="${parsedDateTime}"/>
        </td>
        <td>${meal.description}</td>
        <td>${meal.calories}</td>
        <td><a href="meals?id=${meal.id}&action=${Action.EDIT.action}">Edit</a></td>
        <td><a href="meals?id=${meal.id}&action=${Action.DELETE.action}">Delete</a></td>
        </tr>
    </c:forEach>
</table>
</body>
</html>