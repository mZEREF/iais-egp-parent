<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais"%>
<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>
<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>
<webui:setLayout name="iais-intranet"/>
<form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
    <%@ include file="/include/formHidden.jsp" %>
    <input type="hidden" name="crud_action_type" value="">
    <input type="hidden" name="crud_action_value" value="">
    <input type="hidden" name="crud_action_additional" value="">
    <div class="main-content">
        <div class="container">
            <br><br>
            <div class="row">
                <div class="col-xs-12">
                    <div class="tab-gp dashboard-tab">
                        <ul class="nav nav-tabs hidden-xs hidden-sm" role="tablist">
                            <li class="active" role="presentation"><a href="#tabInfo" aria-controls="tabInfo" role="tab" data-toggle="tab">Info</a></li>
                            <li class="complete" role="presentation"><a href="#tabDocuments" aria-controls="tabDocuments" role="tab"
                                                                        data-toggle="tab">Documents</a></li>
                            <li class="complete" role="presentation"><a href="#tabInspectionReport" aria-controls="tabProcessing" role="tab"
                                                                        data-toggle="tab">Inspection Report</a></li>
                            <li class="complete" role="presentation"><a href="#tabProcessing" aria-controls="tabProcessing" role="tab"
                                                                        data-toggle="tab">Processing</a></li>

                        </ul>
                        <div class="tab-nav-mobile visible-xs visible-sm">
                            <div class="swiper-wrapper" role="tablist">
                                <div class="swiper-slide"><a href="#tabInfo" aria-controls="tabInfo" role="tab" data-toggle="tab">Info</a></div>
                                <div class="swiper-slide"><a href="#tabDocuments" aria-controls="tabDocuments" role="tab" data-toggle="tab">Documents</a></div>
                                <div class="swiper-slide"><a href="#tabInspectionReport" aria-controls="tabInspectionReport" role="tab" data-toggle="tab">Inspection Report</a></div>
                                <div class="swiper-slide"><a href="#tabProcessing" aria-controls="tabProcessing" role="tab" data-toggle="tab">Processing</a></div>
                            </div>
                            <div class="swiper-button-prev"></div>
                            <div class="swiper-button-next"></div>
                        </div>
                        <div class="tab-content">
                            <div class="tab-pane active" id="tabInfo" role="tabpanel">

                                <div class="panel panel-default">
                                    <!-- Default panel contents -->
                                    <div class="panel-heading"><strong>Submission Details</strong></div>
                                    <div class="row">
                                        <div class="col-xs-12">
                                            <div class="table-gp">
                                                <table class="table table-bordered">
                                                    <tbody>
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
                                                        <td align="right">Application Type</td>
                                                        <td>${applicationViewDto.applicationType}</td>
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
                                    <a href="/hcsa-licence-web/eservice/INTRANET/LicenceBEViewService?appId=${applicationViewDto.applicationDto.id}" target="_blank">
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
                                <div class="alert alert-info" role="alert"><strong>
                                    <h4>Supporting Document</h4>
                                </strong></div>
                                <div id="u8522_text" class="text ">
                                    <p><span>These are documents uploaded by the applicant or an officer on behalf of the applicant. Listed
												documents are those defined for this digital service only.</span></p>
                                </div>
                                <div class="row">
                                    <div class="col-xs-12">
                                        <div class="table-gp">
                                            <table class="table">
                                                <thead>
                                                <tr>
                                                    <th>Document</th>
                                                    <th>File</th>
                                                    <th>Size</th>
                                                    <th>Submitted By</th>
                                                    <th>Date Submitted</th>
                                                </tr>
                                                </thead>

                                                <tbody>
                                                <c:forEach items="${applicationViewDto.appSupDocDtoList}"
                                                           var="appSupDocDto">
                                                    <tr>
                                                        <td>
                                                            <p><c:out value="${appSupDocDto.file}"></c:out></p>
                                                        </td>
                                                        <td>
                                                            <p><a href="#"><c:out value="${appSupDocDto.document}"></c:out></a></p>
                                                        </td>
                                                        <td>
                                                            <p><c:out value="${appSupDocDto.size}"></c:out></p>
                                                        </td>
                                                        <td>
                                                            <p><c:out value="${appSupDocDto.submittedBy}"></c:out></p>
                                                        </td>
                                                        <td>
                                                            <p><c:out value="${appSupDocDto.dateSubmitted}"></c:out></p>
                                                        </td>
                                                    </tr>
                                                </c:forEach>
                                                </tbody>

                                            </table>
                                            <div class="alert alert-info" role="alert"><strong>
                                                <h4>Internal Document</h4>
                                            </strong></div>
                                            <div class="text ">
                                                <p><span>These are documents uploaded by an agency officer to support back office processing.</span>
                                                </p>
                                            </div>
                                            <table class="table">
                                                <thead>
                                                <tr>
                                                    <th>Document</th>
                                                    <th>File</th>
                                                    <th>Size</th>
                                                    <th>Submitted By</th>
                                                    <th>Date Submitted</th>
                                                    <th>Action</th>
                                                </tr>
                                                </thead>
                                                <tbody>
                                                <tr>
                                                    <td colspan="5" align="center">
                                                        <p>No record found.</p>
                                                    </td>
                                                </tr>
                                                </tbody>
                                            </table>
                                        </div>
                                    </div>
                                </div>

                            </div>
                            <div class="tab-pane" id="tabInspectionReport" role="tabpanel">
                                <jsp:include page="/iais/auditReport/inspReport.jsp"></jsp:include>
                            </div>
                            <div class="tab-pane" id="tabProcessing" role="tabpanel">
                                <div class="col-xs-12">
                                    <div class="table-gp">
                                        <div class="alert alert-info" role="alert">
                                            <strong>
                                                <h4>Processing Status Update</h4>
                                            </strong>
                                        </div>
                                        <div class="table-gp">
                                            <table class="table">
                                                <tr>
                                                    <td class="col-xs-4">
                                                        <p>Current Status:</p>
                                                    </td>
                                                    <td class="col-xs-8">
                                                        <p> <iais:code code="${insRepDto.currentStatus}"></iais:code></p>
                                                    </td>
                                                </tr>
                                                <tr height="1">
                                                    <td class="col-xs-2" >
                                                        <p >
                                                            Remarks:
                                                        </p>
                                                    </td>
                                                    <td>
                                                        <div class="col-sm-9">
                                                            <p><textarea name="processRemarks" cols="50" rows="6"  title="content" MAXLENGTH="8000"></textarea></p>
                                                        </div>
                                                    </td>
                                                </tr>

                                                <tr>
                                                    <td class="col-xs-4">
                                                        <p>Processing Decision:<b style="color:#ff0000;">*</b></p>
                                                    </td>
                                                    <td class="col-xs-4">
                                                        <iais:select name="processingDecision" options="processingDe" firstOption="Please select" value="${appPremisesRecommendationDto.processingDecision}"/>
                                                        <span id="error_processingDecision" name="iaisErrorMsg" class="error-msg"></span>
                                                    </td>
                                                    <td class="col-xs-4"></td>
                                                </tr>
                                            </table>
                                            <div align="center">
                                                <button id="submitButton" type="button" class="btn btn-primary" onclick="insRepsubmit()">
                                                    SUBMIT
                                                </button>
                                            </div>
                                        </div>
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
        </div>
    </div>
</form>



