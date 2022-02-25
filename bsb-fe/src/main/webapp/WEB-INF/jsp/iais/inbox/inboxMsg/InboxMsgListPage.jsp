<%@ page import="static sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT" %>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
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


<%@include file="../dashboard/dashboard.jsp"%>


<div class="main-content">
    <div class="container">
        <div class="row">
            <div class="col-xs-12">
                <div class="tab-gp dashboard-tab" style="margin-left: 6px;margin-right: -8px;">
                    <%@ include file="../InnerNavBar.jsp"%>


                    <div class="tab-content">
                        <form class="" method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
                            <div class="tab-search">

                                <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
                                <input type="hidden" name="action_type" value="">
                                <input type="hidden" name="action_value" value="">
                                <input type="hidden" name="action_additional" value="">


                                <%--@elvariable id="inboxMsgSearchDto" type="sg.gov.moh.iais.egp.bsb.dto.inbox.InboxMsgSearchDto"--%>
                                <div class="row">
                                    <div class="col-md-3">
                                        <label class="col-md-3 control-label" for="searchMsgType" style="margin-top:5%;">Type</label>
                                        <div class="col-md-8">
                                            <iais:select name="searchMsgType" id="searchMsgType" options="msgTypeOps" value="${inboxMsgSearchDto.searchMsgType}" firstOption="All"/>
                                        </div>
                                    </div>
                                    <div class="col-md-5">
                                        <label class="col-md-5 control-label" for="searchAppType" style="margin-top:3%;">Application Type</label>
                                        <div class="col-md-7">
                                            <iais:select name="searchAppType" id="searchAppType" options="msgAppTypeOps" value="${inboxMsgSearchDto.searchAppType}" firstOption="All"/>
                                        </div>
                                    </div>
                                    <div class="col-md-4">
                                        <div class="col-xs-12 visible-xs visible-sm" style="height: 20px;">
                                        </div>
                                        <div class="col-xs-12">
                                            <div class="search-wrap" style="width: 100%">
                                                <iais:value>
                                                    <div class="input-group">
                                                        <input class="form-control" id="searchSubject" type="text"
                                                               placeholder="Search your keywords" name="searchSubject"
                                                               aria-label="searchSubject" maxlength="50" value="${inboxMsgSearchDto.searchSubject}" />
                                                        <span class="input-group-btn">
                                    <button class="btn btn-default buttonsearch" title="Search your keywords" id="searchSubjectBtn"><em class="fa fa-search"></em></button>
                                </span>
                                                    </div>
                                                </iais:value>
                                            </div>
                                        </div>
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
                                                <%-- need to use new tag in future --%>
                                                <th scope="col" style="display: none"></th>
                                                <iais:sortableHeader needSort="true" field="subject" value="Subject" style="width:25%" isFE="true"/>
                                                <iais:sortableHeader needSort="true" field="msgType" value="Message Type" isFE="true"/>
                                                <iais:sortableHeader needSort="true" field="refNo" value="Ref. No." isFE="true"/>
                                                <iais:sortableHeader needSort="true" field="appType" value="Application Type" isFE="true"/>
                                                <iais:sortableHeader needSort="true" field="createdAt" value="Date" isFE="true"/>
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
                                                        <c:choose>
                                                            <c:when test="${msg.status == 'MSGRS001' || msg.status == 'MSGRS002'}">
                                                                <tr style="font-weight:bold">
                                                            </c:when>
                                                            <c:otherwise>
                                                                <tr>
                                                            </c:otherwise>
                                                        </c:choose>
                                                        <%--                                <C:if test="${msgPage == 'msgView'}">--%>
                                                        <%--                                    <td>--%>
                                                        <%--                                        <div class="form-check">--%>
                                                        <%--                                            <input class="form-check-input msgCheck" id="msgCheck" type="checkbox" name="msgIdList" aria-invalid="false" value="${inboxQuery.id}"--%>
                                                        <%--                                                   <c:if test="${inboxQuery.status == 'MSGRS001' || inboxQuery.status == 'MSGRS002'}">disabled = "disabled"</c:if>>--%>
                                                        <%--                                            <label class="form-check-label" for="msgCheck"><span--%>
                                                        <%--                                                    class="check-square"></span>--%>
                                                        <%--                                            </label>--%>
                                                        <%--                                        </div>--%>
                                                        <%--                                    </td>--%>
                                                        <%--                                </C:if>--%>
                                                        <td>
                                                            <p class="visible-xs visible-sm table-row-title">Subject</p>
                                                            <p><a href="#" onclick="bsbInboxViewMsg('<iais:mask name="action_value" value="${msg.id}"/>')">${msg.subject}</a>
                                                            </p>
                                                        </td>
                                                        <td>
                                                            <p class="visible-xs visible-sm table-row-title">Message Type</p>
                                                            <p><iais:code code="${msg.msgType}"/></p>
                                                        </td>
                                                        <td>
                                                            <p class="visible-xs visible-sm table-row-title">Ref. No.</p>
                                                            <p>${msg.refNo}</p>
                                                        </td>
                                                        <td>
                                                            <p class="visible-xs visible-sm table-row-title">Application Type</p>
                                                            <p><iais:code code="${msg.appType}"/></p>
                                                        </td>
                                                        <td>
                                                            <p class="visible-xs visible-sm table-row-title">Date</p>
                                                            <p><fmt:formatDate value="${msg.createdAt}" pattern="dd/MM/yyyy HH:mm:ss"/></p>
                                                        </td>
                                                        </tr>
                                                    </c:forEach>
                                                </c:otherwise>
                                            </c:choose>
                                            </tbody>
                                        </table>
                                        <!-- Modal -->
                                        <div class="modal fade" id="archiveModal" role="dialog" aria-labelledby="myModalLabel" style="left: 50%;top: 50%;transform: translate(-50%,-50%);min-width:80%; overflow: visible;bottom: inherit;right: inherit;">
                                            <div class="modal-dialog" role="document">
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
                                        <div class="modal fade" id="isArchivedModal" role="dialog" aria-labelledby="myModalLabel" style="left: 50%;top: 50%;transform: translate(-50%,-50%);min-width:80%; overflow: visible;bottom: inherit;right: inherit;">
                                            <div class="modal-dialog" role="document">
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
                                            <div class="modal-dialog" role="document">
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
                                        <div class="row" style="margin-top: 1.5%">
                                            <div class="col-md-12">
                                                <div class="col-md-6 pull-right">
                                                    <button type="button" class="btn btn-primary" id="doArchive" style="margin-right: 10px; display:none; ">Archive</button>
                                                    <button type="button" class="btn btn-primary pull-right" onclick="toArchiveView()" style="display: none">Access Archive</button>
                                                </div>
                                            </div>
                                        </div>
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