<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>OdarKK</title>
<%@ include file="../commons/_header.jspf" %>
</head>
<body>
	<%@ include file="../commons/_top.jspf" %>
<form:form modelAttribute="authenticate" action="/users/login" method="post">
  <title> 로그인 </title>
 <table width=550 border=1 align=center>
 <tr>
  <td colspan=2 bgcolor=#99cc00 align=center>로그인
 <tr>
  <td>사용자 아이디
  <td>
	<form:input path="userId" />
	<form:errors path="userId" cssClass="error" />

 <tr>
  <td>비밀번호
  <td><form:password path="password" />
  <form:errors path="password" cssClass="error" />
 <c:if test="${not empty errorMessage}">
 <tr>
  <td>
  <td>${errorMessage }
 </c:if>
  <td bgcolor=#eeeeee colspan=2 align=center>
  <input type=submit value='로그인'>

</table>
</form:form>
</body>
</html>

