<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>

<div class="mod mod-user-list" data-connectors="1">
	<table class="tablesorter"> 
		<thead> 
			<tr> 
			    <th>Username</th> 
			    <th>First Name</th> 
			    <th>Last Name</th> 
			    <th>Actions</th> 
			</tr> 
		</thead> 
		<tbody> 
			<c:forEach items="${users}" var="user">
				<tr> 
				    <td>${user.username}</td>
				    <td>${user.firstname}</td>
				    <td>${user.lastname}</td>
				    <td><a class="edit btn btn-mini" href="<c:url value="/users/${user.username}"/>" ><i class="icon-edit"></i> Edit</a></td>
				</tr> 
			</c:forEach>
		</tbody> 
	</table> 
</div>
