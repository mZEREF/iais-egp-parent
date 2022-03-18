<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais"%>
<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>

<%@ page import="static sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT" %>
<%
  //handle to the Engine APIs
  sop.webflow.rt.api.BaseProcessClass process =
          (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>
<webui:setLayout name="iais-intranet"/>

<%
  String webroot=IaisEGPConstant.CSS_ROOT + IaisEGPConstant.BE_CSS_ROOT;
%>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-common.js"></script>
<div class="dashboard" style="background-image:url('<%=webroot%>img/Masthead-banner.jpg')">
  <form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
    <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
    <input type="hidden" name="apptReSchInspDateType" value="">
    <input type="hidden" id="actionValue" name="actionValue" value="${apptInspectionDateDto.actionValue}">
    <input type="hidden" id="processDec" name="processDec" value="${apptInspectionDateDto.processDec}">
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
                        <li id="reApptInspTabInfo" class="active" role="presentation"><a href="#tabInfo" aria-controls="tabInfo" role="tab"
                                                                                      data-toggle="tab">Info</a></li>
                        <li id="reApptInspTabDocuments" class="complete" role="presentation"><a href="#tabDocuments"
                                                                                             aria-controls="tabDocuments" role="tab"
                                                                                             data-toggle="tab">Documents</a></li>
                        <li id="reApptInspTabProcessing" class="incomplete" role="presentation"><a href="#tabProcessing"
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
                          <%@ include file="applicationInfo.jsp" %>
                        </div>

                        <div class="tab-pane" id="tabDocuments" role="tabpanel">
                          <%@ include file="../doDocument/tabDocuments.jsp" %>
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
                                <p><span style="font-size: 16px"><c:out value="${apptInspectionDateDto.apptFeSpecificDate}"/></span></p>
                              </iais:value>
                            </iais:row>
                            <iais:row>
                              <iais:field value="Reason"/>
                              <iais:value width="7">
                                <p><span style="font-size: 16px"><c:out value="${apptInspectionDateDto.apptFeReason}"/></span></p>
                              </iais:value>
                            </iais:row>
                            <div class="row">
                              <div class="col-md-4">
                                <label style="font-size: 16px">Date<span style="color: red"> *</span></label>
                              </div>
                              <div class="col-md-6">
                                <div class="col-xs-12 col-md-4" style="padding-left: 0px;">
                                  <iais:datePicker id = "specificStartDate" name = "specificStartDate" dateVal="${apptInspectionDateDto.specificStartDate}"></iais:datePicker>
                                </div>
                                <div class="col-xs-12 col-md-3">
                                  <iais:select name="startHours" options="hoursOption" firstOption="--:--" value="${apptInspectionDateDto.startHours}"></iais:select>
                                </div>
                              </div>
                            </div>
                            <div class="row">
                              <div class="col-md-4">
                                <label style="font-size: 16px"> </label>
                              </div>
                              <div class="col-md-6">
                                <div class="col-xs-12 col-md-4" style="padding-left: 0px;">
                                  <span style="font-size: 16px">To</span>
                                  <p></p>
                                </div>
                              </div>
                            </div>
                            <div class="row">
                              <div class="col-md-4">
                                <label style="font-size: 16px"></label>
                              </div>
                              <div class="col-md-6">
                                <div class="col-xs-12 col-md-4" style="padding-left: 0px;">
                                  <iais:datePicker id = "specificEndDate" name = "specificEndDate" dateVal="${apptInspectionDateDto.specificEndDate}"></iais:datePicker>
                                </div>
                                <div class="col-xs-12 col-md-3">
                                  <iais:select name="endHours" options="endHoursOption" firstOption="--:--" value="${apptInspectionDateDto.endHours}"></iais:select>
                                </div>
                              </div>
                            </div>
                            <div class="row">
                              <div class="col-md-4">
                                <label style="font-size: 16px"> </label>
                              </div>
                              <div class="col-md-6">
                                <div class="col-xs-12 col-md-6" style="padding-left: 0px;">
                                  <span class="error-msg" name="iaisErrorMsg" id="error_specificDate"></span>
                                </div>
                              </div>
                            </div>
                            <iais:action >
                              <a style="float:left;padding-top: 1.1%;" class="back" href="/bsb-be/eservicecontinue/INTRANET/MohBsbTaskList"><em class="fa fa-angle-left"></em> Back</a>
                              <button class="btn btn-primary" style="float:right" type="button" onclick="javascript:apptReShInspectionDateConfirm()">Submit</button>
                            </iais:action>
                            <br>
                            <br>
                            <br>
                          </iais:section>
                          <%@include file="/WEB-INF/jsp/iais/common/processHistory.jsp"%>
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
<%@include file="/WEB-INF/jsp/iais/doDocument/internalFileUploadModal.jsp"%>
<%@include file="/WEB-INF/jsp/iais/include/showErrorMsg.jsp"%>
<script type="text/javascript">
    $(document).ready(function() {
        var actionValue = $("#actionValue").val();
        if(actionValue == "success"){
            apptReShInspectionDateHidOrSh();
            apptReShInspectionDateJump();
        }
    });

    function apptReShInspectionDateJump(){
        $("#apptReInspectionDate").click();
        $("#reApptInspTabInfo").removeClass('active');
        $("#reApptInspTabDocuments").removeClass('active');
        $("#reApptInspTabProcessing").removeClass('active');
        $("#reApptInspTabProcessing").addClass('active');
    }

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
