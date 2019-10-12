<%--
  Created by IntelliJ IDEA.
  User: yichen
  Date: 9/17/2019
  Time: 3:26 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://www.ecq.com/iais"   prefix="iais"%>


<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>


<webui:setAttribute name="header-ext">
    <%
        /* You can add additional content (SCRIPT, STYLE elements)
         * which need to be placed inside HEAD element here.
         */
    %>
</webui:setAttribute>

<webui:setAttribute name="title">
    <%
        /* You can set your page title here. */
    %>

    <%=process.runtime.getCurrentComponentName()%>

</webui:setAttribute>
<!-- START: CSS -->

<!-- END: CSS -->

<form id = "mainForm" method = "post" action=<%=process.runtime.continueURL()%>>
    <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
    <input type="hidden" name="crud_action_type" value="">
    <input type="hidden" name="crud_action_value" value="">
    <input type="hidden" name="crud_action_additional" value="">

    <iais:body>
    <iais:section title="Audit Trail Module" id="atmList">
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
                <input type="date" name="startDate"  />
            </iais:value>

            <iais:field value="Operation End Date Time" required="false"></iais:field>
            <iais:value width="7">
                <input type="date" name="endDate"  />
            </iais:value>

        </iais:row>

        <iais:action>
            <button type="button" class="search btn" onclick="javascript:searchRecords();">Search</button>
        </iais:action>

    </iais:section>
    </br>

    <iais:error>
        <c:if test = "${not empty errorMap}">
            <div class="error">
                <c:forEach items="${errorMap}" var="map">
                    ${map.key}  ${map.value} <br/>
                </c:forEach>
            </div>
        </c:if>
    </iais:error>

    </br>
    <iais:pagination  param="auditTrailSearch" result="auditTrailSearchResult"/>
    <iais:searchSection title="" onclick="">
    <div class="table-responsive" id="no-more-tables">
        <table class="table table-bordered table-condensed cf alignctr shadow" id="tableId">
            <colgroup>
                <col style="width: 10%;"/>
                <col style="width: 20%;"/>
                <col style="width: 20%;"/>
                <col style="width: 20%;"/>
                <col style="width: 20%;"/>
                <col style="width: 10%;"/>
            </colgroup>
            <thead>
            <tr>
                <iais:sortableHeader needSort="false"  field="" value="No."></iais:sortableHeader>
                <iais:sortableHeader needSort="true"  field="operationType" value="Operation Type"></iais:sortableHeader>
                <iais:sortableHeader needSort="true"   field="operation" value="Operation"></iais:sortableHeader>
                <iais:sortableHeader needSort="true"   field="batchJobID" value="Batch Job ID"></iais:sortableHeader>
                <iais:sortableHeader needSort="true"   field="directoryID" value="Active directory ID"></iais:sortableHeader>
                <iais:sortableHeader needSort="true"   field="corpPassId" value="Corp-Pass ID"></iais:sortableHeader>
                <iais:sortableHeader needSort="true"   field="corpPassNric" value="Corp-Pass NRIC"></iais:sortableHeader>
                <iais:sortableHeader needSort="true"   field="uenId" value="UEN"></iais:sortableHeader>
                <iais:sortableHeader needSort="true"   field="singPassId" value="SingPass ID"></iais:sortableHeader>
                <iais:sortableHeader needSort="true"   field="operationDate" value="Operation Date"></iais:sortableHeader>
                <iais:sortableHeader needSort="true"   field="operationTime" value="Operation Time"></iais:sortableHeader>
                <iais:sortableHeader needSort="true"   field="clientIp" value="Source Client IP"></iais:sortableHeader>
                <iais:sortableHeader needSort="true"   field="userAgent" value="User Agent"></iais:sortableHeader>
                <iais:sortableHeader needSort="true"   field="mohUserAccountId" value="MOH User’s account ID"></iais:sortableHeader>
                <iais:sortableHeader needSort="true"   field="sessionId" value="Session Id"></iais:sortableHeader>
                <iais:sortableHeader needSort="true"   field="applicationId" value="Application ID"></iais:sortableHeader>
                <iais:sortableHeader needSort="true"   field="licenceNumber" value="Licence Number"></iais:sortableHeader>
                <iais:sortableHeader needSort="true"   field="module" value="Module"></iais:sortableHeader>
                <iais:sortableHeader needSort="true"   field="userCase" value="Function / Use Case"></iais:sortableHeader>
                <iais:sortableHeader needSort="true"   field="programName" value="Program Name"></iais:sortableHeader>
                <iais:sortableHeader needSort="true"   field="dataActivities" value="Data Activities"></iais:sortableHeader>
                <iais:sortableHeader needSort="true"   field="createdby" value="Created by"></iais:sortableHeader>
                <iais:sortableHeader needSort="true"   field="createddate" value="Created date"></iais:sortableHeader>
            </tr>
            </thead>

            <tbody style="text-align: center">
            <c:choose>
                <c:when test="${empty auditTrailSearchResult.rows}">
                    <tr>
                        <td colspan="6">
                            No Record!!
                        </td>
                    </tr>
                </c:when>
                <c:otherwise>
                    <%-- message entity--%>
                    <c:forEach var = "resultRow" items = "${auditTrailSearchResult.rows}" varStatus="status">
                        <tr>
                            <td class="row_no">${(status.index + 1) + (auditTrailSearch.pageNo - 1) * auditTrailSearch.pageSize}</td>
                            <td>${resultRow.operationType}</td>
                            <td>${resultRow.operation}</td>
                            <td>${resultRow.uenId}</td>
                            <td>${resultRow.actionTime}</td>
                            <td>${resultRow.nricNumber}</td>
                            <td>${resultRow.mohUserId}</td>
                            <td>${resultRow.clientIp}</td>
                            <td>${resultRow.sessionId}</td>
                            <td>${resultRow.appNum}</td>
                            <td>${resultRow.userAgent}</td>
                            <td>${resultRow.licenseNum}</td>
                            <td>${resultRow.programmeName}</td>
                        </tr>
                    </c:forEach>
                    <iais:action>
                        <button type="button" class="export btn" onclick="javascript:exportRecords();"  >Export</button>
                    </iais:action>
                </c:otherwise>

            </c:choose>
            </tbody>
        </table>

        </iais:searchSection>

        </iais:body>
</form>

<script type="text/javascript">
    function doExport() {
        SOP.Crud.cfxSubmit("mainForm", "exportRecords");
    }

    function doQuery(){
        SOP.Crud.cfxSubmit("mainForm", "searchRecords");
    }

    function sortRecords(sortFieldName,sortType){
        SOP.Crud.cfxSubmit("mainForm","sortRecords",sortFieldName,sortType);
    }

    function jumpToPagechangePage(){
        SOP.Crud.cfxSubmit("mainForm","changePage");
    }

    function prepareEdit(rowguid){
        if(confirm('are sure you want to edit ? ')){
            SOP.Crud.cfxSubmit("messageForm", "prepareEdit", rowguid);
        }
    }

    function disable(auditId){
        if(confirm('are sure you want to disable ? ')){
            SOP.Crud.cfxSubmit("messageForm", "disableStatus", auditId);
        }
    }

</script>

