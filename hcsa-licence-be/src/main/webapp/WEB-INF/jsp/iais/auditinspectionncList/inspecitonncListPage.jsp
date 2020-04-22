<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts" %>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://www.ecq.com/iais"   prefix="iais"%>
<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>
<webui:setLayout name="iais-intranet"/>
<form method="post" id="mainForm"  enctype="multipart/form-data"  action=<%=process.runtime.continueURL()%>>
    <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
    <br>
    <br>
    <br>
    <br>
    <br>
    <input type="hidden" name="paramController" id="paramController" value="com.ecquaria.cloud.moh.iais.action.FillupChklistDelegator"/>
    <input type="hidden" name="valEntity" id="valEntity" value="com.ecquaria.cloud.moh.iais.dto.CheckListVadlidateDto"/>
    <input type="hidden" name="valProfiles" id="valProfiles" value=""/>

    <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
    <input type="hidden" name="crud_action_type" value="">
    <input type="hidden" name="crud_action_value" value="">
    <input type="hidden" name="crud_action_additional" value="">
    <div class="main-content">
        <div class="container">
            <div class="row">
                <div class="col-xs-12">
                    <div class="tab-gp dashboard-tab">
                        <ul class="nav nav-tabs hidden-xs hidden-sm" role="tablist">
                            <li class="<c:choose>
                                            <c:when test="${serListDto.checkListTab=='chkList'}">
                                                   complete
                                                </c:when>
                                                <c:otherwise>
                                                    active
                                                </c:otherwise>
                                            </c:choose>" role="presentation"><a href="#tabInfo" aria-controls="tabInfo" role="tab" data-toggle="tab">Info</a></li>
                            <li class="complete" role="presentation"><a href="#tabDocuments" aria-controls="tabDocuments" role="tab"
                                                                        data-toggle="tab">Documents</a></li>
                            <li class="<c:choose>
                                            <c:when test="${serListDto.checkListTab=='chkList'}">
                                                   active
                                                </c:when>
                                                <c:otherwise>
                                                    complete
                                                </c:otherwise>
                                            </c:choose>" role="presentation"><a href="#tabPayment" aria-controls="tabPayment" role="tab"
                                                                                data-toggle="tab">Checklist</a></li>
                            <li class="complete" role="presentation"><a href="#Processing" aria-controls="Processing" role="tab"
                                                                        data-toggle="tab">Processing</a></li>
                        </ul>
                        <div class="tab-nav-mobile visible-xs visible-sm">
                            <div class="swiper-wrapper" role="tablist">
                                <div class="swiper-slide"><a href="#tabInfo" aria-controls="tabInfo" role="tab" data-toggle="tab">Info</a></div>
                                <div class="swiper-slide"><a href="#tabDocuments" aria-controls="tabDocuments" role="tab" data-toggle="tab">Documents</a></div>
                                <div class="swiper-slide"><a href="#tabPayment" aria-controls="tabPayment" role="tab" data-toggle="tab">Payment</a></div>
                                <div class="swiper-slide"><a href="#Processing" aria-controls="Processing" role="tab" data-toggle="tab">Processing</a></div>
                            </div>
                            <div class="swiper-button-prev"></div>
                            <div class="swiper-button-next"></div>
                        </div>
                        <div class="tab-content">
                            <div class="tab-pane  <c:if test="${serListDto.checkListTab!='chkList'}">active</c:if>" id="tabInfo" role="tabpanel">
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
                            <div class="tab-pane <c:if test="${serListDto.checkListTab=='chkList'}">active</c:if>" id="tabPayment" role="tabpanel">
                                <div class="col-xs-12">
                                    <div class="input-group">
                                        <div class="ax_default text_area">
                                            <span  style="font-size: 18px"><strong>Inspection Date</strong></span>
                                            <iais:datePicker id="inspectionDate" name = "inspectionDate"  value="${serListDto.inspectionDate}"></iais:datePicker>
                                            <span class="error-msg" id="error_inspectionDate" name="iaisErrorMsg"></span>
                                        </div>
                                    </div>
                                    <div class="input-group">
                                        <div class="ax_default text_area">
                                            <span  style="font-size: 18px"><strong>Inspection Start Time (HH MM)</strong></span>
                                        </div>
                                        <div style="float: left"><input type="text" maxlength="2" name="startHour" value="<c:out value="${serListDto.startHour}"/>"></div><div style="float: left;padding-left: 10px;"><input type="text" maxlength="2" name="startHourMin" value="<c:out value="${serListDto.startMin}"/>"></div>
                                        <span class="error-msg" id="error_sTime" name="iaisErrorMsg"></span>
                                    </div>

                                    <div class="input-group">
                                        <div class="ax_default text_area">
                                            <span  style="font-size: 18px"><strong>Inspection End Time(HH MM)</strong></span>
                                        </div>
                                        <div style="float: left"><input type="text" maxlength="2" name="endHour" value="<c:out value="${serListDto.endHour}"/>"></div><div style="float: left;padding-left: 10px;"><input type="text" maxlength="2" name="endHourMin" value="<c:out value="${serListDto.endMin}"/>"></div>
                                        <span class="error-msg" id="error_eTime" name="iaisErrorMsg"></span>
                                        <span class="error-msg" id="error_timevad" name="iaisErrorMsg"></span>
                                    </div>

                                    <div class="input-group">
                                        <div class="ax_default text_area">
                                            <span style="font-size: 18px"><strong>Inspection Lead</strong></span> <c:out value="${serListDto.inspectionLeader}"/>
                                        </div>
                                    </div>

                                    <div class="input-group">
                                        <div class="ax_default text_area">
                                            <span style="font-size: 18px"><strong>Inspection Officers</strong></span>
                                            <c:forEach var = "officer" items = "${serListDto.inspectionofficer}" varStatus="status">
                                                <c:out value="${officer}"/>
                                            </c:forEach>
                                        </div>
                                    </div>

                                    <div class="input-group">
                                        <div class="ax_default text_area">
                                            <span style="font-size: 18px"><strong>Other Inspection Officers</strong></span><br>
                                            <textarea cols="70" rows="7" name="otherinspector" id="otherinspector" maxlength="300"><c:out value="${serListDto.otherinspectionofficer}"></c:out></textarea>
                                            <span class="error-msg" id="error_otherofficer" name="iaisErrorMsg"></span>
                                        </div>
                                    </div>
                                    <div class="input-group">
                                        <div class="ax_default text_area">
                                            <span style="font-size: 18px"><strong>No. of Non-Compliance</strong></span>
                                            <c:out value="${serListDto.totalNcNum}"></c:out>
                                        </div>
                                    </div>

                                    <div class="input-group">
                                        <div class="ax_default text_area">
                                            <h4><strong>Remarks</strong></h4> <textarea cols="70" rows="7" name="tcuRemark" id="tcuRemark" maxlength="300"><c:out value="${serListDto.tcuRemark}"></c:out></textarea>
                                            <span class="error-msg" id="error_tcuRemark" name="iaisErrorMsg"></span>
                                        </div>
                                    </div>
                                    <div class="input-group">
                                        <div class="ax_default text_area">
                                            <h4><strong>Best Practices</strong></h4>
                                            <textarea cols="70" rows="7" name="bestpractice" id="bestpractice" maxlength="500"><c:out value="${serListDto.bestPractice}"></c:out></textarea>
                                            <span class="error-msg" id="error_bestPractice" name="iaisErrorMsg"></span>
                                        </div>
                                    </div>
                                    <div class="input-group">
                                        <div class="ax_default text_area">
                                            <h4><strong>Letter Written to Licensee</strong></h4>
                                            <span id="licFileName"></span>
                                            <div class="file-upload-gp">
                                                <input id="selectedFile" name="selectedFile" type="file" style="display: none;" aria-label="selectedFile1"><a class="btn btn-file-upload btn-secondary" href="#">Upload</a>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="ax_default text_area">
                                        <h4><strong>TCU</strong></h4> <input type="checkbox" id="tcuType"  value="tcuType"  <c:if test="${serListDto.tcuFlag == true}">checked</c:if>  name="tcuType" onclick="javascript: showTcuLabel(this);">
                                    </div>

                                    <div class="input-group" id="tcuLabel">
                                        <div class="ax_default text_area">
                                            <h4><strong>TCU Date</strong></h4> &nbsp;<iais:datePicker id = "tuc" name = "tuc" value="${serListDto.tuc}"></iais:datePicker><br>
                                            <span class="error-msg" id="error_tcuDate" name="iaisErrorMsg"></span>
                                        </div>
                                    </div>
                                </div>
                                    <div class="row">
                                        <div class="col-xs-12">
                                            <div align="right">
                                                <input type="hidden" name="viewchk" id = "viewchk">
                                                <button type="button" class="btn btn-primary" onclick="javascript: doViewCheckList();">
                                                    View CheckList
                                                </button>
                                                <span class="error-msg" id="error_fillchkl" name="iaisErrorMsg"></span>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            <div class="tab-pane" id="Processing" role="tabpanel">
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

                                <div class="row">
                                    <div class="col-xs-12">
                                        <div align="right">
                                            <button type="button" class="btn btn-primary" onclick="javascript: doSubmit();">
                                                Submit
                                            </button>
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
<%@ include file="/WEB-INF/jsp/include/validation.jsp" %>
<script type="text/javascript">
    $(document).ready(function (){
        if($("#tcuType").is(":checked")){
            $("#tcuLabel").show()
        }else{
            $("#tcuLabel").hide();
        }
    });
    function doViewCheckList(){
        $("#viewchk").val("viewchk");
        SOP.Crud.cfxSubmit("mainForm", "vewChkl");
    }
    function showTcuLabel(checkbox){
        if(checkbox.checked == true){
            $("#tcuLabel").show()
        }else{
            $("#tcuLabel").hide();
        }
    }
    function doSubmit(){
        $("#viewchk").val("");
        showWaiting();
        SOP.Crud.cfxSubmit("mainForm", "next");
        dismissWaiting();
    }
    function showCheckBox(str){
        var name = str;
        var divId = str+'ck';
        var comId = str+'comck'
        divId  = divId.replace(/\s*/g,"");
        comId = comId.replace(/\s*/g,"");
        var comdivck =document.getElementById(divId);
        var divck =document.getElementById(comId);
        $("#"+divId).show();
        $("#"+comId).show();
    }
    function hideCheckBox(str){
        var name = str;
        var divId = str+'ck';
        var comdivId = str+'comck';
        divId  = divId.replace(/\s*/g,"");
        comdivId = comdivId.replace(/\s*/g,"");
        var divck =document.getElementById(divId);
        var comdivck =document.getElementById(comdivId);
        $("#"+divId).hide();
        $("#"+comdivId).hide();

    }
    function getFileName(o) {
        var pos = o.lastIndexOf("\\");
        return o.substring(pos + 1);
    }

    $('.selectedFile').change(function () {
        var file = $(this).val();
        $("#licFileName").html(getFileName(file));
        alert(123);
/*        $(this).parent().children('span:eq(0)').html(getFileName(file));
        $(this).parent().children('span:eq(0)').next().removeClass("hidden");
        $(this).parent().children('input delFlag').val('N');*/
    });

</script>
