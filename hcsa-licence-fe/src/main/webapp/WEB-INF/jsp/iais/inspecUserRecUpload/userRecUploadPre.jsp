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
<%@include file="../common/dashboard.jsp"%>
<style type="text/css">
  .fa.fa-check-circle {
    cursor: default;
  }
  .fa.fa-times-circle {
    cursor: default;
  }
</style>
<div class="container">
  <div class="component-gp">
    <br>
    <form method="post" id="mainReviewForm" action=<%=process.runtime.continueURL()%>>
      <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
      <input type="hidden" name="inspecUserRecUploadType" value="">
      <input type="hidden" id="ncItemId" name="ncItemId" value="">
      <input type="hidden" id="actionValue" name="actionValue" value="">
      <div class="main-content">
        <div class="row">
          <div class="col-lg-12 col-xs-12">
            <div class="center-content">
              <div class="intranet-content">
                <iais:body >
                  <iais:section title="" id = "upload_Rectification">
                    <span class="error-msg" name="iaisErrorMsg" id="error_subFlag"></span>
                    <div class="table-responsive">
                      <table aria-describedby="" class="table">
                        <thead>
                        <tr>
                          <th scope="col" >No</th>
                          <th scope="col" >Vehicle Number</th>
                          <th scope="col" >NC Clause</th>
                          <th scope="col" >Checklist Question</th>
                          <th scope="col" >Findings/Non-Compliances</th>
                          <th scope="col" >Action Required</th>
                          <th scope="col" >Actions</th>
                          <th scope="col" >Rectification Uploaded?</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:choose>
                          <c:when test="${empty inspecUserRecUploadDtos}">
                            <tr>
                              <td colspan="7">
                                <iais:message key="GENERAL_ACK018" escape="true"></iais:message>
                              </td>
                            </tr>
                          </c:when>
                          <c:otherwise>
                            <c:forEach var="feRecNc" items="${inspecUserRecUploadDtos}" varStatus = "recNo">
                              <tr>
                                <td><c:out value="${recNo.count}"/></td>
                                <td><c:out value="${feRecNc.vehicleNo}"/></td>
                                <td><c:out value="${feRecNc.checkClause}"/></td>
                                <td><iais:code code="${feRecNc.checkQuestion}"/></td>
                                <c:if test="${empty feRecNc.appPremisesPreInspectionNcItemDto.ncs}">
                                  <td><c:out value="N/A"/></td>
                                </c:if>
                                <c:if test="${not empty feRecNc.appPremisesPreInspectionNcItemDto.ncs}">
                                  <td><c:out value="${feRecNc.appPremisesPreInspectionNcItemDto.ncs}"/></td>
                                </c:if>
                                <c:if test="${empty feRecNc.appPremisesPreInspectionNcItemDto.beRemarks}">
                                  <td><c:out value="N/A"/></td>
                                </c:if>
                                <c:if test="${not empty feRecNc.appPremisesPreInspectionNcItemDto.beRemarks}">
                                  <td><c:out value="${feRecNc.appPremisesPreInspectionNcItemDto.beRemarks}"/></td>
                                </c:if>
                                <c:if test="${'SUCCESS' eq feRecNc.buttonFlag}">
                                  <td>
                                    <button class="btn btn-secondary btn-md disabled" type="button" disabled>Rectify</button>
                                  </td>
                                </c:if>
                                <c:if test="${'SUCCESS' ne feRecNc.buttonFlag}">
                                  <td>
                                    <button class="btn btn-secondary btn-md" type="button" onclick="javascript:doUserRecUploadRectify('<iais:mask name="ncItemId" value="${feRecNc.id}"/>')">Rectify</button>
                                  </td>
                                </c:if>
                                <c:if test="${'SUCCESS' eq feRecNc.rectifyFlag}">
                                  <td>
                                    <h4 class="text-success"><em class="fa fa-check-circle"></em></h4>
                                  </td>
                                </c:if>
                                <c:if test="${'SUCCESS' ne feRecNc.rectifyFlag}">
                                  <td>
                                    <h4 class="text-danger"><em class="fa fa-times-circle"></em></h4>
                                  </td>
                                </c:if>
                              </tr>
                            </c:forEach>
                          </c:otherwise>
                        </c:choose>
                        </tbody>
                      </table>
                    </div>
                    <c:if test="${'submit' eq submitButtonFlag && not empty inspecUserRecUploadDtos}">
                      <iais:action >
                        <button class="btn btn-primary" style="float:right" type="button" onclick="javascript:doUserRecUploadSubmit()">Submit</button>
                      </iais:action>
                    </c:if>
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
<%@ include file="/WEB-INF/jsp/include/validation.jsp" %>
<script type="text/javascript">
    function userRecUploadSubmit(action){
        $("[name='inspecUserRecUploadType']").val(action);
        var mainPoolForm = document.getElementById('mainReviewForm');
        mainPoolForm.submit();
    }

    function doUserRecUploadRectify(ncItemId) {
        showWaiting();
        $("#ncItemId").val(ncItemId);
        $("#actionValue").val('confirm');
        userRecUploadSubmit('confirm');
    }

    function doUserRecUploadSubmit() {
        showWaiting();
        $("#actionValue").val('submit');
        userRecUploadSubmit('submit');
    }
</script>