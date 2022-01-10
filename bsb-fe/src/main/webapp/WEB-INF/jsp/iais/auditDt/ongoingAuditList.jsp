<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib uri="http://www.ecquaria.com/menu" prefix="menu" %>
<%@ taglib uri="ecquaria/sop/egov-smc" prefix="egov-smc" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>
<%@ page import="static sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT" %>
<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-internet"/>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-common.js"></script>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-audit.js"></script>
<%@include file="dashboard.jsp" %>

<form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
    <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
    <input type="hidden" name="action_type" value="">
    <input type="hidden" name="action_value" value="">
    <input type="hidden" name="action_additional" value="">
<%--    <div class="main-content" style="min-height: 73vh;">--%>
        <div class="container">
        <div class="col-xs-12">
<%--            <div class="center-content">--%>
                <div class="internet-content" id="clearSelect">
                    <div class="row">
                        <div class="col-xs-10 col-md-12">
                            <div class="components">
                                <a class="btn btn-secondary" data-toggle="collapse" name="filterBtn" data-target="#beInboxFilter">Filter</a>
                            </div>
                        </div>
                    </div>
                    <p></p>
                    <div id="beInboxFilter" class="collapse intranet-content">
                        <iais:row>
                            <iais:field value="Audit Type"/>
                            <iais:value width="18">
                                <iais:select name="auditType" id="auditType" value="${auditSearch.auditType}" codeCategory="CATE_ID_BSB_AUDIT_TYPE" firstOption="Please Select"/>
                            </iais:value>
                        </iais:row>
                        <iais:action style="text-align:right;">
                            <button class="btn btn-secondary" type="button" id="clearBtn" name="clearBtn">Clear</button>
                            <button class="btn btn-primary" type="button" id="searchBtn" name="searchBtn">Search
                            </button>
                        </iais:action>
                    </div>
                    <br><br>
                    <h3><span>Search Results</span></h3>
                    <%--@elvariable id="pageInfo" type="sg.gov.moh.iais.egp.bsb.dto.PageInfo"--%>
                    <iais-bsb:Pagination size="${pageInfo.size}" pageNo="${pageInfo.pageNo + 1}" pageAmt="${pageInfo.totalPages}" totalElements="${pageInfo.totalElements}"/>
                    <div class="table-gp">
                        <table aria-describedby="" class="table application-group" style="border-collapse:collapse;">
                            <thead>
                            <tr>
                                <th scope="col" style="display: none"></th>
                                <iais:sortableHeader needSort="false" field="" value="S/N" isFE="false"/>
                                <iais:sortableHeader needSort="true" field="facility.facilityName" value="Facility Name" isFE="false"/>
                                <iais:sortableHeader needSort="true" field="auditType" value="Audit Type" isFE="false"/>
                                <iais:sortableHeader needSort="true" field="auditDt" value="Audit Date" isFE="false"/>
                                <iais:sortableHeader needSort="false" field="" value="Action" isFE="false"/>
                            </tr>
                            </thead>
                            <%--@elvariable id="dataList" type="java.util.List<sg.gov.moh.iais.egp.bsb.entity.Application>"--%>
                            <c:forEach var="item" items="${dataList}" varStatus="status">
                                <c:set var="auditIndex" value="${(status.index + 1) + (pageInfo.pageNo) * pageInfo.size}"></c:set>
                                <tr style="display: table-row;">
                                    <input type="text" value="<fmt:formatDate value='${item.auditDt}' pattern='dd/MM/yyyy'/>" name="lastAuditDt" hidden>
                                    <td>${(status.index + 1) + (pageInfo.pageNo) * pageInfo.size}</td>
                                    <td>${item.facility.facilityName}</td>
                                    <td><iais:code code="${item.auditType}"></iais:code></td>
                                    <td><fmt:formatDate value='${item.auditDt}' pattern='dd/MM/yyyy'/></td>
                                    <c:choose>
                                        <c:when test="${item.status eq 'AUDITST003' or item.status eq 'AUDITST007' or item.auditType eq 'AUDITTY002' or item.auditType eq 'AUDITTY003'}">
                                            <td></td>
                                        </c:when>
                                        <c:otherwise>
                                            <td>
                                                <p><a id="specifyDt" onclick="doSpecifyDt('<iais:mask name="auditId" value="${item.id}"/>','specifyDt')">Specify audit date</a></p>
                                                <p><a id="changeDt" onclick="doChangeDt('<iais:mask name="auditId" value="${item.id}"/>','changeDt')">Change audit date</a></p>
                                                <p><a id="facSelfAudit" onclick="submitReport('<iais:mask name="auditId" value="${item.id}"/>')">Facility self audit</a></p>
                                            </td>
                                        </c:otherwise>
                                    </c:choose>
                                </tr>
                            </c:forEach>
                        </table>
                        <div class="col-xs-12 col-md-3">
                            <a class="back" href="/bsb-fe/eservice/INTERNET/MohBSBInboxMsg"><em class="fa fa-angle-left"></em> Back</a>
                        </div>
                    </div>
                </div>
<%--            </div>--%>
            </div>
        </div>
<%--    </div>--%>
    <input name="auditId" id="auditId" value="" hidden>
    <input name="moduleType" id="moduleType" value="" hidden>
</form>
