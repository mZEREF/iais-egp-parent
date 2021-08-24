<%--
  Created by IntelliJ IDEA.
  User: ShiCheng_Xu
  Date: 2020/3/12
  Time: 16:36
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
  String webroot=IaisEGPConstant.CSS_ROOT + IaisEGPConstant.BE_CSS_ROOT;
%>
<div class="dashboard" style="background-image:url('<%=webroot%>img/Masthead-banner.jpg')">
  <form method="post" id="mainSpecInspDateForm" action=<%=process.runtime.continueURL()%>>
    <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
    <input type="hidden" name="apptInspectionDateType" value="">
    <input type="hidden" id="actionValue" name="actionValue" value="">
    <div class="main-content">
      <div class="row">
        <div class="col-lg-12 col-xs-12">
          <div class="center-content">
            <div class="intranet-content">
              <div class="bg-title">
                <h2>
                  <span>Assign Specific Date</span>
                </h2>
              </div>
              <iais:body>
                <iais:section title="" id = "inspection_date">
                  <div class="row">
                    <div class="col-md-1">
                      <label style="font-size: 16px">Date<span style="color: red"> *</span></label>
                    </div>
                    <div class="col-md-6">
                      <div class="col-xs-12 col-md-4">
                        <iais:datePicker id = "specificStartDate" name = "specificStartDate" dateVal="${apptInspectionDateDto.specificStartDate}"></iais:datePicker>
                      </div>
                      <div class="col-xs-12 col-md-3">
                        <iais:select name="startHours" options="hoursOption" firstOption="--:--" value="${apptInspectionDateDto.startHours}"></iais:select>
                      </div>
                    </div>
                  </div>
                  <div class="row">
                    <div class="col-md-1">
                      <label style="font-size: 16px"> </label>
                    </div>
                    <div class="col-md-6">
                      <div class="col-xs-12 col-md-4">
                        <span style="font-size: 16px">To</span>
                        <p></p>
                      </div>
                    </div>
                  </div>
                  <div class="row">
                    <div class="col-md-1">
                      <label style="font-size: 16px"></label>
                    </div>
                    <div class="col-md-6">
                      <div class="col-xs-12 col-md-4">
                        <iais:datePicker id = "specificEndDate" name = "specificEndDate" dateVal="${apptInspectionDateDto.specificEndDate}"></iais:datePicker>
                      </div>
                      <div class="col-xs-12 col-md-3">
                        <iais:select name="endHours" options="endHoursOption" firstOption="--:--" value="${apptInspectionDateDto.endHours}"></iais:select>
                      </div>
                    </div>
                  </div>
                  <div class="row">
                    <div class="col-md-1">
                      <label style="font-size: 16px"> </label>
                    </div>
                    <div class="col-md-6">
                      <div class="col-xs-12 col-md-5">
                        <span class="error-msg" name="iaisErrorMsg" id="error_specificDate"></span>
                      </div>
                    </div>
                  </div>
                  <div class="row" style="margin-bottom: 200px;"></div>
                  <iais:action>
                    <button class="btn btn-primary" style="float:right" type="button" onclick="javascript:apptInspectionSpecDateConfirm()">Confirm</button>
                    <a href="#" class="back" id="Back" onclick="javascript:apptInspectionSpecDateBack()" style="float:left"><em class="fa fa-angle-left"></em> Back</a>
                  </iais:action>
                </iais:section>
              </iais:body>
            </div>
          </div>
        </div>
      </div>
    </div>
  </form>
</div>
<%@ include file="/WEB-INF/jsp/include/validation.jsp" %>
<script type="text/javascript">
    function apptInspectionSpecDateSubmit(action){
        $("[name='apptInspectionDateType']").val(action);
        var mainPoolForm = document.getElementById('mainSpecInspDateForm');
        mainPoolForm.submit();
    }

    function apptInspectionSpecDateConfirm() {
        showWaiting();
        clearErrorMsg();
        $("#actionValue").val('success');
        apptInspectionSpecDateSubmit("success");
    }

    function apptInspectionSpecDateBack() {
        showWaiting();
        $("#actionValue").val('back');
        apptInspectionSpecDateSubmit("back");
    }
</script>
