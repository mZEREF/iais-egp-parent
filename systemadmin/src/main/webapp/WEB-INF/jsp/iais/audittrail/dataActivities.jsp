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

                        <c:choose>
                            <c:when test="${!empty auditLogDetailView.searchParam}">
                                <div class="table-responsive col-xs-12">
                                    <div class="table-gp">
                                        <table aria-describedby="" class="table">
                                            <thead>
                                            <tr><th scope="col" >Field</th><th scope="col" >Value</th></tr>
                                            </thead>
                                            <tbody>
                                            <c:forEach var="msg" items="${auditLogDetailView.searchParam}">
                                                <tr><td><c:out value="${msg.colName}"/></td><td class="line-limit-length"><c:out value="${msg.longText}"/>
                                                </td></tr>
                                            </c:forEach>
                                            </tbody>
                                        </table>
                                    </div>
                                </div>
                            </c:when>
                            <c:otherwise>
                                <tr><td>No record found.</td></tr>
                            </c:otherwise>
                        </c:choose>
                    </div>
        <br>
                    <div id="control--printerFriendly--34" class="section control " style="overflow: visible;">
                        <div class="control-set-font control-font-header section-header">
                            <h2>Before Data</h2>
                        </div>

                        <c:choose>
                            <c:when test="${!empty auditLogDetailView.beforeChange}">
                                <div class="table-responsive col-xs-12">
                                    <div class="table-gp">
                                        <table aria-describedby="" class="table">
                                            <thead>
                                            <tr><th scope="col" >Field</th><th scope="col" >Value</th></tr>
                                            </thead>
                                            <tbody>
                                            <c:forEach var="msg" items="${auditLogDetailView.beforeChange}">
                                                <tr><td><c:out value="${msg.colName}"/></td><td class="line-limit-length"><c:out value="${msg.longText}"/>
                                                </td></tr>
                                            </c:forEach>
                                            </tbody>
                                        </table>
                                    </div>
                                </div>
                            </c:when>
                            <c:otherwise>
                                <tr><td>No record found.</td></tr>
                            </c:otherwise>
                        </c:choose>

                    </div>
            <br>
                    <div id="control--printerFriendly--35" class="section control " style="overflow: visible;">
                        <div class="control-set-font control-font-header section-header">
                            <h2>After Data</h2>
                        </div>
                        <c:choose>
                            <c:when test="${!empty auditLogDetailView.afterChange}">
                                <div class="table-responsive col-xs-12">
                                    <div class="table-gp">
                                        <table aria-describedby="" class="table">
                                            <thead>
                                            <tr><th scope="col" >Field</th><th scope="col" >Value</th></tr>
                                            </thead>
                                            <tbody>
                                            <c:forEach var="msg" items="${auditLogDetailView.afterChange}">
                                                <tr><td><c:out value="${msg.colName}"/></td><td class="line-limit-length"><c:out value="${msg.longText}"/>
                                                </td></tr>
                                            </c:forEach>
                                            </tbody>
                                        </table>
                                    </div>
                                </div>
                            </c:when>
                            <c:otherwise>
                                <tr><td>No record found.</td></tr>
                            </c:otherwise>
                        </c:choose>
                    </div>

            <br>
            <div id="control--printerFriendly--36" class="section control " style="overflow: visible;">
                <div class="control-set-font control-font-header section-header">
                    <h2>Request Data</h2>
                </div>

                <c:choose>
                    <c:when test="${!empty auditLogDetailView.request}">
                        <div class="table-responsive col-xs-12">
                            <div class="table-gp">
                                <table aria-describedby="" class="table">
                                    <thead>
                                    <tr><th scope="col" <c:if test="${auditLogDetailView.request.size()==1 and auditLogDetailView.request[0].colName==auditLogDetailView.request[0].longText}">style="display: none" </c:if> >Field</th><th scope="col" >Value</th></tr>
                                    </thead>
                                    <tbody>
                                    <c:forEach var="msg" items="${auditLogDetailView.request}">
                                        <tr>
                                            <td <c:if test="${msg.colName==msg.longText}"> style="display: none" </c:if>><c:out value="${msg.colName}"/></td>
                                            <td class="line-limit-length"><c:out value="${msg.longText}"/></td>
                                        </tr>
                                    </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <tr><td>No record found.</td></tr>
                    </c:otherwise>
                </c:choose>

            </div>
            <br>
            <div id="control--printerFriendly--37" class="section control " style="overflow: visible;">
                <div class="control-set-font control-font-header section-header">
                    <h2>Response Data</h2>
                </div>
                <c:choose>
                    <c:when test="${!empty auditLogDetailView.response}">
                        <div class="table-responsive col-xs-12">
                            <div class="table-gp">
                                <table aria-describedby="" class="table">
                                    <thead>
                                    <tr><th scope="col"  <c:if test="${auditLogDetailView.response.size()==1 and auditLogDetailView.response[0].colName==auditLogDetailView.response[0].longText}">style="display: none"</c:if>>Field</th><th scope="col" >Value</th></tr>
                                    </thead>
                                    <tbody>
                                    <c:forEach var="msg" items="${auditLogDetailView.response}">
                                        <tr>
                                            <td <c:if test="${msg.colName==msg.longText}"> style="display: none" </c:if>><c:out value="${msg.colName}"/></td>
                                            <td class="line-limit-length"><c:out value="${msg.longText}"/></td>
                                        </tr>
                                    </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <tr><td>No record found.</td></tr>
                    </c:otherwise>
                </c:choose>
            </div>

            <br>
                    <div  class="section control " style="overflow: visible;">
                        <div class="control-set-font control-font-header section-header">
                            <h2>Validation Fail Detail</h2>
                        </div>
                        <c:choose>
                            <c:when test="${!empty auditLogDetailView.errorMsg}">
                                <div class="table-responsive col-xs-12">
                                    <div class="table-gp">
                                        <table aria-describedby="" class="table">
                                            <thead>
                                            <tr><th scope="col" >Field</th><th scope="col" >Value</th></tr>
                                            </thead>
                                            <tbody>
                                            <c:forEach var="msg" items="${auditLogDetailView.errorMsg}">
                                                <tr><td><c:out value="${msg.colName}"/></td><td class="line-limit-length"><c:out value="${msg.longText}"/>
                                                </td></tr>
                                            </c:forEach>
                                            </tbody>
                                        </table>
                                    </div>
                                </div>
                            </c:when>
                            <c:otherwise>
                                <tr><td>No record found.</td></tr>
                            </c:otherwise>
                        </c:choose>
                    </div></div></div>
        </div>
        <br>
        <a href="#" class="back" id="Back" onclick="doBack()"><em class="fa fa-angle-left"></em> Back</a>
    </>
</div>

<%@include file="/WEB-INF/jsp/include/utils.jsp"%>

<script type="text/javascript">

    function doBack() {
        $("input[name='switch_action_type']").val("doBack")
        $("#mainForm").submit()
    }
</script>