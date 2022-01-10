<%@ page import="static sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT" %>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
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
                <a id="back" class="btn btn-primary" href="javascript:void(0)">Done</a>
            </div>
        </div>
    </form>
</div>