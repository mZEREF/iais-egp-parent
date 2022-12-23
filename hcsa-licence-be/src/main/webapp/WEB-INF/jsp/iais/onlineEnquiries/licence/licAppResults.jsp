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
                    <div class="row form-horizontal">
                        <div class="bg-title col-xs-12 col-md-12">
                            <h2>Online Enquiry</h2>
                        </div>
                        <div class="bg-title col-xs-12 col-md-12">
                            One search filter must be entered to proceed with search.
                        </div>

                        <div class="col-xs-12 col-md-12">

                            <iais:row>
                                <iais:field width="4" value="Licence No."/>
                                <iais:value width="4" cssClass="col-md-4">
                                    <input type="text" maxlength="24" id="licenceNo" name="licenceNo"
                                           value="${mainEnquiryFilterDto.licenceNo}">
                                </iais:value>
                            </iais:row>
                            <iais:row>
                                <iais:field width="4" value="Licence Status"/>
                                <iais:value width="4" cssClass="col-md-4">
                                    <iais:select name="licenceStatus" codeCategory="CATE_ID_LICENCE_STATUS"
                                                 cssClass="clearSel"  firstOption="Please Select"
                                                 value="${mainEnquiryFilterDto.licenceStatus}"/>
                                </iais:value>
                            </iais:row>
                            <iais:row>
                                <iais:field width="4" value="Application No."/>
                                <iais:value width="4" cssClass="col-md-4">
                                    <input type="text" maxlength="20" id="applicationNo" name="applicationNo"
                                           value="${mainEnquiryFilterDto.applicationNo}">
                                </iais:value>
                            </iais:row>
                            <iais:row>
                                <iais:field width="4" value="Business Name"/>
                                <iais:value width="4" cssClass="col-md-4">
                                    <input type="text" maxlength="100" id="businessName" name="businessName"
                                           value="${mainEnquiryFilterDto.businessName}">
                                </iais:value>
                            </iais:row>
                            <iais:row>
                                <iais:field width="4" value="Licensee ID No."/>
                                <iais:value width="4" cssClass="col-md-4">
                                    <input type="text" maxlength="20" id="licenseeIdNo" name="licenseeIdNo"
                                           value="${mainEnquiryFilterDto.licenseeIdNo}">
                                </iais:value>
                            </iais:row>
                            <iais:row>
                                <iais:field width="4" value="Licensee Name"/>
                                <iais:value width="4" cssClass="col-md-4">
                                    <input type="text" maxlength="100" id="licenseeName" name="licenseeName"
                                           value="${mainEnquiryFilterDto.licenseeName}">
                                </iais:value>
                            </iais:row>
                            <iais:row>
                                <iais:field width="4" value="Last Inspection Date From"/>
                                <iais:value width="4" cssClass="col-md-4">
                                    <iais:datePicker id="inspectionDateFrom" name="inspectionDateFrom"
                                                     dateVal="${mainEnquiryFilterDto.inspectionDateFrom}"/>
                                </iais:value>
                                <label class="col-xs-1 col-md-1 control-label">To&nbsp;</label>
                                <iais:value width="3" cssClass="col-md-3">
                                    <iais:datePicker id="inspectionDateTo" name="inspectionDateTo"
                                                     dateVal="${mainEnquiryFilterDto.inspectionDateTo}"/>
                                </iais:value>
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
                                                         field="APP_LICENSEE_NAME"
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
                                                         field="ADDRESS"
                                                         value="MOSD Address"/>
                                    <iais:sortableHeader needSort="true"
                                                         style="white-space: nowrap;padding: 15px 25px 15px 0px;"
                                                         field="APP_PREM_TYPE"
                                                         value="MOSD Type"/>
                                    <iais:sortableHeader needSort="true"
                                                         style="white-space: nowrap;padding: 15px 25px 15px 0px;"
                                                         field="app_licensee_id_no"
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
                                                    <a href="#"
                                                       onclick="licDetailsView('${MaskUtil.maskValue('licenceId', main.licenceId)}')">${main.licenceNo}</a>
                                                </td>
                                                <td style="vertical-align:middle;">
                                                    <p class="visible-xs visible-sm table-row-title">Application No.</p>
                                                    <a href="#"
                                                       onclick="appDetailsView('${MaskUtil.maskValue('appId', main.appId)}')"
                                                    >${main.applicationNo}</a>
                                                </td>
                                                <td style="vertical-align:middle;">
                                                    <p class="visible-xs visible-sm table-row-title">Business
                                                        Name</p>
                                                    <c:out value="${main.appBusinessName}"/>
                                                </td>
                                                <td style="vertical-align:middle;">
                                                    <p class="visible-xs visible-sm table-row-title">Service
                                                        Name</p>
                                                    <c:out value="${main.appServiceName}"/>
                                                </td>
                                                <td style="vertical-align:middle;">
                                                    <p class="visible-xs visible-sm table-row-title">Licence
                                                        Period</p>
                                                    <c:out value="${main.licencePeriodStr}"/>
                                                </td>
                                                <td style="vertical-align:middle;">
                                                    <p class="visible-xs visible-sm table-row-title">Licensee
                                                        Name</p>
                                                    <c:out value="${main.appLicenseeIdName}"/>
                                                </td>
                                                <td style="vertical-align:middle;">
                                                    <p class="visible-xs visible-sm table-row-title">Application Type</p>
                                                    <iais:code code="${main.applicationType}"/>
                                                </td>
                                                <td style="vertical-align:middle;">
                                                    <p class="visible-xs visible-sm table-row-title">Application Status</p>
                                                    <iais:code code="${main.applicationStatus}"/>
                                                </td>
                                                <td style="vertical-align:middle;">
                                                    <p class="visible-xs visible-sm table-row-title">Licence
                                                        Status</p>
                                                    <iais:code code="${main.licenceStatus}"/>
                                                </td>
                                                <td style="vertical-align:middle;">
                                                    <p class="visible-xs visible-sm table-row-title">MOSD
                                                        Address</p>
                                                    <c:out value="${main.appAddress}"/>
                                                </td>

                                                <td style="vertical-align:middle;">
                                                    <p class="visible-xs visible-sm table-row-title">MOSD Type</p>
                                                    <c:out value="${main.appPremType}"/>
                                                </td>

                                                <td style="vertical-align:middle;">
                                                    <p class="visible-xs visible-sm table-row-title">Licensee ID
                                                        No.</p>
                                                    <a href="#"
                                                       onclick="licDetailsView('${MaskUtil.maskValue('licenseeId', main.licLicenseeId)}')"><c:out value="${main.appLicenseeIdNo}"/></a>

                                                </td>

                                                <td style="vertical-align:middle;">
                                                    <p class="visible-xs visible-sm table-row-title">Last Inspection
                                                        Date</p>
                                                    <c:out value="${main.lastInspectionDateStr}"/>
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
                    </div>
                </div>
            </div>
        </div>
    </form>
</div>
<%@include file="/WEB-INF/jsp/include/utils.jsp" %>
<script>
    function doClear() {
        $('input[type="text"]').val("");
        $('input[type="checkbox"]').prop("checked", false);
        $("select option").prop("selected", false);
        $(".clearSel").children(".current").text("Please Select");

    }


    function jumpToPagechangePage() {
        search();
    }

    function doSearch() {
        $('input[name="pageJumpNoTextchangePage"]').val(1);
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

