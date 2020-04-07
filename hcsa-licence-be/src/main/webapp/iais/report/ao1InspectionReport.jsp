<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>
<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
    String webroot=IaisEGPConstant.BE_CSS_ROOT;
%>
<webui:setLayout name="iais-intranet"/>
<div class="dashboard" style="background-image:url('<%=webroot%>img/Masthead-banner.jpg')">
<form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
    <%@ include file="/include/formHidden.jsp" %>
    <input type="hidden" name="action_type" value="">
    <input type="hidden" name="crud_action_value" value="">
    <input type="hidden" name="crud_action_additional" value="">
    <div class="main-content">
        <div class="row">
            <div class="col-lg-12 col-xs-12">
                <div class="center-content">
                    <div class="intranet-content">
                        <iais:body>
                            <div class="container">
                                <div class="col-xs-12">
                                    <div class="tab-gp dashboard-tab">
                                        <ul class="nav nav-tabs hidden-xs hidden-sm" role="tablist">
                                            <li id="info" class="${infoClassTop}" role="presentation"><a href="#tabInfo" aria-controls="tabInfo" role="tab"
                                                                                                         data-toggle="tab">Info</a></li>
                                            <li class="complete" role="presentation"><a href="#tabDocuments"
                                                                                        aria-controls="tabDocuments" role="tab"
                                                                                        data-toggle="tab">Documents</a></li>
                                            <li id="report" class="${reportClassTop}" role="presentation"><a id="reportClink" href="#tabInspectionReport"
                                                                                                             aria-controls="tabProcessing" role="tab"
                                                                                                             data-toggle="tab">Inspection Report</a></li>
                                            <li class="complete" role="presentation"><a href="#tabProcessing"
                                                                                        aria-controls="tabProcessing" role="tab"
                                                                                        data-toggle="tab">Processing</a></li>
                                        </ul>
                                        <div class="tab-nav-mobile visible-xs visible-sm">
                                            <div class="swiper-wrapper" role="tablist">
                                                <div class="swiper-slide"><a href="#tabInfo" aria-controls="tabInfo" role="tab"
                                                                             data-toggle="tab">Info</a></div>
                                                <div class="swiper-slide"><a href="#tabDocuments" aria-controls="tabDocuments"
                                                                             role="tab" data-toggle="tab">Documents</a></div>
                                                <div class="swiper-slide"><a href="#tabInspectionReport"
                                                                             aria-controls="tabInspectionReport" role="tab"
                                                                             data-toggle="tab">InspectionReport</a></div>
                                                <div class="swiper-slide"><a href="#tabProcessing" aria-controls="tabProcessing"
                                                                             role="tab" data-toggle="tab">Processing</a></div>
                                            </div>
                                            <div class="swiper-button-prev"></div>
                                            <div class="swiper-button-next"></div>
                                        </div>
                                        <div class="tab-content">
                                            <div class="${infoClassBelow}" id="tabInfo" role="tabpanel">

                                                <div class="panel panel-default">
                                                    <!-- Default panel contents -->
                                                    <div class="panel-heading"><strong>Submission Details</strong></div>
                                                    <div class="row">
                                                        <div class="col-xs-12">
                                                            <div class="table-gp">
                                                                <table class="table table-bordered">
                                                                    <tbody>
                                                                    <tr>
                                                                        <td align="right">Application Type</td>
                                                                        <td>${applicationViewDto.applicationType}</td>
                                                                    </tr>
                                                                    <tr>
                                                                        <td class="col-xs-6" align="right">Application No. (Overall)
                                                                        </td>
                                                                        <td class="col-xs-6">${applicationViewDto.applicationNoOverAll}</td>
                                                                    </tr>
                                                                    <tr>
                                                                        <td align="right">Application No.</td>
                                                                        <td>${applicationViewDto.applicationDto.applicationNo}</td>
                                                                    </tr>
                                                                    <tr>
                                                                        <td align="right">Service Type</td>
                                                                        <td>${applicationViewDto.serviceType}</td>
                                                                    </tr>
                                                                    <tr>
                                                                        <td align="right">Submission Date</td>
                                                                        <td>${applicationViewDto.submissionDate}</td>
                                                                    </tr>
                                                                    <tr>
                                                                        <td align="right">Current Status</td>
                                                                        <td>${applicationViewDto.currentStatus}</td>
                                                                    </tr>
                                                                    </tbody>
                                                                </table>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div align="center">
                                                    <a href="/hcsa-licence-web/eservice/INTRANET/LicenceBEViewService" target="_blank">
                                                        <button type="button" class="btn btn-primary">
                                                            View Application
                                                        </button>
                                                    </a>
                                                </div>
                                                <div>&nbsp</div>
                                                <div class="panel panel-default">
                                                    <div class="panel-heading"><strong>Applicant Details</strong></div>
                                                    <div class="row">
                                                        <div class="col-xs-12">
                                                            <div class="table-gp">
                                                                <table class="table table-bordered">
                                                                    <tbody>
                                                                    <tr>
                                                                        <td class="col-xs-6" align="right">HCI Code</td>
                                                                        <td class="col-xs-6">-</td>
                                                                    </tr>
                                                                    <tr>
                                                                        <td align="right">HCI Name</td>
                                                                        <td>${applicationViewDto.hciName}</td>
                                                                    </tr>
                                                                    <tr>
                                                                        <td align="right">HCI Address</td>
                                                                        <td>${applicationViewDto.hciAddress}</td>
                                                                    </tr>
                                                                    <tr>
                                                                        <td align="right">Telephone</td>
                                                                        <td>${applicationViewDto.telephone}</td>
                                                                    </tr>
                                                                    <tr>
                                                                        <td align="right">Fax</td>
                                                                        <td>-</td>
                                                                    </tr>
                                                                    </tbody>
                                                                </table>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="tab-pane" id="tabDocuments" role="tabpanel">
                                                <%@include file="/iais/inspectionncList/tabDocuments.jsp"%>
                                            </div>
                                            <div class="${reportClassBelow}" id="tabInspectionReport" role="tabpanel">
                                                <jsp:include page="/iais/report/ao1Report.jsp"></jsp:include>
                                            </div>
                                            <div class="tab-pane" id="tabProcessing" role="tabpanel">
                                                <div class="col-xs-12">
                                                    <div class="table-gp">
                                                        <div class="alert alert-info" role="alert">
                                                            <strong>
                                                                <h4 style="border-bottom: none">Processing Status Update</h4>
                                                            </strong>
                                                        </div>
                                                        <div class="table-gp">
                                                            <iais:section title="">
                                                                <iais:row>
                                                                    <iais:field value="Current Status"/>
                                                                    <iais:value width="6">
                                                                        <p><strong><iais:code code="${insRepDto.currentStatus}"/></strong></p>
                                                                    </iais:value>
                                                                </iais:row>

                                                                <iais:row>
                                                                    <iais:field value="Internal Remarks"/>
                                                                    <iais:value width="6">
                                                                        <textarea style="resize:none" name="processRemarks" cols="65" rows="6"  title="content" MAXLENGTH="8000"><c:out value="${appPremisesRecommendationDto.processRemarks}"/></textarea>
                                                                    </iais:value>
                                                                </iais:row>

                                                                <iais:row>
                                                                    <iais:field value="Processing Decision" required="true"/>
                                                                    <iais:value width="6">
                                                                        <iais:select name="processingDecision" id="processingDecision" options="processingDe" firstOption="Please Select" value="${appPremisesRecommendationDto.processingDecision}"/>
                                                                        <span id="error_submit" class="error-msg" hidden> The field is mandatory.</span>
                                                                    </iais:value>
                                                                </iais:row>
                                                            </iais:section>
                                                            <iais:action style="text-align:right;">
                                                                <button id="submitButton" type="button" class="btn btn-primary" onclick="aoSubmit()">SUBMIT</button>
                                                            </iais:action>
                                                        </div>
                                                        <br/>
                                                        <div class="alert alert-info" role="alert">
                                                            <strong>
                                                                <h4>Processing History</h4>
                                                            </strong>
                                                        </div>
                                                        <div class="row">
                                                            <div class="col-xs-12">
                                                                <div class="table-gp">
                                                                    <table class="table">
                                                                        <thead>
                                                                        <tr>
                                                                            <th>Username</th>
                                                                            <th>Working Group</th>
                                                                            <th>Status Update</th>
                                                                            <th>Remarks</th>
                                                                            <th>Last Updated</th>
                                                                        </tr>
                                                                        </thead>
                                                                        <tbody>
                                                                        <c:forEach
                                                                                items="${applicationViewDto.appPremisesRoutingHistoryDtoList}"
                                                                                var="appPremisesRoutingHistoryDto">
                                                                            <tr>
                                                                                <td>
                                                                                    <p><c:out
                                                                                            value="${appPremisesRoutingHistoryDto.actionby}"></c:out></p>
                                                                                </td>
                                                                                <td>
                                                                                    <p><c:out
                                                                                            value="${appPremisesRoutingHistoryDto.workingGroup}"></c:out></p>
                                                                                </td>
                                                                                <td>
                                                                                    <p><c:out
                                                                                            value="${appPremisesRoutingHistoryDto.processDecision}"></c:out></p>
                                                                                </td>
                                                                                <td>
                                                                                    <p><c:out
                                                                                            value="${appPremisesRoutingHistoryDto.internalRemarks}"></c:out></p>
                                                                                </td>
                                                                                <td>
                                                                                    <p><c:out
                                                                                            value="${appPremisesRoutingHistoryDto.updatedDt}"></c:out></p>
                                                                                </td>
                                                                            </tr>
                                                                        </c:forEach>
                                                                        </tbody>
                                                                    </table>
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
                        </iais:body>
                    </div>
                </div>
            </div>
        </div>
    </div>
</form>
    <%@ include file="../inspectionncList/uploadFile.jsp" %>
</div>
<script>
    function aoSubmit() {
        var s = $("#processingDecision").val();
        if(s=="" || s==null){
            $("#error_submit").show();
        }else if("Approval"== s) {
            $("[name='action_type']").val('approval');
            $("#mainForm").submit();
            $("#error_submit").hide();
        }else if("Reject"==s) {
            $("[name='action_type']").val('back');
            $("#mainForm").submit();
            $("#error_submit").hide();
        }
    }
</script>


