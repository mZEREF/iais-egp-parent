<%@ page import="static sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT" %>
<%@ taglib prefix="webui" uri="http://www.ecquaria.com/webui" %>
<%@ taglib prefix="ias" uri="http://www.ecq.com/iais" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%@ taglib prefix="iais-bsb" uri="http://www.ecq.com/iais-bsb" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.utils.MaskUtil" %>


<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-internet"/>

<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-common.js"></script>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-report-event.js"></script>

<%@include file="/WEB-INF/jsp/iais/include/showErrorMsg.jsp" %>

<form method="post" id="mainForm" action="<%=process.runtime.continueURL()%>">
    <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
    <input type="hidden" name="action_type" value="">
    <input type="hidden" name="action_value" value="">
    <input type="hidden" name="action_additional" value="">
    <div class="main-content">
        <div class="form-horizontal">
            <div class="container">
                <div class="row" style="margin-top: 30px">
                    <div class="col-xs-12">
                        <div class="tab-gp steps-tab">
                            <div class="tab-content">
                                <div class="tab-pane fade in active">
                                    <div id="previewSubmitPanel" role="tabpanel">
                                        <div class="preview-gp">
                                            <div class="row">
                                                <div class="col-xs-12">
                                                    <div class="panel-group" role="tablist" aria-multiselectable="true">
                                                        <div class="panel panel-default">
                                                            <div class="panel-heading completed">
                                                                <h4 class="panel-title">
                                                                    <a class="collapsed" data-toggle="collapse"
                                                                       href="#previewIncidentInfo">Incident Follow-UP 1A Info</a>
                                                                </h4>
                                                            </div>
                                                            <div id="previewIncidentInfo" class="panel-collapse collapse">
                                                                <div class="panel-body">
                                                                    <div class="panel-main-content form-horizontal min-row">
                                                                        <div class="form-group">
                                                                            <label class="col-xs-5 col-md-4 control-label">Incident
                                                                                Reference No.</label>
                                                                            <div class="col-sm-7 col-md-5 col-xs-7">
                                                                                <p><c:out value="${view.referenceNo}"/></p>
                                                                            </div>
                                                                            <div class="clear"></div>
                                                                        </div>
                                                                        <c:forEach var="info" items="${view.causeDtoList}">
                                                                            <div class="form-group">
                                                                                <label class="col-xs-5 col-md-4 control-label">Explanation of cause</label>
                                                                                <div class="col-sm-7 col-md-5 col-xs-7">
                                                                                    <p><c:out value="${info.causeExplain}"/></p>
                                                                                </div>
                                                                                <div class="clear"></div>
                                                                            </div>
                                                                            <div class="form-group">
                                                                                <label class="col-xs-5 col-md-4 control-label">Corrective measures and/or preventive measures to address the probable cause</label>
                                                                                <div class="col-sm-7 col-md-5 col-xs-7">
                                                                                    <p><c:out value="${info.correctiveMeasure}"/></p>
                                                                                </div>
                                                                                <div class="clear"></div>
                                                                            </div>
                                                                            <div class="form-group">
                                                                                <label class="col-xs-5 col-md-4 control-label">Due date for implementation of corrective and/or preventive measures</label>
                                                                                <div class="col-sm-7 col-md-5 col-xs-7">
                                                                                    <p><c:out value="${info.dueEntityDt}"/></p>
                                                                                </div>
                                                                                <div class="clear"></div>
                                                                            </div>
                                                                            <div class="form-group">
                                                                                <label class="col-xs-5 col-md-4 control-label">Have all the identified corrective and preventive measures been implemented?</label>
                                                                                <div class="col-sm-7 col-md-5 col-xs-7">
                                                                                    <p><c:out value="${info.identifiedCorrective}"/></p>
                                                                                </div>
                                                                                <div class="clear"></div>
                                                                            </div>
                                                                            <div class="form-group">
                                                                                <label class="col-xs-5 col-md-4 control-label">Elaborate on the reason(s) for the delay</label>
                                                                                <div class="col-sm-7 col-md-5 col-xs-7">
                                                                                    <p><c:out value="${info.elaborateReason}"/></p>
                                                                                </div>
                                                                                <div class="clear"></div>
                                                                            </div>
                                                                            <div class="form-group">
                                                                                <label class="col-xs-5 col-md-4 control-label">Specify expected date where all corrective and/or preventive measures will be implemented</label>
                                                                                <div class="col-sm-7 col-md-5 col-xs-7">
                                                                                    <p><c:out value="${info.expectedDt}"/></p>
                                                                                </div>
                                                                                <div class="clear"></div>
                                                                            </div>
                                                                            <div class="form-group">
                                                                                <label class="col-xs-5 col-md-4 control-label">Remarks</label>
                                                                                <div class="col-sm-7 col-md-5 col-xs-7">
                                                                                    <p><c:out value="${info.remark}"/></p>
                                                                                </div>
                                                                                <div class="clear"></div>
                                                                            </div>
                                                                        </c:forEach>

                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </div>
                                                        <div class="panel panel-default">
                                                            <div class="panel-heading completed">
                                                                <h4 class="panel-title">
                                                                    <a class="collapsed" data-toggle="collapse"
                                                                       href="#previewDocuments">Documents</a>
                                                                </h4>
                                                            </div>
                                                            <div id="previewDocuments" class="panel-collapse collapse">
                                                                <div class="panel-body">
                                                                    <div class="panel-main-content form-horizontal min-row">
                                                                        <div class="form-group">
                                                                            <div class="col-10"><h3>Uploaded Documents</h3></div>
                                                                            <div class="clear"></div>
                                                                        </div>

                                                                        <c:forEach var="doc" items="${docSettings}">
                                                                            <c:set var="maskDocType" value="${MaskUtil.maskValue('file', doc.type)}"/>
                                                                            <c:set var="savedFileList" value="${docMap.get(doc.type)}" />
                                                                            <c:if test="${not empty savedFileList}">
                                                                                <div class="form-group">
                                                                                    <div class="col-10"><strong>${doc.typeDisplay}</strong></div>
                                                                                    <div class="clear"></div>
                                                                                </div>
                                                                                <div>
                                                                                    <c:forEach var="file" items="${savedFileList}">
                                                                                        <c:set var="tmpId" value="${MaskUtil.maskValue('file', file.repoId)}"/>
                                                                                        <div class="form-group">
                                                                                            <div class="col-10"><p><a href="javascript:void(0)" onclick="downloadFile('view', '${tmpId}')">${file.filename}</a>(${String.format("%.1f", file.size/1024.0)}KB)</p></div>
                                                                                            <div class="clear"></div>
                                                                                        </div>
                                                                                    </c:forEach>
                                                                                </div>
                                                                            </c:if>
                                                                        </c:forEach>
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="col-xs-12">
                        <a class="back" href="/bsb-fe/eservice/INTERNET/MohBSBInboxApp"><em class="fa fa-angle-left"></em>
                            Back</a>
                    </div>
                </div>
            </div>
        </div>
    </div>
</form>