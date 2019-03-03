<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <title><c:out value="${user.firstName}"/>  <c:out value="${user.lastName}"/> profile</title>
</head>
<body>
    <ul>
        <li>id: <c:out value="${user.id}"/></li>
        <li>first name: <c:out value="${user.firstName}"/></li>
        <li>last name: <c:out value="${user.lastName}"/></li>
        <li>jobTitle: <c:out value="${user.jobTitle}"/></li>
        <li>bio: <c:out value="${user.bio}"/></li>
        <li>
            skills:
            <ul>
                <c:forEach var="skill" items="${user.skills}">
                    <li>
                        <c:out value="${skill.name}"/> : <c:out value="${skill.point}"/>
                            <form action="<c:url value="/deleteSkill"/>" method="GET">
                                <input type="hidden" name="userID" value="${user.id}"/>
                                <input type="hidden" name="skillName" value="${skill.name}"/>
                                <input type="submit" value="Delete"/>
                            </form>
                    </li>
                </c:forEach>
            </ul>
        </li>
    </ul>
    Add Skill:
    <form action="<c:url value="/addSkill"/>" method="GET">
        <select name="choosedSkill">
            <c:forEach var="skill" items="${skillList}">
                <option value="${skill}"><c:out value="${skill}"/></option>
            </c:forEach>
        </select>
        <input type="hidden" name="userID" value="${user.id}"/>
        <button>Add</button>
    </form>


</body>
</html>
