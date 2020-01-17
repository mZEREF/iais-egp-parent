<%--
  Created by IntelliJ IDEA.
  User: yichen
  Date: 10/6/2019
  Time: 3:10 PM
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
<div class="main-content">
  <form id = "mainForm" method = "post" action=<%=process.runtime.continueURL()%>>
    <input type="hidden" name="crud_action_type" value="">
    <input type="hidden" name="crud_action_value" value="">
    <input type="hidden" name="crud_action_additional" value="">
    <%@ include file="/include/formHidden.jsp" %>

    <br><br>
    &nbsp;<span id="error_cloneRecords" name="iaisErrorMsg" class="error-msg"></span>
    <div class="tab-pane active" id="tabInbox" role="tabpanel">

      <div class="tab-content">
        <div class="row">
          <div class="col-xs-12">
            <c:if test="${messageContent != null}">
              <c:forEach var = "msg" items="${messageContent}">
                <strong>${msg.subject}: ${msg.result}</strong><br>
              </c:forEach>
            </c:if>
            <br><br>
            <div class="components">
              <h2 class="component-title">Clone &amp; Result</h2>
              <div class="table-gp">
                <table class="table">
                  <thead>
                  <tr>
                    <iais:sortableHeader needSort="false"   field="regulationClauseNo" value="Regulation Clause Number"></iais:sortableHeader>
                    <iais:sortableHeader needSort="false"   field="regulationClause" value="Regulations"></iais:sortableHeader>
                    <iais:sortableHeader needSort="false"   field="checklistItem" value="Checklist Item"></iais:sortableHeader>
                    <iais:sortableHeader needSort="false"   field="riskLevel" value="Rusk Level"></iais:sortableHeader>
                    <iais:sortableHeader needSort="false"   field="action" value="Action"></iais:sortableHeader>
                  </tr>
                  </thead>
                  <tbody>
                  <c:choose>
                    <c:when test="${empty cloneItems}">
                      <tr>
                        <td colspan="6">
                          No Record!!
                        </td>
                      </tr>
                    </c:when>
                    <c:otherwise>
                      <c:forEach var = "cloneItem" items = "${cloneItems}" varStatus="status">
                        <tr>
                          <td>${cloneItem.regulationClauseNo}</td>
                          <td>${cloneItem.regulationClause}</td>
                          <td>${cloneItem.checklistItem}</td>
                          <td><iais:code code="${cloneItem.riskLevel}"></iais:code></td>
                          <td>
                            <iais:link icon="form_edit" title="Edit" onclick="javascript:prepareCloneItem('${cloneItem.itemId}');"/>
                          </td>
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
                    <div class="col-xs-6 col-md-8 text-right">

                      <br><br>
                      <div class="text-right text-center-mobile">
                        <a class="btn btn-primary next" href="javascript:void(0);" onclick="javascript: cancelClone();">Cancel</a>
                        <a class="btn btn-primary next" href="javascript:void(0);" onclick="javascript: submitCloneItem();">Submit</a>
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


</form>
<%@include file="/include/validation.jsp"%>
<script>
    function prepareCloneItem(itemId){
        SOP.Crud.cfxSubmit("mainForm", "prepareCloneItem", itemId);
    }

    function submitCloneItem(){
        SOP.Crud.cfxSubmit("mainForm", "submitCloneItem");
    }

    function cancelClone(){
        SOP.Crud.cfxSubmit("mainForm","cancelClone");
    }
</script>