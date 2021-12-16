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

<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-common.js"></script>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-report-inventory.js"></script>

<%@include file="/WEB-INF/jsp/iais/include/showErrorMsg.jsp"%>

<%@include file="../dashboard.jsp" %>
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
                                                                <div class="text-right app-font-size-16"><a href="#"><em
                                                                        class="fa fa-pencil-square-o"></em>Edit</a>
                                                                </div>
                                                                <div class="panel-main-content form-horizontal min-row">
                                                                    <div class="form-group">
                                                                        <div class="col-10"><strong>Facility
                                                                            Profile</strong></div>
                                                                        <div class="clear"></div>
                                                                    </div>
                                                                    <div>
                                                                            <div class="form-group">
                                                                                <label class="col-xs-5 col-md-4 control-label">Facility Name</label>
                                                                                <div class="col-sm-7 col-md-5 col-xs-7">
                                                                                    <p>${facilityInfo.facName}</p>
                                                                                </div>
                                                                                <div class="clear"></div>
                                                                            </div>
                                                                            <div class="form-group">
                                                                                <label class="col-xs-5 col-md-4 control-label">Facility Address</label>
                                                                                <div class="col-sm-7 col-md-5 col-xs-7">
                                                                                    <p>${facilityInfo.facAddress}</p>
                                                                                </div>
                                                                                <div class="clear"></div>
                                                                            </div>
                                                                            <div class="form-group">
                                                                                <label class="col-xs-5 col-md-4 control-label">Is the facility a Protected Place?</label>
                                                                                <div class="col-sm-7 col-md-5 col-xs-7">
                                                                                    <p>${facilityInfo.isProtected}</p>
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
                                                                <div class="text-right app-font-size-16"><a href="#" data-step-key=""><em class="fa fa-pencil-square-o"></em>Edit</a>
                                                                </div>
                                                                <c:set var="type" value="${reportType}"/>
                                                                <c:set var="reportDoc" value="${newFiles.get(type)}"/>
                                                                <c:set var="othersDoc" value="${newFiles.get('others')}"/>
                                                                <div class="panel-main-content form-horizontal min-row">
                                                                    <c:if test="${not empty reportDoc}">
                                                                        <div class="form-group">
                                                                            <div class="col-10"><strong>Report Type</strong></div>
                                                                            <div class="clear"></div>
                                                                        </div>
                                                                        <div>
                                                                            <c:forEach var="file" items="${reportDoc}">
                                                                                <div class="form-group">
                                                                                    <div class="col-10"><p>${file.filename}(${String.format("%.1f", file.size/1024.0)}KB)</p></div>
                                                                                    <div class="clear"></div>
                                                                                </div>
                                                                            </c:forEach>
                                                                        </div>
                                                                    </c:if>
                                                                </div>

                                                                <div class="panel-main-content form-horizontal min-row">
                                                                    <c:if test="${not empty othersDoc}">
                                                                        <div class="form-group">
                                                                            <div class="col-10"><strong>Others</strong></div>
                                                                            <div class="clear"></div>
                                                                        </div>
                                                                        <div>
                                                                            <c:forEach var="file" items="${othersDoc}">
                                                                                <div class="form-group">
                                                                                    <div class="col-10"><p>${file.filename}(${String.format("%.1f", file.size/1024.0)}KB)</p></div>
                                                                                    <div class="clear"></div>
                                                                                </div>
                                                                            </c:forEach>
                                                                        </div>
                                                                    </c:if>
                                                                </div>

                                                            </div>
                                                        </div>
                                                    </div>

                                                    <div class="panel panel-default">
                                                        <div class="panel-heading completed">
                                                            <h4 class="panel-title">
                                                                <a class="collapsed" data-toggle="collapse" href="#previewDec">Declarations</a>
                                                            </h4>
                                                        </div>
                                                        <div id="previewDec" class="panel-collapse collapse">
                                                            <div class="panel-body">
                                                                <div class="text-right app-font-size-16"><a href="#"><em
                                                                        class="fa fa-pencil-square-o"></em>Edit</a>
                                                                </div>
                                                                <div class="panel-main-content form-horizontal min-row">
                                                                    <div>
                                                                        <div class="form-group">
                                                                            <p>The information provided in this submission and any document submitted together with the submissoin is not false, misleading or inaccurate in any particular manner.</p>
                                                                            <br/>
                                                                            <div class="col-sm-4" style="margin-top: 8px">
                                                                                <input type="radio" name="declare" id="declareT" value="yes"/>
                                                                                <label for="declareT">yes</label>
                                                                            </div>
                                                                            <div class="col-sm-4" style="margin-top: 8px">
                                                                                <input type="radio" name="declare" id="declareN" value="no"/>
                                                                                <label for="declareN">no</label>
                                                                            </div>
                                                                        </div>
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </div>
                                                    <div class="modal fade" id="submitDeclareModal" role="dialog" aria-labelledby="myModalLabel">
                                                        <div class="modal-dialog modal-dialog-centered" role="document">
                                                            <div class="modal-content">
                                                                <div class="modal-body">
                                                                    <div class="row">
                                                                        <div class="col-md-12"><span style="font-size: 2rem">Please check the declaration box</span></div>
                                                                    </div>
                                                                </div>
                                                                <div class="modal-footer">
                                                                    <button type="button" class="btn btn-secondary btn-md" data-dismiss="modal">Close</button>
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <%@ include file="../InnerFooter.jsp" %>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</form>