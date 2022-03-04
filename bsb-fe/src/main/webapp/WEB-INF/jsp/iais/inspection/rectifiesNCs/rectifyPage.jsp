<%@ page import="static sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT"%>
<%@ taglib prefix="webui" uri="http://www.ecquaria.com/webui" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="java.lang.String"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>


<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-internet"/>

<link href="<%=WEB_ROOT%>/css/bsb/bsb-common.css" rel="stylesheet"/>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-common.js"></script>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-inspection-ncs.js"></script>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-common-file.js"></script>

<%@include file="/WEB-INF/jsp/iais/include/showErrorMsg.jsp"%>

<%@include file="dashboard.jsp"%>

<%--@elvariable id="commentInsReportDTO" type="sg.gov.moh.iais.egp.bsb.dto.inspection.CommentInsReportDto"--%>
<form method="post" id="mainForm" enctype="multipart/form-data" action="<%=process.runtime.continueURL()%>">
    <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
    <input type="hidden" name="action_type" value="">
    <input type="hidden" name="action_value" value="">
    <input type="hidden" name="action_additional" value="">
    <input id="multiUploadTrigger" type="file" multiple="multiple" style="display: none"/>
    <input id="echoReloadTrigger" type="file" style="display: none"/>
    <input type="hidden" id="deleteNewFiles" name="deleteNewFiles" value="">
    <input type="hidden" id="deleteExistFiles" name="deleteExistFiles" value="">
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
                                <div class="col-md-6"><c:out value="${rectifyItemDto.itemText}"/></div>
                                <div class="col-md-3"><c:out value="${rectifyItemDto.remarks}"/></div>
                            </div>
                            <div id="attachmentUploadDiv" class="document-upload-gp">
                                <div class="document-upload-list">
                                    <h3>Attachment</h3>
                                    <div class="file-upload-gp">
                                        <c:if test="${newSavedDoc ne null}">
                                            <c:forEach var="info" items="${newSavedDoc}">
                                                <c:set var="tmpId"><iais:mask name="file" value="${info.tmpId}"/></c:set>
                                                <div id="${tmpId}FileDiv">
                                                    <a href="/bsb-fe/ajax/doc/download/insNonCompliance/new/${tmpId}" style="text-decoration: underline"><span id="${tmpId}Span">${info.filename}</span></a>(<fmt:formatNumber value="${info.size/1024.0}" type="number" pattern="0.0"/>KB)<button
                                                        type="button" class="btn btn-secondary btn-sm" onclick="deleteSavedFile('${tmpId}')">Delete</button>
                                                    <span data-err-ind="${info.tmpId}" class="error-msg"></span>
                                                </div>
                                            </c:forEach>
                                        </c:if>
                                        <c:if test="${oldSavedDoc ne null}">
                                            <c:forEach var="info" items="${oldSavedDoc}">
                                                <c:set var="repoId"><iais:mask name="file" value="${info.repoId}"/></c:set>
                                                <div id="${repoId}FileDiv">
                                                    <a href="/bsb-fe/ajax/doc/download/insNonCompliance/repo/${repoId}" style="text-decoration: underline"><span id="${repoId}Span">${info.filename}</span></a>(<fmt:formatNumber value="${info.size/1024.0}" type="number" pattern="0.0"/>KB)<button
                                                        type="button" class="btn btn-secondary btn-sm" onclick="deleteSavedFile('${repoId}')">Delete</button>
                                                    <span data-err-ind="${info.repoId}" class="error-msg"></span>
                                                </div>
                                            </c:forEach>
                                        </c:if>
                                        <a class="btn file-upload btn-secondary" data-upload-file="attachment" href="javascript:void(0);">Upload</a>
                                        <span data-err-ind="attachment" class="error-msg"></span>
                                    </div>
                                </div>
                            </div>
                            <h3>Remarks</h3>
                            <div class="form-group">
                                <div class="col-md-12 col-sm-12">
                                    <label for="remarks"></label><textarea autocomplete="off" class="col-xs-12" name="remarks" id="remarks" maxlength="1000" style="width: 100%"><c:out value="${rectifyItemSaveDto.remarks}"/></textarea>
                                    <span data-err-ind="remarks" class="error-msg"></span>
                                </div>
                            </div>
                            <div class="application-tab-footer">
                                <div class="row">
                                    <div class="col-xs-12">
                                        <div class="button-group">
                                            <a class="btn btn-primary" id="cancelBtn" >Cancel</a>
                                            <a class="btn btn-primary" id="saveBtn" >Save</a>
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