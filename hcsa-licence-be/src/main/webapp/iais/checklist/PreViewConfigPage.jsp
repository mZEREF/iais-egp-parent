<%--
  Created by IntelliJ IDEA.
  User: yichen
  Date: 11/8/2019
  Time: 10:27 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://www.ecq.com/iais"   prefix="iais"%>
<webui:setLayout name="iais-intranet"/>
<meta http-equiv="Content-Type" content="text/html charset=gb2312">

<%
  sop.webflow.rt.api.BaseProcessClass process =
          (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>

<form id = "mainForm" method = "post" action=<%=process.runtime.continueURL()%>>
  <%@ include file="/include/formHidden.jsp" %>
  <input type="hidden" name="crud_action_type" value="">
  <input type="hidden" name="crud_action_value" value="">
  <input type="hidden" name="crud_action_additional" value="">
  <br><br><br>

    <div class="main-content">
      <div class="container">
        <div class="row">
          <div class="col-xs-12">
            <div class="center-content">
                <c:if test="${configSessionAttr.common eq true}">
                  <div class="bg-title">
                    <h2>General Regulation</h2>
                  </div>
                </c:if>

                <c:if test="${configSessionAttr.common eq false}">
                  <c:choose>
                    <c:when test="${empty item.svcSubType}">
                      <h2>${configSessionAttr.svcCode}</h2>
                    </c:when>
                    <c:otherwise>
                      <h2>${configSessionAttr.svcCode} | ${configSessionAttr.svcSubType}</h2>
                    </c:otherwise>
                  </c:choose>
                </c:if>


              <c:forEach var = "sec" items = "${configSessionAttr.sectionDtos}" varStatus="status">
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

            </div>

            <div class="application-tab-footer">
              <td>
                <div class="text-right text-center-mobile">
                  <a class="btn btn-primary next" href="javascript:void(0);" onclick="javascript: submitConfig();">Submit</a>
                  <a class="btn btn-primary next" href="javascript:void(0);" onclick="javascript: doBack();">Back</a>

                </div>

              </td>
            </div>
          </div>
        </div>
      </div>
    </div>
</>

<script type="text/javascript">
    function submitConfig() {
        SOP.Crud.cfxSubmit("mainForm","submitConfig");
    }

    function doBack(){
        SOP.Crud.cfxSubmit("mainForm","doBack");
    }
</script>