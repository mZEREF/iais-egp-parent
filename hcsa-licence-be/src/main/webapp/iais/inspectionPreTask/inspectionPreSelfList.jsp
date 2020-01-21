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
  String webroot=IaisEGPConstant.BE_CSS_ROOT;
%>
<div class="dashboard" style="background-image:url('<%=webroot%>img/Masthead-banner.jpg')">
  <form method="post" id="mainSelfForm" action=<%=process.runtime.continueURL()%>>
    <%@ include file="/include/formHidden.jsp" %>
    <br>
    <br>
    <br>
    <br>
    <br>
    <input type="hidden" name="inspectorPreType" value="">
    <input type="hidden" name="actionValue" value="">
    <iais:body >
      <div class="container">
        <div class="col-xs-12">
          <div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">
            <h3>
              <span>Self-Assessment Checklists</span>
            </h3>
            <div class="panel panel-default">
              <div class="panel-collapse collapse in" id="collapseOne" role="tabpanel" aria-labelledby="headingOne" aria-expanded="true" style="">
                <div class="panel-body">
                  <div class="panel-main-content">
                    <iais:section title="" id = "self_assessment">
                      <c:if test="${commonDto.sectionDtoList != null}">
                        <h3>Common</h3>
                      </c:if>
                      <div class="table-gp">
                        <c:forEach var ="section" items ="${commonDto.sectionDtoList}">
                          <br/>
                          <h4><c:out value="${section.sectionName}"></c:out></h4>
                          <table class="table">
                            <thead>
                            <tr>
                              <th>No.</th>
                              <th>Regulation Clause Number</th>
                              <th>Item</th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:forEach var = "item" items = "${section.itemDtoList}" varStatus="status">
                              <tr>
                                <td class="row_no">${(status.index + 1) }</td>
                                <td>${item.incqDto.regClauseNo}</td>
                                <td>${item.incqDto.checklistItem}</td>
                              </tr>
                            </c:forEach>
                            </tbody>
                          </table>
                        </c:forEach>
                      </div>
                      <c:forEach var ="cdto" items ="${serListDto}" varStatus="status">
                        <h3>${cdto.svcName}</h3>
                        <div class="table-gp">
                          <c:forEach var ="section" items ="${cdto.sectionDtoList}">
                            <br/>
                            <h4><c:out value="${section.sectionName}"></c:out></h4>
                            <table class="table">
                              <thead>
                              <tr>
                                <th>No.</th>
                                <th>Regulation Clause Number</th>
                                <th>Item</th>
                              </tr>
                              </thead>
                              <tbody>
                              <c:forEach var = "item" items = "${section.itemDtoList}" varStatus="status">
                                <tr>
                                  <td class="row_no">${(status.index + 1) }</td>
                                  <td>${item.incqDto.regClauseNo}</td>
                                  <td>${item.incqDto.checklistItem}</td>
                                </tr>
                              </c:forEach>
                              </tbody>
                            </table>
                          </c:forEach>
                        </div>
                      </c:forEach>
                      <iais:action >
                        <button class="btn btn-lg btn-login-back" style="float:left" type="button" onclick="javascript:doInspectionPreSelfBack()">Back</button>
                      </iais:action>
                    </iais:section>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </iais:body>
  </form>
</div>
<script>
    function doInspectionPreSelfBack() {
        $("[name='actionValue']").val('back');
        inspectionPreSelfSubmit('back');
    }

  function inspectionPreSelfSubmit(action){
      $("[name='inspectorPreType']").val(action);
      var mainPoolForm = document.getElementById('mainSelfForm');
      mainPoolForm.submit();
  }
</script>


