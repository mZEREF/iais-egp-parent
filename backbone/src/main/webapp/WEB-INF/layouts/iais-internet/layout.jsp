<!DOCTYPE html>
<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" language="java"%>

<%-- BEGIN taglib --%>
<%@ taglib uri="ecquaria/sop/layout" prefix="layout"%>
<%@ taglib uri="ecquaria/sop/sop-core" prefix="sop-core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%-- END taglib --%>

<html lang="en">
<head>
    <meta http-equiv="Pragma" content="no-cache"/>
    <meta http-equiv="Cache Control" content="no-store"/>
    <meta http-equiv="Expires" content="0"/>
    <meta name="description" content="HALP e-licensing portal for healthcare services, data submission, Inspection"/>
    <title><c:out value="${iais_Audit_Trail_dto_Attr.functionName}" default="Healthcare Application and Licensing Portal"/></title>
    <link rel="shortcut icon" href="<%=IaisEGPConstant.CSS_ROOT+IaisEGPConstant.BE_CSS_ROOT%>img/favicon.ico"/>
    <%@ include file="/WEB-INF/jsp/inc/iais-internet-common-include.jsp" %>

    <%-- BEGIN additional header --%>
    <layout:insertAttribute name="header-ext" ignore="true" />
    <%-- END additional header --%>
    <c:if test="${not empty iais_Login_User_Info_Attr}">
        <%@ include file="/WEB-INF/layouts/time-out/sessionInterTimeOut.jsp" %>
    </c:if>
</head>
<body style="font-size:16px;">
<%@ include file="header.jsp" %>
<div class="">
    <layout:insertAttribute name="body" ignore="true" />
</div>
<%@ include file="footer.jsp" %>
</body>
</html>