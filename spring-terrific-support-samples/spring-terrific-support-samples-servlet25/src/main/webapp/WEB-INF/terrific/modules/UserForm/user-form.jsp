<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<div class="mod mod-user-form" data-connectors="1">
	<form:form method="post" commandName="user">
		<div>
			<form:label path="username" cssErrorClass="err">Username</form:label>
			<form:input path="username" cssErrorClass="err" />
			<form:errors path="username" cssClass="err" />
		</div>
		<div>
			<form:label path="firstname" cssErrorClass="err">Firstname</form:label>
			<form:input path="firstname" cssErrorClass="err" />
			<form:errors path="firstname" cssClass="err" />
		</div>
		<div>
			<form:label path="lastname" cssErrorClass="err">Lastname</form:label>
			<form:input path="lastname" cssErrorClass="err" />
			<form:errors path="lastname" cssClass="err" />
		</div>
		<div>
			<input type="submit" />
		</div>
	</form:form>
</div>
