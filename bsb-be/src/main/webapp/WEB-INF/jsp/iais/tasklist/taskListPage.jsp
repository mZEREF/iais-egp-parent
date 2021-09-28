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
        <input type="hidden" name="action_additional" value="">



        <div class="col-xs-12">
            <div class="center-content">
                <div class="form-horizontal">
                    <div class="row">
                        <div class="col-xs-10 col-md-12">
                            <div class="components">
                                <a class="btn btn-secondary" data-toggle="collapse" data-target="#taskListSearchFilter">Filter</a>
                            </div>
                        </div>
                    </div>
                    <%--@elvariable id="taskListSearchDto" type="sg.gov.moh.iais.egp.bsb.dto.task.TaskListSearchDto"--%>
                    <div id="taskListSearchFilter" class="collapse">
                        <div class="col-xs-12 col-sm-12 col-md-6"><%-- div for left four search columns --%>
                            <label for="searchFacName" class="col-sm-5 col-md-5 control-label">Facility Name</label>
                            <div class="col-sm-7 col-md-5">
                                <span data-err-ind="searchFacName" class="error-msg"></span>
                                <input type="text" id="searchFacName" name="searchFacName" value="${taskListSearchDto.searchFacName}"/>
                            </div>
                            <label for="searchFacAddr" class="col-sm-5 col-md-5 control-label">Facility Address</label>
                            <div class="col-sm-7 col-md-5">
                                <span data-err-ind="searchFacAddr" class="error-msg"></span>
                                <input type="text" id="searchFacAddr" name="searchFacAddr" value="${taskListSearchDto.searchFacAddr}"/>
                            </div>
                            <label for="searchFacType" class="col-sm-5 col-md-5 control-label">Facility Type</label>
                            <div class="col-sm-7 col-md-5">
                                <span data-err-ind="searchFacType" class="error-msg"></span>
                                <iais:select name="searchFacType" id="searchFacType" options="facTypeOps"
                                             firstOption="Please Select" value="${taskListSearchDto.searchFacType}"/>
                            </div>
                            <label for="searchProcessType" class="col-sm-5 col-md-5 control-label">Process Type</label>
                            <div class="col-sm-7 col-md-5">
                                <span data-err-ind="searchProcessType" class="error-msg"></span>
                                <iais:select name="searchProcessType" id="searchProcessType" options="processTypeOps"
                                             firstOption="Please Select" value="${taskListSearchDto.searchProcessType}"/>
                            </div>
                        </div>
                        <div class="col-xs-12 col-sm-12 col-md-6 push-float-field-md"><%-- div for right three search columns --%>
                            <label for="searchAppNo" class="col-sm-5 col-md-5 control-label">Application No.</label>
                            <div class="col-sm-7 col-md-5">
                                <span data-err-ind="searchAppNo" class="error-msg"></span>
                                <input type="text" id="searchAppNo" name="searchAppNo" value="${taskListSearchDto.searchAppNo}"/>
                            </div>
                            <label for="searchAppType" class="col-sm-5 col-md-5 control-label">Application Type</label>
                            <div class="col-sm-7 col-md-5">
                                <span data-err-ind="searchAppType" class="error-msg"></span>
                                <iais:select name="searchAppType" id="searchAppType" options="appTypeOps"
                                             firstOption="Please Select" value="${taskListSearchDto.searchAppType}"/>
                            </div>
                            <label for="searchAppStatus" class="col-sm-5 col-md-5 control-label">Application Status</label>
                            <div class="col-sm-7 col-md-5">
                                <span data-err-ind="searchAppStatus" class="error-msg"></span>
                                <iais:select name="searchAppStatus" id="searchAppStatus" options="appStatusOps"
                                             firstOption="Please Select" value="${taskListSearchDto.searchAppStatus}"/>
                            </div>
                        </div>
                        <div class="col-xs-12 col-sm-12"><%-- div for app date from and to --%>
                            <label class="col-xs-2 col-sm-2 control-label">Application Date</label>
                            <span data-err-ind="searchAppDateFrom" class="error-msg"></span>
                            <iais:datePicker id="searchAppDateFrom" name="searchAppDateFrom" value="${taskListSearchDto.searchAppDateFrom}"/>
                            To
                            <span data-err-ind="searchAppDateTo" class="error-msg"></span>
                            <iais:datePicker id="searchAppDateTo" name="searchAppDateTo" value="${taskListSearchDto.searchAppDateTo}"/>
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
                            <%--@elvariable id="dataList" type="java.util.List<sg.gov.moh.iais.egp.bsb.entity.Application>"--%>
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
                                            <p>
                                                <c:choose>
                                                    <c:when test="${entity.appType eq 'BSBAPTY006' and entity.status eq 'BSBAPST002'}">
                                                        <a href="/bsb-be/eservicecontinue/INTRANET/MohAOProcessingRevocation?appId=<iais:mask name='id' value='${entity.id}'/>&OWASP_CSRFTOKEN=null"><c:out value="${entity.applicationNo}"/></a>
                                                    </c:when>
                                                    <c:when test="${entity.appType eq 'BSBAPTY001'}">
                                                        <a href="/bsb-be/eservicecontinue/INTRANET/MohOfficersProcess?appId=<iais:mask name='id' value='${entity.id}'/>&OWASP_CSRFTOKEN=null"><c:out value="${entity.applicationNo}"/></a>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <c:out value="${entity.applicationNo}"/>
                                                    </c:otherwise>
                                                </c:choose>
                                            </p>
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
    $(function (){
        $("a").click(function (event){
            var id = $(this).attr('href').split('?')[1].split('&')[0].split('=')[1];
            // $("#appId").val(id);
            // alert($("#appId").val());
        })
    })
</script>