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
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<webui:setLayout name="iais-intranet"/>
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
                        <h2>${modulename}</h2>
                        <div class="table-gp">
                            <table aria-describedby="" class="table">
                                <thead>
                                <tr>
                                    <th scope="col" >HCI Code</th>
                                    <th scope="col" >HCI Name</th>
                                    <th scope="col" >Address</th>
                                    <th scope="col" >Service Name</th>
                                    <th scope="col" >Audit Type</th>
                                    <c:if test="${ISTUC}">
                                        <th scope="col" >
                                            TCU Audit Due Date
                                        </th>
                                    </c:if>
                                    <th scope="col" >Assign task to Inspector</th>
                                    <th scope="col" >Select for Audit</th>
                                    <c:if test="${ISTUC}">
                                        <th scope="col" >
                                            Announced Audit
                                        </th>
                                    </c:if>
                                </tr>
                                </thead>
                                <tbody>

                                <c:forEach var = "item" items = "${auditTaskDataDtos}" varStatus="status">
                                    <c:if test="${item.selectedForAudit}">
                                        <tr>
                                            <c:set var="id" value="${status.index}"></c:set>
                                            <td><c:out value="${item.hclCode}"/></td>
                                            <td><c:out value="${empty item.hclName ? 'N/A' :item.hclName}"/></td>
                                            <td><c:out value="${empty item.address ? 'N/A' : item.address}"/></td>
                                            <td><c:out value="${item.svcName}"/></td>
                                            <td><iais:code code="${item.auditType}"/></td>
                                            <c:if test="${ISTUC}">
                                                <td>
                                                    <fmt:formatDate value="${item.tcuDate}" pattern="dd/MM/yyyy"/>
                                                </td>
                                            </c:if>
                                            <td>
                                                <c:out value="${item.inspector}"/>
                                            </td>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${item.selectedForAudit}">
                                                        <p>Audit Confirmed</p>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <p><iais:select name="${id}auditType" options="aduitTypeOp" firstOption="Please Select" value="${item.auditType}"/></p>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                            <c:if test="${ISTUC}">
                                                <td>
                                                    <input type="checkbox" name = "announced${status.index}" value = "announced"/>
                                                </td>
                                            </c:if>
                                        </tr>
                                    </c:if>
                                </c:forEach>

                                </tbody>
                            </table>
                            <div class="table-footnote">
                            </div>
                        </div>
                        <iais:action style="text-align:right;">
                            <button type="button" class="btn btn-secondary" onclick="javascript:cancel('${actionCancel}');">Cancel
                            </button>
                            <button type="button" class="btn btn-primary next" onclick="javascript:confirm('${actionConfirm}');">Confirm
                                to Audit
                            </button>
                            <!--
                            <div class="col-xs-12 col-sm-6">
                            <div class="text-right text-center-mobile"><a class="btn btn-primary next" href="javascript:void(0);" onclick="javascript: remove();">Remove Audit Task</a></div>
                            </div> -->

                        </iais:action>
                    </div>
                </div>
            </div>

</form>
</div>
<script type="text/javascript">
    function confirm(act) {
        showWaiting();
        SOP.Crud.cfxSubmit("mainForm",act);
    }
    function cancel(act) {
        showWaiting();
        SOP.Crud.cfxSubmit("mainForm",act);
    }
    /*function remove() {
        SOP.Crud.cfxSubmit("mainForm","next");
    }
    function cancelAudit() {
        SOP.Crud.cfxSubmit("mainForm","next");
    }
    function createHcl() {
        SOP.Crud.cfxSubmit("mainForm","next");
    }*/
</script>
