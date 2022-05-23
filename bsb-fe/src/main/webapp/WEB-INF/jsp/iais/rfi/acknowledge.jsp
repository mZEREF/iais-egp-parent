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
        <div class="component-gp col-xs-12 col-sm-11 col-md-10 col-lg-8">
            <br/>
            <p style="font-size: 20px"><strong>Your submission is successful.</strong></p>
            <br/>
            <p>You are apply for:</p>
            <%--@elvariable id="applicationRfiIndicatorDtoList" type="java.util.List<sg.gov.moh.iais.egp.bsb.dto.rfi.ApplicationRfiIndicatorDto>"--%>
            <c:forEach var="dto" items="${applicationRfiIndicatorDtoList}" varStatus="status">
                <p>- <iais:code code="${dto.moduleName}"/></p>
            </c:forEach>
            <br/>
            <div class="row">
                <div class="col-xs-12 col-md-2"></div>
                <div class="col-xs-12 col-md-10">
                    <div class="text-right text-center-mobile">
                        <a class="btn btn-secondary" href="/bsb-web/eservice/INTERNET/MohBSBInboxMsg">HOME</a>
                    </div>
                </div>
            </div>

        </div>
    </div>
</form>
