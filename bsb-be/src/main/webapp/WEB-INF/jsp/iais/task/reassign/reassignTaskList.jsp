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
        <input type="hidden" name="appId" value="">

        <div class="col-xs-12">
            <div class="center-content">
                <div class="form-horizontal">
                    <div class="form-group">
                        <label for="commonRoleId" class="col-xs-12 col-md-4 control-label">Role</label>
                        <div class="col-xs-8 col-sm-6 col-md-6">
                            <select name="commonRoleId" id="commonRoleId">
                                <%--@elvariable id="BsbRoleOptions" type="java.util.List<com.ecquaria.cloud.moh.iais.common.dto.SelectOption>"--%>
                                <%--@elvariable id="bsbCurRole" type="java.lang.String"--%>
                                <c:forEach var="option" items="${BsbRoleOptions}">
                                    <option value="${option.value}" <c:if test="${option.value eq bsbCurRole}">selected="selected"</c:if>>${option.text}</option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-xs-10 col-md-12">
                            <div class="components">
                                <a class="btn btn-secondary" data-toggle="collapse" data-target="#taskListSearchFilter">Filter</a>
                            </div>
                        </div>
                    </div>
                    <%--@elvariable id="taskListSearchDto" type="sg.gov.moh.iais.egp.bsb.dto.task.TaskListSearchDto"--%>
                    <div id="taskListSearchFilter" class="collapse">
                        <div class="col-xs-12 col-sm-12"><%-- div for app date from and to --%>
                            <label for="searchAppNo" class="col-sm-5 col-md-5 control-label">Application No.</label>
                            <div class="col-sm-7 col-md-5">
                                <span data-err-ind="searchAppNo" class="error-msg"></span>
                                <input type="text" id="searchAppNo" name="searchAppNo" value="${taskListSearchDto.searchAppNo}"/>
                            </div>
                            <label for="searchAppType" class="col-sm-5 col-md-5 control-label">Application Type</label>
                            <div class="col-sm-7 col-md-5">
                                <span data-err-ind="searchAppType" class="error-msg"></span>
                                <iais:select name="searchAppType" id="searchAppType" codeCategory="CATE_ID_BSB_APP_TYPE"
                                             firstOption="Please Select" value="${taskListSearchDto.searchAppType}"/>
                            </div>
                        </div>
                        <div class="col-xs-12 col-sm-12" style="text-align:right;"><%-- div for btn --%>
                            <button class="btn btn-secondary" type="reset" id="clearBtn" name="clearBtn">Clear</button>
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
                            <iais:sortableHeader needSort="false" field="applicationNo" value="Application No."/>
                            <iais:sortableHeader needSort="false" field="processType" value="Process Type"/>
                            <iais:sortableHeader needSort="false" field="appType" value="Application Type"/>
                            <iais:sortableHeader needSort="false" field="applicationDt" value="Application Date"/>
                            <iais:sortableHeader needSort="false" field="applicationStatus" value="Application Status"/>
                            <iais:sortableHeader needSort="false" field="role" value="Current Owner"/>
                            <iais:sortableHeader needSort="false" field="" value="Action"/>
                        </tr>
                        </thead>
                        <c:choose>
                            <%--@elvariable id="dataList" type="java.util.List<sg.gov.moh.iais.egp.bsb.entity.TaskView>"--%>
                            <c:when test="${empty dataList}">
                                <tr>
                                    <td colspan="7">
                                        <iais:message key="GENERAL_ACK018" escape="true"/>
                                    </td>
                                </tr>
                            </c:when>
                            <c:otherwise>
                                <c:forEach var="entity" items="${dataList}" varStatus="status">
                                    <c:set var="maskedTaskId" value='${MaskUtil.maskValue("id", entity.id)}'/>
                                    <c:set var="maskedAppId" value='${MaskUtil.maskValue("appId", entity.application.id)}'/>
                                    <tr style="display: table-row;">
                                        <td>
                                            <p class="visible-xs visible-sm table-row-title">S/N</p>
                                            <p>${(status.index + 1) + (pageInfo.pageNo) * pageInfo.size}</p>
                                        </td>
                                        <td>
                                            <p class="visible-xs visible-sm table-row-title">Application No.</p>
                                            <p><c:out value="${entity.application.applicationNo}"/></p>
                                        </td>
                                        <td>
                                            <p class="visible-xs visible-sm table-row-title">Process Type</p>
                                            <p><iais:code code="${entity.application.processType}"/></p>
                                        </td>
                                        <td>
                                            <p class="visible-xs visible-sm table-row-title">Application Type</p>
                                            <p><iais:code code="${entity.application.appType}"/></p>
                                        </td>
                                        <td>
                                            <p class="visible-xs visible-sm table-row-title">Application Date</p>
                                            <p><fmt:formatDate value="${entity.application.applicationDt}" pattern="dd/MM/yyyy"/></p>
                                        </td>
                                        <td>
                                            <p class="visible-xs visible-sm table-row-title">Application Status</p>
                                            <p><iais:code code="${entity.application.status}"/></p>
                                        </td>
                                        <td>
                                            <p class="visible-xs visible-sm table-row-title">Current Owner</p>
                                            <p><iais:code code="${entity.curOwner}"/></p>
                                        </td>
                                        <td style="width: 15%">
                                            <p class="visible-xs visible-sm table-row-title">Action</p>
                                            <button type="button" class="btn btn-default btn-sm" onclick="reassignTask('${maskedTaskId}','${maskedAppId}')">Reassign</button>
                                            <button type="button" class="btn btn-default btn-sm" onclick="viewTaskDetail('${maskedTaskId}','${maskedAppId}')">Detail</button>
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
