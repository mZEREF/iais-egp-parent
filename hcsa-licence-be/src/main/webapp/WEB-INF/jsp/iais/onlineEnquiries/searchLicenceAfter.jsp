<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.utils.MiscUtil" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.utils.MaskUtil" %>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.AppConsts" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-intranet"/>
<form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
    <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
    <input type="hidden" name="crud_action_type" value=""/>
    <input type="hidden" name="crud_action_value" value=""/>
    <input type="hidden" name="crud_action_additional" value=""/>
    <iais:body>
        <div class="container">
            <div class="col-xs-15">
                <div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">
                    <br><br><br><br><br><br>
                    <h3>
                        <span>Advanced Search Criteria</span>
                    </h3>
                    <div class="row">
                        <div class="col-xs-10 col-md-12">
                            <div class="components">
                                <a class="btn btn-secondary" data-toggle="collapse"
                                   data-target="#searchCondition">Filter</a>
                            </div>
                        </div>
                    </div>

                    <div id="searchCondition" class="collapse">
                        <c:if test="${choose[2]==3||choose[1]==2}">
                            <h4>
                        <span>
                            Search By Application
                            <b class="form-check">&nbsp;
                                <c:choose>
                                    <c:when test="${choose[2]==3||choose[1]==2}">
                                    <input class="form-check-input licenceCheck" id="applicationChk" type="checkbox"
                                           checked name="applicationChk">
                                        <label class="form-check-label" for="applicationChk">
                                            <span class="check-square"></span>
                                        </label>
                                    </c:when>
                                    <c:otherwise><input class="form-check-input licenceCheck" id="applicationChk"
                                                        type="checkbox" name="applicationChk">
                                        <label class="form-check-label" for="applicationChk">
                                        <span class="check-square"></span>
                                    </label>
                                    </c:otherwise>
                                </c:choose>
                            </b>
                        </span>
                            </h4>

                            <div class="panel-collapse collapse in" id="collapseOne" role="tabpanel"
                                 aria-labelledby="headingOne" aria-expanded="true" style="">
                                <div class="panel-body">
                                    <div class="panel-main-content">
                                        <iais:section title="" id="supPoolList">
                                        <iais:row>
                                            <iais:field value="Application No."/>
                                            <iais:value width="18">
                                                <label>
                                                    <input type="text" maxlength="20" style="width:180%; font-weight:normal;"
                                                           name="application_no" value="${SearchParam.filters['appNo']}"/>
                                                </label>
                                            </iais:value>
                                        </iais:row>
                                        <iais:row>
                                            <iais:field value="Application Type"/>
                                            <iais:value width="18">
                                                <div id="application_type">
                                                    <iais:select name="application_type" options="appTypeOption"
                                                                 firstOption="Please Select"
                                                                 value="${SearchParam.filters['appType']}"></iais:select>
                                                </div>
                                            </iais:value>
                                        </iais:row>
                                        <iais:row>
                                            <iais:field value="Application Status"/>
                                            <iais:value width="18">
                                                <div id="application_status">
                                                    <iais:select name="application_status" options="appStatusOption"
                                                                 firstOption="Please Select"
                                                                 value="${SearchParam.filters['appStatus']}"></iais:select>
                                                </div>
                                            </iais:value>
                                        </iais:row>
                                        <iais:row>
                                            <iais:field value="Application Submitted Date From"/>
                                            <iais:value width="18">
                                                <iais:datePicker id="sub_date" name="sub_date"
                                                                 value="${SearchParam.filters['subDate']}"></iais:datePicker>
                                            </iais:value>
                                        </iais:row>
                                        <iais:row>
                                            <iais:field value="Application Submitted Date To"/>
                                            <iais:value width="18">
                                                <iais:datePicker id="to_date" name="to_date"
                                                                 value="${SearchParam.filters['toDate']}"></iais:datePicker>
                                            </iais:value>
                                        </iais:row>
                                        <iais:row style="color:#ff0000; display: none" id="submittedDateError">
                                            <iais:field value=""/>
                                            <iais:value width="18">
                                                Application Submitted Date From cannot be later than Application Submitted Date To. </p>
                                            </iais:value>
                                        </iais:row>
                                        <iais:row>
                                            <iais:field value="Licence No."/>
                                            <iais:value width="18">
                                                <label>
                                                    <input type="text" maxlength="20" style="width:180%; font-weight:normal;"
                                                           name="licence_no" value="${SearchParam.filters['licence_no']}"/>
                                                </label>
                                            </iais:value>
                                        </iais:row>
                                        <iais:row>
                                            <iais:field value="UEN No."/>
                                            <iais:value width="18">
                                                <label>
                                                    <input type="text" maxlength="10" style="width:180%; font-weight:normal;"
                                                           name="uen_no" value="${SearchParam.filters['uen_no']}"/>
                                                </label>
                                            </iais:value>
                                        </iais:row>
                                        <iais:row>
                                            <iais:field value="Service Type"/>
                                            <iais:value width="18">
                                                <div id="service_licence_type">
                                                    <iais:select name="service_licence_type" options="licSvcTypeOption"
                                                                 firstOption="Please Select"
                                                                 value="${SearchParam.filters['svc_name']}"></iais:select>
                                                </div>
                                            </iais:value>
                                        </iais:row>
                                        <iais:row>
                                            <iais:field value="Service Sub-Type"/>
                                            <iais:value width="18">
                                                <div id="service_sub_type">
                                                    <iais:select name="service_sub_type" options="licSvcSubTypeOption"
                                                                 firstOption="Please Select"
                                                                 value="${SearchParam.filters['serviceSubTypeName']}"></iais:select>
                                                </div>
                                            </iais:value>
                                        </iais:row>
                                        <iais:row>
                                            <iais:field value="Licence Status"/>
                                            <iais:value width="18">
                                                <div id="licence_status">
                                                    <iais:select name="licence_status" options="licStatusOption"
                                                                 firstOption="Please Select"
                                                                 value="${SearchParam.filters['licence_status']}"></iais:select>
                                                </div>
                                            </iais:value>
                                        </iais:row>
                                        <iais:row>
                                            <iais:field value="Licence Start Date From"/>
                                            <iais:value width="18">
                                                <iais:datePicker id="start_date" name="start_date"
                                                                 value="${SearchParam.filters['start_date']}"></iais:datePicker>
                                            </iais:value>
                                        </iais:row>
                                        <iais:row>
                                            <iais:field value="Licence Start Date To"/>
                                            <iais:value width="18">
                                                <iais:datePicker id="start_to_date" name="start_to_date"
                                                                 value="${SearchParam.filters['start_to_date']}"></iais:datePicker>
                                            </iais:value>
                                        </iais:row>
                                        <iais:row style="color:#ff0000; display: none" id="startDateError">
                                            <iais:field value=""/>
                                            <iais:value width="18">
                                                Licence Start Date From cannot be later than Licence Start Date To. </p>
                                            </iais:value>
                                        </iais:row>
                                        <iais:row>
                                            <iais:field value="Licence Expiry Date From"/>
                                            <iais:value width="18">
                                                <iais:datePicker id="expiry_start_date" name="expiry_start_date"
                                                                 value="${SearchParam.filters['expiry_start_date']}"></iais:datePicker>
                                            </iais:value>
                                        </iais:row>
                                        <iais:row>
                                            <iais:field value="Licence Expiry Date To"/>
                                            <iais:value width="18">
                                                <iais:datePicker id="expiry_date" name="expiry_date"
                                                                 value="${SearchParam.filters['expiry_date']}"></iais:datePicker>
                                            </iais:value>
                                        </iais:row>
                                        <iais:row style="color:#ff0000; display: none" id="expiryDateError">
                                        <iais:field value=""/>
                                        <iais:value width="18">
                                        Licence Expiry Date From cannot be later than Licence Expiry Date To.
                                    </div>
                                    </iais:value>
                                    </iais:row>
                                    </iais:section>
                                </div>
                            </div>
                        </c:if>
                        <c:if test="${choose[0]==1}">
                            <h4>
                            <span>Search By HCI
                                <strong class="form-check">&nbsp;
                                    <c:choose>
                                        <c:when test="${choose[0]==1}">
                                        <input class="form-check-input licenceCheck" id="hciChk" type="checkbox"
                                               name="hciChk" checked>
                                            <label class="form-check-label" for="hciChk">
                                            <span class="check-square"></span>
                                        </label>
                                        </c:when>
                                        <c:otherwise>
                                            <input class="form-check-input licenceCheck" id="hciChk" type="checkbox"
                                                   name="hciChk">
                                            <label class="form-check-label" for="hciChk">
                                            <span class="check-square"></span>
                                        </label>
                                        </c:otherwise>
                                    </c:choose>
                                </strong>
                            </span>
                            </h4>

                            <div class="panel-collapse collapse in" id="collapseTwo" role="tabpanel"
                                 aria-labelledby="headingOne" aria-expanded="true" style="">
                                <div class="panel-body">
                                    <div class="panel-main-content">
                                        <iais:section title="" id="supPoolList">
                                            <iais:row>
                                                <iais:field value="HCI Code"/>
                                                <iais:value width="18">
                                                    <label>
                                                        <input type="text" maxlength="7" style="width:180%; font-weight:normal;"
                                                               name="hci_code" value="${SearchParam.filters['hciCode']}"/>
                                                    </label>
                                                </iais:value>
                                            </iais:row>
                                            <iais:row>
                                                <iais:field value="HCI Name"/>
                                                <iais:value width="18">
                                                    <label>
                                                        <input type="text" maxlength="100"
                                                               style="width:180%; font-weight:normal;" name="hci_name"
                                                               value="${SearchParam.filters['hciName']}"/>
                                                    </label>
                                                </iais:value>
                                            </iais:row>
                                            <iais:row>
                                                <iais:field value="HCI Street Name"/>
                                                <iais:value width="18">
                                                    <label>
                                                        <input type="text" maxlength="32"
                                                               style="width:180%; font-weight:normal;" name="hci_street_name"
                                                               value="${SearchParam.filters['hciStreetName']}"/>
                                                    </label>
                                                </iais:value>
                                            </iais:row>
                                            <iais:row>
                                                <iais:field value="HCI Postal Code"/>
                                                <iais:value width="18">
                                                    <label>
                                                        <input type="text" maxlength="6" style="width:180%; font-weight:normal;"
                                                               name="hci_postal_code"
                                                               value="${SearchParam.filters['hciPostalCode']}"/>
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
                                <strong class="form-check">&nbsp;
                                    <c:choose>
                                        <c:when test="${choose[3]==4}">
                                            <input class="form-check-input licenceCheck" id="licenseeChk" type="checkbox"
                                                   checked name="licenseeChk">
                                            <label class="form-check-label" for="licenseeChk">
                                                <span
                                                        class="check-square"></span>
                                            </label></c:when>
                                        <c:otherwise>
                                            <input class="form-check-input licenceCheck" id="licenseeChk" type="checkbox"
                                                   name="licenseeChk">
                                            <label class="form-check-label" for="licenseeChk">
                                                <span class="check-square"></span>
                                            </label>
                                        </c:otherwise>
                                    </c:choose>
                                </strong>
                            </span>
                            </h4>

                            <div class="panel-collapse collapse in" id="collapseThree" role="tabpanel"
                                 aria-labelledby="headingOne" aria-expanded="true" style="">
                                <div class="panel-body">
                                    <div class="panel-main-content">
                                        <iais:section title="" id="supPoolList">
                                            <iais:row>
                                                <iais:field value="Licensee ID"/>
                                                <iais:value width="18">
                                                    <label>
                                                        <input type="text" maxlength="10" style="width:180%; font-weight:normal;" name="licensee_idNo" value="${SearchParam.filters['licenseeIdNo']}" />
                                                    </label>
                                                </iais:value>
                                            </iais:row>
                                            <iais:row>
                                                <iais:field value="Licensee Name"/>
                                                <iais:value width="18">
                                                    <label>
                                                        <input type="text" maxlength="50"
                                                               style="width:180%; font-weight:normal;" name="licensee_name"
                                                               value="${SearchParam.filters['licenseeName']}"/>
                                                    </label>
                                                </iais:value>
                                            </iais:row>
                                            <iais:row>
                                                <iais:field value="Licensee Professional Regn No."/>
                                                <iais:value width="18">
                                                    <label>
                                                        <input type="text" maxlength="20"
                                                               style="width:180%; font-weight:normal;" name="licensee_regn_no"
                                                               value="${SearchParam.filters['licenseeRegnNo']}"/>
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
                                <strong class="form-check">&nbsp;
                                    <c:choose>
                                        <c:when test="${choose[4]==5}">
                                            <input class="form-check-input licenceCheck" id="servicePersonnelChk"
                                                   type="checkbox" checked name="servicePersonnelChk">
                                            <label class="form-check-label" for="servicePersonnelChk">
                                            <span class="check-square"></span>
                                        </label>
                                        </c:when>
                                        <c:otherwise>
                                            <input class="form-check-input licenceCheck" id="servicePersonnelChk"
                                                   type="checkbox" name="servicePersonnelChk">
                                            <label class="form-check-label" for="servicePersonnelChk">
                                            <span class="check-square"></span>
                                        </label>
                                        </c:otherwise>
                                    </c:choose>
                                </strong>
                            </span>
                            </h4>

                            <div class="panel-collapse collapse in" id="collapseFour" role="tabpanel"
                                 aria-labelledby="headingOne" aria-expanded="true" style="">
                                <div class="panel-body">
                                    <div class="panel-main-content">
                                        <iais:section title="" id="supPoolList">
                                            <iais:row>
                                                <iais:field value="Service Personnal ID:"/>
                                                <iais:value width="18">
                                                    <label>
                                                        <input type="text" maxlength="10"
                                                               style="width:180%; font-weight:normal;" name="personnelId"
                                                               value="${SearchParam.filters['personnelId']}"/>
                                                    </label>
                                                </iais:value>
                                            </iais:row>
                                            <iais:row>
                                                <iais:field value="Service Personnel Name:"/>
                                                <iais:value width="18">
                                                    <label>
                                                        <input type="text" maxlength="50"
                                                               style="width:180%; font-weight:normal;" name="personnelName"
                                                               value="${SearchParam.filters['personnelName']}"/>
                                                    </label>
                                                </iais:value>
                                            </iais:row>
                                            <iais:row>
                                                <iais:field value="Service Personnel Role:"/>
                                                <iais:value width="18">
                                                    <div id="personnelRoleOption">
                                                        <iais:select name="personnelRole" options="servicePersonnelRoleOption"
                                                                     firstOption="Please Select"
                                                                     value="${SearchParam.filters['personnelRole']}"></iais:select>
                                                    </div>
                                                </iais:value>
                                            </iais:row>
                                            <iais:row>
                                                <iais:field value="Service Personnel Professional Regn No."/>
                                                <iais:value width="18">
                                                    <label>
                                                        <input type="text" maxlength="20"
                                                               style="width:180%; font-weight:normal;" name="personnelRegnNo"
                                                               value="${SearchParam.filters['personnelRegnNo']}"/>
                                                    </label>
                                                </iais:value>
                                            </iais:row>
                                        </iais:section>
                                    </div>
                                </div>
                            </div>
                        </c:if>
                        <br>
                        <br>
                        <iais:action style="text-align:left;">
                            <a onclick="javascript:doLicBack()">< Back</a>
                        </iais:action>
                        <iais:action style="text-align:right;">
                            <button class="btn btn-secondary" type="button" onclick="javascript:doLicClear()">Clear</button>
                            <button class="btn btn-primary" type="button" onclick="javascript:doLicSearch()">Search</button>
                        </iais:action>
                    </div>
                </div>
            </div>
        </div>
        <br>
        <div class="container">
            <div class="col-xs-15">
                <div class="components">

                    <iais:pagination param="SearchParam" result="SearchResult"/>
                    <div class="table-gp">
                        <table class="table">
                            <thead>
                            <tr align="center">
                                <th class="form-check">
                                    <c:if test="${!empty SearchResult.rows}">
                                        <input class="form-check-input licenceCheck" type="checkbox" name="userUids"
                                               id="checkboxAll" onchange="javascirpt:checkAll('${isASO}');"/>
                                        <label class="form-check-label" for="checkboxAll">
                                            <span class="check-square"></span>
                                        </label>
                                    </c:if>
                                </th>
                                <iais:sortableHeader needSort="false" field="" value="S/N"/>
                                <iais:sortableHeader needSort="false" field="APPLICATION_NO"
                                                     value="Application No."/>
                                <iais:sortableHeader needSort="false" field="APP_TYPE"
                                                     value="Application Type"/>
                                <iais:sortableHeader needSort="false" field="LICENCE_NO"
                                                     value="Licence No."/>
                                <iais:sortableHeader needSort="false" field="HCI_CODE"
                                                     value="HCI Code"/>
                                <iais:sortableHeader needSort="false" field="HCI_NAME"
                                                     value="HCI Name "/>
                                <iais:sortableHeader needSort="false" field="HCI_ADDRESS"
                                                     value="HCI Address"/>
                                <iais:sortableHeader needSort="false" field="LICENSEE_NAME"
                                                     value="Licensee Name"/>
                                <iais:sortableHeader needSort="false" field="SERVICE_NAME"
                                                     value="Service Name"/>
                                <iais:sortableHeader needSort="false" field="Licence_Period"
                                                     value="Licence Period"/>
                                <iais:sortableHeader needSort="false" field="Licence_Status"
                                                     value="Licence Status"/>
                                <iais:sortableHeader needSort="false" field="2nd_last_compliance_history"
                                                     value="2nd Last Compliance History"/>
                                <iais:sortableHeader needSort="false" field="last_compliance_history"
                                                     value="Last Compliance History"/>
                                <iais:sortableHeader needSort="false" field="current_risk_tagging"
                                                     value="Current Risk Tagging"/>
                            </tr>
                            </thead>
                            <tbody>
                            <c:choose>
                                <c:when test="${empty SearchResult.rows}">
                                    <tr>
                                        <td colspan="15">
                                            <iais:message key="ACK018" escape="true"></iais:message>
                                        </td>
                                    </tr>
                                </c:when>
                                <c:otherwise>
                                    <c:forEach var="pool" items="${SearchResult.rows}" varStatus="status">
                                        <tr>
                                            <td class="form-check" onclick="javascript:controlCease('${isASO}')">
                                                <c:if test="${pool.licenceStatus!='Lapsed'&&pool.licenceStatus!='Ceased'&&pool.licenceStatus!='Expired'}">
                                                    <input class="form-check-input licenceCheck" id="licence${status.index + 1}" type="checkbox"
                                                            name="appIds" value="${pool.appId}|${pool.isCessation}|${pool.licenceId}"   >
                                                    <label class="form-check-label" for="licence${status.index + 1}"><span
                                                            class="check-square"></span>
                                                    </label>
                                                </c:if>
                                            </td>
                                            <td class="row_no">
                                                <c:out value="${status.index + 1+ (SearchParam.pageNo - 1) * SearchParam.pageSize}"/>
                                            </td>
                                            <td>
                                                <c:if test="${pool.appCorrId==null}">${pool.applicationNo}</c:if>
                                                <c:if test="${pool.appCorrId!=null}"><a
                                                        onclick="javascript:doAppInfo('${MaskUtil.maskValue(IaisEGPConstant.CRUD_ACTION_VALUE,pool.appCorrId)}')">${pool.applicationNo}</a></c:if>
                                            </td>
                                            <td><c:out value="${pool.applicationType}"/></td>
                                            <td>
                                                <a onclick="javascript:doLicInfo('${MaskUtil.maskValue(IaisEGPConstant.CRUD_ACTION_VALUE,pool.licenceId)}')">${pool.licenceNo}</a>
                                            </td>
                                            <td><c:out value="${pool.hciCode}"/></td>
                                            <td><c:out value="${pool.hciName}"/></td>
                                            <td>
                                                <c:if test="${pool.licenceNo==null}">
                                                    <c:out value="${MiscUtil.getAddress(pool.blkNo,pool.streetName,pool.buildingName,pool.floorNo,pool.unitNo,'')}"/>
                                                </c:if>
                                                <c:if test="${pool.licenceNo!=null}">
                                                    <c:choose>
                                                        <c:when test="${pool.address.size() == 1}">
                                                            <c:out value="${pool.address[0]}"/>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <select>
                                                                <option value ="">Multiple</option>
                                                                <c:forEach items="${pool.address}" var="address" varStatus="index">
                                                                    <option value ="${address}">${address}</option>
                                                                </c:forEach>
                                                            </select>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </c:if>
                                            </td>
                                            <td><c:out value="${pool.licenseeName}"/></td>
                                            <td><iais:service value="${pool.serviceName}"></iais:service></td>
                                            <td><fmt:formatDate value="${pool.startDate}"
                                                                pattern="${AppConsts.DEFAULT_DATE_FORMAT}"/>-<fmt:formatDate
                                                    value="${pool.expiryDate}" pattern="${AppConsts.DEFAULT_DATE_FORMAT}"/></td>
                                            <td><c:out value="${pool.licenceStatus}"/></td>
                                            <td><c:out value="${pool.twoLastComplianceHistory}"/></td>
                                            <td><c:out value="${pool.lastComplianceHistory}"/></td>
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
    <div class="row" height="1" style="display: none" id="selectDecisionMsg">
        <div class="col-sm-9">
            <p style="color:#ff0000;">
                There is a pending application for this licence, please withdraw the application before proceeding with cessation
            </p>
        </div>
    </div>
    <iais:action style="text-align:right;">
        <a class="btn btn-secondary" onclick="$(this).attr('class', 'btn btn-secondary disabled')" href="${pageContext.request.contextPath}/officer-online-enquiries-information-file">Download</a>
        <button type="button" class="btn btn-primary ReqForInfoBtn" disabled
                onclick="javascript:doReqForInfo();">ReqForInfo</button>
        <c:if test="${cease==1}">
            <button type="button" class="btn btn-primary CeaseBtn" disabled
                    onclick="javascript:doCessation();">Cease</button>
        </c:if>
    </iais:action>


</form>
<%@include file="/WEB-INF/jsp/include/utils.jsp" %>
<script type="text/javascript">
    function controlCease(isAso) {
        var checkOne = false;
        var checkBox = $("input[name='appIds']");
        for (var i = 0; i < checkBox.length; i++) {
            if (checkBox[i].checked) {
                checkOne = true;
            }
        }
        if (checkOne) {
            $('.ReqForInfoBtn').prop('disabled',false);
        } else {
            $('.ReqForInfoBtn').prop('disabled',true);
        }
        if (checkOne&&isAso==="1") {
            $('.CeaseBtn').prop('disabled',false);
        } else {
            $('.CeaseBtn').prop('disabled',true);
        }
    }

    function jumpToPagechangePage() {
        licSearch()
    }

    function doLicSearch() {
        $('input[name="pageJumpNoTextchangePage"]').val(1);
        licSearch()
    }

    function checkAll(isAso) {
        if ($('#checkboxAll').is(':checked')) {
            $("input[name='appIds']").attr("checked", "true");
            $('.ReqForInfoBtn').prop('disabled',false);
            if(isAso==="1"){
                $('.CeaseBtn').prop('disabled',false);
            }
        } else {
            $("input[name='appIds']").removeAttr("checked");
            $('.CeaseBtn').prop('disabled',true);
            $('.ReqForInfoBtn').prop('disabled',true);
        }
    }

    function licSearch() {
        showWaiting();
        var startTo = $('#start_to_date').val();
        var startSub = $('#start_date').val();
        var expiryTo = $('#expiry_date').val();
        var expirySub = $('#expiry_start_date').val();
        var periodTo = $('#to_date').val();
        var periodSub = $('#sub_date').val();
        var flag = true;
        if (startSub > startTo && startTo != "") {
            $("#startDateError").show();
            flag = false;
        }
        if (expirySub > expiryTo && expiryTo != "") {
            $("#expiryDateError").show();
            flag = false;
        }
        if (periodSub > periodTo && periodTo != "") {
            $("#submittedDateError").show();
            flag = false;
        }
        if (flag) {
            SOP.Crud.cfxSubmit("mainForm", "search");
        } else {
            dismissWaiting();
        }
    }

    function doLicBack() {
        showWaiting();
        SOP.Crud.cfxSubmit("mainForm", "back");
    }

    function doLicClear() {
        $('input[type="text"]').val("");
        $("#service_licence_type option:first").prop("selected", 'selected');
        $("#service_sub_type option:first").prop("selected", 'selected');
        $("#licence_status option:first").prop("selected", 'selected');
        $("#application_type option:first").prop("selected", 'selected');
        $("#application_status option:first").prop("selected", 'selected');
        $("#personnelRoleOption option:first").prop("selected", 'selected');
        $("#service_licence_type .current").text("Please Select");
        $("#service_sub_type .current").text("Please Select");
        $("#licence_status .current").text("Please Select");
        $("#application_type .current").text("Please Select");
        $("#application_status .current").text("Please Select");
        $("#personnelRoleOption .current").text("Please Select");
        $('input[name="to_date"]').val("");
        $('input[name="sub_date"]').val("");
        $('input[name="start_date"]').val("");
        $('input[name="start_to_date"]').val("");
        $('input[name="expiry_start_date"]').val("");
        $('input[name="expiry_date"]').val("");
    }

    function doLicInfo(licenceId) {
        showWaiting();

        SOP.Crud.cfxSubmit("mainForm", "details", licenceId);

    }

    function doAppInfo(appCorrId) {
        showWaiting();

        SOP.Crud.cfxSubmit("mainForm", "appDetails", appCorrId);
    }

    function doCessation() {
        showWaiting();
        var chk = $("[name='appIds']:checked");
        var dropIds = new Array();
        chk.each(function () {
            dropIds.push($(this).val());
        });
        var flog = false;
        for (var i = 0; i < dropIds.length; i++) {
            var str = dropIds[i].split('|')[1];
            if (str == '1') {
                flog = true;
            }
        }
        if (flog) {
            SOP.Crud.cfxSubmit("mainForm", "cessation");
        } else {
            $("#selectDecisionMsg").show();
            dismissWaiting();
        }
    }

    function doReqForInfo() {
        showWaiting();
        var chk=$("[name='appIds']:checked");
        var dropIds = new Array();
        chk.each(function(){
            dropIds.push($(this).val());
        });
        var flog=false;
        for(var i=0;i<dropIds.length;i++){
            var str=dropIds[i].split('|')[1];
            if(str=='1'){
                flog=true;
            }
        }
        if(flog){
            SOP.Crud.cfxSubmit("mainForm", "reqForInfo");
        }
        else {
            dismissWaiting();
        }

    }
</script>