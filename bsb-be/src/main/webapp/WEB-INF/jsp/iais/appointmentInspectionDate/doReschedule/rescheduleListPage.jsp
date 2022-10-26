<%@ page import="static sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT" %>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
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
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-appointment.js"></script>
<%@include file="/WEB-INF/jsp/iais/include/showErrorMsg.jsp"%>


<div class="main-content" style="min-height: 73vh;">
    <form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
        <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
        <input type="hidden" name="action_type" value="">
        <input type="hidden" name="action_value" value="">
        <input type="hidden" name="action_additional" value="">
        <input type="hidden" name="maskedAppId" value="">

        <div class="col-xs-12">
            <div class="center-content">
                <div class="bg-title">
                    <h2>
                        <span>Scheduled Appointments</span>
                    </h2>
                </div>
                <div class="form-horizontal">
                    <%--@elvariable id="searchDto" type="sg.gov.moh.iais.egp.bsb.dto.appointment.ApptSearchDto"--%>
                    <div id="beInboxFilter">
                        <div class="col-xs-12 col-sm-12">
                            <label for="searchWorkingGroup" class="col-sm-5 col-md-5 control-label">Working Group</label>
                            <div class="col-sm-7 col-md-5">
                                <select id="searchWorkingGroup" class="searchWorkingGroupDropdown" name="searchWorkingGroup">
                                    <option value="inspection">Inspection Team</option>
                                </select>
                            </div>
                        </div>
                        <div class="col-xs-12 col-sm-12">
                            <label for="searchInspectorName" class="col-sm-5 col-md-5 control-label">Inspector Name</label>
                            <div class="col-sm-7 col-md-5">
                                <select id="searchInspectorName" class="searchInspectorNameDropdown" name="searchInspectorName">
                                    <option value="">Please Select</option>
                                </select>
                            </div>
                        </div>
                        <div class="col-xs-12 col-sm-12" style="text-align:right;"><%-- div for btn --%>
                            <button class="btn btn-secondary" type="button" id="clearBtn" name="clearBtn">Clear</button>
                            <button class="btn btn-primary" type="button" id="searchBtn" name="searchBtn">Search</button>
                        </div>
                    </div>
                </div>
                <h3>
                    <span>Search Results</span>
                </h3>
                <%--@elvariable id="pageInfo" type="sg.gov.moh.iais.egp.bsb.dto.PageInfo"--%>
                <iais-bsb:Pagination size="${pageInfo.size}" pageNo="${pageInfo.pageNo + 1}" pageAmt="${pageInfo.totalPages}" totalElements="${pageInfo.totalElements}"/>


                <div class="table-gp">
                    <table aria-describedby="" class="table application-group" style="border-collapse:collapse;">
                        <thead>
                        <tr>
                            <th scope="col" style="display: none"></th>
                            <iais:sortableHeader needSort="false" field="" value="S/N"/>
                            <iais:sortableHeader needSort="false" field="" value="Facility Name"/>
                            <iais:sortableHeader needSort="false" field="" value="Inspector(s)"/>
                            <iais:sortableHeader needSort="false" field="" value="Date and Time of Inspection"/>
                            <iais:sortableHeader needSort="false" field="" value="Type of Task"/>
                            <iais:sortableHeader needSort="false" field="" value="Facility Classification"/>
                            <iais:sortableHeader needSort="false" field="" value="Action"/>
                        </tr>
                        </thead>
                        <c:choose>
                            <%--@elvariable id="dataList" type="java.util.List<sg.gov.moh.iais.egp.bsb.dto.appointment.AppointmentViewDto>"--%>
                            <c:when test="${empty dataList}">
                                <tr>
                                    <td colspan="7">
                                        <iais:message key="GENERAL_ACK018" escape="true"/>
                                    </td>
                                </tr>
                            </c:when>
                            <c:otherwise>
                                <c:forEach var="entity" items="${dataList}" varStatus="status">
                                    <tr style="display: table-row;">
                                        <td>
                                            <p class="visible-xs visible-sm table-row-title">S/N</p>
                                            <p>${(status.index + 1) + (pageInfo.pageNo) * pageInfo.size}</p>
                                        </td>
                                        <td>
                                            <p class="visible-xs visible-sm table-row-title">Facility Name</p>
                                            <p>
                                                <c:out value="${entity.facilityName}"/>
                                            </p>
                                        </td>
                                        <td>
                                            <p class="visible-xs visible-sm table-row-title">Inspector(s)</p>
                                            <p>
                                                <c:if test="${not empty entity.inspectors}">
                                                    <c:forEach var="inspector" items="${entity.inspectors}">
                                                        <c:out value="${inspector}"/><br>
                                                    </c:forEach>
                                                </c:if>
                                            </p>
                                        </td>
                                        <td>
                                            <p class="visible-xs visible-sm table-row-title">Date and Time of Inspection</p>
                                            <p><fmt:formatDate value="${entity.inspectionDateAndTime}" pattern="dd/MM/yyyy"/></p>
                                        </td>
                                        <td>
                                            <p class="visible-xs visible-sm table-row-title">Type of Task</p>
                                            <p>Inspection</p>
                                        </td>
                                        <td>
                                            <p class="visible-xs visible-sm table-row-title">Facility Classification</p>
                                            <p>
                                                <iais:code code="${entity.facilityClassification}"/>
                                            </p>
                                        </td>
                                        <td>
                                            <p class="visible-xs visible-sm table-row-title">Action</p>
                                            <button type="button" class="btn btn-default btn-sm" onclick="rescheduleAppointment('<iais:mask name="maskedAppId" value="${entity.appId}"/>')">Reschedule</button>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </c:otherwise>
                        </c:choose>
                    </table>
                </div>
                <iais-bsb:Pagination size="${pageInfo.size}" pageNo="${pageInfo.pageNo + 1}" pageAmt="${pageInfo.totalPages}" totalElements="${pageInfo.totalElements}"/>
            </div>
        </div>
    </form>
</div>
