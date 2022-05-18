<%@ page import="static sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT" %>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>
<webui:setLayout name="iais-internet"/>

<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-common.js"></script>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-appointment.js"></script>
<%@include file="/WEB-INF/jsp/iais/include/showErrorMsg.jsp" %>
<%@include file="dashboard.jsp"%>
<form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
    <div class="main-content">
        <br><br><br>
        <div class="container">
            <div class="col-xs-12">
                <div class="components">

                    <br>
                    <div class="table-responsive">

                        <table aria-describedby="" class="table">
                            <tr>
                                <th scope="col" class="sorting">S/N</th>
                                <th scope="col" class="sorting">Facility Name</th>
                                <th scope="col" class="sorting">Facility Classification</th>
                                <th scope="col" class="sorting">Address</th>
                                <th scope="col" class="sorting">Date and Time of Inspection</th>
                                <th scope="col" class="sorting">Reason for Request <strong style="color:#ff0000;">*</strong></th>
                                <th scope="col" class="sorting" style="width:16%">New Preferred Date Range <strong style="color:#ff0000;">*</strong></th>
                                <th scope="col" class="sorting" style="width:14%">New Date</th>
                            </tr>
                            <tbody>
                            <%--@elvariable id="appointmentViewList" type="java.util.List<sg.gov.moh.iais.egp.bsb.dto.appointment.AppointmentViewDto>"--%>
                            <c:choose>
                                <c:when test="${empty appointmentViewList}">
                                    <tr>
                                        <td colspan="7">
                                            <iais:message key="GENERAL_ACK018" escape="true"/>
                                        </td>
                                    </tr>
                                </c:when>
                                <c:otherwise>
                                    <c:forEach var="apptView" items="${appointmentViewList}" varStatus="status">
                                        <tr>
                                            <td class="row_no"><c:out value="${status.index + 1}"/></td>
                                            <td><c:out value="${apptView.facilityName}"/></td>
                                            <td><iais:code code="${apptView.facilityClassification}"/></td>
                                            <td><c:out value="${apptView.address}"/></td>
                                            <td><fmt:formatDate value="${apptView.inspectionDateAndTime}" pattern="dd/MM/yyyy HH:mm:ss" /></td>
                                            <td style="float:left;">
                                                <textarea name="reason${apptView.maskedAppId}" maxlength="500" rows="10" style=" font-weight:normal;"
                                                           cols="50" >${apptView.reason}</textarea><br>
                                                <span data-err-ind="reason${apptView.maskedAppId}" class="error-msg"></span>
                                            </td>
                                            <td>
                                                From :<input type="text" autocomplete="off" name="newStartDate${apptView.maskedAppId}" id="newStartDate${apptView.maskedAppId}" data-date-start-date="01/01/1900" placeholder="dd/mm/yyyy" maxlength="10" class="date_picker form-control" value="${apptView.newStartDate}"/>
                                                <span data-err-ind="newStartDate${apptView.maskedAppId}" class="error-msg"></span>
                                                <br>
                                                To :<input type="text" autocomplete="off" name="newEndDate${apptView.maskedAppId}" id="newEndDate${apptView.maskedAppId}" data-date-start-date="01/01/1900" placeholder="dd/mm/yyyy" maxlength="10" class="date_picker form-control" value="${apptView.newEndDate}"/>
                                                <span data-err-ind="newEndDate${apptView.maskedAppId}" class="error-msg"></span>
                                                <br>
                                            </td>
                                            <td>
                                                <fmt:formatDate value="${apptView.inspNewDate}" pattern="dd/MM/yyyy HH:mm:ss" />
                                                <input type="hidden" name="newDate${apptView.maskedAppId}" id="newDate${apptView.maskedAppId}" value="${apptView.inspNewDate}"/>
                                                <span data-err-ind="newDate${apptView.maskedAppId}" class="error-msg"></span>
                                            </td>

                                        </tr>

                                        <br>
                                    </c:forEach>
                                </c:otherwise>
                            </c:choose>
                            </tbody>
                        </table>
                        <div class="row">
                            <div class="container">
                                <div class="col-xs-12 col-md-6 text-left">
                                    <a class="back" href="/bsb-web/eservice/INTERNET/MohBsbRescheduleApptList"><em class="fa fa-angle-left"></em> Previous</a>
                                </div>
                                <div class="form-group">
                                    <div class="col-xs-12 col-md-6 text-right">
                                        <button class="btn btn-primary" type="button" id="submitBtn">Submit</button>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <h3></h3>
</form>