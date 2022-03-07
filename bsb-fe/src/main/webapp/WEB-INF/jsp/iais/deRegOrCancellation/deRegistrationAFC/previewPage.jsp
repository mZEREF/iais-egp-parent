<%@ page import="static sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT"%>
<%@ taglib prefix="webui" uri="http://www.ecquaria.com/webui" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%@ taglib prefix="iais-bsb" uri="http://www.ecq.com/iais-bsb" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-internet"/>

<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-common.js"></script>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-cancel-de-reg-common.js"></script>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-de-registration-afc.js"></script>

<%@include file="/WEB-INF/jsp/iais/include/showErrorMsg.jsp"%>
<%@include file="../dashboard.jsp"%>
<div class="main-content">
    <div class="container">
        <div class="row">
            <div class="col-xs-12">
                <div class="instruction-content center-content">
                    <form method="post" id="mainForm" action="<%=process.runtime.continueURL()%>">
                        <div class="row form-horizontal">
                            <input type="hidden" name="action_type" value="">
                            <input type="hidden" name="action_value" value="">
                            <div class="col-lg-12 col-xs-12 cesform-box">
                                <div class="row">
                                    <div class="col-lg-12 col-xs-12">
                                        <div class="table-gp tablebox">
                                            <div class="form-group ">
                                                <label class="col-sm-5 control-label">Organization Name</label>
                                                <div class="col-sm-6 col-md-7"><c:out value="${deRegistrationAFCDto.organisationName}"/></div>
                                            </div>
                                            <div class="form-group ">
                                                <label class="col-sm-5 control-label">Organization Address</label>
                                                <div class="col-sm-6 col-md-7"><c:out value="${deRegistrationAFCDto.organisationAddress}"/></div>
                                            </div>
                                            <div class="form-group ">
                                                <label class="col-sm-5 control-label">Reasons</label>
                                                <div class="col-sm-6 col-md-7"><iais:code code="${deRegistrationAFCDto.reasons}"/></div>
                                            </div>
                                            <div class="form-group ">
                                                <label class="col-sm-5 control-label">Remarks</label>
                                                <div class="col-sm-6 col-md-7"><c:out value="${deRegistrationAFCDto.remarks}"/></div>
                                            </div>
                                            <%@include file="../previewDocuments.jsp" %>
                                            <div class="panel-body">
                                                <div class="row">
                                                    <br>
                                                    <div class="form-group " style="z-index: 10">
                                                        <div class="col-xs-1" style="padding: 30px 0 20px 30px;">
                                                            <input type="checkbox" name="declarationReadOnly" value="Y" <c:if test="${deRegistrationAFCDto.declaration eq 'Y'}">checked="checked"</c:if> />
                                                        </div>
                                                        <div class="col-xs-10 control-label">
                                                            <span>I, hereby declare, that all the information provided in this application is true and accurate. Upon submission of this deregistration, the certifying team will no longer offer services as an MOH-Approved Facility Certifier.</span>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <%@ include file="../previewInnerFooter.jsp" %>
                        <%@ include file="/WEB-INF/jsp/iais/include/jumpAfterDraft.jsp" %>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>