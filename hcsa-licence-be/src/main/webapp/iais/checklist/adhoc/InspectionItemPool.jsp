<%--
  Created by IntelliJ IDEA.
  User: yichen
  Date: 12/11/2019
  Time: 1:51 PM
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
  <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
  <input type="hidden" name="crud_action_type" value="">
  <input type="hidden" name="crud_action_value" value="">
  <input type="hidden" name="crud_action_additional" value="">
  <input type="hidden" name="currentValidateId" value="">




  <div class="main-content">
    <div class="container">
      <div class="tab-pane active" id="tabInbox" role="tabpanel">
        <div class="form-horizontal">

        <div class="tab-content">
          <div class="row">
            <div class="col-xs-12">
              <div class="components">
                <h2 class="component-title">Item &amp; Pool</h2>
                <div class="table-gp">
                  <table class="table">
                    <thead>
                    <tr>
                      <iais:sortableHeader needSort="false"  field="" value="No."></iais:sortableHeader>
                      <td></td>
                      <iais:sortableHeader needSort="true"   field="regulationClauseNo" value="Regulation Clause Number"></iais:sortableHeader>
                      <iais:sortableHeader needSort="true"   field="regulationClause" value="Regulations"></iais:sortableHeader>
                      <iais:sortableHeader needSort="true"   field="checklistItem" value="Checklist Item"></iais:sortableHeader>
                      <iais:sortableHeader needSort="true"   field="riskLevel" value="Risk Level"></iais:sortableHeader>
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
                        <c:forEach var = "item" items = "${checklistItemResult.rows}" varStatus="status">
                          <tr>
                            <td class="row_no">${(status.index + 1) + (checklistItemSearch.pageNo - 1) * checklistItemSearch.pageSize}</td>
                            <td><input name="itemCheckbox" id="itemCheckbox" type="checkbox" value="${item.itemId}" /></td>
                            <td>${item.regulationClauseNo}</td>
                            <td>${item.regulationClause}</td>
                            <td>${item.checklistItem}</td>
                            <td>${item.riskLevel}</td>
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

                          <br><br><br>
                          <div class="text-right text-center-mobile">
                                <a  id="customAdhocItembtnId"  class="btn btn-primary custom">Custom Adhoc Item</a>
                                <a  id="adhocSectionbtnId" class="btn btn-primary addToSection">Add to Adhoc Section</a>
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


  </div>


</form>

<script>
    "use strict";
    customAdhocItembtnId.onclick = function(){
        SOP.Crud.cfxSubmit("mainForm", "customItem");
    }

    adhocSectionbtnId.onclick = function(){
        SOP.Crud.cfxSubmit("mainForm", "appendToTail");
    }

</script>
