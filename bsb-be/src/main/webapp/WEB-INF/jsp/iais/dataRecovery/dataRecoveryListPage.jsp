<%@ page import="static sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT" %>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
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
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-data-recovery.js"></script>

<div class="main-content" style="min-height: 73vh;">
    <form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
        <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
        <input type="hidden" name="action_type" value="">
        <input type="hidden" name="action_value" value="">
        <input type="hidden" name="maskedDataRecoveryId" value="">

        <div class="col-xs-12">
            <div class="center-content">
                <div class="form-horizontal">
                    <div class="row">
                        <div class="col-xs-10 col-md-12">
                            <div class="components">
                                <a class="btn btn-secondary" data-toggle="collapse" data-target="#dataRecoverySearchFilter">Filter</a>
                            </div>
                        </div>
                    </div>
                    <%--@elvariable id="dataRecoverySearchDto" type="sg.gov.moh.iais.egp.bsb.dto.datarecovery.DataRecoverySearchDto"--%>
                    <div id="dataRecoverySearchFilter" class="collapse">
                        <div class="col-xs-12 col-sm-12">
                            <label for="searchModuleName" class="col-sm-5 col-md-5 control-label">Module Name</label>
                            <div class="col-sm-7 col-md-5">
                                <input type="text" id="searchModuleName" name="searchModuleName" value="${dataRecoverySearchDto.searchModuleName}"/>
                            </div>
                            <label for="searchFunctionName" class="col-sm-5 col-md-5 control-label">Function Name</label>
                            <div class="col-sm-7 col-md-5">
                                <input type="text" id="searchFunctionName" name="searchFunctionName" value="${dataRecoverySearchDto.searchFunctionName}"/>
                            </div>
                            <label for="searchCreateDateFrom" class="col-sm-5 col-md-5 control-label">Create Date From</label>
                            <div class="col-sm-7 col-md-5">
                                <iais:datePicker id="searchCreateDateFrom" name="searchCreateDateFrom" value="${dataRecoverySearchDto.searchCreateDateFrom}"/>
                            </div>
                            <label for="searchCreateDateTo" class="col-sm-5 col-md-5 control-label">Create Date To</label>
                            <div class="col-sm-7 col-md-5">
                                <iais:datePicker id="searchCreateDateTo" name="searchCreateDateTo" value="${dataRecoverySearchDto.searchCreateDateTo}"/>
                            </div>
                        </div>
                        <div class="col-xs-12 col-sm-12" style="text-align:right;">
                            <button class="btn btn-secondary" type="button" id="clearBtn" name="clearBtn">Clear</button>
                            <button class="btn btn-primary" type="button" id="searchBtn" name="searchBtn">Search</button>
                        </div>
                    </div>
                </div>
                <h3 style="margin-top: 20px">
                    <span>Search Results</span>
                </h3>
                <%--@elvariable id="pageInfo" type="sg.gov.moh.iais.egp.bsb.dto.PageInfo"--%>
                <iais-bsb:Pagination size="${pageInfo.size}" pageNo="${pageInfo.pageNo + 1}" pageAmt="${pageInfo.totalPages}" totalElements="${pageInfo.totalElements}"/>

                <div class="table-gp">
                    <table aria-describedby="" class="table application-group" style="border-collapse:collapse;">
                        <thead>
                        <tr style="text-align: center">
                            <th scope="col" style="display: none"></th>
                            <iais:sortableHeader needSort="false" field="" value="S/N"/>
                            <iais:sortableHeader needSort="false" field="moduleName" value="Module Name"/>
                            <iais:sortableHeader needSort="false" field="functionName" value="Function Name"/>
                            <iais:sortableHeader needSort="false" field="userName" value="User Name"/>
                            <iais:sortableHeader needSort="false" field="createDate" value="Create Date"/>
                            <iais:sortableHeader needSort="false" field="action" value="Action"/>
                        </tr>
                        </thead>
                        <c:choose>
                            <%--@elvariable id="dataList" type="java.util.List<sg.gov.moh.iais.egp.bsb.dto.datarecovery.DataRecoveryDisplayDto>"--%>
                            <c:when test="${empty dataList}">
                                <tr>
                                    <td colspan="7">
                                        <iais:message key="GENERAL_ACK018" escape="true"/>
                                    </td>
                                </tr>
                            </c:when>
                            <c:otherwise>
                                <c:forEach var="data" items="${dataList}" varStatus="status">
                                    <tr style="display: table-row; text-align: center">
                                        <td>
                                            <p class="visible-xs visible-sm table-row-title">S/N</p>
                                            <p>${(status.index + 1) + (pageInfo.pageNo) * pageInfo.size}</p>
                                        </td>
                                        <td>
                                            <p class="visible-xs visible-sm table-row-title">Module Name</p>
                                            <p><c:if test="${data.functionName eq null or data.functionName eq ''}">-</c:if><iais:code code="${data.moduleName}"/></p>
                                        </td>
                                        <td>
                                            <p class="visible-xs visible-sm table-row-title">Function Name</p>
                                            <p><c:if test="${data.functionName eq null}">-</c:if><iais:code code="${data.functionName}"/></p>
                                        </td>
                                        <td>
                                            <p class="visible-xs visible-sm table-row-title">User Name</p>
                                            <p><iais:code code="${data.userName}"/></p>
                                        </td>
                                        <td>
                                            <p class="visible-xs visible-sm table-row-title">Create Date</p>
                                            <p><iais:code code="${data.createDate}"/></p>
                                        </td>
                                        <td>
                                            <p class="visible-xs visible-sm table-row-title">Action</p>
                                            <button type="button" class="btn btn-default btn-sm" onclick="viewDataRecovery('<iais:mask name="maskedDataRecoveryId" value="${data.id}"/>')">View</button>
                                            <button type="button" class="btn btn-default btn-sm" onclick="recoverData('<iais:mask name="maskedDataRecoveryId" value="${data.id}"/>', 'extension')">Recover Data</button>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </c:otherwise>
                        </c:choose>
                    </table>
                </div>
            </div>
        </div>
    </form>
</div>
