<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts" %>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>
<script src="<%=IaisEGPConstant.CSS_ROOT+IaisEGPConstant.COMMON_CSS_ROOT%>js/tinymce/tinymce.min.js"></script>
<script src="<%=IaisEGPConstant.CSS_ROOT+IaisEGPConstant.COMMON_CSS_ROOT%>js/initTinyMce.js"></script>
<jsp:useBean id="insEmailDto" scope="session" type="com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionEmailTemplateDto"/>
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
            <div class="row">
                <div class="col-xs-12">
                    <div class="tab-gp dashboard-tab">
                        <br><br><br>
                        <ul class="nav nav-tabs hidden-xs hidden-sm" role="tablist">
                            <li class="active" role="presentation"><a href="#tabInfo" aria-controls="tabInfo" role="tab" data-toggle="tab">Info</a></li>
                            <li class="complete" role="presentation"><a href="#tabDocuments" aria-controls="tabDocuments" role="tab"
                                                                        data-toggle="tab">Documents</a></li>
                            <li class="complete" role="presentation"><a href="#tabPayment" aria-controls="tabPayment" role="tab"
                                                                        data-toggle="tab">Payment</a></li>
                            <li class="complete" role="presentation"><a href="#tabInspection" aria-controls="tabInspection" role="tab"
                                                                        data-toggle="tab">Inspection</a></li>
                            <li class="incomplete" role="presentation"><a href="#tabCheckList" aria-controls="CheckList" role="tab"
                                                                          data-toggle="tab">CheckList</a></li>
                            <li class="complete" role="presentation"><a href="#tabProcessing" aria-controls="tabProcessing" role="tab"
                                                                        data-toggle="tab">Processing</a></li>
                        </ul>
                        <div class="tab-nav-mobile visible-xs visible-sm">
                            <div class="swiper-wrapper" role="tablist">
                                <div class="swiper-slide"><a href="#tabInfo" aria-controls="tabInfo" role="tab" data-toggle="tab">Info</a></div>
                                <div class="swiper-slide"><a href="#tabDocuments" aria-controls="tabDocuments" role="tab" data-toggle="tab">Documents</a></div>
                                <div class="swiper-slide"><a href="#tabPayment" aria-controls="tabPayment" role="tab" data-toggle="tab">Payment</a></div>
                                <div class="swiper-slide"><a href="#tabInspection" aria-controls="tabInspection" role="tab" data-toggle="tab">Inspection</a></div>
                                <div class="swiper-slide"><a href="#tabCheckList" aria-controls="tabCheckList" role="tab" data-toggle="tab">CheckList</a></div>
                                <div class="swiper-slide"><a href="#tabProcessing" aria-controls="tabProcessing" role="tab" data-toggle="tab">Processing</a></div>
                            </div>
                            <div class="swiper-button-prev"></div>
                            <div class="swiper-button-next"></div>
                        </div>

                        <div class="tab-content">
                            <div class="tab-pane" id="tabInfo" role="tabpanel">

                                <div class="panel panel-default">
                                    <!-- Default panel contents -->
                                    <div class="panel-heading"><b>Submission Details</b></div>
                                    <div class="row">
                                        <div class="col-xs-12">
                                            <div class="table-gp">
                                                <table class="table table-bordered">
                                                    <tbody>
                                                    <tr>
                                                        <td class="col-xs-6" align="right">Application No. (Overall)</td>
                                                        <td class="col-xs-6">${applicationViewDto.applicationDto.applicationNo}</td>
                                                    </tr>
                                                    <tr>
                                                        <td align="right">Application No.</td>
                                                        <td>${applicationViewDto.applicationNoOverAll}</td>
                                                    </tr>
                                                    <tr>
                                                        <td align="right">Application Type</td>
                                                        <td>${applicationViewDto.applicationDto.applicationType}</td>
                                                    </tr>
                                                    <tr>
                                                        <td align="right">Service Type</td>
                                                        <td>${applicationViewDto.applicationDto.serviceId}</td>
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
                                    <button type="button" class="btn btn-primary">
                                        View Application
                                    </button>
                                </div>
                                <div>&nbsp</div>
                                <div class="panel panel-default">
                                    <div class="panel-heading"><b>Applicant Details</b></div>
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
                                                        <td align="right">HCI ADDRESS</td>
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
                                <div class="alert alert-info" role="alert"><b>
                                    <h4>Supporting Document</h4>
                                </b></div>
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
                                                <c:forEach items="${applicationViewDto.appSupDocDtoList}" var="appSupDocDto">
                                                    <tr>
                                                        <td>
                                                            <p><c:out value="${appSupDocDto.document}"></c:out></p>
                                                        </td>
                                                        <td>
                                                            <p><a href="#"><c:out value="${appSupDocDto.file}"></c:out></a></p>
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
                                            <div  class="text ">
                                                <p><span>These are documents uploaded by an agency officer to support back office processing.</span></p>
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
                                                    <td colspan="6" align="center">
                                                        <p>No record found.</p>
                                                    </td>
                                                </tr>
                                                </tbody>
                                            </table>
                                        </div>
                                    </div>
                                </div>

                            </div>
                            <div class="tab-pane" id="tabPayment" role="tabpanel">
                                <div class="alert alert-info" role="alert"><strong>
                                    <h4>Payment Details</h4>
                                </strong></div>
                                <div class="row">
                                    <div class="col-xs-12">
                                        <div class="table-gp">
                                            <table class="table">
                                                <thead>
                                                <tr>
                                                    <th>Payment</th>
                                                    <th>Amount</th>
                                                    <th>Date</th>
                                                    <th>Status</th>
                                                    <th>Reference No.</th>
                                                    <th>Payment Type</th>
                                                </tr>
                                                </thead>
                                                <tbody>
                                                <tr>
                                                    <td>
                                                        <p>New Licence Application</p>
                                                    </td>
                                                    <td>
                                                        <p>S$400.00</p>
                                                    </td>
                                                    <td>
                                                        <p>12-Dec-2018</p>
                                                    </td>
                                                    <td>
                                                        <p>success</p>
                                                    </td>
                                                    <td>
                                                        <p>TRANS-201812000013</p>
                                                    </td>
                                                    <td>
                                                        <p>Credit Card</p>
                                                    </td>
                                                </tr>
                                                </tbody>
                                            </table>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="tab-pane" id="tabInspection" role="tabpanel">
                                <div class="alert alert-info" role="alert">
                                    <strong>
                                        <h4>Inspection Findings</h4>
                                    </strong>
                                </div>
                                <div class="text ">
                                    <p><span><strong>Part I: Inspection Checklist</strong></span></p>
                                </div>
                                <div class="row">
                                    <div class="col-xs-12">
                                        <div class="table-gp">
                                            <table class="table">
                                                <thead>
                                                <tr>
                                                    <th class="col-xs-2"><span>Checklist</span></th>
                                                    <th class="col-xs-10"><span>Interviewed</span></th>
                                                </tr>
                                                </thead>
                                                <tbody>
                                                <tr>
                                                    <td>
                                                        <p>Radiological Service</p>
                                                    </td>
                                                    <td>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td>
                                                        <p>Angiography</p>
                                                    </td>
                                                    <td>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td>
                                                        <p>Mammography</p>
                                                    </td>
                                                    <td>
                                                    </td>
                                                </tr>
                                                </tbody>
                                            </table>
                                            <div class="text ">
                                                <p><span><strong>Part II: Findings</strong></span></p>
                                            </div>
                                            <table class="table">
                                                <tr>
                                                    <td class="col-xs-2">
                                                        <p>Licence Type</p>
                                                    </td>
                                                    <td class="col-xs-10">
                                                        <p>Radiological Service</p>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td>
                                                        <p>Findings/Remarks</p>
                                                    </td>
                                                    <td>
                                                        <p>The clinic offers in-house laboratory services</p>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td>
                                                        <p>Others</p>
                                                    </td>
                                                    <td>
                                                        <p>Refer to ack. no. 180911007711 for x-ray laboratory licence (ultrasound only). At the time of
                                                            inspection, LIA Br was still awaiting clarification on the deputy manager's qualifications and was
                                                            unable to complete the inspection of the ultrasound facility as the sonographer / radiographer and
                                                            radiologist were not on-site. The ultrasound room, N2 licence (N2/06421/001) and preventive maintenance
                                                            records for the ultrasound machine were in place. The ultrasound procedure is limited to abdominal area
                                                            only</p>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td>
                                                        <p>Status</p>
                                                    </td>
                                                    <td>
                                                        <p>Full Compliance</p>
                                                    </td>
                                                </tr>
                                            </table>
                                            <div class="text ">
                                                <p><span><strong>Part III: Inspectors</strong></span></p>
                                            </div>
                                            <table class="table">
                                                <tr>
                                                    <td class="col-xs-2">
                                                        <p>Inspected By</p>
                                                    </td>
                                                    <td class="col-xs-10">
                                                        <p>Jenny</p>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td>
                                                        <p>Other Inspection Officer</p>
                                                    </td>
                                                    <td>
                                                        <p>-</p>
                                                    </td>
                                                </tr>
                                            </table>
                                            <div class="text ">
                                                <p><span><strong>Part IV: Report</strong></span></p>
                                            </div>
                                            <table class="table">
                                                <tr>
                                                    <td class="col-xs-2">
                                                        <p>Reported By</p>
                                                    </td>
                                                    <td class="col-xs-10">
                                                        <p>Jenny</p>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td>
                                                        <p>Report Noted By</p>
                                                    </td>
                                                    <td>
                                                        <p>Steven</p>
                                                    </td>
                                                </tr>
                                            </table>
                                        </div>
                                    </div>
                                </div>
                                <div class="alert alert-info" role="alert">
                                    <strong>
                                        <h4>Recommendations</h4>
                                    </strong>
                                </div>
                                <div class="row">
                                    <div class="col-xs-12">
                                        <div class="table-gp">
                                            <table class="table">
                                                <tr>
                                                    <td class="col-xs-2">
                                                        <p>Recommendation</p>
                                                    </td>
                                                    <td class="col-xs-10">
                                                        <p>2 Years</p>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td>
                                                        <p>Other Remarks</p>
                                                    </td>
                                                    <td>
                                                        <p>-</p>
                                                    </td>
                                                </tr>
                                            </table>
                                        </div>
                                    </div>
                                </div>
                                <div class="alert alert-info" role="alert">
                                    <strong>
                                        <h4>Follow up actions</h4>
                                    </strong>
                                </div>
                                <div class="row">
                                    <div class="col-xs-12">
                                        <div class="table-gp">
                                            <table class="table">
                                                <tr>
                                                    <td class="col-xs-2">
                                                        <p>Follow up actions</p>
                                                    </td>
                                                    <td class="col-xs-10">
                                                        <p>-</p>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td>
                                                        <p>Other</p>
                                                    </td>
                                                    <td>
                                                        <p>-</p>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td>
                                                        <p>Rectification</p>
                                                    </td>
                                                    <td>
                                                        <p>-</p>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td>
                                                        <p>Other</p>
                                                    </td>
                                                    <td>
                                                        <p>-</p>
                                                    </td>
                                                </tr>
                                            </table>
                                        </div>
                                    </div>
                                </div>
                                <div class="alert alert-info" role="alert">
                                    <strong>
                                        <h4>Rectification</h4>
                                    </strong>
                                </div>
                                <div class="row">
                                    <div class="col-xs-12">
                                        <div class="table-gp">
                                            <table class="table">
                                                <tr>
                                                    <td class="col-xs-2">
                                                        <p>Rectifications</p>
                                                    </td>
                                                    <td class="col-xs-10">
                                                        <p>N.A</p>
                                                    </td>
                                                </tr>
                                            </table>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="tab-pane" id="tabCheckList" role="tabpanel">
                                <div class="alert alert-info" role="alert">
                                    <strong>
                                        <h4>Processing Status Update</h4>
                                    </strong>
                                </div>
                                <form method="post" action=<%=process.runtime.continueURL()%>>
                                    <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
                                    <div class="row">
                                        <div class="col-xs-12">
                                            <div class="table-gp">
                                                <table class="table">
                                                    <tr>
                                                        <td class="col-xs-4"><p>Current Status:</p></td>
                                                        <td class="col-xs-8"><p>${applicationViewDto.currentStatus}</p></td>
                                                    </tr>
                                                    <tr>
                                                        <td><span>Internal Remarks(</span><span
                                                                style="color: red">*</span><span>):</span></td>
                                                        <td>
                                                            <div class="input-group">
                                                                <div class="ax_default text_area">
                                                                    <textarea id="internalRemarksId"
                                                                              name="internalRemarks" cols="70"
                                                                              rows="7"></textarea>
                                                                </div>
                                                            </div>
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <td>
                                                            <span>Processing Decision(</span><span
                                                                style="color: red">*</span><span>):</span></span>
                                                        </td>
                                                        <td>
                                                            <select name="nextStage" class="table-select">
                                                                <option>---select---</option>
                                                                <option value="VERIFIED">Verified</option>
                                                                <option value="ROLLBACK">Roll back</option>
                                                            </select>
                                                        </td>
                                                    </tr>
                                                    <tr id="verifiedDropdown" class="hidden">
                                                        <td>
                                                            <span>Verified</span>
                                                        </td>
                                                        <td>
                                                            <select name="verified" class="table-select">
                                                                <option>---select---</option>
                                                                <c:forEach items="${applicationViewDto.verified}" var="verified">
                                                                    <option value="${verified.key}">${verified.value}</option>
                                                                </c:forEach>
                                                            </select>
                                                        </td>
                                                    </tr>
                                                    <tr id="lienceStartDate" class="hidden">
                                                        <td>
                                                            <span>Lience Start Date</span>
                                                        </td>
                                                        <td>
                                                            <iais:datePicker id = "lienceStartDate" name = "tuc" value=""></iais:datePicker>
                                                        </td>
                                                    </tr>
                                                    <tr id="rollBack" class="hidden">
                                                        <td>
                                                            <span>Roll Back:</span>
                                                        </td>
                                                        <td>
                                                            <select name="rollBack" class="table-select">
                                                                <option>Please select</option>
                                                                <c:forEach items="${applicationViewDto.rollBack}" var="rollBack">
                                                                    <option value="${rollBack.value}">${rollBack.key}</option>
                                                                </c:forEach>
                                                            </select>
                                                        </td>
                                                    </tr>
                                                </table>
                                                <div align="center">
                                                    <button type="submit" class="btn btn-primary">Submit</button>
                                                </div>
                                                <div>&nbsp;</div>
                                            </div>
                                        </div>
                                    </div>
                                </form>
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
                                                <tr>
                                                    <td>
                                                        <p>Tan Ah Ming (S1234567D)</p>
                                                    </td>
                                                    <td>
                                                        <p>Internet User</p>
                                                    </td>
                                                    <td>
                                                        <p>Submission</p>
                                                    </td>
                                                    <td>
                                                        <p>-</p>
                                                    </td>
                                                    <td>
                                                        <p>16-Oct-2018 01:20:13 PM</p>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td>
                                                        <p>Mr Tan</p>
                                                    </td>
                                                    <td>
                                                        <p>Internet User</p>
                                                    </td>
                                                    <td>
                                                        <p>Pending Admin Screen</p>
                                                    </td>
                                                    <td>
                                                        <p>-</p>
                                                    </td>
                                                    <td>
                                                        <p>16-Oct-2018 01:20:13 PM</p>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td>
                                                        <p></p>
                                                    </td>
                                                    <td>
                                                        <p></p>
                                                    </td>
                                                    <td>
                                                        <p>Verified</p>
                                                    </td>
                                                    <td>
                                                        <p>-</p>
                                                    </td>
                                                    <td>
                                                        <p>16-Oct-2018 01:20:13 PM</p>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td>
                                                        <p>Ms Lim</p>
                                                    </td>
                                                    <td>
                                                        <p>Internet User</p>
                                                    </td>
                                                    <td>
                                                        <p>Pending Professional Screening</p>
                                                    </td>
                                                    <td>
                                                        <p>-</p>
                                                    </td>
                                                    <td>
                                                        <p>16-Oct-2018 01:20:13 PM</p>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td>
                                                        <p></p>
                                                    </td>
                                                    <td>
                                                        <p></p>
                                                    </td>
                                                    <td>
                                                        <p>Verified</p>
                                                    </td>
                                                    <td>
                                                        <p>-</p>
                                                    </td>
                                                    <td>
                                                        <p>16-Oct-2018 01:20:13 PM</p>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td>
                                                        <p>Mrs Sim</p>
                                                    </td>
                                                    <td>
                                                        <p>Internet User</p>
                                                    </td>
                                                    <td>
                                                        <p>Pending Inspection</p>
                                                    </td>
                                                    <td>
                                                        <p>-</p>
                                                    </td>
                                                    <td>
                                                        <p>16-Oct-2018 01:20:13 PM</p>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td>
                                                        <p></p>
                                                    </td>
                                                    <td>
                                                        <p></p>
                                                    </td>
                                                    <td>
                                                        <p>Inspection Conducted</p>
                                                    </td>
                                                    <td>
                                                        <p>Recommend for Approval</p>
                                                    </td>
                                                    <td>
                                                        <p>16-Oct-2018 01:20:13 PM</p>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td>
                                                        <p>Mr Ong</p>
                                                    </td>
                                                    <td>
                                                        <p>Internet User</p>
                                                    </td>
                                                    <td>
                                                        <p>Pending Approval Officer 1</p>
                                                    </td>
                                                    <td>
                                                        <p>-</p>
                                                    </td>
                                                    <td>
                                                        <p>16-Oct-2018 01:20:13 PM</p>
                                                    </td>
                                                </tr>
                                                </tbody>
                                            </table>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="tab-pane active" id="tabProcessing" role="tabpanel">
                                <table class="table">
                                    <tbody>
                                    <tr height="1">
                                        <td class="col-xs-2" >
                                            <p >
                                                subject:
                                            </p>
                                        </td>
                                        <td>
                                            <div class="col-sm-9">
                                                <p><input name="subject" type="text" id="subject" title="subject"  readonly value="${insEmailDto.subject}"></p>
                                            </div>
                                        </td>

                                    </tr>
                                    <tr height="1">
                                        <td class="col-xs-2" >
                                            <p >
                                                content:
                                            </p>
                                        </td>
                                        <td>
                                            <div class="col-sm-9">
                                                <p><textarea name="messageContent" cols="108" rows="50" class="wenbenkuang" id="htmlEditroArea" title="content"  >${insEmailDto.messageContent}</textarea></p>
                                            </div>
                                        </td>
                                    </tr>
                                    <tr height="1">
                                        <td class="col-xs-2" >
                                            <p >
                                                Processing Decision:
                                            </p>
                                        </td>
                                        <td>
                                            <div class="col-sm-9">
                                                <select id="decision-email" name="decision">
                                                    <option>Select</option>
                                                    <c:forEach items="${appTypeOption}" var="decision">
                                                        <option  value="${decision.value}">${decision.text}</option>
                                                    </c:forEach>
                                                </select>
                                            </div>
                                        </td>
                                    </tr>
                                    <tr height="1">
                                        <td class="col-xs-2" >
                                            <p >
                                                need revise
                                            </p>
                                        </td>
                                        <td>
                                            <div class="col-sm-9">
                                                <c:forEach items="${appIds}" var="revise" varStatus="index">
                                                    <input type="checkbox"  name="revise${index.index+1}" value="${revise}">NC/BP ${index.index+1}</input>
                                                </c:forEach>

                                            </div>
                                        </td>
                                    </tr>
                                    </tbody>
                                </table>
                                <p class="text-right text-center-mobile">

                                    <iais:action>
                                        <button type="button" class="search btn" onclick="javascript:doSend();">Submit</button>
                                    </iais:action>
                                    <iais:action>
                                        <button type="button" class="search btn" onclick="javascript:doPreview();">Preview</button>
                                    </iais:action>
                                </p>


                            </div>

                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</form>


<script type="text/javascript">
    function doPreview(){
        SOP.Crud.cfxSubmit("mainForm", "preview");
    }

    function doSend(){
        SOP.Crud.cfxSubmit("mainForm", "send");
    }


</script>



