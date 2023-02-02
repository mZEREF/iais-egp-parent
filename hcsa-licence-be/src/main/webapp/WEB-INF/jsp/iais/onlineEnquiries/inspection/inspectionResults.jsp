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
        <%@ include file="/WEB-INF/jsp/include/validation.jsp" %>

        <div class="col-lg-12 col-xs-12">
            <div class="center-content">
                <div class="intranet-content">
                    <div class="row form-horizontal">
                        <div class="col-xs-12 col-md-12">
                            <h2>Inspection Search</h2>
                            <span>
                                One search filter must be entered to proceed with search.
                            </span>
                        </div>
                        <div class="row">&nbsp;
                            <div class="row">&nbsp;</div>
                            <div class="row">&nbsp;</div>
                        </div>

                        <div class="form-group">
                            <div class="col-xs-12 col-md-12">
                                <div class="col-xs-12 col-md-12">
                                    <div class="components">
                                        <a class="btn btn-secondary filterButton" data-toggle="collapse" onclick="changeButtonName()"
                                           data-target="#searchCondition">More Filters</a>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div class="col-xs-12 col-md-12">

                            <iais:row>
                                <label class="col-xs-3 col-md-3 control-label">Application No.</label>
                                <iais:value width="5" cssClass="col-md-5">
                                    <input type="text" maxlength="20" id="applicationNo" name="applicationNo"
                                           value="${inspectionEnquiryFilterDto.applicationNo}">
                                </iais:value>
                            </iais:row>
                            <iais:row>
                                <label class="col-xs-3 col-md-3 control-label">Application Type</label>
                                <iais:value width="5" cssClass="col-md-5">
                                    <iais:select name="applicationType" codeCategory="CATE_ID_APP_TYPE"
                                                 firstOption="All"
                                                 cssClass="clearSel"   value="${inspectionEnquiryFilterDto.applicationType}"/>
                                </iais:value>
                            </iais:row>
                            <iais:row>
                                <label class="col-xs-3 col-md-3 control-label">Inspection Type</label>
                                <iais:value width="5" cssClass="col-md-5">
                                    <iais:select name="inspectionType" options="inspectionTypeOption"
                                                 firstOption="All" cssClass="clearSel"
                                                 value="${inspectionEnquiryFilterDto.inspectionType}"/>
                                </iais:value>
                            </iais:row>
                            <iais:row>
                                <label class="col-xs-3 col-md-3 control-label">Inspection Date</label>
                                <iais:value width="2" cssClass="col-md-2">
                                    <iais:datePicker id="inspectionDateFrom" name="inspectionDateFrom"
                                                     dateVal="${inspectionEnquiryFilterDto.inspectionDateFrom}"/>
                                </iais:value>
                                <label class="col-xs-1 col-md-1 control-label" style="text-align: center !important;">To&nbsp;</label>
                                <iais:value width="2" cssClass="col-md-2">
                                    <iais:datePicker id="inspectionDateTo" name="inspectionDateTo"
                                                     dateVal="${inspectionEnquiryFilterDto.inspectionDateTo}"/>
                                </iais:value>
                                <div class="col-md-8 col-xs-8 col-xs-offset-3 col-md-offset-3">
                                    <span class="error-msg " name="iaisErrorMsg" id="error_inspectionDate"></span>
                                </div>
                            </iais:row>


                            <div id="searchCondition" class="collapse">
                                <iais:row>
                                    <label class="col-xs-3 col-md-3 control-label">MOSD Type</label>
                                    <iais:value width="5" cssClass="col-md-5">
                                        <iais:select name="mosdType" id="mosdType" firstOption="All"
                                                     options="mosdTypeOption"
                                                     cssClass="clearSel" value="${inspectionEnquiryFilterDto.mosdType}"/>
                                    </iais:value>
                                </iais:row>
                                <iais:row>
                                    <label class="col-xs-3 col-md-3 control-label">Reason for Inspection</label>
                                    <iais:value width="5" cssClass="col-md-5">
                                        <iais:select name="inspectionReason" id="inspectionReason" firstOption="All"
                                                     options="inspectionReasonOption"
                                                     cssClass="clearSel" value="${inspectionEnquiryFilterDto.inspectionReason}"/>
                                    </iais:value>
                                </iais:row>
                                <iais:row>
                                    <label class="col-xs-3 col-md-3 control-label">Audit Type</label>
                                    <iais:value width="5" cssClass="col-md-5">
                                        <iais:select name="auditType" id="auditType" firstOption="All"
                                                     codeCategory="CATE_ID_AUDIT_TYPE"
                                                     cssClass="clearSel" value="${inspectionEnquiryFilterDto.auditType}"/>
                                    </iais:value>
                                </iais:row>

                                <iais:row>
                                    <label class="col-xs-3 col-md-3 control-label">Service Name</label>
                                    <iais:value width="5" cssClass="col-md-5">
                                        <iais:select name="serviceName" options="licSvcTypeOption"
                                                     firstOption="All"
                                                     cssClass="clearSel"  value="${inspectionEnquiryFilterDto.serviceName}"/>
                                    </iais:value>
                                </iais:row>
                                <iais:row>
                                    <label class="col-xs-3 col-md-3 control-label">Business Name</label>
                                    <iais:value width="5" cssClass="col-md-5">
                                        <input type="text" maxlength="100" id="businessName" name="businessName"
                                               value="${inspectionEnquiryFilterDto.businessName}">
                                    </iais:value>
                                </iais:row>
                            </div>
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
                        <h3>
                            <span>Search Results</span>
                        </h3>
                        <iais:pagination param="inspectionParam" result="inspectionResult"/>
                        <div class="table-gp">
                            <table aria-describedby="" class="table table-responsive"
                                   style="border-collapse:collapse;">
                                <thead>
                                <tr>
                                    <iais:sortableHeader needSort="false"
                                                         style="white-space: nowrap;padding: 15px 25px 15px 0px;"
                                                         field=""
                                                         value="S/N"/>
                                    <iais:sortableHeader needSort="false"
                                                         style="white-space: nowrap;padding: 15px 25px 15px 0px;"
                                                         field=""
                                                         value="View Inspection Report"/>
                                    <iais:sortableHeader needSort="true"
                                                         style="white-space: nowrap;padding: 15px 25px 15px 0px;"
                                                         field="APPLICATION_NO"
                                                         value="Application No."/>
                                    <iais:sortableHeader needSort="true"
                                                         style="white-space: nowrap;padding: 15px 25px 15px 0px;"
                                                         field="APP_TYPE_STR"
                                                         value="Application Type"/>
                                    <iais:sortableHeader needSort="true"
                                                         style="white-space: nowrap;padding: 15px 25px 15px 0px;"
                                                         field="SVC_NAME"
                                                         value="Service Name"/>
                                    <iais:sortableHeader needSort="true"
                                                         style="white-space: nowrap;padding: 15px 25px 15px 0px;"
                                                         field="DICIPLINE_SS"
                                                         value="Discipline/SS"/>
                                    <iais:sortableHeader needSort="true"
                                                         style="white-space: nowrap;padding: 15px 25px 15px 0px;"
                                                         field="BUSINESS_NAME"
                                                         value="Business Name"/>
                                    <iais:sortableHeader needSort="true"
                                                         style="white-space: nowrap;padding: 15px 25px 15px 0px;"
                                                         field="VEHICLE_NUM"
                                                         value="Vehicle No."/>
                                    <iais:sortableHeader needSort="true"
                                                         style="white-space: nowrap;padding: 15px 25px 15px 0px;"
                                                         field="PREM_TYPE"
                                                         value="MOSD Type"/>
                                    <iais:sortableHeader needSort="true"
                                                         style="white-space: nowrap;padding: 15px 25px 15px 0px;"
                                                         field="ADDRESS"
                                                         value="MOSD Address"/>
                                    <iais:sortableHeader needSort="true"
                                                         style="white-space: nowrap;padding: 15px 25px 15px 0px;"
                                                         field="ID_NUMBER"
                                                         value="Licensee ID No."/>
                                    <iais:sortableHeader needSort="true"
                                                         style="white-space: nowrap;padding: 15px 25px 15px 0px;"
                                                         field="LICENSEE_NAME"
                                                         value="Licensee Name"/>
                                    <iais:sortableHeader needSort="true"
                                                         style="white-space: nowrap;padding: 15px 25px 15px 0px;"
                                                         field="AUTO_APPROVE"
                                                         value="Auto Approved"/>
                                    <iais:sortableHeader needSort="true"
                                                         style="white-space: nowrap;padding: 15px 25px 15px 0px;"
                                                         field="APP_STATUS_STR"
                                                         value="Application Status"/>
                                    <iais:sortableHeader needSort="true"
                                                         style="white-space: nowrap;padding: 15px 25px 15px 0px;"
                                                         field="COMPLIANCE"
                                                         value="Compliance Tagging"/>
                                    <iais:sortableHeader needSort="true"
                                                         style="white-space: nowrap;padding: 15px 25px 15px 0px;"
                                                         field="NC_NUM"
                                                         value="No. of NCs"/>
                                    <iais:sortableHeader needSort="true"
                                                         style="white-space: nowrap;padding: 15px 25px 15px 0px;"
                                                         field="LAST_RISK_LEVEL_STR"
                                                         value="Risk Tagging"/>
                                    <iais:sortableHeader needSort="true"
                                                         style="white-space: nowrap;padding: 15px 25px 15px 0px;"
                                                         field="PREM_TYPE"
                                                         value="Inspection Type"/>
                                    <iais:sortableHeader needSort="true"
                                                         style="white-space: nowrap;padding: 15px 25px 15px 0px;"
                                                         field="TCU_DATE"
                                                         value="Reason for Inspection"/>
                                    <iais:sortableHeader needSort="true"
                                                         style="white-space: nowrap;padding: 15px 25px 15px 0px;"
                                                         field="LAST_INSP_START_DATE"
                                                         value="Inspection Date"/>
                                    <iais:sortableHeader needSort="true"
                                                         style="white-space: nowrap;padding: 15px 25px 15px 0px;"
                                                         field="INSP_NAME"
                                                         value="Inspector(s)"/>
                                    <iais:sortableHeader needSort="true"
                                                         style="white-space: nowrap;padding: 15px 25px 15px 0px;"
                                                         field="AUDIT_TYPE_STR"
                                                         value="Audit Type"/>
                                    <iais:sortableHeader needSort="true"
                                                         style="white-space: nowrap;padding: 15px 25px 15px 0px;"
                                                         field="TCU_DATE"
                                                         value="TCU Date"/>
                                </tr>
                                </thead>
                                <tbody class="form-horizontal">
                                <c:choose>
                                    <c:when test="${empty inspectionResult.rows}">
                                        <tr>
                                            <td colspan="22">
                                                <iais:message key="GENERAL_ACK018"
                                                              escape="true"/>
                                            </td>
                                        </tr>
                                    </c:when>
                                    <c:otherwise>
                                        <c:forEach var="inspection"
                                                   items="${inspectionResult.rows}"
                                                   varStatus="status">
                                            <tr>
                                                <td style="vertical-align:middle;">
                                                    <p class="visible-xs visible-sm table-row-title">S/N</p>
                                                    <c:out value="${status.index + 1+ (inspectionParam.pageNo - 1) * inspectionParam.pageSize}"/>
                                                </td>

                                                <td style="vertical-align:middle;">
                                                    <p class="visible-xs visible-sm table-row-title">View Inspection Report</p>
                                                    <a href="#"
                                                       onclick="fullDetailsView('${MaskUtil.maskValue('appCorrId', inspection.appCorrId)}')">View Inspection Report</a>
                                                </td>
                                                <td style="vertical-align:middle;">
                                                    <p class="visible-xs visible-sm table-row-title">Application No.</p>
                                                    <c:out value="${inspection.applicationNo}"/>
                                                </td>
                                                <td style="vertical-align:middle;">
                                                    <p class="visible-xs visible-sm table-row-title">Application Type</p>
                                                    <c:out value="${inspection.appType}"/>
                                                </td>
                                                <td style="vertical-align:middle;">
                                                    <p class="visible-xs visible-sm table-row-title">Service
                                                        Name</p>
                                                    <c:out value="${inspection.serviceName}"/>
                                                </td>
                                                <td style="vertical-align:middle;">
                                                    <p class="visible-xs visible-sm table-row-title">
                                                        Discipline/SS</p>
                                                    <c:out value="${inspection.disciplineAndSs}"/>
                                                </td>
                                                <td style="vertical-align:middle;">
                                                    <p class="visible-xs visible-sm table-row-title">Business
                                                        Name</p>
                                                    <c:out value="${inspection.businessName}"/>
                                                </td>
                                                <td style="vertical-align:middle;">
                                                    <p class="visible-xs visible-sm table-row-title">Vehicle No.</p>
                                                    <iais:code code="${inspection.vehicleNo}" needEscapHtml="false"/>
                                                </td>
                                                <td style="vertical-align:middle;">
                                                    <p class="visible-xs visible-sm table-row-title">MOSD Type</p>
                                                    <c:out value="${inspection.mosdType}"/>
                                                </td>
                                                <td style="vertical-align:middle;">
                                                    <p class="visible-xs visible-sm table-row-title">MOSD
                                                        Address</p>
                                                    <c:out value="${inspection.mosdAddress}"/>
                                                </td>
                                                <td style="vertical-align:middle;">
                                                    <p class="visible-xs visible-sm table-row-title">Licensee ID
                                                        No.</p>
                                                    <c:out value="${inspection.licenseeIdNo}"/>
                                                </td>
                                                <td style="vertical-align:middle;">
                                                    <p class="visible-xs visible-sm table-row-title">Licensee
                                                        Name</p>
                                                    <c:out value="${inspection.licenseeIdName}"/>
                                                </td>
                                                <td style="vertical-align:middle;">
                                                    <p class="visible-xs visible-sm table-row-title">Auto Approved</p>
                                                    <c:out value="${inspection.autoApprove}"/>
                                                </td>
                                                <td style="vertical-align:middle;">
                                                    <p class="visible-xs visible-sm table-row-title">Application Status</p>
                                                    <c:out value="${inspection.appStatus}"/>
                                                </td>
                                                <td style="vertical-align:middle;">
                                                    <p class="visible-xs visible-sm table-row-title">Compliance
                                                        Tagging</p>
                                                    <c:out value="${inspection.compliance}"/>
                                                </td>
                                                <td style="vertical-align:middle;">
                                                    <p class="visible-xs visible-sm table-row-title">No. of NCs</p>
                                                    <c:out value="${inspection.ncNum}"/>
                                                </td>
                                                <td style="vertical-align:middle;">
                                                    <p class="visible-xs visible-sm table-row-title">Risk Tagging</p>
                                                    <c:out value="${inspection.risk}"/>
                                                </td>
                                                <td style="vertical-align:middle;">
                                                    <p class="visible-xs visible-sm table-row-title">Inspection Type</p>
                                                    <c:out value="${inspection.inspectionType}"/>
                                                </td>
                                                <td style="vertical-align:middle;">
                                                    <p class="visible-xs visible-sm table-row-title">Reason for
                                                        Inspection</p>
                                                    <c:out value="${inspection.reason}"/>
                                                </td>
                                                <td style="vertical-align:middle;">
                                                    <p class="visible-xs visible-sm table-row-title">Inspection Date</p>
                                                    <c:out value="${inspection.inspectionDateStr}"/>
                                                </td>
                                                <td style="vertical-align:middle;">
                                                    <p class="visible-xs visible-sm table-row-title">Inspector(s)</p>
                                                    <c:out value="${inspection.inspectors}"/>
                                                </td>
                                                <td style="vertical-align:middle;">
                                                    <p class="visible-xs visible-sm table-row-title">Audit Type</p>
                                                    <c:choose>
                                                        <c:when test="${not empty inspection.auditType}">
                                                            <c:out value="${inspection.auditType}"/>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <iais:code code="-"/>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </td>
                                                <td style="vertical-align:middle;">
                                                    <p class="visible-xs visible-sm table-row-title">TCU Date</p>
                                                    <c:out value="${inspection.tcuDateStr}"/>
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
                               href="${pageContext.request.contextPath}/hcsa/enquiry/hcsa/Inspection-SearchResults-Download">Download</a>
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
        $(".clearSel").children(".current").text("All");

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
        $("[name='crud_action_type']").val('searchIns');
        $('#mainForm').submit();
    }

    function sortRecords(sortFieldName, sortType) {
        $("[name='crud_action_value']").val(sortFieldName);
        $("[name='crud_action_additional']").val(sortType);
        $("[name='crud_action_type']").val('searchIns');
        $('#mainForm').submit();
    }


    var fullDetailsView = function (submissionNo) {

        showWaiting();
        $("[name='crud_action_value']").val(submissionNo);
        $("[name='crud_action_type']").val('insInfo');
        $('#mainForm').submit();
    }

    function changeButtonName() {
        var flag = $("#searchCondition").is(":visible");
        if (flag){
            $(".filterButton").text("More Filters");
        }else {
            $(".filterButton").text("Less Filters");
        }
    }
</script>

