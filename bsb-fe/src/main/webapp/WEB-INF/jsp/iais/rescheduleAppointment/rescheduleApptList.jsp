<%@ page import="static sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT" %>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib uri="ecquaria/sop/egov-smc" prefix="egov-smc" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.ecq.com/iais-bsb" prefix="iais-bsb" %>
<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-internet"/>

<link href="<%=WEB_ROOT%>/css/bsb/bsb-common.css" rel="stylesheet"/>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-common.js"></script>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-appointment.js"></script>

<%@include file="dashboard.jsp"%>

<div class="main-content">
    <div class="container">
        <div class="row">
            <div class="col-xs-12">
                <div class="tab-gp dashboard-tab" style="margin-left: 6px;margin-right: -8px;">
<%--                    <div class="tab-content">--%>
                    <br><br><br>
                        <form class="" method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
                            <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
                            <input type="hidden" name="action_type" value="">
                            <input type="hidden" name="action_value" value="">
                            <input type="hidden" name="action_additional" value="">

                            <%--@elvariable id="pageInfo" type="sg.gov.moh.iais.egp.bsb.dto.PageInfo"--%>
                            <iais-bsb:Pagination size="${pageInfo.size}" pageNo="${pageInfo.pageNo + 1}" pageAmt="${pageInfo.totalPages}" totalElements="${pageInfo.totalElements}"/>
                            <%--@elvariable id="dataList" type="java.util.List<sg.gov.moh.iais.egp.bsb.dto.appointment.AppointmentViewDto>"--%>
                            <div class="row">
                                <div class="col-xs-12">
                                    <div class="table-gp">
                                        <table aria-describedby="" class="table">
                                            <thead>
                                            <tr>
                                                <%-- need to use new tag in future --%>
                                                <th scope="col" style="display: none"></th>
                                                <iais:sortableHeader needSort="false" field="" value=" " isFE="false"/>
                                                <iais:sortableHeader needSort="false" field="" value="S/N" />
                                                <iais:sortableHeader needSort="true" field="facilityName" value="Facility Name" isFE="true" style="width:15%"/>
                                                <iais:sortableHeader needSort="true" field="facilityClassification" value="Facility Classification" isFE="true" style="width:18%"/>
                                                <iais:sortableHeader needSort="true" field="address" value="Address" isFE="true"/>
                                                <iais:sortableHeader needSort="true" field="startDate" value="Date and Time of Inspection" isFE="true" style="width:18%"/>
                                            </tr>
                                            </thead>
                                            <tbody>
                                            <c:choose>
                                                <c:when test="${empty dataList}">
                                                    <tr>
                                                        <td colspan="6">
                                                            <iais:message key="GENERAL_ACK018" escape="true"/>
                                                        </td>
                                                    </tr>
                                                </c:when>
                                                <c:otherwise>
                                                    <c:forEach var="entity" items="${dataList}" varStatus="status">
                                                        <tr>
                                                            <td>
                                                                <input name="apptAppId" type="checkbox" value="<iais:mask name='maskedApptAppId' value='${entity.appId}'/>">
                                                            </td>
                                                            <td>
                                                                <p class="visible-xs visible-sm table-row-title">Facility Name</p>
                                                                <p style="text-align: center"><c:out value="${status.index + 1}"/></p>
                                                            </td>
                                                            <td>
                                                                <p class="visible-xs visible-sm table-row-title">Facility Name</p>
                                                                <p style="text-align: center"><c:out value="${entity.facilityName}"/></p>
                                                            </td>
                                                            <td>
                                                                <p class="visible-xs visible-sm table-row-title">Facility Classification</p>
                                                                <p style="text-align: center"><iais:code code="${entity.facilityClassification}"/></p>
                                                            </td>
                                                            <td>
                                                                <p class="visible-xs visible-sm table-row-title">Address</p>
                                                                <p><c:out value="${entity.address}"/></p>
                                                            </td>
                                                            <td>
                                                                <p class="visible-xs visible-sm table-row-title">Date Submitted</p>
                                                                <p><fmt:formatDate value="${entity.inspectionDateAndTime}" pattern="dd/MM/yyyy HH:mm:ss"/></p>
                                                            </td>
                                                        </tr>
                                                    </c:forEach>
                                                </c:otherwise>
                                            </c:choose>
                                            </tbody>
                                        </table>
                                        <br>
                                        <div class="row">
                                            <div class="container">
                                                <div class="col-xs-12 col-md-6 text-left">
                                                    <%--get href from delegator--%>
                                                    <a class="back" href="/bsb-web/eservice/INTERNET/MohBSBInboxMsg"><em class="fa fa-angle-left"></em> Previous</a>
                                                </div>
                                                <div class="form-group">
                                                    <div class="col-xs-12 col-md-6 text-right">
                                                        <c:if test="${not empty dataList}">
                                                            <button type="button" class="btn btn-primary" onclick="pickUpRescheduleAppt()">REQUEST TO RESCHEDULE</button>
                                                        </c:if>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                    <iais:confirm msg="GENERAL_ERR0023"  needCancel="false" callBack="rescheduleApptCancel()" popupOrder="rescheduleApptAlert"/>
                                </div>
                            </div>
                        </form>

<%--                    </div>--%>
                </div>
            </div>
        </div>
    </div>
</div>