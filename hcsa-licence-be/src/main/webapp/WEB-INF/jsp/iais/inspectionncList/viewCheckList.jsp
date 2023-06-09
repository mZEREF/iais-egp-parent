<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts" %>
<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://www.ecq.com/iais"   prefix="iais"%>
<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
          String webroot=IaisEGPConstant.CSS_ROOT + IaisEGPConstant.BE_CSS_ROOT;
%>
<webui:setLayout name="iais-intranet"/>
<div class="dashboard" style="background-image:url('<%=webroot%>img/Masthead-banner.jpg')">
<form method="post" id="mainForm"  action=<%=process.runtime.continueURL()%>  class="form-horizontal" >
    <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
    <input type="hidden" name="paramController" id="paramController" value="com.ecquaria.cloud.moh.iais.action.FillupChklistDelegator"/>
    <input type="hidden" name="valEntity" id="valEntity" value="com.ecquaria.cloud.moh.iais.dto.CheckListVadlidateDto"/>
    <input type="hidden" name="valProfiles" id="valProfiles" value=""/>
    <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
    <input type="hidden" name="crud_action_type" value="">
    <input type="hidden" name="crud_action_value" value="">
    <input type="hidden" name="crud_action_additional" value="">
    <input type="hidden" name="doSubmitAction" id="doSubmitAction" value="">
    <input type="hidden" name="InspectorProRectificationType" value="">
    <input type="hidden" id="actionValue" name="actionValue" value="">
    <div class="main-content">
        <div class="">
            <div class="row">
                <div class="col-xs-12">
                    <div class="tab-gp dashboard-tab">
                        <div class="tab-content">
                        </div>
                        <div class="tab-pane <c:if test="${serListDto.checkListTab=='chkList'}">active</c:if>" id="tabPayment" role="tabpanel">
                            <ul class="nav nav-tabs hidden-xs hidden-sm" role="tablist">
                                <li class="complete ${(nowComTabIn == null || nowComTabIn== 'General') ? 'active' : ''}" role="presentation"><a href="#General" aria-controls="General" role="tab" data-toggle="tab">General Regulations</a></li>
                                <li class="complete ${(nowComTabIn== 'ServiceInfo') ? 'active' : ''}" role="presentation"><a href="#ServiceInfo" aria-controls="ServiceInfo" role="tab"
                                                                            data-toggle="tab"><c:out value="${serListDto.serviceName}"/></a></li>
                            </ul>

                            <div class="tab-nav-mobile visible-xs visible-sm">
                                <div class="swiper-wrapper" role="tablist">
                                    <div class="swiper-slide"><a href="#General" aria-controls="General" role="tab" data-toggle="tab">General</a></div>
                                    <div class="swiper-slide"><a href="#ServiceInfo" aria-controls="ServiceInfo" role="tab" data-toggle="tab">ServiceInfo</a></div>
                                    <div class="swiper-slide"><a href="#chkInfo" aria-controls="chkInfo" role="tab" data-toggle="tab">chkInfo</a></div>
                                </div>
                                <div class="swiper-button-prev"></div>
                                <div class="swiper-button-next"></div>
                            </div>
                            <span class="error-msg" id="error_fillchkl" name="iaisErrorMsg"></span>
                            <div class="tab-content" id="checkLsitItemArea">
                                <%@include file="common/commonCheckList.jsp" %>
                                <%@include file="common/serviceCheckList.jsp" %>
                            </div>
                            <%@include file="common/submitChecklist.jsp"%>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</form>
</div>
<%@ include file="/WEB-INF/jsp/include/validation.jsp" %>

