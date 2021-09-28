<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-internet"/>

<%@include file="dashboard.jsp"%>

<form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
    <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
    <input type="hidden" name="action_type" value="">
    <input type="hidden" name="action_value" value="">
    <div class="container">
        <div class="row col-xs-12 col-sm-12">
            <div class="dashboard-page-title" style="border-bottom: 1px solid black;">
                <h1>New Application</h1>
            </div>
        </div>
        <div class="component-gp col-xs-12 col-sm-11 col-md-10 col-lg-8">
            <br/>
            <p><strong>Submission successful</strong></p>
            <br/>
            <%--@elvariable id="batList" type="java.util.List<sg.gov.moh.iais.egp.bsb.dto.register.facility.BiologicalAgentToxinDto>"--%>
            <c:forEach var="bat" items="${batList}">
                <p><strong><iais:code code="${bat.name}"/>:
                <c:forEach var="info" items="${bat.batInfos}" varStatus="infoStatus">
                    <c:if test="${infoStatus.index > 0}">, </c:if><iais:code code="${info.schedule}"/>
                </c:forEach>
                </strong></p>
            </c:forEach>
            <br/>
            <p>We will notify you if any changes are required.</p>
            <br/>
            <div class="row">
                <div class="col-xs-12 col-md-2"></div>
                <div class="col-xs-12 col-md-10">
                    <div class="text-right text-center-mobile">
                        <a class="btn btn-secondary" href="javascript:void(0);">PRINT</a>
                        <a class="btn btn-secondary" href="/bsb-fe/eservice/INTERNET/MohBSBInboxMsg">HOME</a>
                        <a class="btn btn-secondary" href="javascript:void(0);">INSPECTION PREFERRED DATE</a>
                    </div>
                </div>
            </div>

        </div>
    </div>
</form>
