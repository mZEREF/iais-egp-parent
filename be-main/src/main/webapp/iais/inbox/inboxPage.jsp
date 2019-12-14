<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais"%>
<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>
<webui:setLayout name="iais-intranet"/>
<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>
<%
    String webroot=IaisEGPConstant.FE_CSS_ROOT;
%>

<form method="post" id="mainSupForm" action=<%=process.runtime.continueURL()%>>
    <%@ include file="/include/formHidden.jsp" %>
    <input type="hidden" name="SearchSwitchType" value="">
    <input type="hidden" id="taskId" name="taskId" value="">
    <input type="hidden" id="inspector_name" name="inspector_name" value="">

    <iais:body >
        <div class="container">
            <div class="col-xs-12">
                <div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">
                    <br><br><br><br>
                    <h3>
                        <span>Search Criteria</span>
                    </h3>
                    <div class="panel panel-default">
                        <div class="panel-collapse collapse in" id="collapseOne" role="tabpanel" aria-labelledby="headingOne" aria-expanded="true" style="">
                            <div class="panel-body">
                                <div class="panel-main-content">
                                    <iais:section title="" id = "supPoolList">
                                        <iais:row>
                                            <iais:field value="HCI Code"/>
                                            <iais:value width="18">
                                                <input type="text" name="hci_code" value="${supTaskSearchParam.filters['hci_code']}" />
                                            </iais:value>
                                        </iais:row>
                                        <iais:row>
                                            <iais:field value="HCI Name"/>
                                            <iais:value width="18">
                                                <input type="text" name="hci_name" value="${supTaskSearchParam.filters['hci_name']}" />
                                            </iais:value>
                                        </iais:row>
                                        <iais:row>
                                            <iais:field value="HCI Address"/>
                                            <iais:value width="18">
                                                <input type="text" name="hci_address" value="${supTaskSearchParam.filters['blk_no']}" />
                                            </iais:value>
                                        </iais:row>
                                        <iais:row>
                                            <iais:field value="Application No."/>
                                            <iais:value width="18">
                                                <input type="text" name="application_no" value="${supTaskSearchParam.filters['application_no']}" />
                                            </iais:value>
                                        </iais:row>
                                        <iais:row>
                                            <iais:field value="Application Type"/>
                                            <iais:value width="18">
                                                <iais:select name="application_type" options="appTypeOption" firstOption="Please select" value="${supTaskSearchParam.filters['application_type']}" ></iais:select>
                                            </iais:value>
                                        </iais:row>
                                        <iais:row>
                                            <iais:field value="Application Status"/>
                                            <iais:value width="18">
                                                <iais:select name="application_status" options="appStatusOption" firstOption="Please select" value="${supTaskSearchParam.filters['application_status']}" ></iais:select>
                                            </iais:value>
                                        </iais:row>
                                        <iais:row>
                                            <iais:field value="Inspector Name"/>
                                            <iais:value width="18">
                                                <div onload="inspectorSearchTask_optionNameAuto(${inspectorValue})">
                                                    <select name = "inspectorSearchTask_inspectorName" id="inspectorSearchTask_inspectorName" onclick="javascript:doInspectorSearchTaskSelect()">
                                                        <option value="-">Please select</option>
                                                        <c:forEach var="inspector" items="${inspectorOption.rows}">
                                                            <option value="<iais:mask name="inspectorName" value="${inspector.value}"/>"><c:out value="${inspector.text}"/></option>
                                                        </c:forEach>
                                                    </select>
                                                </div>
                                            </iais:value>
                                        </iais:row>
                                        <iais:action style="text-align:center;">
                                            <button class="btn btn-lg btn-login-submit" type="button" style="background:#2199E8; color: white" onclick="javascript:doInspectorSearchTaskSearch()">Search</button>
                                            <button class="btn btn-lg btn-login-clear" type="button" style="background:#2199E8; color: white" onclick="javascript:doInspectorSearchTaskClear()">Clear</button>
                                        </iais:action>
                                    </iais:section>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <iais:pagination  param="supTaskSearchParam" result="supTaskSearchResult"/>
        <div class="container">
            <div class="col-xs-12">
                <div class="components">
                    <h3>
                        <span>Search Result</span>
                    </h3>
                    <div class="table-gp">
                        <table class="table">
                            <thead>
                            <tr align="center">
                                <iais:sortableHeader needSort="false" field="" value="S/N"></iais:sortableHeader>
                                <iais:sortableHeader needSort="false"  field="APPLICATION_NO" value="Application No."></iais:sortableHeader>
                                <iais:sortableHeader needSort="false"  field="APP_TYPE" value="Application Type"></iais:sortableHeader>
                                <iais:sortableHeader needSort="false"  field="HCI_CODE" value="HCI Code"></iais:sortableHeader>
                                <iais:sortableHeader needSort="false"  field="HCI_NAME" value="HCI Name / Address"></iais:sortableHeader>
                                <iais:sortableHeader needSort="false"  field="SERVICE_ID" value="Service Name"></iais:sortableHeader>
                                <iais:sortableHeader needSort="false" field="END_DATE" value="Licence Expiry Date (dd/mm/yyyy)"></iais:sortableHeader>
                                <iais:sortableHeader needSort="false" field="" value="Inspection Date (dd/mm/yyyy)"></iais:sortableHeader>
                                <iais:sortableHeader needSort="false" field="Status" value="Application Status"></iais:sortableHeader>
                                <iais:sortableHeader needSort="false" field="" value="Inspector"></iais:sortableHeader>
                                <iais:sortableHeader needSort="false" field="" value="Inspection Lead"></iais:sortableHeader>
                            </tr>
                            </thead>
                            <tbody>
                            <c:choose>
                                <c:when test="${empty supTaskSearchResult.rows}">
                                    <tr>
                                        <td colspan="12">
                                            <iais:message key="No Result!" escape="true"></iais:message>
                                            <!--No Record!!-->
                                        </td>
                                    </tr>
                                </c:when>
                                <c:otherwise>
                                    <c:forEach var="pool" items="${supTaskSearchResult.rows}" varStatus="status">
                                        <tr>
                                            <td class="row_no"><c:out value="${(status.index + 1) + (supTaskSearchParam.pageNo - 1) * supTaskSearchParam.pageSize}"/></td>
                                            <td><c:out value="${pool.applicationNo}"/></td>
                                            <td><c:out value="${pool.applicationType}"/></td>
                                            <td><c:out value="${pool.hciCode}"/></td>
                                            <td><c:out value="${pool.hciName}"/></td>
                                            <td><c:out value="${pool.serviceName}"/></td>
                                            <td><c:out value="${pool.serviceEndDate}"/></td>
                                            <td><c:out value="${pool.inspectionDate}"/></td>
                                            <td><c:out value="${pool.applicationStatus}"/></td>
                                            <td><c:out value="${pool.inspector}"/></td>
                                            <td><c:out value="${pool.inspectorLead}"/></td>
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
    </iais:body>
</form>
<script type="text/javascript">
    function inspectorSearchTask_optionNameAuto(value){
        if(value != null && value != null){
            $("#inspectorSearchTask_inspectorName").val(value);
        }
        doInspectorSearchTaskSelect();
    }

    function doInspectorSearchTaskSelect(){
        var options=$("#inspectorSearchTask_inspectorName option:selected");
        $("#inspector_name").val(options);
    }

    function doInspectorSearchTaskAssign(taskId) {
        $("#taskId").val(taskId);
        submit('assign');
    }

    function doInspectorSearchTaskClear() {
        $('input[name="application_no"]').val("");
        $("#application_type option:first").prop("selected", 'selected');
        $("#application_status option:first").prop("selected", 'selected');
        $("#inspector_name option:first").prop("selected", 'selected');
        $('input[name="hci_code"]').val("");
        $('input[name="hci_name"]').val("");
        $('input[name="hci_address"]').val("");
    }
    function submit(action){
        $("[name='SearchSwitchType']").val(action);
        var mainPoolForm = document.getElementById('mainSupForm');
        mainPoolForm.submit();
    }
    function doInspectorSearchTaskSearch() {
        submit('search');
    }
</script>
