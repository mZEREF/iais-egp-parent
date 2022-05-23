<%@ page import="static sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT"%>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="iais-bsb" uri="http://www.ecq.com/iais-bsb" %>
<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-internet"/>

<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-common.js"></script>


<%@include file="dashboard.jsp"%>

<form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
    <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
    <input type="hidden" name="action_type" value="">
    <input type="hidden" name="action_value" value="">
    <div class="container">
        <div class="component-gp col-xs-12 col-sm-11 col-md-10 col-lg-8">
            <br/>
            <p style="font-size: 20px"><strong>Your submission is successful.</strong></p>
            <br/>
            <div class="row" style="width: 700px">
                <div class="col-md-5"><iais:code code="${processType}"/> -</div>
                <div class="col-md-7">
                    <c:forEach var="item" items="${displayList}">
                        <div class="row">
                            <c:choose>
                                <c:when test="${processType eq 'PROTYPE012'}">
                                    <iais:code code="${item}"/>
                                </c:when>
                                <c:otherwise>
                                    <iais-bsb:bat-code code="${item}"/>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </c:forEach>
                </div>
            </div>

            <p>We will notify you of the status of the application or if additional information is required.</p>
            <br/>
            <div class="col-xs-12" style="padding: 0 0 10px 0">
                <div class="col-xs-4" style="padding: 0"><strong>Application Number</strong></div>
                <div class="col-xs-4" style="padding: 0"><strong>Date &amp; Time</strong></div>
            </div>
            <div class="col-xs-12" style="padding: 0 0 10px 0; margin-bottom: 10px; border-bottom: 1px solid black">
                <div class="col-xs-4" style="padding: 0">${appNo}</div>
                <div class="col-xs-4" style="padding: 0"><fmt:formatDate value="${appDt}" pattern="dd/MM/yyyy HH:mm:ss"/></div>
            </div>
            <br/>
            <div class="row">
                <div class="col-xs-12 col-md-2"></div>
                <div class="col-xs-12 col-md-10">
                    <div class="text-right text-center-mobile">
                        <a class="btn btn-secondary" href="javascript:void(0);" onclick="printPage();">PRINT</a>
                        <a class="btn btn-secondary" href="/bsb-web/eservice/INTERNET/MohBSBInboxMsg">HOME</a>
                    </div>
                </div>
            </div>

        </div>
    </div>
</form>
