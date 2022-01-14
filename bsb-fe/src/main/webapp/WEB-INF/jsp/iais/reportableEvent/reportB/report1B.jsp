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

<link href="<%=WEB_ROOT%>/css/bsb/bsb-common.css" rel="stylesheet"/>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-common.js"></script>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-file.js"></script>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb_followup.js"></script>

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
                                <p>N210205INC001</p>
                            </div>
                            <div class="clear"></div>
                        </div>

                        <div class="form-group">
                            <label class="col-xs-5 col-md-4 control-label">Name of Personnel</label>
                            <div class="col-sm-7 col-md-5 col-xs-7">
                                <p>Suspected exposure to biological agent or toxin</p>
                            </div>
                            <div class="clear"></div>
                        </div>

                        <div class="form-group">
                            <label class="col-xs-5 col-md-4 control-label">Description of medical follow-up</label>
                            <span class="mandatory otherQualificationSpan">*</span>
                            <div class="col-sm-7 col-md-5 col-xs-7">
                                <p>Suspected exposure to biological agent or toxin</p>
                            </div>
                            <div class="clear"></div>
                        </div>

                        <div class="form-group">
                            <label class="col-xs-5 col-md-4 control-label">Interpretation of test results</label>
                            <div class="col-sm-7 col-md-5 col-xs-7">
                                <p>Test01</p>
                            </div>
                            <div class="clear"></div>
                        </div>

                        <div class="form-group">
                            <label class="col-xs-5 col-md-4 control-label">Is further medical follow-up is advised/expected</label>
                            <div class="col-sm-7 col-md-5 col-xs-7">
                                <p>Yes</p>
                            </div>
                            <div class="clear"></div>
                        </div>

                        <div class="form-group">
                            <label class="col-xs-5 col-md-4 control-label">Estimated duration and frequency of follow-up</label>
                            <div class="col-sm-7 col-md-5 col-xs-7">
                                <p>689</p>
                            </div>
                            <div class="clear"></div>
                        </div>

                        <div class="form-group ">
                            <div class="col-xs-5 col-md-4 control-label">
                                <label for="followupDate">Date of next follow-up</label>
                                <span class="mandatory otherQualificationSpan">*</span>
                            </div>
                            <div class="col-sm-7 col-md-5 col-xs-7">
                                <input type="text" autocomplete="off" name="followupDate" id="followupDate" data-date-start-date="01/01/1900" value="" placeholder="dd/mm/yyyy" maxlength="10" class="date_picker form-control"/>
                                <span data-err-ind="followupDate" class="error-msg"></span>
                            </div>
                        </div>

                        <div class="form-group ">
                            <div class="col-xs-5 col-md-4 control-label">
                                <label for="remark">Remarks</label>
                                <span class="mandatory otherQualificationSpan">*</span>
                            </div>
                            <div class="col-sm-7 col-md-5 col-xs-7">
                                <input maxLength="132" type="text" autocomplete="off" name="remark" id="remark" value=''/>
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