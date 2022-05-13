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
<%
    String webrootCom=IaisEGPConstant.CSS_ROOT+IaisEGPConstant.COMMON_CSS_ROOT;
%>
<style>
    .table-gp table.table > tbody > tr > td p a[data-toggle=collapse] {
        position: relative;
        margin-right: 25px;
        margin-top: -12px;
    }
    .form-horizontal p {
        line-height: 23px;
    }
    .hiddenRow {
        padding: 0px 0px !important;
        background-color: #f3f3f3;
    }
</style>
<webui:setLayout name="iais-intranet"/>
<div class="main-content dashboard">
    <form id="mainForm"  method="post" action=<%=process.runtime.continueURL()%>>
        <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>

        <div class="col-lg-12 col-xs-12">
            <div class="center-content">
                <div class="intranet-content">
                    <div class="row form-horizontal">
                        <div class="bg-title col-xs-12 col-md-12">
                            <h2>Voluntary Sterilisation Enquiry</h2>
                        </div>


                        <div class="col-xs-12 col-md-12">
                            <%--                            <iais:row>--%>
                            <%--                                <iais:field width="4" value="SEARCH" />--%>
                            <%--                                <div class="col-md-8">--%>
                            <%--                                </div>--%>
                            <%--                            </iais:row>--%>

                            <%--                            <hr>--%>

                            <iais:row>
                                <iais:field width="4" value="Name of Medical Clinic/Hospital" />
                                <iais:value width="4" cssClass="col-md-4">
                                    <iais:select name="centerName" id="centerName" firstOption="Please Select" options="arCentreSelectOption"
                                                 cssClass="clearSel"  value="${dsEnquiryVssFilterDto.centerName}"  />
                                </iais:value>
                            </iais:row>
                            <iais:row>
                                <iais:field width="4" value="Submission ID"/>
                                <iais:value width="4" cssClass="col-md-4" >
                                    <input type="text" maxlength="20" id="submissionNo"  name="submissionNo" value="${dsEnquiryVssFilterDto.submissionNo}" >
                                </iais:value>
                            </iais:row>
                            <iais:row>
                                <iais:field width="4" value="Name of Patient"/>
                                <iais:value width="4" cssClass="col-md-4" >
                                    <input type="text" maxlength="20" id="patientName"  name="patientName" value="${dsEnquiryVssFilterDto.patientName}" >
                                </iais:value>
                            </iais:row>
                            <iais:row>
                                <iais:field width="4" value="Patient ID Type"/>
                                <iais:value width="4" cssClass="col-md-4">
                                    <iais:select name="patientIdType" id="patientIdType" firstOption="Please Select" codeCategory="CATE_ID_DS_ID_TYPE"
                                                 cssClass="clearSel"   value="${dsEnquiryVssFilterDto.patientIdType}" />
                                </iais:value>
                            </iais:row>
                            <iais:row>
                                <iais:field width="4" value="Patient ID Number"/>
                                <iais:value width="4" cssClass="col-md-4" >
                                    <input type="text" maxlength="20" id="patientIdNo"  name="patientIdNo" value="${dsEnquiryVssFilterDto.patientIdNo}" >
                                </iais:value>
                            </iais:row>
                            <iais:row>
                                <iais:field width="4" value="Marital Status"/>
                                <iais:value width="4" cssClass="col-md-4">
                                    <iais:select name="maritalStatus" id="maritalStatus" firstOption="Please Select" codeCategory="VSS_MARITAL_STATUS"
                                                 cssClass="clearSel"   value="${dsEnquiryVssFilterDto.maritalStatus}" />
                                </iais:value>
                            </iais:row>
                            <iais:row>
                                <iais:field width="4" value="Main Reason For Sterilisation"/>
                                <iais:value width="4" cssClass="col-md-4">
                                    <iais:select name="sterilisationReason" id="sterilisationReason" firstOption="Please Select" codeCategory="VSS_STERILIZATION_REASON"
                                                 cssClass="clearSel"   value="${dsEnquiryVssFilterDto.sterilisationReason}" />
                                </iais:value>
                            </iais:row>
                            <iais:row>
                                <iais:field width="4" value="Submission Date"/>
                                <iais:value width="4" cssClass="col-md-4">
                                    <iais:datePicker id="submissionDateFrom" name="submissionDateFrom" dateVal="${dsEnquiryVssFilterDto.submissionDateFrom}"/>
                                </iais:value>
                                <label class="col-xs-1 col-md-1 control-label">To&nbsp;</label>
                                <iais:value width="3" cssClass="col-md-3">
                                    <iais:datePicker id="submissionDateTo" name="submissionDateTo" dateVal="${dsEnquiryVssFilterDto.submissionDateTo}"/>
                                </iais:value>
                            </iais:row>



                            <div class="col-xs-12 col-md-12">
                                <iais:action style="text-align:right;">
                                    <button type="button" class="btn btn-secondary"
                                            onclick="javascript:doClear();">Clear
                                    </button>
                                    <button type="button" class="btn btn-primary"
                                            onclick="javascript:doSearch();">Search
                                    </button>
                                </iais:action>
                            </div>
                        </div>
                    </div>
                    <br>

                    <div class="col-xs-12 row">
                        <div class="components">

                            <iais:pagination param="vssParam" result="vssResult"/>
                            <div class="table-responsive">
                                <div class="table-gp">
                                    <table aria-describedby="" class="table application-group" style="border-collapse:collapse;">
                                        <thead>
                                        <tr >

                                            <iais:sortableHeader needSort="true" style="white-space: nowrap;padding: 15px 25px 15px 0px;"
                                                                 field="CENTER_NAME"
                                                                 value="Name of Medical Clinic/Hospital"/>
                                            <iais:sortableHeader needSort="true" style="white-space: nowrap;padding: 15px 25px 15px 0px;"
                                                                 field="SUBMISSION_NO"
                                                                 value="Submission ID"/>
                                            <iais:sortableHeader needSort="true" style="white-space: nowrap;padding: 15px 25px 15px 0px;"
                                                                 field="PATIENT_NAME"
                                                                 value="Name of Patient"/>
                                            <iais:sortableHeader needSort="true" style="white-space: nowrap;padding: 15px 25px 15px 0px;"
                                                                 field="PATIENT_ID_TYPE"
                                                                 value="Patient ID Type"/>
                                            <iais:sortableHeader needSort="true" style="white-space: nowrap;padding: 15px 25px 15px 0px;"
                                                                 field="PATIENT_ID_NO"
                                                                 value="Patient ID Number"/>
                                            <iais:sortableHeader needSort="true" style="white-space: nowrap;padding: 15px 25px 15px 0px;"
                                                                 field="PATIENT_BIRTHDAY"
                                                                 value="Patient Date of Birth"/>
                                            <iais:sortableHeader needSort="true" style="white-space: nowrap;padding: 15px 25px 15px 0px;"
                                                                 field="MARITAL_STATUS"
                                                                 value="Marital Status"/>
                                            <iais:sortableHeader needSort="true" style="white-space: nowrap;padding: 15px 25px 15px 0px;"
                                                                 field="STERILIZATION_REASON"
                                                                 value="Main Reason For Sterilisation"/>
                                            <iais:sortableHeader needSort="true" style="white-space: nowrap;padding: 15px 25px 15px 0px;"
                                                                 field="SUBMIT_DT"
                                                                 value="Submission Date"/>
                                        </tr>
                                        </thead>
                                        <tbody class="form-horizontal">
                                        <c:choose>
                                            <c:when test="${empty vssResult.rows}">
                                                <tr>
                                                    <td colspan="15">
                                                        <iais:message key="GENERAL_ACK018"
                                                                      escape="true"/>
                                                    </td>
                                                </tr>
                                            </c:when>
                                            <c:otherwise>
                                                <c:forEach var="vss"
                                                           items="${vssResult.rows}"
                                                           varStatus="status">
                                                    <tr id="advfilter${(status.index + 1) + (vssParam.pageNo - 1) * vssParam.pageSize}">

                                                        <td  style="vertical-align:middle;">
                                                            <p style="white-space: nowrap;"><c:out value="${vss.centerName}"/>
                                                                <c:if test="${vss.patientCount > 1}">
                                                                    <a href="javascript:void(0);" class="accordion-toggle  collapsed" style="float: right;color: #2199E8" data-toggle="collapse" data-target="#dropdown${(status.index + 1) + (vssParam.pageNo - 1) * vssParam.pageSize}" onclick="getVssByIdType('${vss.patientIdNo}','${vss.patientIdType}','${(status.index + 1) + (vssParam.pageNo - 1) * vssParam.pageSize}')">
                                                                    </a>
                                                                </c:if>
                                                            </p>

                                                        </td>
                                                        <td  style="vertical-align:middle;">
                                                            <a href="#" onclick="fullDetailsView('${vss.submissionNo}')">${vss.submissionNo}</a>
                                                        </td>
                                                        <td  style="vertical-align:middle;">
                                                            <c:out value="${vss.patientName}"/>
                                                        </td>
                                                        <td  style="vertical-align:middle;">
                                                            <iais:code code="${vss.patientIdType}"/>
                                                        </td>
                                                        <td  style="vertical-align:middle;">
                                                            <c:out value="${vss.patientIdNo}"/>
                                                        </td>
                                                        <td  style="vertical-align:middle;">
                                                            <fmt:formatDate
                                                                    value="${vss.patientBirthday}"
                                                                    pattern="${AppConsts.DEFAULT_DATE_FORMAT}"/>
                                                        </td>
                                                        <td  style="vertical-align:middle;">
                                                            <iais:code code="${vss.maritalStatus}"/>
                                                        </td>
                                                        <td  style="vertical-align:middle;">
                                                            <iais:code code="${vss.sterilisationReason}"/>
                                                        </td>
                                                        <td  style="vertical-align:middle;">
                                                            <fmt:formatDate
                                                                    value="${vss.submitDt}"
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

                            <iais:action style="text-align:right;">
                                <a class="btn btn-secondary"
                                   href="${pageContext.request.contextPath}/hcsa/enquiry/ar/VSS-SearchResults-DownloadS">Download</a>
                            </iais:action>
                        </div>
                    </div>


                </div>
            </div>
        </div>
    </form>
</div>
<%@include file="/WEB-INF/jsp/include/utils.jsp" %>
<script type="text/javascript" >

    function doClear() {
        $('input[type="text"]').val("");
        $('input[type="checkbox"]').prop("checked", false);
        $("select option").prop("selected", false);
        $(".clearSel").children(".current").text("Please Select");

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
        $("[name='crud_action_type']").val('search');
        $('#mainForm').submit();
    }

    function sortRecords(sortFieldName, sortType) {
        $("[name='crud_action_value']").val(sortFieldName);
        $("[name='crud_action_additional']").val(sortType);
        $("[name='crud_action_type']").val('search');
        $('#mainForm').submit();
    }


    var fullDetailsView = function (submissionNo) {

        showWaiting();
        $("[name='crud_action_value']").val(submissionNo);
        $("[name='crud_action_type']").val('info');
        $('#mainForm').submit();
    }

    var dividajaxlist = [];

    var getVssByIdType = function (patientIdNo,patientIdType, divid) {
        showWaiting();
        var advfiltersonList=$("[id^='advfilterson']");
        var lenSon = advfiltersonList.length;
        for (var i = 0;i<lenSon;i++){
            var hideSon = $(advfiltersonList[i]);
            if(hideSon.prop('id') !=='advfilterson'+divid){
                hideSon.hide();
            }
        }
        var advfilterList=$("a[data-target^='#dropdown']");
        var len = advfilterList.length;
        for (var j = 0;j<len;j++){
            var hide = $(advfilterList[j]);
            if(hide.prop('data-target') !=='#dropdown'+divid){
                hide.addClass('collapsed')
                hide.prop('aria-expanded', false);
            }
        }
        var dropdownList=$("[id^='dropdown']");
        var lendropdown = dropdownList.length;
        for (var k = 0;k<lendropdown;k++){
            var dropdown = $(dropdownList[k]);
            if(dropdown.prop('id') !=='dropdown'+divid){
                dropdown.removeClass('in')
                dropdown.prop('aria-expanded', false);
            }
        }
        if (!isInArray(dividajaxlist,divid)) {
            groupAjax(patientIdNo,patientIdType, divid);
        } else {
            var display = $('#advfilterson' + divid).css('display');
            if (display == 'none') {
                $('#advfilterson' + divid).show();
            } else {
                $('#advfilterson' + divid).hide();
            }
        }
        dismissWaiting();
    };
    function isInArray(arr,value){
        for(var i = 0; i < arr.length; i++){
            if(value === arr[i]){
                return true;
            }
        }
        return false;
    }
    var groupAjax = function (patientIdNo,patientIdType, divid) {
        dividajaxlist.push(divid);
        $.post(
            '/hcsa-licence-web/hcsa/enquiry/ar/vssDetail.do',
            {patientIdNo: patientIdNo,
                patientIdType: patientIdType
            },
            function (data) {
                let result = data.result;
                if('Success' == result) {
                    let res = data.ajaxResult;
                    let html = '<tr style="background-color: #F3F3F3;" class="p" id="advfilterson' + divid + '">' +
                        '<td colspan="9" class="hiddenRow">' +
                        '<div class="accordian-body collapse in" id="dropdown' + divid + '" >' +
                        '<table class="table application-item" style="background-color: #F3F3F3;" >' +
                        '<thead>' +
                        '<tr>';

                    html += '<th colspan="9" style=" text-align: center">Previous Submissions</th>' +
                        '</tr>' +
                        '</thead>' +
                        '<tbody>';
                    for (let i = 0; i < res.rowCount; i++) {
                        var color = "black";

                        html += '<tr style = "color : ' + color + ';">';

                        html += '<td width="15.6%" style="vertical-align:middle;"><p>' + res.rows[i].centerName + '<p></td>';
                        html += '<td width="10.3%" style="vertical-align:middle;"><p><a href="#" onclick="javascript:fullDetailsView(' + "'" + res.rows[i].submissionNo + "'" + ');">' + res.rows[i].submissionNo + '</a></p></td>';

                        html += '</p></td>' +
                            '<td width="9.4%" style="vertical-align:middle;"><p>' + res.rows[i].patientName + '<p></td>' +
                            '<td width="8.6%" style="vertical-align:middle;"><p>' + res.rows[i].patientIdType + '<p></td>' +
                            '<td width="10.4%" style="vertical-align:middle;"><p>' + res.rows[i].patientIdNo + '</p></td>' +
                            '<td width="11%" style="vertical-align:middle;"><p>' + res.rows[i].patientBirthdayStr + '</p></td>' +
                            '<td width="8.3%" style="vertical-align:middle;"><p>' + res.rows[i].maritalStatus + '</p></td>' +
                            '<td width="15%" style="vertical-align:middle;"><p>' + res.rows[i].sterilisationReason + '</p></td>' +
                            '<td width="9.4%" style="vertical-align:middle;"><p>' + res.rows[i].submitDtStr + '</p></td>' +
                            '</tr>';
                    }
                    html += '</tbody></table></div></td></tr>';
                    $('#advfilter' + divid).after(html);
                }
            }
        )

    };
</script>