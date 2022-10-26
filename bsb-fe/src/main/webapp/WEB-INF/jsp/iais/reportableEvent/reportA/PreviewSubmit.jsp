<%@ page import="static sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT" %>
<%@ taglib prefix="webui" uri="http://www.ecquaria.com/webui" %>
<%@ taglib prefix="ias" uri="http://www.ecq.com/iais" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
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
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-followup.js"></script>

<%@include file="/WEB-INF/jsp/iais/include/showErrorMsg.jsp"%>

<%@include file="../dashboard.jsp" %>
<form method="post" id="mainForm" action="<%=process.runtime.continueURL()%>">
    <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
    <input type="hidden" name="action_type" value="">
    <input type="hidden" name="action_value" value="">
    <input type="hidden" name="action_additional" value="">
    <div class="main-content">
        <div class="form-horizontal">
            <div class="container">
                <div class="row">
                    <div class="col-xs-12">
                        <h3 style="border-bottom: 1px solid #BABABA;margin-top: 20px">Preview & Submit</h3>
                        <div class="preview-gp">
                            <div class="row">
                                <div class="col-xs-12">
                                    <div class="form-horizontal">
                                        <div class="form-group">
                                            <div class="col-sm-5 control-label">
                                                <label for="referNo">Incident Reference No.</label>
                                            </div>
                                            <div class="col-sm-6 col-md-7">
                                                <label id="referNo">${previewA.referenceNo}</label>
                                            </div>
                                        </div>

                                        <c:forEach var="item" items="${followup1A.infoADtoList}">
                                            <h3 style="margin: 10px 0"><iais:code code="${item.incidentCause}"/></h3>
                                            <div class="form-group ">
                                                <div class="col-sm-5 control-label">
                                                    <label>Explanation of cause</label>
                                                </div>
                                                <div class="col-sm-6 col-md-7">
                                                    <label><c:out value="${item.explainCause}"/></label>
                                                </div>
                                            </div>

                                            <div class="form-group ">
                                                <div class="col-sm-5 control-label">
                                                    <label>Corrective measures and/or preventive measures to address the probable cause</label>
                                                </div>
                                                <div class="col-sm-6 col-md-7">
                                                    <label><c:out value="${item.measure}"/></label>
                                                </div>
                                            </div>

                                            <div class="form-group ">
                                                <div class="col-sm-5 control-label">
                                                    <label>Due date for implementation of corrective and/or preventive measures</label>
                                                </div>
                                                <div class="col-sm-6 col-md-7">
                                                    <label><c:out value="${item.implementEntityDate}"/></label>
                                                </div>
                                            </div>

                                            <div class="form-group ">
                                                <div class="col-sm-5 control-label">
                                                    <label>Have all the identified corrective and preventive measures been implemented?</label>
                                                </div>
                                                <div class="col-sm-6 col-md-7">
                                                    <label><c:out value="${item.isImplemented}"/></label>
                                                </div>
                                            </div>

                                            <div class="form-group ">
                                                <div class="col-sm-5 control-label">
                                                    <label>Elaborate on the reason(s) for the delay</label>
                                                </div>
                                                <div class="col-sm-6 col-md-7">
                                                    <label><c:out value="${item.delayReason}"/></label>
                                                </div>
                                            </div>

                                            <div class="form-group ">
                                                <div class="col-sm-5 control-label">
                                                    <label>Specify expected date where all corrective and/or preventive measures will be implemented</label>
                                                </div>
                                                <div class="col-sm-6 col-md-7">
                                                    <label><c:out value="${item.correctiveDate}"/></label>
                                                </div>
                                            </div>

                                        </c:forEach>

                                        <h3 style="margin: 10px 0;border-bottom: 0px">Attachments</h3>
                                        <c:forEach var="doc" items="${docSettings}">
                                            <c:set var="savedFileList" value="${savedFiles.get(doc.type)}" />
                                            <c:set var="newFileList" value="${newFiles.get(doc.type)}" />
                                            <c:if test="${not empty savedFileList or not empty newFileList}">
                                                <div class="form-group">
                                                    <div class="col-10"><strong>${doc.typeDisplay}</strong></div>
                                                    <div class="clear"></div>
                                                </div>
                                                <div>
                                                    <c:forEach var="file" items="${savedFileList}">
                                                        <c:set var="repoId"><iais:mask name="file" value="${file.repoId}"/></c:set>
                                                        <div class="form-group">
                                                            <div class="col-10"><p><a href="/bsb-web/ajax/doc/download/reportableEvent/followup/repo/${repoId}">${file.filename}</a>(<fmt:formatNumber value="${file.size/1024.0}" type="number" pattern="0.0"/>KB)</p></div>
                                                            <div class="clear"></div>
                                                        </div>
                                                    </c:forEach>
                                                    <c:forEach var="file" items="${newFileList}">
                                                        <c:set var="tmpId"><iais:mask name="file" value="${file.tmpId}"/></c:set>
                                                        <div class="form-group">
                                                            <div class="col-10"><p><a href="/bsb-web/ajax/doc/download/reportableEvent/followup/new/${tmpId}">${file.filename}</a>(<fmt:formatNumber value="${file.size/1024.0}" type="number" pattern="0.0"/>KB)</p></div>
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
                        <div class="application-tab-footer">
                            <div class="row">
                                <div class="col-xs-12 col-sm-6 ">
                                    <a class="back" id="back" href="#"><em class="fa fa-angle-left"></em> Back</a>
                                </div>
                                <div class="col-xs-12 col-sm-6">
                                    <div class="button-group">
                                        <a class="btn btn-secondary" id="saveDraft" >Save as Draft</a>
                                        <a class="btn btn-primary next" id="submit" >Save</a>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <%@include file="/WEB-INF/jsp/iais/include/jumpAfterDraft.jsp"%>
                    </div>
                </div>
            </div>
        </div>
    </div>
</form>