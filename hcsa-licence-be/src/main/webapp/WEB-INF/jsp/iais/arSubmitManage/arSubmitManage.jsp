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
                      <tr style="display: table-row;" id="advfilter${(status.index + 1) + (arMgrSearchParam.pageNo - 1) * arMgrSearchParam.pageSize}">
                        <td><input type="checkbox" id="patientId${status.index + 1}" name="patientId" value="${patient.patientCode}" onclick="javascript:chooseAllcheckBox('${status.index + 1}');"/></td>
                        <td><p style="white-space: nowrap;"><c:out value="${patient.patientName}"/>
                          <c:if test="${not empty patient.patientCode}">
                            <a href="javascript:void(0);" class="accordion-toggle  collapsed" style="float: right;color: #2199E8" data-toggle="collapse" data-target="#dropdown${(status.index + 1) + (arMgrSearchParam.pageNo - 1) * arMgrSearchParam.pageSize}" onclick="getPatientByPatientCode('${patient.patientCode}','${(status.index + 1) + (patientParam.pageNo - 1) * patientParam.pageSize}')">
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
          <iais:action>
            <a style="float:left;padding-top: 1.1%;text-decoration:none;" class="back" href="/main-web/eservice/INTRANET/MohHcsaBeDashboard?dashProcessBack=1"><em class="fa fa-angle-left"></em> Back</a>
            <div class="text-right">
              <a class="btn btn-primary btn-support" id="unlockBtn">Unlock</a>
            </div>
          </iais:action>
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
        $('input[name="submissionNoFilter"]').val("");
        $('input[name="submitDateFromFilter"]').val("");
        $('input[name="submitDateToFilter"]').val("");
        $("#arCenterFilter option:first").prop("selected", 'selected');
        $("#cycleStageFilter option:first").prop("selected", 'selected');
    })

    $("#searchBtn").click(function () {
        document.getElementById('crud_action_type').value = 'search';
        document.getElementById("mainForm").submit();
    })

    $("#unlockBtn").click(function () {
        document.getElementById('crud_action_type').value = 'unlock';
        document.getElementById("mainForm").submit();
    })

    var getPatientByPatientCode = function (patientCode, divid) {
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

    var groupAjax = function (patientCode, divid) {
        dividajaxlist.push(divid);
        $.post(
            '/hcsa-licence-web/hcsa/enquiry/ar/patientDetail.do',
            {patientCode: patientCode},
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


                    html += '<th width="5%"></th>' +
                        '<th width="5%">AR/IUI/OFO/SFO</th>' +
                        '<th width="10%">AR Treatment Cycle Type</th>' +
                        '<th width="15%">AR Centre</th>' +
                        '<th width="15%">Cycle Start Date</th>' +
                        '<th width="20%">Co-funding Claimed</th>' +
                        '<th width="10%">Status</th>'  +
                        '<th width="10%">Submission ID</th>'  +
                        '<th width="10%">Cycle Stage</th>'  +
                        '</tr>' +
                        '</thead>' +
                        '<tbody>';
                    for (let i = 0; i < res.rowCount; i++) {
                        var color = "black";

                        html += '<tr style = "color : ' + color + ';">';

                        html += '<td style="vertical-align:middle;"><input type="checkbox" name="subId" onclick="javascript:chooseFirstcheckBox(' + divid + ');" value="' + res.rows[i].submissionNo + '"></td>' +
                            '<td style="vertical-align:middle;"><p class="visible-xs visible-sm table-row-title">AR/IUI/OFO/SFO</p><p>' + res.rows[i].dsType + '<p></td>' +
                            '<td style="vertical-align:middle;"><p class="visible-xs visible-sm table-row-title">AR Treatment Cycle Type</p><p>' + res.rows[i].arTreatment + '<p></td>' +
                            '<td style="vertical-align:middle;"><p class="visible-xs visible-sm table-row-title">AR Centre</p><p>' + res.rows[i].arCentre + '<p></td>';
                        html += '<td style="vertical-align:middle;"><p class="visible-xs visible-sm table-row-title">Cycle Start Date</p><p>' + res.rows[i].cycleStartDateStr + '</p></td>';

                        html += '</p></td>' +
                            '<td style="vertical-align:middle;"><p class="visible-xs visible-sm table-row-title">Co-funding Claimed</p><p>' + res.rows[i].coFunding + '</p></td>' +
                            '<td style="vertical-align:middle;"><p class="visible-xs visible-sm table-row-title">Status</p><p>' + res.rows[i].status + '</p></td>' +
                            '<td style="vertical-align:middle;"><p class="visible-xs visible-sm table-row-title">Submission ID</p><p>' + res.rows[i].submissionNo + '</p></td>' +
                            '<td style="vertical-align:middle;"><p class="visible-xs visible-sm table-row-title">Cycle Stage</p><p>' + res.rows[i].stage + '</p></td>' +
                            '</tr>';
                    }
                    html += '</tbody></table></div></td></tr>';
                    $('#advfilter' + divid).after(html);
                }
            }
        )
    };

    function chooseAllcheckBox(id) {
        if ($('#patientId' + id).prop('checked')) {
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
            $('#patientId' + id).prop("checked", true)
        } else {
            $('#patientId' + id).prop("checked", false)
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