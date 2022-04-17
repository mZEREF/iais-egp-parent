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
<webui:setLayout name="iais-intranet"/>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-common.js"></script>
<div class="main-content" style="min-height: 73vh;">
    <form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
        <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
        <input type="hidden" name="action_type" value="">
        <input type="hidden" name="action_value" value="">
        <input type="hidden" name="action_additional" value="">
        <div class="col-xs-12">
            <div class="center-content">
                <div class="intranet-content">
                    <div class="col-xs-12">
                        <div class="components">
                            <h3>
                                <span>Request For Information List</span>
                            </h3>
                            <div class="table-gp">
                                <iais:row style="text-align:left;">
                                    <label class="col-xs-9 col-md-6">
                                        <button class="btn btn-primary" type="button" id="newBtn">New</button>
                                    </label>
                                    <label class="col-xs-9 col-md-4">
                                        <input type="text"  style=" font-weight:normal;" name="searchNo" maxlength="100" value="${adhocSearchDto.facilityNo}" />
                                    </label>
                                    <label class="col-xs-9 col-md-2">
                                        <button class="btn btn-primary" id="searchBtn" type="button">Search</button>
                                    </label>
                                </iais:row>
                                <br>
                                <br>
                                <br>
                                <br>
                                <h3>
                                    <span>Search Results</span>
                                </h3>
                                <%--@elvariable id="pageInfo" type="sg.gov.moh.iais.egp.bsb.dto.PageInfo"--%>
                                <iais-bsb:Pagination size="${pageInfo.size}" pageNo="${pageInfo.pageNo + 1}" pageAmt="${pageInfo.totalPages}" totalElements="${pageInfo.totalElements}"/>
                                <table aria-describedby="" class="table" style="border-collapse:collapse;">
                                    <thead>
                                    <tr>
                                        <th scope="col" style="display: none"></th>
                                        <iais:sortableHeader needSort="false" field="" value="S/N"></iais:sortableHeader>
                                        <iais:sortableHeader needSort="false"  field="FACILITY_NO" value="Facility No."></iais:sortableHeader>
                                        <iais:sortableHeader needSort="false"  field="Email" value="Email"></iais:sortableHeader>
                                        <iais:sortableHeader needSort="false"  field="CREATED_DT" value="Start Date"></iais:sortableHeader>
                                        <iais:sortableHeader needSort="false"  field="DUE_DATE" value="Due Date"></iais:sortableHeader>
                                        <iais:sortableHeader needSort="false" field="" value="Action"></iais:sortableHeader>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <c:choose>
                                        <c:when test="${empty infoList}">
                                            <tr>
                                                <td colspan="7">
                                                    <iais:message key="GENERAL_ACK018" escape="true"></iais:message>
                                                </td>
                                            </tr>
                                        </c:when>
                                        <c:otherwise>
                                            <c:forEach var="info" items="${infoList}" varStatus="status">
                                                <tr>
                                                    <td class="row_no"><c:out value="${status.index + 1}"/></td>
                                                    <td><c:out value="${info.facilityNo}"/></td>
                                                    <td><c:out value="${info.email}"/></td>
                                                    <td><iais-bsb:format-LocalDate localDate='${info.startDate}'/></td>
                                                    <td><iais-bsb:format-LocalDate localDate='${info.dueDate}'/></td>
                                                    <td>
                                                        <iais:action >
                                                            <a href="#" onclick="">View</a>
                                                        </iais:action>
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
                </div>
            </div>
        </div>
        <input name="from" id="from" value="" hidden>
    </form>
</div>
<script>
    $(function () {
        $("#searchBtn").click(function () {
            showWaiting();
            $("[name='action_type']").val("search");
            $("#mainForm").submit();
        });
        $("#newBtn").click(function () {
            showWaiting();
            $("[name='action_type']").val("new");
            $("#mainForm").submit();
        });
    });
</script>