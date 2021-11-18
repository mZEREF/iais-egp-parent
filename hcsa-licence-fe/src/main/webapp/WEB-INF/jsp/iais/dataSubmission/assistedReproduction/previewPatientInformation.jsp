<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
    String continueURL = "";
    if (process != null && process.runtime != null && process.runtime.getBaseProcessClass() != null) {
        continueURL = process.runtime.continueURL();
    }
%>
<webui:setLayout name="iais-internet"/>

<c:set var="headingSign" value="completed"/>

<%@ include file="common/arHeader.jsp" %>

<script type="text/javascript" src="<%=webroot1%>js/dataSubmission/patientInformation.js"></script>

<form method="post" id="mainForm" action=<%=continueURL%>>
    <div class="main-content">
        <div class="container center-content">
            <div class="col-xs-12">
                <div class="row form-group" style="border-bottom: 1px solid #D1D1D1;">
                    <div class="col-xs-12 col-md-10">
                        <strong style="font-size: 2rem;">Preview & Submit</strong>
                    </div>
                    <div class="col-xs-12 col-md-2 text-right">
                        <p class="print" style="font-size: 16px;">
                            <a onclick="printData()" href="javascript:void(0);"> <em class="fa fa-print"></em>Print</a>
                        </p>
                    </div>
                </div>
                <div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">
                    <%@include file="section/previewPatientDetail.jsp" %>
                    <%@include file="section/previewHusbandDetail.jsp" %>
                    <c:if test="${arSuperDataSubmissionDto.submissionType eq 'DSTY_005'}">
                        <%@include file="section/patientAmendment.jsp" %>
                    </c:if>
                    <c:if test="${arSuperDataSubmissionDto.submissionType ne 'DSTY_005'}">
                        <%@include file="common/arDeclaration.jsp" %>
                    </c:if>
                </div>
                <%@include file="common/arFooter.jsp" %>
            </div>
        </div>
    </div>
</form>
<input type="hidden" name="printflag" id="printflag" value="patient">
