<%--
  Created by IntelliJ IDEA.
  User: yichen
  Date: 11/8/2019
  Time: 10:27 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://www.ecq.com/iais"   prefix="iais"%>
<webui:setLayout name="iais-intranet"/>


<%
  sop.webflow.rt.api.BaseProcessClass process =
          (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>
<style>
  .form-check-gp{
    width: 50%;
    float:left;
  }

</style>

<form id = "mainForm" method = "post" action=<%=process.runtime.continueURL()%>>
  <%@ include file="/include/formHidden.jsp" %>
  <input type="hidden" name="crud_action_type" value="">
  <input type="hidden" name="crud_action_value" value="">
  <input type="hidden" name="crud_action_additional" value="">

  <div class="main-content">
    <div class="container">

      <br><br><br>

          <div>
            <span id="error_configCustomValidation" name="iaisErrorMsg" class="error-msg"></span>

          </div>
      <br><br>
      <div class="form-horizontal">
        <div class="form-group">

              <div class="col-xs-12">
                Common  &nbsp; <input class="form-check-input"  <c:if test="${common == true}"> checked="checked"</c:if> id="commmon" type="radio" name="common" aria-invalid="false" value="1"> General Regulation
              </div>
              <br><br>
              <div class="col-xs-12">
                Service Name <iais:select name="svcName" id="svcName" options="svcNameSelect" firstOption="Select Service Name" value="${svcName}"></iais:select>
              </div>

              <div class="col-xs-12">
                Service Sub Type <iais:select name="svcSubType" id="svcSubType" options="subtypeSelect" firstOption="Select Service Sub Type" value="${svcSubType}"></iais:select>
              </div>

          <div class="col-xs-12">
            Module <iais:select name="module" id="module" codeCategory="CATE_ID_CHECKLIST_MODULE" firstOption="Select Module" value="${module}"></iais:select>
          </div>

          <div class="col-xs-12">
            Type <iais:select name="type" id="type" codeCategory="CATE_ID_CHECKLIST_TYPE" firstOption="Select Type" value="${type}"></iais:select>
          </div>


          <div class="col-xs-12">
            Effective Start Date <iais:datePicker name = "eftStartDate" value="${eftStartDate}"></iais:datePicker>
          </div>

          <div class="col-xs-12">
            Effective End Date <iais:datePicker name = "eftEndDate" value="${eftEndDate}"></iais:datePicker>
          </div>

        </div>

      </div>

      <div class="application-tab-footer">
        <div class="row">
          <div class="col-xs-12 col-sm-6">
            <p><a class="back" href="#" onclick="doBack()"><i class="fa fa-angle-left"></i> Back</a></p>
          </div>
          <div class="col-xs-12 col-sm-6">
            <div class="text-right text-center-mobile">
              <a class="btn btn-primary next"  onclick="javascript: doNext();">Next</a>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>

</>

<%@include file="/include/validation.jsp"%>
<script type="text/javascript">
    function doNext() {
        SOP.Crud.cfxSubmit("mainForm","nextPage");
    }

    function doBack(){
        SOP.Crud.cfxSubmit("mainForm","backLastPage");
    }
</script>