<%@ page import="static sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT" %>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
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
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-task.js"></script>
<%@include file="/WEB-INF/jsp/iais/include/showErrorMsg.jsp"%>
<div class="main-content" style="min-height: 73vh;">
    <form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
        <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
        <input type="hidden" name="action_type" value="">
        <input type="hidden" name="action_value" value="">
        <input type="hidden" name="crud_action_type" value="">
        <input type="hidden" name="action_additional" value="">
        <div class="col-xs-12">
            <div class="center-content">
                <iais-bsb:Pagination size="${pageInfo.size}" pageNo="${pageInfo.pageNo + 1}" pageAmt="${pageInfo.totalPages}" totalElements="${pageInfo.totalElements}"/>
                <div class="table-gp">
                    <table aria-describedby="" class="table application-group" style="border-collapse:collapse;">
                        <thead>
                        <tr>
                            <iais:sortableHeader needSort="false" field="" value="S/N"/>
                            <iais:sortableHeader needSort="false" field="applicationNo" value="Application No."/>
                            <iais:sortableHeader needSort="false" field="appType" value="Application Type"/>
                            <iais:sortableHeader needSort="false" field="facilityName" value="Facility Name/Address"/>
                            <iais:sortableHeader needSort="false" field="facilityType" value="Facility Type"/>
                            <iais:sortableHeader needSort="false" field="processType" value="Process Type"/>
                            <iais:sortableHeader needSort="false" field="notCreated" value="Agents/Toxins"/>
                            <iais:sortableHeader needSort="false" field="applicationDt" value="Application Date"/>
                            <iais:sortableHeader needSort="false" field="expiryDt" value="Facility/Application Expiry Date"/>
                            <iais:sortableHeader needSort="false" field="status" value="Application Status"/>
                        </tr>
                        </thead>
                        <c:choose>
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
                                            <p class="visible-xs visible-sm table-row-title">Application No.</p>
                                            <p><a onclick="doProcess('<iais:mask name="appId" value="${entity.id}"/>','${entity.status}')"><c:out value="${entity.applicationNo}"/></a></p>
                                        </td>
                                        <td>
                                            <p class="visible-xs visible-sm table-row-title">Application Type</p>
                                            <p><iais:code code="${entity.appType}"/></p>
                                        </td>
                                        <td>
                                            <p class="visible-xs visible-sm table-row-title">Facility Name/Address</p>
                                            <p><c:out value="${entity.facility.facilityName}"/></p>
                                        </td>
                                        <td>
                                            <p class="visible-xs visible-sm table-row-title">Facility Type</p>
                                            <p><iais:code code="${entity.facility.facilityType}"/></p>
                                        </td>
                                        <td>
                                            <p class="visible-xs visible-sm table-row-title">Process Type</p>
                                            <p><iais:code code="${entity.processType}"/></p>
                                        </td>
                                        <td>
                                            <p class="visible-xs visible-sm table-row-title">Agents/Toxins</p>
                                            <p><c:out value=""/></p>
                                        </td>
                                        <td>
                                            <p class="visible-xs visible-sm table-row-title">Application Date</p>
                                            <p><fmt:formatDate value="${entity.applicationDt}" pattern="dd/MM/yyyy"/></p>
                                        </td>
                                        <td>
                                            <p class="visible-xs visible-sm table-row-title">Facility/Application Expiry Date</p>
                                            <p><fmt:formatDate value="${entity.facility.expiryDt}" pattern="dd/MM/yyyy"/></p>
                                        </td>
                                        <td>
                                            <p class="visible-xs visible-sm table-row-title">Application Status</p>
                                            <p><iais:code code="${entity.status}"/></p>
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
        <input name="appId" id="appId" value="" hidden>
    </form>
</div>
<script>
    function doProcess(id,status){
        $("#appId").val(id);
        if(status == "BSBAPST001" || status == "BSBAPST002" || status == "BSBAPST003"){
            $("[name='crud_action_type']").val("mohOfficersProcess");
            $("#mainForm").submit();
        }
    }
</script>
