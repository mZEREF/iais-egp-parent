<%--
  Created by IntelliJ IDEA.
  User: JiaHao_Chen
  Date: 2019/11/13
  Time: 16:29
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://www.ecq.com/iais"   prefix="iais"%>
<webui:setLayout name="iais-intranet"/>
<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts" %>
<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>
<div class="main-content">
<form id = "mainForm" method = "post" action=<%=process.runtime.continueURL()%>>
    <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>


            <div class="row">
                <div class="col-xs-12">
                    <div class="instruction-content center-content">
                        <h2>Search Results - HCIs and Services</h2>
                        <iais:pagination param="auditTaskDataDtos_pram" result="auditTaskDataDtosResult"/>
                        <div class="table-gp">
                            <table aria-describedby="" class="table">
                                <thead>
                                <tr>
                                    <th scope="col" ></th>
                                    <th scope="col" >Service Name</th>
                                    <th scope="col" >Postal Code</th>
                                    <th scope="col" >Last Inspection done before (Start) </th>
                                    <th scope="col" >Last Inspection done before (End) </th>
                                    <th scope="col" >Results of last Compliance</th>
                                    <th scope="col" >HCSA Service Code</th>
                                    <th scope="col" >HCI Code</th>
                                    <th scope="col" >Mode of Service Delivery</th>
                                    <th scope="col" >Type of Risk</th>
                                </tr>
                                </thead>
                                <tbody>
                                <span class="error-msg" id="error_selectedOne" name="iaisErrorMsg"></span>
                                <c:if test="${empty auditTaskDataDtos}">
                                    <tr>
                                        <td colspan="10" align="left">
                                            <iais:message key="GENERAL_ACK018" escape="true"/>
                                        </td>
                                    </tr>
                                </c:if>
                                <c:forEach var = "item" items = "${auditTaskDataDtos}" varStatus="status">
                                    <tr>
                                        <c:set var="id" value="${status.index}"></c:set>
                                        <td> <input name="<c:out value="${id}"/>selectForAuditList" id="<c:out value="${id}"/>selectForAuditList" type="checkbox" value="ad" <c:if test="${item.addAuditList}">checked</c:if>/></td>
                                        <td><c:out value="${item.svcName}"/></td>
                                        <td><c:out value="${item.postalCode}"/></td>
                                        <td><c:out value="${item.lastInspStart}"/></td>
                                        <td><c:out value="${item.lastInspEnd}"/></td>
                                        <td><c:out value="${item.resultComplicance}"/></td>
                                        <td><c:out value="${item.svcCode}"/></td>
                                        <td><c:out value="${item.hclCode}"/></td>
                                        <td><c:out value="${item.premisesType == ApplicationConsts.PREMISES_TYPE_EAS_MTS_CONVEYANCE ? ApplicationConsts.PREMISES_TYPE_EAS_MTS_CONVEYANCE_SHOW : item.premisesType}"/></td>
                                        <td><iais:code code="${item.riskType}"></iais:code></td>
                                    </tr>
                                </c:forEach>
                                </tbody>
                            </table>
                            <div class="table-footnote">
                            </div>
                        </div>
                        <iais:action style="text-align:right;">
                            <button class="btn btn-secondary" id="clearbtn" type="button"
                                    onclick="javascript:cancel();">
                                Cancel
                            </button>
                            <c:if test="${not empty auditTaskDataDtos}">
                            <button class="btn btn-primary next" type="button" onclick="javascript:addToAudit();;">
                             ADD INTO MANUAL AUDIT LIST
                            </button>
                            </c:if>
                        </iais:action>
                    </div>

                </div>
            </div>

</form>
</div>
<%@ include file="/WEB-INF/jsp/include/validation.jsp" %>
<script type="text/javascript">
    function addToAudit() {
        showWaiting();
        SOP.Crud.cfxSubmit("mainForm","donext");
    }
    function cancel() {
        showWaiting();
        SOP.Crud.cfxSubmit("mainForm","doback");
    }
    function jumpToPagechangePage() {
        SOP.Crud.cfxSubmit("mainForm", "changePage");
    }
</script>
