<%@ page import="com.ecquaria.cloud.helper.EngineHelper" %>
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
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
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

.form-inline .form-group {
  width: 30%;
  margin-bottom: 25px;
  display: inline-block;
  vertical-align: middle;
}

</style>
<div class="main-content">
<form id = "mainForm" method = "post" action=<%=process.runtime.continueURL()%>>
  <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
  <input type="hidden" id="currentMaskId" name="currentMaskId" value="">

    <div class="center-content col-xs-12 col-md-12">
      <div class="form-horizontal">
        <span id="error_configCustomValidation" name="iaisErrorMsg" class="error-msg"></span>
        <br><br>
        <div class="form-group">
          <div class="col-xs-12">
            <tr>
              <td>
                <label>
                  Common  &nbsp;
                  <input class="form-check-input " id="commmon" type="radio" name="module" aria-invalid="false"
                      <c:if test="${param.module != null && param.module != ''}"> checked="checked"</c:if> value="General Regulation"
                      style="margin: 6px 0;">
                  <span style="margin-left: 20px;">General Regulation</span>
                </label>
              </td>
            </tr>

            <br><br>
            <tr>
              <td>
                <iais:checkbox checkboxId="moduleCheckBox" codeCategory = "CATE_ID_CHECKLIST_MODULE" name= "moduleCheckBox" labelName = "Module" request="${pageContext.request}"></iais:checkbox>
              </td>


              <td>
                <iais:checkbox checkboxId="typeCheckBox" codeCategory = "CATE_ID_CHECKLIST_TYPE" forName="typeCheckBoxFor" name= "typeCheckBox" labelName = "Type" request="${pageContext.request}"></iais:checkbox>
              </td>
            </tr>

          </div>
        </div>

        <div class="form-group">
          <label class="col-md-2" style="padding-top: 15px">Service</label>
          <div class="col-md-5">
            <iais:select name="svcName" id="svcName" options = "checklist_svc_name_select" needSort="true" firstOption="Please Select" value="${param.svcName}"></iais:select>
          </div>
        </div>

        <div class="form-group">
          <label class="col-md-2" style="padding-top: 15px">Service Sub-Type &nbsp;</label>
          <div class="col-md-5">
            <iais:select name="svcSubType" id="svcSubType" options = "checklist_config_subtype_select" firstOption="Please Select"
                         value="${param.svcSubType}"></iais:select>
          </div>
        </div>

        <div class="form-group">
          <label class="col-md-2" style="padding-top: 15px">Inspection Entity &nbsp;</label>
          <div class="col-md-5">
            <iais:select name="inspectionEntity" id="inspectionEntity" codeCategory="CATE_ID_INSPECTION_ENTITY_TYPE"
                         firstOption="Please Select" value="${param.inspectionEntity}"></iais:select>
          </div>
        </div>

        <iais:action cssClass="col-xs-12 text-right text-center-mobile">
          <a class="btn btn-secondary" id="crud_clear_button" onclick="clearFields('.form-horizontal')">Clear</a>
          <a class="btn btn-primary" id="crud_search_button" value="doSearch">Search</a>
        </iais:action>
      </div>

      <div class="components">
        <h3>
          <span>Search Results</span>
        </h3>
        <iais:pagination  param="checklistConfigSearch" result="checklistConfigResult"/>
        <div class="table-gp">
          <table aria-describedby="" class="table">
            <thead>
            <tr>
              <iais:sortableHeader needSort="false" style="width:1%; " field="" value="No."></iais:sortableHeader>
              <th scope="col"></th>
              <iais:sortableHeader  style="width:8%" needSort="true"   field="is_common" value="Common"></iais:sortableHeader>
              <iais:sortableHeader needSort="true"   field="type" value="Type"></iais:sortableHeader>
              <iais:sortableHeader  needSort="true"   field="module" value="Module"></iais:sortableHeader>
              <iais:sortableHeader  needSort="true"   field="service" value="Service"></iais:sortableHeader>
              <iais:sortableHeader  style="width:13%" needSort="true"   field="subtype_name" value="Service Sub-Type"></iais:sortableHeader>
              <iais:sortableHeader  style="width:8%" needSort="true"   field="INS_ENT" value="Inspection Entity"></iais:sortableHeader>
              <iais:sortableHeader  style="width:8%" needSort="true"   field="HCI_CODE" value="HCI Code"></iais:sortableHeader>
              <iais:sortableHeader  needSort="true"   field="START_DATE" value="Effective Start Date"></iais:sortableHeader>
              <iais:sortableHeader  needSort="true"   field="END_DATE" value="Effective End Date"></iais:sortableHeader>
              <iais:sortableHeader  needSort="false"  style="width:10%; "  field="action" value="Action"></iais:sortableHeader>
            </tr>
            </thead>
            <tbody>
            <c:choose>
              <c:when test="${empty checklistConfigResult.rows}">
                <tr>
                  <td colspan="6">
                    <iais:message key="GENERAL_ACK018" escape="true"></iais:message>
                  </td>
                </tr>
              </c:when>
              <c:otherwise>
                <tr>
                <c:forEach var = "config" items = "${checklistConfigResult.rows}" varStatus="status">
                  <tr>
                    <td class="row_no">${(status.index + 1) + (checklistConfigSearch.pageNo - 1) * checklistConfigSearch.pageSize}</td>
                    <td></td>
                    <c:if test="${config.common == false}">
                      <td>No</td>
                    </c:if>
                    <c:if test="${config.common  == true }">
                      <td>Yes</td>
                    </c:if>
                    <td>${config.type}</td>
                    <td>${config.module}</td>
                    <td>${config.svcName}</td>
                    <td>${config.svcSubType}</td>
                    <td><iais:code code="${config.inspectionEntity}"/></td>
                    <td>${config.hciCode}</td>
                    <td><fmt:formatDate value="${config.eftStartDate}" pattern="dd/MM/yyyy"/></td>
                    <td><fmt:formatDate value="${config.eftEndDate}" pattern = "dd/MM/yyyy"/></td>
                    <td>
                      <button type="button"  class="btn btn-default btn-sm" data-toggle="modal" onclick="javascript:loadEditData('<iais:mask name="currentMaskId" value="${config.id}"/>')" >Edit</button>
                      <button type="button" onclick="javascript:cloneConfig('<iais:mask name="currentMaskId" value="${config.id}"/>')" class="btn btn-default btn-sm" data-toggle="modal" data-target="#deleteModal" >Clone</button>
                      <button type="button" onclick="javascript:doView('<iais:mask name="currentMaskId" value="${config.id}"/>')" class="btn btn-default btn-sm" data-toggle="modal" data-target="#deleteModal" >View</button>
                      <button type="button" onclick="javascript:exportTemplate('<iais:mask name="currentMaskId" value="${config.id}"/>')" class="btn btn-default btn-sm">Export</button>
                      <button type="button"  class="btn btn-default btn-sm" data-toggle="modal" onclick="javascript:deleteRecord('<iais:mask name="currentMaskId" value="${config.id}"/>')" >Delete</button>
                    </td>
                  </tr>
                </c:forEach>
                </tr>
              </c:otherwise>
            </c:choose>
            </tbody>
          </table>
        </div>

        <iais:action cssClass="col-xs-12 text-right text-center-mobile">
            <a class="btn btn-primary next" href="javascript:void(0);" onclick="Utils.submit('mainForm', 'createUploadConfig')">Upload Configurations</a>
            <a class="btn btn-primary next" href="javascript:void(0);" onclick="Utils.submit('mainForm', 'updateTemplate')">Update Configurations</a>
            <a class="btn btn-primary next" href="javascript:void(0);" onclick="javascript: prepareAddConfig();">Add Configuration</a>
        </iais:action>

      </div>
    </div>

</form>

</div>




<%@include file="/WEB-INF/jsp/include/validation.jsp"%>
<%@include file="/WEB-INF/jsp/include/utils.jsp"%>
<script type="text/javascript">
  function exportTemplate(id) {
    showWaiting();
    let url = '/hcsa-licence-web/eservice/INTRANET/MohChecklistConfiguration/exportConfigTemplate?currentMaskId=' + id
    showPopupWindow(url);
  }

  function doSearch(){
    SOP.Crud.cfxSubmit("mainForm", "doSearch");
  }

  function deleteRecord(id){
    $('#currentMaskId').val(id);
    SOP.Crud.cfxSubmit("mainForm", "deleteRecord", id);
  }

  function doView(id){
    $('#currentMaskId').val(id);
    SOP.Crud.cfxSubmit("mainForm", "doView", id);
  }

  function loadEditData(id){
    $('#currentMaskId').val(id);
    SOP.Crud.cfxSubmit("mainForm", "loadEditData", id);
  }

  function cloneConfig(id){
    $('#currentMaskId').val(id);
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