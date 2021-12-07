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
<%@ include file="common/arHeader.jsp" %>

<form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
    <div class="main-content">
        <div class="container center-content">
            <div class="col-xs-12">
                <div class="row form-group" style="border-bottom: 1px solid #D1D1D1;">
                    <div class="col-xs-12 col-md-10">
                        <strong style="font-size: 2rem;">Please key in the cycle information below.</strong>
                    </div>
                    <div class="col-xs-12 col-md-2 text-right">
                        <p class="print" style="font-size: 16px;">
                            <a onclick="printData()" href="javascript:void(0);"> <em class="fa fa-print"></em>Print</a>
                        </p>
                    </div>
                </div>
                <div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">
                    <%@include file="section/iuiTreatmentSubsidiesSection.jsp" %>
                </div>
                <%@include file="common/arFooter.jsp" %>
            </div>
        </div>
    </div>
</form>
