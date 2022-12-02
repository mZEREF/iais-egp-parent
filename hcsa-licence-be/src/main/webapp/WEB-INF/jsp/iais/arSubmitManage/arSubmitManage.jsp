<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>
<%
  //handle to the Engine APIs
  sop.webflow.rt.api.BaseProcessClass process =
          (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-intranet"/>

<div class="main-content">
  <form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
    <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
    <input type="hidden" name="hcsaBeDashboardSwitchType" value="">
    <input type="hidden" id="action" name="action" value="">
    <input type="hidden" id="chkIdList" name="chkIdList" value="">
    <input type="hidden" id="taskId" name="taskId" value="">
    <div class="col-lg-12 col-xs-12">
      <div class="center-content">
        <div class="intranet-content">
          <div class="row form-horizontal">
            <div class="bg-title">
              <h2>Assisted Reproduction Submission Management</h2>
            </div>
            <div class="col-xs-12 col-md-12">
              <iais:row>
                <iais:field width="4" value="AR Centre"/>
                <iais:value width="4" cssClass="col-md-4" display="true">
                  <iais:select name="arCenterFilter" options="arMgrCenterOptsAttr"
                               firstOption="Please Select" needSort="true"
                               value="${arMgrSearchParam.params['arCenterFilter']}"></iais:select>
                </iais:value>
              </iais:row>
              <iais:row>
                <iais:field width="4" value="Submission ID"/>
                <iais:value width="4" cssClass="col-md-4" display="true">
                  <input type="text" name="submissionNoFilter" id="submissionNoFilter" value="${arMgrSearchParam.params['submissionNoFilter']}" maxlength="19"/>
                </iais:value>
              </iais:row>
              <iais:row>
                <iais:field width="4" value="Submission Type"/>
                <iais:value width="4" cssClass="col-md-4">
                  <p>Cycle Stages</p>
                </iais:value>
                <iais:value width="4" cssClass="col-md-4">
                  <iais:select name="cycleStageFilter" id="cycleStageFilter" firstOption="Please Select" options="arMgrStageOptsAttr" needSort="true"
                               value="${arMgrSearchParam.params['cycleStageFilter']}" cssClass="clearSel" />
                </iais:value>
              </iais:row>
              <iais:row>
                <iais:field width="4" value="Submission Date Range"/>
                <iais:value width="4" cssClass="col-md-4">
                  <iais:datePicker id="submitDateFromFilter" name="submitDateFromFilter" value="${arMgrSearchParam.params['submitDateFromFilter']}"/>
                </iais:value>
                <label class="col-xs-1 col-md-1 control-label">To&nbsp;</label>
                <iais:value width="3" cssClass="col-md-3">
                  <iais:datePicker id="submitDateToFilter" name="submitDateToFilter" value="${arMgrSearchParam.params['submitDateToFilter']}"/>
                </iais:value>
              </iais:row>
              <iais:action style="text-align:right;">
                <button class="btn btn-secondary" type="button" id="clearBtn" name="clearBtn">Clear</button>
                <button class="btn btn-primary" type="button" id="searchBtn" name="searchBtn">Search</button>
              </iais:action>
            </div>
          </div>
            <br>
            <br>
            <h3>
              <span>Search Results</span>
            </h3>
            <iais:pagination param="arMgrSearchParam" result="arMgrSearchResult"/>
            <div class="table-gp">
              <table aria-describedby="" class="table application-group" style="border-collapse:collapse;">
                <thead>
                <tr>
                  <th scope="col" style="display: none"></th>
                  <iais:sortableHeader needSort="false" field="" value=" "></iais:sortableHeader>                  <iais:sortableHeader needSort="false" field="NAME" value="Patient Name" param="arMgrSearchParam"></iais:sortableHeader>
                  <iais:sortableHeader needSort="false" field="ID_TYPE_DESC" value="Patient ID Type" param="arMgrSearchParam"></iais:sortableHeader>
                  <iais:sortableHeader needSort="false" field="ID_NUMBER" value="Patient ID No" param="arMgrSearchParam"></iais:sortableHeader>
                  <iais:sortableHeader needSort="false" field="DATE_OF_BIRTH" value="Patient Date of Birth" param="arMgrSearchParam"></iais:sortableHeader>
                  <iais:sortableHeader needSort="false" field="NATIONALITY_DESC" value="Patient Nationality" param="arMgrSearchParam"></iais:sortableHeader>
                </tr>
                </thead>
                <c:choose>
                  <c:when test="${empty arMgrSearchResult.rows}">
                    <tr>
                      <td colspan="6">
                        <iais:message key="GENERAL_ACK018" escape="true"></iais:message>
                      </td>
                    </tr>
                  </c:when>
                  <c:otherwise>
                    <c:forEach var="patient" items="${arMgrSearchResult.rows}" varStatus="status">
                      <tr style="display: table-row;">
                        <td><input type="checkbox" id="patientId${status}" name="patientId" value="<iais:mask name="patientCode" value="${patient.patientCode}"/>"/></td>
                        <td><p style="white-space: nowrap;"><c:out value="${patient.patientName}"/>
                          <c:if test="${not empty patient.patientCode}">
                            <a href="javascript:void(0);" class="accordion-toggle  collapsed" style="float: right;color: #2199E8" data-toggle="collapse" data-target="#dropdown${(status.index + 1) + (patientParam.pageNo - 1) * patientParam.pageSize}" onclick="getPatientByPatientCode('<iais:mask name="patientCode" value="${patient.patientCode}"/>','${(status.index + 1) + (patientParam.pageNo - 1) * patientParam.pageSize}')">
                            </a>
                          </c:if>
                        </p></td>
                        <td><c:out value="${patient.idTypeDesc}"/></td>
                        <td><c:out value="${patient.idNo}"/></td>
                        <td><fmt:formatDate value='${patient.birthday}' pattern='dd/MM/yyyy' /></td>
                        <td><c:out value="${patient.nationalityDesc}"/></td>
                      </tr>
                    </c:forEach>
                  </c:otherwise>
                </c:choose>
              </table>
            </div>
          </div>
      </div>
    </div>
    <iais:confirm msg="GENERAL_ERR0023"  needCancel="false" callBack="cancel()" popupOrder="support" ></iais:confirm>
    <iais:confirm msg=""  needCancel="false" callBack="aocancel()" popupOrder="approveAo" ></iais:confirm>
  </form>
</div>

<%@ include file="/WEB-INF/jsp/include/utils.jsp" %>
<script type="text/javascript">
    var dividajaxlist = [];

    $("#clearBtn").click(function () {
        $('input[name="application_no"]').val("");
        $('input[name="hci_code"]').val("");
        $('input[name="hci_address"]').val("");
        $('input[name="hci_name"]').val("");
        $('input[name="application_status"]').val("");
        $("#beInboxFilter .current").text("Please Select");
        $("#application_type option:first").prop("selected", 'selected');
        $("#application_status option:first").prop("selected", 'selected');
        $("#inspector_name option:first").prop("selected", 'selected');
        $("#searchCondition .current").text("Please Select");
    })

    $("#searchBtn").click(function () {
        document.getElementById('crud_action_type').value = 'search';
        document.getElementById("mainForm").submit();
    })

    function groupAjax(applicationGroupNo, divid) {
        dividajaxlist.push(divid);
        $.post(
            '/main-web/hcsa/intranet/dashboard/appGroup.do',
            {groupNo: applicationGroupNo},
            function (data) {
                let dashSupportFlag = data.dashSupportFlag;
                let result = data.result;
                if('Success' == result) {
                    let res = data.ajaxResult;
                    let html = '<tr style="background-color: #F3F3F3;" class="p" id="advfilterson' + divid + '">' +
                        '<td colspan="7" style="padding: 0px 8px !important;">' +
                        '<div class="accordian-body p-3 collapse in" id="dropdown' + divid + '" aria-expanded="true" style="">' +
                        '<table class="table application-item" style="background-color: #F3F3F3;margin-bottom:0px;" >' +
                        '<thead>' +
                        '<tr>';
                    if ("true" == dashSupportFlag) {
                        html += '<th scope="col" ><input type="checkbox" id="checkbox' + divid + '" onclick="chooseAllcheckBox(' + divid + ')" </th>';
                    }

                    html += '<th width="15%">Application No.</th>' +
                        '<th width="15%">Service</th>' +
                        '<th width="15%">Licence Expiry Date</th>' +
                        '<th width="15%">Application Status</th>' +
                        '<th width="15%">HCI Code</th>' +
                        '<th width="25%">HCI Name / Address</th>' +
                        '</tr>' +
                        '</thead>' +
                        '<tbody>';
                    for (let i = 0; i < res.rowCount; i++) {
                        var color = "black";
                        if ("black" == res.rows[i].kpiColor) {
                            color = "black";
                        } else if ("red" == res.rows[i].kpiColor) {
                            color = "red";
                        } else if ("amber" == res.rows[i].kpiColor) {
                            color = "#DD9C00";
                        }
                        html += '<tr style = "color : ' + color + ';">';
                        if ("true" == dashSupportFlag) {
                            html += '<td><input type="checkbox" name="taskId" id= "taskId" data-appNo="'+ res.rows[i].applicationNo+'" data-taskstatus = "' + res.rows[i].status + '" value="' + res.rows[i].taskMaskId + '" onclick="chooseFirstcheckBox(' + divid + ')"></td>'
                        }
                        let canDoTask = res.rows[i].canDoTask;
                        if('1' == canDoTask) {
                            html += '<td><p class="visible-xs visible-sm table-row-title">Application No.</p><p><a href="#" id="' + res.rows[i].taskMaskId + '" onclick="javascript:doDashboardTaskOrShow(' + "'" + res.rows[i].taskMaskId + "'" + ');">' + res.rows[i].applicationNo + '</a></p></td>';
                        } else if ('2' == canDoTask) {
                            html += '<td><p class="visible-xs visible-sm table-row-title">Application No.</p><p><a href="#" id="' + res.rows[i].taskMaskId + '" class="applicationNoAHref" data-href=' + res.rows[i].dashTaskUrl +' data-task=' + res.rows[i].taskMaskId +  '>' + res.rows[i].applicationNo + '</a></p></td>';
                        } else {
                            html += '<td><p class="visible-xs visible-sm table-row-title">Application No.</p><p><a href="#" id="' + res.rows[i].taskMaskId + '" onclick="javascript:dashboardAppViewShow(' + "'" + res.rows[i].id + "'" + ');">' + res.rows[i].applicationNo + '</a></p></td>';
                        }
                        html += '<td><p class="visible-xs visible-sm table-row-title">Service</p><p>' + res.rows[i].serviceName + '<p></td>' +
                            '<td><p class="visible-xs visible-sm table-row-title">Licence Expiry Date</p><p>' + res.rows[i].licenceExpiryDateStr + '<p></td>' +
                            '<td><p class="visible-xs visible-sm table-row-title">Application Status</p><p>' + res.rows[i].appStatusStrShow + '</p></td>' +
                            '<td><p class="visible-xs visible-sm table-row-title">HCI Code</p><p>' + res.rows[i].hciCode + '</p></td>' +
                            '<td><p class="visible-xs visible-sm table-row-title">HCI Name / Address</p><p>' + res.rows[i].hciAddress + '</p></td>' +
                            '</tr>';
                    }
                    html += '</tbody></table></div></td></tr>';
                    $('#advfilter' + divid).after(html);
                }
            }
        )
    }

    function doDashboardTaskOrShow(taskId) {
        showWaiting();
        $("#taskId").val(taskId);
        intraDashboardSubmit('comassign');
    }

    function dashboardAppViewShow(appPremCorrId) {
        showWaiting();
        $.post(
            '/main-web/hcsa/intranet/dashboard/applicationView.show',
            {appPremCorrId: appPremCorrId},
            function (data) {
                let dashAppShowFlag = data.dashAppShowFlag;
                if ('SUCCESS' == dashAppShowFlag) {
                    window.open ("/hcsa-licence-web/eservice/INTRANET/LicenceBEViewService");
                    dismissWaiting();
                } else {
                    dismissWaiting();
                }
            }
        )
        dismissWaiting();
    }

    function getAppByGroupId(applicationGroupNo, divid) {
        if (!isInArray(dividajaxlist,divid)) {
            groupAjax(applicationGroupNo, divid);
        } else {
            var display = $('#advfilterson' + divid).css('display');
            if (display == 'none') {
                $('#advfilterson' + divid).show();
            } else {
                $('#advfilterson' + divid).hide();
            }
        }
    }

    function trigger() {
        if ($("input:checkbox:checked").length > 0) {
            $('#action').val('trigger');
            $('#switchAction').val('approve');
            showWaiting();
            intraDashboardSubmit('approve');
        } else {
            $('#support').modal('show');
        }

    }

    function chooseAllcheckBox(id) {
        if ($('#checkbox' + id).prop('checked')) {
            $('#advfilterson' + id + ' input[type="checkbox"]').prop("checked", true)
        } else {
            $('#advfilterson' + id + ' input[type="checkbox"]').prop("checked", false)
        }
    }

    function chooseFirstcheckBox(id) {
        var divid = 'checkbox' + id;
        var flag = true;

        $('#advfilterson' + id + ' input[type="checkbox"]').each(function () {
            if ($(this).attr('id') != divid) {
                if (!$(this).is(':checked')) {
                    flag = false;
                }
            }
        });
        if (flag) {
            $('#checkbox' + id).prop("checked", true)
        } else {
            $('#checkbox' + id).prop("checked", false)
        }
    }

    function isInArray(arr,value){
        for(var i = 0; i < arr.length; i++){
            if(value === arr[i]){
                return true;
            }
        }
        return false;
    }

</script>