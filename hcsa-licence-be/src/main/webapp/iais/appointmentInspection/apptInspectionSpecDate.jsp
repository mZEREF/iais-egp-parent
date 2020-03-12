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
  <form method="post" id="mainInspDateForm" action=<%=process.runtime.continueURL()%>>
    <%@ include file="/include/formHidden.jsp" %>
    <br>
    <br>
    <br>
    <br>
    <br>
    <input type="hidden" name="apptInspectionDateType" value="">
    <input type="hidden" id="actionValue" name="actionValue" value="">
    <input type="hidden" id="hours" name="hours" value="">
    <input type="hidden" id="amPm" name="amPm" value="">
    <iais:body >
      <div class="container">
        <div class="col-xs-12">
          <div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">
            <h3>
              <span><u>Assign Specific Date</u></span>
            </h3>
            <div class="panel panel-default">
              <div class="panel-collapse collapse in" id="collapseOne" role="tabpanel" aria-labelledby="headingOne" aria-expanded="true" style="">
                <div class="panel-body">
                  <div class="panel-main-content">
                    <iais:section title="" id = "inspection_date">
                      <iais:row>
                        <iais:field value="Date"/>
                        <iais:value width="7">
                          <iais:datePicker id = "specificDate" name = "specificDate" value="${apptInspectionDateDto.specificDate}"></iais:datePicker>
                          <iais:select name="hoursOption" options="hoursOption" firstOption="Please select" value="${apptInspectionDateDto.hours}" onchange="javascript:apptInspectionDateHours(this.value)"></iais:select>
                          <iais:select name="amPmOption" options="amPmOption" firstOption="Please select" value="${apptInspectionDateDto.amPm}" onchange="javascript:apptInspectionDateAmPm(this.value)"></iais:select>
                          <br><span class="error-msg" name="iaisErrorMsg" id="error_specificDate"></span>
                          <br><span class="error-msg" name="iaisErrorMsg" id="error_hours"></span>
                          <br><span class="error-msg" name="iaisErrorMsg" id="error_amPm"></span>
                        </iais:value>
                      </iais:row>
                      <iais:action >
                        <button class="btn btn-lg btn-login-submit" style="float:right" type="button" onclick="javascript:apptInspectionSpecDateConfirm()">Confirm</button>
                        <button class="btn btn-lg btn-login-submit" style="float:left" type="button" onclick="javascript:apptInspectionSpecDateBack()">Back</button>
                      </iais:action>
                    </iais:section>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </iais:body>
  </form>
</div>
<%@ include file="/include/validation.jsp" %>
<script type="text/javascript">
    function apptInspectionSpecDateSubmit(action){
        $("[name='apptInspectionDateType']").val(action);
        var mainPoolForm = document.getElementById('mainInspDateForm');
        mainPoolForm.submit();
    }

    function apptInspectionDateHours(value) {
        $("#hours").val(value);
    }

    function apptInspectionDateAmPm(value) {
        $("#amPm").val(value);
    }

    function apptInspectionSpecDateConfirm() {
        $("#actionValue").val('success');
        apptInspectionDateSubmit("success");
    }

    function apptInspectionSpecDateBack() {
        $("#actionValue").val('back');
        apptInspectionDateSubmit("back");
    }
</script>
