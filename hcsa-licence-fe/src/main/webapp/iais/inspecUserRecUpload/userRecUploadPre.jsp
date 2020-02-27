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

<div class="container">
  <div class="component-gp">
    <br>
    <form method="post" id="mainReviewForm" action=<%=process.runtime.continueURL()%>>
      <%@ include file="/include/formHidden.jsp" %>
      <br>
      <br>
      <br>
      <br>
      <br>
      <input type="hidden" name="inspecUserRecUploadType" value="">
      <input type="hidden" id="itemId" name="itemId" value="">
      <input type="hidden" id="actionValue" name="actionValue" value="">
      <iais:body >
        <div class="container">
          <div class="col-xs-12">
            <div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">
              <h3>
                <span>Rectification NC</span>
              </h3>
              <div class="panel panel-default">
                <div class="panel-collapse collapse in" id="collapseOne" role="tabpanel" aria-labelledby="headingOne" aria-expanded="true" style="">
                  <div class="panel-body">
                    <div class="panel-main-content">
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
                                      <button class="btn btn-lg btn-login-Next" style="float:right" type="button" disabled onclick="javascript:doUserRecUploadRectify('<iais:mask name="itemId" value="${feRecNc.itemId}"/>')">Rectify</button>
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
      inspectorSearchTaskSubmit('confirm');
  }
</script>