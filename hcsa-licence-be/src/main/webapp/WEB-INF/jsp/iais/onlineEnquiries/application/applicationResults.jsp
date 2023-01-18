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
                        <div class="col-xs-12 col-md-12">
                            <h2>Application Search</h2>
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
                                        <a class="btn btn-secondary" data-toggle="collapse"
                                           data-target="#searchCondition">Filter</a>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div class="col-xs-12 col-md-12">
                            <iais:row>
                                <label class="col-xs-3 col-md-3 control-label">Application No.</label>
                                <iais:value width="5" cssClass="col-md-5">
                                    <input type="text" maxlength="20" id="applicationNo" name="applicationNo"
                                           value="${applicationTabEnquiryFilterDto.applicationNo}">
                                </iais:value>
                            </iais:row>
                            <iais:row>
                                <label class="col-xs-3 col-md-3 control-label">Application Type</label>
                                <iais:value width="5" cssClass="col-md-5">
                                    <iais:select name="applicationType" codeCategory="CATE_ID_APP_TYPE"
                                                 firstOption="All"
                                                 cssClass="clearSel"
                                                 value="${applicationTabEnquiryFilterDto.applicationType}"/>
                                </iais:value>
                            </iais:row>
                            <iais:row>
                                <label class="col-xs-3 col-md-3 control-label">Business Name</label>
                                <iais:value width="5" cssClass="col-md-5">
                                    <input type="text" maxlength="100" id="businessName" name="businessName"
                                           value="${applicationTabEnquiryFilterDto.businessName}">
                                </iais:value>
                            </iais:row>
                            <iais:row>
                                <label class="col-xs-3 col-md-3 control-label">MOSD Address Postal Code</label>
                                <iais:value width="5" cssClass="col-md-5">
                                    <input type="number" oninput="if(value.length>6)value=value.slice(0,6)"
                                           style="margin-bottom: 0px;" id="postalCode" name="postalCode"
                                           value="${applicationTabEnquiryFilterDto.postalCode}">
                                </iais:value>
                            </iais:row>
                            <iais:row>
                                <label class="col-xs-3 col-md-3 control-label">MOSD Address Street Name</label>
                                <iais:value width="5" cssClass="col-md-5">
                                    <input type="text" maxlength="32" id="streetName" name="streetName"
                                           value="${applicationTabEnquiryFilterDto.streetName}">
                                </iais:value>
                            </iais:row>
                            <iais:row>
                                <label class="col-xs-3 col-md-3 control-label">Auto Approved</label>
                                <iais:value width="5" cssClass="col-md-5">
                                    <iais:select name="autoApproved" id="autoApproved" firstOption="All"
                                                 options="autoApprovedOption"
                                                 cssClass="clearSel"
                                                 value="${applicationTabEnquiryFilterDto.autoApproved}"/>
                                </iais:value>
                            </iais:row>
                            <iais:row>
                                <label class="col-xs-3 col-md-3 control-label">Application Status</label>
                                <iais:value width="5" cssClass="col-md-5">
                                    <iais:select name="appStatus" options="appStatusOption" firstOption="All"
                                                 cssClass="clearSel"
                                                 value="${applicationTabEnquiryFilterDto.appStatus}"/>
                                </iais:value>
                            </iais:row>

                            <div id="searchCondition" class="collapse">
                                <iais:row>
                                    <label class="col-xs-3 col-md-3 control-label">Licensee Name</label>
                                    <iais:value width="5" cssClass="col-md-5">
                                        <input type="text" maxlength="100" id="licenseeName" name="licenseeName"
                                               value="${applicationTabEnquiryFilterDto.licenseeName}">
                                    </iais:value>
                                </iais:row>
                                <iais:row>
                                    <label class="col-xs-3 col-md-3 control-label">Licensee ID No.</label>
                                    <iais:value width="5" cssClass="col-md-5">
                                        <input type="text" maxlength="20" id="licenseeIdNo" name="licenseeIdNo"
                                               value="${applicationTabEnquiryFilterDto.licenseeIdNo}">
                                    </iais:value>
                                </iais:row>

                                <iais:row>
                                    <label class="col-xs-3 col-md-3 control-label">Assigned Officer</label>
                                    <iais:value width="5" cssClass="col-md-5">
                                        <iais:select name="assignedOfficer" id="assignedOfficer"
                                                     firstOption="All"
                                                     options="assignedOfficerOption"
                                                     cssClass="clearSel"
                                                     value="${applicationTabEnquiryFilterDto.assignedOfficer}"/>
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
                        <iais:pagination param="appParam" result="appResult"/>
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
                                                         field="BUSINESS_NAME"
                                                         value="Business Name"/>
                                    <iais:sortableHeader needSort="true"
                                                         style="white-space: nowrap;padding: 15px 25px 15px 0px;"
                                                         field="PMT_STATUS_STR"
                                                         value="Payment Status"/>
                                    <iais:sortableHeader needSort="false"
                                                         style="white-space: nowrap;padding: 15px 25px 15px 0px;"
                                                         field=""
                                                         value="View Payment Details"/>
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
                                                         field="VEHICLE_NUM"
                                                         value="Vehicle No."/>
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
                                                         field="SUBMIT_DT"
                                                         value="Application Date"/>
                                    <iais:sortableHeader needSort="true"
                                                         style="white-space: nowrap;padding: 15px 25px 15px 0px;"
                                                         field="APP_STATUS_STR"
                                                         value="Application Status"/>
                                    <iais:sortableHeader needSort="true"
                                                         style="white-space: nowrap;padding: 15px 25px 15px 0px;"
                                                         field="SUBMIT_BY"
                                                         value="Submitted By"/>
                                    <iais:sortableHeader needSort="true"
                                                         style="white-space: nowrap;padding: 15px 25px 15px 0px;"
                                                         field="DISPLAY_NAME"
                                                         value="Assigned Officer"/>
                                    <iais:sortableHeader needSort="true"
                                                         style="white-space: nowrap;padding: 15px 25px 15px 0px;"
                                                         field="AUTO_APPROVE"
                                                         value="Auto Approved"/>
                                </tr>
                                </thead>
                                <tbody class="form-horizontal">
                                <c:choose>
                                    <c:when test="${empty appResult.rows}">
                                        <tr>
                                            <td colspan="17">
                                                <iais:message key="GENERAL_ACK018"
                                                              escape="true"/>
                                            </td>
                                        </tr>
                                    </c:when>
                                    <c:otherwise>
                                        <c:forEach var="app"
                                                   items="${appResult.rows}"
                                                   varStatus="status">
                                            <tr>
                                                <td style="vertical-align:middle;">
                                                    <p class="visible-xs visible-sm table-row-title">S/N</p>
                                                    <c:out value="${status.index + 1+ (appParam.pageNo - 1) * appParam.pageSize}"/>
                                                </td>
                                                <td style="vertical-align:middle;">
                                                    <p class="visible-xs visible-sm table-row-title">Application No.</p>
                                                    <a href="#"
                                                       onclick="fullDetailsView('${MaskUtil.maskValue('appId', app.appId)}')">${app.applicationNo}</a>

                                                </td>
                                                <td style="vertical-align:middle;">
                                                    <p class="visible-xs visible-sm table-row-title">Application
                                                        Type</p>
                                                    <c:out value="${app.appType}"/>
                                                </td>
                                                <td style="vertical-align:middle;">
                                                    <p class="visible-xs visible-sm table-row-title">Service
                                                        Name</p>
                                                    <c:out value="${app.serviceName}"/>
                                                </td>
                                                <td style="vertical-align:middle;">
                                                    <p class="visible-xs visible-sm table-row-title">Business Name</p>
                                                    <c:out value="${app.businessName}"/>
                                                </td>
                                                <td style="vertical-align:middle;">
                                                    <p class="visible-xs visible-sm table-row-title">Payment Status</p>
                                                    <c:out value="${app.pmtStatus}"/>
                                                </td>
                                                <td style="vertical-align:middle;">
                                                    <p class="visible-xs visible-sm table-row-title">View Payment
                                                        Details</p>
                                                    <a href="#"
                                                       onclick="fullDetailsView('${MaskUtil.maskValue('payAppNo', app.applicationNo)}')"
                                                    >View Payment Details</a>
                                                </td>
                                                <td style="vertical-align:middle;">
                                                    <p class="visible-xs visible-sm table-row-title">MOSD Type</p>
                                                    <c:out value="${app.mosdType}"/>
                                                </td>
                                                <td style="vertical-align:middle;">
                                                    <p class="visible-xs visible-sm table-row-title">MOSD
                                                        Address</p>
                                                    <c:out value="${app.mosdAddress}"/>
                                                </td>
                                                <td style="vertical-align:middle;">
                                                    <p class="visible-xs visible-sm table-row-title">Vehicle No.</p>
                                                    <c:out value="${app.vehicleNo}"/>
                                                </td>
                                                <td style="vertical-align:middle;">
                                                    <p class="visible-xs visible-sm table-row-title">Licensee ID
                                                        No.</p>
                                                    <c:out value="${app.licenseeIdNo}"/>
                                                </td>
                                                <td style="vertical-align:middle;">
                                                    <p class="visible-xs visible-sm table-row-title">Licensee
                                                        Name</p>
                                                    <c:out value="${app.licenseeIdName}"/>
                                                </td>
                                                <td style="vertical-align:middle;">
                                                    <p class="visible-xs visible-sm table-row-title">Application
                                                        Date</p>
                                                    <c:out value="${app.submitDtStr}"/>
                                                </td>
                                                <td style="vertical-align:middle;">
                                                    <p class="visible-xs visible-sm table-row-title">Application
                                                        Status</p>
                                                    <c:out value="${app.appStatus}"/>
                                                </td>
                                                <td style="vertical-align:middle;">
                                                    <p class="visible-xs visible-sm table-row-title">Submitted By</p>
                                                    <c:out value="${app.submitDy}"/>
                                                </td>
                                                <td style="vertical-align:middle;">
                                                    <p class="visible-xs visible-sm table-row-title">Assigned
                                                        Officer</p>
                                                    <c:out value="${app.assignedOfficer}"/>
                                                </td>
                                                <td style="vertical-align:middle;">
                                                    <p class="visible-xs visible-sm table-row-title">Auto Approved</p>
                                                    <c:out value="${app.autoApprove}"/>
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
                               href="${pageContext.request.contextPath}/hcsa/enquiry/hcsa/Application-SearchResults-Download">Download</a>
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
        $("[name='crud_action_type']").val('searchApp');
        $('#mainForm').submit();
    }

    function sortRecords(sortFieldName, sortType) {
        $("[name='crud_action_value']").val(sortFieldName);
        $("[name='crud_action_additional']").val(sortType);
        $("[name='crud_action_type']").val('searchApp');
        $('#mainForm').submit();
    }


    var fullDetailsView = function (submissionNo) {

        showWaiting();
        $("[name='crud_action_value']").val(submissionNo);
        $("[name='crud_action_type']").val('appInfo');
        $('#mainForm').submit();
    }
</script>

