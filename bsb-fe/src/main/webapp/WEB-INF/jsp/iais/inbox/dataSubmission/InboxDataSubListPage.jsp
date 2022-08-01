<%@ page import="static sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT" %>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib uri="ecquaria/sop/egov-smc" prefix="egov-smc" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.ecq.com/iais-bsb" prefix="iais-bsb" %>
<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-internet"/>

<link href="<%=WEB_ROOT%>/css/bsb/bsb-common.css" rel="stylesheet"/>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-common.js"></script>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-inbox.js"></script>

<%@include file="../dashboard/dashboard.jsp" %>

<div class="main-content">
    <div class="container">
        <div class="row">
            <div class="col-xs-12">
                <div class="tab-gp dashboard-tab" style="margin-left: 6px;margin-right: -8px;">
                    <%@ include file="../InnerNavBar.jsp" %>
                    <div class="tab-content">
                        <form class="" method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
                            <div id="inboxFilter" class="tab-search">
                                <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
                                <input type="hidden" name="action_type" value="">
                                <input type="hidden" name="action_value" value="">
                                <input type="hidden" name="action_additional" value="">
                                <%--@elvariable id="inboxDataSubmissionSearchDto" type="sg.gov.moh.iais.egp.bsb.dto.inbox.InboxDataSubSearchDto"--%>
                                <div class="row">
                                    <label for="searchDataSubNo" class="col-sm-3 col-md-2 control-label">Submission ID:</label>
                                    <div class="col-sm-7 col-md-5">
                                        <input type="text" id="searchDataSubNo" name="searchDataSubNo" value="${inboxDataSubmissionSearchDto.searchDataSubNo}"/>
                                    </div>
                                </div>
                                <div class="row" style="margin-bottom: 15px">
                                    <label for="searchFacilityName" class="col-sm-3 col-md-2 control-label">Facility name:</label>
                                    <div class="col-sm-7 col-md-5">
                                        <select name="searchFacilityName" class="searchFacilityNameDropdown" id="searchFacilityName">
                                            <option value='<c:out value=""/>' <c:if test="${inboxDataSubmissionSearchDto.searchFacName eq ''}">selected="selected"</c:if>>All</option>
                                            <%--@elvariable id="facilityName" type="java.util.List<com.ecquaria.cloud.moh.iais.common.dto.SelectOption>"--%>
                                            <c:forEach var="facNameItem" items="${facilityName}">
                                                <option value='<c:out value="${facNameItem.value}"/>' <c:if test="${inboxDataSubmissionSearchDto.searchFacName eq facNameItem.value}">selected="selected"</c:if> ><c:out value="${facNameItem.text}"/></option>
                                            </c:forEach>
                                        </select>
                                    </div>
                                </div>
                                <div class="row" style="margin-bottom: 15px">
                                    <label for="searchType" class="col-sm-3 col-md-2 control-label">Type:</label>
                                    <div class="col-sm-7 col-md-5">
                                        <select name="searchType" class="searchTypeDropdown" id="searchType">
                                            <option value='<c:out value=""/>' <c:if test="${inboxDataSubmissionSearchDto.searchType eq ''}">selected="selected"</c:if>>All</option>
                                            <%--@elvariable id="submissionTypeOps" type="java.util.List<com.ecquaria.cloud.moh.iais.common.dto.SelectOption>"--%>
                                            <c:forEach var="typeItem" items="${submissionTypeOps}">
                                                <option value='<c:out value="${typeItem.value}"/>' <c:if test="${inboxDataSubmissionSearchDto.searchType eq typeItem.value}">selected="selected"</c:if> ><c:out value="${typeItem.text}"/></option>
                                            </c:forEach>
                                        </select>
                                    </div>
                                </div>
                                <div class="row" style="margin-bottom: 15px">
                                    <label for="searchStatus" class="col-sm-3 col-md-2 control-label">Status:</label>
                                    <div class="col-sm-7 col-md-5">
                                        <select name="searchStatus" class="searchStatusDropdown" id="searchStatus">
                                            <option value='<c:out value=""/>' <c:if test="${inboxDataSubmissionSearchDto.searchStatus eq ''}">selected="selected"</c:if>>All</option>
                                            <%--@elvariable id="submissionStatusOps" type="java.util.List<com.ecquaria.cloud.moh.iais.common.dto.SelectOption>"--%>
                                            <c:forEach var="statusItem" items="${submissionStatusOps}">
                                                <option value='<c:out value="${statusItem.value}"/>' <c:if test="${inboxDataSubmissionSearchDto.searchStatus eq statusItem.value}">selected="selected"</c:if> ><c:out value="${statusItem.text}"/></option>
                                            </c:forEach>
                                        </select>
                                    </div>
                                </div>
                                <div class="row text-right text-center-mobile">
                                    <button class="btn btn-secondary" type="button" id="clearDataSubBtn" name="clearBtn">Clear
                                    </button>
                                    <button class="btn btn-primary" type="button" id="searchBtn" name="searchBtn">
                                        Search
                                    </button>
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
                                                <iais:sortableHeader needSort="true" field="submissionNo" value="Submission ID" isFE="true" style="width: 15%"/>
                                                <iais:sortableHeader needSort="true" field="type" value="Type" isFE="true" style="width: 18%"/>
                                                <iais:sortableHeader needSort="true" field="status" value="Status" isFE="true" style="width: 15%"/>
                                                <iais:sortableHeader needSort="true" field="facility.facilityName" value="Facility" isFE="true" style="width: 10%"/>
                                                <iais:sortableHeader needSort="false" field="" value="Facility Address" isFE="true" style="width: 15%"/>
                                                <iais:sortableHeader needSort="true" field="createdAt" value="Submitted On" isFE="true" style="width: 15%"/>
                                                <iais:sortableHeader needSort="false" field="" value="Actions" isFE="true"/>
                                            </tr>
                                            </thead>
                                            <tbody>
                                            <c:choose>
                                                <%--@elvariable id="dataList" type="java.util.List<sg.gov.moh.iais.egp.bsb.entity.Application>"--%>
                                                <c:when test="${empty dataList}">
                                                    <tr>
                                                        <td colspan="6">
                                                            <iais:message key="GENERAL_ACK018" escape="true"/>
                                                        </td>
                                                    </tr>
                                                </c:when>
                                                <c:otherwise>
                                                    <c:forEach var="item" items="${dataList}" varStatus="status">
                                                        <tr>
                                                            <td>
                                                                <p class="visible-xs visible-sm table-row-title">Submission ID</p>
                                                                <p style="text-align: center"><a id="viewSubmission" href="/bsb-web/eservice/INTERNET/ViewDataSubmission?submissionId=<iais:mask name='id' value='${item.id}'/>"><c:out value="${item.submissionNo}"/></a></p>
                                                            </td>
                                                            <td>
                                                                <p class="visible-xs visible-sm table-row-title">Type</p>
                                                                <p style="text-align: center"><iais:code code="${item.type}"/></p>
                                                            </td>
                                                            <td>
                                                                <p class="visible-xs visible-sm table-row-title">Status</p>
                                                                <p><iais:code code="${item.status}"/></p>
                                                            </td>
                                                            <td>
                                                                <p class="visible-xs visible-sm table-row-title">Facility</p>
                                                                <p><c:out value="${item.facilityName}"/></p>
                                                            </td>
                                                            <td>
                                                                <p class="visible-xs visible-sm table-row-title">Facility Address</p>
                                                                <p><c:out value="${item.facilityAddress}"/></p>
                                                            </td>
                                                            <td>
                                                                <p class="visible-xs visible-sm table-row-title">Submitted On</p>
                                                                <p><fmt:formatDate value="${item.submittedOn}" pattern="dd/MM/yyyy HH:mm:ss"/></p>
                                                            </td>
                                                            <td>
                                                                <c:if test="${item.status ne 'DATASTA002'}">
                                                                    <select id="appAction${status.index}" class="appAction${status.index}" name="appAction${status.index}" data-action-select="">
                                                                        <option value="#" selected="selected">Select</option>
                                                                        <%--The application will be in a “non-approved” or “non-rejected” stage--%>
                                                                            <%--todo: check this app status and logic--%>
                                                                        <c:if test="${item.applicationId ne null and (item.applicationStatus ne 'BSBAPST006' and item.applicationStatus ne 'BSBAPST031')}">
                                                                            <option value="/bsb-web/eservice/INTERNET/BsbWithDrawn?withdrawnAppId=<iais:mask name='id' value='${item.applicationId}'/>&from=dataSubmission">Withdrawn</option>
                                                                        </c:if>
                                                                    </select>
                                                                    <p class="visible-xs visible-sm table-row-title">Actions</p>
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
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>