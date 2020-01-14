<%@ page import="com.ecquaria.cloud.helper.EngineHelper" %><%--
  Created by IntelliJ IDEA.
  User: yichen
  Date: 11/8/2019
  Time: 10:27 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://www.ecq.com/iais"   prefix="iais"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
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

.form-inline .form-group {
  width: 30%;
  margin-bottom: 25px;
  display: inline-block;
  vertical-align: middle;
}

</style>
<form id = "mainForm" method = "post" action=<%=process.runtime.continueURL()%>>
  <%@ include file="/include/formHidden.jsp" %>
  <input type="hidden" name="crud_action_type" value="">
  <input type="hidden" name="crud_action_value" value="">
  <input type="hidden" name="crud_action_additional" value="">
  <div class="main-content">
    <div class="container">
      <div class="form-horizontal">
        <span id="error_configCustomValidation" name="iaisErrorMsg" class="error-msg"></span>
        <br><br>
        <div class="form-group">
          <div class="col-xs-12">
            <td>
              <label>
                Common  &nbsp; <input class="form-check-input " id="commmon" type="radio" name="module" aria-invalid="false" value="General Regulation"> General Regulation
              </label>
            </td>
          </div>
        </div>

      <div>
          <td>
            <iais:checkbox checkboxId="moduleCheckBox" codeCategory = "CATE_ID_CHECKLIST_MODULE" name= "moduleCheckBox" labelName = "Module" ></iais:checkbox>
          </td>


          <td>
            <iais:checkbox checkboxId="typeCheckBox" codeCategory = "CATE_ID_CHECKLIST_TYPE" forName="typeCheckBoxFor" name= "typeCheckBox" labelName = "Type" ></iais:checkbox>
          </td>
       </div>


        <br><br>
        <div class="form-group">
          <label class="col-md-2">Service Name &nbsp;</label>
          <div class="col-md-5">
            <iais:select name="svcName" id="svcName" options = "svcNameSelect" firstOption="Select Service Name" value="${svcName}"></iais:select>
          </div>
        </div>

        <div class="form-group">
          <label class="col-md-2">Service Sub Type &nbsp;</label>
          <div class="col-md-5">
            <iais:select name="svcSubType" id="svcSubType"   options = "subtypeSelect" firstOption="Select Sub Type Name" value="${svcSubType}"></iais:select>
          </div>
        </div>

      </div>




      <div class="components">
        <h2 class="component-title">Search &amp; Result</h2>
        <iais:pagination  param="checklistConfigSearch" result="checklistConfigResult"/>
        <div class="table-gp">
          <table class="table">
            <thead>
            <tr>
              <iais:sortableHeader needSort="false"  field="" value="No."></iais:sortableHeader>
              <td></td>
              <iais:sortableHeader needSort="true"   field="IS_COMMON" value="Common"></iais:sortableHeader>
              <iais:sortableHeader needSort="true"   field="type" value="Type"></iais:sortableHeader>
              <iais:sortableHeader needSort="true"   field="module" value="Module"></iais:sortableHeader>
              <iais:sortableHeader needSort="true"   field="service" value="Service"></iais:sortableHeader>
              <iais:sortableHeader needSort="true"   field="SUBTYPE_NAME" value="Service Sub-type"></iais:sortableHeader>
              <iais:sortableHeader needSort="true"   field="HCI_CODE" value="HCI Code"></iais:sortableHeader>
              <iais:sortableHeader needSort="true"   field="START_DATE" value="Effective Start Date"></iais:sortableHeader>
              <iais:sortableHeader needSort="true"   field="END_DATE" value="Effective End Date"></iais:sortableHeader>
              <iais:sortableHeader needSort="false"   field="action" value="Action"></iais:sortableHeader>

            </tr>
            </thead>
            <tbody>
            <tr>
              <c:forEach var = "config" items = "${checklistConfigResult.rows}" varStatus="status">
            <tr>
              <td class="row_no">${(status.index + 1) + (checklistConfigSearch.pageNo - 1) * checklistConfigSearch.pageSize}</td>
              <td></td>
              <td>${config.common}</td>
              <td>${config.type}</td>
              <td>${config.module}</td>
              <td>${config.svcName}</td>
              <td>${config.svcSubType}</td>
              <td>${config.hciCode}</td>
              <td><fmt:formatDate value="${config.eftStartDate}" pattern="MM/dd/yyyy"/></td>
              <td><fmt:formatDate value="${config.eftEndDate}" pattern = "MM/dd/yyyy"/></td>
              <td>
                <iais:link icon="form_edit" title="Edit" onclick="javascript:loadEditData('${config.id}');"/>
                <iais:link icon="form_edit" title="Clone" onclick="javascript:cloneConfig('${config.id}');"/>
                <iais:link icon="form_delete" title="Disable" onclick="javascript:ivActiveRecord('${config.id}');"/>
                <iais:link icon="form_view" title="View" onclick="javascript:doView('${config.id}');"/>
              </td>
            </tr>
            </c:forEach>
            </tr>
            </tbody>
          </table>

        </div>

        <div class="application-tab-footer">
              <td>
                <div class="text-right text-center-mobile">
                  <a class="btn btn-primary next" href="javascript:void(0);" onclick="Utils.clearClickStatus();">Clear</a>
                  <a class="btn btn-primary next" href="javascript:void(0);" onclick="javascript: doSearch();">Search</a>
                  <a class="btn btn-primary next" href="javascript:void(0);" onclick="javascript: prepareAddConfig();">Add Configuration</a>

                </div>

              </td>
        </div>

      </div>
    </div>
  </div>

</form>


<%@include file="/include/validation.jsp"%>
<script src="/hcsa-licence-web/iais/js/CommonUtils.js"></script>
<script type="text/javascript">

  function doSearch(){
    SOP.Crud.cfxSubmit("mainForm", "doSearch");
  }

  function ivActiveRecord(id){
      console.log("=========id==>>>>>>>", id);
    SOP.Crud.cfxSubmit("mainForm", "deleteRecord", id);
  }

  function doView(id){
    SOP.Crud.cfxSubmit("mainForm", "doView", id);
  }

  function loadEditData(id){
      console.log("=========prepareEditConfig==>>>>>>>", id);
    SOP.Crud.cfxSubmit("mainForm", "loadEditData", id);
  }

  function cloneConfig(id){
      console.log("=========prepareEditConfig==>>>>>>>", id);
    SOP.Crud.cfxSubmit("mainForm", "cloneConfig", id);
  }

  function prepareAddConfig(){
    SOP.Crud.cfxSubmit("mainForm", "prepareAddConfig");
  }

  function jumpToPagechangePage(){
      SOP.Crud.cfxSubmit("mainForm", "doPage");
  }

  function sortRecords(sortFieldName,sortType){
      SOP.Crud.cfxSubmit("mainForm","doSort",sortFieldName,sortType);
  }
</script>