<%@ page import="static sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT" %>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib uri="http://www.ecquaria.com/menu" prefix="menu" %>
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

<%@include file="/WEB-INF/jsp/iais/include/showErrorMsg.jsp" %>
<%--@elvariable id="isFacAdmin" type="java.lang.Boolean"--%>
<c:choose>
    <c:when test="${isFacAdmin}"><%@include file="../dashboard/dashboardFAC.jsp"%></c:when>
    <c:otherwise><%@include file="../dashboard/dashboardAFC.jsp"%></c:otherwise>
</c:choose>

<div class="main-content">
    <div class="container">
        <div class="row">
            <div class="col-xs-12">
                <div class="tab-gp dashboard-tab" style="margin-left: 6px;margin-right: -8px;">
                    <c:choose>
                        <c:when test="${isFacAdmin}"><%@include file="../InnerNavBarFAC.jsp"%></c:when>
                        <c:otherwise><%@include file="../InnerNavBarAFC.jsp"%></c:otherwise>
                    </c:choose>

                    <div style="padding: 50px 0">
                        <form class="" method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
                            <div class="tab-search">

                                <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
                                <input type="hidden" name="action_type" value="">
                                <input type="hidden" name="action_value" value="">
                                <input type="hidden" name="action_additional" value="">
                                <input type="hidden" name="afterDoArchive" value="${AFTER_ARCHIVE}">


                                <%--@elvariable id="inboxMsgSearchDto" type="sg.gov.moh.iais.egp.bsb.dto.inbox.InboxMsgSearchDto"--%>
                                <div class="row">
                                    <div class="col-xs-12 col-sm-4">
                                        <label class="col-xs-12 col-sm-5 control-label" for="searchMsgType">Message Type:</label>
                                        <div class="col-xs-12 col-sm-7">
                                            <iais:select name="searchMsgType" id="searchMsgType" cssClass="searchMsgTypeDropDown" options="msgTypeOps" value="${inboxMsgSearchDto.searchMsgType}" firstOption="Please Select"/>
                                        </div>
                                    </div>
                                    <div class="col-xs-12 col-sm-4">
                                        <label class="col-xs-12 col-sm-5 control-label" for="searchSubType">Submission Type:</label>
                                        <div class="col-xs-12 col-sm-7">
                                            <iais:select name="searchSubType" cssClass="searchSubTypeDropdown" id="searchSubType" options="msgSubTypeOps" value="${inboxMsgSearchDto.searchSubType}" firstOption="Please Select"/>
                                        </div>
                                    </div>
                                    <div class="col-xs-12 col-sm-4">
                                        <div class="col-xs-12 visible-xs visible-sm" style="height: 20px;">
                                        </div>
                                        <div class="col-xs-12">
                                            <div class="search-wrap" style="width: 100%">
                                                <iais:value>
                                                    <div class="input-group">
                                                        <input class="form-control" id="searchSubject" type="text"
                                                               placeholder="Search your keywords" name="searchSubject"
                                                               aria-label="searchSubject" maxlength="50" value="<c:out value="${inboxMsgSearchDto.searchSubject}"/>" />
                                                        <span class="input-group-btn">
                                                             <button class="btn btn-default buttonsearch" title="Search your keywords" id="searchSubjectBtn"><em class="fa fa-search"></em></button>
                                                        </span>
                                                    </div>
                                                </iais:value>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div class="row" style="margin-top: 20px">
                                    <div class="col-xs-12 col-sm-4">
                                        <label class="col-xs-12 col-sm-5 control-label">Date From:</label>
                                        <div class="col-xs-12 col-sm-7">
                                            <iais:datePicker id="searchMsgDateFrom" name="searchMsgDateFrom" value="${inboxMsgSearchDto.searchMsgDateFrom}"/>
                                        </div>
                                        <span data-err-ind="searchMsgDateFrom" class="error-msg"></span>
                                    </div>
                                    <div class="col-xs-12 col-sm-4">
                                        <label class="col-xs-12 col-sm-5 control-label">Date To:</label>
                                        <div class="col-xs-12 col-sm-7">
                                            <iais:datePicker id="searchMsgDateTo" name="searchMsgDateTo" value="${inboxMsgSearchDto.searchMsgDateTo}"/>
                                        </div>
                                        <span data-err-ind="searchMsgDateTo" class="error-msg"></span>
                                    </div>
                                </div>
                                <%--@elvariable id="pageInfo" type="sg.gov.moh.iais.egp.bsb.dto.PageInfo"--%>
                                <iais-bsb:Pagination size="${pageInfo.size}" pageNo="${pageInfo.pageNo + 1}" pageAmt="${pageInfo.totalPages}" totalElements="${pageInfo.totalElements}"/>
                            </div>


                            <div class="row">
                                <div class="col-xs-12">
                                    <div class="table-gp">
                                        <table aria-describedby="" class="table">
                                            <thead>
                                            <tr>

                                                <th scope="col" style="display: none"></th>
                                                <th scope="col">
                                                    <div class="form-check" style="margin-bottom: 1rem;padding-left: 14px">
                                                        <input class="form-check-input msgCheck" id="msgCheckAll" type="checkbox" name="chkParent" aria-invalid="false">
                                                        <label class="form-check-label" for="msgCheckAll"><span
                                                                class="check-square"></span>
                                                        </label>
                                                    </div>
                                                </th>
                                                <iais:sortableHeader needSort="true" field="subject" value="Subject" style="width:20%" isFE="true"/>
                                                <iais:sortableHeader needSort="true" field="msgType" value="Message Type" style="width:13%" isFE="true"/>
                                                <iais:sortableHeader needSort="true" field="facilityName" value="Facility Name" style="width:15%" isFE="true"/>
                                                <iais:sortableHeader needSort="true" field="refNo" value="Reference Number" isFE="true"/>
                                                <iais:sortableHeader needSort="true" field="submissionType" value="Submission Type" isFE="true"/>
                                                <iais:sortableHeader needSort="true" field="createdAt" value="Date of Notification" isFE="true"/>
                                            </tr>
                                            </thead>
                                            <tbody>
                                            <c:choose>
                                                <%--@elvariable id="dataList" type="java.util.List<sg.gov.moh.iais.egp.bsb.dto.entity.BsbInboxDto>"--%>
                                                <c:when test="${empty dataList}">
                                                    <tr>
                                                        <td colspan="6">
                                                            <iais:message key="GENERAL_ACK018" escape="true"/>
                                                        </td>
                                                    </tr>
                                                </c:when>
                                                <c:otherwise>
                                                    <c:forEach var="msg" items="${dataList}" varStatus="status">
                                                        <c:set var="isActionRequired" value="${actionRequiredMap.get(msg.id)}"/>
                                                        <c:choose>
                                                            <c:when test="${msg.msgType == 'BSBMSGT001'}">
                                                                <c:choose>
                                                                    <c:when test="${msg.status == 'MSGRS001'}">
                                                                        <tr style="font-weight:bold">
                                                                    </c:when>
                                                                    <c:otherwise>
                                                                        <tr>
                                                                    </c:otherwise>
                                                                </c:choose>
                                                            </c:when>
                                                            <c:when test="${msg.msgType == 'BSBMSGT002'}">
                                                                <c:choose>
                                                                    <c:when test="${msg.status == 'MSGRS001' || !isActionRequired}">
                                                                        <tr style="font-weight:bold">
                                                                    </c:when>
                                                                    <c:otherwise>
                                                                        <tr>
                                                                    </c:otherwise>
                                                                </c:choose>
                                                            </c:when>
                                                        </c:choose>
                                                        <c:set var="maskedMsgId"><iais:mask name="action_value" value="${msg.id}"/></c:set>
                                                        <td>
                                                            <div class="form-check">
                                                                <input class="form-check-input msgCheck" id="msgCheck" type="checkbox" name="chkChild" aria-invalid="false" value="${maskedMsgId}"
                                                                       <c:if test="${msg.status == 'MSGRS001' && msg.msgType == 'BSBMSGT001' ||
                                                                        msg.msgType == 'BSBMSGT002' && (msg.status == 'MSGRS001' || !isActionRequired) }">disabled = "disabled"</c:if>>
                                                                <label class="form-check-label" for="msgCheck"><span
                                                                        class="check-square"></span>
                                                                </label>
                                                            </div>
                                                        </td>
                                                        <td>
                                                            <p class="visible-xs visible-sm table-row-title">Subject</p>
                                                            <p><a href="#" onclick="bsbInboxViewMsg('${maskedMsgId}')" >${msg.subject}</a>
                                                            </p>
                                                        </td>
                                                        <td>
                                                            <p class="visible-xs visible-sm table-row-title">Message Type</p>
                                                            <p><iais:code code="${msg.msgType}"/></p>
                                                        </td>
                                                        <td>
                                                            <p class="visible-xs visible-sm table-row-title">Facility Name</p>
                                                            <p><iais:code code="${msg.facilityName}"/></p>
                                                        </td>
                                                        <td>
                                                            <p class="visible-xs visible-sm table-row-title">Reference Number</p>
                                                            <p>${msg.refNo}</p>
                                                        </td>
                                                        <td>
                                                            <p class="visible-xs visible-sm table-row-title">Submission Type</p>
                                                            <p><iais:code code="${msg.submissionType}"/></p>
                                                        </td>
                                                        <td>
                                                            <p class="visible-xs visible-sm table-row-title">Date of Notification</p>
                                                            <p><fmt:formatDate value="${msg.createdAt}" pattern="dd/MM/yyyy HH:mm:ss"/></p>
                                                        </td>
                                                        </tr>
                                                    </c:forEach>
                                                </c:otherwise>
                                            </c:choose>
                                            </tbody>
                                        </table>
                                        <div class="row"><span data-err-ind="archiveInfo" class="error-msg"></span></div>
                                        <div class="row" style="margin-top: 1.5%">
                                            <div class="col-md-12">
                                                <c:if test="${msgPage == 'inbox'}">
                                                    <div class="col-md-6 pull-right">
                                                        <button type="button" class="btn btn-primary" id="doArchive" style="margin-right: 10px;">Archive</button>
                                                        <button type="button" class="btn btn-primary pull-right" id="archive">Access Archive</button>
                                                    </div>
                                                </c:if>
                                                <c:if test="${msgPage == 'archive'}">
                                                    <div class="col-md-6 pull-right">
                                                        <button type="button" class="btn btn-primary" id="moveArchive" style="margin-right: 10px;">Move to Inbox</button>
                                                        <button type="button" class="btn btn-primary pull-right" id="inbox">Return to Inbox</button>
                                                    </div>
                                                </c:if>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <!-- Modal -->
                            <div class="modal fade" id="archiveModal" role="dialog" aria-labelledby="myModalLabel" style="left: 50%;top: 50%;transform: translate(-50%,-50%);min-width:80%; overflow: visible;bottom: inherit;right: inherit;">
                                <div class="modal-dialog modal-dialog-centered">
                                    <div class="modal-content">
                                        <div class="modal-body">
                                            <div class="row">
                                                <div class="col-md-12"><span style="font-size: 2rem">Please select at least one record</span></div>
                                            </div>
                                        </div>
                                        <div class="modal-footer">
                                            <button type="button" class="btn btn-secondary btn-md" data-dismiss="modal">Close</button>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <!--Modal End-->
                            <!-- Modal -->
                            <div class="modal fade " id="isArchivedModal" role="dialog" aria-labelledby="myModalLabel" style="left: 50%;top: 50%;transform: translate(-50%,-50%);min-width:80%; overflow: visible;bottom: inherit;right: inherit;">
                                <div class="modal-dialog modal-dialog-centered">
                                    <div class="modal-content">
                                        <div class="modal-body">
                                            <div class="row">
                                                <div class="col-md-12"><span style="font-size: 2rem">The message(s) is/are archived</span></div>
                                            </div>
                                        </div>
                                        <div class="modal-footer">
                                            <button type="button" class="btn btn-secondary btn-md" data-dismiss="modal">Close</button>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <!--Modal End-->
                            <div class="modal fade" id="doArchiveModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" style="left: 50%;top: 50%;transform: translate(-50%,-50%);min-width:80%; overflow: visible;bottom: inherit;right: inherit;">
                                <div class="modal-dialog modal-dialog-centered">
                                    <div class="modal-content">
                                        <div class="modal-body">
                                            <div class="row">
                                                <div class="col-md-12"><span style="font-size: 2rem">Are you sure you want to archive ?</span></div>
                                            </div>
                                        </div>
                                        <div class="modal-footer">
                                            <button type="button" class="btn btn-secondary btn-md" data-dismiss="modal">Close</button>
                                            <button type="button" class="btn btn-primary btn-md" id="confirmArchive">Confirm</button>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <!--Modal End-->
                        </form>

                    </div>
                </div>
            </div>
        </div>
    </div>
</div>