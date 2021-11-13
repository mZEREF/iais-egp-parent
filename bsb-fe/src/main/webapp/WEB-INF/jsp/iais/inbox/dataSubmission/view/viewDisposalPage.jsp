<%@ page import="static sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT" %>
<%@ page import="sg.gov.moh.iais.egp.bsb.util.TableDisplayUtil" %>
<%@ taglib prefix="webui" uri="http://www.ecquaria.com/webui" %>
<%@ taglib prefix="ias" uri="http://www.ecq.com/iais" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%@ taglib prefix="iais-bsb" uri="http://www.ecq.com/iais-bsb" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="java.lang.String" %>

<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-internet"/>

<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-data-submission.js"></script>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-data-sub-common.js"></script>

<%@include file="dashboard.jsp" %>
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
                                                                            <label class="col-xs-5 col-md-4 control-label">Facility Name</label>
                                                                            <div class="col-sm-7 col-md-5 col-xs-7">
                                                                                <p>${dataSubInfo.facilityName}</p>
                                                                            </div>
                                                                            <div class="clear"></div>
                                                                        </div>
                                                                        <div class="form-group">
                                                                            <label class="col-xs-5 col-md-4 control-label">Facility Address</label>
                                                                            <div class="col-sm-7 col-md-5 col-xs-7">
                                                                                <p>${dataSubInfo.facilityName}</p>
                                                                            </div>
                                                                            <div class="clear"></div>
                                                                        </div>
                                                                        <div class="form-group">
                                                                            <label class="col-xs-5 col-md-4 control-label">Is the facility a Protected Place?</label>
                                                                            <div class="col-sm-7 col-md-5 col-xs-7">
                                                                                <p>${dataSubInfo.facilityName}</p>
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
                                                                <a class="collapsed" data-toggle="collapse" href="#previewBatInfo">Biological Agents&amp;Toxins</a>
                                                            </h4>
                                                        </div>
                                                        <div id="previewBatInfo" class="panel-collapse collapse">
                                                            <div class="panel-body">
                                                                <c:forEach var="info" items="${dataSubInfo.submissionBats}" varStatus="status">
                                                                    <div class="panel-main-content form-horizontal min-row">
                                                                        <div class="form-group">
                                                                            <div class="col-10">
                                                                                <strong>Agents/Toxin ${status.index+1}</strong>
                                                                            </div>
                                                                            <div class="clear"></div>
                                                                        </div>
                                                                        <div>
                                                                            <div class="form-group">
                                                                                <label class="col-xs-5 col-md-4 control-label">Schedule Type</label>
                                                                                <div class="col-sm-7 col-md-5 col-xs-7">
                                                                                    <p><iais:code code="${info.schedule}"/></p>
                                                                                </div>
                                                                                <div class="clear"></div>
                                                                            </div>
                                                                            <div class="form-group">
                                                                                <label class="col-xs-5 col-md-4 control-label">Biological Agent/Toxin</label>
                                                                                <div class="col-sm-7 col-md-5 col-xs-7">
                                                                                    <p>${info.biologicalName}</p>
                                                                                </div>
                                                                                <div class="clear"></div>
                                                                            </div>
                                                                            <c:if test="${info.schedule eq 'SCHTYPE006'}">
                                                                                <div class="form-group">
                                                                                    <label class="col-xs-5 col-md-4 control-label">Quantity Disposed</label>
                                                                                    <div class="col-sm-7 col-md-5 col-xs-7">
                                                                                        <p>${info.actualQty}</p>
                                                                                    </div>
                                                                                    <div class="clear"></div>
                                                                                </div>
                                                                                <div class="form-group">
                                                                                    <label class="col-xs-5 col-md-4 control-label">Unit of Measurement Transferred</label>
                                                                                    <div class="col-sm-7 col-md-5 col-xs-7">
                                                                                        <p><iais:code code="${info.measurementUnit}"/></p>
                                                                                    </div>
                                                                                    <div class="clear"></div>
                                                                                </div>
                                                                            </c:if>
                                                                            <div class="form-group">
                                                                                <label class="col-xs-5 col-md-4 control-label">Complete Destruction/Disposal</label>
                                                                                <div class="col-sm-7 col-md-5 col-xs-7">
                                                                                    <p>Yes</p>
                                                                                </div>
                                                                                <div class="clear"></div>
                                                                            </div>
                                                                            <div class="form-group">
                                                                                <label class="col-xs-5 col-md-4 control-label">Method of Destruction</label>
                                                                                <div class="col-sm-7 col-md-5 col-xs-7">
                                                                                    <p>${info.destructionMethod}</p>
                                                                                </div>
                                                                                <div class="clear"></div>
                                                                            </div>
                                                                            <div class="form-group">
                                                                                <label class="col-xs-5 col-md-4 control-label">Details and/or procedures of destruction</label>
                                                                                <div class="col-sm-7 col-md-5 col-xs-7">
                                                                                    <p>${info.destructionProceduresDetails}</p>
                                                                                </div>
                                                                                <div class="clear"></div>
                                                                            </div>
                                                                        </div>
                                                                    </div>
                                                                </c:forEach>

                                                                <div class="panel-main-content form-horizontal min-row">
                                                                    <div class="form-group">
                                                                        <div class="col-10"><strong>Additional Details</strong></div>
                                                                        <div class="clear"></div>
                                                                    </div>
                                                                    <div>
                                                                        <div class="form-group">
                                                                            <label class="col-xs-5 col-md-4 control-label">Facility Name</label>
                                                                            <div class="col-sm-7 col-md-5 col-xs-7">
                                                                                <p>${dataSubInfo.facilityName}</p>
                                                                            </div>
                                                                            <div class="clear"></div>
                                                                        </div>
                                                                        <div class="form-group">
                                                                            <label class="col-xs-5 col-md-4 control-label">Remarks</label>
                                                                            <div class="col-sm-7 col-md-5 col-xs-7">
                                                                                <p>${dataSubInfo.remarks}</p>
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
                                                                <a class="collapsed" data-toggle="collapse" href="#previewUploadInfo">Uploaded Documents</a>
                                                            </h4>
                                                        </div>
                                                        <div id="previewUploadInfo" class="panel-collapse collapse">
                                                            <div class="panel-body">
                                                                <c:if test="${not empty docs}">
                                                                    <div class="panel-main-content form-horizontal min-row">
                                                                        <div class="form-group">
                                                                            <div class="col-10"><strong>Report Type</strong></div>
                                                                            <div class="clear"></div>
                                                                        </div>
                                                                        <c:forEach var="file" items="${docs}">
                                                                            <div>
                                                                                <div class="form-group">
                                                                                    <div class="col-10"><p>${file.docName}(${String.format("%.1f", file.docSize/1024.0)}KB)</p></div>
                                                                                    <div class="clear"></div>
                                                                                </div>
                                                                            </div>
                                                                        </c:forEach>
                                                                    </div>
                                                                </c:if>
                                                                <c:if test="${not empty otherDocs}">
                                                                    <div class="panel-main-content form-horizontal min-row">
                                                                        <div class="form-group">
                                                                            <div class="col-10"><strong>Others</strong></div>
                                                                            <div class="clear"></div>
                                                                        </div>
                                                                        <c:forEach var="file" items="${otherDocs}">
                                                                            <div>
                                                                                <div class="form-group">
                                                                                    <div class="col-10"><p>${file.docName}(${String.format("%.1f", file.docSize/1024.0)}KB)</p></div>
                                                                                    <div class="clear"></div>
                                                                                </div>
                                                                            </div>
                                                                        </c:forEach>
                                                                    </div>
                                                                </c:if>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div class="col-xs-12">
                                    <a class="back" href="/bsb-fe/eservice/INTERNET/DataSubInbox"><em class="fa fa-angle-left"></em> Back</a>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</form>