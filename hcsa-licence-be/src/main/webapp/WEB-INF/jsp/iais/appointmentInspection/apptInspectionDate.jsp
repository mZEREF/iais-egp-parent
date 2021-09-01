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
  String webroot=IaisEGPConstant.CSS_ROOT + IaisEGPConstant.BE_CSS_ROOT;
%>
<style type="text/css">
  li.apptInspScheduleUl:before {
    background-color: #333333;
  }
</style>
<div class="dashboard" style="background-image:url('<%=webroot%>img/Masthead-banner.jpg')">
  <form method="post" id="mainInspDateForm" action=<%=process.runtime.continueURL()%>>
    <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
    <input type="hidden" name="apptInspectionDateType" value="">
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
                                        <iais:message key="GENERAL_ACK018" escape="true"></iais:message>
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
                            <c:if test="${'APTY002' eq applicationViewDto.applicationDto.applicationType}">
                              <div class="row">
                                <div class="col-md-2">
                                  <label style="font-size: 16px">Licence Start Date</label>
                                </div>
                                <div class="col-md-6">
                                  <c:if test="${applicationViewDto.recomLiceStartDate != null}">
                                    <span style="font-size: 16px"><fmt:formatDate value='${applicationViewDto.recomLiceStartDate}' pattern='dd/MM/yyyy' /></span>
                                  </c:if>
                                  <c:if test="${applicationViewDto.recomLiceStartDate == null}">
                                    <span style="font-size: 16px">-</span>
                                  </c:if>
                                </div>
                              </div>
                            </c:if>
                            <div class="row">
                              <div class="col-md-2">
                                <label style="font-size: 16px">Fast Tracking?</label>
                              </div>
                              <div class="col-md-6">
                                <input disabled type="checkbox" <c:if test="${applicationViewDto.applicationDto.fastTracking}">checked="checked"</c:if>/>
                              </div>
                            </div>
                            <c:if test="${'SUCCESS' eq apptInspectionDateDto.actionButtonFlag && 'APTY007' ne applicationViewDto.applicationDto.applicationType}">
                              <div id="apptThreeInspDate">
                                <div class="row" id = "apptDateTitle">
                                  <div class="col-md-4">
                                    <label style="font-size: 16px">Available Appointment Date</label>
                                  </div>
                                </div>
                              </div>
                              <c:if test="${not empty apptInspectionDateDto.inspectionDate}">
                                <div class="row">
                                  <div class="col-md-6">
                                    <ul>
                                      <c:forEach var="insepctionDate" items="${apptInspectionDateDto.inspectionDate}">
                                        <li class="apptInspScheduleUl"><span style="font-size: 16px"><c:out value="${insepctionDate}"/></span></li>
                                      </c:forEach>
                                    </ul>
                                  </div>
                                </div>
                              </c:if>
                              <iais:action>
                                <a style="float:left;padding-top: 1.1%;" class="back" href="/main-web/eservice/INTRANET/MohHcsaBeDashboard?dashProcessBack=1"><em class="fa fa-angle-left"></em> Back</a>
                                <button id="apptSpecInspDate" class="btn btn-primary" style="float:right" type="button" onclick="javascript:apptInspectionDateSpecific()">Assign Specific Date</button>
                                <button id="disApptSpecInspDate" class="btn btn-primary disabled" style="float:right" type="button">Assign Specific Date</button>
                                <span style="float:right">&nbsp;</span>
                                <button id="disApptSysInspDate" class="btn btn-primary disabled" disabled style="float:right" type="button">Confirm System-proposed Date</button>
                                <button id="apptSysInspDate" class="btn btn-primary" style="float:right" type="button" onclick="javascript:apptInspectionDateConfirm()">Confirm System-proposed Date</button>
                              </iais:action>
                            </c:if>
                            <c:if test="${'SUCCESS' eq apptInspectionDateDto.actionButtonFlag && 'APTY007' eq applicationViewDto.applicationDto.applicationType}">
                              <iais:action>
                                <a style="float:left;padding-top: 1.1%;" class="back" href="/main-web/eservice/INTRANET/MohHcsaBeDashboard?dashProcessBack=1"><em class="fa fa-angle-left"></em> Back</a>
                                <button class="btn btn-primary" style="float:right" type="button" onclick="javascript:apptInspectionDateSpecific()">Assign Specific Date</button>
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
<script type="text/javascript">
    $(document).ready(function() {
        var sysInspDateFlag = $("#sysInspDateFlag").val();
        var sysSpecDateFlag = $("#sysSpecDateFlag").val();
        if('true' == sysInspDateFlag){
            $("#disApptSysInspDate").hide();
            $("#apptSysInspDate").show();
            $("#apptThreeInspDate").show();
        } else {
            $("#disApptSysInspDate").show();
            $("#apptSysInspDate").hide();
            $("#apptThreeInspDate").hide();
        }
        if('true' == sysSpecDateFlag){
            $("#disApptSpecInspDate").hide();
            $("#apptSpecInspDate").show();
        } else {
            $("#disApptSpecInspDate").show();
            $("#apptSpecInspDate").hide();
        }
        var apptBackShow = $("#apptBackShow").val();
        if('back' == apptBackShow){
            apptInspectionDateJump();
        }
    });

    function apptInspectionDateJump(){
        $("#apptInspTabInfo").removeClass('active');
        $("#apptInspTabDocuments").removeClass('active');
        $("#apptInspTabProcessing").removeClass('active');
        $('#apptInspectionDate').trigger("click");
        $("#apptInspectionDateProcess").click();
        $("#apptInspTabProcessing").addClass('active');
    }

    function apptInspectionDateSubmit(action){
        $("[name='apptInspectionDateType']").val(action);
        var mainPoolForm = document.getElementById('mainInspDateForm');
        mainPoolForm.submit();
    }

    function apptInspectionDateConfirm() {
        showWaiting();
        $("#actionValue").val('success');
        $("#processDec").val('REDECI017');
        apptInspectionDateSubmit("success");
    }

    function apptInspectionDateGetDate() {
        showWaiting();
        var sysInspDateFlag = $("#sysInspDateFlag").val();
        if('true' == sysInspDateFlag){
            $("#disApptSysInspDate").hide();
            $("#apptSysInspDate").show();
            $("#apptThreeInspDate").show();
            dismissWaiting();
        } else {
            $.post(
                '/hcsa-licence-web/online-appt/insp.date',
                function (data) {
                    dismissWaiting();
                    var ajaxFlag = data.buttonFlag;
                    var inspDateList = data.inspDateList;
                    var specButtonFlag = data.specButtonFlag;
                    if('true' == specButtonFlag){
                        $("#disApptSpecInspDate").hide();
                        $("#apptSpecInspDate").show();
                    } else {
                        $("#disApptSpecInspDate").show();
                        $("#apptSpecInspDate").hide();
                    }
                    if('true' == ajaxFlag){
                        $("#disApptSysInspDate").hide();
                        $("#apptSysInspDate").show();
                        $("#apptThreeInspDate").show();
                        var html = '<div class="row">' +
                            '<div class="col-md-6">' +
                            '<ul>';
                        for(var i = 0; i < inspDateList.length; i++){
                            html += '<li class="apptInspScheduleUl"><span style="font-size: 16px">' + inspDateList[i] + '</span></li>';
                        }
                        html += '</ul>' +
                            '</div>' +
                            '</div>';
                        $("#apptDateTitle").after(html);
                        $("#sysInspDateFlag").val('true');
                    } else {
                        $("#disApptSysInspDate").show();
                        $("#apptSysInspDate").hide();
                        $("#apptThreeInspDate").hide();
                    }
                }
            )
        }
    }

    function apptInspectionDateSpecific() {
        showWaiting();
        $("#actionValue").val('confirm');
        $("#processDec").val('REDECI018');
        apptInspectionDateSubmit("confirm");
    }
</script>


