<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

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
    <c:forEach var="meal" items="${allMeals}">
        <c:choose>
            <c:when test="${meal.excess}">
                <tr style="color: darkred">
            </c:when>
            <c:otherwise>
                <tr style="color: darkgreen">
            </c:otherwise>
        </c:choose>
        <td></td>
        <td>
            <fmt:parseDate value="${meal.dateTime}" pattern="yyyy-MM-dd'T'HH:mm" var="parsedDateTime" type="both"/>
            <fmt:formatDate pattern="yyyy-MM-dd HH:mm" value="${parsedDateTime}"/>
        </td>
        <td>${meal.description}</td>
        <td>${meal.calories}</td>
        <td></td>
        <td></td>
        </tr>
    </c:forEach>
</table>
</body>
</html>