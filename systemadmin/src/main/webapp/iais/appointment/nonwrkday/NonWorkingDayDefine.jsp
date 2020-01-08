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
    <%@ include file="/include/formHidden.jsp" %>
    <input type="hidden" name="crud_action_type" value="">
    <input type="hidden" name="crud_action_value" value="">
    <input type="hidden" name="crud_action_additional" value="">
    <input type="hidden" name="currentValidateId" value="">

    <br><br>

    <div class="bg-title"><h2>Update Inspection Team's Weekly Non-Working Days</h2></div>


    <div class="form-horizontal">

      <div class="form-group">
        <div class="col-md-12">
          <label class="col-md-1">Day:
          </label>
          <div class="col-md-5">
            ${nonWorkingDateDto.recursivceDate}
          </div>
        </div>
      </div>

      <div class="form-group">
        <div class="col-md-12">
          <label class="col-md-1">AM Availability
          </label>
          <div class="col-md-5">
            <select name="amAvailability" value="">
              <option value=>Please select</option>
              <option value="Y" >Y</option>
              <option value="N" >N</option>
            </select>
            <span id="error_startAt" name="iaisErrorMsg" class="error-msg"></span>
          </div>
        </div>
      </div>

      <div class="form-group">
        <div class="col-md-12">
          <label class="col-md-1">PM Availability
          </label>
          <div class="col-md-5">
            <select name="pmAvailability" value="">
              <option value=>Please select</option>
              <option value="Y">Y</option>
              <option value="N">N</option>
            </select>
            <span id="error_endAt" name="iaisErrorMsg" class="error-msg"></span>
          </div>
        </div>
      </div>


<%--
      <div class="form-group">
        <div class="col-md-12">
          <label class="col-md-1">Status:
          </label>
          <div class="col-md-5">
            <iais:select name="status" id="status" codeCategory="CATE_ID_COMMON_STATUS"
                         firstOption="Select Status" filterValue="CMSTAT002"
                         value="${nonWorkingDateDto.status}"></iais:select>
          </div>
        </div>
        <span id="error_status" name="iaisErrorMsg" class="error-msg"></span>
      </div>
--%>
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
        SOP.Crud.cfxSubmit("mainForm", "updateNonWorkingDay");
    }


</script>