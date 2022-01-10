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
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-data-sub-common.js"></script>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-data-submission.js"></script>

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
                                                                <a class="collapsed" data-toggle="collapse" href="#previewBatInfo">Biological Agents &amp;Toxins</a>
                                                            </h4>
                                                        </div>
                                                        <div id="previewBatInfo" class="panel-collapse collapse">
                                                            <div class="panel-body">
                                                                <div class="text-right app-font-size-16"><a href="#" data-step-key="approvalProfile"><em class="fa fa-pencil-square-o"></em>Edit</a>
                                                                </div>
                                                                <c:forEach var="item" items="${transferLists}" varStatus="status">
                                                                    <div class="panel-main-content form-horizontal min-row">
                                                                        <div class="form-group">
                                                                            <div class="col-10"><strong>Agents/Toxin ${status.index+1}</strong></div>
                                                                            <div class="clear"></div>
                                                                        </div>
                                                                            <div>
                                                                                <div class="form-group">
                                                                                    <label class="col-xs-5 col-md-4 control-label">Schedule Type</label>
                                                                                    <div class="col-sm-7 col-md-5 col-xs-7"><p>${item.scheduleType}</p></div>
                                                                                    <div class="clear"></div>
                                                                                </div>
                                                                                <div class="form-group">
                                                                                    <label class="col-xs-5 col-md-4 control-label">Biological Agent/Toxin</label>
                                                                                    <div class="col-sm-7 col-md-5 col-xs-7"><p>${item.batCode}</p></div>
                                                                                    <div class="clear"></div>
                                                                                </div>
                                                                                <c:choose>
                                                                                    <c:when test="${item.scheduleType ne 'SCHTYPE006'}">
                                                                                        <div class="form-group">
                                                                                            <label class="col-xs-5 col-md-4 control-label">Expected Quantity of Biological Agent</label>
                                                                                            <div class="col-sm-7 col-md-5 col-xs-7"><p>${item.expectedBatQty}</p></div>
                                                                                            <div class="clear"></div>
                                                                                        </div>
                                                                                    </c:when>
                                                                                    <c:otherwise>
                                                                                        <div class="form-group">
                                                                                            <label class="col-xs-5 col-md-4 control-label">Expected Quantity to Receive</label>
                                                                                            <div class="col-sm-7 col-md-5 col-xs-7"><p>${item.expReceivedQty}</p></div>
                                                                                            <div class="clear"></div>
                                                                                        </div>
                                                                                        <div class="form-group">
                                                                                            <label class="col-xs-5 col-md-4 control-label">Unit of Measurement</label>
                                                                                            <div class="col-sm-7 col-md-5 col-xs-7"><p>${item.meaUnit}</p></div>
                                                                                            <div class="clear"></div>
                                                                                        </div>
                                                                                    </c:otherwise>
                                                                                </c:choose>
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
                                                                            <label class="col-xs-5 col-md-4 control-label">Transferring Facility</label>
                                                                            <div class="col-sm-7 col-md-5 col-xs-7"><p>${facilityInfo.facName}</p></div>
                                                                            <div class="clear"></div>
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