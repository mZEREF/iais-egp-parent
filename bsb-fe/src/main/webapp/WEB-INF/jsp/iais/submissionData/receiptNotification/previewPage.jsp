<%@ page import="static sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT" %>
<%@ page import="sg.gov.moh.iais.egp.bsb.util.TableDisplayUtil" %>
<%@ taglib prefix="webui" uri="http://www.ecquaria.com/webui" %>
<%@ taglib prefix="ias" uri="http://www.ecq.com/iais" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%@ taglib prefix="iais-bsb" uri="http://www.ecq.com/iais-bsb" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="java.lang.String" %>

<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-internet"/>

<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-data-submission.js"></script>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-data-sub-common.js"></script>

<%@include file="../../inbox/dashboard/dashboard.jsp" %>
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
                                                                <a class="collapsed" data-toggle="collapse" href="#previewBatInfo">Biological Agents&amp;Toxins</a>
                                                            </h4>
                                                        </div>
                                                        <div id="previewBatInfo" class="panel-collapse collapse">
                                                            <div class="panel-body">
                                                                <div class="text-right app-font-size-16"><a href="#" id="edit" data-step-key="approvalProfile"><em class="fa fa-pencil-square-o"></em>Edit</a></div>
                                                                <c:forEach var="info" items="${receiveNotification.receiptNotList}" varStatus="status">
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
                                                                                <p><iais:code code="${info.scheduleType}"/></p>
                                                                            </div>
                                                                            <div class="clear"></div>
                                                                        </div>
                                                                        <div class="form-group">
                                                                            <label class="col-xs-5 col-md-4 control-label">Biological Agent/Toxin</label>
                                                                            <div class="col-sm-7 col-md-5 col-xs-7">
                                                                                <p>${info.bat}</p>
                                                                            </div>
                                                                            <div class="clear"></div>
                                                                        </div>
                                                                        <div class="form-group">
                                                                            <label class="col-xs-5 col-md-4 control-label">Quantity to Receive</label>
                                                                            <div class="col-sm-7 col-md-5 col-xs-7">
                                                                                <p>${info.receiveQty}</p>
                                                                            </div>
                                                                            <div class="clear"></div>
                                                                        </div>
                                                                        <div class="form-group">
                                                                            <label class="col-xs-5 col-md-4 control-label">Unit of Measurement Transferred</label>
                                                                            <div class="col-sm-7 col-md-5 col-xs-7">
                                                                                <p><iais:code code="${info.meaUnit}"/></p>
                                                                            </div>
                                                                            <div class="clear"></div>
                                                                        </div>
                                                                    </div>
                                                                    </c:forEach>
                                                                </div>

                                                                <div class="panel-main-content form-horizontal min-row">
                                                                    <div class="form-group">
                                                                        <div class="col-10"><strong>Additional Details</strong></div>
                                                                        <div class="clear"></div>
                                                                    </div>
                                                                    <div>
                                                                        <div class="form-group">
                                                                            <label class="col-xs-5 col-md-4 control-label">Mode of Procurement</label>
                                                                            <div class="col-sm-7 col-md-5 col-xs-7">
                                                                                <p><iais:code code="${receiveNotification.modeProcurement}"/></p>
                                                                            </div>
                                                                            <div class="clear"></div>
                                                                        </div>
                                                                        <div class="form-group">
                                                                            <label class="col-xs-5 col-md-4 control-label">Facility Name</label>
                                                                            <div class="col-sm-7 col-md-5 col-xs-7">
                                                                                <p>${facilityInfo.facName}</p>
                                                                            </div>
                                                                            <div class="clear"></div>
                                                                        </div>
                                                                        <div class="form-group">
                                                                            <label class="col-xs-5 col-md-4 control-label">Name of Source Facility</label>
                                                                            <div class="col-sm-7 col-md-5 col-xs-7">
                                                                                <p>${receiveNotification.sourceFacilityName}</p>
                                                                            </div>
                                                                            <div class="clear"></div>
                                                                        </div>
                                                                        <div class="form-group">
                                                                            <label class="col-xs-5 col-md-4 control-label">Address of Source Facility</label>
                                                                            <div class="col-sm-7 col-md-5 col-xs-7">
                                                                                <p>${receiveNotification.sourceFacilityAddress}</p>
                                                                            </div>
                                                                            <div class="clear"></div>
                                                                        </div>
                                                                        <div class="form-group">
                                                                            <label class="col-xs-5 col-md-4 control-label">Contact Person of Source Facility</label>
                                                                            <div class="col-sm-7 col-md-5 col-xs-7">
                                                                                <p>${receiveNotification.sourceFacilityContactPerson}</p>
                                                                            </div>
                                                                            <div class="clear"></div>
                                                                        </div>
                                                                        <div class="form-group">
                                                                            <label class="col-xs-5 col-md-4 control-label">Email of Contact Person</label>
                                                                            <div class="col-sm-7 col-md-5 col-xs-7">
                                                                                <p>${receiveNotification.contactPersonEmail}</p>
                                                                            </div>
                                                                            <div class="clear"></div>
                                                                        </div>
                                                                        <div class="form-group">
                                                                            <label class="col-xs-5 col-md-4 control-label">Tel No of Contact Person</label>
                                                                            <div class="col-sm-7 col-md-5 col-xs-7">
                                                                                <p>${receiveNotification.contactPersonTel}</p>
                                                                            </div>
                                                                            <div class="clear"></div>
                                                                        </div>
                                                                        <div class="form-group">
                                                                            <label class="col-xs-5 col-md-4 control-label">Name of Courier Service Provider</label>
                                                                            <div class="col-sm-7 col-md-5 col-xs-7">
                                                                                <p>${receiveNotification.provider}</p>
                                                                            </div>
                                                                            <div class="clear"></div>
                                                                        </div>
                                                                        <div class="form-group">
                                                                            <label class="col-xs-5 col-md-4 control-label">Flight No.</label>
                                                                            <div class="col-sm-7 col-md-5 col-xs-7">
                                                                                <p>${receiveNotification.flightNo}</p>
                                                                            </div>
                                                                            <div class="clear"></div>
                                                                        </div>
                                                                        <div class="form-group">
                                                                            <label class="col-xs-5 col-md-4 control-label">Actual Arrival Date</label>
                                                                            <div class="col-sm-7 col-md-5 col-xs-7">
                                                                                <p>${receiveNotification.actualArrivalDate}</p>
                                                                            </div>
                                                                            <div class="clear"></div>
                                                                        </div>
                                                                        <div class="form-group">
                                                                            <label class="col-xs-5 col-md-4 control-label">Actual Arrival Time</label>
                                                                            <div class="col-sm-7 col-md-5 col-xs-7">
                                                                                <p>${receiveNotification.actualArrivalTime}</p>
                                                                            </div>
                                                                            <div class="clear"></div>
                                                                        </div>
                                                                        <div class="form-group">
                                                                            <label class="col-xs-5 col-md-4 control-label">Remarks</label>
                                                                            <div class="col-sm-7 col-md-5 col-xs-7">
                                                                                <p>${receiveNotification.remarks}</p>
                                                                            </div>
                                                                            <div class="clear"></div>
                                                                        </div>
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </div>

                                                    <div class="panel panel-default">
                                                        <div class="panel-heading">
                                                            <h4 class="panel-title">
                                                                <a class="collapsed" data-toggle="collapse" href="#previewDocs">Declarations</a>
                                                            </h4>
                                                        </div>
                                                        <div id="previewDocs" class="panel-collapse collapse">
                                                            <div class="panel-body">
                                                                <div class="panel-main-content form-horizontal min-row">
                                                                    <div class="form-group ">
                                                                        <div class="col-xs-1" style="padding: 30px 0 20px 30px;">
                                                                            <input type="checkbox" name="ensure" id="ensure" value="Y"/>
                                                                        </div>
                                                                        <div class="col-xs-10 control-label">
                                                                            <label for="ensure">I have obtained the necessary import permit(s) and/or approval(s) from the relevant authorities for this importation</label>
                                                                            <span data-err-ind="ensure" class="error-msg"></span>
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