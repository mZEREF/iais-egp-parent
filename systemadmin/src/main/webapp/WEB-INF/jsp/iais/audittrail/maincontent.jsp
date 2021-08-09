<%--
  Created by IntelliJ IDEA.
  User: yichen
  Date: 2/6/2020
  Time: 10:00 AM
  To change this template use File | Settings | File Templates.
--%>



<div class="row form-horizontal" style="margin-right: 80px">
    <div class="form-group">
        <div class="col-xs-5 col-md-10">
            <div class="col-xs-10 col-md-12">
                <div class="components">
                    <a id="filterBtn" class="btn btn-secondary" data-toggle="collapse" name="filterBtn"
                       data-target="#searchCondition" aria-expanded="true">Filter</a>
                </div>
            </div>
        </div>
    </div>
    <div id="searchCondition" class="collapse" aria-expanded="true" style="">
        <div class="form-group">
            <div class="col-xs-5 col-md-10">
                <iais:field value="Operation Type" required="true"></iais:field>
                <div class="col-xs-5 col-md-5">
                    <iais:select name="operationType" id="operationType" value="${param.operationType}"
                                 codeCategory="CATE_ID_AUDIT_TRAIL_OPERATION_TYPE" firstOption="Please Select" needSort="true"></iais:select>
                    <span id="error_domain" name="iaisErrorMsg" class="error-msg"></span>
                </div>
            </div>
        </div>

        <div class="form-group">
            <div class="col-xs-5 col-md-10">
                <iais:field value="Operation" required="true"></iais:field>
                <div class="col-xs-5 col-md-5">
                    <iais:select name="operation" id="operation" value="${param.operation}"
                                 options="operationValueTypeSelect" firstOption="Please Select" needSort="true"></iais:select>
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
                <iais:field value="Data Activities" required="false"></iais:field>
                <div class="col-xs-5 col-md-5">
                    <iais:select name="dataActivites" id="dataActivites" value="${param.dataActivites}"
                                 options="dataActivitesTypeSelect" firstOption="Please Select" needSort="true"></iais:select>
                    <span id="error_dataActivites" name="iaisErrorMsg" class="error-msg"></span>
                </div>
            </div>
        </div>

        <div class="form-group">
            <div class="col-xs-5 col-md-10">
                <iais:field value="Operation Start Date" required="true"></iais:field>
                <div class="col-xs-5 col-md-5">
                    <iais:datePicker id="startDate" name="startDate" value="${param.startDate}"></iais:datePicker>
                    <span id="error_dateStart" name="iaisErrorMsg" class="error-msg"></span>
                    <span id="error_compareDateError" name="iaisErrorMsg" class="error-msg"></span>
                </div>
            </div>
        </div>


        <div class="form-group">
            <div class="col-xs-5 col-md-10">
                <iais:field value="Operation End Date" required="true"></iais:field>
                <div class="col-xs-5 col-md-5">
                    <iais:datePicker id="endDate" name="endDate" value="${param.endDate}"></iais:datePicker>
                    <span id="error_dateEnd" name="iaisErrorMsg" class="error-msg"></span>
                </div>
            </div>
        </div>


        <div class="text-right" style="width:108%">
            <a class="btn btn-secondary" id="crud_clear_button" href="#">Clear</a>
            <a class="btn btn-primary" id="crud_search_export"
               href="${pageContext.request.contextPath}/audit-trail-file"
               onclick="$('#crud_search_export').attr('class', 'btn btn-primary disabled')"
               value="doQuery" href="#">Export Audit Trail</a>
            <a class="btn btn-primary" id="crud_search_button" value="doQuery" href="#">Search</a>
        </div>
    </div>
</div>

<div class="tab-pane active" id="tabInbox" role="tabpanel">
    <div class="tab-content">
        <div class="row">
            <iais:pagination param="auditTrailSearch" result="auditTrailSearchResult"/>
            <div class="col-xs-12">
                <h3>
                    <span>Search Results</span>
                </h3>
                <div class="table-responsive">
                    <div class="table-gp">
                        <table aria-describedby="" class="table">
                            <thead>
                            <tr><th scope="col" style="display: none"></th>
                                <iais:sortableHeader needSort="false" field="" value="No."></iais:sortableHeader>
                                <iais:sortableHeader needSort="true" field="operation_des c"
                                                     value="Operation"></iais:sortableHeader>
                                <iais:sortableHeader needSort="true" field="domain"
                                                     value="Operation Type"></iais:sortableHeader>
                                <iais:sortableHeader needSort="true" field="entity_id"
                                                     value="Batch Job ID"></iais:sortableHeader>
                                <iais:sortableHeader needSort="true" field="moh_user_id"
                                                     value="Active directory ID"></iais:sortableHeader>

                                <iais:sortableHeader needSort="true" field="entity_id"
                                                     value="CorpPass ID"></iais:sortableHeader>

                                <iais:sortableHeader needSort="true" field="nric_number"
                                                     value="CorpPass NRIC"></iais:sortableHeader>

                                <iais:sortableHeader needSort="true" field="uen_id"
                                                     value="Uen"></iais:sortableHeader>

                                <iais:sortableHeader needSort="true" field="nric_number"
                                                     value="Singpass ID"></iais:sortableHeader>

                                <iais:sortableHeader needSort="true" field="action_time"
                                                     value="Operation Date and Time"></iais:sortableHeader>
                                <iais:sortableHeader needSort="true" field="client_ip"
                                                     value="Source Client IP"></iais:sortableHeader>
                                <iais:sortableHeader needSort="true" field="user_agent"
                                                     value="User Agent"></iais:sortableHeader>
                                <iais:sortableHeader needSort="true" field="moh_user_id"
                                                     value="MOH user's account ID"></iais:sortableHeader>
                                <iais:sortableHeader needSort="true" field="session_id"
                                                     value="Session ID"></iais:sortableHeader>
                                <iais:sortableHeader needSort="true" field="TOTAL_SESSION_DURATION"
                                                     value="Total Session Duration"></iais:sortableHeader>
                                <iais:sortableHeader needSort="true" field="application_number"
                                                     value="Application ID"></iais:sortableHeader>
                                <iais:sortableHeader needSort="true" field="license_number"
                                                     value="Licence Number"></iais:sortableHeader>
                                <iais:sortableHeader needSort="true" field="module" value="Module"></iais:sortableHeader>
                                <iais:sortableHeader needSort="true" field="function_name"
                                                     value="Function / Use Case"></iais:sortableHeader>
                                <iais:sortableHeader needSort="true" field="programme_name"
                                                     value="Program Name"></iais:sortableHeader>
                                <iais:sortableHeader needSort="true" field="operation_desc"
                                                     value="Data Activities"></iais:sortableHeader>
                                <iais:sortableHeader needSort="true" field="moh_user_id"
                                                     value="Created By"></iais:sortableHeader>
                                <iais:sortableHeader needSort="true" field="action_time"
                                                     value="Created Date"></iais:sortableHeader>
                            </tr>
                            </thead>

                            <tbody>
                            <c:choose>
                                <c:when test="${empty auditTrailSearchResult.rows}">
                                    <tr>
                                        <td colspan="6">
                                            <iais:message key="GENERAL_ACK018" escape="true"></iais:message>
                                        </td>
                                    </tr>
                                </c:when>
                                <c:otherwise>
                                    <c:forEach var="resultRow" items="${auditTrailSearchResult.rows}" varStatus="status">
                                        <tr>
                                            <td class="row_no">${(status.index + 1) + (auditTrailSearch.pageNo - 1) * auditTrailSearch.pageSize}</td>
                                                <%-- <td>${resultRow.operationType}</td>--%>
                                            <td>
                                                <a href="javascript:;" style="text-decoration:underline;"
                                                   onclick="viewData('<iais:mask name="auditId"
                                                                                 value="${resultRow.auditId}"/>')">${resultRow.operationDesc}</a>
                                            </td>
                                            <td><c:out value="${resultRow.domainDesc}"></c:out></td>
                                            <c:choose>

                                                <%--batchjob--%>
                                                <c:when test="${resultRow.domain == 20001}">
                                                    <td><c:out value="${resultRow.entityId}"></c:out></td>
                                                    <td>-</td>
                                                    <td>-</td>
                                                    <td>-</td>
                                                    <td>-</td>
                                                    <td>-</td>
                                                </c:when>

                                                <%--internet--%>
                                                <c:when test="${resultRow.domain == 20003}">
                                                    <c:choose>
                                                        <%-- singpass--%>
                                                        <c:when test="${resultRow.loginType == 10001}">
                                                            <td>-</td>
                                                            <td>-</td>
                                                            <td>-</td>
                                                            <td>-</td>
                                                            <td>-</td>
                                                            <td><c:out value="${resultRow.nricNumber}"></c:out></td>
                                                        </c:when>
                                                        <%--corppass--%>
                                                        <c:when test="${resultRow.loginType == 10002}">
                                                            <td>-</td>
                                                            <td>-</td>

                                                            <c:choose>
                                                                <c:when test="${empty resultRow.entityId}">
                                                                    <td><c:out value="${resultRow.nricNumber}"></c:out></td>
                                                                    <td><c:out value="${resultRow.nricNumber}"></c:out></td>
                                                                    <td><c:out value="${resultRow.uenId}"></c:out></td>
                                                                </c:when>
                                                                <c:otherwise>
                                                                    <td><c:out value="${resultRow.entityId}"></c:out></td>
                                                                    <td><c:out value="-"></c:out></td>
                                                                    <td><c:out value="${resultRow.uenId}"></c:out></td>
                                                                </c:otherwise>
                                                            </c:choose>


                                                            <td>-</td>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <td>-</td>
                                                            <td>-</td>
                                                            <td>-</td>
                                                            <td>-</td>
                                                            <td>-</td>
                                                            <td></td>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </c:when>

                                                <%--intranet--%>
                                                <c:when test="${resultRow.domain == 20002}">
                                                    <td>-</td>
                                                    <td><c:out value="${resultRow.mohUserId}"></c:out></td>
                                                    <td>-</td>
                                                    <td>-</td>
                                                    <td>-</td>
                                                    <td>-</td>
                                                </c:when>

                                                <c:otherwise>
                                                    <td>-</td>
                                                    <td>-</td>
                                                    <td>-</td>
                                                    <td>-</td>
                                                    <td>-</td>
                                                    <td>-</td>
                                                </c:otherwise>
                                            </c:choose>




                                            <td><fmt:formatDate value='${resultRow.actionTime}' pattern='dd/MM/yyyy HH:mm:ss'/></td>
                                            <td>${empty resultRow.clientIp ? "-" : resultRow.clientIp}</td>
                                            <td>${empty resultRow.userAgent ? "-" : resultRow.userAgent}</td>

                                                <%-- intranet --%>
                                            <c:choose>
                                                <c:when test="${resultRow.domain == 20002}">
                                                    <td>${empty resultRow.mohUserId ? "-" : resultRow.mohUserId}</td>
                                                </c:when>
                                                <c:otherwise>
                                                    <td>-</td>
                                                </c:otherwise>
                                            </c:choose>

                                            <td>${empty resultRow.sessionId ? "-" : resultRow.sessionId}</td>

                                            <td>${empty resultRow.totalSessionDuration ? "-" : resultRow.totalSessionDuration}</td>
                                            <td>${empty resultRow.appNum ? "-" : resultRow.appNum}</td>
                                            <td>${empty resultRow.licenseNum ? "-" : resultRow.licenseNum}</td>
                                            <td>${empty resultRow.module ? "-" : resultRow.module}</td>
                                            <td>${empty resultRow.functionName ? "-" : resultRow.functionName}</td>
                                            <td>${empty resultRow.programmeName ? "-" : resultRow.programmeName}</td>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${resultRow.operation == 4}">
                                                        Data Read
                                                    </c:when>
                                                    <c:when test="${resultRow.operation == 6}">
                                                        Data Inserted
                                                    </c:when>
                                                    <c:when test="${resultRow.operation == 7}">
                                                        Data Updated
                                                    </c:when>
                                                    <c:when test="${resultRow.operation == 8}">
                                                        Data Deleted
                                                    </c:when>
                                                    <c:otherwise>
                                                        <c:out value="-"></c:out>
                                                    </c:otherwise>
                                                </c:choose>

                                            </td>

                                            <c:choose>
                                                <%--batchjob--%>
                                                <c:when test="${resultRow.domain == 20001}">
                                                    <td><c:out value="${resultRow.nricNumber}"></c:out></td>
                                                </c:when>

                                                <%--internet--%>
                                                <c:when test="${resultRow.domain == 20003}">
                                                    <td><c:out value="${resultRow.nricNumber}"></c:out></td>
                                                </c:when>

                                                <%--intranet--%>
                                                <c:when test="${resultRow.domain == 20002}">
                                                    <td><c:out value="${resultRow.mohUserId}"></c:out></td>
                                                </c:when>

                                                <c:otherwise>
                                                    <td>-</td>
                                                </c:otherwise>
                                            </c:choose>

                                            <td><fmt:formatDate value='${resultRow.actionTime}' pattern='dd/MM/yyyy'/></td>
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
</div>

<%@include file="/WEB-INF/jsp/include/validation.jsp" %>
<%@include file="/WEB-INF/jsp/include/utils.jsp" %>
<script>
    function viewData(id) {
        $("#auditId").val(id)
        SOP.Crud.cfxSubmit("mainForm", "viewActivities");
    }

    $(document).ready(function () {
        let collapseFlag = $('input[name="collapseFlag"]').val();
        $('#searchCondition').addClass(collapseFlag)
    });

    $('#filterBtn').on('click', function () {
        let expanded = $('#searchCondition').attr("aria-expanded")
        if ('true' == expanded){
            $('input[name="collapseFlag"]').val('in');
        }else {
            $('input[name="collapseFlag"]').val();
        }
    });
</script>


