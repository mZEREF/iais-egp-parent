<%--
  Created by IntelliJ IDEA.
  User: yichen
  Date: 10/6/2019
  Time: 3:10 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<webui:setLayout name="iais-intranet"/>

<%@ page contentType="text/html; charset=UTF-8" %>
<%
  sop.webflow.rt.api.BaseProcessClass process =
          (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>

<style>
  @media only screen and (max-width: 992px) {
    div.action1 span {
      display: block;
    }
  }
  @media only screen and (min-width: 993px) {
    table.table > thead > tr > th:nth-of-type(1),
    table.table > thead > tr > th:nth-of-type(1) {
      width: 1%;
    }
    table.table > thead > tr > th:nth-of-type(2),
    table.table > thead > tr > th:nth-of-type(2) {
      width: 2%;
    }
    table.table > thead > tr > th:nth-of-type(3),
    table.table > thead > tr > th:nth-of-type(3) {
      width: 15%;
    }
    table.table > thead > tr > th:nth-of-type(4),
    table.table > thead > tr > th:nth-of-type(4) {
      width: 30%;
    }
    table.table > thead > tr > th:nth-of-type(5),
    table.table > thead > tr > th:nth-of-type(5) {
      width: 20%;
    }
    table.table > thead > tr > th:nth-of-type(6),
    table.table > thead > tr > th:nth-of-type(6) {
      width: 10%;
    }
    table.table > thead > tr > th:nth-of-type(7),
    table.table > thead > tr > th:nth-of-type(7) {
      width: 10%;
    }
    table.table > thead > tr > th:nth-of-type(8),
    table.table > thead > tr > th:nth-of-type(8) {
      width: 5%;
    }
  }
</style>

<div class="main-content">
  <div class="center-content col-xs-12 col-md-12">
    <form id="mainForm" method="post" action=<%=process.runtime.continueURL()%>>
      <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
      <input type="hidden" name="currentValidateId" value="">
      <input type="hidden" id="currentMaskId" name="currentMaskId" value="">

      <div class="bg-title"><h2>Checklist Item Management</h2></div>

      <span id="error_deleteItemMsg" name="iaisErrorMsg" class="error-msg"></span>
      <span id="error_cloneItemMsg" name="iaisErrorMsg" class="error-msg"></span>
      <span id="error_configItemMsg" name="iaisErrorMsg" class="error-msg"></span>

      <br><br>
      <div class="tab-pane active" id="tabInbox" role="tabpanel">
        <div class="form-horizontal">
          <div class="form-group">
            <iais:field value="Regulation Clause Number" ></iais:field>
            <div class="col-xs-5 col-md-3">
              <input type="text" name="regulationClauseNo" maxlength="100" value="${regulationClauseNo}"/>
              <span id="error_regulationClauseNo" name="iaisErrorMsg" class="error-msg"></span>
            </div>
          </div>

          <div class="form-group">
            <iais:field value="Regulation" ></iais:field>
            <div class="col-xs-5 col-md-3">
              <input type="text" name="regulationClause" maxlength="8000" value="${regulationClause}"/>
              <span id="error_regulationClause" name="iaisErrorMsg" class="error-msg"></span>
            </div>
          </div>

          <div class="form-group">
            <iais:field value="Checklist Item" ></iais:field>
            <div class="col-xs-5 col-md-3">
              <input type="text" name="checklistItem" maxlength="500" value="${checklistItem}"/>
              <span id="error_checklistItem" name="iaisErrorMsg" class="error-msg"></span>
            </div>
          </div>

          <div class="form-group">
            <iais:field value="Risk Level" ></iais:field>
            <div class="col-xs-5 col-md-3">
              <iais:select name="riskLevel" id="riskLevel" codeCategory="CATE_ID_RISK_LEVEL"
                           firstOption="Please Select" value="${riskLevel}"></iais:select>
            </div>
          </div>

          <div class="form-group">
            <iais:field value="Status" ></iais:field>
            <div class="col-xs-5 col-md-3">
              <iais:select name="status" id="status" codeCategory="CATE_ID_COMMON_STATUS"
                           firstOption="Please Select" filterValue="CMSTAT002,CMSTAT004,DRAFT001" value="${status}"/>
            </div>
          </div>

          <iais:action cssClass="col-xs-12 text-right action1 text-center-mobile">
            <a class="btn btn-secondary" id="exportButtonId" href="${pageContext.request.contextPath}/checklist-item-file?action=checklistItem">Export Checklist Item</a>
            <span>
            <a class="btn btn-secondary" id="exportTemplateButtonId" href="/hcsa-licence-web/eservice/INTRANET/MohChecklistItem/exportItemToConfigTemplate">Export Checklist Configurations</a>
            </span>
            <a class="btn btn-primary next" id="crud_search_button" value="doSearch" href="#">Search</a>
            <a class="btn btn-secondary" id="crud_clear_button" onclick="$('#status').val('')" href="#">Clear</a>
          </iais:action>
        </div>

        <div class="tab-content">
          <div class="row">
            <div class="col-xs-12">
              <div class="components">
                <h3>
                  <span>Search Results</span>
                </h3>
                <iais:pagination  param="checklistItemSearch" result="checklistItemResult"/>
                <div class="table-gp">
                <table aria-describedby="" class="table">
                    <thead>
                    <tr>
                      <iais:sortableHeader needSort="false" field=""  value="No."></iais:sortableHeader>
                      <th scope="col"></th>
                      <iais:sortableHeader needSort="true" field="CLAUSE_NO"
                                           value="Regulation Clause Number"></iais:sortableHeader>
                      <iais:sortableHeader needSort="true" field="CLAUSE"
                                           value="Regulations"></iais:sortableHeader>
                      <iais:sortableHeader needSort="true" field="CHECKLISTITEM"
                                           value="Checklist Item"></iais:sortableHeader>
                      <iais:sortableHeader needSort="true" field="RISK_LEVEL" value="Risk Level"></iais:sortableHeader>
                      <iais:sortableHeader needSort="true" field="status" value="Status"></iais:sortableHeader>
                      <iais:sortableHeader needSort="false" field="action" value="Action"></iais:sortableHeader>
                    </tr>
                    </thead>
                    <tbody>
                    <c:choose>
                      <c:when test="${empty checklistItemResult.rows}">
                        <tr>
                          <td colspan="6">
                            <iais:message key="GENERAL_ACK018" escape="true"></iais:message>
                          </td>
                        </tr>
                      </c:when>
                      <c:otherwise>
                        <c:forEach var="item" items="${checklistItemResult.rows}" varStatus="status">
                          <tr>
                            <td class="row_no">${(status.index + 1) + (checklistItemSearch.pageNo - 1) * checklistItemSearch.pageSize}</td>
                            <td><iais:checkbox name="itemCheckbox" checkboxId="itemCheckbox" request="${pageContext.request}"  value="${item.itemId}" forName="checklist_item_CheckboxReDisplay"></iais:checkbox></td>
                            <%--<td><input name="itemCheckbox" id="itemCheckbox" type="checkbox" value=""/>--%>
                            <td class="word-wrap">${item.regulationClauseNo}</td>
                            <td class="word-wrap">${item.regulationClause}</td>
                            <td class="word-wrap">${item.checklistItem}</td>
                            <td><iais:code code="${item.riskLevel}"></iais:code></td>
                            <td><iais:code code="${item.status}"></iais:code></td>
                            <c:if test="${empty currentValidateId}">
                              <td>
                                <c:if test="${item.status == 'CMSTAT001'}">
                                <button type="button" class="btn btn-default btn-sm"
                                        onclick="javascript:prepareEditItem('<iais:mask name="currentMaskId" value="${item.itemId}"/>');">Edit
                                </button>
                                  <button type="button"  class="btn btn-default btn-sm" data-toggle="modal" data-target="#DeleteTemplateModal${status.index + 1}" >Delete</button>
                                  <div class="modal fade" id="DeleteTemplateModal${status.index + 1}" tabindex="-1" role="dialog" aria-labelledby="DeleteTemplateModal">
                                    <div class="modal-dialog modal-dialog-centered" role="document">
                                      <div class="modal-content">
<%--                                        <div class="modal-header">--%>
<%--                                          <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>--%>
<%--                                          <div class="modal-title" id="gridSystemModalLabel" style="font-size:2rem;">Confirmation Box</div>--%>
<%--                                        </div>--%>
                                        <div class="modal-body">
                                          <div class="row">
                                            <div class="col-md-12"><span style="font-size: 2rem">Do you confirm the delete ?</span></div>
                                          </div>
                                        </div>
                                        <div class="modal-footer">
                                          <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
                                          <button type="button" class="btn btn-primary" onclick="javascript:disable('<iais:mask name="currentMaskId" value="${item.itemId}"/>');" >Confirm</button>
                                        </div>
                                      </div>
                                    </div>
                                  </div>
                                </c:if>
                                <c:if test="${item.status == 'CMSTAT003'}">
                                  <button type="button" class="btn btn-default btn-sm"
                                          onclick="javascript:prepareEditItem('<iais:mask name="currentMaskId" value="${item.itemId}"/>');">Edit
                                  </button>
                                </c:if>
                              </td>
                            </c:if>
                          </tr>
                        </c:forEach>
                      </c:otherwise>

                    </c:choose>
                    </tbody>
                  </table>
                  </div>
                  <%--<div class="table-footnote">
                    <div class="row">
                      <div class="col-xs-6 col-md-4">
                      </div>
                      <div class="col-xs-50 col-md-10 text-right">
                        <div class="nav">

                          <br><br><br>
                          <div class="text-right text-center-mobile">--%>
                  <iais:action cssClass="table-footnote text-right text-center-mobile">
                    <c:choose>
                      <c:when test="${!empty sessionScope.currentValidateId}">
                        <a class="btn btn-secondary" href="javascript:void(0);"
                           onclick="javascript: cancelConfig();">Cancel</a>
                        <a class="btn btn-primary next" href="javascript:void(0);"
                           onclick="javascript: configToChecklist();">Add to Config</a>
                      </c:when>
                      <c:otherwise>
                        <%--<a class="btn btn-primary next" href="javascript:void(0);"
                           onclick="javascript: clearCheckBox();">Clear CheckBox</a>--%>
                        <a class="btn btn-primary next" href="javascript:void(0);"
                           onclick="javascript: prepareAddItem();">Add Checklist Item</a>
                        <a class="btn btn-primary next" href="javascript:void(0);"
                           onclick="javascript: prepareClone();">Clone Checklist Item</a>
                        <a class="btn btn-primary next" href="javascript:void(0);"
                           onclick="javascript: doUploadFile('createData');">Upload Checklist Item</a>
                        <a class="btn btn-primary next" href="javascript:void(0);"
                           onclick="javascript: doUploadFile('updateData');">Update Checklist Item</a>
                      </c:otherwise>
                    </c:choose>
                  </iais:action>
              </div>
            </div>
          </div>
        </div>
      </div>
    </form>
  </div>
</div>

<%@include file="/WEB-INF/jsp/include/validation.jsp" %>
<%@include file="/WEB-INF/jsp/include/utils.jsp"%>
<script type="text/javascript">
  function exportToConfigTemplate(){
      showWaiting();
      let url = '/hcsa-licence-web/eservice/INTRANET/MohChecklistItem/exportItemToConfigTemplate'
      showPopupWindow(url);
    }

  function cancelConfig() {
      SOP.Crud.cfxSubmit("mainForm", "cancelConfig");
  }

  function doUploadFile(value) {
      showWaiting();
      SOP.Crud.cfxSubmit("mainForm", "preUploadData", value);
  }

  function clearCheckBox(){
    SOP.Crud.cfxSubmit("mainForm", "clearCheckBox");
  }

  function disable(itemId) {
    $('#currentMaskId').val(itemId)
      SOP.Crud.cfxSubmit("mainForm", "deleteChecklistItem", itemId);
  }

  function configToChecklist() {
      showWaiting();
      var checkBoxLen = $('input[type=checkbox]:checked').length;

      if (checkBoxLen > 0){
          SOP.Crud.cfxSubmit("mainForm", "configToChecklist")
      }
  }

  function prepareAddItem() {
      showWaiting();
      SOP.Crud.cfxSubmit("mainForm", "prepareAddItem");
  }

  function prepareClone() {
      showWaiting();
      SOP.Crud.cfxSubmit("mainForm", "viewCloneData");
  }

  function prepareEditItem(id) {
    $('#currentMaskId').val(id)
      SOP.Crud.cfxSubmit("mainForm", "prepareEditItem", id);
  }

  function jumpToPagechangePage(){
      SOP.Crud.cfxSubmit("mainForm", "doPage");
  }

  function sortRecords(sortFieldName,sortType){
      SOP.Crud.cfxSubmit("mainForm","sortRecords",sortFieldName,sortType);
  }




</script>