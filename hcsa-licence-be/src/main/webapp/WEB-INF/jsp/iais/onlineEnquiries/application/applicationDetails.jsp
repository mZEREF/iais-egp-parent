<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.AppConsts" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.utils.MaskUtil" %>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<%
    String webrootCom = IaisEGPConstant.CSS_ROOT + IaisEGPConstant.COMMON_CSS_ROOT;
%>

<style>
    .form-horizontal p {
        line-height: 23px;
    }
</style>
<webui:setLayout name="iais-intranet"/>
<div class="dashboard">
    <form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
        <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
        <input type="hidden" name="back" id="back"/>
        <input type="hidden" name="preActiveHidden" id="preActiveHidden" value="${preActive}"/>
        <div class="main-content">
            <div class="row">
                <div class="col-lg-12 col-xs-12">
                    <div class="center-content">
                        <div class="intranet-content">
                            <iais:body>
                                <div class="col-xs-12">
                                    <div class="tab-gp dashboard-tab">
                                        <ul class="nav nav-tabs hidden-xs hidden-sm" role="tablist">
                                            <li class="<c:if test="${empty preActive }">active</c:if><c:if test="${not empty preActive }">complete</c:if>"
                                                role="presentation">
                                                <a href="#tabApplicationInfo" aria-controls="tabApplicationInfo" role="tab"
                                                   data-toggle="tab">Application</a>
                                            </li>
                                            <li
                                                class="complete"
                                                role="presentation">
                                                <a href="#tabDocuments" aria-controls="tabDocuments" role="tab"
                                                   data-toggle="tab">Documents</a>
                                            </li>
                                            <li
                                                class="complete"
                                                role="presentation">
                                                <a href="#tabProcessingHistory" aria-controls="tabProcessingHistory" role="tab"
                                                   data-toggle="tab">Processing History</a></li>
                                            <li
                                                class="<c:if test="${preActive == '3'}">active</c:if><c:if test="${ preActive != '3' }">complete</c:if>"
                                                role="presentation">
                                                <a href="#tabInspections" aria-controls="tabInspections" role="tab"
                                                   data-toggle="tab">Inspections</a>
                                            </li>
                                        </ul>
                                        <div class="tab-nav-mobile visible-xs visible-sm">
                                            <div class="swiper-wrapper" role="tablist">
                                                <div class="swiper-slide"><a href="#tabApplicationInfo"
                                                                             aria-controls="tabApplicationInfo"
                                                                             role="tab" data-toggle="tab">Application</a>
                                                </div>
                                                <div class="swiper-slide"><a href="#tabDocuments"
                                                                             aria-controls="tabDocuments"
                                                                             role="tab"
                                                                             data-toggle="tab">Documents</a></div>
                                                <div class="swiper-slide"><a href="#tabProcessingHistory"
                                                                             aria-controls="tabProcessingHistory" role="tab"
                                                                             data-toggle="tab">Processing History</a></div>
                                                <div class="swiper-slide"><a href="#tabInspections"
                                                                             aria-controls="tabInspections"
                                                                             role="tab"
                                                                             data-toggle="tab">Inspections</a></div>
                                            </div>
                                            <div class="swiper-button-prev"></div>
                                            <div class="swiper-button-next"></div>
                                        </div>

                                        <div class="tab-content row ">
                                            <div class="tab-pane  panel-group <c:if test="${empty preActive }">active</c:if> "
                                                 id="tabApplicationInfo" role="tabpanel">
                                                <%@include file="applicationTab.jsp" %>
                                                <c:if test="${applicationViewDto.applicationDto.applicationType == 'APTY002'||applicationViewDto.applicationDto.applicationType == 'APTY005'||applicationViewDto.applicationDto.applicationType == 'APTY004'}">
                                                    <iais:action style="text-align:right;">
                                                        <a class="btn btn-primary" href="#"
                                                           onclick="jumpPayPage('${MaskUtil.maskValue('payAppNo', applicationViewDto.applicationDto.applicationNo)}')"
                                                        >Payment Details</a>
                                                    </iais:action>
                                                </c:if>
                                            </div>

                                            <div class="tab-pane " id="tabDocuments" role="tabpanel">
                                                <%@ include file="../../inspectionncList/tabDocuments.jsp" %>
                                            </div>
                                            <div class="tab-pane" id="tabProcessingHistory" role="tabpanel">
                                                <%@include file="/WEB-INF/jsp/iais/inspectionncList/processHistory.jsp"%>
                                            </div>
                                            
                                            <div class="tab-pane <c:if test="${preActive == '3'}">active</c:if>" id="tabInspections" role="tabpanel">
                                                <%@include file="../licence/inspectionsTab.jsp" %>
                                            </div>


                                        </div>
                                        <div class="tab-content row">
                                            <c:choose>
                                                <c:when test="${not empty licAppMain}">
                                                    <a style="float:left;padding-top: 1.1%;text-decoration:none;" class="back" href="/hcsa-licence-web/eservice/INTRANET/MohLicAppMainOnlineEnquiry/1/preSearch?back=back"><em class="fa fa-angle-left"></em> Back</a>
                                                </c:when>
                                                <c:when test="${not empty licAppTab}">
                                                    <a style="float:left;padding-top: 1.1%;text-decoration:none;" class="back" href="/hcsa-licence-web/eservice/INTRANET/MohLicenceOnlineEnquiry/1/preApplicationsSearch?back=back"><em class="fa fa-angle-left"></em> Back</a>
                                                </c:when>
                                                <c:when test="${not empty payBack}">
                                                    <a style="float:left;padding-top: 1.1%;text-decoration:none;" class="back" href="/hcsa-licence-web/eservice/INTRANET/MohPaymentOnlineEnquiry/1/preSearch?back=back"><em class="fa fa-angle-left"></em> Back</a>
                                                </c:when>
                                                <c:otherwise>
                                                    <a href="#"
                                                       onclick="javascript:$('#back').val('back');$('#mainForm').submit();"><em
                                                            class="fa fa-angle-left"> </em> Back</a>
                                                </c:otherwise>
                                            </c:choose>
                                        </div>
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
