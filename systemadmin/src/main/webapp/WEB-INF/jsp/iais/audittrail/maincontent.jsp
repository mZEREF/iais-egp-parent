<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %><%--
  Created by IntelliJ IDEA.
  User: yichen
  Date: 2/6/2020
  Time: 10:00 AM
  To change this template use File | Settings | File Templates.
--%>

<br><br>


<div class="form-horizontal">
  <div class="form-group">
    <div class="col-xs-5 col-md-10">
      <iais:field value="Operation Type" required="true"></iais:field>
      <div class="col-xs-5 col-md-5">
        <iais:select name="operationType" id="operationType" value="${param.operationType}" options="operationTypeSelect" firstOption="Please Select" ></iais:select>
        <span id="error_domain" name="iaisErrorMsg" class="error-msg"></span>
      </div>
    </div>
  </div>


  <div class="form-group">
    <div class="col-xs-5 col-md-10">
      <iais:field value="Operation" required="false"></iais:field>
      <div class="col-xs-5 col-md-5">
        <iais:select name="operation" id="operation" value="${param.operation}" options="operationValueTypeSelect" firstOption="Please Select"></iais:select>
        <span id="error_operation" name="iaisErrorMsg" class="error-msg"></span>
      </div>
    </div>
  </div>



  <div class="form-group">
    <div class="col-xs-5 col-md-10">
      <iais:field value="User" required="false"></iais:field>
      <div class="col-xs-5 col-md-5">
        <input type="text" name="user" value="${param.user}" maxlength="100"/>
        <span id="error_user" name="iaisErrorMsg" class="error-msg"></span>
      </div>
    </div>
  </div>


  <div class="form-group">
    <div class="col-xs-5 col-md-10">
      <iais:field value="Operation Start Date Time" required="true"></iais:field>
      <div class="col-xs-5 col-md-5">
        <iais:datePicker id = "startDate" name = "startDate"  value="${param.startDate}"></iais:datePicker>
        <span id="error_actionTime" name="iaisErrorMsg" class="error-msg"></span>
        <span id="error_compareDateError" name="iaisErrorMsg" class="error-msg"></span>
      </div>
    </div>
  </div>


  <div class="form-group">
    <div class="col-xs-5 col-md-10">
      <iais:field value="Operation End Date Time" required="true"></iais:field>
      <div class="col-xs-5 col-md-5">
        <iais:datePicker id = "endDate" name = "endDate"  value="${param.endDate}"></iais:datePicker>
      </div>
    </div>
  </div>

  <iais:action style="text-align:center;">
    <div class="row">
      <div class="col-xs-5 col-md-12">
        <div class="text-right">
          <a class="btn btn-secondary" id="crud_clear_button"  href="#">Clear</a>
          <a class="btn btn-primary" id="crud_search_export" href="${pageContext.request.contextPath}/audit-trail-file"  onclick="$('#crud_search_export').attr('class', 'btn btn-primary disabled')" value="doQuery" href="#">Export Audit Trail</a>
          <a class="btn btn-primary" id="crud_search_button" value="doQuery" href="#">Search</a>
        </div>
      </div>
    </div>
  </iais:action>
</div>
<br><br><br>
<input type="hidden" name="auditId" id="auditId"/>
<div class="tab-pane active" id="tabInbox" role="tabpanel">
  <div class="tab-content">
    <div class="row">
      <h3>
        <span>Search Results</span>
      </h3>
      <div class="col-xs-12">
        <div class="components">
          <iais:pagination  param="auditTrailSearch" result="auditTrailSearchResult"/>
            <table class="table">
              <thead>
              <tr>
                <iais:sortableHeader needSort="false"  field="" value="No."></iais:sortableHeader>
                <iais:sortableHeader needSort="true"   field="operation" value="Operation"></iais:sortableHeader>
                <iais:sortableHeader needSort="true"  field="domain" value="Operation Type"></iais:sortableHeader>
                <iais:sortableHeader needSort="true"   field="action_time" value="Operation Date"></iais:sortableHeader>
                <iais:sortableHeader needSort="true"   field="action_time" value="Operation Time"></iais:sortableHeader>
                <iais:sortableHeader needSort="true"   field="client_ip" value="Source Client IP"></iais:sortableHeader>
                <iais:sortableHeader needSort="true"   field="user_agent" value="User Agent"></iais:sortableHeader>
                <iais:sortableHeader needSort="true"   field="moh_user_id" value="MOH User&apos;s account ID"></iais:sortableHeader>
                <iais:sortableHeader needSort="true"   field="session_id" value="Session Id"></iais:sortableHeader>
                <iais:sortableHeader needSort="true"   field="application_number" value="Application ID"></iais:sortableHeader>
                <iais:sortableHeader needSort="true"   field="license_number" value="Licence Number"></iais:sortableHeader>
                <iais:sortableHeader needSort="true"   field="module" value="Module"></iais:sortableHeader>
                <iais:sortableHeader needSort="true"   field="userCase" value="Function / Use Case"></iais:sortableHeader>
                <iais:sortableHeader needSort="true"   field="programmeName" value="Program Name"></iais:sortableHeader>
                <iais:sortableHeader needSort="true"   field="operation" value="Data Activities"></iais:sortableHeader>
              </tr>
              </thead>
              <tbody>
              <c:choose>
                <c:when test="${empty auditTrailSearchResult.rows}">
                  <tr>
                    <td colspan="6">
                      <iais:message key="ACK018" escape="true"></iais:message>
                    </td>
                  </tr>
                </c:when>
                <c:otherwise>
                  <c:forEach var = "resultRow" items = "${auditTrailSearchResult.rows}" varStatus="status">
                    <tr>
                      <td class="row_no">${(status.index + 1) + (auditTrailSearch.pageNo - 1) * auditTrailSearch.pageSize}</td>
                        <%-- <td>${resultRow.operationType}</td>--%>
                      <td><c:out value="${resultRow.operationDesc}"></c:out></td>
                      <td><c:out value="${resultRow.domainDesc}"></c:out></td>
                      <td><fmt:formatDate value='${resultRow.actionTime}' pattern='dd/MM/yyyy' /></td>
                      <td><fmt:formatDate value='${resultRow.actionTime}' pattern='HH:mm:ss' /></td>
                      <td><c:out value="${resultRow.clientIp}"></c:out></td>
                      <td><c:out value="${resultRow.userAgent}"></c:out></td>
                      <td><c:out value="${resultRow.mohUserId}"></c:out></td>
                      <td><c:out value="${resultRow.sessionId}"></c:out></td>
                      <td><c:out value="${resultRow.appNum}"></c:out></td>
                      <td><c:out value="${resultRow.licenseNum}"></c:out></td>
                      <td><c:out value="${resultRow.module}"></c:out></td>
                      <td><c:out value="${resultRow.functionName}"></c:out></td>
                      <td><c:out value="${resultRow.programmeName}"></c:out></td>


                      <td>
                        <c:choose>
                          <c:when test="${resultRow.operation == 4}">
                            <a href="javascript:;"  style="text-decoration:underline;" onclick="viewData('<iais:mask name="auditId" value="${resultRow.auditId}"/>')">Data Read</a>
                          </c:when>
                          <c:when test="${resultRow.operation == 6}">
                           <a href="javascript:;"  style="text-decoration:underline;" onclick="viewData('<iais:mask name="auditId" value="${resultRow.auditId}"/>')" >Data Added</a>
                          </c:when>
                          <c:when test="${resultRow.operation == 7}">
                            <a href="javascript:;"  style="text-decoration:underline;" onclick="viewData('<iais:mask name="auditId" value="${resultRow.auditId}"/>')" >Data Updated</a>
                          </c:when>
                          <c:when test="${resultRow.operation == 8}">
                           <a href="javascript:;" style="text-decoration:underline;" onclick="viewData('<iais:mask name="auditId" value="${resultRow.auditId}"/>')">Data Deleted</a>
                          </c:when>
                          <c:otherwise>
                            <c:out value="-"></c:out>
                          </c:otherwise>
                        </c:choose>

                      </td>
                    </tr>
                  </c:forEach>

                </c:otherwise>
              </c:choose>
              </tbody>
            </table>
        </div>
      </div>
    </div>
  </div>
</div>

<%@include file="/WEB-INF/jsp/include/validation.jsp"%>
<%@include file="/WEB-INF/jsp/include/utils.jsp"%>
<script>
  function viewData(id) {
    $("#auditId").val(id)
    SOP.Crud.cfxSubmit("mainForm", "viewActivities");
  }

</script>