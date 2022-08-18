<%@ page import="static sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT" %>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib uri="http://www.ecq.com/iais-bsb" prefix="iais-bsb" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.utils.MaskUtil" %>

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
        <input type="hidden" name="action_additional" value="">

        <div class="col-xs-12">
            <div class="center-content">
                <%@include file="../searchSection.jsp"%>
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
                            <iais:sortableHeader needSort="true" field="application.applicationNo" value="Application No."/>
                            <iais:sortableHeader needSort="true" field="application.appType" value="Application Type"/>
                            <iais:sortableHeader needSort="true" field="application.processType" value="Application Sub-Type"/>
                            <iais:sortableHeader needSort="true" field="application.submissionType" value="Submission Type"/>
                            <iais:sortableHeader needSort="true" field="application.status" value="Application Status"/>
                            <iais:sortableHeader needSort="true" field="application.applicationDt" value="Application Date"/>
                            <iais:sortableHeader needSort="false" field="Duty Officer" value="Duty Officer"/>
                        </tr>
                        </thead>
                        <c:choose>
                            <%--@elvariable id="dataList" type="java.util.List<sg.gov.moh.iais.egp.bsb.dto.task.ResultDto>"--%>
                            <c:when test="${empty dataList}">
                                <tr>
                                    <td colspan="7">
                                        <iais:message key="GENERAL_ACK018" escape="true"/>
                                    </td>
                                </tr>
                            </c:when>
                            <c:otherwise>
                                <c:forEach var="pool" items="${dataList}" varStatus="status">
                                    <c:set var="entity" value="${pool.taskDto}"/>
                                    <c:set var="divId" value="${(status.index + 1) + (pageInfo.pageNo - 1) * pageInfo.size}"/>
                                    <c:set var="maskedTaskId" value='${MaskUtil.maskValue("id", entity.id)}'/>
                                    <tr style="display: table-row;" id="advfilter${divId}">
                                        <td>
                                            <p class="visible-xs visible-sm table-row-title">S/N</p>
                                            <p>${(status.index + 1) + (pageInfo.pageNo) * pageInfo.size}</p>
                                        </td>
                                        <td>
                                            <p class="visible-xs visible-sm table-row-title">Application No.</p>
                                            <p style="width: 180px;">
                                                <c:out value="${entity.application.applicationNo}"/><a href="javascript:void(0);" class="accordion-toggle  collapsed" style="float: right"
                                                                                                       data-toggle="collapse" aria-expanded="false"
                                                                                                       data-target="#dropdown${divId}"></a>
                                            </p>
                                        </td>
                                        <td>
                                            <p class="visible-xs visible-sm table-row-title">Application Type</p>
                                            <p><iais:code code="${entity.application.appType}"/></p>
                                        </td>
                                        <td>
                                            <p class="visible-xs visible-sm table-row-title">Application Sub-Type</p>
                                            <p><iais:code code="${entity.application.processType}"/></p>
                                        </td>
                                        <td>
                                            <p class="visible-xs visible-sm table-row-title">Submission Type</p>
                                            <p><iais:code code="${entity.application.submissionType ne null ? entity.application.submissionType : 'N/A'}"/></p>
                                        </td>
                                        <td>
                                            <p class="visible-xs visible-sm table-row-title">Application Status</p>
                                            <p><iais:code code="${entity.application.status}"/></p>
                                        </td>
                                        <td>
                                            <p class="visible-xs visible-sm table-row-title">Application Date</p>
                                            <p><fmt:formatDate value="${entity.application.applicationDt}" pattern="dd/MM/yyyy"/></p>
                                        </td>
                                        <td>
                                            <p class="visible-xs visible-sm table-row-title">Duty Officer</p>
                                            <p>"N/A"</p>
                                        </td>
                                    </tr>
                                    <tr style="background-color: #F3F3F3;" class="p" id="advfilterson${divId}">
                                        <td colspan="9" style="padding: 0 8px !important;">
                                            <div class="accordian-body p-3 collapse" id="dropdown${divId}">
                                                <table aria-describedby=""  class="table application-item" style="background-color: #F3F3F3;margin-bottom:0;" >
                                                    <thead>
                                                    <tr>
                                                        <th scope="col" style="width: 15%">Application No.</th>
                                                        <th scope="col" style="width: 25%">Facility Classification</th>
                                                        <th scope="col" style="width: 15%">Facility Name</th>
                                                        <th scope="col" style="width: 15%">Facility Address</th>
                                                        <th scope="col" style="width: 10%">Inspection Date</th>
                                                        <th scope="col" style="width: 10%">Validity End Date</th>
                                                        <th scope="col" style="width: 10%">Status</th>
                                                    </tr>
                                                    </thead>
                                                    <tbody>
                                                    <tr style="color: black">
                                                        <td>
                                                            <p class="visible-xs visible-sm table-row-title">Application No.</p>
                                                            <p>
                                                                <a href="/bsb-web/eservicecontinue/INTRANET/MultiAssignInspectionTask?appId=<iais:mask name='id' value='${entity.application.id}'/>&taskId=<iais:mask name='id' value='${entity.id}'/>&OWASP_CSRFTOKEN=null&from=app"><c:out value="${entity.application.applicationNo}"/></a>
                                                            </p>
                                                        </td>
                                                        <td>
                                                            <p class="visible-xs visible-sm table-row-title">Facility Classification</p>
                                                            <p><iais:code code="${pool.facClassification ne null ? pool.facClassification : 'N/A'}"/></p>
                                                        </td>
                                                        <td>
                                                            <p class="visible-xs visible-sm table-row-title">Facility Name</p>
                                                            <p><c:out value="${pool.facName ne null ? pool.facName : 'N/A'}"/></p>
                                                        </td>
                                                        <td>
                                                            <p class="visible-xs visible-sm table-row-title">Facility Address</p>
                                                            <p><c:out value="${pool.facAddress ne null ? pool.facAddress : 'N/A'}"/></p>
                                                        </td>
                                                        <td>
                                                            <p class="visible-xs visible-sm table-row-title">Inspection Date</p>
                                                            <p><c:out value="${pool.inspectionDate ne null ? pool.inspectionDate : 'N/A'}"/></p>
                                                        </td>
                                                        <td>
                                                            <p class="visible-xs visible-sm table-row-title">Validity End Date</p>
                                                            <p><c:out value="${pool.validityEndDate ne null ? pool.validityEndDate : 'N/A'}"/></p>
                                                        </td>
                                                        <td>
                                                            <p class="visible-xs visible-sm table-row-title">Status</p>
                                                            <p><c:out value="${pool.status ne null ? pool.status : 'N/A'}"/></p>
                                                        </td>
                                                    </tr>
                                                    </tbody>
                                                </table>
                                            </div>
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
        <iais:confirm msg="GENERAL_ERR0023"  needCancel="false" callBack="multiAssignCancel()" popupOrder="multiAssignAlert"/>
    </form>
</div>
