<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts" %>
<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.ecq.com/iais"   prefix="iais"%>
<%@ page import="static sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT" %>

<%
//handle to the Engine APIs
sop.webflow.rt.api.BaseProcessClass process =
(sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
String webroot=IaisEGPConstant.CSS_ROOT + IaisEGPConstant.BE_CSS_ROOT;
%>
<webui:setLayout name="iais-intranet"/>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-inspection.js"></script>
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
    <input id="nowTabIn" name="nowTabIn" value="${nowTabIn == null ? 'Combined' : nowTabIn}" type="hidden">
    <input id="nowComTabIn" name="nowComTabIn" value="${nowComTabIn == null ? 'General' : nowComTabIn}" type="hidden">
    <div class="main-content">
        <div class="">
            <div class="row">
                <div class="col-xs-12">
                    <div class="tab-gp dashboard-tab">
                        <div class="tab-content">
                        </div>
                        <div class="tab-pane " id="tabPayment" role="tabpanel">
<%--                            <ul class="nav nav-tabs hidden-xs hidden-sm" role="tablist">--%>
<%--                                <li class="complete active" role="presentation" onclick="javascript:doChangeTab('ServiceInfo')"><a href="#ServiceInfo" aria-controls="ServiceInfo" role="tab"--%>
<%--                                                                            data-toggle="tab"><c:out value="BSB Regulations"/></a></li>--%>
<%--                            </ul>--%>

<%--                            <div class="tab-nav-mobile visible-xs visible-sm">--%>
<%--                                <div class="swiper-wrapper" role="tablist">--%>
<%--                                    <div class="swiper-slide"><a href="#ServiceInfo" aria-controls="ServiceInfo" role="tab" data-toggle="tab">ServiceInfo</a></div>--%>
<%--                                    <div class="swiper-slide"><a href="#chkInfo" aria-controls="chkInfo" role="tab" data-toggle="tab">chkInfo</a></div>--%>
<%--                                </div>--%>
<%--                                <div class="swiper-button-prev"></div>--%>
<%--                                <div class="swiper-button-next"></div>--%>
<%--                            </div>--%>
<%--                            <span class="error-msg" id="error_fillchkl" name="iaisErrorMsg"></span>--%>
                            <h2>BATA Inspection</h2>
                            <div class="tab-content">
                                <div class="tab-pane ${(nowComTabIn == null || nowComTabIn== 'General') ? 'active' : ''}" id="General" role="tabpanel">
                                    <span><strong>do/total:</strong></span>&nbsp;<c:out value="${serListDto.generalDo}"/>/<c:out value="${serListDto.generalTotal}"/><br>
                                    <span><strong>No of Non-Compliance:</strong></span>&nbsp;<c:out value="${serListDto.generalNc}"/><br>
<%--                                    <span><strong>General</strong></span>--%>
                                    <%@ include file="TabsForMoreIns.jsp" %>
                                    <%@ include file="common/combined.jsp" %>
                                    <%@ include file="inspChecklist.jsp" %>
                                  </div>

                            </div>
                            <%@ include file="common/submitMoreInsChecklist.jsp" %>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <%@ include file="/WEB-INF/jsp/include/validation.jsp" %>
</form>
</div>
<%--@elvariable id="afterSaveDraft" type="java.lang.Boolean"--%>
<input type="hidden" id="afterSaveDraft" name="afterSaveDraft" value="${afterSaveDraft}" readonly disabled/>
<div class="modal fade" id="afterSaveDraftMd" role="dialog" aria-labelledby="myModalLabel">
    <div class="modal-dialog modal-dialog-centered" role="document">
        <div class="modal-content">
            <div class="modal-body" >
                <div class="row">
                    <div class="col-md-12">
                        <span style="font-size: 2rem;" id="prsMsg">
                            <iais:message key="BISINSACK001" escape="false" />
                        </span>
                    </div>
                </div>
            </div>
            <div class="row " style="margin-top: 5%;margin-bottom: 5%">
                <button type="button" style="margin-left: 50%" class="next btn btn-primary col-md-6" data-dismiss="modal" onclick="cancelSaveDraftModule()">CLOSE</button>
            </div>
        </div>
    </div>
</div>
