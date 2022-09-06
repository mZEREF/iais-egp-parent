<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ page import="com.ecquaria.cloud.moh.iais.helper.MessageUtil" %>
<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-internet"/>

<%@ include file="common/arHeader.jsp" %>

<form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
    <div class="main-content">
        <div class="container center-content">
            <div class="col-xs-12">
                <%@include file="common/headStepNavTab.jsp" %>
                <h3>Please key in the cycle information below.</h3>
                <div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">
                    <c:if test="${arSuperDataSubmissionDto.selectionDto.cycle == 'DSCL_010'}">
                        <%@include file="section/efoDetailSection.jsp" %>
                    </c:if>
                    <c:if test="${arSuperDataSubmissionDto.selectionDto.cycle == 'DSCL_016'}">
                        <%@include file="section/sfoDetailSection.jsp" %>
                    </c:if>
                    <%@include file="section/disposalStageDetailSection.jsp" %>
                    <%@include file="common/dsAmendment.jsp" %>
                </div>
                <%@include file="common/arFooter.jsp" %>
            </div>
        </div>
    </div>
</form>
