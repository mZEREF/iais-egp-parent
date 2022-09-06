<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="com.ecquaria.cloud.moh.iais.helper.MessageUtil" %>
<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-internet"/>

<%@ include file="common/arHeader.jsp" %>

<c:set var="headingSign" value="completed"/>
<form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
    <div class="main-content">
        <div class="container center-content">
            <div class="col-xs-12">
                <div class="row form-group" style="border-bottom: 1px solid #D1D1D1;">
                    <%@include file="common/headStepNavTab.jsp" %>
                    <div class="col-xs-12 col-md-10">
                        <strong style="font-size: 2rem;">Preview & Submit</strong>
                    </div>
                    <div class="col-xs-12 col-md-2 text-right">
                        <p class="print" style="font-size: 16px;">
                            <label class="fa fa-print" style="color: #147aab;" onclick="printData()"></label> <a onclick="printData()" href="javascript:void(0);">Print</a>
                        </p>
                    </div>
                </div>
                <div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">
                    <c:if test="${arSuperDataSubmissionDto.selectionDto.cycle == 'DSCL_010'}">
                        <%@include file="section/previewEfoDetail.jsp" %>
                    </c:if>
                    <c:if test="${arSuperDataSubmissionDto.selectionDto.cycle == 'DSCL_016'}">
                        <%@include file="section/previewSfoDetail.jsp" %>
                    </c:if>
                    <%@include file="section/previewDisposalDetail.jsp" %>
                    <%@include file="common/previewDsAmendment.jsp" %>
                    <%@include file="common/arDeclaration.jsp" %>
                </div>
                <%@include file="common/arFooter.jsp" %>
            </div>
        </div>
    </div>
</form>
