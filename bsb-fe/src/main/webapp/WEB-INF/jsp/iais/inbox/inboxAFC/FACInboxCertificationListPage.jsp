<%@ page import="static sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT" %>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib uri="ecquaria/sop/egov-smc" prefix="egov-smc" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.ecq.com/iais-bsb" prefix="iais-bsb" %>
<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-internet"/>

<link href="<%=WEB_ROOT%>/css/bsb/bsb-common.css" rel="stylesheet"/>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-common.js"></script>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-inbox.js"></script>


<%@include file="../dashboard/dashboardFAC.jsp"%>
<%@include file="/WEB-INF/jsp/iais/include/showErrorMsg.jsp"%>

<div class="main-content">
    <div class="container">
        <div class="row">
            <div class="col-xs-12">
                <div class="tab-gp dashboard-tab" style="margin-left: 6px;margin-right: -8px;">
                    <%@ include file="../InnerNavBarFAC.jsp"%>

                    <div style="padding: 50px 0">
                        <form class="" method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
                            <div id="searchPanel" class="tab-search" style="padding: 0 90px">

                                <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
                                <input type="hidden" name="action_type" value="">
                                <input type="hidden" name="action_value" value="">
                                <input type="hidden" name="action_additional" value="">


                                <%--@elvariable id="inboxCertificationSearchDto" type="sg.gov.moh.iais.egp.bsb.dto.inbox.InboxAFCSearchDto"--%>
                                <div class="row">
                                    <div class="col-xs-12 col-sm-6">
                                        <label class="col-xs-12 col-sm-5 control-label" for="searchAppNo" style="padding-left: 0">Application No.:</label>
                                        <div class="col-xs-12 col-sm-7">
                                            <input type="text" id="searchAppNo" name="searchAppNo" value="${inboxCertificationSearchDto.searchAppNo}"/>
                                            <span data-err-ind="searchAppNo" class="error-msg"></span>
                                        </div>

                                    </div>
                                    <div class="col-xs-12 col-sm-6">
                                        <label class="col-xs-12 col-sm-5 control-label" for="searchFacilityName" style="padding-left: 0">Facility Name:</label>
                                        <div class="col-xs-12 col-sm-7">
                                            <input type="text" id="searchFacilityName" name="searchFacilityName" value="${inboxCertificationSearchDto.searchFacilityName}"/>
                                            <span data-err-ind="searchFacilityName" class="error-msg"></span>
                                        </div>
                                    </div>
                                </div>
                                <div class="row">
                                    <div class="col-xs-12 col-sm-6">
                                        <label class="col-xs-12 col-sm-5 control-label" style="padding-left: 0">Last Updated Date From:</label>
                                        <div class="col-xs-12 col-sm-7">
                                            <iais:datePicker id="searchUpdatedDateFrom" name="searchUpdatedDateFrom" value="${inboxCertificationSearchDto.searchUpdatedDateFrom}"/>
                                        </div>
                                        <span data-err-ind="searchUpdatedDateFrom" class="error-msg"></span>
                                    </div>
                                    <div class="col-xs-12 col-sm-6">
                                        <label class="col-xs-12 col-sm-5 control-label" style="padding-left: 0">Last Updated Date To:</label>
                                        <div class="col-xs-12 col-sm-7">
                                            <iais:datePicker id="searchUpdatedDateTo" name="searchUpdatedDateTo" value="${inboxCertificationSearchDto.searchUpdatedDateTo}"/>
                                        </div>
                                        <span data-err-ind="searchUpdatedDateTo" class="error-msg"></span>
                                    </div>
                                </div>
                            </div>
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
                                                <%-- need to use new tag in future --%>
                                                <th scope="col" style="display: none"></th>
                                                <iais:sortableHeader needSort="true" field="application.applicationNo" value="Application No." isFE="true" style="width:15%"/>
                                                <iais:sortableHeader needSort="true" field="application.processType" value="Application Sub-Type" style="width:13%" isFE="true"/>
                                                <iais:sortableHeader needSort="true" field="application.status" value="Application Status" style="width:13%" isFE="true"/>
                                                <iais:sortableHeader needSort="true" field="facilityName" value="Facility Name" isFE="true" style="width:13%"/>
                                                <iais:sortableHeader needSort="true" field="facilityAddress" value="Facility Address" isFE="true" style="width:14%"/>
                                                <iais:sortableHeader needSort="true" field="application.modifiedAt" value="Last Updated" isFE="true" style="width:15%"/>
                                                <iais:sortableHeader needSort="false" field="" value="Download" isFE="true" style="width:17%"/>
                                            </tr>
                                            </thead>
                                            <tbody>
                                            <c:choose>
                                                <%--@elvariable id="dataList" type="java.util.List<sg.gov.moh.iais.egp.bsb.dto.inbox.InboxAFCResultDto>"--%>
                                                <c:when test="${empty dataList}">
                                                    <tr>
                                                        <td colspan="6">
                                                            <iais:message key="GENERAL_ACK018" escape="true"/>
                                                        </td>
                                                    </tr>
                                                </c:when>
                                                <c:otherwise>
                                                    <c:forEach var="data" items="${dataList}" varStatus="status">
                                                        <c:set var="maskedAppId"><iais:mask name='appId' value='${data.appId}'/></c:set>
                                                        <c:set var="maskedInsCerFacRelId"><iais:mask name='insCerFacRelId' value='${data.insCerFacRelId}'/></c:set>
                                                        <tr>
                                                            <td>
                                                                <p class="visible-xs visible-sm table-row-title">Application No.</p>
<%--                                                                <a href="/bsb-web/eservice/INTERNET/ViewInspectionCertification?appId=${maskedAppId}&insCerFacRelId=${maskedInsCerFacRelId}&type=${data.dataType}"><c:out value="${data.appNo}"/></a>--%>
                                                                <a href="/bsb-web/eservice/INTERNET/MohBsbViewFacRegApplication?appId=<iais:mask name='appId' value='${data.mainAppId}'/>"><c:out value="${data.appNo}"/></a>
                                                            </td>
                                                            <td>
                                                                <p class="visible-xs visible-sm table-row-title">Application Sub-Type</p>
                                                                <p><iais:code code="${data.appSubType}"/></p>
                                                            </td>
                                                            <td>
                                                                <p class="visible-xs visible-sm table-row-title">Application Status</p>
                                                                <p><iais:code code="${data.appStatus}"/></p>
                                                            </td>
                                                            <td>
                                                                <p class="visible-xs visible-sm table-row-title">Facility Name</p>
                                                                <p style="text-align: center"><c:out value="${data.facilityName}"/></p>
                                                            </td>
                                                            <td>
                                                                <p class="visible-xs visible-sm table-row-title">Facility Address</p>
                                                                <p style="text-align: center"><c:out value="${data.facilityAddress}"/></p>
                                                            </td>

                                                            <td>
                                                                <p class="visible-xs visible-sm table-row-title">Last Updated</p>
                                                                <p><fmt:formatDate value="${data.updateDate}" pattern="dd/MM/yyyy HH:mm:ss"/></p>
                                                            </td>
                                                            <td>
                                                                <p class="visible-xs visible-sm table-row-title">Download</p>
                                                                <c:set var="insCerFacRelId"><iais:mask name='file' value='${data.insCerFacRelId}'/></c:set>
                                                                <c:choose>
                                                                    <c:when test="${'INS'.equals(data.dataType) && data.insCompleted}">
                                                                        <a class="btn btn-secondary" onclick="downloadInsCerFile('INS','${insCerFacRelId}')">DOWNLOAD</a>
                                                                    </c:when>
                                                                    <c:when test="${'CER'.equals(data.dataType) && data.cerCompleted}">
                                                                        <a class="btn btn-secondary" onclick="downloadInsCerFile('CER','${insCerFacRelId}')">DOWNLOAD</a>
                                                                    </c:when>
                                                                    <c:when test="${'ALL'.equals(data.dataType)}">
                                                                        <a class="btn btn-secondary" onclick="downloadInsCerFile('ALL','${insCerFacRelId}')">DOWNLOAD</a>
                                                                    </c:when>
                                                                    <c:otherwise>
                                                                        <p></p>
                                                                    </c:otherwise>
                                                                </c:choose>
                                                            </td>
                                                        </tr>
                                                    </c:forEach>
                                                </c:otherwise>
                                            </c:choose>
                                            </tbody>
                                        </table>
                                        <%--@elvariable id="AFTER_DELETE_DRAFT_APP" type="java.lang.Boolean"--%>
                                        <iais:confirm msg="Are you sure you want to delete?" needFungDuoJi="false" popupOrder="deleteDraftModal" callBack="delDraftCancelBtn()" title=" " cancelFunc="delDraftYesBtn()" cancelBtnDesc="OK" yesBtnDesc="Cancel" cancelBtnCls="btn btn-primary" yesBtnCls="btn btn-secondary"  />
                                        <iais:confirm msg="The draft application is deleted" needFungDuoJi="false" popupOrder="deleteDraftMessage"  title=" " callBack="delDraftMsgYesBtn()"  needCancel="false" />
                                        <input type="hidden" id="afterDeleteDraftApp" name="afterDeleteDraftApp" value="${AFTER_DELETE_DRAFT_APP}" readonly disabled/>

                                    </div>
                                </div>
                            </div>
                        </form>

                    </div>
                </div>
            </div>
        </div>
    </div>
</div>