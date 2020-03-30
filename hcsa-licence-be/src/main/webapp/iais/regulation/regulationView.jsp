<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %><%--
  Created by IntelliJ IDEA.
  User: yichen
  Date: 10/6/2019
  Time: 3:10 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<webui:setLayout name="iais-intranet"/>

<%@ page contentType="text/html; charset=UTF-8" %>
<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>

<div class="main-content">
    <form id="mainForm" method="post" action=<%=process.runtime.continueURL()%>>
        <%@ include file="/include/formHidden.jsp" %>
        <div class="bg-title"><h2>Regulation Management</h2></div>


        <iais:section title="">

            <div class="form-horizontal">
                <div class="form-group">
                    <iais:field value="Regulation Clause Number" ></iais:field>
                    <div class="col-xs-5 col-md-3">
                        <input type="text" name="regulationClauseNo" maxlength="100" value="${regulationClauseNo}"/>
                        <span id="error_regulationClauseNo" name="iaisErrorMsg" class="error-msg"></span>
                    </div>
                </div>

                <div class="form-group">
                    <iais:field value="Regulation" ></iais:field>
                    <div class="col-xs-5 col-md-3">
                        <input type="text" name="regulationClause" maxlength="2000" value="${regulationClause}"/>
                        <span id="error_regulationClause" name="iaisErrorMsg" class="error-msg"></span>
                    </div>
                </div>


            </div>

        </iais:section>
        <div class="col-xs-12 col-md-12">
            <div class="text-right">
                <a class="btn btn-secondary" id="crud_clear_button"  href="#">Clear</a>
                <a class="btn btn-secondary"  id="exportButtonId" href="${pageContext.request.contextPath}/regulation-result-file?action=regulation" onclick="$('#exportButtonId').attr('class', 'btn btn-secondary disabled') ">Export Regulation</a>
                <a class="btn btn-primary next" id="crud_search_button" value="doSearch" href="#">Search</a>
            </div>
        </div>

        <div class="tab-content">
            <div class="row">
                <div class="col-xs-12">
                    <div class="components">
                        <iais:pagination  param="regulationSearch" result="regulationResult"/>
                        <div class="table-gp">
                            <table class="table">
                                <thead>
                                <tr>
                                    <iais:sortableHeader needSort="false" field="" value="No."></iais:sortableHeader>
                                    <iais:sortableHeader needSort="true" field="CLAUSE_NO"
                                                         value="Regulation Clause Number"></iais:sortableHeader>
                                    <iais:sortableHeader needSort="true" field="CLAUSE"
                                                         value="Regulations"></iais:sortableHeader>
                                    <iais:sortableHeader needSort="false" field="status" value="Status"></iais:sortableHeader>
                                    <iais:sortableHeader needSort="false" field="action" value="Action"></iais:sortableHeader>
                                </tr>
                                </thead>
                                <tbody>
                                <c:choose>
                                    <c:when test="${empty regulationResult.rows}">
                                        <tr>
                                            <td colspan="6">
                                                <iais:message key="ACK018" escape="true"></iais:message>
                                            </td>
                                        </tr>
                                    </c:when>
                                    <c:otherwise>
                                        <c:forEach var="item" items="${regulationResult.rows}" varStatus="status">
                                            <tr>
                                                <td class="row_no">${(status.index + 1) + (regulationSearch.pageNo - 1) * regulationSearch.pageSize}</td>
                                                </td>
                                                <td>${item.clauseNo}</td>
                                                <td>${item.clause}</td>
                                                <td><iais:code code="${item.status}"></iais:code></td>
                                                <td>
                                                    <button type="button"  class="btn btn-default btn-sm" data-toggle="modal" onclick="" >Edit</button>
                                                    <button type="button"  class="btn btn-default btn-sm" data-toggle="modal" onclick="" >Delete</button>
                                                </td>

                                            </tr>
                                        </c:forEach>
                                    </c:otherwise>

                                </c:choose>
                                </tbody>
                            </table>
                            <div class="table-footnote">
                                <div class="row">
                                    <div class="col-xs-6 col-md-4">
                                    </div>
                                    <div class="col-xs-50 col-md-12 text-right">
                                        <div class="nav">
                                            <br><br><br>
                                            <div class="text-right text-center-mobile">
                                                <a class="btn btn-primary next" href="javascript:void(0);"
                                                   onclick="Utils.submit('mainForm', 'preUploadData', 'regulation')">Upload Regulation</a>

                                                <a class="btn btn-primary next" href="javascript:void(0);"
                                                   onclick="Utils.submit('mainForm', 'preCreate')">Create</a>
                                            </div>
                                        </div>


                                    </div>
                                </div>


                            </div>


                        </div>
                    </div>
                </div>
            </div>
        </div>

    </form>
</div>

<%@include file="/include/validation.jsp" %>
<%@include file="/include/utils.jsp"%>
<script type="text/javascript">

</script>