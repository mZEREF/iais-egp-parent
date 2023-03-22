<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.utils.MaskUtil" %>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.AppConsts" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<%
    String webrootCom = IaisEGPConstant.CSS_ROOT + IaisEGPConstant.COMMON_CSS_ROOT;
%>
<style>

    .form-horizontal p {
        line-height: 23px;
    }

    .hiddenRow {
        padding: 0px 0px !important;
        background-color: #f3f3f3;
    }
</style>
<webui:setLayout name="iais-intranet"/>
<div class="main-content dashboard">
    <form id="mainForm" method="post" action=<%=process.runtime.continueURL()%>>
        <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>

        <div class="col-lg-12 col-xs-12">
            <div class="center-content">
                <div class="intranet-content">
                    <div class="row form-horizontal normal-label">
                        <div class="col-xs-12 col-md-12">
                            <h2>Online Enquiry</h2>
                            <span>
                                One search filter must be entered to proceed with search.
                            </span>
                        </div>
                        <div class="row">&nbsp;
                            <div class="row">&nbsp;</div>
                            <div class="row">&nbsp;</div>
                        </div>
                        <div class="col-xs-12 col-md-12">

                            <iais:row>
                                <label class="col-xs-3 col-md-3 control-label">Licence No.</label>
                                <iais:value width="5" cssClass="col-md-5">
                                    <input type="text" maxlength="24" id="licenceNo" name="licenceNo"
                                           value="<c:out value="${mainEnquiryFilterDto.licenceNo}"/>">
                                </iais:value>
                            </iais:row>
                            <iais:row>
                                <label class="col-xs-3 col-md-3 control-label">Licence Status</label>
                                <iais:value width="5" cssClass="col-md-5">
                                    <iais:select name="licenceStatus" codeCategory="CATE_ID_LICENCE_STATUS"
                                                 cssClass="clearSel"  firstOption="All"
                                                 value="${mainEnquiryFilterDto.licenceStatus}"/>
                                </iais:value>
                            </iais:row>
                            <iais:row>
                                <label class="col-xs-3 col-md-3 control-label">Application No.</label>
                                <iais:value width="5" cssClass="col-md-5">
                                    <input type="text" maxlength="20" id="applicationNo" name="applicationNo"
                                           value="<c:out value="${mainEnquiryFilterDto.applicationNo}"/>">
                                </iais:value>
                            </iais:row>
                            <iais:row>
                                <label class="col-xs-3 col-md-3 control-label">Business Name</label>
                                <iais:value width="5" cssClass="col-md-5">
                                    <input type="text" maxlength="100" id="businessName" name="businessName"
                                           value="<c:out value="${mainEnquiryFilterDto.businessName}"/>">
                                </iais:value>
                            </iais:row>
                            <iais:row>
                                <label class="col-xs-3 col-md-3 control-label">Licensee ID No.</label>
                                <iais:value width="5" cssClass="col-md-5">
                                    <input type="text" maxlength="20" id="licenseeIdNo" name="licenseeIdNo"
                                           value="<c:out value="${mainEnquiryFilterDto.licenseeIdNo}"/>">
                                </iais:value>
                            </iais:row>
                            <iais:row>
                                <label class="col-xs-3 col-md-3 control-label">Licensee Name</label>
                                <iais:value width="5" cssClass="col-md-5">
                                    <input type="text" maxlength="100" id="licenseeName" name="licenseeName"
                                           value="<c:out value="${mainEnquiryFilterDto.licenseeName}"/>">
                                </iais:value>
                            </iais:row>
                            <iais:row>
                                <label class="col-xs-3 col-md-3 control-label">Last Inspection Date</label>
                                <iais:value width="2" cssClass="col-md-2">
                                    <iais:datePicker id="inspectionDateFrom" name="inspectionDateFrom"
                                                     dateVal="${mainEnquiryFilterDto.inspectionDateFrom}"/>
                                </iais:value>
                                <label class="col-xs-1 col-md-1 control-label" style="padding-left: 53px;">To&nbsp;</label>
                                <iais:value width="2" cssClass="col-md-2">
                                    <iais:datePicker id="inspectionDateTo" name="inspectionDateTo"
                                                     dateVal="${mainEnquiryFilterDto.inspectionDateTo}"/>
                                </iais:value>
                                <div class="col-md-8 col-xs-8 col-xs-offset-3 col-md-offset-3">
                                    <span class="error-msg " name="iaisErrorMsg" id="error_inspectionDate"></span>
                                </div>
                            </iais:row>

                            <iais:row>
                                <div class="col-xs-3 col-md-5 control-label">
                                    <span class="error-msg " name="iaisErrorMsg" id="error_checkAllFileds"></span>
                                </div>
                            </iais:row>


                            <div class="col-xs-12 col-md-12">
                                <iais:action style="text-align:right;">
                                    <button type="button" class="btn btn-secondary"
                                            onclick="javascript:doClear();">Clear
                                    </button>
                                    <button type="button" class="btn btn-primary"
                                            onclick="javascript:doSearch();">Search
                                    </button>
                                </iais:action>
                            </div>
                        </div>


                    </div>
                    <br>
                    <h3>
                        <span>Search Results</span>
                    </h3>
                    <div class="components">
                        <iais:pagination param="mainParam" result="mainResult"/>
                        <div class="table-gp">
                            <table aria-describedby="" class="table table-responsive"
                                   style="border-collapse:collapse;">
                                <thead>
                                <tr>
                                    <iais:sortableHeader needSort="false"
                                                         style="white-space: nowrap;padding: 15px 25px 15px 0px;"
                                                         field=""
                                                         value="S/N"/>
                                    <iais:sortableHeader needSort="true"
                                                         style="white-space: nowrap;padding: 15px 25px 15px 0px;"
                                                         field="LICENCE_NO"
                                                         value="Licence No."/>
                                    <iais:sortableHeader needSort="true"
                                                         style="white-space: nowrap;padding: 15px 25px 15px 0px;"
                                                         field="application_no"
                                                         value="Application No."/>
                                    <iais:sortableHeader needSort="true"
                                                         style="white-space: nowrap;padding: 15px 25px 15px 0px;"
                                                         field="APP_BUSINESS_NAME"
                                                         value="Business Name"/>
                                    <iais:sortableHeader needSort="true"
                                                         style="white-space: nowrap;padding: 15px 25px 15px 0px;"
                                                         field="SVC_NAME"
                                                         value="Service Name"/>
                                    <iais:sortableHeader needSort="true"
                                                         style="white-space: nowrap;padding: 15px 25px 15px 0px;"
                                                         field="START_DATE"
                                                         value="Licence Period"/>
                                    <iais:sortableHeader needSort="true"
                                                         style="white-space: nowrap;padding: 15px 25px 15px 0px;"
                                                         field="LIC_LICENSEE_NAME"
                                                         value="Licensee Name"/>
                                    <iais:sortableHeader needSort="true"
                                                         style="white-space: nowrap;padding: 15px 25px 15px 0px;"
                                                         field="app_type_desc"
                                                         value="Application Type"/>
                                    <iais:sortableHeader needSort="true"
                                                         style="white-space: nowrap;padding: 15px 25px 15px 0px;"
                                                         field="app_status_desc"
                                                         value="Application Status"/>
                                    <iais:sortableHeader needSort="true"
                                                         style="white-space: nowrap;padding: 15px 25px 15px 0px;"
                                                         field="lic_status_desc"
                                                         value="Licence Status"/>
                                    <iais:sortableHeader needSort="true"
                                                         style="white-space: nowrap;padding: 15px 25px 15px 0px;"
                                                         field="LIC_ADDRESS"
                                                         value="MOSD Address"/>
                                    <iais:sortableHeader needSort="true"
                                                         style="white-space: nowrap;padding: 15px 25px 15px 0px;"
                                                         field="LIC_PREM_TYPE"
                                                         value="MOSD Type"/>
                                    <iais:sortableHeader needSort="true"
                                                         style="white-space: nowrap;padding: 15px 25px 15px 0px;"
                                                         field="lic_licensee_id_no"
                                                         value="Licensee ID No."/>
                                    <iais:sortableHeader needSort="true"
                                                         style="white-space: nowrap;padding: 15px 25px 15px 0px;"
                                                         field="LAST_INSP_START_DATE"
                                                         value="Last Inspection Date"/>
                                </tr>
                                </thead>
                                <tbody class="form-horizontal">
                                <c:choose>
                                    <c:when test="${empty mainResult.rows}">
                                        <tr>
                                            <td colspan="15">
                                                <iais:message key="GENERAL_ACK018"
                                                              escape="true"/>
                                            </td>
                                        </tr>
                                    </c:when>
                                    <c:otherwise>
                                        <c:forEach var="main"
                                                   items="${mainResult.rows}"
                                                   varStatus="status">
                                            <tr>
                                                <td style="vertical-align:middle;">
                                                    <p class="visible-xs visible-sm table-row-title">S/N</p>
                                                    <c:out value="${status.index + 1+ (mainParam.pageNo - 1) * mainParam.pageSize}"/>
                                                </td>
                                                <td style="vertical-align:middle;">
                                                    <p class="visible-xs visible-sm table-row-title">Licence No.</p>
                                                    <c:choose>
                                                        <c:when test="${not empty main.licenceNo && main.licenceNo!='-'}">
                                                            <a href="#"
                                                               onclick="licDetailsView('${MaskUtil.maskValue('licenceId', main.licenceId)}')">${main.licenceNo}</a>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <c:out value="-"/>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </td>
                                                <td style="vertical-align:middle;">
                                                    <p class="visible-xs visible-sm table-row-title">Application No.</p>
                                                    <c:choose>
                                                        <c:when test="${not empty main.applicationNo && main.applicationNo!='-'}">
                                                            <a href="#"
                                                               onclick="appDetailsView('${MaskUtil.maskValue('appId', main.appId)}')">${main.applicationNo}</a>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <c:out value="-"/>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </td>
                                                <td style="vertical-align:middle;">
                                                    <p class="visible-xs visible-sm table-row-title">Business
                                                        Name</p>
                                                    <c:choose>
                                                        <c:when test="${not empty main.appBusinessName}">
                                                            <c:out value="${main.appBusinessName}"/>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <c:out value="-"/>
                                                        </c:otherwise>
                                                    </c:choose>

                                                </td>
                                                <td style="vertical-align:middle;">
                                                    <p class="visible-xs visible-sm table-row-title">Service
                                                        Name</p>
                                                    <c:choose>
                                                        <c:when test="${not empty main.appServiceName}">
                                                            <c:out value="${main.appServiceName}"/>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <c:out value="-"/>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </td>
                                                <td style="vertical-align:middle;">
                                                    <p class="visible-xs visible-sm table-row-title">Licence
                                                        Period</p>
                                                    <c:choose>
                                                        <c:when test="${not empty main.licencePeriodStr}">
                                                            <c:out value="${main.licencePeriodStr}"/>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <c:out value="-"/>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </td>
                                                <td style="vertical-align:middle;">
                                                    <p class="visible-xs visible-sm table-row-title">Licensee
                                                        Name</p>
                                                    <c:choose>
                                                        <c:when test="${not empty main.appLicenseeIdName}">
                                                            <c:out value="${main.appLicenseeIdName}"/>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <c:out value="-"/>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </td>
                                                <td style="vertical-align:middle;">
                                                    <p class="visible-xs visible-sm table-row-title">Application Type</p>
                                                    <c:choose>
                                                        <c:when test="${not empty main.applicationType}">
                                                            <iais:code code="${main.applicationType}"/>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <c:out value="-"/>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </td>
                                                <td style="vertical-align:middle;">
                                                    <p class="visible-xs visible-sm table-row-title">Application Status</p>
                                                    <c:choose>
                                                        <c:when test="${not empty main.applicationStatus}">
                                                            <iais:code code="${main.applicationStatus}"/>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <c:out value="-"/>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </td>
                                                <td style="vertical-align:middle;">
                                                    <p class="visible-xs visible-sm table-row-title">Licence
                                                        Status</p>
                                                    <c:choose>
                                                        <c:when test="${not empty main.licenceStatus}">
                                                            <iais:code code="${main.licenceStatus}"/>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <c:out value="-"/>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </td>
                                                <td style="vertical-align:middle;">
                                                    <p class="visible-xs visible-sm table-row-title">MOSD
                                                        Address</p>
                                                    <c:choose>
                                                        <c:when test="${not empty main.appAddress}">
                                                            <c:out value="${main.appAddress}"/>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <c:out value="-"/>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </td>

                                                <td style="vertical-align:middle;">
                                                    <p class="visible-xs visible-sm table-row-title">MOSD Type</p>
                                                    <c:choose>
                                                        <c:when test="${not empty main.appPremType}">
                                                            <c:out value="${main.appPremType}"/>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <c:out value="-"/>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </td>

                                                <td style="vertical-align:middle;">
                                                    <p class="visible-xs visible-sm table-row-title">Licensee ID
                                                        No.</p>
                                                    <c:choose>
                                                        <c:when test="${not empty main.appLicenseeIdNo}">
                                                            <a href="#"
                                                               onclick="licDetailsView('${MaskUtil.maskValue('licenseeId', main.licLicenseeId)}')"><c:out value="${main.appLicenseeIdNo}"/></a>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <c:out value="-"/>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </td>

                                                <td style="vertical-align:middle;">
                                                    <p class="visible-xs visible-sm table-row-title">Last Inspection
                                                        Date</p>
                                                    <c:if test="${main.lastInspectionDateStr =='-'}">-</c:if>
                                                    <c:if test="${main.lastInspectionDateStr !='-'}">
                                                        <a href="#"
                                                           onclick="appDetailsView('${MaskUtil.maskValue('appCorrId', main.appId)}')"
                                                        ><c:out value="${main.lastInspectionDateStr}"/></a>
                                                    </c:if>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </c:otherwise>
                                </c:choose>
                                </tbody>
                            </table>
                        </div>
                        <iais:action style="text-align:right;">
                            <a class="btn btn-secondary"
                               href="${pageContext.request.contextPath}/hcsa/enquiry/hcsa/Main-SearchResults-Download">Download</a>
                        </iais:action>
                        <input type="hidden" name="Search" value="0">
                    </div>
                </div>
            </div>
        </div>
    </form>
</div>
<%@include file="/WEB-INF/jsp/include/utils.jsp" %>
<%@ include file="/WEB-INF/jsp/include/validation.jsp" %>
<script>
    function doClear() {
        $('input[type="text"]').val("");
        $('input[type="checkbox"]').prop("checked", false);
        $("select option").prop("selected", false);
        $(".clearSel").children(".current").text("All");

    }


    function jumpToPagechangePage() {
        search();
    }

    function doSearch() {
        $('input[name="pageJumpNoTextchangePage"]').val(1);
        $('input[name="Search"]').val(1);
        search();
    }

    function search() {
        showWaiting();
        $("[name='crud_action_type']").val('searchMain');
        $('#mainForm').submit();
    }

    function sortRecords(sortFieldName, sortType) {
        $("[name='crud_action_value']").val(sortFieldName);
        $("[name='crud_action_additional']").val(sortType);
        $("[name='crud_action_type']").val('searchMain');
        $('#mainForm').submit();
    }


    var licDetailsView = function (submissionNo) {

        showWaiting();
        $("[name='crud_action_value']").val(submissionNo);
        $("[name='crud_action_type']").val('licInfo');
        $('#mainForm').submit();
    }
    var appDetailsView = function (submissionNo) {

        showWaiting();
        $("[name='crud_action_value']").val(submissionNo);
        $("[name='crud_action_type']").val('appInfo');
        $('#mainForm').submit();
    }
</script>

