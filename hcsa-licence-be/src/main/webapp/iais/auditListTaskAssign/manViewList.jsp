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
    <%@ include file="/include/formHidden.jsp" %>
    <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
    <input type="hidden" name="crud_action_type" value="">
    <input type="hidden" name="crud_action_value" value="">
    <input type="hidden" name="crud_action_additional" value="">

    <div class="main-content">
        <div class="container">
            <div class="row">
                <div class="col-xs-12">
                    <div class="instruction-content center-content">
                        <h2>System Audit List</h2>
                        <div class="gray-content-box">
                            <div class="table-gp">
                                <table class="table">
                                    <thead>
                                    <tr>
                                        <th></th>
                                        <th>Service Name</th>
                                        <th>Postal Code/Region </th>
                                        <th>Last Inspection done before(Start) </th>
                                        <th>Last Inspection done before(End) </th>
                                        <th>Results of last Compliance</th>
                                        <th>Hcsa Service Code</th>
                                        <th>HCI Code</th>
                                        <th>Premises Type</th>
                                        <th>Type of Risk</th>
                                    </tr>
                                    </thead>
                                    <tbody>

                                    <c:forEach var = "item" items = "${auditTaskDataDtos}" varStatus="status">
                                        <tr>
                                            <c:set var="id" value="${status.index}"></c:set>
                                            <td> <input name="<c:out value="${id}"/>selectForAd" id="<c:out value="${id}"/>selectForAd" type="checkbox" value="ad" <c:if test="${item.selectedForAudit}">checked</c:if>/></td>
                                            <td><c:out value="${item.svcName}"/></td>
                                            <td><c:out value="${item.postalCode}"/></td>
                                            <td><c:out value="${item.lastInspStart}"/></td>
                                            <td><c:out value="${item.lastInspEnd}"/></td>
                                            <td><c:out value="${item.resultComplicance}"/></td>
                                            <td><c:out value="${item.svcCode}"/></td>
                                            <td><c:out value="${item.hclCode}"/></td>
                                            <td><c:out value="${item.premisesType}"/></td>
                                        </tr>
                                    </c:forEach>
                                    </tbody>
                                </table>
                                <div class="table-footnote">
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="application-tab-footer">
                        <div class="row">
                            <div class="col-xs-12 col-sm-6">
                                <div class="text-right text-center-mobile"><a class="btn btn-primary next" href="javascript:void(0);" onclick="javascript: addToAudit();">Add to Audit List</a></div>
                            </div>
                            <div class="col-xs-12 col-sm-6">
                                <div class="text-right text-center-mobile"><a class="btn btn-primary next" href="javascript:void(0);" onclick="javascript: cancel();">Cancel</a></div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</form>
<script type="text/javascript">
    function addToAudit() {
        SOP.Crud.cfxSubmit("mainForm","donext");
    }
    function cancel() {
        SOP.Crud.cfxSubmit("mainForm","doback");
    }

</script>
