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
            <div class="col-xs-15">
                <div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">
                    <br><br><br><br><br><br>
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

                            <div class="panel-collapse collapse in" id="collapseOne" role="tabpanel" aria-labelledby="headingOne" aria-expanded="true" style="">
                                <div class="panel-body">
                                    <div class="panel-main-content">
                                        <iais:section title="" id = "supPoolList">
                                            <iais:row>
                                                <iais:field value="Application No."/>
                                                <iais:value width="18">
                                                    <label>
                                                        <input type="text" style="width:180%; font-weight:normal;" name="application_no" value="${SearchParam.filters['appNo']}" />
                                                    </label>
                                                </iais:value>
                                            </iais:row>
                                            <iais:row>
                                                <iais:field value="Application Type"/>
                                                <iais:value width="18">
                                                    <iais:select name="application_type" options="appTypeOption" firstOption="Please Select" value="${SearchParam.filters['appType']}" ></iais:select>
                                                </iais:value>
                                            </iais:row>
                                            <iais:row>
                                                <iais:field value="Application Status"/>
                                                <iais:value width="18">
                                                    <iais:select name="application_status" options="appStatusOption" firstOption="Please Select" value="${SearchParam.filters['appStatus']}" ></iais:select>
                                                </iais:value>
                                            </iais:row>
                                            <iais:row>
                                                <iais:field value="Application Submitted Date From"/>
                                                <iais:value width="18">
                                                    <iais:datePicker id = "sub_date" name = "sub_date" value="${SearchParam.filters['subDate']}" ></iais:datePicker>
                                                </iais:value>
                                                <iais:field value="Application Submitted Date To"/>
                                                <iais:value width="18">
                                                    <iais:datePicker id = "to_date" name = "to_date" value="${SearchParam.filters['toDate']}"></iais:datePicker>
                                                </iais:value>
                                            </iais:row>
                                            <iais:row>
                                                <iais:field value="Licence No."/>
                                                <iais:value width="18">
                                                    <label>
                                                        <input type="text" style="width:180%; font-weight:normal;" name="licence_no" value="${SearchParam.filters['licence_no']}" />
                                                    </label>
                                                </iais:value>
                                            </iais:row>
                                            <iais:row>
                                                <iais:field value="UEN No."/>
                                                <iais:value width="18">
                                                    <label>
                                                        <input type="text" style="width:180%; font-weight:normal;" name="uen_no" value="${SearchParam.filters['uen_no']}" />
                                                    </label>
                                                </iais:value>
                                            </iais:row>
                                            <iais:row>
                                                <iais:field value="Service Licence:"/>
                                                <iais:value width="18">
                                                    <iais:select name="service_licence_type" options="licSvcTypeOption" firstOption="Please Select" value="${serviceLicenceType}" ></iais:select>
                                                </iais:value>
                                            </iais:row>
                                            <iais:row>
                                                <iais:field value="Service Sub-Type:"/>
                                                <iais:value width="18">
                                                    <iais:select name="service_sub_type" options="licSvcSubTypeOption" firstOption="Please Select" value="${SearchParam.filters['serviceSubTypeName']}" ></iais:select>
                                                </iais:value>
                                            </iais:row>
                                            <iais:row>
                                                <iais:field value="Licence Status:"/>
                                                <iais:value width="18">
                                                    <iais:select name="licence_status" options="licStatusOption" firstOption="Please Select" value="${SearchParam.filters['licence_status']}" ></iais:select>
                                                </iais:value>
                                            </iais:row>
                                            <iais:row>
                                                <iais:field value="Licence Start Date From"/>
                                                <iais:value width="18">
                                                    <iais:datePicker id = "start_date" name = "start_date" value="${SearchParam.filters['start_date']}" ></iais:datePicker>
                                                </iais:value>
                                                <iais:field value="Licence Start Date To"/>
                                                <iais:value width="18">
                                                    <iais:datePicker id = "start_to_date" name = "start_to_date" value="${SearchParam.filters['start_to_date']}"></iais:datePicker>
                                                </iais:value>
                                            </iais:row>
                                            <iais:row>
                                                <iais:field value="Licence Expiry Date From"/>
                                                <iais:value width="18">
                                                    <iais:datePicker id = "expiry_start_date" name = "expiry_start_date" value="${SearchParam.filters['expiry_start_date']}" ></iais:datePicker>
                                                </iais:value>

                                                <iais:field value="Licence Expiry Date To"/>
                                                <iais:value width="18">
                                                    <iais:datePicker id = "expiry_date" name = "expiry_date" value="${SearchParam.filters['expiry_date']}"></iais:datePicker>
                                                </iais:value>
                                            </iais:row>
                                        </iais:section>
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

                            <div class="panel-collapse collapse in" id="collapseTwo" role="tabpanel" aria-labelledby="headingOne" aria-expanded="true" style="">
                                <div class="panel-body">
                                    <div class="panel-main-content">
                                        <iais:section title="" id = "supPoolList">
                                            <iais:row>
                                                <iais:field value="HCI Code:"/>
                                                <iais:value width="18">
                                                    <label>
                                                        <input type="text" style="width:180%; font-weight:normal;" name="hci_code" value="${SearchParam.filters['hciCode']}" />
                                                    </label>
                                                </iais:value>
                                            </iais:row>
                                            <iais:row>
                                                <iais:field value="HCI Name:"/>
                                                <iais:value width="18">
                                                    <label>
                                                        <input type="text" style="width:180%; font-weight:normal;" name="hci_name" value="${SearchParam.filters['hciName']}" />
                                                    </label>
                                                </iais:value>
                                            </iais:row>
                                            <iais:row>
                                                <iais:field value="HCI Street Name:"/>
                                                <iais:value width="18">
                                                    <label>
                                                        <input type="text" style="width:180%; font-weight:normal;" name="hci_street_name" value="${SearchParam.filters['hciStreetName']}" />
                                                    </label>
                                                </iais:value>
                                            </iais:row>
                                            <iais:row>
                                                <iais:field value="HCI Postal Code:"/>
                                                <iais:value width="18">
                                                    <label>
                                                        <input type="text" style="width:180%; font-weight:normal;" name="hci_postal_code" value="${SearchParam.filters['hciPostalCode']}" />
                                                    </label>
                                                </iais:value>
                                            </iais:row>

                                        </iais:section>
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

                            <div class="panel-collapse collapse in" id="collapseThree" role="tabpanel" aria-labelledby="headingOne" aria-expanded="true" style="">
                                <div class="panel-body">
                                    <div class="panel-main-content">
                                        <iais:section title="" id = "supPoolList">
                                            <iais:row>
                                                <iais:field value="Licensee ID:"/>
                                                <iais:value width="18">
                                                    <label>
                                                        <input type="text" style="width:180%; font-weight:normal;" name="licensee_id" value="${SearchParam.filters['licenseeId']}" />
                                                    </label>
                                                </iais:value>
                                            </iais:row>
                                            <iais:row>
                                                <iais:field value="Licensee Name:"/>
                                                <iais:value width="18">
                                                    <label>
                                                        <input type="text" style="width:180%; font-weight:normal;" name="licensee_name" value="${SearchParam.filters['licenseeName']}" />
                                                    </label>
                                                </iais:value>
                                            </iais:row>
                                            <iais:row>
                                                <iais:field value="Licensee Professional Regn No."/>
                                                <iais:value width="18">
                                                    <label>
                                                        <input type="text" style="width:180%; font-weight:normal;" name="licensee_regn_no" value="${SearchParam.filters['licenseeRegnNo']}" />
                                                    </label>
                                                </iais:value>
                                            </iais:row>
                                        </iais:section>
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

                            <div class="panel-collapse collapse in" id="collapseFour" role="tabpanel" aria-labelledby="headingOne" aria-expanded="true" style="">
                                <div class="panel-body">
                                    <div class="panel-main-content">
                                        <iais:section title="" id = "supPoolList">
                                            <iais:row>
                                                <iais:field value="Service Personnal ID:"/>
                                                <iais:value width="18">
                                                    <label>
                                                        <input type="text" style="width:180%; font-weight:normal;" name="service_id" value="${SearchParam.filters['serviceId']}" />
                                                    </label>
                                                </iais:value>
                                            </iais:row>
                                            <iais:row>
                                                <iais:field value="Service Personnel Name:"/>
                                                <iais:value width="18">
                                                    <label>
                                                        <input type="text" style="width:180%; font-weight:normal;" name="service_name" value="${SearchParam.filters['serviceName']}" />
                                                    </label>
                                                </iais:value>
                                            </iais:row>
                                            <iais:row>
                                                <iais:field value="Service Professional Regn No."/>
                                                <iais:value width="18">
                                                    <label>
                                                        <input type="text" style="width:180%; font-weight:normal;" name="service_regn_no" value="${SearchParam.filters['serviceRegnNo']}" />
                                                    </label>
                                                </iais:value>
                                            </iais:row>
                                            <iais:row>
                                                <iais:field value="Service Professional Role:"/>
                                                <iais:value width="18">
                                                    <label>
                                                        <input type="text" style="width:180%; font-weight:normal;" name="service_role" value="${SearchParam.filters['serviceRole']}" />
                                                    </label>
                                                </iais:value>
                                            </iais:row>
                                        </iais:section>
                                    </div>
                                </div>
                            </div>

                    </c:if>
                    <iais:action style="text-align:right;">
                        <button class="btn btn-primary" type="button"  onclick="javascript:doLicSearch()">Search</button>
                        <button class="btn btn-primary" type="button"  onclick="javascript:doLicBack()">Back</button>
                        <button class="btn btn-secondary" type="button"  onclick="javascript:doLicClear()">Clear</button>
                    </iais:action>
                </div>
            </div>
        </div>
        <div class="container">
            <div class="col-xs-15">
                <div class="components">

                    <iais:pagination  param="SearchParam" result="SearchResult"/>
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
                                <iais:sortableHeader needSort="false"  field="HCI_ADDRESS" value="HCI Address"></iais:sortableHeader>
                                <iais:sortableHeader needSort="false"  field="LICENSEE_NAME" value="Licensee Name"></iais:sortableHeader>
                                <iais:sortableHeader needSort="false"  field="SERVICE_NAME" value="Service Name"></iais:sortableHeader>
                                <iais:sortableHeader needSort="false"  field="Licence_Period" value="Licence Period"></iais:sortableHeader>
                                <iais:sortableHeader needSort="false"  field="Licence_Status" value="Licence Status"></iais:sortableHeader>
                                <iais:sortableHeader needSort="false"  field="2nd_last_compliance_history" value="2nd Last Compliance History"></iais:sortableHeader>
                                <iais:sortableHeader needSort="false"  field="last_compliance_history" value="Last Compliance History"></iais:sortableHeader>                                <iais:sortableHeader needSort="false"  field="current_risk_tagging" value="Current Risk Tagging"></iais:sortableHeader>
                                <iais:sortableHeader needSort="false" field="" value="Action"></iais:sortableHeader>
                            </tr>
                            </thead>
                            <tbody>
                            <c:choose>
                                <c:when test="${empty SearchResult.rows}">
                                    <tr>
                                        <td colspan="14">
                                            <iais:message key="ACK018" escape="true"></iais:message>
                                        </td>
                                    </tr>
                                </c:when>
                                <c:otherwise>
                                    <c:forEach var="pool" items="${SearchResult.rows}" varStatus="status">
                                        <tr>
                                            <td class="row_no"><c:out value="${status.index + 1+ (SearchParam.pageNo - 1) * SearchParam.pageSize}"/></td>
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
                                            <td><iais:service value="${pool.serviceName}"></iais:service></td>
                                            <td><fmt:formatDate value="${pool.startDate}" pattern="dd/MM/yyyy" />-<fmt:formatDate value="${pool.expiryDate}" pattern="dd/MM/yyyy" /></td>
                                            <td><c:out value="${pool.licenceStatus}"/></td>
                                            <td><c:out value="${pool.twoLastComplianceHistory}"/></td>
                                            <td><c:out value="${pool.lastComplianceHistory}"/></td>
                                            <td><c:out value="${pool.currentRiskTagging}"/></td>
                                            <td>
                                                <c:if test="${pool.isCessation==1}">
                                                    <iais:action style="text-align:center;">
                                                        <a onclick="javascript:doCessation('${pool.licenceId}');" >Cessation</a>
                                                    </iais:action>
                                                </c:if>
                                                <c:if test="${pool.isCessation==0}">
                                                    <iais:action style="text-align:center;">
                                                        <a data-toggle="modal" data-target="#editUser"  >Cessation</a>
                                                    </iais:action>
                                                </c:if>
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
    </iais:body>


</form>
<script type="text/javascript">
    function jumpToPagechangePage(){
        doLicSearch()
    }
    function doLicSearch(){
        showWaiting();SOP.Crud.cfxSubmit("mainForm", "search");
    }
    function doLicBack(){
        showWaiting();SOP.Crud.cfxSubmit("mainForm", "back");
    }
    function doLicClear(){
        $('input[type="text"]').val("");
        $("#service_licence_type option[text = 'Please Select']").val("selected", "selected");
        $("#service_sub_type option[text = 'Please Select']").val("selected", "selected");
        $("#licence_status option[text = 'Please Select']").val("selected", "selected");
        $("#application_type option[text = 'Please Select']").val("selected", "selected");
        $("#application_status option[text = 'Please Select']").val("selected", "selected");
        $(".current").text("Please Select");
        $('input[name="to_date"]').val("");
        $('input[name="sub_date"]').val("");
        $('input[name="start_date"]').val("");
        $('input[name="start_to_date"]').val("");
        $('input[name="expiry_start_date"]').val("");
        $('input[name="expiry_date"]').val("");
    }

    function doLicInfo(licenceId) {
        showWaiting();

        SOP.Crud.cfxSubmit("mainForm", "details",licenceId);

    }
    function doAppInfo(appCorrId) {
        showWaiting();

        SOP.Crud.cfxSubmit("mainForm", "appDetails",appCorrId);
    }
    function doCessation(licId) {
        showWaiting();
        SOP.Crud.cfxSubmit("mainForm", "cessation",licId);
    }
</script>