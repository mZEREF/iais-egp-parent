<%--
  Created by IntelliJ IDEA.
  User: yichen
  Date: 12/28/2019
  Time: 2:21 PM
  To change this template use File | Settings | File Templates.
--%>

<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>
<%@ page import="java.util.Date" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.utils.Formatter" %>
<%
  sop.webflow.rt.api.BaseProcessClass process =
          (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>


<style>
  .nice-select {
    width: 30%;
  }
</style>
<webui:setLayout name="iais-intranet"/>
<div class="main-content">
  <form id="mainForm" method="post" action=<%=process.runtime.continueURL()%>>
    <%@ include file="/include/formHidden.jsp" %>
    <input type="hidden" name="currentValidateId" value="">

    <br><br>

    <c:if test="${switchPageAction == 'create'}">
      <div class="bg-title"><h2>Create Blacked Out Dates </h2></div>
    </c:if>
    <c:if test="${switchPageAction == 'update'}">
      <div class="bg-title"><h2>Update Blacked Out Dates </h2></div>
    </c:if>

    <span id="error_customValidation" name="iaisErrorMsg" class="error-msg"></span>


    <div class="form-horizontal">
      <c:if test="${switchPageAction == 'create'}">
        <div class="form-group">
          <div class="col-md-12">
            <label class="col-md-2">Blacked Out Date: From
            </label>
            <div class="col-md-5">
              <iais:select name="wrlGrpNameOpt" id="wrlGrpNameOpt" options="wrlGrpNameOpt"
                           firstOption="Please Select" value="${shortName}"></iais:select>
              <br><br>  <br><br><span id="error_shortName" name="iaisErrorMsg" class="error-msg"></span>
            </div>
          </div>
        </div>
      </c:if>




      <div class="form-group">
          <div class="col-md-12">
            <label class="col-md-2">Blacked Out Date Start :
            </label>
            <div class="col-md-5">
              <%Date ssd = Formatter.parseDate((String) request.getAttribute("startDate"));%>
              <iais:datePicker name="startDate"
                                dateVal="<%=ssd%>"
              ></iais:datePicker>
              <span id="error_startDate" name="iaisErrorMsg" class="error-msg"></span>
            </div>
          </div>
      </div>

      <div class="form-group">
          <div class="col-md-12">
            <label class="col-md-2">Blacked Out Date End:
            </label>
            <div class="col-md-5">
              <%Date esd = Formatter.parseDate((String) request.getAttribute("endDate"));%>
              <iais:datePicker name="endDate"
                                dateVal="<%=esd%>"></iais:datePicker>
            </div>
            <span id="error_endDate" name="iaisErrorMsg" class="error-msg"></span>
          </div>
      </div>


      <div class="form-group">
          <div class="col-md-12">
            <label class="col-md-2">Blacked Out Date Description:
            </label>
            <div class="col-md-5">
              <input type="text" max="255" name="desc"
                     value="${desc}">
              <span id="error_desc" name="iaisErrorMsg" class="error-msg"></span>
            </div>
          </div>
      </div>

      <div class="form-group">
          <div class="col-md-12">
            <label class="col-md-2">Status:
            </label>
            <div class="col-md-5">
              <iais:select name="status" id="status" codeCategory="CATE_ID_COMMON_STATUS"
                           firstOption="Select Status" filterValue="CMSTAT002" value="${status}"></iais:select>
            </div>
        </div>
      </div>

      <div class="col-xs-12 col-sm-6">
        <p><a class="back" onclick="doCancel();"><i class="fa fa-angle-left"></i> Back</a></p>
      </div>
      <div class="text-right text-center-mobile">
        <a class="btn btn-primary next" id="submitBtn">Submit</a>
      </div>

    </div>
  </form>
</div>

<%@include file="/include/validation.jsp" %>
<script>
    function doCancel() {
        SOP.Crud.cfxSubmit("mainForm", "doBack");
    }

    submitBtn.onclick = function () {
        var action = '${switchPageAction}' + "";
        if (action == null) {
            return;
        }

        if (action == "create") {
            SOP.Crud.cfxSubmit("mainForm", "createBlackedOutCalendar");
        }

        if (action == "update") {
            SOP.Crud.cfxSubmit("mainForm", "updateBlackedOutCalendar");
        }
    }

</script>
