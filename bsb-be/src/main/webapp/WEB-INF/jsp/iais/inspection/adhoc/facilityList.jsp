<%@ page import="static sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT" %>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib uri="http://www.ecq.com/iais-bsb" prefix="iais-bsb" %>

<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-intranet"/>

<link href="<%=WEB_ROOT%>/css/bsb/bsb-common.css" rel="stylesheet"/>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-common.js"></script>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-adhoc-inspection.js"></script>
<%@include file="/WEB-INF/jsp/iais/include/showErrorMsg.jsp"%>

<div class="main-content" style="min-height: 73vh;">
    <form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
        <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
        <input type="hidden" name="action_type" value="">
        <input type="hidden" name="action_value" value="">
        <input type="hidden" name="action_additional" value="">

        <div class="col-xs-12">
            <div class="center-content">
                <%@include file="searchSection.jsp"%>
                <h3 style="margin-top: 20px">
                    <span>Search Results</span>
                </h3>
                <%--@elvariable id="pageInfo" type="sg.gov.moh.iais.egp.bsb.dto.PageInfo"--%>
                <iais-bsb:Pagination size="${pageInfo.size}" pageNo="${pageInfo.pageNo + 1}" pageAmt="${pageInfo.totalPages}" totalElements="${pageInfo.totalElements}"/>

                <div class="table-gp">
                    <table aria-describedby="" class="table application-group" style="border-collapse:collapse;">
                        <thead>
                        <tr>
                            <th scope="col" style="display: none"></th>
                            <iais:sortableHeader needSort="false" field="" value=" "/>
                            <iais:sortableHeader needSort="false" field="" value="S/N"/>
                            <iais:sortableHeader needSort="true" field="facilityNo" value="Facility No."/>
                            <iais:sortableHeader needSort="true" field="facilityName" value="Facility Name"/>
                            <iais:sortableHeader needSort="true" field="facilityClassification" value="Facility Classification"/>
                            <iais:sortableHeader needSort="false" field="" value="Facility Activity Type"/>
                            <iais:sortableHeader needSort="true" field="status" value="Facility Status"/>
                        </tr>
                        </thead>
                        <c:choose>
                            <%--@elvariable id="dataList" type="java.util.List<sg.gov.moh.iais.egp.bsb.dto.facilitymanagement.FacilityManagementDisplayDto>"--%>
                            <c:when test="${empty dataList}">
                                <tr>
                                    <td colspan="7">
                                        <iais:message key="GENERAL_ACK018" escape="true"/>
                                    </td>
                                </tr>
                            </c:when>
                            <c:otherwise>
                                <c:forEach var="pool" items="${dataList}" varStatus="status">
                                    <tr style="display: table-row;">
                                        <td>
                                            <p class="visible-xs visible-sm table-row-title"> </p>
                                            <p><input type="radio" value="<iais:mask name="facId" value="${pool.id}"/>" name="facId"></p>
                                        </td>
                                        <td>
                                            <p class="visible-xs visible-sm table-row-title">S/N</p>
                                            <p>${(status.index + 1) + (pageInfo.pageNo) * pageInfo.size}</p>
                                        </td>
                                        <td>
                                            <p class="visible-xs visible-sm table-row-title">Facility No.</p>
                                            <p><a onclick="viewFacility('<iais:mask name="facId" value="${pool.id}"/>')"><c:out value="${pool.facilityNo}"/></a></p>
                                        </td>
                                        <td>
                                            <p class="visible-xs visible-sm table-row-title">Facility Name</p>
                                            <p><c:out value="${pool.name}"/></p>
                                        </td>
                                        <td>
                                            <p class="visible-xs visible-sm table-row-title">Facility Classification</p>
                                            <p><iais:code code="${pool.classification}"/></p>
                                        </td>
                                        <td>
                                            <p class="visible-xs visible-sm table-row-title">Facility Activity Type</p>
                                            <p>
                                                <ul>
                                                <c:forEach var="activityType" items="${pool.facilityActivityTypes}">
                                                    <li><iais:code code="${activityType}"/></li>
                                                </c:forEach>
                                                </ul>
                                            </p>
                                        </td>
                                        <td>
                                            <p class="visible-xs visible-sm table-row-title">Facility Status</p>
                                            <p><iais:code code="${pool.status}"/></p>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </c:otherwise>
                        </c:choose>
                    </table>
                    <div style="text-align:right;">
                        <button type="button" class="btn btn-primary" id="adhocInspection">ADHOC INSPECTION</button>
                    </div>
                </div>
            </div>
        </div>
        <iais:confirm msg="GENERAL_ERR0023"  needCancel="false" callBack="adhocInspectionCancel()" popupOrder="adhocInspectionAlert"/>
    </form>
</div>
