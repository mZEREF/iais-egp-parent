<%@ page import="static sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT" %>
<%@ taglib prefix="webui" uri="http://www.ecquaria.com/webui" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-internet"/>
<link href="<%=WEB_ROOT%>/css/bsb/bsb-common.css" rel="stylesheet"/>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-common.js"></script>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-defer-renew.js"></script>

<script>
    <% String jsonStr = (String) request.getAttribute("docTypeOpsJson");
    if (jsonStr == null || "".equals(jsonStr)) {
       jsonStr = "undefined";
    }
    %>
    var docTypeOps = <%=jsonStr%>;
</script>

<%@include file="/WEB-INF/jsp/iais/include/showErrorMsg.jsp" %>

<%@include file="dashboard.jsp" %>
<%--@elvariable id="deferRenewDto" type="sg.gov.moh.iais.egp.bsb.dto.renewal.defer.DeferRenewDto"--%>
<form method="post" id="mainForm" enctype="multipart/form-data" action="<%=process.runtime.continueURL()%>"  onsubmit="return validateOtherDocType();">
    <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
    <input type="hidden" name="action_type" value="">
    <input type="hidden" name="action_value" value="">
    <input type="hidden" name="action_additional" value="">
    <input type="hidden" name="print_action_type" value="">

    <div class="main-content">
        <div class="container">
            <div class="row">
                <div class="col-xs-12">
                    <div class="form-horizontal">
                        <br/>
                        <div class="form-group">
                            <lable>
                                <em>A request to defer the renewal must be supported by strong justification and is subject to
                                approval by MOH. Please state the reason for the application below and provide the relevant
                                supporting documents which may include details or further justifications for the
                                request,proposed plans, timeline for planned renewal, etc.</em>
                            </lable>
                            <br/>
                            <br/>
                            <lable>
                                <em>Please note that if your application to defer the renewal of the facility is rejected, the
                                facility registration and all approvals held by the facility shall cease to be valid by the
                                expiry date for the facility registration.</em>
                            </lable>
                            <br/>
                            <br/>
                            <lable>
                                <em>For Certified Facilities, please note that an approval to defer the renewal DOES NOT
                                constitute an extension of the validity of the certification period. All facilities are
                                required to stop all work involving the regulated biological agent and/or toxin by the
                                stipulated expiry date.</em>
                            </lable>
                        </div>
                        <br/>
                        <div class="form-group">
                            <div class="col-sm-5 control-label">
                                <label>Active Facility Number</label>
                            </div>
                            <div class="col-sm-6 col-md-7">
                                <label>${deferRenewDto.facilityNo}</label>
                            </div>
                        </div>
                        <div class="form-group">
                            <div class="col-sm-5 control-label">
                                <label>Facility Name</label>
                            </div>
                            <div class="col-sm-6 col-md-7">
                                <label>${deferRenewDto.facilityName}</label>
                            </div>
                        </div>
                        <div class="form-group">
                            <div class="col-sm-5 control-label">
                                <label>Expected Date of Renewal<span class="mandatory otherQualificationSpan">*</span></label>
                            </div>
                            <div class="col-sm-6 col-md-7">
                                <iais:datePicker id="deferDate" name="deferDate" dateVal="${deferRenewDto.deferDate}"/>
                                <span data-err-ind="deferDate" class="error-msg"></span>
                            </div>
                        </div>
                        <div class="form-group">
                            <div class="col-sm-5 control-label">
                                <label>Reason for deferment<span class="mandatory otherQualificationSpan">*</span></label>
                            </div>
                            <div class="col-sm-6 col-md-7">
                                <textarea name="reason" id="reason" maxlength="1000" cols="100" rows="10">${deferRenewDto.deferReason}</textarea>
                                <span data-err-ind="deferReason" class="error-msg"></span>
                            </div>
                        </div>
                        <br/><br/><br/><br/>
                        <%@ include file="supportingDocuments.jsp" %>
                        <%@ include file="footer.jsp" %>
                    </div>
                </div>
                <div class="modal fade" id="submitDeclareModal" role="dialog" aria-labelledby="myModalLabel">
                    <div class="modal-dialog modal-dialog-centered" role="document">
                        <div class="modal-content">
                            <div class="modal-body">
                                <div class="row">
                                    <div class="col-md-12"><span style="font-size: 2rem">Please check the declaration box</span></div>
                                </div>
                            </div>
                            <div class="modal-footer">
                                <button type="button" class="btn btn-secondary btn-md" data-dismiss="modal">Close</button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</form>