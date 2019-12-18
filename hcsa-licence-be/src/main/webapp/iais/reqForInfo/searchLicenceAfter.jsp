<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts" %>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>
<webui:setLayout name="iais-intranet"/>
<form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
    <%@ include file="/include/formHidden.jsp" %>
    <input type="hidden" name="crud_action_type" value="">
    <input type="hidden" id="applicationNo" name="crud_action_value" value="">
    <input type="hidden" name="crud_action_additional" value="">
    <iais:body >
        <div class="container">
            <div class="col-xs-12">
                <div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">
                    <br><br><br><br>
                    <h3>
                        <span>Advanced Search Criteria For Licence</span>
                    </h3>
                    <div class="panel panel-default">
                        <div class="panel-collapse collapse in" id="collapseOne" role="tabpanel" aria-labelledby="headingOne" aria-expanded="true" style="">
                            <div class="panel-body">
                                <div class="panel-main-content">
                                    <iais:section title="" id = "supPoolList">
                                        <iais:row>
                                            <iais:field value="Licence No:"/>
                                            <iais:value width="18">
                                                <label>
                                                    <input type="text" name="licence_no" value="${searchNo}" />
                                                </label>
                                            </iais:value>
                                        </iais:row>
                                        <iais:row>
                                            <iais:field value="Service Licence Type"/>
                                            <iais:value width="18">
                                                <iais:select name="service_licence_type" options="licSvcTypeOption" firstOption="Please select" value="${licSvcTypeOption}" ></iais:select>
                                            </iais:value>
                                        </iais:row>
                                        <iais:row>
                                            <iais:field value="Licence Status"/>
                                            <iais:value width="18">
                                                <iais:select name="licence_status" options="licStatusOption" firstOption="Please select" value="${licStatusOption}" ></iais:select>
                                            </iais:value>
                                        </iais:row>
                                        <iais:row>
                                            <iais:field value="Licence Period:"/>
                                            <iais:value width="18">
                                                <iais:datePicker id = "sub_date" name = "sub_date" ></iais:datePicker>
                                            </iais:value>
                                        </iais:row>
                                        <iais:row>
                                            <iais:field value="To:"/>
                                            <iais:value width="18">
                                                <iais:datePicker id = "to_date" name = "to_date" ></iais:datePicker>
                                            </iais:value>
                                        </iais:row>
                                        <iais:action style="text-align:center;">
                                            <button class="btn btn-lg btn-login-submit" type="button" style="background:#2199E8; color: white" onclick="javascript:doLicSearch()">Search</button>
                                            <button class="btn btn-lg btn-login-submit" type="button" style="background:#2199E8; color: white" onclick="javascript:doLicBack()">Back</button>
                                            <button class="btn btn-lg btn-login-clear" type="button" style="background:#2199E8; color: white" onclick="javascript:doLicClear()">Clear</button>
                                        </iais:action>
                                    </iais:section>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
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
                                <iais:sortableHeader needSort="true"  field="APPLICATION_NO" value="Application No."></iais:sortableHeader>
                                <iais:sortableHeader needSort="true"  field="APP_TYPE" value="Application Type"></iais:sortableHeader>
                                <iais:sortableHeader needSort="true"  field="LICENCE_NO" value="Licence No."></iais:sortableHeader>
                                <iais:sortableHeader needSort="true"  field="HCI_CODE" value="HCI Code"></iais:sortableHeader>
                                <iais:sortableHeader needSort="true"  field="HCI_NAME" value="HCI Name "></iais:sortableHeader>
                                <iais:sortableHeader needSort="false"  field="ADDRESS" value=" Address"></iais:sortableHeader>
                                <iais:sortableHeader needSort="true"  field="LICENSEE_NAME" value="Licence NAME"></iais:sortableHeader>
                                <iais:sortableHeader needSort="true"  field="SERVICE_NAME" value="Service NAME"></iais:sortableHeader>
                                <iais:sortableHeader needSort="false"  field="Licence_Period" value="Licence Period"></iais:sortableHeader>
                                <iais:sortableHeader needSort="false"  field="Licence_Status" value="Licence Status"></iais:sortableHeader>
                                <iais:sortableHeader needSort="false"  field="past_compliance_history" value="Past Compliance History"></iais:sortableHeader>
                                <iais:sortableHeader needSort="false"  field="current_risk_tagging" value="Current Risk Tagging"></iais:sortableHeader>
                                <iais:sortableHeader needSort="false" field="" value="Action"></iais:sortableHeader>
                            </tr>
                            </thead>
                            <tbody>
                            <c:choose>
                                <c:when test="${empty supTaskSearchResult.rows}">
                                    <tr>
                                        <td colspan="7">
                                            <iais:message key="ACK018" escape="true"></iais:message>
                                        </td>
                                    </tr>
                                </c:when>
                                <c:otherwise>
                                    <c:forEach var="pool" items="${supTaskSearchResult.rows}" varStatus="status">
                                        <tr>
                                            <td class="row_no"><c:out value="${(status.index + 1) + (supTaskSearchParam.pageNo - 1) * supTaskSearchParam.pageSize}"/></td>
                                            <td><a onclick="doAppInfo(${pool.applicationNo})">${pool.applicationNo}</a></td>
                                            <td><c:out value="${pool.applicationType}"/></td>
                                            <td><c:out value="${pool.licenceNo}"/></td>
                                            <td><c:out value="${pool.hciCode}"/></td>
                                            <td><c:out value="${pool.hciName}"/></td>
                                            <td><c:out value="${pool.address}"/></td>
                                            <td><c:out value="${pool.licenseeName}"/></td>
                                            <td><c:out value="${pool.serviceName}"/></td>
                                            <td><c:out value="${pool.licencePeriod}"/></td>
                                            <td><c:out value="${pool.licenceStatus}"/></td>
                                            <td><c:out value="${pool.pastComplianceHistory}"/></td>
                                            <td><c:out value="${pool.currentRiskTagging}"/></td>

                                            <td><button type="button"  class="btn btn-default" onclick="javascript:doReqForInfo('<iais:mask name="applicationNo" value="${pool.applicationNo}"/>');">Select</button></td>
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
    function doLicSearch(){
        SOP.Crud.cfxSubmit("mainForm", "searchLic");
    }
    function doLicBack(){
        SOP.Crud.cfxSubmit("mainForm", "back");
    }
    function doLicClear(){
        $('input[name="licence_no"]').val("");
        $("#licence_type option:first").val("");
        $("#licence_type option:first").prop("selected", 'selected');
        $("#licence_status option:first").val("");
        $("#licence_status option:first").prop("selected", 'selected');
        $('input[name="to_date"]').val("");
        $('input[name="sub_date"]').val("");
    }
    function doReqForInfo(appNo) {
        $("#applicationNo").val(appNo);
        submit('reqForInfo');
    }
    function doAppInfo(appNo) {
        $("#applicationNo").val(appNo);
        submit('appInfo');
    }
</script>