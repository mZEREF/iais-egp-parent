<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %><%--
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
  .col-md-10 {
    width: 100%;
  }

</style>
<div class="main-content">
  <form id="mainForm" method="post" action=<%=process.runtime.continueURL()%>>
    <%@ include file="/include/formHidden.jsp" %>
    <input type="hidden" name="currentValidateId" value="">
     <div class="bg-title"><h2>Checklist Item Management</h2></div>

    <span id="error_deleteItemMsg" name="iaisErrorMsg" class="error-msg"></span>
    <span id="error_cloneItemMsg" name="iaisErrorMsg" class="error-msg"></span>

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
              <input type="text" name="regulationClause" maxlength="2000" value="${regulationClause}"/>
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
                           firstOption="Please Select" filterValue="CMSTAT002,CMSTAT004" value="${status}"></iais:select>
            </div>
          </div>

          <div class="application-tab-fo  oter">
            <div class="row">
              <div class="col-xs-12 col-md-10">
                <div class="text-right">
                  <a class="btn btn-secondary" id="crud_clear_button"  href="#">Clear</a>
                  <a class="btn btn-secondary"  href="${pageContext.request.contextPath}/checklist-item-file?action=checklistItem">Export Checklist Item</a>
                  <a class="btn btn-secondary"   href="${pageContext.request.contextPath}/checklist-item-file?action=regulation">Export Regulation</a>
                  <a class="btn btn-primary next" id="crud_search_button" value="doSearch" href="#">Search</a>
                </div>
              </div>
            </div>
          </div>

        </div>
        <div class="tab-content">
          <div class="row">
            <div class="col-xs-12">
              <div class="components">
                <iais:pagination  param="checklistItemSearch" result="checklistItemResult"/>
                <div class="table-gp">
                  <table class="table">
                    <thead>
                    <tr>
                      <iais:sortableHeader needSort="false" field="" value="No."></iais:sortableHeader>
                      <td></td>
                      <iais:sortableHeader needSort="true" field="CLAUSE_NO"
                                           value="Regulation Clause Number"></iais:sortableHeader>
                      <iais:sortableHeader needSort="true" field="CLAUSE"
                                           value="Regulations"></iais:sortableHeader>
                      <iais:sortableHeader needSort="true" field="CHECKLISTITEM"
                                           value="Checklist Item"></iais:sortableHeader>
                      <iais:sortableHeader needSort="true" field="RISK_LEVEL" value="Risk Level"></iais:sortableHeader>
                      <iais:sortableHeader needSort="false" field="status" value="Status"></iais:sortableHeader>
                    </tr>
                    </thead>
                    <tbody>
                    <c:choose>
                      <c:when test="${empty checklistItemResult.rows}">
                        <tr>
                          <td colspan="6">
                            <iais:message key="ACK018" escape="true"></iais:message>
                          </td>
                        </tr>
                      </c:when>
                      <c:otherwise>
                        <c:forEach var="item" items="${checklistItemResult.rows}" varStatus="status">
                          <tr>
                            <td class="row_no">${(status.index + 1) + (checklistItemSearch.pageNo - 1) * checklistItemSearch.pageSize}</td>
                            <td><input name="itemCheckbox" id="itemCheckbox" type="checkbox" value="${item.itemId}"/>
                            </td>
                            <td>${item.regulationClauseNo}</td>
                            <td>${item.regulationClause}</td>
                            <td>${item.checklistItem}</td>
                            <td><iais:code code="${item.riskLevel}"></iais:code></td>
                            <td><iais:code code="${item.status}"></iais:code></td>
                            <c:if test="${empty sessionScope.currentValidateId}">
                              <td>
                                <c:if test="${item.status == 'CMSTAT001'}">
                                <button type="button" class="btn btn-default btn-sm"
                                        onclick="javascript:prepareEditItem('${item.itemId}');">Edit
                                </button>
                                  <button type="button"  class="btn btn-default btn-sm" data-toggle="modal" data-target="#DeleteTemplateModal" >Delete</button>

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
                                          <button type="button" class="btn btn-primary" onclick="javascript:disable('${item.itemId}');" >Confirm</button>
                                        </div>
                                      </div>
                                    </div>
                                  </div>

                                </c:if>
                                <c:if test="${item.status == 'CMSTAT003'}">
                                  <button type="button" class="btn btn-default btn-sm"
                                          onclick="javascript:prepareEditItem('${item.itemId}');">Edit
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
                  <div class="table-footnote">
                    <div class="row">
                      <div class="col-xs-6 col-md-4">
                      </div>
                      <div class="col-xs-50 col-md-10 text-right">
                        <div class="nav">

                          <br><br><br>
                          <div class="text-right text-center-mobile">

                            <c:choose>
                              <c:when test="${!empty sessionScope.currentValidateId}">
                                <a class="btn btn-secondary" href="javascript:void(0);"
                                   onclick="javascript: cancelConfig();">Cancel</a>
                                <a class="btn btn-primary next" href="javascript:void(0);"
                                   onclick="javascript: configToChecklist();">Add to Config</a>
                              </c:when>
                              <c:otherwise>
                                <a class="btn btn-primary next" href="javascript:void(0);"
                                   onclick="javascript: prepareAddItem();">Add ChecklistItem</a>
                                <a class="btn btn-primary next" href="javascript:void(0);"
                                   onclick="javascript: prepareClone();">Clone ChecklistItem</a>
                                <a class="btn btn-primary next" href="javascript:void(0);"
                                   onclick="javascript: doUploadFile('regulation');">Upload Regulation</a>
                                <a class="btn btn-primary next" href="javascript:void(0);"
                                   onclick="javascript: doUploadFile('checklistItem');">Upload Checklist Item</a>



                              </c:otherwise>
                            </c:choose>


                          </div>
                        </div>


                      </div>
                    </div>


                  </div>


                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
  </form>
</div>

<%@include file="/include/validation.jsp" %>
<%@include file="/include/utils.jsp"%>
<script type="text/javascript">

    function cancelConfig() {
        SOP.Crud.cfxSubmit("mainForm", "cancelConfig");
    }

    function doUploadFile(value) {
        SOP.Crud.cfxSubmit("mainForm", "preUploadData", value);
    }

    function disable(itemId) {
        SOP.Crud.cfxSubmit("mainForm", "deleteChecklistItem", itemId);
    }

    function configToChecklist() {
        var checkBoxLen = $('input[type=checkbox]:checked').length;

        if (checkBoxLen > 0){
            SOP.Crud.cfxSubmit("mainForm", "configToChecklist")
        }
    }

    function prepareAddItem() {
        SOP.Crud.cfxSubmit("mainForm", "prepareAddItem");
    }

    function prepareClone() {
        SOP.Crud.cfxSubmit("mainForm", "viewCloneData");
    }

    function prepareEditItem(id) {
        SOP.Crud.cfxSubmit("mainForm", "prepareEditItem", id);
    }

    function jumpToPagechangePage(){
        SOP.Crud.cfxSubmit("mainForm", "doPage");
    }

    function sortRecords(sortFieldName,sortType){
        SOP.Crud.cfxSubmit("mainForm","sortRecords",sortFieldName,sortType);
    }



</script>