<%@ page import="static sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT" %>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib uri="ecquaria/sop/egov-smc" prefix="egov-smc" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.ecq.com/iais-bsb" prefix="iais-bsb" %>
<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ page import="sg.gov.moh.iais.egp.bsb.util.TableDisplayUtil" %>
<%@ page import="sg.gov.moh.iais.egp.bsb.constant.InboxActionControlConstants" %>

<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-internet"/>

<link href="<%=WEB_ROOT%>/css/bsb/bsb-common.css" rel="stylesheet"/>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-common.js"></script>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-inbox.js"></script>


<%@include file="../dashboard/dashboardFAC.jsp"%>


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


                                <%--@elvariable id="InboxFacSearchDto" type="sg.gov.moh.iais.egp.bsb.dto.inbox.InboxFacSearchDto"--%>
                                <div class="row">
                                    <div class="col-md-6">
                                        <label for="facilityName" class="col-sm-3 col-md-4 control-label">Facility Name:</label>
                                        <div class="col-sm-7 col-md-8">
                                            <input type="text" id="facilityName" name="facilityName" value="${InboxFacSearchDto.facilityName}"/>
                                            <span data-err-ind="facilityName" class="error-msg"></span>
                                        </div>
                                    </div>
                                    <div class="col-md-6">
                                        <label for="facilityStatus" class="col-sm-3 col-md-4 control-label">Status:</label>
                                        <div class="col-sm-7 col-md-8">
                                            <select id="facilityStatus" class="facilityStatusDropdown" name="facilityStatus">
                                                <option value='<c:out value=""/>' <c:if test="${InboxFacSearchDto.facilityStatus eq ''}">selected="selected"</c:if>>All</option>
                                                <%--@elvariable id="facilityStatusOps" type="java.util.List<com.ecquaria.cloud.moh.iais.common.dto.SelectOption>"--%>
                                                <c:forEach var="item" items="${facilityStatusOps}">
                                                    <option value='<c:out value="${item.value}"/>' <c:if test="${InboxFacSearchDto.facilityStatus eq item.value}">selected="selected"</c:if> ><c:out value="${item.text}"/></option>
                                                </c:forEach>
                                            </select>
                                            <span data-err-ind="facilityStatus" class="error-msg"></span>
                                        </div>
                                    </div>
                                </div>
                                <div class="row" style="margin-bottom: 15px">
                                    <div class="col-md-6">
                                        <label for="role" class="col-sm-3 col-md-4 control-label">Role:</label>
                                        <div class="col-sm-7 col-md-8">
                                            <select id="role" class="roleDropdown" name="role">
                                                <option value='<c:out value=""/>' <c:if test="${InboxFacSearchDto.role eq ''}">selected="selected"</c:if>>All</option>
                                                <%--@elvariable id="processTypeOps" type="java.util.List<com.ecquaria.cloud.moh.iais.common.dto.SelectOption>"--%>
                                                <option value='RLINFAC001' <c:if test="${InboxFacSearchDto.role eq 'RLINFAC001'}">selected="selected"</c:if> >Facility Main Administrator</option>
                                                <option value='RLINFAC002' <c:if test="${InboxFacSearchDto.role eq 'RLINFAC002'}">selected="selected"</c:if> >Facility Alternate Administrator</option>
                                                <option value='RLINFAC003' <c:if test="${InboxFacSearchDto.role eq 'RLINFAC003'}">selected="selected"</c:if> >Facility Officer</option>
                                            </select>
                                            <span data-err-ind="role" class="error-msg"></span>
                                        </div>
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
                                                <iais:sortableHeader needSort="true" field="facilityNo" value="Facility Number" isFE="true" style="width:12%"/>
                                                <iais:sortableHeader needSort="true" field="name" value="Facility Name" isFE="true" style="width:10%"/>
                                                <iais:sortableHeader needSort="true" field="classification" value="Facility Classification" isFE="true" style="width:13%"/>
                                                <iais:sortableHeader needSort="true" field="address" value="Facility Address" isFE="true" style="width:15%"/>
                                                <iais:sortableHeader needSort="true" field="roleInFac" value="Role" isFE="true" style="width:10%"/>
                                                <iais:sortableHeader needSort="true" field="status" value="Status" isFE="true" style="width:10%"/>
                                                <iais:sortableHeader needSort="true" field="effectiveEndDate" value="Expiry Date" isFE="true" style="width:10%"/>
                                                <iais:sortableHeader needSort="false" field="" value="Actions" isFE="true" style="width:20%"/>
                                            </tr>
                                            </thead>
                                            <tbody>
                                            <c:choose>
                                                <%--@elvariable id="resultDto" type="java.util.List<sg.gov.moh.iais.egp.bsb.dto.inbox.InboxFacResultDto>"--%>
                                                <c:when test="${empty resultDto}">
                                                    <tr>
                                                        <td colspan="6">
                                                            <iais:message key="GENERAL_ACK018" escape="true"/>
                                                        </td>
                                                    </tr>
                                                </c:when>
                                                <c:otherwise>
                                                    <c:forEach var="fac" items="${resultDto}" varStatus="status">
                                                        <tr>
                                                            <td>
                                                                <p class="visible-xs visible-sm table-row-title">Facility Number</p>
                                                                <a href="/bsb-web/eservice/INTERNET/MohBsbViewFacRegApplication?facId=<iais:mask name='facId' value='${fac.facilityId}'/>"><c:out value="${fac.facilityNo}"/></a>
                                                            </td>
                                                            <td>
                                                                <p class="visible-xs visible-sm table-row-title">Facility Name</p>
                                                                <p style="text-align: center"><c:out value="${fac.facilityName}" /></p>
                                                            </td>
                                                            <td>
                                                                <p class="visible-xs visible-sm table-row-title">Facility Classification</p>
                                                                <p><iais:code code="${fac.facilityClassification}"/></p>
                                                            </td>
                                                            <td>
                                                                <p class="visible-xs visible-sm table-row-title">Facility Address</p>
                                                                <p><c:out value="${TableDisplayUtil.getOneLineAddress(fac.blkNo,fac.streetName,fac.floorNo,fac.unitNo,fac.postalCode)}"/></p>
                                                            </td>
                                                            <td>
                                                                <p class="visible-xs visible-sm table-row-title">Role</p>
                                                                <p><iais:code code="${fac.roleInFac}"/></p>
                                                            </td>
                                                            <td>
                                                                <p class="visible-xs visible-sm table-row-title">Facility Status</p>
                                                                <p><iais:code code="${fac.status}"/></p>
                                                            </td>
                                                            <td>
                                                                <p class="visible-xs visible-sm table-row-title">Expiry Date</p>
                                                                <p><iais-bsb:format-LocalDate localDate="${fac.effectiveEndDate}" /></p>
                                                            </td>
                                                            <td>
                                                                <iais-bsb:fac-action facClassification="${fac.facilityClassification}" facStatus="${fac.status}" renewable="${fac.renewable}" attributeKey="actionAvailable"/>
                                                                    <%--@elvariable id="actionAvailable" type="java.lang.Boolean"--%>
                                                                <p class="visible-xs visible-sm table-row-title">Actions</p>
                                                                <c:choose>
                                                                    <c:when test="${not actionAvailable}">
                                                                        <select class="naDropdown${status.index}" disabled="disabled">
                                                                            <option>N/A</option>
                                                                        </select>
                                                                    </c:when>
                                                                    <c:otherwise>
                                                                        <label for="facAction${status.index}"></label><select name="facAction${status.index}" class="facActionDropdown${status.index}" id="facAction${status.index}" onchange="facilityActionControl('${fac.facilityId}', 'facAction${status.index}')">
                                                                            <option value="#" selected="selected">Select</option>
                                                                            <%--@elvariable id="FacApplyApprovalJudge" type="java.lang.Boolean"--%>
                                                                            <c:if test="${FacApplyApprovalJudge}">
                                                                                <option value="${InboxActionControlConstants.ACTION_FACILITY_APPLY_FOR_APPROVAL}">Apply for Approval</option>
                                                                            </c:if>
                                                                            <%--@elvariable id="InventoryNotificationDataSubmissionJudge" type="java.lang.Boolean"--%>
<%--                                                                            <c:if test="${InventoryNotificationDataSubmissionJudge}">--%>
<%--                                                                                <option value="${InboxActionControlConstants.ACTION_FACILITY_INVENTORY_NOTIFICATION_DATA_SUBMISSION}">Inventory Notification/ Data Submission</option>--%>
<%--                                                                            </c:if>--%>
                                                                            <%--@elvariable id="FacRenewJudge" type="java.lang.Boolean"--%>
                                                                            <c:if test="${FacRenewJudge}">
                                                                                <option value="${InboxActionControlConstants.ACTION_FACILITY_RENEW}">Renew</option>
                                                                            </c:if>
                                                                            <%--@elvariable id="FacRfcJudge" type="java.lang.Boolean"--%>
                                                                            <c:if test="${FacRfcJudge}">
                                                                                <option value="${InboxActionControlConstants.ACTION_FACILITY_UPDATE}">Update</option>
                                                                            </c:if>
                                                                            <%--@elvariable id="IncidentReportingJudge" type="java.lang.Boolean"--%>
<%--                                                                            <c:if test="${IncidentReportingJudge}">--%>
<%--                                                                                <option value="${InboxActionControlConstants.ACTION_FACILITY_UPDATE}">Incident Reporting</option>--%>
<%--                                                                            </c:if>--%>
                                                                            <%--@elvariable id="DeregisterJudge" type="java.lang.Boolean"--%>
<%--                                                                            <c:if test="${DeregisterJudge}">--%>
<%--                                                                                <option value="${InboxActionControlConstants.ACTION_FACILITY_DEREGISTER}">Deregister</option>--%>
<%--                                                                            </c:if>--%>
                                                                            <%--@elvariable id="DeferRenewalJudge" type="java.lang.Boolean"--%>
                                                                            <c:if test="${DeferRenewalJudge}">
                                                                                <option value="${InboxActionControlConstants.ACTION_FACILITY_DEFER_RENEWAL}">Defer Renewal</option>
                                                                            </c:if>
                                                                        </select>
                                                                    </c:otherwise>
                                                                </c:choose>
                                                            </td>
                                                        </tr>
                                                    </c:forEach>
                                                </c:otherwise>
                                            </c:choose>
                                            </tbody>
                                        </table>
                                        <%--@elvariable id="lastUrl" type="java.lang.String"--%>
                                        <c:if test="${not empty param.lastUrl}">
                                            <input type="hidden" id="lastUrl" value="${param.lastUrl}">
                                            <iais:confirm msg="The is an existing draft." popupOrder="existDraftModal" needFungDuoJi="false"
                                                          yesBtnCls="btn btn-secondary" yesBtnDesc="Resume from draft" callBack="draftAction('resume')"
                                                          cancelBtnCls="btn btn-primary" cancelBtnDesc="Continue" cancelFunc="draftAction('delete')"/>
                                        </c:if>
                                        <iais:confirm msg="This facility has ongoing application." callBack="closeInvalidActionModal()" popupOrder="invalidActionModal" needCancel="false" needFungDuoJi="false"/>
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