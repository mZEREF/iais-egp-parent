<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib uri="http://www.ecquaria.com/menu" prefix="menu" %>
<%@ taglib uri="ecquaria/sop/egov-smc" prefix="egov-smc" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>
<%@ page import="static sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT" %>
<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-internet"/>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-audit.js"></script>
<%@include file="dashboard.jsp" %>
<form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
    <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
    <input type="hidden" name="action_type" value="">
    <input type="hidden" name="action_value" value="">
    <input type="hidden" name="action_additional" value="">
    <div class="container">
        <div class="row">
            <div class="col-lg-12 col-xs-12">
                <div class="internet-content">
                    <iais:body>
                        <div class="col-xs-12">
                            <div class="tab-gp dashboard-tab">
                                <ul class="nav nav-tabs hidden-xs hidden-sm" role="tablist">
                                    <li class="active" id="info" role="presentation">
                                        <a href="#tabInfo"
                                           id="infoa"
                                           aria-controls="tabInfo"
                                           role="tab"
                                           data-toggle="tab">Info</a></li>
                                    <li class="complete" id="document" role="presentation">
                                        <a href="#tabDocuments"
                                           id="documenta"
                                           aria-controls="tabDocuments" role="tab"
                                           data-toggle="tab">Documents</a></li>
                                    <li class="incomplete" id="process" role="presentation">
                                        <a href="#tabProcessing"
                                           id="processa"
                                           aria-controls="tabProcessing" role="tab"
                                           data-toggle="tab">Processing</a></li>
                                </ul>
                                <div class="tab-nav-mobile visible-xs visible-sm">
                                    <div class="swiper-wrapper" role="tablist">
                                        <div class="swiper-slide"><a href="#tabInfo" aria-controls="tabInfo"
                                                                     role="tab"
                                                                     data-toggle="tab">Info</a></div>
                                        <div class="swiper-slide"><a href="#tabDocuments" id="doDocument"
                                                                     aria-controls="tabDocuments"
                                                                     role="tab" data-toggle="tab">Documents</a>
                                        </div>
                                        <div class="swiper-slide"><a href="#tabProcessing" id="doProcess"
                                                                     aria-controls="tabProcessing"
                                                                     role="tab" data-toggle="tab">Processing</a></div>
                                    </div>
                                    <div class="swiper-button-prev"></div>
                                    <div class="swiper-button-next"></div>
                                </div>
                                <div class="tab-content">
                                    <div class="tab-pane active" id="tabInfo" role="tabpanel">
                                        <%@include file="facilityInfo.jsp" %>
                                    </div>
                                    <div class="tab-pane" id="tabDocuments" role="tabpanel">
                                        <%@include file="tabDocuments.jsp" %>
                                    </div>
                                    <div class="tab-pane" id="tabProcessing" role="tabpanel">
                                        <span id="error_document" name="iaisErrorMsg" class="error-msg"></span>
                                        <br/><br/>
                                        <div class="alert alert-info" role="alert">
                                            <strong><h4>Submit Self-Audit Report</h4></strong>
                                        </div>
                                        <div class="row">
                                            <div class="col-xs-12">
                                                <div class="table-gp">
                                                    <iais:section title="">
                                                        <input name="auditId" id="auditId" value="<iais:mask name="auditId" value="${facilityAudit.id}"></iais:mask>" hidden>
                                                        <iais:row>
                                                            <iais:field value="Audit type" required="false" width="12"/>
                                                            <iais:value width="10">
                                                                <p><c:out value="${selfAudit.auditType}"/></p>
                                                            </iais:value>
                                                        </iais:row>
                                                        <iais:row>
                                                            <iais:field value="Audit Date" required="false"/>
                                                            <iais:value width="10">
                                                                <p><fmt:formatDate value='${selfAudit.auditDate}' pattern='dd/MM/yyyy'/></p>
                                                            </iais:value>
                                                        </iais:row>
                                                        <iais:row>
                                                            <iais:field value="Scenario Category" required="true"/>
                                                            <iais:value width="10">
                                                                <p><iais:code code="${selfAudit.scenarioCategory}"/></p>
                                                            </iais:value>
                                                        </iais:row>
                                                    </iais:section>
                                                    <a style="float:left;padding-top: 1.1%;" class="back" id="back" href="#"><em class="fa fa-angle-left"></em>Back</a>
                                                    <div align="right">
                                                        <button name="submitBtn" id="submitBtn" type="button" class="btn btn-primary">Submit</button>
                                                    </div>
                                                    <div>&nbsp;</div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </iais:body>
                </div>
            </div>
        </div>
    </div>
</form>