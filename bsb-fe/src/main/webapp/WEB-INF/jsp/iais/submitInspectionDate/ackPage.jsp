<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib uri="http://www.ecquaria.com/menu" prefix="menu" %>
<%@ taglib uri="ecquaria/sop/egov-smc" prefix="egov-smc" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="static sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT" %>
<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-internet"/>
<%@include file="dashboard.jsp" %>
<form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
    <div class="container">
        <div class="row">
            <div class="col-lg-12 col-xs-12">
                <div class="internet-content">
                    <div class="bg-title">
                        <%--@elvariable id="ackMsg" type="java.lang.String"--%>
                        <h2>Acknowledgement</h2>
                        <p>${ackMsg}</p>
                    </div>
                </div>
                <br><br>
            </div>
        </div>
    </div>

    <div class="row">
        <div class="container">
            <%--@elvariable id="confirmRfi" type="java.lang.String"--%>
            <c:choose>
                <c:when test="${confirmRfi ne null && confirmRfi eq 'Y'}">
                    <div class="col-xs-12 col-md-6 text-left">
                            <%--@elvariable id="backUrl" type="java.lang.String"--%>
                        <a class="back" href="/bsb-web/eservice/INTERNET/MohBSBInboxMsg"><em class="fa fa-angle-left"></em> Previous</a>
                    </div>
                    <div class="form-group">
                        <div class="col-xs-12 col-md-6 text-right">
                            <a class="btn btn-secondary" href="/bsb-web/eservice/INTERNET/MohBsbRfi?appId=<iais:mask name='rfiAppId' value='${appId}'/>">Return To RFI List</a>
                            <a href="/bsb-web/eservice/INTERNET/MohBSBInboxMsg" type="button" class="btn btn-primary save">DONE</a>
                        </div>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="col-xs-12 col-md-6 text-left">
                            <%--@elvariable id="backUrl" type="java.lang.String"--%>
                        <a class="back" href="${backUrl}"><em class="fa fa-angle-left"></em> Previous</a>
                    </div>
                    <div class="form-group">
                        <div class="col-xs-12 col-md-6 text-right">
                            <a href="${backUrl}" type="button" class="btn btn-primary save">DONE</a>
                        </div>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</form>