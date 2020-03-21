<%--
  Created by IntelliJ IDEA.
  User: ShiCheng_Xu
  Date: 2019/12/23
  Time: 14:37
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais"%>

<%
  //handle to the Engine APIs
  sop.webflow.rt.api.BaseProcessClass process =
          (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>
<webui:setLayout name="iais-internet"/>
<%@include file="../inspecUserRecUpload/dashboard.jsp"%>

<div class="container">
  <div class="component-gp">
    <br>
    <form method="post" id="mainReviewForm" action=<%=process.runtime.continueURL()%>>
      <%@ include file="/include/formHidden.jsp" %>
      <input type="hidden" name="inspecUserRecUploadType" value="">
      <input type="hidden" id="itemId" name="itemId" value="">
      <input type="hidden" id="actionValue" name="actionValue" value="">
      <div class="main-content">
        <div class="row">
          <div class="col-lg-12 col-xs-12">
            <div class="center-content">
              <div class="intranet-content">
                <iais:body >
                  <iais:section title="" id = "upload_Rectification">
                      <div class="table-gp">
                        <table class="table">
                          <thead>
                          <tr align="center">
                            <th>No</th>
                            <th>NC Clause</th>
                            <th>Checklist Question</th>
                            <th>Actions</th>
                            <th>Rectified?</th>
                          </tr>
                          </thead>
                          <tbody>
                          <c:choose>
                            <c:when test="${empty inspecUserRecUploadDtos}">
                              <tr>
                                <td colspan="7">
                                  <iais:message key="ACK018" escape="true"></iais:message>
                                </td>
                              </tr>
                            </c:when>
                            <c:otherwise>
                              <c:forEach var="feRecNc" items="${inspecUserRecUploadDtos}" varStatus = "recNo">
                                <tr>
                                  <td><c:out value="${recNo.count}"/></td>
                                  <td><c:out value="${feRecNc.checkClause}"/></td>
                                  <td><iais:code code="${feRecNc.checkQuestion}"/></td>
                                  <td>
                                    <c:if test="${'SUCCESS' eq feRecNc.buttonFlag}">
                                      <button class="btn btn-default btn-sm disabled" type="button" disabled>Rectify</button>
                                    </c:if>
                                    <c:if test="${'SUCCESS' ne feRecNc.buttonFlag}">
                                      <button class="btn btn-default btn-sm" type="button" onclick="javascript:doUserRecUploadRectify('<iais:mask name="itemId" value="${feRecNc.itemId}"/>')">Rectify</button>
                                    </c:if>
                                  </td>
                                  <td><c:out value="${feRecNc.buttonFlag}"/></td>
                                </tr>
                              </c:forEach>
                            </c:otherwise>
                          </c:choose>
                          </tbody>
                        </table>
                      </div>
                    </iais:section>
                </iais:body>
              </div>
            </div>
          </div>
        </div>
      </div>
    </form>
  </div>
</div>
<%@ include file="/include/validation.jsp" %>
<script type="text/javascript">
  function userRecUploadSubmit(action){
      $("[name='inspecUserRecUploadType']").val(action);
      var mainPoolForm = document.getElementById('mainReviewForm');
      mainPoolForm.submit();
  }

  function doUserRecUploadRectify(itemId) {
      $("#itemId").val(itemId);
      $("#actionValue").val('confirm');
      userRecUploadSubmit('confirm');
  }
</script>