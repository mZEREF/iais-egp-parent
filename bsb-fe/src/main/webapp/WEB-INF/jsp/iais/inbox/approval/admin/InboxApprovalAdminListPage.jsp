<%@ page import="static sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT" %>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib uri="ecquaria/sop/egov-smc" prefix="egov-smc" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.ecq.com/iais-bsb" prefix="iais-bsb" %>
<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ page import="sg.gov.moh.iais.egp.bsb.util.TableDisplayUtil" %>
<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-internet"/>

<link href="<%=WEB_ROOT%>/css/bsb/bsb-common.css" rel="stylesheet"/>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-common.js"></script>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-inbox.js"></script>


<%@include file="../../dashboard/dashboard.jsp"%>
<%@include file="/WEB-INF/jsp/iais/include/showErrorMsg.jsp"%>

<div class="main-content">
    <div class="container">
        <div class="row">
            <div class="col-xs-12">
                <div class="tab-gp dashboard-tab" style="margin-left: 6px;margin-right: -8px;">
                    <%@ include file="../../InnerNavBar.jsp"%>

                    <div class="tab-content">
                        <form class="" method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
                            <div class="tab-search">

                                <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
                                <input type="hidden" name="action_type" value="">
                                <input type="hidden" name="action_value" value="">
                                <input type="hidden" name="action_additional" value="">


                                <%--@elvariable id="inboxApprovalSearchDto" type="sg.gov.moh.iais.egp.bsb.dto.inbox.InboxApprovalSearchDto"--%>
                                <div class="row">
                                    <label for="searchApprovalNo" class="col-sm-3 col-md-2 control-label">Approval No.:</label>
                                    <div class="col-sm-7 col-md-5">
                                        <input type="text" id="searchApprovalNo" name="searchApprovalNo" value="${inboxApprovalSearchDto.searchApprovalNo}"/>
                                        <span data-err-ind="searchApprovalNo" class="error-msg"></span>
                                    </div>
                                </div>
                                <div class="row" style="margin-bottom: 15px">
                                    <label for="searchProcessType" class="col-sm-3 col-md-2 control-label">Process Type:</label>
                                    <div class="col-sm-7 col-md-5">
                                        <select id="searchProcessType" name="searchProcessType">
                                            <option value='<c:out value=""/>' <c:if test="${inboxApprovalSearchDto.searchProcessType eq ''}">selected="selected"</c:if>>All</option>
                                            <%--@elvariable id="processTypeOps" type="java.util.List<com.ecquaria.cloud.moh.iais.common.dto.SelectOption>"--%>
                                            <c:forEach var="appStatusItem" items="${processTypeOps}">
                                                <option value='<c:out value="${appStatusItem.value}"/>' <c:if test="${inboxApprovalSearchDto.searchProcessType eq appStatusItem.value}">selected="selected"</c:if> ><c:out value="${appStatusItem.text}"/></option>
                                            </c:forEach>
                                        </select>
                                        <span data-err-ind="searchProcessType" class="error-msg"></span>
                                    </div>
                                </div>
                                <div class="row" style="margin-bottom: 15px">
                                    <label for="searchStatus" class="col-sm-3 col-md-2 control-label">Status:</label>
                                    <div class="col-sm-7 col-md-5">
                                        <select id="searchStatus" name="searchStatus">
                                            <option value='<c:out value=""/>' <c:if test="${inboxApprovalSearchDto.searchStatus eq ''}">selected="selected"</c:if>>All</option>
                                            <%--@elvariable id="approvalStatusOps" type="java.util.List<com.ecquaria.cloud.moh.iais.common.dto.SelectOption>"--%>
                                            <c:forEach var="approvalStatusItem" items="${approvalStatusOps}">
                                                <option value='<c:out value="${approvalStatusItem.value}"/>' <c:if test="${inboxApprovalSearchDto.searchStatus eq approvalStatusItem.value}">selected="selected"</c:if> ><c:out value="${approvalStatusItem.text}"/></option>
                                            </c:forEach>
                                        </select>
                                        <span data-err-ind="searchStatus" class="error-msg"></span>
                                    </div>
                                </div>
                                <div class="row" style="margin-bottom: 15px">
                                    <div class="col-xs-12 col-sm-6">
                                        <label class="col-xs-12 col-sm-4 control-label" style="padding-left: 0">Start Date</label>
                                        <div class="col-xs-12 col-sm-8">
                                            <iais:datePicker id="searchStartDateFrom" name="searchStartDateFrom" value="${inboxApprovalSearchDto.searchStartDateFrom}"/>
                                        </div>
                                        <span data-err-ind="searchStartDateFrom" class="error-msg"></span>
                                    </div>
                                    <div class="col-xs-12 col-sm-6">
                                        <label class="col-xs-2 col-sm-2 control-label" style="padding-left: 0">To</label>
                                        <div class="col-xs-12 col-sm-8">
                                            <iais:datePicker id="searchStartDateTo" name="searchStartDateTo" value="${inboxApprovalSearchDto.searchStartDateTo}"/>
                                        </div>
                                        <span data-err-ind="searchStartDateTo" class="error-msg"></span>
                                    </div>
                                </div>
                                <div class="row">
                                    <div class="col-xs-12 col-sm-6">
                                        <label class="col-xs-12 col-sm-4 control-label" style="padding-left: 0">Expiry Date</label>
                                        <div class="col-xs-12 col-sm-8">
                                            <iais:datePicker id="searchExpiryDateFrom" name="searchExpiryDateFrom" value="${inboxApprovalSearchDto.searchExpiryDateFrom}"/>
                                        </div>
                                        <span data-err-ind="searchExpiryDateFrom" class="error-msg"></span>
                                    </div>
                                    <div class="col-xs-12 col-sm-6">
                                        <label class="col-xs-2 col-sm-2 control-label" style="padding-left: 0">To</label>
                                        <div class="col-xs-12 col-sm-8">
                                            <iais:datePicker id="searchExpiryDateTo" name="searchExpiryDateTo" value="${inboxApprovalSearchDto.searchExpiryDateTo}"/>
                                        </div>
                                        <span data-err-ind="searchExpiryDateTo" class="error-msg"></span>
                                    </div>
                                </div>
                                <div class="row text-right text-center-mobile">
                                    <button class="btn btn-secondary" type="reset" id="clearBtn" name="clearBtn">Clear</button>
                                    <button class="btn btn-primary" type="button" id="searchBtn" name="searchBtn">Search</button>
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
                                                <iais:sortableHeader needSort="true" field="approveNo" value="Approval No." isFE="true" style="width:12%"/>
                                                <iais:sortableHeader needSort="true" field="processType" value="Process Type" isFE="true" style="width:12%"/>
                                                <iais:sortableHeader needSort="true" field="status" value="Status" isFE="true" style="width:10%"/>
                                                <iais:sortableHeader needSort="false" field="facilityName" value="Facility" isFE="true" style="width:8%"/>
                                                <iais:sortableHeader needSort="false" field="" value="Facility Address" isFE="true" style="width:14%"/>
                                                <iais:sortableHeader needSort="true" field="approvalStartDate" value="Start Date" isFE="true" style="width:13%"/>
                                                <iais:sortableHeader needSort="true" field="approvalExpiryDate" value="Expiry Date" isFE="true" style="width:14%"/>
                                                <iais:sortableHeader needSort="false" field="" value="Download" isFE="true" style="width:5%"/>
                                                <iais:sortableHeader needSort="false" field="" value="Actions" isFE="true" style="width:12%"/>
                                            </tr>
                                            </thead>
                                            <tbody>
                                            <c:choose>
                                                <%--@elvariable id="dataList" type="java.util.List<sg.gov.moh.iais.egp.bsb.dto.inbox.InboxApprovalFacAdminResultDto$ApprovalInfo>"--%>
                                                <c:when test="${empty dataList}">
                                                    <tr>
                                                        <td colspan="6">
                                                            <iais:message key="GENERAL_ACK018" escape="true"/>
                                                        </td>
                                                    </tr>
                                                </c:when>
                                                <c:otherwise>
                                                    <c:forEach var="approval" items="${dataList}" varStatus="status">
                                                        <tr>
                                                            <td>
                                                                <p class="visible-xs visible-sm table-row-title">Approval No.</p>
                                                                <p><c:out value="${approval.approveNo}"/></p>
                                                            </td>
                                                            <td>
                                                                <p class="visible-xs visible-sm table-row-title">Process Type</p>
                                                                <p><iais:code code="${approval.processType}"/></p>
                                                            </td>
                                                            <td>
                                                                <p class="visible-xs visible-sm table-row-title">Status</p>
                                                                <p style="text-align: center"><iais:code code="${approval.status}"/></p>
                                                            </td>
                                                            <td>
                                                                <p class="visible-xs visible-sm table-row-title">Facility</p>
                                                                <p style="text-align: center"><c:out value="${approval.facilityName}"/></p>
                                                            </td>
                                                            <td>
                                                                <p class="visible-xs visible-sm table-row-title">Facility Address</p>
                                                                <p><c:out value="${TableDisplayUtil.getOneLineAddress(approval.blkNo, approval.streetName, approval.floorNo, approval.unitNo, approval.postalCode)}"/></p>
                                                            </td>
                                                            <td>
                                                                <p class="visible-xs visible-sm table-row-title">Start Date</p>
                                                                <p><fmt:formatDate value="${approval.approvalStartDate}" pattern="dd MMM yyyy"/></p>
                                                            </td>
                                                            <td>
                                                                <p class="visible-xs visible-sm table-row-title">Expiry Date</p>
                                                                <p><fmt:formatDate value="${approval.approvalExpiryDate}" pattern="dd MMM yyyy"/></p>
                                                            </td>
                                                            <td>
                                                                <p class="visible-xs visible-sm table-row-title">Download</p>
                                                                <p></p>
                                                            </td>
                                                            <td>
                                                                <p class="visible-xs visible-sm table-row-title">Actions</p>
                                                                <select id="approvalAction${status.index}" name="approvalAction${status.index}" data-action-select="">
                                                                    <option value="#" selected="selected">Select</option>
                                                                </select>
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
                                        <div class="row" style="margin-top: 1.5%">
                                            <div class="col-md-12">
                                                <div class="col-md-6 pull-right">
                                                    <button type="button" class="btn btn-primary" id="doArchive" style="margin-right: 10px; display: none">Archive</button>
                                                    <button style="display: none" type="button" class="btn btn-primary pull-right" onclick="toArchiveView()">Access Archive</button>
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