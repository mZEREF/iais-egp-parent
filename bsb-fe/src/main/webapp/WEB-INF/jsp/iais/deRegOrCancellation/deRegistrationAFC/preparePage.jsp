<%@ page import="static sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT"%>
<%@ taglib prefix="webui" uri="http://www.ecquaria.com/webui" %>
<%@ taglib prefix="ias" uri="http://www.ecq.com/iais" %>
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
                    <form method="post" id="mainForm" enctype="multipart/form-data" action="<%=process.runtime.continueURL()%>">
                        <div class="row form-horizontal">
                            <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
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
                                                <label class="col-sm-5 control-label">Reasons <span style="color: red">*</span></label>
                                                <div class="col-sm-6 col-md-7">
                                                    <select name="reasons" id="reasons">
                                                        <option value="">Please Select</option>
                                                        <option value="BSBRFAD001" <c:if test="${deRegistrationAFCDto.reasons eq 'BSBRFAD001'}">selected="selected"</c:if>>AFC team no longer have a qualified Biosafety Professional</option>
                                                        <option value="BSBRFAD002" <c:if test="${deRegistrationAFCDto.reasons eq 'BSBRFAD002'}">selected="selected"</c:if>>AFC team no longer have a qualified Engineering Professional</option>
                                                        <option value="BSBRFAD003" <c:if test="${deRegistrationAFCDto.reasons eq 'BSBRFAD003'}">selected="selected"</c:if>>Company no longer in business</option>
                                                        <option value="BSBRFAD004" <c:if test="${deRegistrationAFCDto.reasons eq 'BSBRFAD004'}">selected="selected"</c:if>>No longer wants to provide facility certification services</option>
                                                        <option value="BSBRFAD005" <c:if test="${deRegistrationAFCDto.reasons eq 'BSBRFAD005'}">selected="selected"</c:if>>Others, please provide details in Remarks field</option>
                                                    </select>
                                                    <span data-err-ind="reasons" class="error-msg"></span>
                                                </div>
                                            </div>
                                            <div class="form-group ">
                                                <label class="col-sm-5 control-label">Remarks</label>
                                                <div class="col-sm-6 col-md-7">
                                                    <textarea maxLength="300" class="col-xs-12" name="remarks" id="remarks" rows="5"><c:out value="${deRegistrationAFCDto.remarks}"/></textarea>
                                                    <span data-err-ind="remarks" class="error-msg"></span>
                                                </div>
                                            </div>
                                            <%@include file="../primaryDocuments.jsp" %>
                                            <div class="panel-body">
                                                <div class="row">
                                                    <br>
                                                    <div class="form-group " style="z-index: 10">
                                                        <div class="col-xs-1" style="padding: 30px 0 20px 30px;">
                                                            <input type="checkbox" name="declaration" id="declaration" value="Y" <c:if test="${deRegistrationAFCDto.declaration eq 'Y'}">checked="checked"</c:if> />
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
                        <div class="application-tab-footer">
                            <div class="row">
                                <div class="col-xs-12 col-sm-6 ">
                                    <a class="back" href="/bsb-fe/eservice/INTERNET/MohBSBInboxMsg"><em class="fa fa-angle-left"></em> Back</a>
                                </div>
                                <div class="col-xs-12 col-sm-6">
                                    <div class="button-group">
                                        <a class="btn btn-secondary" id="saveDraft" >Save as Draft</a>
                                        <a class="btn btn-primary next" id="next" >Next</a>
                                    </div>
                                </div>
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
                        <%@include file="/WEB-INF/jsp/iais/include/jumpAfterDraft.jsp"%>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>