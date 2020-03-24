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
                <a class="btn btn-secondary"   href="${pageContext.request.contextPath}/checklist-item-file?action=regulation">Export Regulation</a>
                <a class="btn btn-primary next" id="crud_search_button" value="doSearch" href="#">Search</a>
            </div>
        </div>

        <div class="tab-content">
            <div class="row">
                <div class="col-xs-12">
                    <div class="components">
                        <iais:pagination  param="checklistItemSearch" result="checklistItemResult"/>
                        <div class="table-gp">
                            <table class="table">
                                <thead>
                                <tr>
                                    <iais:sortableHeader needSort="false" field="" value="No."></iais:sortableHeader>
                                    <td></td>
                                    <iais:sortableHeader needSort="true" field="CLAUSE_NO"
                                                         value="Regulation Clause Number"></iais:sortableHeader>
                                    <iais:sortableHeader needSort="true" field="CLAUSE"
                                                         value="Regulations"></iais:sortableHeader>
                                    <iais:sortableHeader needSort="true" field="CHECKLISTITEM"
                                                         value="Checklist Item"></iais:sortableHeader>
                                    <iais:sortableHeader needSort="true" field="RISK_LEVEL" value="Risk Level"></iais:sortableHeader>
                                    <iais:sortableHeader needSort="false" field="status" value="Status"></iais:sortableHeader>
                                </tr>
                                </thead>
                                <tbody>
                                <c:choose>
                                    <c:when test="${empty checklistItemResult.rows}">
                                        <tr>
                                            <td colspan="6">
                                                <iais:message key="ACK018" escape="true"></iais:message>
                                            </td>
                                        </tr>
                                    </c:when>
                                    <c:otherwise>
                                        <c:forEach var="item" items="${checklistItemResult.rows}" varStatus="status">
                                            <tr>
                                                <td class="row_no">${(status.index + 1) + (checklistItemSearch.pageNo - 1) * checklistItemSearch.pageSize}</td>
                                                <td><input name="itemCheckbox" id="itemCheckbox" type="checkbox" value="${item.itemId}"/>
                                                </td>
                                                <td>${item.regulationClauseNo}</td>
                                                <td>${item.regulationClause}</td>
                                                <td>${item.checklistItem}</td>
                                                <td><iais:code code="${item.riskLevel}"></iais:code></td>
                                                <td><iais:code code="${item.status}"></iais:code></td>
                                                <c:if test="${empty sessionScope.currentValidateId}">
                                                    <td>
                                                        <c:if test="${item.status == 'CMSTAT001'}">
                                                            <button type="button" class="btn btn-default btn-sm"
                                                                    onclick="javascript:prepareEditItem('${item.itemId}');">Edit
                                                            </button>
                                                            <button type="button"  class="btn btn-default btn-sm" data-toggle="modal" data-target="#DeleteTemplateModal" >Delete</button>

                                                            <div class="modal fade" id="DeleteTemplateModal" tabindex="-1" role="dialog" aria-labelledby="DeleteTemplateModal" style="left: 50%;top: 50%;transform: translate(-50%,-50%);min-width:80%; overflow: visible;bottom: inherit;right: inherit;">
                                                                <div class="modal-dialog" role="document">
                                                                    <div class="modal-content">
                                                                        <div class="modal-header">
                                                                            <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                                                                            <h5 class="modal-title" id="gridSystemModalLabel">Confirmation Box</h5>
                                                                        </div>
                                                                        <div class="modal-body">
                                                                            <div class="row">
                                                                                <div class="col-md-8 col-md-offset-2"><span style="font-size: 2rem">Do you confirm the delete ?</span></div>
                                                                            </div>
                                                                        </div>
                                                                        <div class="modal-footer">
                                                                            <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
                                                                            <button type="button" class="btn btn-primary" onclick="javascript:disable('${item.itemId}');" >Confirm</button>
                                                                        </div>
                                                                    </div>
                                                                </div>
                                                            </div>

                                                        </c:if>
                                                        <c:if test="${item.status == 'CMSTAT003'}">
                                                            <button type="button" class="btn btn-default btn-sm"
                                                                    onclick="javascript:prepareEditItem('${item.itemId}');">Edit
                                                            </button>
                                                        </c:if>
                                                    </td>
                                                </c:if>
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