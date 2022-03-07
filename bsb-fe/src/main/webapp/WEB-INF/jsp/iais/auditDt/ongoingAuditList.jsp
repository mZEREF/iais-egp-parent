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
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-internet"/>

<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-common.js"></script>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-audit.js"></script>

<%@include file="dashboard.jsp" %>
<br>
<br>
<form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
    <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
    <input type="hidden" name="action_type" value="">
    <input type="hidden" name="action_value" value="">
    <input type="hidden" name="action_additional" value="">
    <div class="container">
        <div class="col-xs-12">
            <div class="internet-content" id="clearSelect">
                <div class="row">
                    <iais:field value="Audit Type"/>
                    <iais:value width="18">
                        <iais:select name="auditType" id="auditType" value="${auditSearch.auditType}"
                                     codeCategory="CATE_ID_BSB_AUDIT_TYPE" firstOption="Please Select"/>
                    </iais:value>
                </div>
                <br><br>
                <div class="row text-right text-center-mobile">
                    <button class="btn btn-secondary" type="button" id="clearBtn" name="clearBtn">Clear</button>
                    <button class="btn btn-primary" type="button" id="searchBtn" name="searchBtn">Search</button>
                </div>

                <%--@elvariable id="pageInfo" type="sg.gov.moh.iais.egp.bsb.dto.PageInfo"--%>
                <iais-bsb:Pagination size="${pageInfo.size}" pageNo="${pageInfo.pageNo + 1}" pageAmt="${pageInfo.totalPages}" totalElements="${pageInfo.totalElements}"/>

                <div class="row">
                    <div class="col-xs-12">
                        <div class="table-gp">
                            <table aria-describedby="" class="table">
                                <thead>
                                <tr>
                                    <th scope="col" style="display: none"></th>
                                    <iais:sortableHeader needSort="false" field="" value="S/N" isFE="false"/>
                                    <iais:sortableHeader needSort="false" field="facility.facilityName" value="Facility Name" isFE="false"/>
                                    <iais:sortableHeader needSort="false" field="auditType" value="Audit Type" isFE="false"/>
                                    <iais:sortableHeader needSort="false" field="auditDt" value="Audit Date" isFE="false"/>
                                    <iais:sortableHeader needSort="false" field="" value="Action" isFE="false"/>
                                </tr>
                                </thead>
                                <tbody>
                                <%--@elvariable id="dataList" type="java.util.List<sg.gov.moh.iais.egp.bsb.entity.FacilityAudit>"--%>
                                <c:choose>
                                    <c:when test="${empty dataList}">
                                        <tr>
                                            <td colspan="6">
                                                <iais:message key="GENERAL_ACK018" escape="true"/>
                                            </td>
                                        </tr>
                                    </c:when>
                                    <c:otherwise>
                                        <c:forEach var="item" items="${dataList}" varStatus="status">
                                            <tr style="display: table-row;">
                                                <td>${(status.index + 1) + (pageInfo.pageNo) * pageInfo.size}</td>
                                                <td>${item.facility.facilityName}</td>
                                                <td><iais:code code="${item.auditType}"/></td>
                                                <td><fmt:formatDate value='${item.auditDt}' pattern='dd/MM/yyyy'/></td>
                                                <p class="visible-xs visible-sm table-row-title">Actions</p>
                                                <c:choose>
                                                    <c:when test="${item.status eq 'AUDITST003' or item.status eq 'AUDITST007' or item.auditType eq 'AUDITTY002' or item.auditType eq 'AUDITTY003'}">
                                                        <td></td>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <td>
                                                            <c:if test="${item.auditDt eq null}">
                                                                <p><a id="specifyDt" onclick="doSpecifyDt('<iais:mask name="auditId" value="${item.id}"/>','specifyDt')">Specify audit date</a></p>
                                                            </c:if>
                                                            <c:if test="${item.auditDt ne null}">
                                                                <p><a id="changeDt" onclick="doChangeDt('<iais:mask name="auditId" value="${item.id}"/>','changeDt')">Change audit date</a></p>
                                                            </c:if>
                                                            <c:if test="${item.auditDt ne null and item.status eq 'AUDITST002'}">
                                                                <p><a id="facSelfAudit" onclick="submitReport('<iais:mask name="auditId" value="${item.id}"/>')">Facility self audit</a></p>
                                                            </c:if>
                                                        </td>
                                                    </c:otherwise>
                                                </c:choose>
                                            </tr>
                                        </c:forEach>
                                    </c:otherwise>
                                </c:choose>
                                </tbody>
                            </table>
                            <div class="col-xs-12 col-md-3">
                                <a class="back" href="/bsb-fe/eservice/INTERNET/MohBSBInboxMsg"><em class="fa fa-angle-left"></em> Back</a>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <input name="auditId" id="auditId" value="" hidden>
    <input name="moduleType" id="moduleType" value="" hidden>
</form>
