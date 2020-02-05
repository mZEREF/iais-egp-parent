<%--
  Created by IntelliJ IDEA.
  User: yichen
  Date: 9/17/2019
  Time: 3:26 PM
  To change this template use File | Settings | File Templates.
--%>
--%>
--%>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>

<webui:setLayout name="iais-intranet"/>

<div class="main-content">
    <form id="mainForm" method="post" action=<%=process.runtime.continueURL()%>>
        <%@ include file="/include/formHidden.jsp" %>
        <input type="hidden" name="crud_action_type" value="">
        <input type="hidden" name="crud_action_value" value="">
        <input type="hidden" name="crud_action_additional" value="">
        <div class="bg-title"><h2>Audit Trail Mask Module</h2></div>

        <iais:section title="" id="atmList">
            <iais:row>
                <iais:field value="Operation Type" required="false"></iais:field>
                <iais:value width="7">
                    <iais:select name="operationType" id="operationType" options="operationTypeSelect" firstOption="Please select" onchange="displaySection()"></iais:select>
                </iais:value>

                <iais:field value="Operation" required="false"></iais:field>
                <iais:value width="7">
                    <iais:select name="operation" id="operation" options="operationValueTypeSelect" firstOption="Please select"></iais:select>
                </iais:value>

                <iais:field value="User" required="false"></iais:field>
                <iais:value width="7">
                    <input type="text" name="user" value="" />
                </iais:value>



                <iais:field value="Operation Start Date Time" required="false"></iais:field>
                <iais:value width="7">
                    <iais:datePicker id = "startDate" name = "startDate"  value="${startDate}"></iais:datePicker>
                </iais:value>

                <iais:field value="Operation End Date Time" required="false"></iais:field>
                <iais:value width="7">
                    <iais:datePicker id = "endDate" name = "endDate"  value="${endDate}"></iais:datePicker>
                </iais:value>

            </iais:row>

            <iais:action style="text-align:center;">
                <button class="btn btn-lg btn-login-search" type="button" style="background:#2199E8; color: white" >Search</button>
                <button class="btn btn-lg btn-login-clear" type="button" style="background:#2199E8; color: white" onclick="javascript:Utils.clearClickStatus()">Clear</button>
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


    </form>
</div>
<script src="/system-admin-web/iais/js/CommonUtils.js"></script>

