<%@ page import="static sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT" %>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib uri="ecquaria/sop/egov-smc" prefix="egov-smc" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.ecq.com/iais-bsb" prefix="iais-bsb" %>
<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>
<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
    String webroot1 = IaisEGPConstant.CSS_ROOT + IaisEGPConstant.FE_CSS_ROOT;
%>
<webui:setLayout name="iais-internet"/>

<link href="<%=WEB_ROOT%>/css/bsb/bsb-common.css" rel="stylesheet"/>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-common.js"></script>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-inbox.js"></script>

<%@include file="../dashboard/dashboard.jsp" %>

<div class="main-content">
    <div class="container">
        <div class="row">
            <div class="col-xs-12">
                <div class="tab-gp dashboard-tab" style="margin-left: 6px;margin-right: -8px;">
                    <%@ include file="../InnerNavBar.jsp" %>
                    <div class="tab-content">
                        <form class="" method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
                            <div class="tab-search">
                                <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
                                <input type="hidden" name="action_type" value="">
                                <input type="hidden" name="action_value" value="">
                                <input type="hidden" name="action_additional" value="">
                                <%--@elvariable id="inboxAppSearchDto" type="sg.gov.moh.iais.egp.bsb.dto.inbox.InboxAppSearchDto"--%>
                                <div class="row">
                                    <label for="searchDataSubNo" class="col-sm-3 col-md-2 control-label">Submission ID:</label>
                                    <div class="col-sm-7 col-md-5">
                                        <input type="text" id="searchDataSubNo" name="searchDataSubNo" value=""/>
                                    </div>
                                </div>
                                <div class="row" style="margin-bottom: 15px">
                                    <label for="searchFacilityName" class="col-sm-3 col-md-2 control-label">Facility name:</label>
                                    <div class="col-sm-7 col-md-5">
                                        <iais:select name="searchFacilityName" id="searchFacilityName"
                                                     options="facilityName"
                                                     firstOption="All"
                                                     value=""/>
                                    </div>
                                </div>
                                <div class="row" style="margin-bottom: 15px">
                                    <label for="searchType" class="col-sm-3 col-md-2 control-label">Type:</label>
                                    <div class="col-sm-7 col-md-5">
                                        <select name="searchType" id="searchType">
                                            <option>All</option>
                                            <option value="DATTYPE001">Notification of Consume</option>
                                            <option value="DATTYPE002">Notification of Disposal and Inactivation</option>
                                            <option value="DATTYPE003">Notification of Export</option>
                                            <option value="DATTYPE004">Notification of Import</option>
                                            <option value="DATTYPE005">Notification of Transfer</option>
                                            <option value="DATTYPE006">Notification of Receipt</option>
                                            <option value="DATTYPE007">Red Teaming Report</option>
                                            <option value="DATTYPE008">Biological Agent & Toxins Inventory</option>
                                        </select>
                                    </div>
                                </div>
                                <div class="row" style="margin-bottom: 15px">
                                    <label for="searchStatus" class="col-sm-3 col-md-2 control-label">Status:</label>
                                    <div class="col-sm-7 col-md-5">
                                        <select name="searchStatus" id="searchStatus">
                                            <option>All</option>
                                            <option value="DATASTA001">Received</option>
                                            <option value="DATASTA002">Withdrawn</option>
                                        </select>
                                    </div>
                                </div>
                                <div class="row text-right text-center-mobile">
                                    <button class="btn btn-secondary" type="reset" id="clearBtn" name="clearBtn">Clear
                                    </button>
                                    <button class="btn btn-primary" type="button" id="searchBtn" name="searchBtn">
                                        Search
                                    </button>
                                </div>
                                <%--@elvariable id="pageInfo" type="sg.gov.moh.iais.egp.bsb.dto.PageInfo"--%>
                                <iais-bsb:Pagination size="${pageInfo.size}" pageNo="${pageInfo.pageNo + 1}" pageAmt="${pageInfo.totalPages}" totalElements="${pageInfo.totalElements}"/>
                            </div>

                            <div class="row">
                                <div class="col-xs-12">
                                    <div class="table-gp">
                                        <table class="table">
                                            <thead>
                                            <tr>
                                                <%-- need to use new tag in future --%>
                                                <iais:sortableHeader needSort="true" field="" value="Submission ID" isFE="true"/>
                                                <iais:sortableHeader needSort="true" field="" value="Type" isFE="true"/>
                                                <iais:sortableHeader needSort="true" field="" value="Status" isFE="true"/>
                                                <iais:sortableHeader needSort="true" field="" value="Facility" isFE="true"/>
                                                <iais:sortableHeader needSort="true" field="" value="Facility Address" isFE="true"/>
                                                <iais:sortableHeader needSort="true" field="" value="Submitted On" isFE="true"/>
                                                <iais:sortableHeader needSort="false" field="" value="Actions" isFE="true"/>
                                            </tr>
                                            </thead>
                                            <tbody>
                                            <c:choose>
                                                <%--@elvariable id="dataList" type="java.util.List<sg.gov.moh.iais.egp.bsb.entity.Application>"--%>
                                                <c:when test="${empty dataList}">
                                                    <tr>
                                                        <td colspan="6">
                                                            <iais:message key="GENERAL_ACK018" escape="true"/>
                                                        </td>
                                                    </tr>
                                                </c:when>
                                                <c:otherwise>
                                                    <c:forEach var="item" items="${dataList}" varStatus="status">
                                                        <tr>
                                                            <td>
                                                                <p class="visible-xs visible-sm table-row-title">Submission ID</p>
                                                                <a href="#"><c:out value="${item.submissionNo}"/></a>
                                                            </td>
                                                            <td>
                                                                <p class="visible-xs visible-sm table-row-title">Type</p>
                                                                <p style="text-align: center"><iais:code code="${item.type}"/></p>
                                                            </td>
                                                            <td>
                                                                <p class="visible-xs visible-sm table-row-title">Status</p>
                                                                <p><iais:code code="${item.status}"/></p>
                                                            </td>
                                                            <td>
                                                                <p class="visible-xs visible-sm table-row-title">Facility</p>
                                                                <p><c:out value="${item.facilityName}"/></p>
                                                            </td>
                                                            <td>
                                                                <p class="visible-xs visible-sm table-row-title">Facility Address</p>
                                                                <p><c:out value="${item.facilityAddress}"/></p>
                                                            </td>
                                                            <td>
                                                                <p class="visible-xs visible-sm table-row-title">Submitted On</p>
                                                                <p><fmt:formatDate value="${item.submittedOn}" pattern="dd/MM/yyyy HH:mm:ss"/></p>
                                                            </td>
                                                            <td>
                                                                <p class="visible-xs visible-sm table-row-title">Actions</p>
                                                                <select id="appAction${status.index}" name="appAction${status.index}" data-action-select="">
                                                                    <option value="#" selected="selected">Select</option>
<%--                                                                    <c:choose>--%>
<%--                                                                        <c:when test="${app.processType eq 'PROTYPE001' and app.status eq 'BSBAPST001'}">--%>
<%--                                                                            <option value="/bsb-fe/eservice/INTERNET/MohBsbFacilityRegistration?editId=<iais:mask name='editId' value='${app.id}'/>">--%>
<%--                                                                                Edit--%>
<%--                                                                            </option>--%>
<%--                                                                        </c:when>--%>
<%--                                                                        <c:when test="${(app.processType eq 'PROTYPE002' or app.processType eq 'PROTYPE003' or app.processType eq 'PROTYPE004') and app.status eq 'BSBAPST001'}">--%>
<%--                                                                            <option value="/bsb-fe/eservice/INTERNET/MohApprovalApplication?editId=<iais:mask name='editId' value='${app.id}'/>&processType=<iais:mask name='processType' value='${app.processType}'/>">--%>
<%--                                                                                Edit--%>
<%--                                                                            </option>--%>
<%--                                                                        </c:when>--%>
<%--                                                                        <c:when test="${app.processType eq 'PROTYPE005' and app.status eq 'BSBAPST001'}">--%>
<%--                                                                            <option value="/bsb-fe/eservice/INTERNET/MohFacilityCertifierRegistration?editId=<iais:mask name='editId' value='${app.id}'/>">--%>
<%--                                                                                Edit--%>
<%--                                                                            </option>--%>
<%--                                                                        </c:when>--%>
<%--                                                                    </c:choose>--%>
                                                                </select>
                                                            </td>
                                                        </tr>
                                                    </c:forEach>
                                                </c:otherwise>
                                            </c:choose>
                                            </tbody>
                                        </table>
                                        <!-- Modal -->
                                        <div class="modal fade" id="archiveModal" role="dialog"
                                             aria-labelledby="myModalLabel"
                                             style="left: 50%;top: 50%;transform: translate(-50%,-50%);min-width:80%; overflow: visible;bottom: inherit;right: inherit;">
                                            <div class="modal-dialog" role="document">
                                                <div class="modal-content">
                                                    <div class="modal-body">
                                                        <div class="row">
                                                            <div class="col-md-12"><span style="font-size: 2rem">Please select at least one record</span>
                                                            </div>
                                                        </div>
                                                    </div>
                                                    <div class="modal-footer">
                                                        <button type="button" class="btn btn-secondary btn-md"
                                                                data-dismiss="modal">Close
                                                        </button>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                        <!--Modal End-->
                                        <div class="row" style="margin-top: 1.5%">
                                            <div class="col-md-12">
                                                <div class="col-md-6 pull-right">
                                                    <button type="button" class="btn btn-primary" id="doArchive"
                                                            style="margin-right: 10px; display: none">Archive
                                                    </button>
                                                    <button style="display: none" type="button"
                                                            class="btn btn-primary pull-right"
                                                            onclick="toArchiveView()">Access Archive
                                                    </button>
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