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
                                <%--@elvariable id="inbox" type="sg.gov.moh.iais.egp.bsb.dto.inbox.InboxDataSubSearchDto"--%>
                                <div class="row">
                                    <label for="referenceNo" class="col-sm-3 col-md-2 control-label">Incident Reference No:</label>
                                    <div class="col-sm-7 col-md-5">
                                        <input type="text" id="referenceNo" name="referenceNo" value="<c:out value="${inboxSearchDto.referenceNo}"/>"/>
                                    </div>
                                </div>
                                <div class="row" style="margin-bottom: 15px">
                                    <label for="facilityName" class="col-sm-3 col-md-2 control-label">Facility name:</label>
                                    <div class="col-sm-7 col-md-5">
                                        <select name="facilityName" class="facilityNameDropdown" id="facilityName">
                                            <option value='<c:out value=""/>' <c:if test="${inboxSearchDto.facilityName eq ''}">selected="selected"</c:if>>All</option>
                                            <%--@elvariable id="facilityName" type="java.util.List<com.ecquaria.cloud.moh.iais.common.dto.SelectOption>"--%>
                                            <c:forEach var="item" items="${facilityName}">
                                                <option value='<c:out value="${item.value}"/>' <c:if test="${inboxSearchDto.facilityName eq item.value}">selected="selected"</c:if> ><c:out value="${item.text}"/></option>
                                            </c:forEach>
                                        </select>
                                    </div>
                                </div>
                                <div class="row" style="margin-bottom: 15px">
                                    <label for="incidentType" class="col-sm-3 col-md-2 control-label">Type of Incident(s)</label>
                                    <div class="col-sm-7 col-md-5">
                                        <select name="incidentType" class="incidentTypeDropdown" id="incidentType">
                                            <option value='<c:out value=""/>' <c:if test="${inboxSearchDto.incidentType eq ''}">selected="selected"</c:if>>All</option>
                                            <%--@elvariable id="submissionTypeOps" type="java.util.List<com.ecquaria.cloud.moh.iais.common.dto.SelectOption>"--%>
                                            <c:forEach var="item" items="${incidentTypeOps}">
                                                <option value='<c:out value="${item.value}"/>' <c:if test="${inboxSearchDto.incidentType eq item.value}">selected="selected"</c:if> ><c:out value="${item.text}"/></option>
                                            </c:forEach>
                                        </select>
                                    </div>
                                </div>
                                <div class="row" style="margin-bottom: 15px">
                                    <label for="incidentDate" class="col-sm-3 col-md-2 control-label">Date of Incident:</label>
                                    <div class="col-sm-7 col-md-5">
                                        <input type="text" autocomplete="off" name="incidentDate" id="incidentDate" data-date-start-date="01/01/1900" value="<c:out value="${inboxSearchDto.incidentDate}"/>" placeholder="dd/mm/yyyy" maxlength="10" class="date_picker form-control"/>
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
                                                <iais:sortableHeader needSort="false" field="incidentNo" value="Incident Reference No" isFE="true" style="width: 15%"/>
                                                <iais:sortableHeader needSort="false" field="incidentType" value="Type of Incident(s)" isFE="true" style="width: 10%"/>
                                                <iais:sortableHeader needSort="false" field="facilityName" value="Facility Name" isFE="true" style="width: 10%"/>
                                                <iais:sortableHeader needSort="false" field="facAddress" value="Facility Address" isFE="true" style="width: 15%"/>
                                                <iais:sortableHeader needSort="true" field="incidentDate" value="Date of Incident" isFE="true" style="width: 15%"/>
                                                <iais:sortableHeader needSort="false" field="batName" value="Name of Agent or Toxin Involved" isFE="true" style="width: 15%"/>
                                                <iais:sortableHeader needSort="false" field="" value="Actions" isFE="true" style="width: 20%"/>
                                            </tr>
                                            </thead>
                                            <tbody>
                                            <c:choose>
                                                <%--@elvariable id="dataList" type="java.util.List<sg.gov.moh.iais.egp.bsb.entity.Application>"--%>
                                                <c:when test="${empty inboxResultDto}">
                                                    <tr>
                                                        <td colspan="6">
                                                            <iais:message key="GENERAL_ACK018" escape="true"/>
                                                        </td>
                                                    </tr>
                                                </c:when>
                                                <c:otherwise>
                                                    <c:forEach var="item" items="${inboxResultDto}" varStatus="status">
                                                        <tr>
                                                            <td>
                                                                <p class="visible-xs visible-sm table-row-title">Incident Reference No</p>
                                                                <p><a id="viewIncident" href="/bsb-web/eservice/INTERNET/ViewIncidentNotificaiton?incidentId=<iais:mask name='id' value='${item.incidentId}'/>"><c:out value="${item.referenceNo}"/></a></p>
                                                            </td>
                                                            <td>
                                                                <p class="visible-xs visible-sm table-row-title">Type of Incident(s)</p>
                                                                <p style="text-align: center"><iais:code code="${item.incidentType}"/></p>
                                                            </td>
                                                            <td>
                                                                <p class="visible-xs visible-sm table-row-title">Facility name</p>
                                                                <p><c:out value="${item.facilityName}"/></p>
                                                            </td>
                                                            <td>
                                                                <p class="visible-xs visible-sm table-row-title">Facility Address</p>
                                                                <p><c:out value="${item.facAddress}"/></p>
                                                            </td>
                                                            <td>
                                                                <p class="visible-xs visible-sm table-row-title">Date of Incident</p>
                                                                <p><c:out value="${item.incidentEntityDate}"/></p>
                                                            </td>
                                                            <td>
                                                                <p class="visible-xs visible-sm table-row-title">Name of Agent or Toxin Involved</p>
                                                                <p><c:out value="${item.batName}"/></p>
                                                            </td>
                                                            <td>
                                                                <select id="appAction${status.index}" class="appActionDropdown${status.index}" name="appAction${status.index}" data-action-select="">
                                                                    <option value="" selected="selected">Select</option>
                                                                        <%--The application will be in a “non-approved” or “non-rejected” stage--%>
                                                                    <c:if test="${item.haveInvest eq 'Y'}">
                                                                        <option value="/bsb-web/eservice/INTERNET/ViewInvestReport?referenceNo=<iais:mask name='referNo' value='${item.referenceNo}'/>">Investigation Report</option>
                                                                    </c:if>
                                                                    <c:if test="${item.haveFollowup1A eq 'Y'}">
                                                                        <option value="/bsb-web/eservice/INTERNET/ViewFollowup1A?referenceNo=<iais:mask name='referNo' value='${item.referenceNo}'/>">Follow-up 1A</option>
                                                                    </c:if>
                                                                    <c:if test="${item.haveFollowup1A eq 'Y'}">
                                                                        <option value="/bsb-web/eservice/INTERNET/ViewFollowup1B?referenceNo=<iais:mask name='referNo' value='${item.referenceNo}'/>">Follow-up 1B</option>
                                                                    </c:if>
                                                                </select>
                                                                <p class="visible-xs visible-sm table-row-title">Actions</p>
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