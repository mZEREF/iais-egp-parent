<%@ page import="static sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT" %>
<%@ taglib prefix="webui" uri="http://www.ecquaria.com/webui" %>
<%@ taglib prefix="ias" uri="http://www.ecq.com/iais" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%@ taglib prefix="iais-bsb" uri="http://www.ecq.com/iais-bsb" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="java.lang.String" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.utils.MaskUtil" %>

<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>

<webui:setLayout name="iais-intranet"/>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-revocation.js"></script>
<div class="dashboard">
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
                                    <div id="previewSubmitPanel" role="tabpanel">
                                        <div class="preview-gp">
                                            <div class="row">
                                                <div class="col-xs-12">
                                                    <div class="panel-group" role="tablist" aria-multiselectable="true">
                                                        <div class="panel panel-default">
                                                            <div class="panel-heading completed">
                                                                <h4 class="panel-title">
                                                                    <a class="collapsed" data-toggle="collapse" href="#previewFacInfo">Facility Information</a>
                                                                </h4>
                                                            </div>
                                                            <div id="previewFacInfo" class="panel-collapse collapse">
                                                                <div class="panel-body">
                                                                    <div class="panel-main-content form-horizontal min-row">
                                                                        <div class="form-group">
                                                                            <div class="col-10"><strong>Facility Profile</strong></div>
                                                                            <div class="clear"></div>
                                                                        </div>
                                                                        <div>
                                                                            <div class="form-group">
                                                                                <label class="col-xs-5 col-md-4 control-label">Active Approval No. to be revoked</label>
                                                                                <div class="col-sm-7 col-md-5 col-xs-7">
                                                                                    <p><c:out value="${revokeDto.approvalNo}"/></p>
                                                                                </div>
                                                                                <div class="clear"></div>
                                                                            </div>
                                                                            <div class="form-group">
                                                                                <label class="col-xs-5 col-md-4 control-label">Facility Name</label>
                                                                                <div class="col-sm-7 col-md-5 col-xs-7">
                                                                                    <p><c:out value="${revokeDto.facName}"/></p>
                                                                                </div>
                                                                                <div class="clear"></div>
                                                                            </div>
                                                                            <div class="form-group">
                                                                                <label class="col-xs-5 col-md-4 control-label">Facility Address</label>
                                                                                <div class="col-sm-7 col-md-5 col-xs-7">
                                                                                    <p><c:out value="${revokeDto.facAddress}"/></p>
                                                                                </div>
                                                                                <div class="clear"></div>
                                                                            </div>
                                                                            <div class="form-group">
                                                                                <label class="col-xs-5 col-md-4 control-label">Facility Classification</label>
                                                                                <div class="col-sm-7 col-md-5 col-xs-7">
                                                                                    <p><iais:code code="${revokeDto.facClassification}"/></p>
                                                                                </div>
                                                                                <div class="clear"></div>
                                                                            </div>
                                                                            <div class="form-group">
                                                                                <label class="col-xs-5 col-md-4 control-label">Activity Type</label>
                                                                                <div class="col-sm-7 col-md-5 col-xs-7">
                                                                                    <p><iais:code code="${revokeDto.activityType}"/></p>
                                                                                </div>
                                                                                <div class="clear"></div>
                                                                            </div>
                                                                            <div class="form-group">
                                                                                <label class="col-xs-5 col-md-4 control-label">Approval</label>
                                                                                <div class="col-sm-7 col-md-5 col-xs-7">
                                                                                    <p></p>
                                                                                </div>
                                                                                <div class="clear"></div>
                                                                            </div>
                                                                            <div class="form-group">
                                                                                <label class="col-xs-5 col-md-4 control-label">Approval Status</label>
                                                                                <div class="col-sm-7 col-md-5 col-xs-7">
                                                                                    <p><iais:code code="${revokeDto.approvalStatus}"/></p>
                                                                                </div>
                                                                                <div class="clear"></div>
                                                                            </div>
                                                                        </div>
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </div>

                                                        <div class="panel panel-default">
                                                            <div class="panel-heading completed">
                                                                <h4 class="panel-title">
                                                                    <a class="collapsed" data-toggle="collapse" href="#previewProcessData">Processing Data</a>
                                                                </h4>
                                                            </div>
                                                            <div id="previewProcessData" class="panel-collapse collapse">
                                                                <div class="panel-body">
                                                                    <div class="panel-main-content form-horizontal min-row">
                                                                        <div class="form-group">
                                                                            <label class="col-xs-5 col-md-4 control-label">Reason for Revocation</label>
                                                                            <div class="col-sm-7 col-md-5 col-xs-7">
                                                                                <p><c:out value="${revokeDto.reasonContent}"/></p>
                                                                            </div>
                                                                            <div class="clear"></div>
                                                                        </div>
                                                                        <div class="form-group">
                                                                            <label class="col-xs-5 col-md-4 control-label">Remarks</label>
                                                                            <div class="col-sm-7 col-md-5 col-xs-7">
                                                                                <p><c:out value="${revokeDto.remarks}"/></p>
                                                                            </div>
                                                                            <div class="clear"></div>
                                                                        </div>
                                                                    </div>

                                                                    <div class="panel-main-content form-horizontal min-row">
                                                                        <div class="form-group">
                                                                            <div class="col-10"><strong>Uploaded Documents</strong></div>
                                                                            <div class="clear"></div>
                                                                        </div>
                                                                        <div>
                                                                            <div class="form-group">
                                                                                <div class="col-10"><strong>Internal</strong>
                                                                                    <c:if test="${savedFiles ne null}">
                                                                                        <c:forEach var="docTypes" items="${docTypes}">
                                                                                            <c:forEach var="info" items="${savedFiles.get(docTypes)}">
                                                                                                <c:set var="tmpId" value="${MaskUtil.maskValue('file', info.repoId)}"/>
                                                                                                <p><a href="javascript:void(0)" onclick="downloadFile('saved', '${tmpId}')">${info.filename}</a> ${String.format("%.1f", info.size/1024.0)}KB</p>
                                                                                            </c:forEach>
                                                                                        </c:forEach>
                                                                                    </c:if>
                                                                                </div>
                                                                                <div class="clear"></div>
                                                                            </div>

                                                                            <div class="form-group">
                                                                                <label class="col-xs-5 col-md-4 control-label">Others</label>
                                                                                <div class="col-sm-7 col-md-5 col-xs-7"><p></p>
                                                                                </div>
                                                                                <div class="clear"></div>
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
                                <a style="float:left;padding-top: 1.1%;" class="back" id="back" href="#"><em class="fa fa-angle-left"></em> Back</a>
                                <div align="right">
                                    <button name="submitRevoke" id="submitRevoke" type="button" class="btn btn-primary">Submit</button>
                                </div>
                                <div>&nbsp;</div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </form>
</div>