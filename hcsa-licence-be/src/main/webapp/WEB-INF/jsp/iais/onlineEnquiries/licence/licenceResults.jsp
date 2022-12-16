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
                            <h2>Licence Search</h2>
                        </div>
                        <div class="bg-title col-xs-12 col-md-12">
                            One search filter must be entered to proceed with search.
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
                                <iais:field width="4" value="Licence No."/>
                                <iais:value width="4" cssClass="col-md-4">
                                    <input type="text" maxlength="24" id="licenceNo" name="licenceNo"
                                           value="${licenceEnquiryFilterDto.licenceNo}">
                                </iais:value>
                            </iais:row>
                            <iais:row>
                                <iais:field width="4" value="MOSD Type"/>
                                <iais:value width="4" cssClass="col-md-4">
                                    <iais:select name="mosdType" id="mosdType" firstOption="Please Select"
                                                 options="mosdTypeOption"
                                                 cssClass="clearSel" value="${licenceEnquiryFilterDto.mosdType}"/>
                                </iais:value>
                            </iais:row>

                            <iais:row>
                                <iais:field width="4" value="MOSD Address Postal Code"/>
                                <iais:value width="4" cssClass="col-md-4">
                                    <input type="number" oninput="if(value.length>6)value=value.slice(0,6)"
                                           style="margin-bottom: 0px;" id="postalCode" name="postalCode"
                                           value="${licenceEnquiryFilterDto.postalCode}">
                                </iais:value>
                            </iais:row>
                            <iais:row>
                                <iais:field width="4" value="MOSD Address Street Name"/>
                                <iais:value width="4" cssClass="col-md-4">
                                    <input type="text" maxlength="32" id="streetName" name="streetName"
                                           value="${licenceEnquiryFilterDto.streetName}">
                                </iais:value>
                            </iais:row>
                            <iais:row>
                                <iais:field width="4" value="Service Name"/>
                                <iais:value width="4" cssClass="col-md-4">
                                    <iais:select name="serviceName" options="licSvcTypeOption"
                                                 firstOption="Please Select"
                                                 value="${licenceEnquiryFilterDto.serviceName}"></iais:select>
                                </iais:value>
                            </iais:row>
                            <iais:row>
                                <iais:field width="4" value="Business Name"/>
                                <iais:value width="4" cssClass="col-md-4">
                                    <input type="text" maxlength="100" id="businessName" name="businessName"
                                           value="${licenceEnquiryFilterDto.businessName}">
                                </iais:value>
                            </iais:row>

                            <iais:row>
                                <iais:field width="4" value="Licence Status"/>
                                <iais:value width="4" cssClass="col-md-4">
                                    <iais:select name="licenceStatus" codeCategory="CATE_ID_LICENCE_STATUS"
                                                 firstOption="Please Select"
                                                 value="${licenceEnquiryFilterDto.licenceStatus}"></iais:select>
                                </iais:value>
                            </iais:row>
                            <div id="searchCondition" class="collapse">
                                <iais:row>
                                    <iais:field width="4" value="Licensee ID No."/>
                                    <iais:value width="4" cssClass="col-md-4">
                                        <input type="text" maxlength="20" id="licenseeIdNo" name="licenseeIdNo"
                                               value="${licenceEnquiryFilterDto.licenseeIdNo}">
                                    </iais:value>
                                </iais:row>
                                <iais:row>
                                    <iais:field width="4" value="Licensee Name"/>
                                    <iais:value width="4" cssClass="col-md-4">
                                        <input type="text" maxlength="100" id="licenseeName" name="licenseeName"
                                               value="${licenceEnquiryFilterDto.licenseeName}">
                                    </iais:value>
                                </iais:row>
                                <iais:row>
                                    <iais:field width="4" value="Vehicle No."/>
                                    <iais:value width="4" cssClass="col-md-4">
                                        <input type="text" maxlength="10" id="vehicleNo" name="vehicleNo"
                                               value="${licenceEnquiryFilterDto.vehicleNo}">
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
                        <iais:pagination param="licParam" result="licenceResult"/>
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
                                                         field="SVC_NAME"
                                                         value="Service Name"/>
                                    <iais:sortableHeader needSort="true"
                                                         style="white-space: nowrap;padding: 15px 25px 15px 0px;"
                                                         field="BUSINESS_NAME"
                                                         value="Business Name"/>
                                    <iais:sortableHeader needSort="true"
                                                         style="white-space: nowrap;padding: 15px 25px 15px 0px;"
                                                         field="PREMISES_TYPE"
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
                                                         field="START_DATE"
                                                         value="Licence Period"/>
                                    <iais:sortableHeader needSort="true"
                                                         style="white-space: nowrap;padding: 15px 25px 15px 0px;"
                                                         field="LicSTATUS"
                                                         value="Licence Status"/>
                                    <iais:sortableHeader needSort="true"
                                                         style="white-space: nowrap;padding: 15px 25px 15px 0px;"
                                                         field="LICENSEE_NAME"
                                                         value="Licensee Name"/>
                                    <iais:sortableHeader needSort="true"
                                                         style="white-space: nowrap;padding: 15px 25px 15px 0px;"
                                                         field="LICENSEE_ID_NO"
                                                         value="Licensee ID No."/>
                                    <iais:sortableHeader needSort="true"
                                                         style="white-space: nowrap;padding: 15px 25px 15px 0px;"
                                                         field="CATEGORY"
                                                         value="Category"/>
                                    <iais:sortableHeader needSort="true"
                                                         style="white-space: nowrap;padding: 15px 25px 15px 0px;"
                                                         field="DICIPLINE_SS"
                                                         value="Discipline/SS"/>
                                    <iais:sortableHeader needSort="true"
                                                         style="white-space: nowrap;padding: 15px 25px 15px 0px;"
                                                         field="LAST_INSP_START_DATE"
                                                         value="Last Inspection Date"/>
                                </tr>
                                </thead>
                                <tbody class="form-horizontal">
                                <c:choose>
                                    <c:when test="${empty licenceResult.rows}">
                                        <tr>
                                            <td colspan="15">
                                                <iais:message key="GENERAL_ACK018"
                                                              escape="true"/>
                                            </td>
                                        </tr>
                                    </c:when>
                                    <c:otherwise>
                                        <c:forEach var="licence"
                                                   items="${licenceResult.rows}"
                                                   varStatus="status">
                                            <tr>
                                                <td style="vertical-align:middle;">
                                                    <p class="visible-xs visible-sm table-row-title">S/N</p>
                                                    <c:out value="${status.index + 1+ (licParam.pageNo - 1) * licParam.pageSize}"/>
                                                </td>
                                                <td style="vertical-align:middle;">
                                                    <p class="visible-xs visible-sm table-row-title">Licence No.</p>
                                                    <a href="#"
                                                       onclick="fullDetailsView('${MaskUtil.maskValue('licenceId', licence.licenceId)}')">${licence.licenceNo}</a>
                                                </td>
                                                <td style="vertical-align:middle;">
                                                    <p class="visible-xs visible-sm table-row-title">Service
                                                        Name</p>
                                                    <c:out value="${licence.serviceName}"/>
                                                </td>
                                                <td style="vertical-align:middle;">
                                                    <p class="visible-xs visible-sm table-row-title">Business
                                                        Name</p>
                                                    <c:out value="${licence.businessName}"/>
                                                </td>
                                                <td style="vertical-align:middle;">
                                                    <p class="visible-xs visible-sm table-row-title">MOSD Type</p>
                                                    <c:out value="${licence.mosdType}"/>
                                                </td>
                                                <td style="vertical-align:middle;">
                                                    <p class="visible-xs visible-sm table-row-title">MOSD
                                                        Address</p>
                                                    <c:out value="${licence.mosdAddress}"/>
                                                </td>
                                                <td style="vertical-align:middle;">
                                                    <p class="visible-xs visible-sm table-row-title">Vehicle No.</p>
                                                    <c:out value="${licence.vehicleNo}"/>
                                                </td>
                                                <td style="vertical-align:middle;">
                                                    <p class="visible-xs visible-sm table-row-title">Licence
                                                        Period</p>
                                                    <fmt:formatDate
                                                            value="${licence.startDate}"
                                                            pattern="${AppConsts.DEFAULT_DATE_FORMAT}"/>-<fmt:formatDate
                                                        value="${licence.expiryDate}"
                                                        pattern="${AppConsts.DEFAULT_DATE_FORMAT}"/>
                                                </td>
                                                <td style="vertical-align:middle;">
                                                    <p class="visible-xs visible-sm table-row-title">Licence
                                                        Status</p>
                                                    <iais:code code="${licence.licenceStatus}"/>
                                                </td>
                                                <td style="vertical-align:middle;">
                                                    <p class="visible-xs visible-sm table-row-title">Licensee
                                                        Name</p>
                                                    <c:out value="${licence.licenseeIdName}"/>
                                                </td>
                                                <td style="vertical-align:middle;">
                                                    <p class="visible-xs visible-sm table-row-title">Licensee ID
                                                        No.</p>
                                                    <c:out value="${licence.licenseeIdNo}"/>
                                                </td>
                                                <td style="vertical-align:middle;">
                                                    <p class="visible-xs visible-sm table-row-title">Category</p>
                                                    <c:out value="${licence.category}"/>
                                                </td>
                                                <td style="vertical-align:middle;">
                                                    <p class="visible-xs visible-sm table-row-title">
                                                        Discipline/SS</p>
                                                    <c:out value="${licence.disciplineAndSs}"/>
                                                </td>
                                                <td style="vertical-align:middle;">
                                                    <p class="visible-xs visible-sm table-row-title">Last Inspection
                                                        Date</p>
                                                    <fmt:formatDate
                                                            value="${licence.lastInspectionDate}"
                                                            pattern="${AppConsts.DEFAULT_DATE_FORMAT}"/>
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
                               href="${pageContext.request.contextPath}/hcsa/enquiry/hcsa/Licence-SearchResults-Download">Download</a>
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
        $("[name='crud_action_type']").val('searchLic');
        $('#mainForm').submit();
    }

    function sortRecords(sortFieldName, sortType) {
        $("[name='crud_action_value']").val(sortFieldName);
        $("[name='crud_action_additional']").val(sortType);
        $("[name='crud_action_type']").val('searchLic');
        $('#mainForm').submit();
    }


    var fullDetailsView = function (submissionNo) {

        showWaiting();
        $("[name='crud_action_value']").val(submissionNo);
        $("[name='crud_action_type']").val('preLicInfo');
        $('#mainForm').submit();
    }
</script>
