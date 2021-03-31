<!DOCTYPE html>
<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" language="java"%>

<%-- BEGIN taglib --%>
<%@ taglib uri="ecquaria/sop/layout" prefix="layout"%>
<%@ taglib uri="ecquaria/sop/sop-core" prefix="sop-core"%>

<%-- END taglib --%>

<html lang="en">
<head>
    <meta http-equiv="Pragma" content="no-cache"/>
    <meta http-equiv="Cache Control" content="no-store"/>
    <meta http-equiv="Expires" content="0"/>
    <meta name="description" content="HALP e-licensing portal for healthcare services, data submission, Inspection"/>
    <title><layout:insertAttribute name="title" ignore="true" /></title>
    <%@ include file="/WEB-INF/jsp/inc/iais-internet-common-include.jsp" %>
    <link rel="shortcut icon" href="<%=IaisEGPConstant.CSS_ROOT+IaisEGPConstant.BE_CSS_ROOT%>img/favicon.ico"/>

    <%-- BEGIN additional header --%>
    <layout:insertAttribute name="header-ext" ignore="true" />
    <%-- END additional header --%>

</head>
<body style="font-size: 14px ;">

<div class="">
    <layout:insertAttribute name="body" ignore="true" />
</div>
</body>
</html>