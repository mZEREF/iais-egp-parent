<%--
  Created by IntelliJ IDEA.
  User: ShiCheng_Xu
  Date: 2020/1/21
  Time: 9:41
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>
<%
  //handle to the Engine APIs
  sop.webflow.rt.api.BaseProcessClass process =
          (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>
<webui:setLayout name="iais-intranet"/>

<%
  String webroot=IaisEGPConstant.CSS_ROOT + IaisEGPConstant.BE_CSS_ROOT;
%>
<div class="dashboard" style="background-image:url('<%=webroot%>img/Masthead-banner.jpg')">
  <form method="post" id="mainSelfForm" action=<%=process.runtime.continueURL()%>>
    <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
    <input type="hidden" name="inspectorPreType" value="">
    <input type="hidden" name="actionValue" value="">
    <div class="main-content">
      <div class="row">
        <div class="col-lg-12 col-xs-12">
          <div class="center-content">
            <div class="intranet-content">
              <div class="bg-title">
                <h2>
                  <span>Self-Assessment Checklists</span>
                </h2>
              </div>
              <iais:body >
                <iais:section title="" id = "self_assessment">
                  <c:if test="${!empty commonDto}">
                    <h3>Common</h3>
                  </c:if>
                  <div class="table-gp">
                    <table aria-describedby="" class="table">
                      <thead>
                      <tr>
                        <th scope="col" width="10%">No.</th>
                        <th scope="col" width="36%">Regulation Clause Number</th>
                        <th scope="col" width="36%">Item</th>
                        <th scope="col" width="6%">Yes</th>
                        <th scope="col" width="6%">No</th>
                        <th scope="col" width="6%">N/A</th>
                      </tr>
                      </thead>
                      <tbody>
                        <c:forEach items="${commonDto}" var="question" varStatus="status">
                          <tr>
                            <td class="row_no"><c:out value="${(status.index + 1)}"/></td>
                            <td><c:out value="${question.regulation}"/></td>
                            <td><c:out value="${question.checklistItem}"/></td>
                            <td><input type="radio" disabled <c:if test="${'YES' eq question.answer}">checked</c:if>/></td>
                            <td><input type="radio" disabled <c:if test="${'NO' eq question.answer}">checked</c:if>/></td>
                            <td><input type="radio" disabled <c:if test="${'NA' eq question.answer}">checked</c:if>/></td>
                          </tr>
                        </c:forEach>
                      </tbody>
                      </table>
                  </div>
                  <c:forEach var ="cdto" items ="${serListDto123}">
                    <h3>${cdto.svcName}</h3>
                    <div class="table-gp">
                      <table aria-describedby="" class="table">
                        <thead>
                        <tr>
                          <th scope="col" width="10%">No.</th>
                          <th scope="col" width="36%">Regulation Clause Number</th>
                          <th scope="col" width="36%">Item</th>
                          <th scope="col" width="6%">Yes</th>
                          <th scope="col" width="6%">No</th>
                          <th scope="col" width="6%">N/A</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach var ="question" items ="${cdto.question}" varStatus="status">
                          <tr>
                            <td class="row_no"><c:out value="${(status.index + 1)}"/></td>
                            <td><c:out value="${question.regulation}"/></td>
                            <td><c:out value="${question.checklistItem}"/></td>
                            <td><input type="radio" disabled <c:if test="${'YES' eq question.answer}">checked</c:if>/></td>
                            <td><input type="radio" disabled <c:if test="${'NO' eq question.answer}">checked</c:if>/></td>
                            <td><input type="radio" disabled <c:if test="${'NA' eq question.answer}">checked</c:if>/></td>
                          </tr>
                        </c:forEach>
                        </tbody>
                      </table>
                    </div>
                  </c:forEach>
                  <iais:action >
                    <a href="#" class="back" id="Back" onclick="javascript:doInspectionPreSelfBack()" style="float:left"><em class="fa fa-angle-left"></em> Back</a>
                  </iais:action>
                </iais:section>
              </iais:body>
            </div>
          </div>
        </div>
      </div>
    </div>
  </form>
</div>
<script>
    function doInspectionPreSelfBack() {
        showWaiting();
        $("[name='actionValue']").val('back');
        inspectionPreSelfSubmit('back');
    }

  function inspectionPreSelfSubmit(action){
      $("[name='inspectorPreType']").val(action);
      var mainPoolForm = document.getElementById('mainSelfForm');
      mainPoolForm.submit();
  }
</script>


