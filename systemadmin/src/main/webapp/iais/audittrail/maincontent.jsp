<%--
  Created by IntelliJ IDEA.
  User: yichen
  Date: 2/6/2020
  Time: 10:00 AM
  To change this template use File | Settings | File Templates.
--%>



<iais:section title="" id="atmList">
  <iais:row>
    <iais:field value="Operation Type" required="false"></iais:field>
    <iais:value width="7">
      <iais:select name="operationType" id="operationType" value="${operationType}" options="operationTypeSelect" firstOption="Please select" onchange="displaySection()"></iais:select>
      <span id="error_domain" name="iaisErrorMsg" class="error-msg"></span>
    </iais:value>

    <iais:field value="Operation" required="false"></iais:field>
    <iais:value width="7">
      <iais:select name="operation" id="operation" value="${operation}" options="operationValueTypeSelect" firstOption="Please select"></iais:select>
      <span id="error_operation" name="iaisErrorMsg" class="error-msg"></span>
    </iais:value>

    <iais:field value="User" required="false"></iais:field>
    <iais:value width="7">
      <input type="text" name="user" value="${user}" />
      <span id="error_user" name="iaisErrorMsg" class="error-msg"></span>
    </iais:value>



    <iais:field value="Operation Start Date Time" required="false"></iais:field>
    <iais:value width="7">
      <iais:datePicker id = "startDate" name = "startDate"  value="${startDate}"></iais:datePicker>
      <span id="error_startDate" name="iaisErrorMsg" class="error-msg"></span>
    </iais:value>

    <iais:field value="Operation End Date Time" required="false"></iais:field>
    <iais:value width="7">
      <iais:datePicker id = "endDate" name = "endDate"  value="${endDate}"></iais:datePicker>
      <span id="error_endDate" name="iaisErrorMsg" class="error-msg"></span>
    </iais:value>

  </iais:row>

  <iais:action style="text-align:center;">
    <button class="btn btn-lg btn-login-search" type="button" style="background:#2199E8; color: white" >Search</button>
    <a class="btn btn-lg btn-login-export" type="button" href="${pageContext.request.contextPath}/audit-trail-file" style="background:#2199E8; color: white" >Export</a>
    <button class="btn btn-lg btn-login-clear" type="button"  style="background:#2199E8; color: white" onclick="javascript:doClear() ">Clear</button>
  </iais:action>

</iais:section>

<br><br><br>
<div>
  <div class="tab-pane active" id="tabInbox" role="tabpanel">
    <div class="tab-content">
      <div class="row">
        <br><br>
        <div class="col-xs-12">
          <div class="components">
            <iais:pagination  param="auditTrailSearch" result="auditTrailSearchResult"/>
            <div class="table-gp">
              <table class="table">
                <thead>
                <tr>
                  <iais:sortableHeader needSort="false"  field="" value="No."></iais:sortableHeader>
                  <iais:sortableHeader needSort="true"   field="operation" value="Operation"></iais:sortableHeader>
                  <iais:sortableHeader needSort="true"  field="domain" value="Operation Type"></iais:sortableHeader>
                  <iais:sortableHeader needSort="true"   field="batchJobID" value="Batch Job ID"></iais:sortableHeader>
                  <iais:sortableHeader needSort="true"   field="directoryID" value="Active directory ID"></iais:sortableHeader>
                  <iais:sortableHeader needSort="true"   field="corpPassId" value="Corp-Pass ID"></iais:sortableHeader>
                  <iais:sortableHeader needSort="true"   field="corpPassNric" value="Corp-Pass NRIC"></iais:sortableHeader>
                  <iais:sortableHeader needSort="true"   field="uenId" value="UEN"></iais:sortableHeader>
                  <iais:sortableHeader needSort="true"   field="singPassId" value="SingPass ID"></iais:sortableHeader>
                  <iais:sortableHeader needSort="true"   field="operationDate" value="Operation Date"></iais:sortableHeader>
                  <iais:sortableHeader needSort="true"   field="operationTime" value="Operation Time"></iais:sortableHeader>
                  <iais:sortableHeader needSort="true"   field="client_ip" value="Source Client IP"></iais:sortableHeader>
                  <iais:sortableHeader needSort="true"   field="user_agent" value="User Agent"></iais:sortableHeader>
                  <iais:sortableHeader needSort="true"   field="moh_user_id" value="MOH User’s account ID"></iais:sortableHeader>
                  <iais:sortableHeader needSort="true"   field="session_id" value="Session Id"></iais:sortableHeader>
                  <iais:sortableHeader needSort="true"   field="application_number" value="Application ID"></iais:sortableHeader>
                  <iais:sortableHeader needSort="true"   field="license_number" value="Licence Number"></iais:sortableHeader>
                  <iais:sortableHeader needSort="true"   field="module" value="Module"></iais:sortableHeader>
                  <iais:sortableHeader needSort="true"   field="userCase" value="Function / Use Case"></iais:sortableHeader>
                  <iais:sortableHeader needSort="true"   field="programmeName" value="Program Name"></iais:sortableHeader>
                  <iais:sortableHeader needSort="true"   field="dataActivities" value="Data Activities"></iais:sortableHeader>
                  <iais:sortableHeader needSort="true"   field="createdby" value="Created by"></iais:sortableHeader>
                  <iais:sortableHeader needSort="true"   field="createddate" value="Created date"></iais:sortableHeader>
                </tr>
                </thead>
                <tbody>
                <c:choose>
                  <c:when test="${empty auditTrailSearchResult.rows}">
                    <tr>
                      <td colspan="6">
                        No Record!!
                      </td>
                    </tr>
                  </c:when>
                  <c:otherwise>
                    <c:forEach var = "resultRow" items = "${auditTrailSearchResult.rows}" varStatus="status">
                      <tr>
                        <td class="row_no">${(status.index + 1) + (auditTrailSearch.pageNo - 1) * auditTrailSearch.pageSize}</td>
                          <%-- <td>${resultRow.operationType}</td>--%>
                        <td><c:out value="${resultRow.operation}"></c:out></td>
                        <td><c:out value="${resultRow.domain}"></c:out></td>
                        <td><%--<c:out value="${resultRow.batchjobId}"></c:out>--%></td>
                        <td><%--<c:out value="${resultRow.corpPassId}"></c:out>--%></td>
                        <td></td>
                        <td><c:out value="${resultRow.nricNumber}"></c:out></td>
                        <td><c:out value="${resultRow.uenId}"></c:out></td>
                        <td><c:out value="${resultRow.nricNumber}"></c:out></td>

                        <td><fmt:formatDate value='${resultRow.actionTime}' pattern='MM/dd/yyyy' /></td>
                        <td><fmt:formatDate value='${resultRow.actionTime}' pattern='hh:mm:ss' /></td>
                        <td><c:out value="${resultRow.clientIp}"></c:out></td>
                        <td><c:out value="${resultRow.userAgent}"></c:out></td>
                        <td><c:out value="${resultRow.mohUserId}"></c:out></td>
                        <td><c:out value="${resultRow.sessionId}"></c:out></td>
                        <td><c:out value="${resultRow.appNum}"></c:out></td>
                        <td><c:out value="${resultRow.licenseNum}"></c:out></td>
                        <td><c:out value="${resultRow.module}"></c:out></td>
                        <td><c:out value="${resultRow.functionName}"></c:out></td>
                        <td><c:out value="${resultRow.programmeName}"></c:out></td>
                        <td><c:out value="${resultRow.programmeName}"></c:out></td>
                      </tr>
                    </c:forEach>

                  </c:otherwise>
                </c:choose>
                </tbody>
              </table>
              <div class="table-footnote">
                <div class="row">
                  <div class="col-xs-6 col-md-8 text-right">
                    <br><br>


                  </div>
                </div>
              </div>


            </div>
          </div>
        </div>
      </div>
    </div>
  </div>


</div>
<%@include file="/include/validation.jsp"%>
<script src="/system-admin-web/iais/js/CommonUtils.js"></script>

<script>
    function doClear() {
        $('input[name="user"]').val("");
        $('input[name="startDate"]').val("");
        $('input[name="endDate"]').val("");

        $("#operationType option[text = 'Please select']").val("selected", "selected");
        $("#operation option[text = 'Please select']").val("selected", "selected");
        $(".current").text("Please select");
        $("#operationType").val("");
        $("#operation").val("");
    }

</script>