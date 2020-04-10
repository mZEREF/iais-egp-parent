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
  String webroot=IaisEGPConstant.BE_CSS_ROOT;
%>
<div class="dashboard" style="background-image:url('<%=webroot%>img/Masthead-banner.jpg')">
  <form method="post" id="mainSpecInspDateForm" action=<%=process.runtime.continueURL()%>>
    <%@ include file="/include/formHidden.jsp" %>
    <input type="hidden" name="apptInspectionDateType" value="">
    <input type="hidden" id="actionValue" name="actionValue" value="">
    <input type="hidden" id="hours" name="hours" value="">
    <input type="hidden" id="amPm" name="amPm" value="">
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
                  <iais:row>
                    <iais:field value="Date"/>
                    <iais:value width="7">
                      <div class="col-xs-12 col-md-4">
                        <iais:datePicker id = "specificDate" name = "specificDate" value="${apptInspectionDateDto.specificDate}"></iais:datePicker>
                      </div><br>
                      <div class="col-xs-12 col-md-3">
                        <iais:select name="hoursOption" options="hoursOption" firstOption="Please select" value="${apptInspectionDateDto.hours}" onchange="javascript:apptInspectionDateHours(this.value)"></iais:select>
                      </div>
                      <div class="col-xs-12 col-md-3">
                        <iais:select name="amPmOption" options="amPmOption" firstOption="Please select" value="${apptInspectionDateDto.amPm}" onchange="javascript:apptInspectionDateAmPm(this.value)"></iais:select>
                      </div>
                      <br><span class="error-msg" name="iaisErrorMsg" id="error_specificDate"></span>
                      <br><span class="error-msg" name="iaisErrorMsg" id="error_hours"></span>
                      <br><span class="error-msg" name="iaisErrorMsg" id="error_amPm"></span>
                    </iais:value>
                  </iais:row>
                  <iais:action>
                    <button class="btn btn-primary" style="float:right" type="button" onclick="javascript:apptInspectionSpecDateConfirm()">Confirm</button>
                    <a class="back" id="Back" onclick="javascript:apptInspectionSpecDateBack()" style="float:left"><em class="fa fa-angle-left"></em> Back</a>
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
<%@ include file="/include/validation.jsp" %>
<script type="text/javascript">
    function apptInspectionSpecDateSubmit(action){
        $("[name='apptInspectionDateType']").val(action);
        var mainPoolForm = document.getElementById('mainSpecInspDateForm');
        mainPoolForm.submit();
    }

    function apptInspectionDateHours(value) {
        $("#hours").val(value);
    }

    function apptInspectionDateAmPm(value) {
        $("#amPm").val(value);
    }

    function apptInspectionSpecDateConfirm() {
        showWaiting();
        $("#actionValue").val('success');
        apptInspectionSpecDateSubmit("success");
    }

    function apptInspectionSpecDateBack() {
        showWaiting();
        $("#actionValue").val('back');
        apptInspectionSpecDateSubmit("back");
    }
</script>
