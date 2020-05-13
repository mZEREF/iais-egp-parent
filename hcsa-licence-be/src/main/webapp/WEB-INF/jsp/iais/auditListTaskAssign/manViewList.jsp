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
<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>

<form id = "mainForm" method = "post" action=<%=process.runtime.continueURL()%>>
    <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
    <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
    <input type="hidden" name="crud_action_type" value="">
    <input type="hidden" name="crud_action_value" value="">
    <input type="hidden" name="crud_action_additional" value="">

    <div class="main-content">
        <div class="container">
            <div class="row">
                <div class="col-xs-12">
                    <div class="instruction-content center-content">
                        <h2>Search Results - HCIs and Services</h2>
                        <div class="table-gp">
                            <table class="table">
                                <thead>
                                <tr>
                                    <th></th>
                                    <th>Service Name</th>
                                    <th>Postal Code</th>
                                    <th>Last Inspection done before(Start) </th>
                                    <th>Last Inspection done before(End) </th>
                                    <th>Results of last Compliance</th>
                                    <th>HCSA Service Code</th>
                                    <th>HCI Code</th>
                                    <th>Premises Type</th>
                                    <th>Type of Risk</th>
                                </tr>
                                </thead>
                                <tbody>
                                <span class="error-msg" id="error_selectedOne" name="iaisErrorMsg"></span>
                                <c:if test="${empty auditTaskDataDtos}">
                                    <tr>
                                        <td colspan="10" align="center">
                                            <iais:message key="ACK018" escape="true"/>
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
                                        <td><c:out value="${item.premisesType}"/></td>
                                        <td><c:out value="${item.riskType}"/></td>
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
                            <button class="btn btn-primary next" type="button" onclick="javascript:addToAudit();;">
                             ADD INTO MANUAL AUDIT LIST
                            </button>
                        </iais:action>
                    </div>

                </div>
            </div>
        </div>
    </div>
</form>
<%@ include file="/WEB-INF/jsp/include/validation.jsp" %>
<script type="text/javascript">
    function addToAudit() {
        SOP.Crud.cfxSubmit("mainForm","donext");
    }
    function cancel() {
        SOP.Crud.cfxSubmit("mainForm","doback");
    }

</script>
