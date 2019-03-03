<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <title><c:out value="${project.title}"/></title>
</head>
<body>
    <ul>
        <li>id:  <c:out value="${project.id}"/></li>
        <li>title: <c:out value="${project.title}"/></li>
        <li>description: <c:out value="${project.descp}"/></li>
        <li>imageUrl: <img src="<c:out value="${project.picURL}"/>" style="width: 50px; height: 50px;"></li>
        <li>budget: <c:out value="${project.budget}"/></li>
    </ul>
    <c:if test="${!thisUser.hasBidded(project.id)}">
        <form action="<c:url value="/bidProject"/>" method="GET">
                <%--@declare id="bidamount"--%><label for="bidAmount">Bid Amount:</label>
            <input type="number" name="bidAmount">

            <button>Submit</button>
        </form>
    </c:if>
</body>
</html>
