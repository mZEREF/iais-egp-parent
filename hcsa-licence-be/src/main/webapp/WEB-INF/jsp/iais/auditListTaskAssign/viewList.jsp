<%--
  Created by IntelliJ IDEA.
  User: JiaHao_Chen
  Date: 2019/11/13
  Time: 16:29
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<webui:setLayout name="iais-intranet"/>
<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<div class="main-content">
<form id="mainForm" method="post" action=<%=process.runtime.continueURL()%>>
    <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
    <input type="hidden" name="paramController" id="paramController"
           value="com.ecquaria.cloud.moh.iais.action.AuditSystemListDelegator"/>
    <input type="hidden" name="valEntity" id="valEntity"
           value="com.ecquaria.cloud.moh.iais.dto.AuditAssginListValidateDto"/>
    <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
    <input type="hidden" name="actionTodo" id="actionTodo" />
            <div class="row">
                <div class="col-xs-12">
                    <div class="instruction-content center-content">
                        <div hidden="hidden">
                        <c:if test="${ISTUC}">
                            <iais:section title="" id="searchRole">
                                <iais:row>
                                    <iais:field value="Role"/>
                                    <iais:value width="8">
                                        <iais:select name="roleIdsForAuditSelect" options="roleIdsForAudit"  value="${roleIdsForAuditSelect}"/>
                                    </iais:value>
                                </iais:row>
                            </iais:section>
                            <iais:action style="text-align:right;">
                                <button class="btn btn-primary next" type="button" onclick="javascript:doNextSearch();">Search
                                </button>
                            </iais:action>
                        </c:if>
                        </div>
                        <h2>${modulename}</h2>
                        <c:if test="${modulename != 'Manual Audit List'}">
                            <iais:pagination param="auditTaskDataDtos_pram" result="auditTaskDataDtosResult"/>
                        </c:if>
                        <div class="table-gp">
                            <table aria-describedby="" class="table">
                                <thead>
                   <c:if test="${empty auditTaskDataDtos || (not empty auditTaskDataDtos && roleIdsForAuditSelect != 'APO')}">
                       <tr>
                           <th scope="col">HCI Code</th>
                           <th scope="col">HCI Name</th>
                           <th scope="col">Address</th>
                           <th scope="col">Service Name</th>
                           <th scope="col" >Audit Type</th>
                           <c:if test="${ISTUC}">
                               <th scope="col" >TCU Audit Due Date </th>
                           </c:if>
                           <th scope="col">Assign task to Inspector</th>
                           <th scope="col">Select for Audit</th>
                       </tr>
                   </c:if>
                    <c:if test="${not empty auditTaskDataDtos && roleIdsForAuditSelect eq 'APO'}">
                                <tr>
                                    <th scope="col" width="10%">HCI Code</th>
                                    <th scope="col" width="10%">HCI Name</th>
                                    <th scope="col" width="26%">Address</th>
                                    <th scope="col" width="10%">Service Name</th>
                                    <th scope="col" width="19%">Audit Type</th>
                                    <c:if test="${ISTUC}">
                                        <th scope="col" >TCU Audit Due Date  </th>
                                    </c:if>
                                    <th scope="col" width="19%">Assign task to Inspector</th>
                                    <th scope="col" width="6%">Select for Audit</th>
                                </tr>
                    </c:if>
                                </thead>
                                <tbody>
                                <span class="error-msg" id="error_selectedOne" name="iaisErrorMsg"></span>
                                <c:if test="${empty auditTaskDataDtos}">
                                    <tr>
                                        <td colspan="${ISTUC ? 8 : 9}" align="left">
                                            <iais:message key="GENERAL_ACK018" escape="true"/>
                                        </td>
                                    </tr>
                                </c:if>
                                <c:forEach var="item" items="${auditTaskDataDtos}" varStatus="status">
                                    <tr>
                                        <c:set var="id" value="${status.index}"></c:set>
                                        <td  width="10%"><c:out value="${item.hclCode}"/></td>
                                        <td  width="10%"><c:out value="${empty item.hclName ? 'N/A' :item.hclName}"/></td>
                                        <td  width="28%"><c:out value="${empty item.address ? 'N/A' : item.address}"/></td>
                                        <td width="10%"><c:out value="${item.svcName}"/></td>
                                        <td  width="10%"><c:if test="${ !item.audited}">
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
                                        <c:if test="${ISTUC}">
                                            <td><fmt:formatDate value="${item.tcuDate}" pattern="dd/MM/yyyy"></fmt:formatDate></td>
                                        </c:if>
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
                                            <input name="<c:out value="${id}"/>selectForAd"
                                                   id="<c:out value="${id}"/>selectForAd"
                                                   <c:if test="${item.selectedForAudit}">checked</c:if>
                                                   type="checkbox" value="ad">
                                            <br/>
                                            <c:set value="error_${id}select" var="errselect"/>
                                            <span class="error-msg" id="<c:out value="${errselect}"/>"
                                                  name="iaisErrorMsg"></span>
                                        </td>
                                    </tr>
                                </c:forEach>

                                </tbody>
                            </table>
                            <div class="table-footnote">
                            </div>
                        </div>
                        <iais:action style="text-align:right;">
                            <c:if test="${!ISTUC}">
                                <button type="button" class="btn btn-secondary" onclick="javascript:cancel();">Cancel
                                </button>
                            </c:if>
                            <c:if test="${roleIdsForAuditSelect eq 'APO'}">
                            <button type="button" class="btn btn-primary next" onclick="javascript:confirm();">Confirm
                                to Audit
                            </button>
                            </c:if>
                            <button type="button" class="btn btn-primary next" onclick="javascript:cancelAudit();">Cancel
                                Audit Task
                            </button>
                        </iais:action>
                    </div>
                </div>
            </div>

</form>
</div>
<%@ include file="/WEB-INF/jsp/include/validation.jsp" %>
<script type="text/javascript">
    function confirm() {
        showWaiting();
        $('#actionTodo').val("confirm");
        SOP.Crud.cfxSubmit("mainForm", "confirm");
    }

    function cancel() {
        showWaiting();
        SOP.Crud.cfxSubmit("mainForm", "doback");
    }

    function remove() {
        showWaiting();
        SOP.Crud.cfxSubmit("mainForm", "remove");
    }

    function cancelAudit() {
        showWaiting();
        $('#actionTodo').val("cancel");
        SOP.Crud.cfxSubmit("mainForm", "cancel");
    }

    function createHcl() {
        SOP.Crud.cfxSubmit("mainForm", "next");
    }

    function jumpToPagechangePage() {
        SOP.Crud.cfxSubmit("mainForm", "changePage");
    }


    function  doNextSearch(){
        SOP.Crud.cfxSubmit("mainForm", "serach");
    }
</script>
