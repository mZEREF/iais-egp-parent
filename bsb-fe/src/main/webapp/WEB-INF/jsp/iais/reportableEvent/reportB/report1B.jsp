<%@ page import="static sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT"%>
<%@ taglib prefix="webui" uri="http://www.ecquaria.com/webui" %>
<%@ taglib prefix="ias" uri="http://www.ecq.com/iais" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%@ taglib prefix="iais-bsb" uri="http://www.ecq.com/iais-bsb" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-internet"/>

<link href="<%=WEB_ROOT%>/css/bsb/bsb-common.css" rel="stylesheet"/>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-common.js"></script>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-common-file.js"></script>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-followup.js"></script>

<%@include file="/WEB-INF/jsp/iais/include/showErrorMsg.jsp"%>

<%@include file="dashboard.jsp"%>

<form method="post" id="mainForm" enctype="multipart/form-data" action="<%=process.runtime.continueURL()%>">
    <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
    <input type="hidden" name="action_type" value="">
    <input type="hidden" name="action_value" value="">
    <div class="main-content">
        <div class="container">
            <div class="row">
                <div class="col-xs-12">
                    <div class="form-horizontal">
                        <div class="form-group">
                            <div class="col-10"><h2 style="border-bottom: 0">Follow-up Report 1B</h2></div>
                            <div class="clear"></div>
                        </div>

                        <div class="form-group">
                            <label class="col-xs-5 col-md-4 control-label">Incident Reference No.</label>
                            <div class="col-sm-7 col-md-5 col-xs-7">
                                <a href="#" onclick="openIncident('${MaskUtil.maskValue('editId',followup1B.incidentId)}')"><c:out value="${followup1B.referenceNo}"/></a></label>
                            </div>
                            <div class="clear"></div>
                        </div>

                        <div class="form-group">
                            <label class="col-xs-5 col-md-4 control-label">Name of Personnel</label>
                            <div class="col-sm-7 col-md-5 col-xs-7">
                                <p><c:out value="${followup1B.personnelName}"/></p>
                            </div>
                            <div class="clear"></div>
                        </div>

                        <div class="form-group">
                            <div class="col-xs-5 col-md-4 control-label">
                                <label for="followupDes">Description of medical follow-up</label>
                                <span class="mandatory otherQualificationSpan">*</span>
                            </div>
                            <div class="col-sm-7 col-md-5 col-xs-7">
                                <input maxLength="500" type="text" autocomplete="off" name="followupDes" id="followupDes" value='<c:out value="${followup1B.followupDes}"/>'/>
                                <span data-err-ind="followupDes" class="error-msg"></span>
                            </div>
                            <div class="clear"></div>
                        </div>

                        <div class="form-group">
                            <div class="col-xs-5 col-md-4 control-label">
                                <label for="testResult">Interpretation of test results</label>
                            </div>
                            <div class="col-sm-7 col-md-5 col-xs-7">
                                <input maxLength="400" type="text" autocomplete="off" name="testResult" id="testResult" value='<c:out value="${followup1B.testResult}"/>'/>
                                <span data-err-ind="testResult" class="error-msg"></span>
                            </div>
                            <div class="clear"></div>
                        </div>

                        <div class="form-group">
                            <div class="col-xs-5 col-md-4 control-label">
                                <label for="testResult">Is further medical follow-up is advised/expected</label>
                            </div>
                            <div class="col-sm-7 col-md-5 col-xs-7">
                                <div class="col-sm-5" style="margin-top: 8px">
                                    <input type="radio" name="isExpected" id="isExpectedY" value="Y" <c:if test="${followup1B.isExpected eq 'Y'}">checked="checked"</c:if> />
                                    <label for="isExpectedY">Yes</label>
                                </div>
                                <div class="col-sm-5" style="margin-top: 8px">
                                    <input type="radio" name="isExpected" id="isExpectedN" value="N" <c:if test="${followup1B.isExpected eq 'N'}">checked="checked"</c:if> />
                                    <label for="isExpectedN">No</label>
                                </div>
                                <span data-err-ind="isExpected" class="error-msg"></span>
                            </div>
                            <div class="clear"></div>
                        </div>

                        <div class="form-group">
                            <div class="col-xs-5 col-md-4 control-label">
                                <label for="followupDuration">Estimated duration and frequency of follow-up</label>
                            </div>
                            <div class="col-sm-7 col-md-5 col-xs-7">
                                <input maxLength="100" type="text" autocomplete="off" name="followupDuration" id="followupDuration" value='<c:out value="${followup1B.followupDuration}"/>'/>
                                <span data-err-ind="followupDuration" class="error-msg"></span>
                            </div>
                            <div class="clear"></div>
                        </div>

                        <div class="form-group ">
                            <div class="col-xs-5 col-md-4 control-label">
                                <label for="followupDate">Date of next follow-up</label>
                                <span class="mandatory otherQualificationSpan">*</span>
                            </div>
                            <div class="col-sm-7 col-md-5 col-xs-7">
                                <input type="text" autocomplete="off" name="followupDate" id="followupDate" data-date-start-date="01/01/1900" value='<c:out value="${followup1B.followupDate}"/>' placeholder="dd/mm/yyyy" maxlength="10" class="date_picker form-control"/>
                                <span data-err-ind="followupDate" class="error-msg"></span>
                            </div>
                        </div>

                        <div class="form-group ">
                            <div class="col-xs-5 col-md-4 control-label">
                                <label for="remarks">Remarks</label>
                                <span class="mandatory otherQualificationSpan">*</span>
                            </div>
                            <div class="col-sm-7 col-md-5 col-xs-7">
                                <input maxLength="132" type="text" autocomplete="off" name="remarks" id="remarks" value='<c:out value="${followup1B.remarks}"/>'/>
                                <span data-err-ind="remark" class="error-msg"></span>
                            </div>
                        </div>

                        <div class="form-group">
                            <div class="col-10"><strong>Attachments</strong></div>
                            <div class="clear"></div>
                        </div>

                        <%@ include file="../primaryDocuments.jsp" %>

                    </div>
                    <%@ include file="InnerFooter.jsp" %>

                    <%@include file="/WEB-INF/jsp/iais/include/jumpAfterDraft.jsp"%>
                </div>
            </div>
        </div>
    </div>
</form>