<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
		
<!-- Load all javascript in single file -->			
<script type="text/javascript" src="<c:url value="/terrific/main.js?debug=true"/>"></script>

<!-- Bootstrap terrific -->
<script type="text/javascript">		
	(function($){
		$(document).ready(function(){
		
			var $page=$("html");
			config={
					
			};
			var application=new Tc.Application($page, config);
			application.registerModules();
			application.start();
		})
	;})(Tc.$);
</script>
