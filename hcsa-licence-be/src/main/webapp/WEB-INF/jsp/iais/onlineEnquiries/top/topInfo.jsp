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
                        <div class="intranet-content">
                            <iais:body >
                                <div class="tab-gp col-xs-10" style="left: 8%;">
                                    <%@include file="previewPatientDetails.jsp" %>
                                    <%@include file="previewFamilyPlanning.jsp" %>
                                    <%@include file="previewPreTermination.jsp" %>
                                    <%@include file="previewPresentTermination.jsp" %>
                                    <%@include file="previewPostTermination.jsp" %>
                                    <div class="tab-content row">
                                        <a href="#" onclick="javascript:$('#back').val('back');$('#mainForm').submit();" ><em class="fa fa-angle-left"> </em> Back</a>
                                    </div>
                                </div>
                            </iais:body>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </form>
</div>

