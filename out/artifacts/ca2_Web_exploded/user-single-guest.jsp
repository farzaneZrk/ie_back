<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <meta charset="UTF-8">
    <title><c:out value="${requestedUser.firstName}"/>  <c:out value="${requestedUser.lastName}"/> profile</title>

</head>
<body>
    <ul>
        <li>id: <c:out value="${requestedUser.id}"/></li>
        <li>first name: <c:out value="${requestedUser.firstName}"/></li>
        <li>last name: <c:out value="${requestedUser.lastName}"/></li>
        <li>jobTitle: <c:out value="${requestedUser.jobTitle}"/></li>
        <li>bio: <c:out value="${requestedUser.bio}"/></li>
        <li>
            skills:
            <ul>
                <c:forEach var="skill" items="${requestedUser.skills}">
                    <li>
                        <c:out value="${skill.name}"/> : <c:out value="${skill.point}"/>
                        <c:if test="${!skill.hasEndorsed(thisUser.id)}">
                            <form action="<c:url value="/endorse"/>" method="GET">
                                <input type="hidden" name="endorserID" value="${thisUser.id}"/>
                                <input type="hidden" name="endorsedID" value="${requestedUser.id}"/>
                                <input type="hidden" name="skillName" value="${skill.name}"/>
                                <input type="submit" value="Endorse"/>
                            </form>
                        </c:if>
                    </li>
                </c:forEach>
            </ul>
        </li>
    </ul>
</body>
</html>