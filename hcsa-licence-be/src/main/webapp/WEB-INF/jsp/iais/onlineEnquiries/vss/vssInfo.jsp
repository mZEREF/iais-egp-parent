<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>


<webui:setLayout name="iais-intranet"/>
<div class="dashboard" >
    <form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
        <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
        <input type="hidden" name="back" id="back"/>
        <div class="main-content">
            <div class="row">
                <div class="col-lg-12 col-xs-12">
                    <div class="center-content">
                        <iais:body >
                            <div class="tab-gp dashboard-tab">
                                <div class="tab-content row">
                                    <div class="tab-pane active col-lg-10 col-xs-10 panel-group" style="left: 8%;">
                                        <%@include file="previewTreatmentDetails.jsp" %>
                                        <%@include file="previewParticularsConsentOrCourtOrder.jsp" %>
                                        <%@include file="previewParticularsSterilizationSection.jsp" %>
                                    </div>
                                </div>
                                <div class="tab-content row">
                                    <a href="#" onclick="javascript:$('#back').val('back');$('#mainForm').submit();" ><em class="fa fa-angle-left"> </em> Back</a>
                                </div>
                            </div>
                        </iais:body>
                    </div>
                </div>
            </div>
        </div>
    </form>
</div>

