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
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-common.js"></script>

<%@include file="/WEB-INF/jsp/iais/include/showErrorMsg.jsp"%>

<%@include file="dashboard.jsp"%>

<%--@elvariable id="commentInsReportDTO" type="sg.gov.moh.iais.egp.bsb.dto.inspection.CommentInsReportDto"--%>
<form method="post" id="mainForm" enctype="multipart/form-data" action="<%=process.runtime.continueURL()%>">
    <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
    <input type="hidden" name="action_type" value="">
    <input type="hidden" name="action_value" value="">
    <input type="hidden" name="action_additional" value="">
    <input type="hidden" id="deleteNewFiles" name="deleteNewFiles" value="">
    <div id="fileUploadInputDiv" style="display: none"></div>

    <div class="main-content">
        <div class="container">
            <div class="row">
                <div class="col-xs-12">
                    <div class="form-horizontal">
                        <div class="col-md-10">
                            <div class="row" style="font-weight: 700;text-align: center">
                                <div class="col-md-3">S/N</div>
                                <div class="col-md-6">Checklist Question</div>
                                <div class="col-md-3">Remarks</div>
                            </div>
                            <div class="row" style="text-align: center;border-top:1px solid #D1D1D1;padding: 10px 0 ">
                                <div class="col-md-3">1</div>
                                <div class="col-md-6">The requirement for document control and record retention for BC meeting minutes and other relevant records.</div>
                                <div class="col-md-3">-</div>
                            </div>
                            <div id="attachmentUploadDiv" class="document-upload-gp">
                                <div class="document-upload-list">
                                    <h3>Attachment</h3>
                                    <div class="file-upload-gp">
                                        <c:forEach var="info" items="${commentInsReportDTO.newDocMap.values()}">
                                            <c:set var="tmpId" value="${MaskUtil.maskValue('file', info.tmpId)}"/>
                                            <div id="${tmpId}FileDiv">
                                                <span id="${tmpId}Span"><a href="/bsb-fe/ajax/doc/download/commentInsReport/comment/${tmpId}">${info.filename}</a>(${String.format("%.1f", info.size/1024.0)}KB)</span><button
                                                    type="button" class="btn btn-secondary btn-sm" onclick="deleteFile('${tmpId}')">Delete</button>
                                                <span data-err-ind="${info.tmpId}" class="error-msg"></span>
                                            </div>
                                        </c:forEach>
                                        <a class="btn file-upload btn-secondary" data-upload-file="attachment" href="javascript:void(0);">Upload</a>
                                        <span data-err-ind="attachment" class="error-msg"></span>
                                    </div>
                                </div>
                            </div>
                            <h3>Remarks</h3>
                            <div class="form-group">
                                <div class="col-md-12 col-sm-12">
                                    <label for="remark"></label><textarea autocomplete="off" class="col-xs-12" name="remark" id="remark" maxlength="1000" style="width: 100%"></textarea>
                                    <span data-err-ind="remark" class="error-msg"></span>
                                </div>
                            </div>
                            <div class="application-tab-footer">
                                <div class="row">
                                    <div class="col-xs-12">
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
</form>