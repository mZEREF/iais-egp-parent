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
<form id = "mainForm" method = "post" action=<%=process.runtime.continueURL()%>>
  <%@ include file="/include/formHidden.jsp" %>
  <input type="hidden" name="crud_action_type" value="">
  <input type="hidden" name="crud_action_value" value="">
  <input type="hidden" name="crud_action_additional" value="">
  <div class="main-content">
    <div class="">
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
            <iais:checkbox checkboxId="moduleCheckBox" codeCategory = "CATE_ID_CHECKLIST_MODULE" name= "moduleCheckBox" labelName = "Module" request="${pageContext.request}"></iais:checkbox>
          </td>


          <td>
            <iais:checkbox checkboxId="typeCheckBox" codeCategory = "CATE_ID_CHECKLIST_TYPE" forName="typeCheckBoxFor" name= "typeCheckBox" labelName = "Type" request="${pageContext.request}"></iais:checkbox>
          </td>
       </div>


        <br><br>
        <div class="form-group">
          <label class="col-md-2">Service Name &nbsp;</label>
          <div class="col-md-5">
            <iais:select name="svcName" id="svcName" options = "svcNameSelect" firstOption="Please Select" value="${svcName}"></iais:select>
          </div>
        </div>

        <div class="form-group">
          <label class="col-md-2">Service Sub Type &nbsp;</label>
          <div class="col-md-5">
            <iais:select name="svcSubType" id="svcSubType"   options = "subtypeSelect" firstOption="Please Select" value="${svcSubType}"></iais:select>
          </div>
        </div>

        <iais:action style="text-align:center;">
          <div class="text-right">
            <a class="btn btn-secondary" id="crud_clear_button" href="#">Clear</a>
            <a class="btn btn-primary" id="crud_search_button" value="doSearch" href="#">Search</a>
          </div>
        </iais:action>

      </div>




      <div class="components">
        <iais:pagination  param="checklistConfigSearch" result="checklistConfigResult"/>
        <div class="table-gp">
          <table class="table">
            <thead>
            <tr>
              <iais:sortableHeader needSort="false"  field="" value="No."></iais:sortableHeader>
              <td></td>
              <iais:sortableHeader needSort="true"   field="is_common" value="Common"></iais:sortableHeader>
              <iais:sortableHeader needSort="true"   field="type" value="Type"></iais:sortableHeader>
              <iais:sortableHeader needSort="true"   field="module" value="Module"></iais:sortableHeader>
              <iais:sortableHeader needSort="true"   field="service" value="Service"></iais:sortableHeader>
              <iais:sortableHeader needSort="true"   field="subtype_name" value="Service Sub-type"></iais:sortableHeader>
              <iais:sortableHeader needSort="true"   field="HCI_CODE" value="HCI Code"></iais:sortableHeader>
              <iais:sortableHeader needSort="true"   field="START_DATE" value="Effective Start Date"></iais:sortableHeader>
              <iais:sortableHeader needSort="true"   field="END_DATE" value="Effective End Date"></iais:sortableHeader>
              <iais:sortableHeader needSort="false"   field="action" value="Action"></iais:sortableHeader>

            </tr>
            </thead>
            <tbody>
            <c:choose>
              <c:when test="${empty checklistConfigResult.rows}">
                <tr>
                  <td colspan="6">
                    No Record!!
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
                    <td>${config.hciCode}</td>
                    <td><fmt:formatDate value="${config.eftStartDate}" pattern="MM/dd/yyyy"/></td>
                    <td><fmt:formatDate value="${config.eftEndDate}" pattern = "MM/dd/yyyy"/></td>

                    <div class="modal fade" id="DeleteTemplateModal" tabindex="-1" role="dialog" aria-labelledby="DeleteTemplateModal" style="left: 50%;top: 50%;transform: translate(-50%,-50%);min-width:80%; overflow: visible;bottom: inherit;right: inherit;">
                      <div class="modal-dialog" role="document">
                        <div class="modal-content">
                          <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                            <h5 class="modal-title" id="gridSystemModalLabel">Confirmation Box</h5>
                          </div>
                          <div class="modal-body">
                            <div class="row">
                              <div class="col-md-8 col-md-offset-2"><span style="font-size: 2rem">Do you confirm the delete ?</span></div>
                            </div>
                          </div>
                          <div class="modal-footer">
                            <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
                            <button type="button" class="btn btn-primary" onclick="javascript:deleteRecord('${config.id}')" >Confirm</button>
                          </div>
                        </div>
                      </div>
                    </div>


                    <td>
                      <button type="button"  class="btn btn-default btn-sm" data-toggle="modal" onclick="javascript:loadEditData('${config.id}')" >Edit</button>
                      <button type="button" onclick="javascript:cloneConfig('${config.id}')" class="btn btn-default btn-sm" data-toggle="modal" data-target="#deleteModal" >Clone</button>
                      <button type="button" onclick="javascript:doView('${config.id}')" class="btn btn-default btn-sm" data-toggle="modal" data-target="#deleteModal" >View</button>
                      <button type="button"  class="btn btn-default btn-sm" data-toggle="modal" data-target="#DeleteTemplateModal" >Delete</button>
                    </td>
                  </tr>
                </c:forEach>
                </tr>

              </c:otherwise>
            </c:choose>
            </tbody>
          </table>

        </div>


              <td>
                <div class="text-right text-center-mobile">
                  <a class="btn btn-primary next" href="javascript:void(0);" onclick="javascript: prepareAddConfig();">Add Configuration</a>
                </div>
              </td>

      </div>
    </div>
  </div>



</form>





<%@include file="/include/validation.jsp"%>
<%@include file="/include/utils.jsp"%>
<script type="text/javascript">

  function doSearch(){
    SOP.Crud.cfxSubmit("mainForm", "doSearch");
  }

  function deleteRecord(id){
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