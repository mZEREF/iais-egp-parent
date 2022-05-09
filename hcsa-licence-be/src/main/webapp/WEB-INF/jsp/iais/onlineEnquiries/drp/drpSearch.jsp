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

    thead > tr > th > span {
        line-height: 0px;
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
                            <h2>Drug Practices Enquiry</h2>
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
                                                 cssClass="clearSel"  value="${dsEnquiryDrpFilterDto.centerName}"  />
                                </iais:value>
                            </iais:row>
                            <iais:row>
                                <iais:field width="4" value="Submission ID"/>
                                <iais:value width="4" cssClass="col-md-4" >
                                    <input type="text" maxlength="20" id="submissionNo"  name="submissionNo" value="${dsEnquiryDrpFilterDto.submissionNo}" >
                                </iais:value>
                            </iais:row>
                            <iais:row>
                                <iais:field width="4" value="Name of Patient"/>
                                <iais:value width="4" cssClass="col-md-4" >
                                    <input type="text" maxlength="20" id="patientName"  name="patientName" value="${dsEnquiryDrpFilterDto.patientName}" >
                                </iais:value>
                            </iais:row>
                            <iais:row>
                                <iais:field width="4" value="Patient ID Type"/>
                                <iais:value width="4" cssClass="col-md-4">
                                    <iais:select name="patientIdType" id="patientIdType" firstOption="Please Select" codeCategory="CATE_ID_DS_ID_TYPE"
                                                 cssClass="clearSel"   value="${dsEnquiryDrpFilterDto.patientIdType}" />
                                </iais:value>
                            </iais:row>
                            <iais:row>
                                <iais:field width="4" value="Patient ID Number"/>
                                <iais:value width="4" cssClass="col-md-4" >
                                    <input type="text" maxlength="20" id="patientIdNo"  name="patientIdNo" value="${dsEnquiryDrpFilterDto.patientIdNo}" >
                                </iais:value>
                            </iais:row>
                            <iais:row>
                                <iais:field width="4" value="Patient Date Of Birth"/>
                                <iais:value width="4" cssClass="col-md-4">
                                    <iais:datePicker id="birthDateFrom" name="birthDateFrom" dateVal="${dsEnquiryDrpFilterDto.birthDateFrom}"/>
                                </iais:value>
                                <label class="col-xs-1 col-md-1 control-label">To&nbsp;</label>
                                <iais:value width="3" cssClass="col-md-3">
                                    <iais:datePicker id="birthDateTo" name="birthDateTo" dateVal="${dsEnquiryDrpFilterDto.birthDateTo}"/>
                                </iais:value>
                            </iais:row>
                            <iais:row>
                                <iais:field width="4" value="Submission Date"/>
                                <iais:value width="4" cssClass="col-md-4">
                                    <iais:datePicker id="submissionDateFrom" name="submissionDateFrom" dateVal="${dsEnquiryDrpFilterDto.submissionDateFrom}"/>
                                </iais:value>
                                <label class="col-xs-1 col-md-1 control-label">To&nbsp;</label>
                                <iais:value width="3" cssClass="col-md-3">
                                    <iais:datePicker id="submissionDateTo" name="submissionDateTo" dateVal="${dsEnquiryDrpFilterDto.submissionDateTo}"/>
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

                            <iais:pagination param="drpParam" result="drpResult"/>
                            <div class="table-responsive">
                                <div class="table-gp">
                                    <table aria-describedby="" class="table application-group" style="border-collapse:collapse;">
                                        <thead>
                                        <tr >

                                            <iais:sortableHeader needSort="true" style="white-space: nowrap;padding: 15px 30px 15px 0px;"
                                                                 field="CENTER_NAME"
                                                                 value="Medical Clinic/Hospital"/>
                                            <iais:sortableHeader needSort="true" style="white-space: nowrap;padding: 15px 30px 15px 0px;"
                                                                 field="SUBMISSION_NO"
                                                                 value="Submission ID"/>
                                            <iais:sortableHeader needSort="true" style="white-space: nowrap;padding: 15px 30px 15px 0px;"
                                                                 field="PATIENT_NAME"
                                                                 value="Name of Patient"/>
                                            <iais:sortableHeader needSort="true" style="white-space: nowrap;padding: 15px 30px 15px 0px;"
                                                                 field="PATIENT_ID_TYPE"
                                                                 value="Patient ID Type"/>
                                            <iais:sortableHeader needSort="true" style="white-space: nowrap;padding: 15px 30px 15px 0px;"
                                                                 field="PATIENT_ID_NO"
                                                                 value="Patient ID Number"/>
                                            <iais:sortableHeader needSort="true" style="white-space: nowrap;padding: 15px 30px 15px 0px;"
                                                                 field="PATIENT_BIRTHDAY"
                                                                 value="Patient Date of Birth"/>
                                            <iais:sortableHeader needSort="true" style="white-space: nowrap;padding: 15px 30px 15px 0px;"
                                                                 field="SUBMIT_DT"
                                                                 value="Submission Date"/>
                                        </tr>
                                        </thead>
                                        <tbody class="form-horizontal">
                                        <c:choose>
                                            <c:when test="${empty drpResult.rows}">
                                                <tr>
                                                    <td colspan="15">
                                                        <iais:message key="GENERAL_ACK018"
                                                                      escape="true"/>
                                                    </td>
                                                </tr>
                                            </c:when>
                                            <c:otherwise>
                                                <c:forEach var="drp"
                                                           items="${drpResult.rows}"
                                                           varStatus="status">
                                                    <tr id="advfilter${(status.index + 1) + (drpParam.pageNo - 1) * drpParam.pageSize}">

                                                        <td  style="vertical-align:middle;">
                                                            <p style="white-space: nowrap;"><c:out value="${drp.centerName}"/>
                                                                <c:if test="${not empty drp.cdPatientCode}">
                                                                    <a href="javascript:void(0);" class="accordion-toggle  collapsed" style="float: right;color: #2199E8" data-toggle="collapse" data-target="#dropdown${(status.index + 1) + (drpParam.pageNo - 1) * drpParam.pageSize}" onclick="getDrpByIdType('${drp.patientCode}','${(status.index + 1) + (drpParam.pageNo - 1) * drpParam.pageSize}')">
                                                                    </a>
                                                                </c:if>
                                                            </p>

                                                        </td>
                                                        <td  style="vertical-align:middle;">
                                                            <a href="#" onclick="fullDetailsView('${drp.submissionNo}')">${drp.submissionNo}</a>
                                                        </td>
                                                        <td  style="vertical-align:middle;">
                                                            <c:out value="${drp.patientName}"/>
                                                        </td>
                                                        <td  style="vertical-align:middle;">
                                                            <iais:code code="${drp.patientIdType}"/>
                                                        </td>
                                                        <td  style="vertical-align:middle;">
                                                            <c:out value="${drp.patientIdNo}"/>
                                                        </td>
                                                        <td  style="vertical-align:middle;">
                                                            <fmt:formatDate
                                                                    value="${drp.patientBirthday}"
                                                                    pattern="${AppConsts.DEFAULT_DATE_FORMAT}"/>
                                                        </td>
                                                        
                                                        <td  style="vertical-align:middle;">
                                                            <fmt:formatDate
                                                                    value="${drp.submitDt}"
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
                                   href="${pageContext.request.contextPath}/hcsa/enquiry/ar/DRP-SearchResults-DownloadS">Download</a>
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

    var getDrpByIdType = function (patientCode, divid) {
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
            groupAjax(patientCode, divid);
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
    var groupAjax = function (patientCode, divid) {
        dividajaxlist.push(divid);
        $.post(
            '/hcsa-licence-web/hcsa/enquiry/ar/drpDetail.do',
            {patientCode: patientCode
            },
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

                    html += '<th >Name of Medical Clinic/Hospital</th>' +
                        '<th >Submission ID</th>' +
                        '<th >Drug Prescribed/Dispensed</th>' +
                        '<th >Type of Medication</th>' +
                        '<th >Doctor PRN</th>' +
                        '<th >Doctor Name</th>' +
                        '<th >Submission Date</th>' +
                        '</tr>' +
                        '</thead>' +
                        '<tbody>';
                    for (let i = 0; i < res.rowCount; i++) {
                        var color = "black";

                        html += '<tr style = "color : ' + color + ';">';

                        html += '<td  style="vertical-align:middle;"><p>' + res.rows[i].centerName + '<p></td>';
                        html += '<td  style="vertical-align:middle;"><p><a href="#" onclick="javascript:fullDetailsView(' + "'" + res.rows[i].submissionNo + "'" + ');">' + res.rows[i].submissionNo + '</a></p></td>';

                        html += '</p></td>' +
                            '<td  style="vertical-align:middle;"><p>' + res.rows[i].drugType + '<p></td>' +
                            '<td  style="vertical-align:middle;"><p>' + res.rows[i].medicationType + '<p></td>' +
                            '<td  style="vertical-align:middle;"><p>' + res.rows[i].doctorReignNo + '</p></td>' +
                            '<td  style="vertical-align:middle;"><p>' + res.rows[i].doctorName + '</p></td>' +
                            '<td  style="vertical-align:middle;"><p>' + res.rows[i].submitDtStr + '</p></td>' +
                            '</tr>';
                    }
                    html += '</tbody></table></div></td></tr>';
                    $('#advfilter' + divid).after(html);
                }
            }
        )

    };
</script>
