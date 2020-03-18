<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %><%--
  Created by IntelliJ IDEA.
  User: yichen
  Date: 11/8/2019
  Time: 10:27 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://www.ecq.com/iais"   prefix="iais"%>
<%@ page contentType="text/html; charset=UTF-8" %>
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

<div class="main-content">
<form id = "mainForm" method = "post" action=<%=process.runtime.continueURL()%>>
  <%@ include file="/include/formHidden.jsp" %>
      <br><br><br>
          <div>
            <span id="error_configCustomValidation" name="iaisErrorMsg" class="error-msg"></span>
          </div>
      <br><br>
      <div class="form-horizontal">
          <div class="form-group">
            <iais:field value="Common" ></iais:field>
            <div class="col-md-3">
              <input class="form-check-input"  <c:if test="${common == true}"> checked="checked"</c:if> id="commmon" type="radio" name="common" aria-invalid="false" value="1"> General Regulation
              <%--<span id="error_regulationClauseNo" name="iaisErrorMsg" class="error-msg"></span>--%>
            </div>
          </div>

          <div class="form-group">
            <iais:field value="Service Name" ></iais:field>
            <div class="col-md-5">
              <iais:select name="svcName" id="svcName" options="svcNameSelect" firstOption="Select Service Name" value="${svcName}"></iais:select>
              <%--<span id="error_regulationClauseNo" name="iaisErrorMsg" class="error-msg"></span>--%>
            </div>
          </div>


          <div class="form-group">
            <iais:field value="Service Sub Type" ></iais:field>
            <div class="col-md-5">
              <iais:select name="svcSubType" id="svcSubType" options="subtypeSelect" firstOption="Select Service Sub Type" value="${svcSubType}"></iais:select>
              <%--<span id="error_regulationClauseNo" name="iaisErrorMsg" class="error-msg"></span>--%>
            </div>
          </div>

          <div class="form-group">
            <iais:field value="Module" ></iais:field>
            <div class="col-md-3">
              <iais:select name="module" id="module" codeCategory="CATE_ID_CHECKLIST_MODULE" firstOption="Select Module" value="${module}"></iais:select>
              <%--<span id="error_regulationClauseNo" name="iaisErrorMsg" class="error-msg"></span>--%>
            </div>
          </div>

          <div class="form-group">
            <iais:field value="Type" ></iais:field>
            <div class="col-md-3">
              <iais:select name="type" id="type" codeCategory="CATE_ID_CHECKLIST_TYPE" firstOption="Select Type" value="${type}"></iais:select>
              <%--<span id="error_regulationClauseNo" name="iaisErrorMsg" class="error-msg"></span>--%>
            </div>
          </div>

          <div class="form-group">
            <iais:field value="HCI Code" ></iais:field>
            <div class="col-md-3">
              <input type="text" name="hciCode" maxlength="7" value="${hciCode}"/>
            <span id="error_hciCode" name="iaisErrorMsg" class="error-msg"></span>
            </div>
          </div>

          <div class="form-group">
            <iais:field value="Effective Start Date" required="true"></iais:field>
            <div class="col-md-3">
               <iais:datePicker name = "eftStartDate" value="${eftStartDate}"></iais:datePicker>
              <span id="error_eftStartDate" name="iaisErrorMsg" class="error-msg"></span>
            </div>
          </div>

          <div class="form-group">
            <iais:field value="Effective End Date" required="true"></iais:field>
            <div class="col-md-3">
              <iais:datePicker name = "eftEndDate" value="${eftEndDate}"></iais:datePicker>
              <span id="error_eftEndDate" name="iaisErrorMsg" class="error-msg"></span>
            </div>
          </div>


      </div>

        <div class="row">
          <div class="col-xs-12 col-sm-6">
            <p><a class="back" onclick="doBack()();"><em class="fa fa-angle-left"></em> Back</a></p>
          </div>
          <div class="col-xs-12 col-sm-6">
            <div class="text-right text-center-mobile">
              <a class="btn btn-primary next" href="javascript:void(0);" onclick="Utils.clearClickStatus();">Clear</a>
              <a class="btn btn-primary next"  onclick="javascript: doNext();">Next</a>
            </div>
          </div>
        </div>
</>
</div>

<%@include file="/include/validation.jsp"%>
<%@include file="/include/utils.jsp"%>
<script type="text/javascript">
    function doNext() {
        SOP.Crud.cfxSubmit("mainForm","nextPage");
    }

    function doBack(){
        SOP.Crud.cfxSubmit("mainForm","backLastPage");
    }
</script>