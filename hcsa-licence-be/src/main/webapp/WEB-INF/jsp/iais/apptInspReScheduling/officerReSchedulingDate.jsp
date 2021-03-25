<%--
  Created by IntelliJ IDEA.
  User: ShiCheng_Xu
  Date: 2021/3/25
  Time: 10:07
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
  <form method="post" id="reSchOfficerDateForm" action=<%=process.runtime.continueURL()%>>
    <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
    <input type="hidden" name="mohOfficerReSchedulingType" value="">
    <input type="hidden" id="actionValue" name="actionValue" value="">
    <div class="main-content">
      <div class="row">
        <div class="col-lg-12 col-xs-12">
          <div class="center-content">
            <div class="intranet-content">
              <iais:body >
                <iais:section title="" id = "reSchOfficerDate">
                  <c:if test="${'reSchOfficerDateShow' eq reSchOfficerDateShow}">
                    <div class="table-gp">
                      <table class="apptApp table">
                        <thead>
                        <tr align="center">
                          <th>Application No</th>
                          <th>Application Status</th>
                          <th>MOH Officer(s)</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:choose>
                          <c:when test="${empty apptInspectionDateDto.applicationInfoShow}">
                            <tr>
                              <td colspan="7">
                                <iais:message key="GENERAL_ACK018" escape="true"></iais:message>
                              </td>
                            </tr>
                          </c:when>
                          <c:otherwise>
                            <c:forEach var="appInfoShow" items="${apptInspectionDateDto.applicationInfoShow}">
                              <tr>
                                <td><c:out value="${appInfoShow.applicationNo}"/></td>
                                <td><iais:code code="${appInfoShow.status}"/></td>
                                <td>
                                  <c:if test="${appInfoShow.userDisName != null}">
                                    <c:forEach var="worker" items="${appInfoShow.userDisName}" varStatus="status">
                                      <c:out value="${worker}"/><br>
                                    </c:forEach>
                                  </c:if>
                                </td>
                              </tr>
                            </c:forEach>
                          </c:otherwise>
                        </c:choose>
                        </tbody>
                      </table>
                    </div>
                    <iais:action >
                      <a class="back" id="Back" onclick="javascript:officerReSchedulingDateBack()" style="float:left"><em class="fa fa-angle-left"></em> Back</a>
                      <button class="btn btn-primary" style="float:right" type="button" onclick="javascript:apptInspectionDateSpecific()">Find new Date</button>
                      <span style="float:right">&nbsp;</span>
                      <button class="btn btn-primary" style="float:right" type="button" onclick="javascript:apptInspectionDateConfirm()">Confirm System-proposed Date</button>
                    </iais:action>
                  </c:if>
                  <c:if test="${'reSchOfficerDateShow' ne reSchOfficerDateShow}">
                    <iais:action >
                      <a class="back" id="Back" onclick="javascript:officerReSchedulingDateBack()" style="float:left"><em class="fa fa-angle-left"></em> Back</a>
                      <button class="btn btn-primary" style="float:right" type="button" onclick="javascript:apptInspectionDateYes()">Yes</button>
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
<script type="text/javascript">
    function officerReSchedulingDateBack() {
        showWaiting();
        $("[name='actionValue']").val('back');
        officerReSchedulingDateSubmit('back');
    }

    function apptInspectionDateYes() {
        showWaiting();
        $("[name='actionValue']").val('yes');
        officerReSchedulingDateSubmit('success');
    }

    function officerReSchedulingDateSuccess() {
        showWaiting();
        $("[name='actionValue']").val('success');
        officerReSchedulingDateSubmit('success');
    }

    function officerReSchedulingDateAgain() {
        showWaiting();
        $("[name='actionValue']").val('again');
        officerReSchedulingDateSubmit('again');
    }

    function officerReSchedulingDateSubmit(action){
        $("[name='mohOfficerReSchedulingType']").val(action);
        var mainPoolForm = document.getElementById('reSchOfficerDateForm');
        mainPoolForm.submit();
    }
</script>

