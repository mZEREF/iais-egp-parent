<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts" %>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais"   prefix="iais"%>
<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-intranet"/>
<form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>

    <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
    <input type="hidden" name="iaisErrorFlag" id="iaisErrorFlag"/>
    <input type="hidden" name="crud_action_additional" id="crud_action_additional"/>

    <div class="main-content">
        <div class="container">
            <div class="row">
                <br/>
                <br/>
                <br/>
                <div class="col-xs-12">
                    <div class="tab-gp dashboard-tab">
                        <ul class="nav nav-tabs hidden-xs hidden-sm" role="tablist">
                            <li class="active" role="presentation"><a href="#tabInfo" aria-controls="tabInfo" role="tab"
                                                                      data-toggle="tab">Info</a></li>
                            <li class="complete" role="presentation"><a href="#tabDocuments"
                                                                        aria-controls="tabDocuments" role="tab"
                                                                        data-toggle="tab">Documents</a></li>
                            <li id="ApplicationViewInspection" class="complete" role="presentation" style="display: block"><a href="#tabInspection"
                                                                        aria-controls="tabInspection" role="tab"
                                                                        data-toggle="tab">Inspection</a></li>
                            <li class="incomplete" role="presentation"><a href="#tabProcessing"
                                                                          aria-controls="tabProcessing" role="tab"
                                                                          data-toggle="tab">Processing</a></li>
                        </ul>
                        <div class="tab-nav-mobile visible-xs visible-sm">
                            <div class="swiper-wrapper" role="tablist">
                                <div class="swiper-slide"><a href="#tabInfo" aria-controls="tabInfo" role="tab"
                                                             data-toggle="tab">Info</a></div>
                                <div class="swiper-slide"><a href="#tabDocuments" aria-controls="tabDocuments"
                                                             role="tab" data-toggle="tab">Documents</a></div>

                                <div class="swiper-slide"><a href="#tabInspection" aria-controls="tabInspection"
                                                             role="tab" data-toggle="tab">Inspection</a></div>
                                <div class="swiper-slide"><a href="#tabProcessing" aria-controls="tabProcessing"
                                                             role="tab" data-toggle="tab">Processing</a></div>
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
                                                        <td align="right">Clinical Laboratory</td>
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
                                                        <p>Refer to ack. no. 180911007711 for x-ray laboratory licence
                                                            (ultrasound only). At the time of
                                                            inspection, LIA Br was still awaiting clarification on the
                                                            deputy manager's qualifications and was
                                                            unable to complete the inspection of the ultrasound facility
                                                            as the sonographer / radiographer and
                                                            radiologist were not on-site. The ultrasound room, N2
                                                            licence (N2/06421/001) and preventive maintenance
                                                            records for the ultrasound machine were in place. The
                                                            ultrasound procedure is limited to abdominal area
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
                            <div class="tab-pane" id="tabProcessing" role="tabpanel">
                                <div class="alert alert-info" role="alert">
                                    <strong>
                                        <h4>Processing Status Update</h4>
                                    </strong>
                                </div>
                                <form method="post" action=<%=process.runtime.continueURL()%>>
                                    <input type="hidden" name="sopEngineTabRef"
                                           value="<%=process.rtStatus.getTabRef()%>">
                                    <div class="row">
                                        <div class="col-xs-12">
                                            <div class="table-gp">
                                                <table class="table">
                                                    <tr>
                                                        <td class="col-xs-4"><p>Current Status:</p></td>
                                                        <td class="col-xs-8"><p>${applicationViewDto.currentStatus}</p>
                                                        </td>
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
                                                    <tr id="processingDecision">
                                                        <td>
                                                            <span>Processing Decision(</span><span
                                                                style="color: red">*</span><span>):</span></span>
                                                        </td>
                                                        <td>
                                                            <select name="nextStage" class="table-select processing-Decision">
                                                                <option>---select---</option>
                                                                <option value="VERIFIED">Verified</option>
                                                                <option value="ROLLBACK">Roll back</option>
                                                            </select>
                                                        </td>
                                                    </tr>
                                                    <tr id="replytr" class="hidden">
                                                        <td>
                                                            <span>Processing Decision(</span><span
                                                                style="color: red">*</span><span>):</span></span>
                                                        </td>
                                                        <td>
                                                            <select name="nextStageReply" class="table-select processing-Decision">
                                                                <option>---select---</option>
                                                                <option value="PROCREP">Give Clarification</option>
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
                                                    <tr id="rollBackDropdown" class="hidden">
                                                        <td>
                                                            <span>Roll Back</span>
                                                        </td>
                                                        <td>
                                                            <select name="rollBack" class="table-select">
                                                                <option>---select---</option>
                                                                <c:forEach items="${applicationViewDto.rollBack}" var="rollBack">
                                                                    <option value="${rollBack.value}">${rollBack.key}</option>
                                                                </c:forEach>
                                                            </select>
                                                        </td>
                                                    </tr>
                                                    <tr id="lienceStartDate">
                                                        <td>
                                                            <span>Lience Start Date</span>
                                                        </td>
                                                        <td>
                                                            <iais:datePicker id = "lienceStartDate" name = "tuc" value=""></iais:datePicker>
                                                        </td>
                                                    </tr>
                                                    <tr id="recomedationDropdown" class="hidden">
                                                        <td>
                                                            <span>Recommendation:</span>
                                                        </td>
                                                        <td>
                                                            <select name="recomedation" class="table-select recomedation-type">
                                                                <option>---select---</option>
                                                                   <c:forEach items="${applicationViewDto.recomeDation}" var="recomedation">
                                                                       <option><c:out value="${recomedation}"></c:out></option>
                                                                   </c:forEach>
                                                                <option>reject</option>
                                                            </select>
                                                        </td>
                                                    </tr>
                                                    <tr id="rfiSelect">
                                                        <td>
                                                            <span>Sections Allowed for Change:</span>
                                                        </td>
                                                        <td >
                                                            <p id = "selectDetail"></p>
                                                        </td>
                                                    </tr>
                                                </table>
                                                <div align="center">
                                                    <button id="submitButton" type="button" class="btn btn-primary">
                                                        Submit
                                                    </button>
                                                </div>
                                                <div>&nbsp</div>
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
</form>


<script type="text/javascript">
    $(document).ready(function(){
        if('${taskDto.taskKey}' == '12848A70-820B-EA11-BE7D-000C29F371DC'||'${taskDto.taskKey}' == '13848A70-820B-EA11-BE7D-000C29F371DC'){
            $('#ApplicationViewInspection').css('display','none');
            $('#recomedationDropdown').removeClass('hidden');
        }
        if('${applicationViewDto.applicationDto.status}' == 'APST000'){
            $('#processingDecision').addClass('hidden');
            $('#recomedationDropdown').addClass('hidden');
            $('#replytr').removeClass('hidden');
            $('#lienceStartDate').addClass('hidden');
        }
        $('#rfiSelect').hide();
    });




    $("#submitButton").click(function () {
        showWaiting();
        var textarea = $("#internalRemarksId").val();
        if (textarea == "") {
            $('#notNull').remove();
            $("#internalRemarksId").after("<span id='notNull' style='color: red;'>*NOT NULL!</span>");
            dismissWaiting();
            return false;
        }

        if('${applicationViewDto.applicationDto.status}' == 'APST000'){
            var nextStageReply= $("[name='nextStageReply']").val();
            if(nextStageReply == "---select---"){
                $('#NSRnotNull').remove();
                $("[name='nextStageReply']").after("<span id='NSRnotNull' style='color: red;'>*PLEASE SELECT!</span>");
                dismissWaiting();
                return false;
            }else{
                document.getElementById("mainForm").submit();
            }
        }

        var selectValue = $("[name='nextStage']").val();
        if(selectValue == "---select---"){
            $('#NSnotNull').remove();
            $("[name='nextStage']").after("<span id='NSnotNull' style='color: red;'>*PLEASE SELECT!</span>");
            dismissWaiting();
            return false;
        }
        if(selectValue == "VERIFIED"){
            var verified=$("[name='verified']").val();
            if(verified == "---select---"){
                $('#VnotNull').remove();
                $("[name='verified']").after("<span id='VnotNull' style='color: red;'>*PLEASE SELECT!</span>");
                dismissWaiting();
                return false;
            }
        }else if(selectValue == "ROLLBACK"){
            var rollBack=$("[name='rollBack']").val();
            if(rollBack == "---select---"){
                $('#BnotNull').remove();
                $("[name='rollBack']").after("<span id='BnotNull' style='color: red;'>*PLEASE SELECT!</span>");
                dismissWaiting();
                return false;
            }
        }
        document.getElementById("mainForm").submit();
        $("#submitButton").attr("disabled",true);
    });





    $("[name='nextStage']").change(function selectChange() {
        var selectValue = $("[name='nextStage']").val();
        if (selectValue == "VERIFIED") {
            $('#verifiedDropdown').removeClass('hidden');
            $('#rollBackDropdown').addClass('hidden');
        }else if(selectValue == "ROLLBACK"){
            $('#rollBackDropdown').removeClass('hidden');
            $('#verifiedDropdown').addClass('hidden');
        }else{
            $('#rollBackDropdown').addClass('hidden');
            $('#verifiedDropdown').addClass('hidden');

        }
    });
    $("[name='verified']").change(function selectChange() {
        var selectValue = $("[name='verified']").val();
        if (selectValue == "PROCRFI") {
            showPopupWindow('/hcsa-licence-web/eservice/INTRANET/LicenceBEViewService');
        }else{
            $('#rfiSelect').hide();
        }
    });


    $('#verifiedDropdown').change(function verifiedChange() {
        //var verified= $("[name='verified']").val();
        // if(verified=="PROCLSD") {
        //     $('#lienceStartDate').removeClass('hidden');
        // }else{
        //     $('#lienceStartDate').addClass('hidden');
        // }
    });
    function showWaiting() {
        $.blockUI({message: '<div style="padding:3px;">We are processing your request now, please do not click the Back or Refresh buttons in the browser.</div>',
            css: {width: '25%', border: '1px solid #aaa'},
            overlayCSS: {opacity: 0.2}});
    }
    function dismissWaiting() {
        $.unblockUI();
    }

    function showPopupWindow(url) {
        showPopupWindow(url,'N','popupWindow');
    }

    function showPopupWindow(url,wName) {
        showPopupWindow(url,'N',wName);
    }

    function showPopupWindow(url,full,wName) {
        showWaiting();

        var w, h;
        w = $(window).width();
        h = $(window).height();
        var popW = 980, popH = h + 40;
        if (full == 'Y') {
            popW = w;
        }
        var leftPos = (w - popW) / 2, topPos = (h - popH) / 2;
        var params = "scrollbars=yes,location=no,resizable=yes,width=" + popW + ",height=" + popH + ",left=" + leftPos + ",top="+topPos;
        if (wName == "" || wName == undefined) {
            wName = "popupWindow";
        }
        var emsTabId = $('#emsStoredTabId').val();
        if (emsTabId != null && emsTabId != "") {
            if (url.indexOf("?") >= 0) {
                url += "&emsStoredTabId=" + emsTabId;
            } else {
                url += "?emsStoredTabId=" + emsTabId;
            }
        }
        var popupWin = window.open(url, wName, params);
        if (window.focus) {
            popupWin.focus();
        }

        dismissWaiting();
        return false;
    }

</script>



