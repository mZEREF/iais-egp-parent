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
                        <c:if test="${count=='3'||count=='2'}">
                            <h4>
                                <span>
                                    Search By Application&nbsp;
                                    <strong>&nbsp;
                                        <input  id="applicationChk" type="radio"
                                               checked name="searchChk"  value="2"/>
                                    </strong>
                                </span>
                            </h4>

                            <div class="panel-collapse collapse in" id="collapseOne" role="tabpanel"
                                 aria-labelledby="headingOne" aria-expanded="true" style="">
                                <div class="panel-body">
                                    <div class="panel-main-content">
                                        <iais:section title="" id="supPoolList">
                                        <iais:row>
                                            <iais:field value="Application No."/>
                                            <div class="col-sm-7 col-md-4 col-xs-10">
                                                
                                                    <input type="text" maxlength="20" style=" font-weight:normal;"
                                                           name="application_no" value="${SearchParam.filters['appNo']}"/>
                                                
                                            </div>
                                        </iais:row>
                                        <iais:row>
                                            <iais:field value="Application Type"/>
                                            <div class="col-sm-7 col-md-4 col-xs-10">
                                                <div id="application_type">
                                                    <iais:select name="application_type" options="appTypeOption"
                                                                 firstOption="Please Select"
                                                                 value="${SearchParam.filters['appType']}"></iais:select>
                                                </div>
                                            </div>
                                        </iais:row>
                                        <iais:row>
                                            <iais:field value="Application Status"/>
                                            <div class="col-sm-7 col-md-4 col-xs-10">
                                                <div id="application_status">
                                                    <iais:select name="application_status" options="appStatusOption"
                                                                 firstOption="Please Select"
                                                                 value="${SearchParam.filters['appStatus']}"></iais:select>
                                                </div>
                                            </div>
                                        </iais:row>
                                        <iais:row>
                                            <iais:field value="Application Submitted Date From"/>
                                            <div class="col-sm-7 col-md-4 col-xs-10">
                                                <iais:datePicker id="sub_date" name="sub_date"
                                                                 value="${SearchParam.filters['subDate']}"></iais:datePicker>
                                            </div>
                                        </iais:row>
                                        <iais:row>
                                            <iais:field value="Application Submitted Date To"/>
                                            <div class="col-sm-7 col-md-4 col-xs-10">
                                                <iais:datePicker id="to_date" name="to_date"
                                                                 value="${SearchParam.filters['toDate']}"></iais:datePicker>
                                                <span style="font-weight:normal;" id="error_to_date" name="iaisErrorMsg" class="error-msg" ></span>
                                            </div>
                                        </iais:row>

                                        <iais:row>
                                            <iais:field value="Licence No."/>
                                            <div class="col-sm-7 col-md-4 col-xs-10">
                                                
                                                    <input type="text" maxlength="24" style=" font-weight:normal;"
                                                           name="licence_no" value="${SearchParam.filters['licence_no']}"/>
                                                
                                            </div>
                                        </iais:row>
                                        <iais:row>
                                            <iais:field value="UEN No."/>
                                            <div class="col-sm-7 col-md-4 col-xs-10">
                                                
                                                    <input type="text" maxlength="10" style=" font-weight:normal;"
                                                           name="uen_no" value="${SearchParam.filters['uen_no']}"/>
                                                
                                            </div>
                                        </iais:row>
                                        <iais:row>
                                            <iais:field value="Service Type"/>
                                            <div class="col-sm-7 col-md-4 col-xs-10">
                                                <div id="service_licence_type">
                                                    <iais:select name="service_licence_type" options="licSvcTypeOption" firstOption="Please Select" value="${SearchParam.filters['svc_name']}"></iais:select>
                                                </div>
                                            </div>
                                        </iais:row>
                                        <iais:row>
                                            <iais:field value="Service Sub-Type"/>
                                            <div class="col-sm-7 col-md-4 col-xs-10">
                                                <div id="service_sub_type">
                                                    <iais:select name="service_sub_type" options="licSvcSubTypeOption" firstOption="Please Select" value="${SearchParam.filters['serviceSubTypeName']}"></iais:select>
                                                </div>
                                            </div>
                                        </iais:row>
                                        <iais:row>
                                            <iais:field value="Licence Status"/>
                                            <div class="col-sm-7 col-md-4 col-xs-10">
                                                <div id="licence_status">
                                                    <iais:select name="licence_status" options="licStatusOption"
                                                                 firstOption="Please Select"
                                                                 value="${SearchParam.filters['licence_status']}"></iais:select>
                                                </div>
                                            </div>
                                        </iais:row>
                                        <iais:row>
                                            <iais:field value="Licence Start Date From"/>
                                            <div class="col-sm-7 col-md-4 col-xs-10">
                                                <iais:datePicker id="start_date" name="start_date"
                                                                 value="${SearchParam.filters['start_date']}"></iais:datePicker>
                                            </div>
                                        </iais:row>
                                        <iais:row>
                                            <iais:field value="Licence Start Date To"/>
                                            <div class="col-sm-7 col-md-4 col-xs-10">
                                                <iais:datePicker id="start_to_date" name="start_to_date"
                                                                 value="${SearchParam.filters['start_to_date']}"></iais:datePicker>
                                                <span style="font-weight:normal;" id="error_start_to_date" name="iaisErrorMsg" class="error-msg" ></span>
                                            </div>
                                        </iais:row>

                                        <iais:row>
                                            <iais:field value="Licence Expiry Date From"/>
                                            <div class="col-sm-7 col-md-4 col-xs-10">
                                                <iais:datePicker id="expiry_start_date" name="expiry_start_date"
                                                                 value="${SearchParam.filters['expiry_start_date']}"></iais:datePicker>
                                            </div>
                                        </iais:row>
                                        <iais:row>
                                            <iais:field value="Licence Expiry Date To"/>
                                            <div class="col-sm-7 col-md-4 col-xs-10">
                                                <iais:datePicker id="expiry_date" name="expiry_date"
                                                                 value="${SearchParam.filters['expiry_date']}"></iais:datePicker>
                                                <span style="font-weight:normal;" id="error_expiry_date" name="iaisErrorMsg" class="error-msg" ></span>
                                            </div>
                                        </iais:row>
                                    </iais:section>
                                </div>
                            </div>
                            </div>
                        </c:if>
                        <c:if test="${count=='1'}">
                            <h4>
                            <span>Search By HCI&nbsp;
                                <strong >&nbsp;
                                    <input  id="hciChk" type="radio"
                                           checked name="searchChk"  value="1"/>
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
                                                <div class="col-sm-7 col-md-4 col-xs-10">
                                                    
                                                        <input type="text" maxlength="7" style=" font-weight:normal;"
                                                               name="hci_code" value="${SearchParam.filters['hciCode']}"/>
                                                    
                                                </div>
                                            </iais:row>
                                            <iais:row>
                                                <iais:field value="HCI Name"/>
                                                <div class="col-sm-7 col-md-4 col-xs-10">
                                                    
                                                        <input type="text" maxlength="100"
                                                               style=" font-weight:normal;" name="hci_name"
                                                               value="${SearchParam.filters['hciName']}"/>
                                                    
                                                </div>
                                            </iais:row>
                                            <iais:row>
                                                <iais:field value="HCI Street Name"/>
                                                <div class="col-sm-7 col-md-4 col-xs-10">
                                                    
                                                        <input type="text" maxlength="32"
                                                               style=" font-weight:normal;" name="hci_street_name"
                                                               value="${SearchParam.filters['hciStreetName']}"/>
                                                    
                                                </div>
                                            </iais:row>
                                            <iais:row>
                                                <iais:field value="HCI Postal Code"/>
                                                <div class="col-sm-7 col-md-4 col-xs-10">
                                                    
                                                        <input type="text" maxlength="6" style=" font-weight:normal;"
                                                               name="hci_postal_code"
                                                               value="${SearchParam.filters['hciPostalCode']}"/>
                                                    
                                                </div>
                                            </iais:row>

                                        </iais:section>
                                    </div>
                                </div>
                            </div>

                        </c:if>
                        <c:if test="${count=='4'}">
                            <h4>
                            <span>Search By Licensee&nbsp;
                                <strong >&nbsp;
                                    <input  id="licenseeChk" type="radio"
                                           checked name="searchChk"  value="4"/>
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
                                                <div class="col-sm-7 col-md-4 col-xs-10">
                                                    
                                                        <input type="text" maxlength="10" style=" font-weight:normal;" name="licensee_idNo" value="${SearchParam.filters['licenseeIdNo']}" />
                                                    
                                                </div>
                                            </iais:row>
                                            <iais:row>
                                                <iais:field value="Licensee Name"/>
                                                <div class="col-sm-7 col-md-4 col-xs-10">
                                                    
                                                        <input type="text" maxlength="50"
                                                               style=" font-weight:normal;" name="licensee_name"
                                                               value="${SearchParam.filters['licenseeName']}"/>
                                                    
                                                </div>
                                            </iais:row>
                                            <iais:row>
                                                <iais:field value="Licensee Professional Regn No."/>
                                                <div class="col-sm-7 col-md-4 col-xs-10">
                                                    
                                                        <input type="text" maxlength="20"
                                                               style=" font-weight:normal;" name="licensee_regn_no"
                                                               value="${SearchParam.filters['licenseeRegnNo']}"/>
                                                    
                                                </div>
                                            </iais:row>
                                        </iais:section>
                                    </div>
                                </div>
                            </div>
                        </c:if>
                        <c:if test="${count=='5'}">
                            <h4>
                            <span>Search By Service Personnel Details (CGO, PO, DPO & MedAlert)&nbsp;
                                <strong >&nbsp;
                                    <input  id="servicePersonnelChk"
                                           type="radio" checked name="searchChk"  value="5"/>
                                </strong>
                            </span>
                            </h4>

                            <div class="panel-collapse collapse in" id="collapseFour" role="tabpanel"
                                 aria-labelledby="headingOne" aria-expanded="true" style="">
                                <div class="panel-body">
                                    <div class="panel-main-content">
                                        <iais:section title="" id="supPoolList">
                                            <iais:row>
                                                <iais:field value="Service Personnel ID:"/>
                                                <div class="col-sm-7 col-md-4 col-xs-10">
                                                    
                                                        <input type="text" maxlength="10"
                                                               style=" font-weight:normal;" name="personnelId"
                                                               value="${SearchParam.filters['personnelId']}"/>
                                                    
                                                </div>
                                            </iais:row>
                                            <iais:row>
                                                <iais:field value="Service Personnel Name:"/>
                                                <div class="col-sm-7 col-md-4 col-xs-10">
                                                    
                                                        <input type="text" maxlength="50"
                                                               style=" font-weight:normal;" name="personnelName"
                                                               value="${SearchParam.filters['personnelName']}"/>
                                                    
                                                </div>
                                            </iais:row>
                                            <iais:row>
                                                <iais:field value="Service Personnel Role:"/>
                                                <div class="col-sm-7 col-md-4 col-xs-10">
                                                    <div id="personnelRoleOption">
                                                        <iais:select name="personnelRole" options="servicePersonnelRoleOption"
                                                                     firstOption="Please Select"
                                                                     value="${SearchParam.filters['personnelRole']}"></iais:select>
                                                    </div>
                                                </div>
                                            </iais:row>
                                            <iais:row>
                                                <iais:field value="Service Personnel Professional Regn No."/>
                                                <div class="col-sm-7 col-md-4 col-xs-10">
                                                    
                                                        <input type="text" maxlength="20"
                                                               style=" font-weight:normal;" name="personnelRegnNo"
                                                               value="${SearchParam.filters['personnelRegnNo']}"/>
                                                    
                                                </div>
                                            </iais:row>
                                        </iais:section>
                                    </div>
                                </div>
                            </div>
                        </c:if>
                        <br>
                        <br>
                        <iais:action style="text-align:left;">
                            <a onclick="javascript:doLicBack()"><em class="fa fa-angle-left"> </em> Back</a>
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
                                <c:if test="${cease==1}">
                                    <th class="form-check">
                                        <c:if test="${!empty SearchResult.rows}">
                                            <input class="form-check-input licenceCheck" type="checkbox" name="userUids"
                                                   id="checkboxAll" onchange="javascirpt:checkAll('${isASO}');"/>
                                            <label class="form-check-label" for="checkboxAll">
                                                <span class="check-square"></span>
                                            </label>
                                        </c:if>
                                    </th>
                                </c:if>
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
                                            <c:if test="${cease==1}">
                                                <td class="form-check" onclick="javascript:controlCease('${isASO}')">
                                                    <c:if test="${pool.licenceStatus!='Lapsed'&&pool.licenceStatus!='Ceased'&&pool.licenceStatus!='Expired'&&pool.licenceId!=null}">
                                                        <input class="form-check-input licenceCheck" id="licence${status.index + 1}" type="checkbox"
                                                               name="appIds" value="${pool.appId}|${pool.isCessation}|${pool.licenceId}|${pool.licenceStatus}"   >
                                                        <label class="form-check-label" for="licence${status.index + 1}"><span
                                                                class="check-square"></span>
                                                        </label>
                                                    </c:if>
                                                </td>
                                            </c:if>

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
                                                <c:if test="${pool.licenceId!=null}"><a onclick="javascript:doLicInfo('${MaskUtil.maskValue(IaisEGPConstant.CRUD_ACTION_VALUE,pool.licenceId)}')">${pool.licenceNo}</a></c:if>
                                                <c:if test="${pool.licenceId==null}">${pool.licenceNo}</c:if>
                                            </td>
                                            <td><c:out value="${pool.hciCode}"/><c:if test="${empty pool.hciCode}">-</c:if></td>
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
        <c:if test="${cease==1}">
            <button type="button" class="btn btn-primary ReqForInfoBtn" disabled
                    onclick="javascript:doReqForInfo();">ReqForInfo</button>
            <button type="button" class="btn btn-primary CeaseBtn" disabled
                    onclick="javascript:doCessation();">Cease</button>
        </c:if>
    </iais:action>
</form>
<%@include file="/WEB-INF/jsp/include/validation.jsp"%>
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
            var chk = $("[name='appIds']:checked");
            var dropIds = new Array();
            chk.each(function () {
                dropIds.push($(this).val());
            });
            if(dropIds.length!==0){
                $('.ReqForInfoBtn').prop('disabled',false);
                if(isAso==="1"){
                    $('.CeaseBtn').prop('disabled',false);
                }
            }

        } else {
            $("input[name='appIds']").removeAttr("checked");
            $('.CeaseBtn').prop('disabled',true);
            $('.ReqForInfoBtn').prop('disabled',true);
        }
    }

    function licSearch() {
        showWaiting();

        SOP.Crud.cfxSubmit("mainForm", "search");

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
            var str=dropIds[i].split('|')[3];
            if(str=='Active'){
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