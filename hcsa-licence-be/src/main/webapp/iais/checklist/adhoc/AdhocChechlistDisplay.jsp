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
      <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
      <input type="hidden" name="crud_action_type" value="">
      <input type="hidden" name="crud_action_value" value="">
      <input type="hidden" name="crud_action_additional" value="">
      <div class="main-content">
        <div class="container">

          <br><br>

          <div class="col-lg-12 col-xs-12">
            <div class="center-content">
              <div class="intranet-content">

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

                    <!-- Content goes here -->
                    <c:forEach var = "sec" items="${item.sectionDtos}">
                      <p>
                        <div class="panel panel-default">
                          <div class="panel-collapse collapse in" id="collapsePremise" role="tabpanel" aria-labelledby="headingPremise">
                            <div class="panel-body">
                              <div class="panel-main-content">
                                <div class="preview-info">
                                  <p>Section: &nbsp;<b>${sec.section}</b></p>
                                  <p>Description: &nbsp;${sec.description}</p>

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
                      </div>
                      </p>
                    </c:forEach>
                </c:forEach>


              <!-- Adhoc item goes here -->

                <p>
                  <div class="panel panel-default">
                    <div class="panel-collapse collapse in" role="tabpanel" aria-labelledby="headingPremise">
                      <div class="panel-body">
                        <div class="panel-main-content">
                          <div class="preview-info">
                              <p>Section: &nbsp;<b>Adhoc Section</b></p>

                              <table class="table">
                                <thead>
                                <tr>
                                  <th>Checklist Item</th>
                                  <th>Answer Type</th>
                                  <th>Risk Level</th>
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
                                  </tr>
                                </c:forEach>
                                </tbody>
                              </table>
                              </div>
                              </div>
                              </div>
                              </div>
                              </div>
                              </p>
                              <!----------------------->
              </div>
              <div class="application-tab-footer">
                <div class="row">
                  <div class="col-xs-12 col-sm-12">
                    <div class="text-right text-center-mobile">
                      <a id = "addAdhocItemBtn" class="btn btn-primary" href="#">Add Adhoc Item</a>
                      <a class="btn btn-primary" href="#">Next</a>
                    </div>
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

<script>
  "use strict"
  addAdhocItemBtn.onclick = function(){
    SOP.Crud.cfxSubmit("mainForm", "receiveItemPool");
  }

</script>