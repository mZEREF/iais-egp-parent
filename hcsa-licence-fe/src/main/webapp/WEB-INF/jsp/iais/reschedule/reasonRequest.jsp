
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.AppConsts" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>
<webui:setLayout name="iais-internet"/>
<%@include file="../common/dashboard.jsp"%>
<form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
    <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
    <div class="main-content">
        <br><br><br>

        <div class="container">
            <div class="col-xs-12">
                <div class="components">

                    <br>
                    <div class="table-responsive">

                        <table class="table">
                            <tr >
                                <td class="sorting"><strong>S/N</strong></td>
                                <td class="sorting"><strong>Mode of Service Delivery</strong></td>
                                <td class="sorting"><strong>Service(s)</strong></td>
                                <td class="sorting"><strong>Date and Time of Inspection</strong></td>
                                <td class="sorting"><strong>Reason for Request </strong><strong style="color:#ff0000;">*</strong></td>
                                <td class="sorting" style="width:16%"><strong>New Preferred Date Range </strong><strong style="color:#ff0000;">*</strong></td>
                                <td class="sorting" style="width:14%"><strong>New Date </strong><strong style="color:#ff0000;">*</strong></td>
                            </tr>
                            <tbody>
                            <c:choose>
                                <c:when test="${empty apptViewDtos}">
                                    <tr>
                                        <td colspan="7">
                                            <iais:message key="GENERAL_ACK018" escape="true"></iais:message>
                                        </td>
                                    </tr>
                                </c:when>
                                <c:otherwise>
                                    <c:forEach var="pool" items="${apptViewDtos}" varStatus="status">
                                        <tr >
                                            <td class="row_no"><c:out value="${status.index + 1}"/></td>
                                            <td><c:out value="${pool.address}"/></td>
                                            <td>
                                                <c:forEach var="svcId" items="${pool.svcIds}">
                                                    <iais:service value="${svcId}"></iais:service><br>
                                                </c:forEach>
                                            </td>
                                            <td><fmt:formatDate value="${pool.inspStartDate}" pattern="${AppConsts.DEFAULT_DATE_TIME_FORMAT}" /></td>
                                            <td style="float:left;">
                                                <textarea  name="reason${pool.viewCorrId}" maxlength="500" rows="10" style=" font-weight:normal;"
                                                           cols="50" >${pool.reason}</textarea><br>
                                                <span style="float:left;" id="error_reason${pool.appId}" name="iaisErrorMsg" class="error-msg"></span>
                                            </td>
                                            <td >
                                                From :<iais:datePicker  id = "newStartDate${pool.viewCorrId}" name = "newStartDate${pool.viewCorrId}" dateVal="${pool.specificStartDate}"/>
                                                <br>
                                                To :<iais:datePicker id = "newEndDate${pool.viewCorrId}" name = "newEndDate${pool.viewCorrId}" dateVal="${pool.specificEndDate}"/>
                                                <br>
                                                <span style="float:left;" id="error_newDate${pool.appId}" name="iaisErrorMsg" class="error-msg"></span>

                                            </td>
                                            <td>
                                                <fmt:formatDate value="${pool.inspNewDate}" pattern="${AppConsts.DEFAULT_DATE_TIME_FORMAT}" />
                                                <input type="hidden" name="newDate${pool.viewCorrId}" id="newDate${pool.viewCorrId}" value="${pool.inspNewDate}"/>
                                                <span style="float:left;" id="error_inspDate${pool.appId}" name="iaisErrorMsg" class="error-msg"></span>
                                            </td>

                                        </tr>

                                        <br>
                                    </c:forEach>
                                </c:otherwise>
                            </c:choose>
                            </tbody>
                        </table>
                    </div>
                    <iais:row>
                        <iais:action style="text-align:left;">
                            <div align="left"><span><a  href="/hcsa-licence-web/eservice/INTERNET/MohClientRescheduling"><em class="fa fa-angle-left"> </em> Back</a></span></div>
                        </iais:action>
                        <br>
                        <iais:action style="text-align:right;"  >
                            <button class="btn btn-primary RescheduleButton" type="button"  onclick="doReschedule()">Submit</button>
                        </iais:action>
                    </iais:row>
                </div>
            </div>
        </div>
    </div>
    <h3></h3>
</form>
<%@include file="/WEB-INF/jsp/include/validation.jsp" %>
<%@include file="/WEB-INF/jsp/include/utils.jsp"%>
<script type="text/javascript">

    function doReschedule(){
        showWaiting();
        $("#mainForm").submit();

    }

</script>