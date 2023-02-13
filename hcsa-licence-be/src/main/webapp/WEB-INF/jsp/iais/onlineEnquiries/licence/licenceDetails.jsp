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
                                                <a href="#tabLicenceInfo" aria-controls="tabLicenceInfo" role="tab"
                                                   data-toggle="tab">Licence</a>
                                            </li>
                                            <li id="applicationsTab"
                                                class="<c:if test="${preActive == '1'}">active</c:if><c:if test="${ preActive != '1' }">complete</c:if>"
                                                role="presentation">
                                                <a href="#tabApplications" aria-controls="tabApplications" role="tab"
                                                   data-toggle="tab">Applications</a>
                                            </li>
                                            <li id="adHocRfiTab"
                                                class="<c:if test="${preActive == '2'}">active</c:if><c:if test="${ preActive != '2' }">complete</c:if>"
                                                role="presentation">
                                                <a href="#tabAdHocRfi" aria-controls="tabAdHocRfi" role="tab"
                                                   data-toggle="tab">Ad-Hoc
                                                    RFI</a></li>
                                            <li id="inspectionsTab"
                                                class="<c:if test="${preActive == '3'}">active</c:if><c:if test="${ preActive != '3' }">complete</c:if>"
                                                role="presentation">
                                                <a href="#tabInspections" aria-controls="tabInspections" role="tab"
                                                   data-toggle="tab">Inspections</a>
                                            </li>
                                        </ul>
                                        <div class="tab-nav-mobile visible-xs visible-sm">
                                            <div class="swiper-wrapper" role="tablist">
                                                <div class="swiper-slide"><a href="#tabLicenceInfo"
                                                                             aria-controls="tabLicenceInfo"
                                                                             role="tab" data-toggle="tab">Licence</a>
                                                </div>
                                                <div class="swiper-slide"><a href="#tabApplications"
                                                                             aria-controls="tabApplications"
                                                                             role="tab"
                                                                             data-toggle="tab">Applications</a></div>
                                                <div class="swiper-slide"><a href="#tabAdHocRfi"
                                                                             aria-controls="tabAdHocRfi" role="tab"
                                                                             data-toggle="tab">Ad-Hoc RFI</a></div>
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
                                                 id="tabLicenceInfo" role="tabpanel">
                                                <%@include file="licenceTab.jsp" %>
                                            </div>

                                            <c:if test="${preActive == '1'}">
                                                <div class="tab-pane active " id="tabApplications" role="tabpanel">
                                                    <%@include file="applicationsTab.jsp" %>
                                                </div>
                                            </c:if>
                                            <c:if test="${preActive == '2'}">
                                                <div class="tab-pane active" id="tabAdHocRfi" role="tabpanel">
                                                    <%@include file="adHocRfiTab.jsp" %>
                                                </div>
                                            </c:if>
                                            <c:if test="${preActive == '3'}">
                                                <div class="tab-pane active" id="tabInspections" role="tabpanel">
                                                    <%@include file="inspectionsTab.jsp" %>
                                                </div>
                                            </c:if>


                                        </div>
                                        <div class="tab-content row">
                                            <c:if test="${not empty licAppMain}">
                                                <a style="float:left;padding-top: 1.1%;text-decoration:none;" class="back" href="/hcsa-licence-web/eservice/INTRANET/MohLicAppMainOnlineEnquiry/1/preSearch?back=back"><em class="fa fa-angle-left"></em> Back</a>
                                            </c:if>
                                            <c:if test="${empty licAppMain}">
                                                <a href="#"
                                                   onclick="javascript:$('#back').val('back');$('#mainForm').submit();"><em
                                                        class="fa fa-angle-left"> </em> Back</a>
                                            </c:if>

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
<script type="text/javascript">
    $(document).ready(function () {
        $('#applicationsTab').click(function () {
            showWaiting();
            $('#back').val('back');
            $("[name='crud_action_type']").val('searchApp');
            $('#mainForm').submit();
        });
        $('#adHocRfiTab').click(function () {
            showWaiting();
            $('#back').val('back');
            $("[name='crud_action_type']").val('searchRfi');
            $('#mainForm').submit();
        });
        $('#inspectionsTab').click(function () {
            showWaiting();
            $('#back').val('back');
            $("[name='crud_action_type']").val('searchIns');
            $('#mainForm').submit();
        });

    });
</script>