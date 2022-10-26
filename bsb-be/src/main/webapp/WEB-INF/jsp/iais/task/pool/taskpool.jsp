<%@ page import="static sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT" %>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
        <input type="hidden" name="appId" value="">



        <div class="col-xs-12">
            <div class="center-content">
                <%@include file="../searchSection.jsp"%>
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
                            <iais:sortableHeader needSort="false" field="" value="S/N"/>
                            <iais:sortableHeader needSort="true" field="application.applicationNo" value="Application No."/>
                            <iais:sortableHeader needSort="true" field="application.appType" value="Application Type"/>
                            <iais:sortableHeader needSort="true" field="application.processType" value="Application Sub-Type"/>
                            <iais:sortableHeader needSort="true" field="application.submissionType" value="Submission Type"/>
                            <iais:sortableHeader needSort="true" field="application.status" value="Application Status"/>
                            <iais:sortableHeader needSort="true" field="application.applicationDt" value="Application Date"/>
                            <iais:sortableHeader needSort="true" field="application.modifiedAt" value="Last Modified Date"/>
                        </tr>
                        </thead>
                        <c:choose>
                            <%--@elvariable id="dataList" type="java.util.List<sg.gov.moh.iais.egp.bsb.dto.task.TaskListDisplayDto>"--%>
                            <c:when test="${empty dataList}">
                                <tr>
                                    <td colspan="7">
                                        <iais:message key="GENERAL_ACK018" escape="true"/>
                                    </td>
                                </tr>
                            </c:when>
                            <c:otherwise>
                                <c:forEach var="pool" items="${dataList}" varStatus="status">
                                    <c:set var="entity" value="${pool.appAndMiscDto}"/>
                                    <c:set var="divId" value="${(status.index + 1) + (pageInfo.pageNo - 1) * pageInfo.size}"/>
                                    <tr style="display: table-row;" id="advfilter${divId}">
                                        <td>
                                            <p class="visible-xs visible-sm table-row-title">S/N</p>
                                            <p>${(status.index + 1) + (pageInfo.pageNo) * pageInfo.size}</p>
                                        </td>
                                        <td>
                                            <p class="visible-xs visible-sm table-row-title">Application No.</p>
                                            <p style="width: 185px;">
                                                <c:out value="${entity.applicationNo}"/><a href="javascript:void(0);" class="accordion-toggle  collapsed" style="float: right"
                                                                                                       data-toggle="collapse" aria-expanded="false"
                                                                                                       data-target="#dropdown${divId}"></a>
                                            </p>
                                        </td>
                                        <td>
                                            <p class="visible-xs visible-sm table-row-title">Application Type</p>
                                            <p><iais:code code="${entity.appType}"/></p>
                                        </td>
                                        <td>
                                            <p class="visible-xs visible-sm table-row-title">Application Sub-Type</p>
                                            <p><iais:code code="${entity.processType}"/></p>
                                        </td>
                                        <td>
                                            <p class="visible-xs visible-sm table-row-title">Submission Type</p>
                                            <p><iais:code code="${entity.submissionType ne null ? entity.submissionType : 'N/A'}"/></p>
                                        </td>
                                        <td>
                                            <p class="visible-xs visible-sm table-row-title">Application Status</p>
                                            <p><iais:code code="${entity.status}"/></p>
                                        </td>
                                        <td>
                                            <p class="visible-xs visible-sm table-row-title">Application Date</p>
                                            <p><fmt:formatDate value="${entity.submitDate}" pattern="dd/MM/yyyy"/></p>
                                        </td>
                                        <td>
                                            <p class="visible-xs visible-sm table-row-title">Last Modified Date</p>
                                            <p><fmt:formatDate value="${entity.modifiedAt}" pattern="dd/MM/yyyy"/></p>
                                        </td>
                                    </tr>
                                    <tr style="background-color: #F3F3F3;" class="p" id="advfilterson${divId}">
                                        <td colspan="8" style="padding: 0 8px !important;">
                                            <div class="accordian-body p-3 collapse" id="dropdown${divId}">
                                                <table aria-describedby=""  class="table application-item" style="background-color: #F3F3F3;margin-bottom:0;" >
                                                    <thead>
                                                    <tr>
                                                        <th scope="col"></th>
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
                                                    <c:forEach var="subTask" items="${pool.taskListDetailViewDtos}">
                                                        <c:set var="taskEntity" value="${subTask.taskDto}"/>
                                                        <c:set var="maskedTaskId" value='${MaskUtil.maskValue("id", taskEntity.id)}'/>
                                                        <c:set var="maskedAppId" value='${MaskUtil.maskValue("appId", taskEntity.application.id)}'/>
                                                        <tr style="color: black">
                                                            <td>
                                                                <p class="visible-xs visible-sm table-row-title"></p>
                                                                <p><input type="checkbox" name="pickTaskIds" value="${maskedTaskId}"/></p>
                                                            </td>
                                                            <td>
                                                                <p class="visible-xs visible-sm table-row-title">Application No.</p>
                                                                <p>
                                                                    <a href="javascript:void(0)" onclick="pickUpTask('${maskedTaskId}','${maskedAppId}')"><c:out value="${taskEntity.application.applicationNo}"/></a>
                                                                </p>
                                                            </td>
                                                            <td>
                                                                <p class="visible-xs visible-sm table-row-title">Facility Classification</p>
                                                                <p><iais:code code="${subTask.facClassification ne null ? subTask.facClassification : 'N/A'}"/></p>
                                                            </td>
                                                            <td>
                                                                <p class="visible-xs visible-sm table-row-title">Facility Name</p>
                                                                <p><c:out value="${subTask.facName ne null ? subTask.facName : 'N/A'}"/></p>
                                                            </td>
                                                            <td>
                                                                <p class="visible-xs visible-sm table-row-title">Facility Address</p>
                                                                <p><c:out value="${subTask.facAddress ne null ? subTask.facAddress : 'N/A'}"/></p>
                                                            </td>
                                                            <td>
                                                                <p class="visible-xs visible-sm table-row-title">Inspection Date</p>
                                                                <p><c:out value="${subTask.inspectionDate ne null ? subTask.inspectionDate : 'N/A'}"/></p>
                                                            </td>
                                                            <td>
                                                                <p class="visible-xs visible-sm table-row-title">Validity End Date</p>
                                                                <p><c:out value="${subTask.validityEndDate ne null ? subTask.validityEndDate : 'N/A'}"/></p>
                                                            </td>
                                                            <td>
                                                                <p class="visible-xs visible-sm table-row-title">Status</p>
                                                                <p><iais:code code="${subTask.status ne null ? subTask.status : 'N/A'}"/></p>
                                                            </td>
                                                        </tr>
                                                    </c:forEach>
                                                    </tbody>
                                                </table>
                                            </div>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </c:otherwise>
                        </c:choose>
                    </table>
                    <div style="text-align:right;">
                        <button type="button" class="btn btn-primary" onclick="pickUpMultiTask()">Assign</button>
                    </div>
                </div>
            </div>
        </div>
        <iais:confirm msg="GENERAL_ERR0023"  needCancel="false" callBack="multiAssignCancel()" popupOrder="multiAssignAlert" needFungDuoJi="false" />
    </form>
</div>
