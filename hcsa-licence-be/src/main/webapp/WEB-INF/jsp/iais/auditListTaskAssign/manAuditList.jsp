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
    <input type="hidden" name="actionTodo" id="actionTodo" />

    <div class="main-content">
        <div class="container">
            <div class="row">
                <div class="col-xs-12">
                    <div class="instruction-content center-content">
                        <h2>${modulename}</h2>
                            <div class="table-gp">
                                <table class="table">
                                    <thead>
                                    <tr>
                                        <th width="10%">HCI Code</th>
                                        <th width="10%">HCI Name</th>
                                        <th  width="30%">Address</th>
                                        <th  width="10%">Service Name</th>
                                        <th  width="15%">Audit Type</th>
                                        <th width="19%">Assign task to Inspector</th>
                                        <th width="6%">Select for Audit</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <span class="error-msg" id="error_selectedOne" name="iaisErrorMsg"></span>
                                  <c:if test="${isSelectedAudit}">
                                      <tr>
                                   <td colspan="7" align="center">
                                   <iais:message key="ACK018" escape="true"/>
                                   </td>
                                      </tr>
                                  </c:if>
                                    <c:forEach var = "item" items = "${auditTaskDataDtos}" varStatus="status">
                                       <c:if test="${item.addAuditList}">
                                        <tr>
                                            <c:set var="id" value="${status.index}"></c:set>
                                            <td width="10%"><c:out value="${item.hclCode}"/></td>
                                            <td  width="10%"><c:out value="${item.hclName}"/></td>
                                            <td  width="30%"><c:out value="${item.address}"/></td>
                                            <td width="10%"><c:out value="${item.svcName}"/></td>
                                            <td width="15%"><c:if test="${ !item.audited}">
                                                <iais:select name="${id}auditType" options="aduitTypeOp"
                                                             firstOption="Please Select" value="${item.auditType}"></iais:select>
                                                <c:set value="error_${id}adtype" var="erradtype"/>
                                                <span class="error-msg" id="<c:out value="${erradtype}"/>"
                                                      name="iaisErrorMsg"></span>
                                            </c:if>
                                                <c:if test="${item.audited}">
                                                    <iais:code code= "${item.auditType}"/>
                                                    <input name="${id}auditType" id="${id}auditType" type="hidden" value="${item.auditType}">
                                                </c:if>
                                            </td>
                                            <td width="19%">
                                                <c:if test="${!item.audited}">
                                                    <iais:select  name="${id}insOp" options="inspectors${item.workGroupId}" value="${item.inspectorId}"   firstOption="Please Select"></iais:select>
                                                    <c:set value="error_${id}insp" var="errboth"/>
                                                    <span class="error-msg" id="<c:out value="${errboth}"/>"
                                                          name="iaisErrorMsg"></span>
                                                </c:if>
                                                <c:if test="${item.audited}">
                                                    <label>${item.inspector}</label>
                                                    <input name="${id}insOp" id="${id}insOp" type="hidden" value="<iais:mask name="${id}insOp" value="${item.inspectorId}"/>">
                                                </c:if>
                                            </td>
                                            <td width="6%">
                                                <input name="<c:out value="${id}"/>selectForAd" id="<c:out value="${id}"/>selectForAd" type="checkbox" value="ad" <c:if test="${item.selectedForAudit}">checked</c:if>/>
                                                <br/>
                                                <c:set value="error_${id}select" var="errselect"/>
                                                <span class="error-msg" id="<c:out value="${errselect}"/>"
                                                      name="iaisErrorMsg"></span>
                                            </td>
                                        </tr>
                                       </c:if>
                                    </c:forEach>

                                    </tbody>
                                </table>
                                <div class="table-footnote">
                                </div>
                            </div>
                        <iais:action style="text-align:right;">
                            <button type="button" class="btn btn-secondary" onclick="javascript:cancel();">Cancel
                            </button>
                            <button type="button" class="btn btn-primary next" onclick="javascript:confirm();">Confirm
                                to Audit
                            </button>
                            <button type="button" class="btn btn-primary next" onclick="javascript:cancelAudit();">Cancel
                                Audit Task
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
    function confirm() {
        showWaiting();
        $('#actionTodo').val("confirm");
        SOP.Crud.cfxSubmit("mainForm","confirm");
    }
    function cancel() {
        SOP.Crud.cfxSubmit("mainForm","doback");
    }

    function cancelAudit() {
        showWaiting();
        $('#actionTodo').val("cancel");
        SOP.Crud.cfxSubmit("mainForm","precancel");
    }

</script>
