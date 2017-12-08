<%@ page pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<script src="slideshow/js/modernizr.custom.63321.js"></script>
<script src="slideshow/js/jquery.catslider.js"></script>
<link href="slideshow/css/catslider.css" rel="stylesheet" />

<div class="nn-sheet">
<div class="row">
	<div class="col-md-9">
		<div id="carousel-example-generic" class="carousel slide"
			data-ride="carousel">
			<!-- Indicators -->
			<ol class="carousel-indicators">
				<li data-target="#carousel-example-generic" data-slide-to="0"
					class="active"></li>
				<li data-target="#carousel-example-generic" data-slide-to="1"></li>
				<li data-target="#carousel-example-generic" data-slide-to="2"></li>
				<li data-target="#carousel-example-generic" data-slide-to="3"></li>
			</ol>

			<!-- Wrapper for slides -->
			<div class="carousel-inner" role="listbox">
				<div class="item active">
					<img src="images/contacts/Panel1.jpg" alt="Panel1">
					<div class="carousel-caption">...</div>
				</div>
				<div class="item">
					<img src="images/contacts/Panel2.jpg" alt="Panel2">
					<div class="carousel-caption">...</div>
				</div>
				<div class="item">
					<img src="images/contacts/Panel3.jpg" alt="Panel3">
					<div class="carousel-caption">...</div>
				</div>
				<div class="item">
					<img src="images/contacts/Panel4.jpg" alt="Panel4">
					<div class="carousel-caption">...</div>
				</div>
			</div>

			<!-- Controls -->
			<a class="left carousel-control" href="#carousel-example-generic"
				role="button" data-slide="prev"> <span
				class="glyphicon glyphicon-chevron-left" aria-hidden="true"></span>
				<span class="sr-only">Previous</span>
			</a> <a class="right carousel-control" href="#carousel-example-generic"
				role="button" data-slide="next"> <span
				class="glyphicon glyphicon-chevron-right" aria-hidden="true"></span>
				<span class="sr-only">Next</span>
			</a>
		</div>
	</div>

    <div class="col-md-3">
		<img alt="Trans" src="images/transfer.png">
    </div>
</div>

<br>

<div class="row">
	<div class="col-md-12">
		<div class="product-title">
			<a class="title" ><spring:message code="home.topdc"/></a>
			<a class="linktoProduct" href="product/special/salesoff.htm"><spring:message code="home.topdc.more"/></a>
		</div>
	</div>
</div>

<div class="row">
	<div class="col-md-12">
		<c:forEach var="p" items="${saleoffProducts}">
			<div class="box">
				<ul>
					<li><p style="font-size: 16px;">${p.name}</p></li>
					<li><a href="product/detail/${p.id}.htm"> <img
							id="${p.id}" src="images/products/${p.image}">
					</a></li>
					<li class="productPrice" >
					<div style="display: inline-block;" class="new-price"><fmt:formatNumber maxFractionDigits="2">${p.unitPrice*(1-p.discount)}</fmt:formatNumber>$</div>
					<div style="display: inline-block;" class="old-price"> <fmt:formatNumber maxFractionDigits="2">${p.unitPrice}</fmt:formatNumber>$</div>
					<div style="display: inline-block;" class="remains"><spring:message code="home.topdc.remain"/> ${p.quantity}</div>
					</li>
					<li class="feature"> <a class="discount">-<fmt:formatNumber value="${p.discount}" type="percent" /></a>
					</li>
				</ul>
			</div>
		</c:forEach>
	</div>
</div>
</div>

<style>
.product-title{
width: 80%;
border: 1px solid orange;
border-radius: 5px;
background: orange;
position: relative;
height: 40px;
}
a.title{
color: white;
font-weight: bold;
font-size: 25px;
position: absolute;
left: 15px;
top: 4px;
}
.linktoProduct{
position: absolute;
right: 0px;
margin-right: 10px;
top: 8px;
font-weight: bold;
}
.box>ul img[id]:hover{
opacity: 0.6;
}
.box>ul:hover{
	box-shadow:0 0 5px red;
}
.productPrice{
	text-align: left;
}
.new-price{
font-size: 25px;
color: red;
margin-left: 15px;
}
.old-price{
font-size: 17px;
color: gray;
text-decoration: line-through;
}
.box .discount{
	font-size: 17px;
	width: 48px;
	height: 30px;
}
.remains{
background-color: #99FF33;
border:1px solid #99FF33;
position: absolute;
padding: 2px 5px 2px 5px;
right: 10px;
bottom: 10px;
font-size: 15px;
}
</style>