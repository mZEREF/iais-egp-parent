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
        <div class="main-content">
            <div class="row">
                <div class="col-lg-12 col-xs-12">
                    <div class="center-content">
                        <div class="intranet-content">
                            <iais:body>
                                <div class="col-xs-12">
                                    <div class="tab-gp dashboard-tab">
                                        <ul class="nav nav-tabs hidden-xs hidden-sm" role="tablist">
                                            <li class="active"
                                                role="presentation">
                                                <a href="#tabApplicationInfo" aria-controls="tabApplicationInfo" role="tab"
                                                   data-toggle="tab">Inspection</a>
                                            </li>
                                            <li
                                                    class="complete"
                                                    role="presentation">
                                                <a href="#tabChecklist" aria-controls="tabChecklist" role="tab"
                                                   data-toggle="tab">Checklist</a>
                                            </li>
                                            <li
                                                    class="complete"
                                                    role="presentation">
                                                <a href="#tabNcRectification" aria-controls="tabNcRectification" role="tab"
                                                   data-toggle="tab">NC Rectification</a></li>
                                            <li
                                                    class="complete"
                                                    role="presentation">
                                                <a href="#tabReport" aria-controls="tabReport" role="tab"
                                                   data-toggle="tab">Report</a>
                                            </li>
                                        </ul>
                                        <div class="tab-nav-mobile visible-xs visible-sm">
                                            <div class="swiper-wrapper" role="tablist">
                                                <div class="swiper-slide"><a href="#tabApplicationInfo"
                                                                             aria-controls="tabApplicationInfo"
                                                                             role="tab" data-toggle="tab">Inspection</a>
                                                </div>
                                                <div class="swiper-slide"><a href="#tabChecklist"
                                                                             aria-controls="tabChecklist"
                                                                             role="tab"
                                                                             data-toggle="tab">Checklist</a></div>
                                                <div class="swiper-slide"><a href="#tabNcRectification"
                                                                             aria-controls="tabNcRectification" role="tab"
                                                                             data-toggle="tab">NC Rectification</a></div>
                                                <div class="swiper-slide"><a href="#tabReport"
                                                                             aria-controls="tabReport"
                                                                             role="tab"
                                                                             data-toggle="tab">Report</a></div>
                                            </div>
                                            <div class="swiper-button-prev"></div>
                                            <div class="swiper-button-next"></div>
                                        </div>

                                        <div class="tab-content row ">
                                            <div class="alert" role="alert">
                                                <h4><span style="color: red">Note</span>: To print the page, please right-click and select "Print".</h4>
                                            </div>

                                            <div class="tab-pane panel-group  active "
                                                 id="tabApplicationInfo" role="tabpanel">
                                                <%@include file="../application/applicationTab.jsp" %>
                                            </div>

                                            <div class="tab-pane " id="tabChecklist" role="tabpanel">
                                                <%@include file="viewCheckList.jsp"%>
                                            </div>
                                            <div class="tab-pane " id="tabNcRectification" role="tabpanel">
                                                <%@include file="ncRectificationTab.jsp"%>
                                            </div>

                                            <div class="tab-pane " id="tabReport" role="tabpanel">
                                                <jsp:include page="/WEB-INF/jsp/iais/report/ao1Report.jsp"/>
                                            </div>
                                        </div>
                                        <div class="tab-content row">
                                            <c:choose>
                                                <c:when test="${not empty licAppMain}">
                                                    <a style="float:left;padding-top: 1.1%;text-decoration:none;" class="back" href="/hcsa-licence-web/eservice/INTRANET/MohLicAppMainOnlineEnquiry/1/preSearch?back=back"><em class="fa fa-angle-left"></em> Back</a>
                                                </c:when>
                                                <c:when test="${not empty appInsStep}">
                                                    <a style="float:left;padding-top: 1.1%;text-decoration:none;" class="back" href="/hcsa-licence-web/eservice/INTRANET/MohApplicationOnlineEnquiry/1/preInsTab?back=back"><em class="fa fa-angle-left"></em> Back</a>
                                                </c:when>
                                                <c:when test="${not empty licInsStep}">
                                                    <a style="float:left;padding-top: 1.1%;text-decoration:none;" class="back" href="/hcsa-licence-web/eservice/INTRANET/MohLicenceOnlineEnquiry/1/preInspectionsSearch?back=back"><em class="fa fa-angle-left"></em> Back</a>
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
