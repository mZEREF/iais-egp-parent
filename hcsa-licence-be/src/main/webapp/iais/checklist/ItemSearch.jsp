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
    <input type="hidden" name="crud_action_type" value="">
    <input type="hidden" name="crud_action_value" value="">
    <input type="hidden" name="crud_action_additional" value="">
    <input type="hidden" name="currentValidateId" value="">
     <div class="bg-title"><h2>Checklist Item Management</h2></div>

      <div class="tab-pane active" id="tabInbox" role="tabpanel">
        <span id="error_deleteItemMsg" name="iaisErrorMsg" class="error-msg"></span>
        <div class="form-horizontal">
          <div class="form-group">
            <label class="col-xs-4 col-md-2 control-label">Regulation Clause Number</label>
            <div class="col-xs-5 col-md-3">
              <input type="text" name="regulationClauseNo" value=""/>
              <span id="error_regulationClauseNo" name="iaisErrorMsg" class="error-msg"></span>
            </div>
          </div>

          <div class="form-group">
            <label class="col-xs-4 col-md-2 control-label">Regulation</label>
            <div class="col-xs-5 col-md-3">
              <input type="text" name="regulationClause" value=""/>
              <span id="error_regulationClause" name="iaisErrorMsg" class="error-msg"></span>
            </div>
          </div>

          <div class="form-group">
            <label class="col-xs-4 col-md-2 control-label">Checklist Item</label>
            <div class="col-xs-5 col-md-3">
              <input type="text" name="checklistItem" value=""/>
              <span id="error_checklistItem" name="iaisErrorMsg" class="error-msg"></span>
            </div>
          </div>

          <div class="form-group">
            <label class="col-xs-4 col-md-2 control-label">Risk Level</label>
            <div class="col-xs-12 col-md-8 col-lg-9">
              <iais:select name="riskLevel" id="riskLevel" codeCategory="CATE_ID_RISK_LEVEL"
                           firstOption="Select Risk Level"></iais:select>
            </div>
          </div>

          <div class="form-group">
            <label class="col-xs-4 col-md-2 control-label">Status</label>
            <div class="col-xs-12 col-md-8 col-lg-9">
              <iais:select name="status" id="status" codeCategory="CATE_ID_COMMON_STATUS"
                           firstOption="Select Status" filterValue="CMSTAT002"></iais:select>
            </div>
          </div>
        </div>
        <div class="tab-content">
          <div class="row">
            <div class="col-xs-12">
              <div class="components">
                <h2 class="component-title">Search &amp; Result</h2>
                <div class="table-gp">
                  <table class="table">
                    <thead>
                    <tr>
                      <iais:sortableHeader needSort="false" field="" value="No."></iais:sortableHeader>
                      <td></td>
                      <iais:sortableHeader needSort="true" field="regulationClauseNo"
                                           value="Regulation Clause Number"></iais:sortableHeader>
                      <iais:sortableHeader needSort="true" field="regulationClause"
                                           value="Regulations"></iais:sortableHeader>
                      <iais:sortableHeader needSort="true" field="checklistItem"
                                           value="Checklist Item"></iais:sortableHeader>
                      <iais:sortableHeader needSort="true" field="riskLevel" value="Risk Level"></iais:sortableHeader>
                      <iais:sortableHeader needSort="false" field="status" value="Status"></iais:sortableHeader>
                    </tr>
                    </thead>
                    <tbody>
                    <c:choose>
                      <c:when test="${empty checklistItemResult.rows}">
                        <tr>
                          <td colspan="6">
                            No Record!!
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
                            <td>${item.riskLevel}</td>
                            <td>${item.status}</td>
                            <c:if test="${empty sessionScope.currentValidateId}">
                              <td>
                                <button type="button" class="btn btn-default btn-sm"
                                        onclick="javascript:prepareEditItem('${item.itemId}');">Edit
                                </button>
                                <button type="button" class="btn btn-default btn-sm"
                                        onclick="javascript:disable('${item.itemId}');">Disable
                                </button>
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
                        <td class="row_no">${(status.index + 1) + (checklistItemSearch.pageNo - 1) * checklistItemSearch.pageSize}</td>
                      </div>
                      <div class="col-xs-50 col-md-10 text-right">
                        <div class="nav">
                          <ul class="pagination">
                            <li class="hidden"><a href="#" aria-label="Previous"><span aria-hidden="true"><i
                                    class="fa fa-chevron-left"></i></span></a></li>
                            <li class="active"><a href="#">1</a></li>
                            <li><a href="#">2</a></li>
                            <li><a href="#">3</a></li>
                            <li><a href="#" aria-label="Next"><span aria-hidden="true"><i
                                    class="fa fa-chevron-right"></i></span></a></li>
                          </ul>

                          <br><br><br>
                          <div class="text-right text-center-mobile">

                            <c:choose>
                              <c:when test="${!empty sessionScope.currentValidateId}">
                                <a class="btn btn-primary next" href="javascript:void(0);"
                                   onclick="javascript: configToChecklist();">Add to Config</a>
                                <a class="btn btn-primary next" href="javascript:void(0);"
                                   onclick="javascript: doSearch();">Search</a>
                                <a class="btn btn-primary next" href="javascript:void(0);"
                                   onclick="javascript: cancelConfig();">Cancel</a>
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
                                <a class="btn btn-primary next" href="javascript:void(0);"
                                   onclick="javascript: doSearch();">Search</a>
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
<script type="text/javascript">
    function cancelConfig() {
        SOP.Crud.cfxSubmit("mainForm", "cancelConfig");
    }

    function doSearch() {
        SOP.Crud.cfxSubmit("mainForm", "doSearch");
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

    function doCancel() {
        SOP.Crud.cfxSubmit("mainForm", "doCancel");
    }
</script>