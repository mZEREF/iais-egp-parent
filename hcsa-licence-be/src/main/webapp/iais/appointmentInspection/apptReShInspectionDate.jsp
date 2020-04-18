<%--
  Created by IntelliJ IDEA.
  User: ShiCheng_Xu
  Date: 2020/2/19
  Time: 10:15
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais"%>
<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>
<%
  //handle to the Engine APIs
  sop.webflow.rt.api.BaseProcessClass process =
          (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>
<webui:setLayout name="iais-intranet"/>

<%
  String webroot=IaisEGPConstant.BE_CSS_ROOT;
%>
<div class="dashboard" style="background-image:url('<%=webroot%>img/Masthead-banner.jpg')">
  <form method="post" id="mainInspDateForm" action=<%=process.runtime.continueURL()%>>
    <%@ include file="/include/formHidden.jsp" %>
    <input type="hidden" name="apptReSchInspDateType" value="">
    <input type="hidden" id="actionValue" name="actionValue" value="">
    <input type="hidden" id="processDec" name="processDec" value="${apptInspectionDateDto.processDec}">
    <div class="main-content">
      <div class="row">
        <div class="col-lg-12 col-xs-12">
          <div class="center-content">
            <div class="intranet-content">
              <iais:body >
                <div class="container">
                  <div class="col-xs-12">
                    <div class="tab-gp dashboard-tab">
                      <ul class="nav nav-tabs hidden-xs hidden-sm" role="tablist">
                        <li id="ReApptInspTabInfo" class="active" role="presentation"><a href="#tabInfo" aria-controls="tabInfo" role="tab"
                                                                                      data-toggle="tab">Info</a></li>
                        <li id="ReApptInspTabDocuments" class="complete" role="presentation"><a href="#tabDocuments"
                                                                                             aria-controls="tabDocuments" role="tab"
                                                                                             data-toggle="tab">Documents</a></li>
                        <li id="ReApptInspTabProcessing" class="incomplete" role="presentation"><a href="#tabProcessing"
                                                                                                aria-controls="tabProcessing" role="tab"
                                                                                                data-toggle="tab">Processing</a></li>
                      </ul>
                      <div class="tab-nav-mobile visible-xs visible-sm">
                        <div class="swiper-wrapper" role="tablist">
                          <div class="swiper-slide"><a href="#tabInfo" aria-controls="tabInfo" role="tab"
                                                       data-toggle="tab">Info</a></div>
                          <div class="swiper-slide"><a href="#tabDocuments" aria-controls="tabDocuments"
                                                       role="tab" data-toggle="tab">Documents</a></div>
                          <div class="swiper-slide"><a id="apptReInspectionDate" href="#tabProcessing" aria-controls="tabProcessing"
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
                              <h4>Appointment Re-Scheduling</h4>
                            </strong>
                          </div>
                          <iais:section title="" id = "inspection_date">
                            <iais:row>
                              <iais:field value="Available Appointment Dates"/>
                              <iais:value width="7">
                                <span style="font-size: 16px"><c:out value="${apptInspectionDateDto.apptFeSpecificDate}"/></span>
                              </iais:value>
                            </iais:row>
                            <iais:row>
                              <iais:field value="Reason"/>
                              <iais:value width="7">
                                <span style="font-size: 16px"><c:out value="${apptInspectionDateDto.apptFeReason}"/></span>
                              </iais:value>
                            </iais:row>
                            <div class="row">
                              <div class="col-md-1">
                                <label style="font-size: 16px">Date<span style="color: red"> *</span></label>
                              </div>
                              <div class="col-md-6">
                                <div class="col-xs-12 col-md-4">
                                  <iais:datePicker id = "specificDate" name = "specificDate" dateVal="${apptInspectionDateDto.specificDate}"></iais:datePicker>
                                </div>
                                <div class="col-xs-12 col-md-3">
                                  <iais:select name="hours" options="hoursOption" firstOption="--:--" value="${apptInspectionDateDto.hours}"></iais:select>
                                </div>
                              </div>
                            </div>
                            <iais:action >
                              <button class="btn btn-primary" style="float:right" type="button" onclick="javascript:apptReShInspectionDateConfirm()">Submit</button>
                            </iais:action>
                            <br>
                            <br>
                            <br>
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
                          </iais:section>
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
<%@ include file="/include/validation.jsp" %>
<%@ include file="../inspectionncList/uploadFile.jsp" %>
<script type="text/javascript">
    $(document).ready(function() {
        var actionValue = $("#actionValue").val();
        if(actionValue == "success"){
            apptReShInspectionDateHidOrSh();
        }
    });

    function apptReShInspectionDateHidOrSh(){
        var processDec = $("#processDec").val();
        if(processDec == "REDECI018"){
            $("#specificDate").show();
        } else {
            $("#specificDate").hide();
        }
    }

    function apptReShInspectionDateSubmit(action){
        $("[name='apptReSchInspDateType']").val(action);
        var mainPoolForm = document.getElementById('mainInspDateForm');
        mainPoolForm.submit();
    }

    function apptReShInspectionDateChange(value) {
        $("#processDec").val(value);
        apptReShInspectionDateHidOrSh();
    }

    function apptReShInspectionDateConfirm() {
        showWaiting();
        $("#actionValue").val('success');
        apptReShInspectionDateSubmit("success");
    }
</script>
