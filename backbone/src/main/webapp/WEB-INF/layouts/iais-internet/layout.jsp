<!DOCTYPE html>
<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" language="java"%>

<%-- BEGIN taglib --%>
<%@ taglib uri="ecquaria/sop/layout" prefix="layout"%>
<%@ taglib uri="ecquaria/sop/sop-core" prefix="sop-core"%>

<%-- END taglib --%>

<html lang="en">
<head>
    <title>HALP</title>
    <%@ include file="/WEB-INF/jsp/inc/iais-internet-common-include.jsp" %>

    <%-- BEGIN additional header --%>
    <layout:insertAttribute name="header-ext" ignore="true" />
    <%-- END additional header --%>

</head>
<body style="font-size: 14px ;">
<jsp:include page="header.jsp" />
<div class="">
    <layout:insertAttribute name="body" ignore="true" />
</div>
<jsp:include page="footer.jsp" />
</body>
</html>