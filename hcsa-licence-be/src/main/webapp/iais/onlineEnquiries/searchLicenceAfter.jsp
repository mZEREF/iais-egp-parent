<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.utils.ParamUtil" %>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>
<webui:setLayout name="iais-intranet"/>
<form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
    <%@ include file="/include/formHidden.jsp" %>
    <input type="hidden" name="crud_action_type" value=""/>
    <input type="hidden" name="crud_action_value" value=""/>
    <input type="hidden" name="crud_action_additional" value=""/>
    <iais:body >
        <div class="container">
            <div class="col-xs-12">
                <div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">
                    <br><br><br><br>
                    <h3>
                        <span>Advanced Search Criteria</span>
                    </h3>
                    <c:if test="${choose[2]==3||choose[1]==2}">
                        <h4>
                            <span>
                                Search By Application
                                <c:choose >
                                    <c:when test="${choose[2]==3||choose[1]==2}">
                                    <label>
                                        <input type="checkbox" checked name="application" />
                                    </label>
                                    </c:when>
                                    <c:otherwise>
                                    <label>
                                        <input type="checkbox" name="application" />
                                    </label>
                                    </c:otherwise>
                                </c:choose>
                            </span>

                        </h4>
                        <div class="panel panel-default">
                            <div class="panel-collapse collapse in" id="collapseOne" role="tabpanel" aria-labelledby="headingOne" aria-expanded="true" style="">
                                <div class="panel-body">
                                    <div class="panel-main-content">
                                        <iais:section title="" id = "supPoolList">
                                            <iais:row>
                                                <iais:field value="Application No:"/>
                                                <iais:value width="18">
                                                    <label>
                                                        <input type="text" style="width:400px; font-weight:normal;" name="application_no" value="${SearchParam.filters['appNo']}" />
                                                    </label>
                                                </iais:value>
                                            </iais:row>
                                            <iais:row>
                                                <iais:field value="Application Type"/>
                                                <iais:value width="18">
                                                    <iais:select name="application_type" options="appTypeOption" firstOption="Please select" value="${appTypeOption}" ></iais:select>
                                                </iais:value>
                                            </iais:row>
                                            <iais:row>
                                                <iais:field value="Application Status"/>
                                                <iais:value width="18">
                                                    <iais:select name="application_status" options="appStatusOption" firstOption="Please select" value="${appStatusOption}" ></iais:select>
                                                </iais:value>
                                            </iais:row>
                                            <iais:row>
                                                <iais:field value="Application Submitted Date:"/>
                                                <iais:value width="18">
                                                    <iais:datePicker id = "sub_date" name = "sub_date" value="${SearchParam.filters['subDate']}" ></iais:datePicker>
                                                </iais:value>
                                                <iais:field value="To:"/>
                                                <iais:value width="18">
                                                    <iais:datePicker id = "to_date" name = "to_date" value="${SearchParam.filters['toDate']}"></iais:datePicker>
                                                </iais:value>
                                            </iais:row>
                                            <iais:row>
                                                <iais:field value="Licence No:"/>
                                                <iais:value width="18">
                                                    <label>
                                                        <input type="text" style="width:400px; font-weight:normal;" name="licence_no" value="${SearchParam.filters['licence_no']}" />
                                                    </label>
                                                </iais:value>
                                            </iais:row>
                                            <iais:row>
                                                <iais:field value="UEN No:"/>
                                                <iais:value width="18">
                                                    <label>
                                                        <input type="text" style="width:400px; font-weight:normal;" name="uen_no" value="${SearchParam.filters['uen_no']}" />
                                                    </label>
                                                </iais:value>
                                            </iais:row>
                                            <iais:row>
                                                <iais:field value="Service Licence:"/>
                                                <iais:value width="18">
                                                    <iais:select name="service_licence_type" options="licSvcTypeOption" firstOption="Please select" value="${licSvcTypeOption}" ></iais:select>
                                                </iais:value>
                                            </iais:row>
                                            <iais:row>
                                                <iais:field value="Service Sub-Type:"/>
                                                <iais:value width="18">
                                                    <iais:select name="service_sub_type" options="licSvcSubTypeOption" firstOption="Please select" value="${licSvcSubTypeOption}" ></iais:select>
                                                </iais:value>
                                            </iais:row>
                                            <iais:row>
                                                <iais:field value="Licence Status:"/>
                                                <iais:value width="18">
                                                    <iais:select name="licence_status" options="licStatusOption" firstOption="Please select" value="${licStatusOption}" ></iais:select>
                                                </iais:value>
                                            </iais:row>
                                            <iais:row>
                                                <iais:field value="Licence Start Date:"/>
                                                <iais:value width="18">
                                                    <iais:datePicker id = "start_date" name = "start_date" value="${SearchParam.filters['start_date']}" ></iais:datePicker>
                                                </iais:value>
                                                <iais:field value="To:"/>
                                                <iais:value width="18">
                                                    <iais:datePicker id = "start_to_date" name = "start_to_date" value="${SearchParam.filters['expiry_date']}"></iais:datePicker>
                                                </iais:value>
                                            </iais:row>
                                            <iais:row>
                                                <iais:field value="Licence Expiry Date:"/>
                                                <iais:value width="18">
                                                    <iais:datePicker id = "expiry_start_date" name = "expiry_start_date" value="${SearchParam.filters['start_date']}" ></iais:datePicker>
                                                </iais:value>

                                                <iais:field value="To:"/>
                                                <iais:value width="18">
                                                    <iais:datePicker id = "expiry_date" name = "expiry_date" value="${SearchParam.filters['expiry_date']}"></iais:datePicker>
                                                </iais:value>
                                            </iais:row>
                                        </iais:section>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </c:if>
                    <c:if test="${choose[0]==1}">
                        <h4>
                        <span>Search By HCI
                            <c:choose >
                                <c:when test="${choose[0]==1}">
                                    <label>
                                        <input type="checkbox" checked name="hci" />
                                    </label>
                                </c:when>
                                <c:otherwise>
                                    <label>
                                        <input type="checkbox" name="hci" />
                                    </label>
                                </c:otherwise>
                            </c:choose>
                        </span>
                        </h4>
                        <div class="panel panel-default">
                            <div class="panel-collapse collapse in" id="collapseTwo" role="tabpanel" aria-labelledby="headingOne" aria-expanded="true" style="">
                                <div class="panel-body">
                                    <div class="panel-main-content">
                                        <iais:section title="" id = "supPoolList">
                                            <iais:row>
                                                <iais:field value="HCI Code:"/>
                                                <iais:value width="18">
                                                    <label>
                                                        <input type="text" style="width:400px; font-weight:normal;" name="hci_code" value="${SearchParam.filters['hci_code']}" />
                                                    </label>
                                                </iais:value>
                                            </iais:row>
                                            <iais:row>
                                                <iais:field value="HCI Name:"/>
                                                <iais:value width="18">
                                                    <label>
                                                        <input type="text" style="width:400px; font-weight:normal;" name="hci_name" value="${SearchParam.filters['hci_name']}" />
                                                    </label>
                                                </iais:value>
                                            </iais:row>
                                            <iais:row>
                                                <iais:field value="HCI Street Name:"/>
                                                <iais:value width="18">
                                                    <label>
                                                        <input type="text" style="width:400px; font-weight:normal;" name="hci_street_name" value="${SearchParam.filters['hci_street_name']}" />
                                                    </label>
                                                </iais:value>
                                            </iais:row>
                                            <iais:row>
                                                <iais:field value="HCI Postal Code:"/>
                                                <iais:value width="18">
                                                    <label>
                                                        <input type="text" style="width:400px; font-weight:normal;" name="hci_postal_code" value="${SearchParam.filters['hci_postal_code']}" />
                                                    </label>
                                                </iais:value>
                                            </iais:row>

                                        </iais:section>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </c:if>
                    <c:if test="${choose[3]==4}">
                        <h4>
                        <span>Search By Licensee
                            <c:choose >
                                <c:when test="${choose[3]==4}">
                                    <label>
                                        <input type="checkbox" checked name="licensee" />
                                    </label>
                                </c:when>
                                <c:otherwise>
                                    <label>
                                        <input type="checkbox" name="licensee" />
                                    </label>
                                </c:otherwise>
                            </c:choose>
                        </span>
                        </h4>
                        <div class="panel panel-default">
                            <div class="panel-collapse collapse in" id="collapseThree" role="tabpanel" aria-labelledby="headingOne" aria-expanded="true" style="">
                                <div class="panel-body">
                                    <div class="panel-main-content">
                                        <iais:section title="" id = "supPoolList">
                                            <iais:row>
                                                <iais:field value="Licensee ID:"/>
                                                <iais:value width="18">
                                                    <label>
                                                        <input type="text" name="licensee_id" value="${SearchParam.filters['licensee_id']}" />
                                                    </label>
                                                </iais:value>
                                            </iais:row>
                                            <iais:row>
                                                <iais:field value="Licensee Name:"/>
                                                <iais:value width="18">
                                                    <label>
                                                        <input type="text" name="licensee_name" value="${SearchParam.filters['licensee_name']}" />
                                                    </label>
                                                </iais:value>
                                            </iais:row>
                                            <iais:row>
                                                <iais:field value="Licensee Professional Regn No:"/>
                                                <iais:value width="18">
                                                    <label>
                                                        <input type="text" name="licensee_regn_no" value="${SearchParam.filters['licensee_regn_no']}" />
                                                    </label>
                                                </iais:value>
                                            </iais:row>
                                        </iais:section>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </c:if>
                    <c:if test="${choose[4]==5}">
                        <h4>
                        <span>Search By Service Personnel Details (CGO, PO, DPO & MedAlert)
                            <c:choose >
                                <c:when test="${choose[4]==5}">
                                    <label>
                                        <input type="checkbox" checked name="servicePersonnel" />
                                    </label>
                                </c:when>
                                <c:otherwise>
                                    <label>
                                        <input type="checkbox" name="servicePersonnel" />
                                    </label>
                                </c:otherwise>
                            </c:choose>

                        </span>
                        </h4>
                        <div class="panel panel-default">
                            <div class="panel-collapse collapse in" id="collapseFour" role="tabpanel" aria-labelledby="headingOne" aria-expanded="true" style="">
                                <div class="panel-body">
                                    <div class="panel-main-content">
                                        <iais:section title="" id = "supPoolList">
                                            <iais:row>
                                                <iais:field value="Service Personnal ID:"/>
                                                <iais:value width="18">
                                                    <label>
                                                        <input type="text" name="service_id" value="${SearchParam.filters['service_id']}" />
                                                    </label>
                                                </iais:value>
                                            </iais:row>
                                            <iais:row>
                                                <iais:field value="Service Personnel Name:"/>
                                                <iais:value width="18">
                                                    <label>
                                                        <input type="text" name="service_name" value="${SearchParam.filters['service_name']}" />
                                                    </label>
                                                </iais:value>
                                            </iais:row>
                                            <iais:row>
                                                <iais:field value="Service Professional Regn No:"/>
                                                <iais:value width="18">
                                                    <label>
                                                        <input type="text" name="service_regn_no" value="${SearchParam.filters['service_regn_no']}" />
                                                    </label>
                                                </iais:value>
                                            </iais:row>
                                            <iais:row>
                                                <iais:field value="Service Professional Role:"/>
                                                <iais:value width="18">
                                                    <label>
                                                        <input type="text" name="service_role" value="${SearchParam.filters['service_role']}" />
                                                    </label>
                                                </iais:value>
                                            </iais:row>
                                        </iais:section>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </c:if>
                    <iais:action style="text-align:center;">
                        <button class="btn btn-lg btn-login-submit" type="button" style="background:#2199E8; color: white" onclick="javascript:doLicSearch()">Search</button>
                        <button class="btn btn-lg btn-login-submit" type="button" style="background:#2199E8; color: white" onclick="javascript:doLicBack()">Back</button>
                        <button class="btn btn-lg btn-login-clear" type="button" style="background:#2199E8; color: white" onclick="javascript:doLicClear()">Clear</button>
                    </iais:action>
                </div>
            </div>
        </div>
        <div class="container">
            <div class="col-xs-14">
                <div class="components">
                    <h3>
                        <span>Search Results</span>
                    </h3>
                        <%--                    <iais:pagination  param="SearchParam" result="SearchResult"/>--%>
                    <div class="table-gp">
                        <table class="table">
                            <thead>
                            <tr align="center">
                                <iais:sortableHeader needSort="false" field="" value="S/N"></iais:sortableHeader>
                                <iais:sortableHeader needSort="false"  field="APPLICATION_NO" value="Application No."></iais:sortableHeader>
                                <iais:sortableHeader needSort="false"  field="APP_TYPE" value="Application Type"></iais:sortableHeader>
                                <iais:sortableHeader needSort="false"  field="LICENCE_NO" value="Licence No."></iais:sortableHeader>
                                <iais:sortableHeader needSort="false"  field="HCI_CODE" value="HCI Code"></iais:sortableHeader>
                                <iais:sortableHeader needSort="false"  field="HCI_NAME" value="HCI Name "></iais:sortableHeader>
                                <iais:sortableHeader needSort="false"  field="ADDRESS" value=" Address"></iais:sortableHeader>
                                <iais:sortableHeader needSort="false"  field="LICENSEE_NAME" value="Licensee Name"></iais:sortableHeader>
                                <iais:sortableHeader needSort="false"  field="SERVICE_NAME" value="Service Name"></iais:sortableHeader>
                                <iais:sortableHeader needSort="false"  field="Licence_Period" value="Licence Period"></iais:sortableHeader>
                                <iais:sortableHeader needSort="false"  field="Licence_Status" value="Licence Status"></iais:sortableHeader>
                                <iais:sortableHeader needSort="false"  field="past_compliance_history" value="Past Compliance History"></iais:sortableHeader>
                                <iais:sortableHeader needSort="false"  field="current_risk_tagging" value="Current Risk Tagging"></iais:sortableHeader>
                                <iais:sortableHeader needSort="false" field="" value="Action"></iais:sortableHeader>
                            </tr>
                            </thead>
                            <tbody>
                            <c:choose>
                                <c:when test="${empty SearchResult.rows}">
                                    <tr>
                                        <td colspan="7">
                                            <iais:message key="ACK018" escape="true"></iais:message>
                                        </td>
                                    </tr>
                                </c:when>
                                <c:otherwise>
                                    <c:forEach var="pool" items="${SearchResult.rows}" varStatus="status">
                                        <tr>
                                            <td class="row_no"><c:out value="${status.index + 1}"/></td>
                                            <td>
                                                <c:if test="${pool.appCorrId==null}">${pool.applicationNo}</c:if>
                                                <c:if test="${pool.appCorrId!=null}"><a onclick="javascript:doAppInfo('${pool.appCorrId}')">${pool.applicationNo}</a></c:if>
                                            </td>
                                            <td><c:out value="${pool.applicationType}"/></td>
                                            <td><a onclick="javascript:doLicInfo('${pool.licenceId}')">${pool.licenceNo}</a></td>
                                            <td><c:out value="${pool.hciCode}"/></td>
                                            <td><c:out value="${pool.hciName}"/></td>
                                            <td><c:out value="${pool.blkNo}-${pool.floorNo}-${pool.unitNo}-${pool.streetName}-${pool.buildingName}"/></td>
                                            <td><c:out value="${pool.licenseeName}"/></td>
                                            <td><c:out value="${pool.serviceName}"/></td>
                                            <td><fmt:formatDate value="${pool.startDate}" pattern="dd/MM/yyyy" />-<fmt:formatDate value="${pool.expiryDate}" pattern="dd/MM/yyyy" /></td>
                                            <td><c:out value="${pool.licenceStatus}"/></td>
                                            <td><c:out value="${pool.pastComplianceHistory}"/></td>
                                            <td><c:out value="${pool.currentRiskTagging}"/></td>

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
        showWaiting();SOP.Crud.cfxSubmit("mainForm", "search");
    }
    function doLicBack(){
        showWaiting();SOP.Crud.cfxSubmit("mainForm", "back");
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

    function doLicInfo(licenceId) {
        showWaiting();

        SOP.Crud.cfxSubmit("mainForm", "details",licenceId);

    }
    function doAppInfo(appCorrId) {
        showWaiting();

        SOP.Crud.cfxSubmit("mainForm", "appDetails",appCorrId);
    }
</script>