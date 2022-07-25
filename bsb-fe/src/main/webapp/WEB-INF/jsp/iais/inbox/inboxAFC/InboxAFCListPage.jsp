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


<%@include file="../dashboard/dashboard.jsp"%>


<div class="main-content">
    <div class="container">
        <div class="row">
            <div class="col-xs-12">
                <div class="tab-gp dashboard-tab" style="margin-left: 6px;margin-right: -8px;">
                    <%@ include file="../InnerNavBar.jsp"%>

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
                                                <c:forEach var="item" items="${roleInFacOps}">
                                                    <option value='<c:out value="${item.value}"/>' <c:if test="${InboxFacSearchDto.role eq item.value}">selected="selected"</c:if> ><c:out value="${item.text}"/></option>
                                                </c:forEach>
                                            </select>
                                            <span data-err-ind="role" class="error-msg"></span>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <div class="row text-right text-center-mobile">
                                <button class="btn btn-secondary" type="reset" id="clearBtn" name="clearBtn">Clear</button>
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
                                                <iais:sortableHeader needSort="true" field="facilityName" value="Facility Name" isFE="true" style="width:11%"/>
                                                <iais:sortableHeader needSort="true" field="facilityClassification" value="Facility Classification" isFE="true" style="width:15%"/>
                                                <iais:sortableHeader needSort="false" field="" value="Facility Address" isFE="true" style="width:20%"/>
                                                <iais:sortableHeader needSort="true" field="roleInFac" value="Role" isFE="true" style="width:12%"/>
                                                <iais:sortableHeader needSort="true" field="status" value="Status" isFE="true" style="width:10%"/>
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
                                                                <a href="#"><c:out value="${fac.facilityNo}"/></a>
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
                                                                <p class="visible-xs visible-sm table-row-title">Status</p>
                                                                <p><iais:code code="${fac.status}"/></p>
                                                            </td>
                                                            <td>
                                                                <p class="visible-xs visible-sm table-row-title">Actions</p>
                                                                <label for="facAction${status.index}"></label><select name="facAction${status.index}" class="facActionDropdown${status.index}" id="facAction${status.index}" data-action-select="">
                                                                    <option value="">Select</option>
                                                                    <option value="/bsb-web/eservicecontinue/INTERNET/MohApprovalBatAndActivity?facId=<iais:mask name='applyApprovalFacId' value='${fac.facilityId}'/>">Apply for Approval</option>
                                                                    <option value="">Renew</option>
                                                                    <option value="">Update</option>
                                                                    <option value="">Data Submission</option>
                                                                    <option value="">Deregister</option>
                                                                </select>
                                                            </td>
                                                        </tr>
                                                    </c:forEach>
                                                </c:otherwise>
                                            </c:choose>
                                            </tbody>
                                        </table>
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