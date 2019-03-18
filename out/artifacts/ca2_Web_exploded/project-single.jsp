<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <title><c:out value="${project.title}"/></title>
</head>
<body>

    <c:if test="${not empty msg}">
        <h1><c:out value="${msg}"/></h1>
    </c:if>

    <ul>
        <li>id:  <c:out value="${project.id}"/></li>
        <li>title: <c:out value="${project.title}"/></li>
        <li>description: <c:out value="${project.description}"/></li>
        <li>imageUrl: <img src="<c:out value="${project.imageUrl}"/>" style="width: 50px; height: 50px;"></li>
        <li>budget: <c:out value="${project.budget}"/></li>
    </ul>
    <br><br>
    <c:if test="${!thisUser.hasBidded(project.id)}">
        <c:if test="${project.checkUserForProject(thisUser.id)}">
            <form action="<c:url value="/bidProject"/>" method="GET">
                    <%--@declare id="bidamount"--%><label for="bidAmount">Bid Amount:</label>
                <input type="number" name="bidAmount">
                <input type="hidden" name="userID" value="${thisUser.id}"/>
                <input type="hidden" name="projectID" value="${project.id}"/>

                <button>Submit</button>
            </form>
        </c:if>
    </c:if>
    <c:if test="${thisUser.hasBidded(project.id)}">
        <p>You have bid on this project before.</p>
    </c:if>
</body>
</html>
