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
                                                <a href="#tabLicenseeInfo" aria-controls="tabLicenseeInfo" role="tab"
                                                   data-toggle="tab">Licensee</a>
                                            </li>
                                            <li id="LicencesTab"
                                                class="<c:if test="${preActive == '1'}">active</c:if><c:if test="${ preActive != '1' }">complete</c:if>"
                                                role="presentation">
                                                <a href="#tabLicences" aria-controls="tabLicences" role="tab"
                                                   data-toggle="tab">Licences</a>
                                            </li>
                                        </ul>
                                        <div class="tab-nav-mobile visible-xs visible-sm">
                                            <div class="swiper-wrapper" role="tablist">
                                                <div class="swiper-slide"><a href="#tabLicenseeInfo"
                                                                             aria-controls="tabLicenseeInfo"
                                                                             role="tab" data-toggle="tab">Licensee</a>
                                                </div>
                                                <div class="swiper-slide"><a href="#tabLicences"
                                                                             aria-controls="tabLicences"
                                                                             role="tab"
                                                                             data-toggle="tab">Licences</a></div>

                                            </div>
                                            <div class="swiper-button-prev"></div>
                                            <div class="swiper-button-next"></div>
                                        </div>

                                        <div class="tab-content row ">
                                            <div class="tab-pane  panel-group <c:if test="${empty preActive }">active</c:if> "
                                                 id="tabLicenseeInfo" role="tabpanel">
                                                <%@include file="licenseeTab.jsp" %>
                                            </div>

                                            <div class="tab-pane <c:if test="${preActive == '1' }">active</c:if>" id="tabLicences" role="tabpanel">
                                                <%@include file="licenceTab.jsp" %>
                                            </div>


                                        </div>
                                        <div class="tab-content row">
                                            <a href="#"
                                               onclick="javascript:$('#back').val('back');$('#mainForm').submit();"><em
                                                    class="fa fa-angle-left"> </em> Back</a>
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
