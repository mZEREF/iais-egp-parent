<%--
  Created by IntelliJ IDEA.
  User: yichen
  Date: 12/10/2019
  Time: 8:17 PM
  To change this template use File | Settings | File Templates.
--%>

<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://www.ecq.com/iais"   prefix="iais"%>
<webui:setLayout name="iais-intranet"/>
<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>

<%
  sop.webflow.rt.api.BaseProcessClass process =
          (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>

<c:choose>
  <c:when test="${empty inspectionChecklistAttr}">
    <tr>
      <td colspan="6">
        No Record!!
      </td>
    </tr>
  </c:when>
  <c:otherwise>

<form id = "mainForm" method = "post" action=<%=process.runtime.continueURL()%>>
  <%@ include file="/include/formHidden.jsp" %>
  <input type="hidden" name="crud_action_type" value="">
  <input type="hidden" name="crud_action_value" value="">
  <input type="hidden" name="crud_action_additional" value="">

  <br><br><br>
  <div class="main-content">
    <div class="container">
      <br><br>
      <span id="error_checklistItem" name="iaisErrorMsg" class="error-msg"></span>
      <div class="row">
        <div class="col-xs-12">

          <div class="center-content">
            <c:forEach var = "item" items="${inspectionChecklistAttr}" varStatus="status">
              <c:if test="${item.common eq true}">
                <div class="bg-title">
                  <h2>General Regulation</h2>
                </div>
              </c:if>

              <c:if test="${item.common eq false}">
                <c:choose>
                  <c:when test="${empty item.svcSubType}">
                    <h2>${item.svcCode}</h2>
                  </c:when>
                  <c:otherwise>
                    <h2>${item.svcCode} | ${item.svcSubType}</h2>
                  </c:otherwise>
                </c:choose>
              </c:if>

            <c:forEach var = "sec" items="${item.sectionDtos}">
            <div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">
              <div class="panel panel-default">
                <div class="panel-heading" id="headingPremise" role="tab">
                  <h4 class="panel-title"><a role="button" data-toggle="collapse" href="#collapsePremise" aria-expanded="true" aria-controls="collapsePremise">${sec.section}</a></h4>
                </div>
                <div class="panel-collapse collapse in" id="collapsePremise" role="tabpanel" aria-labelledby="headingPremise">
                  <div class="panel-body">
                    <table class="table">
                      <thead>
                      <tr>
                        <th>Regulation Clause Number</th>
                        <th>Regulations</th>
                        <th>Checklist Item</th>
                        <th>Risk Level</th>
                      </tr>
                      </thead>
                      <tbody>
                      <c:forEach var = "chklitem" items = "${sec.checklistItemDtos}" varStatus="status">
                        <tr>
                          <td>
                            <p>${chklitem.regulationClauseNo}</p>
                          </td>
                          <td>
                            <p>${chklitem.regulationClause}</p>
                          </td>
                          <td>
                            <p>${chklitem.checklistItem}</p>
                          </td>
                          <td>
                            <p>${chklitem.riskLevel}</p>
                          </td>
                        </tr>
                      </c:forEach>
                      </tbody>
                    </table>
                  </div>
                </div>
              </div>
            </div>

            </c:forEach>
            </c:forEach>

            <c:if test="${!empty adhocCheckListAttr}">
              <div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">
                <div class="panel panel-default">
                  <div class="panel-heading"  role="tab">
                    <h4 class="panel-title"><a role="button" data-toggle="collapse" href="#collapsePremise" aria-expanded="true" aria-controls="collapsePremise">Adhoc Item</a></h4>
                  </div>
                  <div class="panel-collapse collapse in" role="tabpanel" aria-labelledby="headingPremise">
                    <div class="panel-body">
                      <table class="table">
                        <thead>
                        <tr>
                          <th>Checklist Item</th>
                          <th>Answer Type</th>
                          <th>Risk Level</th>
                          <th>Actionl</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach var = "adhocItem" items = "${adhocCheckListAttr.allAdhocItem}" varStatus="status">
                          <tr>
                            <td>
                              <p>${adhocItem.question}</p>
                            </td>
                            <td>
                              <p>${adhocItem.answerType}</p>
                            </td>
                            <td>
                              <p>${adhocItem.riskLvl}</p>
                            </td>

                            <td>
                              <button id="removeAdhocItemId" value="${adhocItem.question}" type="button">Remove</button>
                            </td>
                          </tr>
                        </c:forEach>
                        </tbody>
                      </table>
                    </div>
                  </div>
                </div>
              </div>
            </c:if>

          </div>
        </div>
        <div class="application-tab-footer">
          <div class="row">
            <div class="col-xs-12 col-sm-12">
              <div class="text-right text-center-mobile">
                <a id = "addAdhocItemBtn" class="btn btn-primary" href="#">Add Adhoc Item</a>
                <a id = "nextAdhocItemBtn" class="btn btn-primary" href="#">Draft</a>
              </div>
            </div>
          </div>
        </div>
      </div>


    </div>
  </div>


</form>
  </c:otherwise>
</c:choose>
<%@include file="/include/validation.jsp"%>
<script>
  "use strict"
  addAdhocItemBtn.onclick = function(){
    SOP.Crud.cfxSubmit("mainForm", "receiveItemPool");
  }

  nextAdhocItemBtn.onclick = function(){
      SOP.Crud.cfxSubmit("mainForm", "saveAdhocItem");
  }

  removeAdhocItemId.onclick = function () {
      var content = $("#removeAdhocItemId").val();
      SOP.Crud.cfxSubmit("mainForm", "removeAdhocItem", content);
  }

</script>