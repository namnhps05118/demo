<%@ page pageEncoding="utf-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<h1><spring:message code="contact.title"/></h1>
<hr>

<div class="row">
	<div class="col-md-8">
		<div class="panel panel-info">
			<div class="panel-heading">
				<b><spring:message code="contact.company.name"/></b>
			</div>
			<div class="panel-body">
				<spring:message code="contact.company.address"/>
				<br><spring:message code="contact.company.businessNo"/><br>
				<spring:message code="contact.company.phone"/><br> 
				Email:projectjava26012015@gmail.com<br>
			</div>
		</div>
	</div>

	<div class="col-md-4"></div>
</div>

<div class="row">
	<div class="col-md-12">
		<p style="font-size: 22px;"><b><spring:message code="contact.map"/></b></p>
		<iframe class="map" style="width: 100%; height: 400px;"
			src="http://www.meomap.net/embmap.htm?ids=529"></iframe>
	</div>
</div>
