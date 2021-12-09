<%@ page import="static sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT"%>
<%@ taglib prefix="webui" uri="http://www.ecquaria.com/webui" %>
<%@ taglib prefix="ias" uri="http://www.ecq.com/iais" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%@ taglib prefix="iais-bsb" uri="http://www.ecq.com/iais-bsb" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="java.lang.String" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.utils.MaskUtil" %>

<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-internet"/>

<link href="<%=WEB_ROOT%>/css/bsb/bsb-common.css" rel="stylesheet"/>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-common.js"></script>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-common-approval-app.js"></script>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-approval-app.js"></script>

<%@include file="/WEB-INF/jsp/iais/include/showErrorMsg.jsp"%>

<%@include file="dashboard.jsp"%>

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
                        <%@ include file="../mainAppCommon/approvalapp/InnerNavTab.jsp" %>
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
                                                                <a class="collapsed" data-toggle="collapse" href="#previewBatInfo">Biological Agents &amp; Toxins</a>
                                                            </h4>
                                                        </div>
                                                        <div id="previewBatInfo" class="panel-collapse collapse">
                                                            <div class="panel-body">
                                                                <div class="text-right app-font-size-16"><a href="#" data-step-key="approvalProfile"><em class="fa fa-pencil-square-o"></em>Edit</a></div>
                                                                <c:forEach var="approvalProfile" items="${approvalProfileList}">
                                                                    <div class="panel-main-content form-horizontal min-row">
                                                                        <div class="form-group">
                                                                            <div class="col-10"><strong><iais:code code="${approvalProfile.schedule}"/></strong></div>
                                                                            <div class="clear"></div>
                                                                        </div>
                                                                        <c:forEach var="info" items="${approvalProfile.batInfos}">
                                                                            <div>
                                                                                <div class="form-group">
                                                                                    <label class="col-xs-5 col-md-4 control-label">List of Agents or Toxins</label>
                                                                                    <div class="col-sm-7 col-md-5 col-xs-7"><p>${info.batName}</p></div>
                                                                                    <div class="clear"></div>
                                                                                </div>
                                                                            </div>
                                                                            <div>
                                                                                <div class="form-group">
                                                                                    <label class="col-xs-5 col-md-4 control-label">Estimated maximum volume (in litres) of production at any one time</label>
                                                                                    <div class="col-sm-7 col-md-5 col-xs-7"><p>${info.prodMaxVolumeLitres}</p></div>
                                                                                    <div class="clear"></div>
                                                                                </div>
                                                                            </div>
                                                                            <div>
                                                                                <div class="form-group">
                                                                                    <label class="col-xs-5 col-md-4 control-label">Method or system used for large scale production</label>
                                                                                    <div class="col-sm-7 col-md-5 col-xs-7"><p>${info.lspMethod}</p></div>
                                                                                    <div class="clear"></div>
                                                                                </div>
                                                                            </div>
                                                                            <div>
                                                                                <div class="form-group">
                                                                                    <label class="col-xs-5 col-md-4 control-label">Mode of Procurement</label>
                                                                                    <div class="col-sm-7 col-md-5 col-xs-7"><p><iais:code code="${info.procurementMode}"/></p></div>
                                                                                    <div class="clear"></div>
                                                                                </div>
                                                                            </div>
                                                                            <div>
                                                                                <div class="form-group">
                                                                                    <label class="col-xs-5 col-md-4 control-label">Transfer From Facility Name</label>
                                                                                    <div class="col-sm-7 col-md-5 col-xs-7"><p>${info.facilityNameOfTransfer}</p></div>
                                                                                    <div class="clear"></div>
                                                                                </div>
                                                                            </div>
                                                                            <div>
                                                                                <div class="form-group">
                                                                                    <label class="col-xs-5 col-md-4 control-label">Expected Date of Transfer</label>
                                                                                    <div class="col-sm-7 col-md-5 col-xs-7"><p>${info.expectedDateOfImport}</p></div>
                                                                                    <div class="clear"></div>
                                                                                </div>
                                                                            </div>
                                                                            <div>
                                                                                <div class="form-group">
                                                                                    <label class="col-xs-5 col-md-4 control-label">Contact Person from Transferring Facility</label>
                                                                                    <div class="col-sm-7 col-md-5 col-xs-7"><p>${info.contactPersonNameOfTransfer}</p></div>
                                                                                    <div class="clear"></div>
                                                                                </div>
                                                                            </div>
                                                                            <div>
                                                                                <div class="form-group">
                                                                                    <label class="col-xs-5 col-md-4 control-label">Contact No of Contact Person from Transferring Facility</label>
                                                                                    <div class="col-sm-7 col-md-5 col-xs-7"><p>${info.impCtcPersonNo}</p></div>
                                                                                    <div class="clear"></div>
                                                                                </div>
                                                                            </div>
                                                                            <div>
                                                                                <div class="form-group">
                                                                                    <label class="col-xs-5 col-md-4 control-label">Email Address of Contact Person from Transferring Facility</label>
                                                                                    <div class="col-sm-7 col-md-5 col-xs-7"><p>${info.contactPersonEmailOfTransfer}</p></div>
                                                                                    <div class="clear"></div>
                                                                                </div>
                                                                            </div>
                                                                            <div>
                                                                                <div class="form-group">
                                                                                    <label class="col-xs-5 col-md-4 control-label">Facility Address 1</label>
                                                                                    <div class="col-sm-7 col-md-5 col-xs-7"><p>${info.transferFacAddr1}</p></div>
                                                                                    <div class="clear"></div>
                                                                                </div>
                                                                            </div>
                                                                            <div>
                                                                                <div class="form-group">
                                                                                    <label class="col-xs-5 col-md-4 control-label">Facility Address 2</label>
                                                                                    <div class="col-sm-7 col-md-5 col-xs-7"><p>${info.transferFacAddr2}</p></div>
                                                                                    <div class="clear"></div>
                                                                                </div>
                                                                            </div>
                                                                            <div>
                                                                                <div class="form-group">
                                                                                    <label class="col-xs-5 col-md-4 control-label">Facility Address 3</label>
                                                                                    <div class="col-sm-7 col-md-5 col-xs-7"><p>${info.transferFacAddr3}</p></div>
                                                                                    <div class="clear"></div>
                                                                                </div>
                                                                            </div>
                                                                            <div>
                                                                                <div class="form-group">
                                                                                    <label class="col-xs-5 col-md-4 control-label">Country</label>
                                                                                    <div class="col-sm-7 col-md-5 col-xs-7"><p><iais:code code="${info.transferCountry}"/></p></div>
                                                                                    <div class="clear"></div>
                                                                                </div>
                                                                            </div>
                                                                            <div>
                                                                                <div class="form-group">
                                                                                    <label class="col-xs-5 col-md-4 control-label">City</label>
                                                                                    <div class="col-sm-7 col-md-5 col-xs-7"><p>${info.transferCity}</p></div>
                                                                                    <div class="clear"></div>
                                                                                </div>
                                                                            </div>
                                                                            <div>
                                                                                <div class="form-group">
                                                                                    <label class="col-xs-5 col-md-4 control-label">State</label>
                                                                                    <div class="col-sm-7 col-md-5 col-xs-7"><p>${info.transferState}</p></div>
                                                                                    <div class="clear"></div>
                                                                                </div>
                                                                            </div>
                                                                            <div>
                                                                                <div class="form-group">
                                                                                    <label class="col-xs-5 col-md-4 control-label">Postal Code</label>
                                                                                    <div class="col-sm-7 col-md-5 col-xs-7"><p>${info.transferPostalCode}</p></div>
                                                                                    <div class="clear"></div>
                                                                                </div>
                                                                            </div>
                                                                            <div>
                                                                                <div class="form-group">
                                                                                    <label class="col-xs-5 col-md-4 control-label">Name of Courier Service Provider</label>
                                                                                    <div class="col-sm-7 col-md-5 col-xs-7"><p>${info.courierServiceProviderName}</p></div>
                                                                                    <div class="clear"></div>
                                                                                </div>
                                                                            </div>
                                                                            <div>
                                                                                <div class="form-group">
                                                                                    <label class="col-xs-5 col-md-4 control-label">Remarks</label>
                                                                                    <div class="col-sm-7 col-md-5 col-xs-7"><p>${info.remarks}</p></div>
                                                                                    <div class="clear"></div>
                                                                                </div>
                                                                            </div>
                                                                        </c:forEach>
                                                                    </div>
                                                                </c:forEach>
                                                            </div>
                                                        </div>
                                                    </div>
                                                    <div class="panel panel-default">
                                                        <div class="panel-heading completed">
                                                            <h4 class="panel-title">
                                                                <a class="collapsed" data-toggle="collapse" href="#previewDocs">Primary Documents</a>
                                                            </h4>
                                                        </div>
                                                        <div id="previewDocs" class="panel-collapse collapse">
                                                            <div class="panel-body">
                                                                <div class="text-right app-font-size-16"><a href="#" data-step-key="primaryDocs"><em class="fa fa-pencil-square-o"></em>Edit</a></div>
                                                                <div class="panel-main-content form-horizontal min-row">
                                                                    <c:forEach var="doc" items="${docSettings}">
                                                                        <c:set var="maskDocType" value="${MaskUtil.maskValue('file', doc.type)}"/>
                                                                        <c:set var="savedFileList" value="${savedFiles.get(doc.type)}" />
                                                                        <c:set var="newFileList" value="${newFiles.get(doc.type)}" />
                                                                        <c:if test="${not empty savedFileList or not empty newFileList}">
                                                                            <div class="form-group">
                                                                                <div class="col-10"><strong>${doc.typeDisplay}</strong></div>
                                                                                <div class="clear"></div>
                                                                            </div>
                                                                            <div>
                                                                                <c:forEach var="file" items="${savedFileList}">
                                                                                    <c:set var="tmpId" value="${MaskUtil.maskValue('file', file.repoId)}"/>
                                                                                    <div class="form-group">
                                                                                        <div class="col-10"><p><a href="javascript:void(0)" onclick="downloadFile('saved', '${tmpId}')">${file.filename}</a>(${String.format("%.1f", file.size/1024.0)}KB)</p></div>
                                                                                        <div class="clear"></div>
                                                                                    </div>
                                                                                </c:forEach>
                                                                                <c:forEach var="file" items="${newFileList}">
                                                                                    <c:set var="tmpId" value="${MaskUtil.maskValue('file', file.tmpId)}"/>
                                                                                    <div class="form-group">
                                                                                        <div class="col-10"><p><a href="javascript:void(0)" onclick="downloadFile('new', '${tmpId}')">${file.filename}</a>(${String.format("%.1f", file.size/1024.0)}KB)</p></div>
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
                                <%@ include file="InnerFooter.jsp" %>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</form>