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
                                        <th>HCI Code</th>
                                        <th>HCI Name</th>
                                        <th>Address</th>
                                        <th>Service Name</th>
                                        <th>Audit Type</th>
                                        <th>Inspector</th>
                                        <th>Select for Audit</th>
                                    </tr>
                                    </thead>
                                    <tbody>

                                    <c:forEach var = "item" items = "${auditTaskDataDtos}" varStatus="status">
                                        <tr>
                                            <c:set var="id" value="${status.index}"></c:set>
                                            <td><c:out value="${item.hclCode}"/></td>
                                            <td><c:out value="${item.hclName}"/></td>
                                            <td><c:out value="${item.address}"/></td>
                                            <td><c:out value="${item.svcName}"/></td>
                                            <td><iais:select name="${id}auditType" options="aduitTypeOp" firstOption="Please select" value=""></iais:select>
                                                <c:set value = "error_${id}adtype" var = "erradtype"/>
                                                <span class="error-msg" id="<c:out value="${erradtype}"/>" name="iaisErrorMsg"></span>
                                            </td>
                                            <td>
                                                <select name="<c:out value="${id}insOp"/>">
                                                    <c:forEach var="inspOp" items="${item.inspectors}">
                                                        <option  value="<c:out value="${inspOp.value}"/>"><c:out value="${inspOp.text}"/></option>
                                                    </c:forEach>
                                                </select>
                                                <c:set value = "error_${id}insp" var = "errboth"/>
                                                <span class="error-msg" id="<c:out value="${errboth}"/>" name="iaisErrorMsg"></span>
                                            </td>
                                            <td>
                                                <input name="<c:out value="${id}"/>selectForAd" id="<c:out value="${id}"/>selectForAd" type="checkbox" value="ad" <c:if test="${item.selectedForAudit}">checked</c:if>/>
                                            </td>
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
                                <div class="text-right text-center-mobile"><a class="btn btn-primary next" href="javascript:void(0);" onclick="javascript: confirm();">Confirm to Audit</a></div>
                            </div>
                            <div class="col-xs-12 col-sm-6">
                                <div class="text-right text-center-mobile"><a class="btn btn-primary next" href="javascript:void(0);" onclick="javascript: cancel();">Cancel</a></div>
                            </div>
                            <div class="col-xs-12 col-sm-6">
                                <div class="text-right text-center-mobile"><a class="btn btn-primary next" href="javascript:void(0);" onclick="javascript: cancelAudit();">Cancel Audit Task</a></div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</form>
<script type="text/javascript">
    function confirm() {
        SOP.Crud.cfxSubmit("mainForm","confirm");
    }
    function cancel() {
        SOP.Crud.cfxSubmit("mainForm","doback");
    }

    function cancelAudit() {
        SOP.Crud.cfxSubmit("mainForm","cancel");
    }

</script>
