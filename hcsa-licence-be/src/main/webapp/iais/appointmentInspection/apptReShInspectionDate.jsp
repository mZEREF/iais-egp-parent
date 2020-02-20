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
    <br>
    <br>
    <br>
    <br>
    <br>
    <input type="hidden" name="apptInspectionDateSwitch" value="">
    <input type="hidden" id="actionValue" name="actionValue" value="">
    <input type="hidden" id="processDec" name="processDec" value="${apptInspectionDateDto.processDec}">
    <iais:body >
      <div class="container">
        <div class="col-xs-12">
          <div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">
            <h3>
              <span>Choosing of Appointment Date</span>
            </h3>
            <div class="panel panel-default">
              <div class="panel-collapse collapse in" id="collapseOne" role="tabpanel" aria-labelledby="headingOne" aria-expanded="true" style="">
                <div class="panel-body">
                  <div class="panel-main-content">
                    <iais:section title="" id = "inspection_date">
                      <iais:row>
                        <iais:field value="Available Appointment Dates"/>
                        <iais:value width="7">
                          <label><c:out value="${apptInspectionDateDto.apptFeSpecificDate}"/></label>
                        </iais:value>
                      </iais:row>
                      <iais:row>
                        <iais:field value="Reason"/>
                        <iais:value width="7">
                          <label><c:out value="${apptInspectionDateDto.apptFeReason}"/></label>
                        </iais:value>
                      </iais:row>
                      <iais:row>
                        <iais:field value="Processing Decision" required="true"/>
                        <iais:value width="7">
                          <iais:select name="inspecProcessDec" options="inspecProDec" firstOption="Please select" value="${apptInspectionDateDto.processDec}" onchange="javascript:apptReShInspectionDateChange(this.value)"></iais:select>
                          <br><span class="error-msg" name="iaisErrorMsg" id="error_processDec"></span>
                        </iais:value>
                      </iais:row>
                      <div id = "specificDate">
                        <iais:row>
                          <iais:field value="Date"/>
                          <iais:value width="7">
                            <iais:datePicker id = "specificDate" name = "specificDate" value="${apptInspectionDateDto.specificDate}"></iais:datePicker>
                            <iais:select name="hoursOption" options="hoursOption" firstOption="Please select" value="${apptInspectionDateDto.hours}"></iais:select>
                            <iais:select name="amPmOption" options="amPmOption" firstOption="Please select" value="${apptInspectionDateDto.amPm}"></iais:select>
                            <br><span class="error-msg" name="iaisErrorMsg" id="error_specificDate"></span>
                            <br><span class="error-msg" name="iaisErrorMsg" id="error_hours"></span>
                            <br><span class="error-msg" name="iaisErrorMsg" id="error_amPm"></span>
                          </iais:value>
                        </iais:row>
                      </div>
                      <iais:action >
                        <button class="btn btn-lg btn-login-submit" style="float:right" type="button" onclick="javascript:apptReShInspectionDateConfirm()">Confirm</button>
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
    $(document).ready(function() {
        var actionValue = $("#actionValue").val();
        if(actionValue == "success"){
            apptReShInspectionDateHidOrSh();
        }
    });

    function apptReShInspectionDateHidOrSh(){
        var processDec = $("#processDec").val();
        if(processDec == "REDECI018"){
            $("#specificDate").hidden;
        } else {
            $("#specificDate").show();
        }
    }

    function apptReShInspectionDateSubmit(action){
        $("[name='apptReSchInspDateType']").val(action);
        var mainPoolForm = document.getElementById('mainInspDateForm');
        mainPoolForm.submit();
    }

    function apptReShInspectionDateChange(value) {
        $("#processDec").val(value);
        apptInspectionDateHidOrSh();
    }

    function apptReShInspectionDateConfirm() {
        $("#actionValue").val('success');
        apptReShInspectionDateSubmit("success");
    }
</script>
