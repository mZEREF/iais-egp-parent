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
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-cancellation-approval.js"></script>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-common-file.js"></script>

<%@include file="/WEB-INF/jsp/iais/include/showErrorMsg.jsp"%>
<%@include file="../dashboard.jsp"%>
<div class="main-content">
    <div class="container">
        <div class="row">
            <div class="col-xs-12">
                <div class="instruction-content center-content">
                    <form method="post" id="mainForm" enctype="multipart/form-data" action="<%=process.runtime.continueURL()%>">
                        <div class="row form-horizontal">
                            <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
                            <input type="hidden" name="action_type" value="">
                            <input type="hidden" name="action_value" value="">
                            <input id="multiUploadTrigger" type="file" multiple="multiple" style="display: none"/>
                            <input id="echoReloadTrigger" type="file" style="display: none"/>
                            <div class="col-lg-12 col-xs-12 cesform-box">
                                <div class="row">
                                    <div class="col-lg-12 col-xs-12">
                                        <div class="table-gp tablebox">
                                            <div class="form-group ">
                                                <label class="col-sm-5 control-label">Facility Name</label>
                                                <div class="col-sm-6 col-md-7"><c:out value="${cancellationApprovalDto.facilityName}"/></div>
                                            </div>
                                            <div class="form-group ">
                                                <label class="col-sm-5 control-label">Facility Address</label>
                                                <div class="col-sm-6 col-md-7"><c:out value="${cancellationApprovalDto.facilityAddress}"/></div>
                                            </div>
                                            <div class="form-group ">
                                                <label class="col-sm-5 control-label">Approval Type</label>
                                                <div class="col-sm-6 col-md-7"><iais:code code="${cancellationApprovalDto.approvalType}"/></div>
                                            </div>
                                            <div class="form-group ">
                                                <label class="col-sm-5 control-label">Biological Agents/Toxins</label>
                                                <div class="col-sm-6 col-md-7"><c:out value="${cancellationApprovalDto.biologicalAgentToxin}"/></div>
                                            </div>
                                            <div class="form-group ">
                                                <label class="col-sm-5 control-label">Note</label>
                                                <div class="col-sm-6 col-md-7"></div>
                                            </div>
                                            <div class="form-group ">
                                                <label class="col-sm-5 control-label">Physical Possession of BA/T in Facility</label>
                                                <div class="col-sm-6 col-md-7"><c:out value="${cancellationApprovalDto.physicalPossession}"/></div>
                                            </div>
                                            <div class="form-group ">
                                                <label class="col-sm-5 control-label">Reasons <span style="color: red">*</span></label>
                                                <div class="col-sm-6 col-md-7">
                                                    <select name="reasons" class="reasonsDropdown" id="reasons">
                                                        <option value="">Please Select</option>
                                                        <option value="BSBRFAC001" <c:if test="${cancellationApprovalDto.reasons eq 'BSBRFAC001'}">selected="selected"</c:if>>No work involving the biological agent and/or toxin</option>
                                                        <option value="BSBRFAC002" <c:if test="${cancellationApprovalDto.reasons eq 'BSBRFAC002'}">selected="selected"</c:if>>Termination of project/business</option>
                                                        <option value="BSBRFAC003" <c:if test="${cancellationApprovalDto.reasons eq 'BSBRFAC003'}">selected="selected"</c:if>>Change in business model</option>
                                                        <option value="BSBRFAC004" <c:if test="${cancellationApprovalDto.reasons eq 'BSBRFAC004'}">selected="selected"</c:if>>Others, please provide details in Remarks field</option>
                                                    </select>
                                                    <span data-err-ind="reasons" class="error-msg"></span>
                                                </div>
                                            </div>
                                            <div class="form-group ">
                                                <label class="col-sm-5 control-label">Remarks</label>
                                                <div class="col-sm-6 col-md-7">
                                                    <textarea maxLength="300" class="col-xs-12" name="remarks" id="remarks" rows="5"><c:out value="${cancellationApprovalDto.remarks}"/></textarea>
                                                    <span data-err-ind="remarks" class="error-msg"></span>
                                                </div>
                                            </div>
                                            <%@include file="../primaryDocuments.jsp" %>
                                            <div class="panel-body">
                                                <div class="row">
                                                    <br>
                                                    <p>I, hereby declare and agree, the following:</p>
                                                    <br>
                                                    <div class="form-group " style="z-index: 10">
                                                        <div class="col-xs-1" style="padding: 30px 0 20px 30px;">
                                                            <input type="checkbox" name="declaration1" id="declaration1" value="Y" <c:if test="${cancellationApprovalDto.declaration1 eq 'Y'}">checked="checked"</c:if> />
                                                        </div>
                                                        <div class="col-xs-10 control-label">
                                                            <span>My facility meets the relevant requirements listed above for the cancellation of the approval indicated in the Cancellation Form.</span>
                                                        </div>
                                                    </div>
                                                    <div class="form-group " style="z-index: 10">
                                                        <div class="col-xs-1" style="padding: 30px 0 20px 30px;">
                                                            <input type="checkbox" name="declaration2" id="declaration2" value="Y" <c:if test="${cancellationApprovalDto.declaration2 eq 'Y'}">checked="checked"</c:if> />
                                                        </div>
                                                        <div class="col-xs-10 control-label">
                                                            <span>All the information provided in this application is true and accurate.</span>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <%@ include file="../prepareInnerFooter.jsp" %>
                        <%@ include file="../submitDeclareModal.jsp" %>
                        <%@ include file="/WEB-INF/jsp/iais/include/jumpAfterDraft.jsp" %>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>