<%--
  Created by IntelliJ IDEA.
  User: yichen
  Date: 1/6/2020
  Time: 5:30 PM
  To change this template use File | Settings | File Templates.
--%>

<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
  sop.webflow.rt.api.BaseProcessClass process =
          (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>

<webui:setLayout name="iais-intranet"/>
<div class="main-content">
  <form id="mainForm" method="post" action=<%=process.runtime.continueURL()%>>
    <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
    <input type="hidden" name="crud_action_type" value="">
    <input type="hidden" name="crud_action_value" value="">
    <input type="hidden" name="crud_action_additional" value="">
    <input type="hidden" name="currentValidateId" value="">

    <br><br>

    <div class="bg-title"><h2>Update Inspection Team's Weekly Non-Working Days</h2></div>


    <div class="form-horizontal">

      <div class="form-group">
        <div class="col-md-12">
          <iais:field value="Day:" required="false"></iais:field>
          <div class="col-md-3">
            &nbsp; &nbsp;<c:out value="${nonWorkingDayAttr.recursivceDate}"></c:out>
          </div>
        </div>
      </div>

      <div class="form-group">
        <div class="col-md-12">
          <iais:field value="AM Availability:" required="true"></iais:field>
          <c:if test="${nonWorkingDayAttr.am == true}">
            Yes &nbsp;<input type="radio" name="amAvailability" value="N" >
            No &nbsp;<input type="radio" name="amAvailability" value="Y" checked>
          </c:if>
          <c:if test="${nonWorkingDayAttr.am == false}">
            Yes &nbsp; <input type="radio" name="amAvailability" value="N" checked>
            No &nbsp;<input type="radio" name="amAvailability" value="Y" >
          </c:if>

          <span id="error_startAt" name="iaisErrorMsg" class="error-msg"></span>
        </div>
      </div>


      <div class="form-group">
        <div class="col-md-12">
          <iais:field value="PM Availability:" required="true"></iais:field>
          <c:if test="${nonWorkingDayAttr.pm == true}">
            Yes &nbsp;<input type="radio" name="pmAvailability" value="N" >
            No &nbsp;<input type="radio" name="pmAvailability" value="Y" checked>
          </c:if>
          <c:if test="${nonWorkingDayAttr.pm == false}">
            Yes &nbsp;<input type="radio" name="pmAvailability" value="N" checked>
            No &nbsp;<input type="radio" name="pmAvailability" value="Y" >
          </c:if>

          <span id="error_endAt" name="iaisErrorMsg" class="error-msg"></span>
      </div>


      <%--
            <div class="form-group">
              <div class="col-md-12">
                <label class="col-md-1">Status:
                </label>
                <div class="col-md-5">
                  <iais:select name="status" id="status" codeCategory="CATE_ID_COMMON_STATUS"
                               firstOption="Select Status" filterValue="CMSTAT002"
                               value="${nonWorkingDayAttr.status}"></iais:select>
                </div>
              </div>
              <span id="error_status" name="iaisErrorMsg" class="error-msg"></span>
            </div>
      --%>
    </div>

    <div class="col-xs-12 col-sm-6">
      <a href="#" class="back" onclick="doCancel();"><em class="fa fa-angle-left"></em> Back</a>
    </div>
    <div class="text-right text-center-mobile">
      <a class="btn btn-primary next" id="submitBtn">Submit</a>
    </div>

</div>
</form>
</div>

<%@include file="/WEB-INF/jsp/include/validation.jsp" %>
<script>
    function doCancel() {
        SOP.Crud.cfxSubmit("mainForm", "doBack");
    }

    submitBtn.onclick = function () {
        SOP.Crud.cfxSubmit("mainForm", "updateNonWorkingDay");
    }


</script>