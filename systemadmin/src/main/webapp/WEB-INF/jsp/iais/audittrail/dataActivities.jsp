<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2020/9/1
  Time: 10:40
  To change this template use File | Settings | File Templates.
--%>

<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://www.ecq.com/iais"   prefix="iais"%>
<%@ page contentType="text/html; charset=UTF-8" %>
<webui:setLayout name="iais-intranet"/>

<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>

<div class="main-content dashboard">
    <form id = "mainForm" method = "post" action=<%=process.runtime.continueURL()%>>
        <input type="hidden" name="collapseFlag" value="${param.collapseFlag}">
        <input type="hidden" name="operationType" value="${param.operationType}">
        <br><br><br>
        <div class="col-lg-12 col-xs-12"><div class="center-content"><div class="row">
            <div id="control--printerFriendly--33" class="section control " style="overflow: visible;">
                <div class="control-set-font control-font-header section-header">
                    <h2>Search Param</h2>
                </div>
                <div id="control--printerFriendly--33**errorMsg_section_top" class="error_placements"></div>
                <c:if test="${!empty auditLogDetailView.searchParam}">
                    <div class="table-responsive">
                        <tbody width = "50%" border = "1">
                        <thead>
                        <tr><th>Field</th><th>Value</th></tr>
                        </thead>
                        <tbody>
                        <c:forEach var="msg" items="${auditLogDetailView.searchParam}">
                            <tr><td><c:out value="${msg.colName}"/></td><td class="line-limit-length"><c:out value="${msg.longText}"/>
                            </td></tr>
                        </c:forEach>
                        </tbody>
                        </table>
                    </div>
                </c:if>
            </div>
            <br>
            <div id="control--printerFriendly--34" class="section control " style="overflow: visible;">
                <div class="control-set-font control-font-header section-header">
                    <h2>Before Data</h2>
                </div>
                <c:if test="${!empty auditLogDetailView.beforeChange}">
                    <div class="table-responsive col-xs-12"><div class="table-gp">
                        <table class="table">
                            <thead>
                            <tr><th>Field</th><th>Value</th></tr>
                            </thead>
                            <tbody>
                            <c:forEach var="msg" items="${auditLogDetailView.beforeChange}">
                                <tr><td><c:out value="${msg.colName}"/></td><td class="line-limit-length"><c:out value="${msg.longText}"/>
                                </td></tr>
                            </c:forEach>
                            </tbody>
                        </table>
                    </div></div>
                </c:if>
            </div>
            <br>
            <div id="control--printerFriendly--35" class="section control " style="overflow: visible;">
                <div class="control-set-font control-font-header section-header">
                    <h2>After Data</h2>
                </div>
                <c:if test="${!empty auditLogDetailView.afterChange}">
                    <div class="table-responsive col-xs-12"><div class="table-gp">
                        <table class="table">
                            <thead>
                            <tr><th>Field</th><th>Value</th></tr>
                            </thead>
                            <tbody>
                            <c:forEach var="msg" items="${auditLogDetailView.afterChange}">
                                <tr><td><c:out value="${msg.colName}"/></td><td class="line-limit-length"><c:out value="${msg.longText}"/>
                                </td></tr>
                            </c:forEach>
                            </tbody>
                        </table>
                    </div></div>
                </c:if>
            </div>

            <br>
            <div  class="section control " style="overflow: visible;">
                <div class="control-set-font control-font-header section-header">
                    <h2>Validation Fail Detail</h2>
                </div>
                <c:if test="${!empty auditLogDetailView.errorMsg}">
                    <div class="table-responsive col-xs-12"><div class="table-gp">
                        <table class="table">
                            <thead>
                            <tr><th>Field</th><th>Value</th></tr>
                            </thead>
                            <tbody>
                            <c:forEach var="msg" items="${auditLogDetailView.errorMsg}">
                                <tr><td><c:out value="${msg.colName}"/></td><td class="line-limit-length"><c:out value="${msg.longText}"/>
                                </td></tr>
                            </c:forEach>
                            </tbody>
                        </table>
                    </div></div>
                </c:if>
            </div></div></div>
        </div>
        <br>
        <a class="back" id="Back" onclick="doBack()"><em class="fa fa-angle-left"></em> Back</a>
    </>
</div>

<%@include file="/WEB-INF/jsp/include/utils.jsp"%>

<script type="text/javascript">

    function doBack() {
        $("input[name='switch_action_type']").val("doBack")
        $("#mainForm").submit()
    }
</script>