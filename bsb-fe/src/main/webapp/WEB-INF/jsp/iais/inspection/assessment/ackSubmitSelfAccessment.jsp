<%@ page import="static sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT" %>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>

<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-internet"/>

<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-assessment.js"></script>

<%@include file="dashboard.jsp"%>

<div class="main-content">
    <form id="mainForm" method="post" action=<%=process.runtime.continueURL()%>>
        <div class="container" >
            <br>
            <div class="bg-title"><h2>Acknowledgement</h2></div>

            <p><c:out value="${ackMsg}"/></p>

            <div class="text-right text-center-mobile">
                <%--@elvariable id="confirmRfi" type="java.lang.String"--%>
                <c:if test="${confirmRfi ne null && confirmRfi eq 'Y'}">
                    <a class="btn btn-secondary" href="/bsb-web/eservice/INTERNET/MohBsbRfi?appId=<iais:mask name='rfiAppId' value='${appId}'/>">Return To RFI List</a>
                </c:if>
                    <a id="back" class="btn btn-primary" href="javascript:void(0)">Done</a>
            </div>
        </div>
    </form>
</div>