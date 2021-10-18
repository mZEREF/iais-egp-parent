<%--
  Created by IntelliJ IDEA.
  User: ShiCheng_Xu
  Date: 2020/6/24
  Time: 13:29
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
  <form method="post" id="mainReSchedulingForm" action=<%=process.runtime.continueURL()%>>
    <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
    <input type="hidden" name="mohOfficerReSchedulingType" value="">
    <input type="hidden" id="actionValue" name="actionValue" value="">
    <div class="main-content">
      <div class="row">
        <div class="col-lg-12 col-xs-12">
          <div class="center-content">
            <div class="intranet-content">
              <div class="bg-title">
                <h2>
                  <span>Audit Date Re-scheduling</span>
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
                        <iais:datePicker id = "specificStartDate" name = "specificStartDate"></iais:datePicker>
                      </div>
                      <div class="col-xs-12 col-md-3">
                        <iais:select name="startHours" options="hoursOption" firstOption="--:--"></iais:select>
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
                        <iais:datePicker id = "specificEndDate" name = "specificEndDate"></iais:datePicker>
                      </div>
                      <div class="col-xs-12 col-md-3">
                        <iais:select name="endHours" options="endHoursOption" firstOption="--:--"></iais:select>
                      </div>
                    </div>
                  </div>
                  <div class="row">
                    <div class="col-md-1">
                      <label style="font-size: 16px"> </label>
                    </div>
                    <div class="col-md-6">
                      <div class="col-xs-12 col-md-7">
                        <span class="error-msg" name="iaisErrorMsg" id="error_specificDate"></span>
                      </div>
                    </div>
                  </div>
                  <div class="row" style="margin-bottom: 200px;"></div>
                  <iais:action>
                    <button class="btn btn-primary" style="float:right" type="button" onclick="javascript:auditReSchedulingConfirm()">Confirm</button>
                    <a href="#" class="back" id="Back" onclick="javascript:auditReSchedulingBack()" style="float:left"><em class="fa fa-angle-left"></em> Back</a>
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
    function auditReSchedulingSubmit(action){
        $("[name='mohOfficerReSchedulingType']").val(action);
        var mainPoolForm = document.getElementById('mainReSchedulingForm');
        mainPoolForm.submit();
    }

    function auditReSchedulingConfirm() {
        showWaiting();
        clearErrorMsg();
        $("#actionValue").val('success');
        auditReSchedulingSubmit("success");
    }

    function auditReSchedulingBack() {
        showWaiting();
        $("#actionValue").val('back');
        auditReSchedulingSubmit("back");
    }
</script>