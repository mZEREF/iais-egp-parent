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


<form id = "mainForm" method = "post" action=<%=process.runtime.continueURL()%>>
  <input type="hidden" name="crud_action_type" value="">
  <input type="hidden" name="crud_action_value" value="">
  <input type="hidden" name="crud_action_additional" value="">
  <%@ include file="/include/formHidden.jsp" %>

  <br><br>
  <span id="error_cloneRecords" name="iaisErrorMsg" class="error-msg"></span>

  <div class="main-content">
    <div class="container">
      <div class="tab-pane active" id="tabInbox" role="tabpanel">

        <div class="tab-content">
          <div class="row">

            <c:if test="${messageContent != null}">
              <c:forEach var = "msg" items="${messageContent}">
                <b>${msg.subject}: ${msg.result}<b><br>
              </c:forEach>
            </c:if>
            <br><br>
            <div class="col-xs-12">
              <div class="components">
                <h2 class="component-title">Clone &amp; Result</h2>
                <div class="table-gp">
                  <table class="table">
                    <thead>
                    <tr>
                      <iais:sortableHeader needSort="true"   field="regulationClauseNo" value="Regulation Clause Number"></iais:sortableHeader>
                      <iais:sortableHeader needSort="true"   field="regulationClause" value="Regulations"></iais:sortableHeader>
                      <iais:sortableHeader needSort="true"   field="checklistItem" value="Checklist Item"></iais:sortableHeader>
                      <iais:sortableHeader needSort="true"   field="riskLevel" value="Rusk Level"></iais:sortableHeader>
                      <iais:sortableHeader needSort="false"   field="status" value="Status"></iais:sortableHeader>
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
                            <td>${cloneItem.riskLevel}</td>
                            <td>${cloneItem.status}</td>
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
                        <td class="row_no">${(status.index + 1) + (checklistItemSearch.pageNo - 1) * checklistItemSearch.pageSize}</td>
                      </div>
                      <div class="col-xs-6 col-md-8 text-right">
                        <div class="nav">
                          <ul class="pagination">
                            <li class="hidden"><a href="#" aria-label="Previous"><span aria-hidden="true"><i class="fa fa-chevron-left"></i></span></a></li>
                            <li class="active"><a href="#">1</a></li>
                            <li><a href="#">2</a></li>
                            <li><a href="#">3</a></li>
                            <li><a href="#" aria-label="Next"><span aria-hidden="true"><i class="fa fa-chevron-right"></i></span></a></li>

                          </ul>


                        </div>
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


    </div>


  </div>




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