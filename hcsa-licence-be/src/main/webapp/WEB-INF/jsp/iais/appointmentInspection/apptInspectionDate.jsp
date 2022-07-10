<%--
  Created by IntelliJ IDEA.
  User: ShiCheng_Xu
  Date: 2019/12/9
  Time: 9:40
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>
<%
  //handle to the Engine APIs
  sop.webflow.rt.api.BaseProcessClass process =
          (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>
<webui:setLayout name="iais-intranet"/>

<%
  String webroot = IaisEGPConstant.CSS_ROOT + IaisEGPConstant.BE_CSS_ROOT;
%>
<style type="text/css">
  li.apptInspScheduleUl:before {
    background-color: #333333;
  }
</style>
<%--@elvariable id="apptInspectionDateDto" type="com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptInspectionDateDto"--%>
<%--@elvariable id="applicationViewDto" type="com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto"--%>
<div class="dashboard" style="background-image:url('<%=webroot%>img/Masthead-banner.jpg')">
  <form method="post" id="mainInspDateForm" action=<%=process.runtime.continueURL()%>>
    <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
    <input type="hidden" id="actionValue" name="actionValue" value="">
    <input type="hidden" id="processDec" name="processDec" value="">
    <input type="hidden" id="sysInspDateFlag" name="sysInspDateFlag" value="${apptInspectionDateDto.sysInspDateFlag}">
    <input type="hidden" id="sysSpecDateFlag" name="sysSpecDateFlag" value="${apptInspectionDateDto.sysSpecDateFlag}">
    <input type="hidden" id="apptBackShow" name="apptBackShow" value="${apptBackShow}">
    <div class="main-content">
      <div class="row">
        <div class="col-lg-12 col-xs-12">
          <div class="center-content">
            <div class="intranet-content">
              <iais:body >
                <div class="">
                  <div class="col-xs-12">
                    <div class="tab-gp dashboard-tab">
                      <ul class="nav nav-tabs hidden-xs hidden-sm" role="tablist">
                        <li id="apptInspTabInfo" class="active" role="presentation"><a href="#tabInfo" aria-controls="tabInfo" role="tab"
                                                                                       data-toggle="tab">Info</a></li>
                        <li id="apptInspTabDocuments" class="complete" role="presentation"><a href="#tabDocuments"
                                                                                              aria-controls="tabDocuments" role="tab"
                                                                                              data-toggle="tab">Documents</a></li>
                        <li id="apptInspTabProcessing" class="incomplete" role="presentation"><a href="#tabProcessing" id = "apptInspectionDateProcess"
                                                                                                 aria-controls="tabProcessing" role="tab"
                                                                                                 data-toggle="tab" onclick="javascript:apptInspectionDateGetDate()">Processing</a></li>
                      </ul>
                      <div class="tab-nav-mobile visible-xs visible-sm">
                        <div class="swiper-wrapper" role="tablist">
                          <div class="swiper-slide"><a href="#tabInfo" aria-controls="tabInfo" role="tab"
                                                       data-toggle="tab">Info</a></div>
                          <div class="swiper-slide"><a href="#tabDocuments" aria-controls="tabDocuments"
                                                       role="tab" data-toggle="tab">Documents</a></div>
                          <div class="swiper-slide"><a id="apptInspectionDate" href="#tabProcessing" aria-controls="tabProcessing"
                                                       role="tab" data-toggle="tab">Processing</a></div>
                        </div>
                        <div class="swiper-button-prev"></div>
                        <div class="swiper-button-next"></div>
                      </div>
                      <div class="tab-content">
                        <div class="tab-pane active" id="tabInfo" role="tabpanel">
                          <%@ include file="../hcsaLicence/applicationInfo.jsp" %>
                        </div>
                        <div class="tab-pane" id="tabDocuments" role="tabpanel">
                          <%@ include file="../inspectionncList/tabDocuments.jsp" %>
                        </div>

                        <div class="tab-pane" id="tabProcessing" role="tabpanel">
                          <div class="alert alert-info" role="alert">
                            <strong>
                              <h4>Appointment Scheduling (Inspection)</h4>
                            </strong>
                          </div>
                          <iais:section title="" id = "inspection_date">
                            <div class="table-gp">
                              <table aria-describedby="" class="apptApp table">
                                <thead>
                                <tr>
                                  <th scope="col" >Application No</th>
                                  <th scope="col" >Application Status</th>
                                  <th scope="col" >MOH Officer(s)</th>
                                </tr>
                                </thead>
                                <tbody>
                                <c:choose>
                                  <c:when test="${empty apptInspectionDateDto.applicationInfoShow}">
                                    <tr>
                                      <td colspan="7">
                                        <iais:message key="GENERAL_ACK018" escape="true"/>
                                      </td>
                                    </tr>
                                  </c:when>
                                  <c:otherwise>
                                    <c:forEach var="appInfoShow" items="${apptInspectionDateDto.applicationInfoShow}">
                                      <tr>
                                        <td><c:out value="${appInfoShow.applicationNo}"/></td>
                                        <td><iais:code code="${appInfoShow.status}"/></td>
                                        <td>
                                          <c:if test="${appInfoShow.userDisName != null}">
                                            <c:forEach var="worker" items="${appInfoShow.userDisName}" varStatus="status">
                                              <c:out value="${worker}"/><br>
                                            </c:forEach>
                                          </c:if>
                                        </td>
                                      </tr>
                                    </c:forEach>
                                  </c:otherwise>
                                </c:choose>
                                </tbody>
                              </table>
                            </div>
                            <iais:row>
                              <iais:field value="Processing Decision" required="true"/>
                              <iais:value width="10" display="true">
                                <iais:select cssClass="nextStage" name="nextStage" id="nextStage"
                                             firstOption="Please Select"
                                             options="nextStages"
                                             value="${apptInspectionDateDto.processDec}"/>
                              </iais:value>
                            </iais:row>

                            <iais:row id="systemDateRow">
                              <iais:field value="Available Appointment Date" required="true" id="apptDateTitle"/>
                              <iais:value width="10" display="true">
                                <ul>
                                  <c:forEach var="insepctionDate" items="${apptInspectionDateDto.inspectionDate}">
                                    <li class="apptInspScheduleUl"><span style="font-size: 16px"><c:out
                                            value="${insepctionDate}"/></span></li>
                                  </c:forEach>
                                </ul>
                              </iais:value>
                            </iais:row>

                            <iais:row id="specDateRow">
                              <iais:field value="Date" required="true"/>
                              <iais:value width="10" display="true">
                                <div class="d-flex">
                                  <p>From</p>
                                  <div class="col-sm-4 col-md-3 col-xs-2">
                                    <iais:datePicker id="specificStartDate" name="specificStartDate"
                                                     dateVal="${apptInspectionDateDto.specificStartDate}"/>
                                  </div>
                                  <div class="col-sm-4 col-md-3 col-xs-2">
                                    <iais:select name="startHours" options="hoursOption" firstOption="--:--"
                                                 value="${apptInspectionDateDto.startHours}"/>
                                  </div>
                                  <p>To</p>
                                  <div class="col-sm-4 col-md-3 col-xs-2">
                                    <iais:datePicker id="specificEndDate" name="specificEndDate"
                                                     dateVal="${apptInspectionDateDto.specificEndDate}"/>
                                  </div>
                                  <div class="col-sm-4 col-md-3 col-xs-2">
                                    <iais:select name="endHours" options="endHoursOption" firstOption="--:--"
                                                 value="${apptInspectionDateDto.endHours}"/>
                                  </div>
                                </div>
                                <span class="error-msg" name="iaisErrorMsg" id="error_specificDate"></span>
                              </iais:value>
                            </iais:row>

                            <iais:row id="rollBackToRow">
                              <iais:field value="Roll Back To" required="true"/>
                              <iais:value width="10" display="true">
                                <iais:select name="rollBackTo" id="rollBackTo"
                                             firstOption="Please Select"
                                             options="rollBackOptions"
                                             value=""/>
                                <span id="error_rollBackTo1" class="error-msg"
                                      style="display: none;"><iais:message key="GENERAL_ERR0006"/></span>
                              </iais:value>
                            </iais:row>

                            <c:if test="${'APTY002' eq applicationViewDto.applicationDto.applicationType}">
                              <iais:row>
                                <iais:field value="Licence Start Date"/>
                                <iais:value width="10" display="true">
                                  <c:if test="${applicationViewDto.recomLiceStartDate != null}">
                                    <span style="font-size: 16px"><fmt:formatDate
                                            value='${applicationViewDto.recomLiceStartDate}'
                                            pattern='dd/MM/yyyy'/></span>
                                  </c:if>
                                  <c:if test="${applicationViewDto.recomLiceStartDate == null}">
                                    <span style="font-size: 16px">-</span>
                                  </c:if>
                                </iais:value>
                              </iais:row>
                            </c:if>

                            <iais:row>
                              <iais:field value="Fast Tracking?"/>
                              <iais:value width="10" display="true">
                                <p></p>
                                <input disabled type="checkbox"
                                       <c:if test="${applicationViewDto.applicationDto.fastTracking}">checked="checked"</c:if>/>
                                <p></p>
                              </iais:value>
                            </iais:row>

                            <c:if test="${'SUCCESS' eq apptInspectionDateDto.actionButtonFlag}">
                              <iais:action>
                                <a style="float:left;padding-top: 1.1%;" class="back"
                                   href="/main-web/eservice/INTRANET/MohHcsaBeDashboard?dashProcessBack=1"><em
                                        class="fa fa-angle-left"></em> Back</a>
                                <button id="submitBut" class="btn btn-primary" style="float:right" type="button"
                                        onclick="javascript:submitButFun()">
                                  SUBMIT
                                </button>
                              </iais:action>
                            </c:if>
                            <c:if test="${'SUCCESS' ne apptInspectionDateDto.actionButtonFlag}">
                              <iais:action>
                                <a style="float:left;padding-top: 1.1%;" class="back" href="/main-web/eservice/INTRANET/MohHcsaBeDashboard?dashProcessBack=1"><em class="fa fa-angle-left"></em> Back</a>
                              </iais:action>
                            </c:if>
                            <br><br><br>
                          </iais:section>
                          <%@include file="/WEB-INF/jsp/iais/inspectionncList/processHistory.jsp"%>
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
</div>
<%@ include file="../inspectionncList/uploadFile.jsp" %>
<%@ include file="/WEB-INF/jsp/include/validation.jsp" %>

<iais:confirm msg="INSPE_ACK001" popupOrder="confirmTag"
              cancelFunc="$('#confirmTag').modal('hide')" cancelBtnCls="btn btn-secondary" cancelBtnDesc="NO"
              callBack="$('#confirmTag').modal('hide');submit()" yesBtnCls="btn btn-primary" yesBtnDesc="YES"/>

<script type="text/javascript">
  $(document).ready(function () {
    changeNextStageFunc();
    var apptBackShow = $("#apptBackShow").val();
    if ('back' === apptBackShow) {
      apptInspectionDateJump();
    }
    $('#nextStage').change(changeNextStageFunc);

    <c:if test="${not empty errorMsg}">
    console.log('errormsg is not empty')
    $('#apptInspectionDateProcess').trigger('click');
    </c:if>
  });

  function apptInspectionDateJump() {
    $("#apptInspTabInfo").removeClass('active');
    $("#apptInspTabDocuments").removeClass('active');
    $("#apptInspTabProcessing").removeClass('active');
    $('#apptInspectionDate').trigger("click");
    $("#apptInspectionDateProcess").click();
    $("#apptInspTabProcessing").addClass('active');
  }

  function changeNextStageFunc() {
    let nextStageValue = $('#nextStage').find('option:selected').val();
    let systemDateRow = $('#systemDateRow');
    let specDateRow = $('#specDateRow');
    let rollBackToRow = $('#rollBackToRow');

    systemDateRow.hide();
    specDateRow.hide();
    rollBackToRow.hide();
    if ('REDECI017' === nextStageValue) {
      systemDateRow.show();
    } else if ('REDECI018' === nextStageValue) {
      specDateRow.show();
    } else if ('REDECI027' === nextStageValue) {
      rollBackToRow.show();
    }
  }

  function apptInspectionDateGetDate() {
    showWaiting();
    let sysInspDateFlag = $("#sysInspDateFlag");
    if ('true' === sysInspDateFlag.val()) {
      dismissWaiting();
    } else {
      $.post(
              '/hcsa-licence-web/online-appt/insp.date',
              function (data) {
                dismissWaiting();
                var ajaxFlag = data.buttonFlag;
                var inspDateList = data.inspDateList;
                var specButtonFlag = data.specButtonFlag;
                let nextStage = $('#nextStage');
                let nextStageOption = $("#nextStage option[value='']");
                let systemDate = $("#nextStage option[value='REDECI017']");
                let specificDate = $("#nextStage option[value='REDECI018']");
                if ('true' === specButtonFlag) {
                  if (specificDate.length === 0) {
                    nextStageOption.after("<option value='REDECI018'>Assign Specific Date</option>");
                    nextStage.niceSelect('update');
                  }
                } else {
                  if (specificDate.length > 0) {
                    specificDate.remove();
                    nextStage.niceSelect('update');
                  }
                }

                if ('true' === ajaxFlag) {
                  if (systemDate.length === 0) {
                    nextStageOption.after("<option value='REDECI017'>Use System-Proposed Date</option>")
                    nextStage.niceSelect('update');
                  }
                  var html = '<div class="row">' +
                          '<div class="col-md-6">' +
                          '<ul>';
                  for (var i = 0; i < inspDateList.length; i++) {
                    html += '<li class="apptInspScheduleUl"><span style="font-size: 16px">' + inspDateList[i] + '</span></li>';
                  }
                  html += '</ul>' +
                          '</div>' +
                          '</div>';
                  $("#apptDateTitle").after(html);
                  sysInspDateFlag.val('true');
                } else {
                  if (systemDate.length > 0) {
                    systemDate.remove();
                    nextStage.niceSelect('update');
                  }
                }

              }
      )
    }
  }

  function submitButFun() {
    clearErrorMsg();
    $("#error_rollBackTo1").hide();
    let nextStageValue = $('#nextStage').find('option:selected').val();
    $("#processDec").val(nextStageValue);
    if ('REDECI027' === nextStageValue) {
      const rollBackToVal = $("#rollBackTo").val();
      if(rollBackToVal === null || rollBackToVal === undefined || rollBackToVal === ''){
        $("#error_rollBackTo1").show();
      } else {
        $('#confirmTag').modal('show');
      }
    } else {
      submit();
    }
  }

  function submit() {
    showWaiting();
    document.getElementById('mainInspDateForm').submit();
  }
</script>


