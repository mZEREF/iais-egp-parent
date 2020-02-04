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
    <input type="hidden" name="crud_action_type" value="">
    <input type="hidden" name="crud_action_value" value="">
    <input type="hidden" name="crud_action_additional" value="">
    <input type="hidden" name="currentValidateId" value="">

    <br><br>

    <c:if test="${switchPageAction == 'create'}">
      <div class="bg-title"><h2>Add Blacked Out Dates Form</h2></div>
    </c:if>
    <c:if test="${switchPageAction == 'update'}">
      <div class="bg-title"><h2>Update Blacked Out Dates Form</h2></div>
    </c:if>

    <span id="error_customValidation" name="iaisErrorMsg" class="error-msg"></span>


    <div class="form-horizontal">
      <c:if test="${switchPageAction == 'create'}">
        <div class="form-group">
          <div class="col-md-12">
            <label class="col-md-2">Group Name
            </label>
            <div class="col-md-7">
              <iais:select name="wrlGrpNameOpt" id="wrlGrpNameOpt" options="wrlGrpNameOpt"
                           firstOption="Please Select"></iais:select>
            </div>
          </div>
        </div>
      </c:if>

      <div class="form-group">
          <div class="col-md-12">
            <label class="col-md-2">Blacked Out Date: From
            </label>
            <div class="col-md-5">
              <iais:datePicker name="startDate" dateVal="${blackedOutDateAttr.startDate}"></iais:datePicker>
            </div>
          </div>
      </div>

      <div class="form-group">
          <div class="col-md-12">
            <label class="col-md-2">Blacked Out Date: To
            </label>
            <div class="col-md-5">
              <iais:datePicker name="endDate"
                               dateVal="${blackedOutDateAttr.endDate}"></iais:datePicker>
            </div>
          </div>
      </div>


      <div class="form-group">
          <div class="col-md-12">
            <label class="col-md-2">Blacked Out Date Description:
            </label>
            <div class="col-md-5">
              <input type="text" name="desc" maxlength="255"
                     value="${blackedOutDateAttr.desc}">
            </div>
          </div>
      </div>

      <%--<div class="form-group">
          <div class="col-md-12">
            <label class="col-md-2">Status:
            </label>
            <div class="col-md-5">
              <iais:select name="status" id="status" codeCategory="CATE_ID_COMMON_STATUS"
                           firstOption="Select Status" value="${itemRequestAttr.status}"></iais:select>
            </div>
        </div>
      </div>--%>

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
