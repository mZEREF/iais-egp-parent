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
<div class="dashboard" style="background-image:url('<%=webroot%>img/Masthead-banner.jpg')">
  <form method="post" id="mainAssignForm" action=<%=process.runtime.continueURL()%>>
    <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
    <input type="hidden" name="systemPoolAssignType" value="">
    <input type="hidden" id="checkUser" name="checkUser" value="${systemAssignTaskDto.checkUser}">
    <input type="hidden" id="checkWorkGroup" name="checkWorkGroup" value="${systemAssignTaskDto.checkWorkGroup}">
    <input type="hidden" name="actionValue" value="">
    <div class="main-content">
      <div class="row">
        <div class="col-lg-12 col-xs-12">
          <div class="center-content">
            <div class="intranet-content">
              <div class="bg-title">
                <h2>
                  <span>Task Details</span>
                </h2>
              </div>
              <iais:body >
                <iais:section title="" id = "assign_Task">
                  <div class="row">
                    <div class="col-md-2">
                      <label style="font-size: 16px">Application Number</label>
                    </div>
                    <div class="col-md-6">
                      <a style="font-size: 16px" href="/hcsa-licence-web/eservice/INTRANET/LicenceBEViewService" target="_blank">
                        <u><c:out value="${systemAssignTaskDto.applicationDto.applicationNo}"/></u>
                      </a>
                    </div>
                  </div>
                  <p></p>
                  <div class="row">
                    <div class="col-md-2">
                      <label style="font-size: 16px">Application Status</label>
                    </div>
                    <div class="col-md-6">
                      <span style="font-size: 16px"><iais:code code="${systemAssignTaskDto.applicationDto.status}"/></span>
                    </div>
                  </div>
                  <p></p>
                  <div class="row">
                    <div class="col-md-2">
                      <label style="font-size: 16px">HCI Code</label>
                    </div>
                    <div class="col-md-6">
                      <span style="font-size: 16px"><c:out value="${systemAssignTaskDto.hciCode}"/></span>
                    </div>
                  </div>
                  <p></p>
                  <div class="row">
                    <div class="col-md-2">
                      <label style="font-size: 16px">HCI Name / Address</label>
                    </div>
                    <div class="col-md-6">
                      <span style="font-size: 16px">${systemAssignTaskDto.hciName}</span>
                    </div>
                  </div>
                  <p></p>
                  <div class="row">
                    <div class="col-md-2">
                      <label style="font-size: 16px">Service Name</label>
                    </div>
                    <div class="col-md-6">
                      <span style="font-size: 16px"><c:out value="${systemAssignTaskDto.serviceName}"/></span>
                    </div>
                  </div>
                  <p></p>
                  <div class="row">
                    <div class="col-md-2">
                      <label style="font-size: 16px">Submission Date</label>
                    </div>
                    <div class="col-md-6">
                      <span style="font-size: 16px"><fmt:formatDate value='${systemAssignTaskDto.submitDt}' pattern='dd/MM/yyyy' /></span>
                    </div>
                  </div>
                  <p></p>
                  <div class="row">
                    <div class="col-md-2">
                      <label style="font-size: 16px">Work Group Name<span style="color: red"> *</span></label>
                    </div>
                    <div class="col-md-3">
                      <iais:select name="systemAssignWorkGroup" options="workGroupOptions" value="${systemAssignTaskDto.checkWorkGroup}" ></iais:select>
                      <span class="error-msg" name="iaisErrorMsg" id="error_checkWorkGroup"></span>
                    </div>
                  </div>
                  <p></p>
                  <div class="row">
                    <div class="col-md-2">
                      <label style="font-size: 16px"><c:out value="${groupRoleFieldDto.groupMemBerName}"/><span style="color: red"> *</span></label>
                    </div>
                    <div class="col-md-3">
                      <c:forEach var="workGroupNo" items="${systemAssignTaskDto.workGroupNos}">
                        <div id="workGroupNo${workGroupNo}">
                          <iais:select name="sysMohOfficerName${workGroupNo}" options="sysMohOfficerOption${workGroupNo}" firstOption="Please Select"></iais:select>
                        </div>
                      </c:forEach>
                      <span class="error-msg" name="iaisErrorMsg" id="error_systemUserCheck"></span>
                    </div>
                  </div>
                  <p></p>
                  <iais:action >
                    <a href="#" class="back" id="Back" onclick="javascript:doSystemUserAssignBack()" style="float:left"><em class="fa fa-angle-left"></em> Back</a>
                    <button class="btn btn-primary" style="float:right" type="button" onclick="javascript:doSystemUserAssignNext()">Next</button>
                  </iais:action>
                  <div style="margin-bottom: 200px;"></div>
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
<script>
    $(document).ready(function() {
        var checkUser = $("#checkUser").val();
        var checkWorkGroup = $("#checkWorkGroup").val();
        var numbers = $("#sysMohOfficerName" + checkWorkGroup).find("option");
        for (var j = 1; j < numbers.length; j++) {
            if ($(numbers[j]).val() == checkUser) {
                $(numbers[j]).attr("selected", "selected");
                var inspName = $(numbers[j]).text();
                $("#workGroupNo" + checkWorkGroup + " .current").text(inspName);
            }
        }
        $("#systemAssignWorkGroup option").each(function(){
            var value = $(this).val();
            $("#workGroupNo" + value).addClass("hidden");
        });
        $("#workGroupNo" + checkWorkGroup).removeClass("hidden");
    });

    $("#systemAssignWorkGroup").change(function systemAssignWorkGroupCheck() {
        var workGroupCheck = $("#systemAssignWorkGroup").val();
        $("#systemAssignWorkGroup option").each(function(){
            var value = $(this).val();
            $("#workGroupNo" + value).addClass("hidden");
        });
        $("#workGroupNo" + workGroupCheck).removeClass("hidden");
    });

    function doSystemUserAssignBack() {
        clearErrorMsg();
        showWaiting();
        $("[name='actionValue']").val('back');
        systemUserAssignSubmit('back');
    }

    function doSystemUserAssignNext() {
        clearErrorMsg();
        showWaiting();
        $("[name='actionValue']").val('confirm');
        systemUserAssignSubmit('confirm');
    }

    function systemUserAssignSubmit(action){
        $("[name='systemPoolAssignType']").val(action);
        var mainPoolForm = document.getElementById('mainAssignForm');
        mainPoolForm.submit();
    }
</script>

