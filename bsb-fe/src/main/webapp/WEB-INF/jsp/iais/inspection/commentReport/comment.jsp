<%@ page import="static sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT"%>
<%@ taglib prefix="webui" uri="http://www.ecquaria.com/webui" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="java.lang.String"%>
<%@ page import="com.ecquaria.cloud.moh.iais.common.utils.MaskUtil" %>

<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-internet"/>

<link href="<%=WEB_ROOT%>/css/bsb/bsb-common.css" rel="stylesheet"/>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-inspection-comment-report.js"></script>

<%@include file="/WEB-INF/jsp/iais/include/showErrorMsg.jsp"%>

<%@include file="dashboard.jsp"%>

<%--@elvariable id="commentInsReportDTO" type="sg.gov.moh.iais.egp.bsb.dto.inspection.CommentInsReportDto"--%>
<form method="post" id="mainForm" action="<%=process.runtime.continueURL()%>">
    <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
    <input type="hidden" name="action_type" value="">
    <input type="hidden" name="action_value" value="">
    <input type="hidden" name="action_additional" value="">

    <div class="main-content">
        <div class="container">
            <div class="row">
                <div class="col-xs-12">
                    <div class="tab-gp steps-tab">
                        <div class="tab-content">
                            <div class="tab-pane fade in active">
                                <div id="commentInsReportPanel" role="tabpanel">
                                    <div class="form-horizontal">
                                        <div class="form-group ">
                                            <div class="col-sm-5 control-label">
                                                <label>Download Inspection/Certification Report</label>
                                            </div>
                                            <div class="col-sm-6 col-md-7">
                                                <a href="/bsb-fe/ajax/doc/download/commentInsReport/report/<iais:mask name="file" value="${reportRepoId}"/>">Download</a>
                                            </div>
                                        </div>
                                        <div class="form-group ">
                                            <div class="col-sm-5 control-label">
                                                <label>Upload Inspection/Certification Report with Comments?</label>
                                            </div>
                                            <div class="col-sm-6 col-md-7">
                                                <div class="col-sm-4 col-md-2" style="margin-top: 8px">
                                                    <label for="doUpload">Yes</label>
                                                    <input type="radio" name="upload" id="doUpload" value="Y" <c:if test="${commentInsReportDTO.upload eq 'Y'}">checked="checked"</c:if> />
                                                </div>
                                                <div class="col-sm-4 col-md-2" style="margin-top: 8px">
                                                    <label for="noComment">No</label>
                                                    <input type="radio" name="upload" id="noComment" value="N" <c:if test="${commentInsReportDTO.upload eq 'N'}">checked="checked"</c:if> />
                                                </div>
                                                <span data-err-ind="upload" class="error-msg"></span>
                                            </div>
                                        </div>
                                        <div id="attachmentUploadDiv" class="document-upload-gp" <c:if test="${commentInsReportDTO.upload ne 'Y'}">style="display: none"</c:if>>
                                            <div class="document-upload-list">
                                                <h3>Attachment</h3>
                                                <div class="file-upload-gp">
                                                    <c:forEach var="info" items="${commentInsReportDTO.newDocMap.values()}">
                                                        <c:set var="tmpId" value="${MaskUtil.maskValue('file', info.tmpId)}"/>
                                                        <div id="${tmpId}FileDiv">
                                                            <span id="${tmpId}Span"><a href="/bsb-fe/ajax/doc/download/commentInsReport/comment/${tmpId}">${info.filename}</a>(${String.format("%.1f", info.size/1024.0)}KB)</span><button
                                                                type="button" class="btn btn-secondary btn-sm" onclick="deleteFile('${tmpId}')">Delete</button><button
                                                            <span data-err-ind="${info.tmpId}" class="error-msg"></span>
                                                        </div>
                                                    </c:forEach>
                                                    <a class="btn file-upload btn-secondary" data-upload-file="attachment" href="javascript:void(0);">Upload</a>
                                                    <span data-err-ind="attachment" class="error-msg"></span>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div class="application-tab-footer">
                                    <div class="row">
                                        <div class="col-xs-12 col-sm-6">
                                            <div class="button-group">
                                                <a class="btn btn-primary" id="submitBtn" >Submit</a>
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
</form>