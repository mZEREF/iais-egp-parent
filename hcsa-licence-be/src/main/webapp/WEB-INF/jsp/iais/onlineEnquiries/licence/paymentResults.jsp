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
                            <h2>Payment Status Enquiry</h2>
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
                                <label class="col-xs-3 col-md-3 control-label">Application No.</label>
                                <iais:value width="5" cssClass="col-md-5">
                                    <input type="text" maxlength="20" id="applicationNo" name="applicationNo"
                                           value="${paymentEnquiryFilterDto.applicationNo}">
                                </iais:value>
                            </iais:row>
                            <iais:row>
                                <label class="col-xs-3 col-md-3 control-label">Licence No.</label>
                                <iais:value width="5" cssClass="col-md-5">
                                    <input type="text" maxlength="24" id="licenceNo" name="licenceNo"
                                           value="${paymentEnquiryFilterDto.licenceNo}">
                                </iais:value>
                            </iais:row>
                            <iais:row>
                                <label class="col-xs-3 col-md-3 control-label">Business Name</label>
                                <iais:value width="5" cssClass="col-md-5">
                                    <input type="text" maxlength="100" id="businessName" name="businessName"
                                           value="${paymentEnquiryFilterDto.businessName}">
                                </iais:value>
                            </iais:row>
                            <iais:row>
                                <label class="col-xs-3 col-md-3 control-label">Service Name</label>
                                <iais:value width="5" cssClass="col-md-5">
                                    <iais:select name="serviceName" options="licSvcTypeOption"
                                                 firstOption="All"
                                                 cssClass="clearSel"  value="${paymentEnquiryFilterDto.serviceName}"/>
                                </iais:value>
                            </iais:row>
                            <iais:row>
                                <label class="col-xs-3 col-md-3 control-label">Application Type</label>
                                <iais:value width="5" cssClass="col-md-5">
                                    <iais:select name="applicationType" codeCategory="CATE_ID_APP_TYPE"
                                                 firstOption="All"
                                                 cssClass="clearSel"   value="${paymentEnquiryFilterDto.applicationType}"/>
                                </iais:value>
                            </iais:row>
                            <iais:row>
                                <label class="col-xs-3 col-md-3 control-label">Application Date From</label>
                                <iais:value width="2" cssClass="col-md-2">
                                    <iais:datePicker id="applicationDateFrom" name="applicationDateFrom"
                                                     dateVal="${paymentEnquiryFilterDto.applicationDateFrom}"/>
                                </iais:value>
                                <label class="col-xs-1 col-md-1 control-label">To&nbsp;</label>
                                <iais:value width="2" cssClass="col-md-2">
                                    <iais:datePicker id="applicationDateTo" name="applicationDateTo"
                                                     dateVal="${paymentEnquiryFilterDto.applicationDateTo}"/>
                                </iais:value>
                            </iais:row>

                            <iais:row>
                                <label class="col-xs-3 col-md-3 control-label">Payment Mode</label>
                                <iais:value width="5" cssClass="col-md-5">
                                    <iais:select name="paymentMode" options="paymentModeOption"
                                                 cssClass="clearSel"  firstOption="All"
                                                 value="${paymentEnquiryFilterDto.paymentMode}"/>
                                </iais:value>
                            </iais:row>
                            <iais:row>
                                <label class="col-xs-3 col-md-3 control-label">Payment Status</label>
                                <iais:value width="5" cssClass="col-md-5">
                                    <iais:select name="paymentStatus" options="paymentStatusOption"
                                                 cssClass="clearSel"  firstOption="All"
                                                 value="${paymentEnquiryFilterDto.paymentStatus}"/>
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
                        <iais:pagination param="paymentParam" result="paymentResult"/>
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
                                                         field="BUSINESS_NAME"
                                                         value="Business Name"/>
                                    <iais:sortableHeader needSort="true"
                                                         style="white-space: nowrap;padding: 15px 25px 15px 0px;"
                                                         field="SVC_NAME"
                                                         value="Service Name"/>
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
                                                         field="SUBMIT_DT"
                                                         value="Application Date"/>
                                    <iais:sortableHeader needSort="true"
                                                         style="white-space: nowrap;padding: 15px 25px 15px 0px;"
                                                         field="AMOUNT"
                                                         value="Fees Amount"/>
                                    <iais:sortableHeader needSort="true"
                                                         style="white-space: nowrap;padding: 15px 25px 15px 0px;"
                                                         field="PAY_METHOD_STR"
                                                         value="Payment Mode"/>
                                    <iais:sortableHeader needSort="true"
                                                         style="white-space: nowrap;padding: 15px 25px 15px 0px;"
                                                         field="PMT_STATUS_STR"
                                                         value="Payment Status"/>
                                    <iais:sortableHeader needSort="true"
                                                         style="white-space: nowrap;padding: 15px 25px 15px 0px;"
                                                         field="PAYMENT_DT"
                                                         value="Payment Date"/>
                                </tr>
                                </thead>
                                <tbody class="form-horizontal">
                                <c:choose>
                                    <c:when test="${empty paymentResult.rows}">
                                        <tr>
                                            <td colspan="15">
                                                <iais:message key="GENERAL_ACK018"
                                                              escape="true"/>
                                            </td>
                                        </tr>
                                    </c:when>
                                    <c:otherwise>
                                        <c:forEach var="main"
                                                   items="${paymentResult.rows}"
                                                   varStatus="status">
                                            <tr>
                                                <td style="vertical-align:middle;">
                                                    <p class="visible-xs visible-sm table-row-title">S/N</p>
                                                    <c:out value="${status.index + 1+ (paymentParam.pageNo - 1) * paymentParam.pageSize}"/>
                                                </td>
                                                <td style="vertical-align:middle;">
                                                    <p class="visible-xs visible-sm table-row-title">Application No.</p>
                                                    <c:out value="${main.applicationNo}"/>
                                                </td>
                                                <td style="vertical-align:middle;">
                                                    <p class="visible-xs visible-sm table-row-title">Application Type</p>
                                                    <c:out value="${main.appType}"/>
                                                </td>
                                                <td style="vertical-align:middle;">
                                                    <p class="visible-xs visible-sm table-row-title">Business
                                                        Name</p>
                                                    <c:out value="${main.businessName}"/>
                                                </td>
                                                <td style="vertical-align:middle;">
                                                    <p class="visible-xs visible-sm table-row-title">Service
                                                        Name</p>
                                                    <c:out value="${main.serviceName}"/>
                                                </td>
                                                <td style="vertical-align:middle;">
                                                    <p class="visible-xs visible-sm table-row-title">MOSD Type</p>
                                                    <c:out value="${main.mosdType}"/>
                                                </td>
                                                <td style="vertical-align:middle;">
                                                    <p class="visible-xs visible-sm table-row-title">MOSD
                                                        Address</p>
                                                    <c:out value="${main.mosdAddress}"/>
                                                </td>
                                                <td style="vertical-align:middle;">
                                                    <p class="visible-xs visible-sm table-row-title">Application Date</p>
                                                    <c:out value="${main.submitDtStr}"/>
                                                </td>
                                                <td style="vertical-align:middle;">
                                                    <p class="visible-xs visible-sm table-row-title">Fees Amount</p>
                                                    <c:out value="${main.feesAmount}"/>
                                                </td>
                                                <td style="vertical-align:middle;">
                                                    <p class="visible-xs visible-sm table-row-title">Payment Mode</p>
                                                    <c:out value="${main.pmtMode}"/>
                                                </td>
                                                <td style="vertical-align:middle;">
                                                    <p class="visible-xs visible-sm table-row-title">Payment
                                                        Status</p>
                                                    <c:out value="${main.pmtStatus}"/>
                                                </td>
                                                <td style="vertical-align:middle;">
                                                    <p class="visible-xs visible-sm table-row-title">Payment
                                                        Date</p>
                                                    <c:out value="${main.paymentDtStr}"/>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </c:otherwise>
                                </c:choose>
                                </tbody>
                            </table>
                        </div>
                        <p class="text-right text-center-mobile">
                            <c:if test="${not empty payAppStep}">
                                <a style="float:left;padding-top: 1.1%;text-decoration:none;" class="back" href="/hcsa-licence-web/eservice/INTRANET/MohApplicationOnlineEnquiry/1/preSearch?back=back"><em class="fa fa-angle-left"></em> Back</a>
                            </c:if>
                            <c:if test="${not empty payAppInsStep}">
                                <a style="float:left;padding-top: 1.1%;text-decoration:none;" class="back" href="/hcsa-licence-web/eservice/INTRANET/MohApplicationOnlineEnquiry/1/preAppInfo"><em class="fa fa-angle-left"></em> Back</a>
                            </c:if>
                            <c:if test="${not empty payLicStep}">
                                <a style="float:left;padding-top: 1.1%;text-decoration:none;" class="back" href="/hcsa-licence-web/eservice/INTRANET/MohLicenceOnlineEnquiry/1/preLicInfo"><em class="fa fa-angle-left"></em> Back</a>
                            </c:if>
                            <iais:action style="text-align:right;">
                                <a class="btn btn-secondary"
                                   href="${pageContext.request.contextPath}/hcsa/enquiry/hcsa/Payment-SearchResults-Download">Download</a>
                            </iais:action>
                        </p>
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

