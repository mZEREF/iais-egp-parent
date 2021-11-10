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

<%
    java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("dd-MM-yyyy");
    java.util.Date currentTime = new java.util.Date();
    String currentDate = formatter.format(currentTime);
%>

<form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
    <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
    <input type="hidden" name="action_type" value="">
    <input type="hidden" name="action_value" value="">
    <div class="container">
        <div class="component-gp col-xs-12 col-sm-11 col-md-10 col-lg-8">
            <br/>
            <p>You are <strong>${rfcFlowType}</strong></p>
            <c:if test="${rfcFlowType eq 'NOTIFICATION' or rfcFlowType eq 'AMENDMENT'}">
                <br/>
                <p><strong>Submission successful</strong></p>
                <br/>
                <p>A confirmation email will be sent to lindatan@gmail.com, test@test.com, testing123@gmail.com.

                    Your application/ request has been submitted successfully. MOH Officer will be in contact with you shortly.

                    Transactional details:</p>
                <p><strong>Approval No.</strong>${approveNo}</p>
                <p><strong>Date</strong><%=currentDate%></p>
            </c:if>
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
