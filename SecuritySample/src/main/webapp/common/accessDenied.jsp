<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isErrorPage="true" %>

<%@ page import="org.springframework.security.core.context.SecurityContextHolder"%>
<%@ page import="org.springframework.security.core.Authentication" %>
<%@ page import="org.springframework.security.core.userdetails.UserDetails" %>
<%@ page import="org.springframework.security.core.userdetails.AuthenticationUserDetailsService" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<html>
<head>
<title>logout</title>

<link href="<c:url value='/css/egovframework/sample.css'/>" rel="stylesheet" type="text/css">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

</head>
<body bgcolor="#ffffff" text="#000000">
<br>
<br>

<div align="center">
<table width="300" border="0" cellpadding="0" cellspacing="0" align="center">
	<tr>
		<td>
			<table border="0" width="100%" align="center">
				<tr>
					<td align="center">
						접근권한이 없습니다. <br> 담당자에게 문의하여 주시기 바랍니다.<br>
						${SPRING_SECURITY_403_EXCEPTION }
						<br>
						<%
						Authentication auth = SecurityContextHolder.getContext().getAuthentication();
						Object principal = auth.getPrincipal();
						if (principal instanceof UserDetails) {
							String username = ((UserDetails)principal).getUsername();
							String password = ((UserDetails)principal).getPassword();
							out.println("Account : " + username.toString() + "<br>");
						}
						%>
					</td>
				</tr>
				<tr>
					<td height="30">&nbsp;</td>
				</tr>
				
				<tr>
					<td align="center" height="25">
						<a href="<c:url value='/j_spring_security_logout'/>">로그아웃</a>
					</td>
				</tr>
			
			</table>
		
		</td>
	</tr>
		
</table>
</div>


</body>
</html>