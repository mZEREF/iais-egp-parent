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
                            <div class="col-10"><h2 style="border-bottom: 0;padding-top: 10px">Follow-up Report 1A</h2></div>
                            <div class="clear"></div>
                        </div>

                        <div class="form-group">
                            <label class="col-xs-5 col-md-4 control-label">Incident Reference No.</label>
                            <div class="col-sm-7 col-md-5 col-xs-7">
                                <p><c:out value="${followup1A.referenceNo}"/></p>
                            </div>
                            <div class="clear"></div>
                        </div>

                        <c:forEach var="info" items="${followup1A.infoADtoList}">

                            <div class="form-group">
                                <div class="col-10"><h3><iais:code code="${info.incidentCause}"/></h3></div>
                                <div class="clear"></div>
                            </div>

                            <div class="form-group">
                                <label class="col-xs-5 col-md-4 control-label">Explanation of cause</label>
                                <div class="col-sm-7 col-md-5 col-xs-7">
                                    <p><c:out value="${info.explainCause}" /></p>
                                </div>
                                <div class="clear"></div>
                            </div>

                            <div class="form-group">
                                <label class="col-xs-5 col-md-4 control-label">Corrective measures and/or preventive measures to address the probable cause</label>
                                <div class="col-sm-7 col-md-5 col-xs-7">
                                    <p><c:out value="${info.measure}"/></p>
                                </div>
                                <div class="clear"></div>
                            </div>

                            <div class="form-group">
                                <label class="col-xs-5 col-md-4 control-label">Due date for implementation of corrective and/or preventive measures</label>
                                <div class="col-sm-7 col-md-5 col-xs-7">
                                    <p><c:out value="${info.implementEntityDate}"/></p>
                                </div>
                                <div class="clear"></div>
                            </div>

                            <div class="form-group ">
                                <div class="col-xs-5 col-md-4 control-label">
                                    <label>Have all the identified corrective and preventive measures been implemented?</label>
                                    <span class="mandatory otherQualificationSpan">*</span>
                                </div>
                                <div class="col-sm-7 col-md-5 col-xs-7">
                                    <div class="col-sm-4 col-md-2" style="margin-top: 8px">
                                        <label for="isImplementY--v--${info.incidentCause}">Yes</label>
                                        <input type="radio" name="isImplemented--v--${info.incidentCause}" id="isImplementY--v--${info.incidentCause}" <c:if test="${info.isImplemented eq 'Y'}">checked = "checked"</c:if> value="Y" onclick="hideImplement('${info.incidentCause}')"/>
                                    </div>
                                    <div class="col-sm-4 col-md-2" style="margin-top: 8px">
                                        <label for="isImplementN--v--${info.incidentCause}">No</label>
                                        <input type="radio" name="isImplemented--v--${info.incidentCause}" id="isImplementN--v--${info.incidentCause}" <c:if test="${info.isImplemented eq 'N'}">checked = "checked"</c:if> value="N" onclick="showImplement('${info.incidentCause}')" />
                                    </div>
                                    <span data-err-ind="isImplemented--v--${info.incidentCause}" class="error-msg"></span>
                                </div>
                            </div>

                        <div id="isAllIdentified--v--${info.incidentCause}" <c:if test="${info.isImplemented ne 'N'}">style="display: none" </c:if>>
                                <div class="form-group ">
                                    <div class="col-xs-5 col-md-4 control-label">
                                        <label for="delayReason--v--${info.incidentCause}">Elaborate on the reason(s) for the delay</label>
                                        <span class="mandatory otherQualificationSpan">*</span>
                                    </div>
                                    <div class="col-sm-7 col-md-5 col-xs-7">
                                        <input maxLength="400" type="text" autocomplete="off" name="delayReason--v--${info.incidentCause}" id="delayReason--v--${info.incidentCause}" value="<c:out value="${info.delayReason}"/>"/>
                                        <span data-err-ind="delayReason--v--${info.incidentCause}" class="error-msg"></span>
                                    </div>
                                </div>

                                <div class="form-group ">
                                    <div class="col-xs-5 col-md-4 control-label">
                                        <label for="correctiveDate--v--${info.incidentCause}">Specify expected date where all corrective and/or preventive measures will be implemented</label>
                                        <span class="mandatory otherQualificationSpan">*</span>
                                    </div>
                                    <div class="col-sm-7 col-md-5 col-xs-7">
                                        <input type="text" autocomplete="off" name="correctiveDate--v--${info.incidentCause}" id="correctiveDate--v--${info.incidentCause}" data-date-start-date="01/01/1900" value="<c:out value="${info.correctiveDate}"/>" placeholder="dd/mm/yyyy" maxlength="10" class="date_picker form-control"/>
                                        <span data-err-ind="correctiveDate--v--${info.incidentCause}" class="error-msg"></span>
                                    </div>
                                </div>
                            </div>

                            <div class="form-group ">
                                <div class="col-xs-5 col-md-4 control-label">
                                    <label for="remarks--v--${info.incidentCause}">Remarks</label>
                                    <span class="mandatory otherQualificationSpan">*</span>
                                </div>
                                <div class="col-sm-7 col-md-5 col-xs-7">
                                    <input maxLength="600" type="text" autocomplete="off" name="remarks--v--${info.incidentCause}" id="remarks--v--${info.incidentCause}" value="<c:out value="${info.remarks}"/>"/>
                                    <span data-err-ind="remarks--v--${info.incidentCause}" class="error-msg"></span>
                                </div>
                            </div>
                        </c:forEach>

                        <div class="form-group">
                            <div class="col-10"><h3>Attachments</h3></div>
                            <div class="clear"></div>
                        </div>

                        <%@ include file="../primaryDocuments.jsp" %>

                        <div class="form-group">
                            <label class="col-xs-5 col-md-4 control-label">Created By</label>
                            <div class="col-sm-7 col-md-5 col-xs-7">
                                <p>Alice, 25/04/2021 13:00</p>
                            </div>
                            <div class="clear"></div>
                        </div>

                        <div class="form-group">
                            <label class="col-xs-5 col-md-4 control-label">Last Updated By</label>
                            <div class="col-sm-7 col-md-5 col-xs-7">
                                <p>Bob, 25/04/2021 15:00</p>
                            </div>
                            <div class="clear"></div>
                        </div>

                    </div>

                    <%@ include file="InnerFooter.jsp" %>

                    <%@include file="/WEB-INF/jsp/iais/include/jumpAfterDraft.jsp"%>
                </div>
            </div>
        </div>
    </div>
</form>