<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.utils.MaskUtil" %>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.AppConsts" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-intranet"/>
<div class="main-content dashboard">
    <form id="mainForm"  method="post" action=<%=process.runtime.continueURL()%>>
        <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
        <input type="hidden" name="base_action_type" id="base_action_type"/>

        <div class="col-lg-12 col-xs-12">
            <div class="center-content">
                <div class="intranet-content">
                    <div class="row form-horizontal">
                        <div class="bg-title col-xs-12 col-md-12">
                            <h2>Assisted Reproduction Enquiry</h2>
                        </div>


                        <div class="col-xs-12 col-md-12">
                            <iais:row>
                                <iais:field width="4" value="SEARCH BY" />
                                <div class="col-md-8">
                                    <iais:value width="4" cssClass="col-md-4">
                                        <div class="form-check">
                                            <input class="form-check-input"
                                                   type="radio"
                                                   name="searchBy"
                                                   value="1"
                                                   id="searchByPatient"
                                                   <c:if test="${ assistedReproductionEnquiryFilterDto.searchBy =='1' }">checked</c:if>
                                                   aria-invalid="false">
                                            <label class="form-check-label"
                                                   for="searchByPatient"><span
                                                    class="check-circle"></span>Patient Information</label>
                                        </div>
                                    </iais:value>
                                    <iais:value width="4" cssClass="col-md-4">
                                        <div class="form-check">
                                            <input class="form-check-input" type="radio"
                                                   name="searchBy" value="0" id="searchBySubmission"
                                                   <c:if test="${assistedReproductionEnquiryFilterDto.searchBy == '0'}">checked</c:if>
                                                   aria-invalid="false">
                                            <label class="form-check-label"
                                                   for="searchBySubmission"><span
                                                    class="check-circle"></span>Submission ID</label>
                                        </div>
                                    </iais:value>
                                </div>
                            </iais:row>

                            <hr>

                            <iais:row>
                                <iais:field width="4" value="AR Centre" />
                                <iais:value width="4" cssClass="col-md-4">
                                    <select name="arCentre" id="arCentre">
                                        <option value="" <c:if test="${empty assistedReproductionEnquiryFilterDto.arCentre}">selected="selected"</c:if>>Please Select</option>
                                        <c:forEach items="${embryosBiopsiedLocalSelectOption}" var="selectOption">
                                            <option value="${selectOption.value}" <c:if test="${assistedReproductionEnquiryFilterDto.arCentre ==selectOption.value}">selected="selected"</c:if>>${selectOption.text}</option>
                                        </c:forEach>
                                    </select>
                                </iais:value>
                            </iais:row>
                            <div id="patientInformationFilter" <c:if test="${ assistedReproductionEnquiryFilterDto.searchBy !='1' }">style="display: none"</c:if>>
                                <iais:row>
                                    <iais:field width="4" value="Patient Name"/>
                                    <iais:value width="4" cssClass="col-md-4" >
                                        <input type="text" maxlength="66" id="patientName"  name="patientName" value="${assistedReproductionEnquiryFilterDto.patientName}" >
                                    </iais:value>
                                </iais:row>
                                <iais:row>
                                    <iais:field width="4" value="Patient ID Type"/>
                                    <div class="col-md-4 multi-select col-xs-4">
                                        <iais:select name="patientIdTypeList"  multiValues="${assistedReproductionEnquiryFilterDto.patientIdTypeList}" codeCategory="CATE_ID_DS_ID_TYPE"  multiSelect="true"/>
                                    </div>
                                </iais:row>
                                <iais:row>
                                    <iais:field width="4" value="Patient ID No."/>
                                    <iais:value width="4" cssClass="col-md-4"  >
                                        <input type="text" maxlength="20" id="patientIdNumber"  name="patientIdNumber" value="${assistedReproductionEnquiryFilterDto.patientIdNumber}" >
                                    </iais:value>
                                </iais:row>

                            </div>

                            <div id="submissionFilter" <c:if test="${ assistedReproductionEnquiryFilterDto.searchBy !='0'}">style="display: none"</c:if>>
                                <iais:row>
                                    <iais:field width="4" value="Submission ID"/>
                                    <iais:value width="4" cssClass="col-md-4" display="true" >
                                        <input type="text" maxlength="20" id="submissionId"  name="submissionId" value="${assistedReproductionEnquiryFilterDto.submissionId}" >
                                    </iais:value>
                                </iais:row>
                                <iais:row>
                                    <iais:field width="4" value="Submission Type"/>
                                    <iais:value width="4" cssClass="col-md-4">
                                        <iais:select name="submissionType" id="submissionType" firstOption="Please Select" options="submissionTypeOptions"
                                                     value="${assistedReproductionEnquiryFilterDto.submissionType}" cssClass="idTypeSel" />
                                    </iais:value>
                                    <iais:value width="4" cssClass="col-md-4">
                                        <div id="cycleStageDisplay" <c:if test="${assistedReproductionEnquiryFilterDto.submissionType!='AR_TP002'}">style="display: none"</c:if> >
                                            <iais:select name="cycleStage" id="cycleStage" firstOption="Please Select" codeCategory="CATE_ID_DS_STAGE_TYPE"
                                                         value="${assistedReproductionEnquiryFilterDto.cycleStage}" cssClass="idTypeSel" />
                                        </div>
                                    </iais:value>
                                </iais:row>
                                <iais:row>
                                    <iais:field width="4" value="Submission Date Range"/>
                                    <iais:value width="4" cssClass="col-md-4">
                                        <iais:datePicker id="submissionDateFrom" name="submissionDateFrom" dateVal="${assistedReproductionEnquiryFilterDto.submissionDateFrom}"/>
                                    </iais:value>
                                    <iais:value width="4" cssClass="col-md-4">
                                        <iais:datePicker id="submissionDateTo" name="submissionDateTo" dateVal="${assistedReproductionEnquiryFilterDto.submissionDateTo}"/>
                                    </iais:value>
                                </iais:row>

                            </div>


                            <div class="col-xs-12 col-md-12">
                                <iais:action style="text-align:right;">
                                    <button type="button" class="btn btn-secondary"
                                            onclick="javascript:doClear();">Clear
                                    </button>
                                    <button type="button" class="btn btn-primary"
                                            onclick="javascript:doSearch();">Search
                                    </button>
                                    <button type="button" class="btn btn-primary"
                                            onclick="javascript:doAdvancedSearch();">Advanced Search
                                    </button>
                                </iais:action>
                            </div>
                        </div>
                    </div>
                    <br>

                    <div id="u82" class="ax_default" data-label="Data Quick View" style="visibility: inherit;" data-left="591" data-top="586" data-width="569" data-height="810">


                    </div>

                    <div id="patientResultDisplay" <c:if test="${ assistedReproductionEnquiryFilterDto.searchBy !='1' }">style="display: none"</c:if>>
                        <div class="col-xs-12">
                            <div class="components">

                                <iais:pagination param="patientParam" result="patientResult"/>
                                <div class="table-responsive">
                                    <div class="table-gp">
                                        <table aria-describedby="" class="table application-group" style="border-collapse:collapse;">
                                            <thead>
                                            <tr >

                                                <iais:sortableHeader needSort="false"
                                                                     field="NAME"
                                                                     value="Patient Name"/>
                                                <iais:sortableHeader needSort="false"
                                                                     field="ID_TYPE"
                                                                     value="Patient ID Type"/>
                                                <iais:sortableHeader needSort="false"
                                                                     field="ID_NUMBER"
                                                                     value="Patient ID No"/>
                                                <iais:sortableHeader needSort="false"
                                                                     field="DATE_OF_BIRTH"
                                                                     value="Patient Date of Birth"/>
                                                <iais:sortableHeader needSort="false"
                                                                     field="NATIONALITY"
                                                                     value="Patient Nationality"/>

                                                <iais:sortableHeader needSort="false"
                                                                     field=""
                                                                     value="Action"/>
                                            </tr>
                                            </thead>
                                            <tbody class="form-horizontal">
                                            <c:choose>
                                                <c:when test="${empty patientResult.rows}">
                                                    <tr>
                                                        <td colspan="15">
                                                            <iais:message key="GENERAL_ACK018"
                                                                          escape="true"/>
                                                        </td>
                                                    </tr>
                                                </c:when>
                                                <c:otherwise>
                                                    <c:forEach var="patient"
                                                               items="${patientResult.rows}"
                                                               varStatus="status">
                                                        <tr id="advfilter${(status.index + 1) + (patientParam.pageNo - 1) * patientParam.pageSize}">

                                                            <td style="vertical-align:middle;">

                                                                <p style="width: 165px;"><c:out value="${patient.patientName}"/>
                                                                    <a href="javascript:void(0);" class="accordion-toggle  collapsed" style="float: right" data-toggle="collapse" data-target="#dropdown${(status.index + 1) + (patientParam.pageNo - 1) * patientParam.pageSize}" onclick="getPatientByPatientId('${patient.patientId}','${(status.index + 1) + (patientParam.pageNo - 1) * patientParam.pageSize}')">
                                                                    </a>
                                                                </p>
                                                            </td>
                                                            <td style="vertical-align:middle;">
                                                                <iais:code code="${patient.patientIdType}"/>
                                                            </td>
                                                            <td style="vertical-align:middle;">
                                                                <c:out value="${patient.patientIdNo}"/>
                                                            </td>
                                                            <td style="vertical-align:middle;">
                                                                <fmt:formatDate
                                                                        value="${patient.patientDateBirth}"
                                                                        pattern="${AppConsts.DEFAULT_DATE_FORMAT}"/>
                                                            </td>
                                                            <td style="vertical-align:middle;">
                                                                <iais:code code="${patient.patientNationality}"/>
                                                            </td>

                                                            <td >
                                                                <button  href="#newappModal"  onclick="quickView('${patient.patientId}')" data-toggle="modal" data-target="#newappModal" type="button" class=" btn btn-default btn-sm">
                                                                    Quick View
                                                                </button>
                                                                <br>
                                                                <button type="button" onclick="fullDetailsView('${patient.patientId}')" class="btn btn-default btn-sm">
                                                                    View Full Details
                                                                </button>
                                                            </td>
                                                        </tr>
                                                    </c:forEach>
                                                </c:otherwise>
                                            </c:choose>
                                            </tbody>
                                        </table>
                                        <div id="newappModal" class="modal fade" tabindex="-1" role="dialog" style="top:10px">
                                            <div class="col-md-8"  role="document" style="float:right ">
                                                <div class="modal-content">
                                                    <div class="row">
                                                        <div class="col-md-1" >
                                                            <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true"><</span></button>
                                                        </div>
                                                        <div class="col-md-11 " >
                                                            <div class="quickBodyDiv"></div>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>

                                </div>

                                <iais:action style="text-align:right;">
                                    <a class="btn btn-secondary"
                                       href="${pageContext.request.contextPath}/officer-online-enquiries-information-file">Download</a>
                                </iais:action>
                            </div>
                        </div>
                    </div>


                    <div id="submissionResultDisplay" <c:if test="${ assistedReproductionEnquiryFilterDto.searchBy !='0' }">style="display: none"</c:if>>
                        <div class="col-xs-12">
                            <div class="components">

                                <iais:pagination param="submissionParam" result="submissionResult"/>
                                <div class="table-responsive">
                                    <div class="table-gp">
                                        <table aria-describedby="" class="table">
                                            <thead>
                                            <tr >
                                                <iais:sortableHeader needSort="false"
                                                                     field="BUSINESS_NAME"
                                                                     value="AR Centre"/>
                                                <iais:sortableHeader needSort="false"
                                                                     field="SUBMISSION_NO"
                                                                     value="Submission ID"/>
                                                <iais:sortableHeader needSort="false"
                                                                     field="SUBMISSION_TYPE"
                                                                     value="Submission Type"/>
                                                <iais:sortableHeader needSort="false"
                                                                     field="CYCLE_STAGE"
                                                                     value="Submission Subtype"/>
                                                <iais:sortableHeader needSort="false"
                                                                     field="SUBMIT_DT"
                                                                     value="Submission Date"/>
                                            </tr>
                                            </thead>
                                            <tbody class="form-horizontal">
                                            <c:choose>
                                                <c:when test="${empty submissionResult.rows}">
                                                    <tr>
                                                        <td colspan="15">
                                                            <iais:message key="GENERAL_ACK018"
                                                                          escape="true"/>
                                                        </td>
                                                    </tr>
                                                </c:when>
                                                <c:otherwise>
                                                    <c:forEach var="submission"
                                                               items="${submissionResult.rows}"
                                                               varStatus="status">
                                                        <tr>

                                                            <td>
                                                                <c:out value="${submission.arCentre}"/>
                                                            </td>
                                                            <td>
                                                                <a href="#" onclick="fullDetailsView('${submission.submissionIdNo}')">${submission.submissionIdNo}
                                                                </a>
                                                            </td>
                                                            <td>
                                                                <c:out value="${submission.submissionType}"/>
                                                            </td>
                                                            <td>
                                                                <iais:code code="${submission.submissionSubtype}"/>
                                                            </td>
                                                            <td>
                                                                <fmt:formatDate
                                                                        value="${submission.submissionDate}"
                                                                        pattern="${AppConsts.DEFAULT_DATE_FORMAT}"/>
                                                            </td>

                                                        </tr>
                                                    </c:forEach>
                                                </c:otherwise>
                                            </c:choose>
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
    </form>
</div>
<%@include file="/WEB-INF/jsp/include/utils.jsp" %>
<script type="text/javascript">
    var dividajaxlist = [];

    $(document).ready(function () {
        quickView();

        $('#searchByPatient').change(function () {
            if($(this).is(':checked')){
                $('#patientInformationFilter').attr("style","display: block");
                $('#patientResultDisplay').attr("style","display: block");
                $('#submissionFilter').attr("style","display: none");
                $('#submissionResultDisplay').attr("style","display: none");
            }else {
                $('#patientInformationFilter').attr("style","display: none");
                $('#patientResultDisplay').attr("style","display: none");
                $('#submissionFilter').attr("style","display: block");
                $('#submissionResultDisplay').attr("style","display: block");
            }
        });

        $('#searchBySubmission').change(function () {
            if($(this).is(':checked')){
                $('#patientInformationFilter').attr("style","display: none");
                $('#patientResultDisplay').attr("style","display: none");
                $('#submissionFilter').attr("style","display: block");
                $('#submissionResultDisplay').attr("style","display: block");
            }else {
                $('#patientInformationFilter').attr("style","display: block");
                $('#patientResultDisplay').attr("style","display: block");
                $('#submissionFilter').attr("style","display: none");
                $('#submissionResultDisplay').attr("style","display: none");
            }
        });

        $('#submissionType').change(function () {

            var reason= $('#submissionType option:selected').val();

            if("AR_TP002"==reason){
                $('#cycleStageDisplay').attr("style","display: block");
            }else {
                $('#cycleStageDisplay').attr("style","display: none");
            }

        });
    })


    var getPatientByPatientId = function (applicationGroupId, divid) {
        if (!isInArray(dividajaxlist,divid)) {
            groupAjax(applicationGroupId, divid);
        } else {
            var display = $('#advfilterson' + divid).css('display');
            if (display == 'none') {
                $('#advfilterson' + divid).show();
            } else {
                $('#advfilterson' + divid).hide();
            }
        }
    };
    function isInArray(arr,value){
        for(var i = 0; i < arr.length; i++){
            if(value === arr[i]){
                return true;
            }
        }
        return false;
    };
    var groupAjax = function (applicationGroupId, divid) {
        dividajaxlist.push(divid);
        $.post(
            '/hcsa-licence-web/hcsa/intranet/ar/patientDetail.do',
            {patientId: applicationGroupId},
            function (data) {
                let result = data.result;
                if('Success' == result) {
                    let res = data.ajaxResult;
                    let html = '<tr style="background-color: #F3F3F3;" class="p" id="advfilterson' + divid + '">' +
                        '<td colspan="7" class="hiddenRow">' +
                        '<div class="accordian-body p-3 collapse in" id="dropdown' + divid + '" >' +
                        '<table class="table application-item" style="background-color: #F3F3F3;" >' +
                        '<thead>' +
                        '<tr>';


                    html += '<th width="10%">AR/IUI/EFO</th>' +
                        '<th width="20%">AR Treatment Cycle Type</th>' +
                        '<th width="25%">AR Centre</th>' +
                        '<th width="15%">Cycle Start Date</th>' +
                        '<th width="20%">Co-funding Claimed</th>' +
                        '<th width="15%">Status</th>'  +
                        '</tr>' +
                        '</thead>' +
                        '<tbody>';
                    for (let i = 0; i < 0; i++) {
                        var color = "black";

                        html += '<tr style = "color : ' + color + ';">';

                        html += '<td><p class="visible-xs visible-sm table-row-title">AR/IUI/EFO</p><p>' + res.rows[i].appTypeStrShow + '<p></td>' +
                            '<td><p class="visible-xs visible-sm table-row-title">AR Treatment Cycle Type</p><p>' + res.rows[i].serviceName + '<p></td>' +
                            '<td><p class="visible-xs visible-sm table-row-title">AR Centre</p><p>' + res.rows[i].serviceName + '<p></td>';
                        html += '<td><p class="visible-xs visible-sm table-row-title">Cycle Start Date</p><p><a href="#" onclick="javascript:fullStagesView(' + "'" + res.rows[i].id + "'" + ');">' + res.rows[i].applicationNo + '</a></p></td>';

                        html += '</p></td>' +
                            '<td><p class="visible-xs visible-sm table-row-title">Co-funding Claimed</p><p>' + res.rows[i].slaDays + '</p></td>' +
                            '<td><p class="visible-xs visible-sm table-row-title">Status</p><p>' + res.rows[i].tolalSlaDays + '</p></td>' +
                            '</tr>';
                    }
                    html += '</tbody></table></div></td></tr>';
                    $('#advfilter' + divid).after(html);
                }
            }
        )
    };

    function doClear() {
        $('input[type="text"]').val("");
        $('input[type="radio"]').prop("checked", false);
        $('input[type="checkbox"]').prop("checked", false);
    }

    function doAdvancedSearch() {
        showWaiting();
        $("[name='base_action_type']").val('advanced');
        $('#mainForm').submit();
    }

    function jumpToPagechangePage() {
        search();
    }

    function doSearch() {
        $('input[name="pageJumpNoTextchangePage"]').val(1);
        search();
    }

    function search() {
        showWaiting();
        $("[name='base_action_type']").val('search');
        $('#mainForm').submit();
    }

    function sortRecords(sortFieldName, sortType) {
        $("[name='crud_action_value']").val(sortFieldName);
        $("[name='crud_action_additional']").val(sortType);
        $("[name='base_action_type']").val('search');
        submit('search');
    }

    var quickView = function (submissionIdNo) {

        var jsonData={
            'submissionIdNo': submissionIdNo
        };
        $.ajax({
            'url':'${pageContext.request.contextPath}/ar-quick-view',
            'dataType':'text',
            'data':jsonData,
            'type':'GET',
            'success':function (data) {
                if(data == null){
                    return;
                }
                $('.quickBodyDiv').html(data);


            },
            'error':function () {
            }
        });
    }

    var fullDetailsView = function (submissionIdNo) {

        showWaiting();
        $("[name='crud_action_value']").val(submissionIdNo);
        $("[name='base_action_type']").val('viewFull');
        $('#mainForm').submit();
    }

    var fullStagesView = function (submissionIdNo) {

        showWaiting();
        $("[name='crud_action_value']").val(submissionIdNo);
        $("[name='base_action_type']").val('viewStage');
        $('#mainForm').submit();
    }
</script>