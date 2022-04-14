<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-internet"/>

<%-- current page: stage--%>
<input type="hidden" name="ar_page" value="stage"/>
<%--preview/ack--%>
<%@ include file="common/topPiHeader.jsp" %>

<form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
    <input type="hidden" name="crud_action_type">
    <div class="main-content">
        <div class="container center-content">
            <div class="col-xs-12">
                <h3>Please key in patient information.</h3>
                <div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">
                    <input id="isEditHiddenVal" type="hidden" name="isEdit" value="0"/>
                    <%@include file="section/patientDetails.jsp" %>
                    <%@include file="common/topDsAmendment.jsp" %>
                </div>
                <%@include file="common/topPiFooter.jsp" %>
            </div>
        </div>
    </div>
</form>