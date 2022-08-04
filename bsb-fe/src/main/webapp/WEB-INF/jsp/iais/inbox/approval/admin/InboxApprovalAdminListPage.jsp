<%@ page import="static sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT" %>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib uri="ecquaria/sop/egov-smc" prefix="egov-smc" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.ecq.com/iais-bsb" prefix="iais-bsb" %>
<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ page import="sg.gov.moh.iais.egp.bsb.util.TableDisplayUtil" %>
<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-internet"/>

<link href="<%=WEB_ROOT%>/css/bsb/bsb-common.css" rel="stylesheet"/>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-common.js"></script>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-inbox.js"></script>


<%@include file="../../dashboard/dashboardFAC.jsp"%>
<%@include file="/WEB-INF/jsp/iais/include/showErrorMsg.jsp"%>

<fmt:setLocale value="en"/>
<div class="main-content">
    <div class="container">
        <div class="row">
            <div class="col-xs-12">
                <div class="tab-gp dashboard-tab" style="margin-left: 6px;margin-right: -8px;">
                    <%@ include file="../../InnerNavBarFAC.jsp"%>

                    <div class="tab-content">
                        <form class="" method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
                            <div id="searchPanel" class="tab-search">

                                <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
                                <input type="hidden" name="action_type" value="">
                                <input type="hidden" name="action_value" value="">
                                <input type="hidden" name="action_additional" value="">


                                <%--@elvariable id="inboxApprovalSearchDto" type="sg.gov.moh.iais.egp.bsb.dto.inbox.InboxApprovalSearchDto"--%>
                                <div class="row">
                                    <div class="col-xs-12 col-sm-6">
                                        <label class="col-xs-12 col-sm-5 control-label" for="searchApprovalNo">Approval No.:</label>
                                        <div class="col-xs-12 col-sm-7">
                                            <input type="text" id="searchApprovalNo" name="searchApprovalNo" value="${inboxApprovalSearchDto.searchApprovalNo}"/>
                                            <span data-err-ind="searchApprovalNo" class="error-msg"></span>
                                        </div>

                                    </div>
                                    <div class="col-xs-12 col-sm-6">
                                        <label class="col-xs-12 col-sm-5 control-label" for="searchProcessType">Application Sub-Type:</label>
                                        <div class="col-xs-12 col-sm-7">
                                            <select id="searchProcessType" class="searchProcessTypeDropDown" name="searchProcessType">
                                                <option value='<c:out value=""/>' <c:if test="${inboxApprovalSearchDto.searchProcessType eq ''}">selected="selected"</c:if>>All</option>
                                                <%--@elvariable id="processTypeOps" type="java.util.List<com.ecquaria.cloud.moh.iais.common.dto.SelectOption>"--%>
                                                <c:forEach var="appStatusItem" items="${processTypeOps}">
                                                    <option value='<c:out value="${appStatusItem.value}"/>' <c:if test="${inboxApprovalSearchDto.searchProcessType eq appStatusItem.value}">selected="selected"</c:if> ><c:out value="${appStatusItem.text}"/></option>
                                                </c:forEach>
                                            </select>
                                            <span data-err-ind="searchProcessType" class="error-msg"></span>
                                        </div>
                                    </div>
                                </div>

                                <div class="row">
                                    <div class="col-xs-12 col-sm-6">
                                        <label class="col-xs-12 col-sm-5 control-label" for="searchFacilityName">Facility Name:</label>
                                        <div class="col-xs-12 col-sm-7">
                                            <input type="text" id="searchFacilityName" name="searchFacilityName" value="${inboxApprovalSearchDto.searchFacilityName}"/>
                                            <span data-err-ind="searchFacilityName" class="error-msg"></span>
                                        </div>

                                    </div>
                                    <div class="col-xs-12 col-sm-6">
                                        <label class="col-xs-12 col-sm-5 control-label" FOR="searchStatus">Approval Status:</label>
                                        <div class="col-xs-12 col-sm-7">
                                            <select id="searchStatus" class="searchStatusDropdown" name="searchStatus">
                                                <option value='<c:out value=""/>' <c:if test="${inboxApprovalSearchDto.searchStatus eq ''}">selected="selected"</c:if>>All</option>
                                                <%--@elvariable id="approvalStatusOps" type="java.util.List<com.ecquaria.cloud.moh.iais.common.dto.SelectOption>"--%>
                                                <c:forEach var="approvalStatusItem" items="${approvalStatusOps}">
                                                    <option value='<c:out value="${approvalStatusItem.value}"/>' <c:if test="${inboxApprovalSearchDto.searchStatus eq approvalStatusItem.value}">selected="selected"</c:if> ><c:out value="${approvalStatusItem.text}"/></option>
                                                </c:forEach>
                                            </select>
                                            <span data-err-ind="searchStatus" class="error-msg"></span>
                                        </div>
                                    </div>
                                </div>

                                <div class="row">
                                    <div class="col-xs-12 col-sm-6">
                                        <label class="col-xs-12 col-sm-5 control-label">Approval Date From:</label>
                                        <div class="col-xs-12 col-sm-7">
                                            <iais:datePicker id="searchStartDateFrom" name="searchStartDateFrom" value="${inboxApprovalSearchDto.searchStartDateFrom}"/>
                                            <span data-err-ind="searchStartDateFrom" class="error-msg"></span>
                                        </div>

                                    </div>
                                    <div class="col-xs-12 col-sm-6">
                                        <label class="col-xs-12 col-sm-5 control-label">Approval Date To:</label>
                                        <div class="col-xs-12 col-sm-7">
                                            <iais:datePicker id="searchStartDateTo" name="searchStartDateTo" value="${inboxApprovalSearchDto.searchStartDateTo}"/>
                                            <span data-err-ind="searchStartDateTo" class="error-msg"></span>
                                        </div>
                                    </div>
                                </div>

                                <div class="row">
                                    <div class="col-xs-12 col-sm-6">
                                        <label class="col-xs-12 col-sm-5 control-label">Expiry Date From:</label>
                                        <div class="col-xs-12 col-sm-7">
                                            <iais:datePicker id="searchExpiryDateFrom" name="searchExpiryDateFrom" value="${inboxApprovalSearchDto.searchExpiryDateFrom}"/>
                                            <span data-err-ind="searchExpiryDateFrom" class="error-msg"></span>
                                        </div>

                                    </div>
                                    <div class="col-xs-12 col-sm-6">
                                        <label class="col-xs-12 col-sm-5 control-label">Expiry Date To:</label>
                                        <div class="col-xs-12 col-sm-7">
                                            <iais:datePicker id="searchExpiryDateTo" name="searchExpiryDateTo" value="${inboxApprovalSearchDto.searchExpiryDateTo}"/>
                                            <span data-err-ind="searchExpiryDateTo" class="error-msg"></span>
                                        </div>
                                    </div>
                                </div>


                                <div class="row text-right text-center-mobile">
                                    <button class="btn btn-secondary" type="reset" id="clearBtn" name="clearBtn">Clear</button>
                                    <button class="btn btn-primary" type="button" id="searchBtn" name="searchBtn">Search</button>
                                </div>
                            </div>

                            <%--@elvariable id="pageInfo" type="sg.gov.moh.iais.egp.bsb.dto.PageInfo"--%>
                            <iais-bsb:Pagination size="${pageInfo.size}" pageNo="${pageInfo.pageNo + 1}" pageAmt="${pageInfo.totalPages}" totalElements="${pageInfo.totalElements}"/>

                            <div class="row">
                                <div class="col-xs-12">
                                    <div class="table-gp">
                                        <table aria-describedby="" class="table">
                                            <thead>
                                            <tr>
                                                <%-- need to use new tag in future --%>
                                                <th scope="col" style="display: none"></th>
                                                <iais:sortableHeader needSort="true" field="approveNo" value="Approval No." isFE="true" style="width:12%"/>
                                                <iais:sortableHeader needSort="true" field="processType" value="Application Sub-Type" isFE="true" style="width:13%"/>
                                                <iais:sortableHeader needSort="true" field="status" value="Approval Status" isFE="true" style="width:11%"/>
                                                <iais:sortableHeader needSort="false" field="facilityName" value="Facility Name" isFE="true" style="width:8%"/>
                                                <iais:sortableHeader needSort="false" field="" value="Facility Address" isFE="true" style="width:14%"/>
                                                <iais:sortableHeader needSort="true" field="approvalStartDate" value="Approval Start Date" isFE="true" style="width:13%"/>
                                                <iais:sortableHeader needSort="true" field="approvalExpiryDate" value="Expiry Date" isFE="true" style="width:12%"/>
                                                <iais:sortableHeader needSort="false" field="" value="Actions" isFE="true" style="width:14%"/>
                                            </tr>
                                            </thead>
                                            <tbody>
                                            <c:choose>
                                                <%--@elvariable id="dataList" type="java.util.List<sg.gov.moh.iais.egp.bsb.dto.inbox.InboxApprovalFacAdminResultDto$ApprovalInfo>"--%>
                                                <c:when test="${empty dataList}">
                                                    <tr>
                                                        <td colspan="6">
                                                            <iais:message key="GENERAL_ACK018" escape="true"/>
                                                        </td>
                                                    </tr>
                                                </c:when>
                                                <c:otherwise>
                                                    <c:forEach var="approval" items="${dataList}" varStatus="status">
                                                        <tr>
                                                            <td>
                                                                <p class="visible-xs visible-sm table-row-title">Approval No.</p>
                                                                <c:out value="${approval.approveNo}"/>
                                                            </td>
                                                            <td>
                                                                <p class="visible-xs visible-sm table-row-title">Application Sub-Type</p>
                                                                <p><iais:code code="${approval.processType}"/></p>
                                                            </td>
                                                            <td>
                                                                <p class="visible-xs visible-sm table-row-title">Approval Status</p>
                                                                <p style="text-align: center"><iais:code code="${approval.status}"/></p>
                                                            </td>
                                                            <td>
                                                                <p class="visible-xs visible-sm table-row-title">Facility Name</p>
                                                                <p style="text-align: center"><c:out value="${approval.facilityName}"/></p>
                                                            </td>
                                                            <td>
                                                                <p class="visible-xs visible-sm table-row-title">Facility Address</p>
                                                                <p><c:out value="${TableDisplayUtil.getOneLineAddress(approval.blkNo, approval.streetName, approval.floorNo, approval.unitNo, approval.postalCode)}"/></p>
                                                            </td>
                                                            <td>
                                                                <p class="visible-xs visible-sm table-row-title">Approval Start Date</p>
                                                                <p><c:out value='${approval.approvalStartDt}'/></p>
                                                            </td>
                                                            <td>
                                                                <p class="visible-xs visible-sm table-row-title">Expiry Date</p>
                                                                <p><c:out value='${approval.approvalExpiryDt}'/></p>
                                                            </td>
                                                            <td>
                                                                <p class="visible-xs visible-sm table-row-title">Actions</p>
                                                                <select id="approvalAction${status.index}" class="approvalActionDropdown${status.index}" name="approvalAction${status.index}" data-action-select="">
                                                                    <option value="#" selected="selected">Select</option>
                                                                    <c:choose>
                                                                        <c:when test="${approval.status eq 'APPRSTA001' and approval.processType eq 'PROTYPE001'}">
                                                                            <option value="/bsb-web/eservice/INTERNET/MohRfcViewFacRegApplication?appId=<iais:mask name='id' value='${approval.id}'/>&processType=<iais:mask name='processType' value='${approval.processType}'/>&approveNo=${approval.approveNo}<c:if test="${approval.status eq 'APPRSTA001'}">&editId=<iais:mask name='editId' value='${approval.id}'/></c:if>">RFC</option>
                                                                        </c:when>
                                                                        <c:when test="${approval.status eq 'APPRSTA001' and approval.processType eq 'PROTYPE002' or approval.processType eq 'PROTYPE003' or approval.processType eq 'PROTYPE004'}">
                                                                            <option value="/bsb-web/eservice/INTERNET/MohRfcViewApprovalPossessApplication?appId=<iais:mask name='id' value='${approval.id}'/>&processType=<iais:mask name='processType' value='${approval.processType}'/>&approveNo=${approval.approveNo}<c:if test="${approval.status eq 'APPRSTA001'}">&editId=<iais:mask name='editId' value='${approval.id}'/></c:if>">RFC</option>
                                                                        </c:when>
                                                                    </c:choose>
                                                                    <c:choose>
                                                                        <c:when test="${approval.status eq 'APPRSTA001' and approval.renewable eq 'Y'}">
                                                                            <option value="/bsb-web/eservice/INTERNET/MohRenewalFacilityRegistration?editId=<iais:mask name='editId' value='${approval.id}'/>">Renewal</option>
                                                                        </c:when>
                                                                        <c:when test="${approval.status eq 'APPRSTA004' and approval.renewable eq 'Y'}">
                                                                            <option value="/bsb-web/eservice/INTERNET/MohDelayRenewalFacilityRegistration?editId=<iais:mask name='editId' value='${approval.id}'/>">Delay Renewal</option>
                                                                        </c:when>
                                                                    </c:choose>

                                                                    <c:if test="${approval.processType eq 'PROTYPE001' and (approval.status eq 'APPRSTA001' or approval.status eq 'APPRSTA007' or approval.status eq 'APPRSTA009' or approval.status eq 'APPRSTA010')}">
                                                                        <option value="/bsb-web/eservice/INTERNET/ApplicantDeRegistrationFacility?approvalId=<iais:mask name='approvalId' value='${approval.id}'/>">DeRegistration</option>
                                                                    </c:if>
                                                                    <c:if test="${(approval.processType eq 'PROTYPE002' or approval.processType eq 'PROTYPE003' or approval.processType eq 'PROTYPE004') and (approval.status eq 'APPRSTA001' or approval.status eq 'APPRSTA007' or approval.status eq 'APPRSTA009' or approval.status eq 'APPRSTA010')}">
                                                                        <option value="/bsb-web/eservice/INTERNET/ApplicantCancellationApproval?approvalId=<iais:mask name='approvalId' value='${approval.id}'/>">Cancellation</option>
                                                                    </c:if>
                                                                </select>
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
                        </form>

                    </div>
                </div>
            </div>
        </div>
    </div>
</div>