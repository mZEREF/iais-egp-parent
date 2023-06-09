<%--
  Created by IntelliJ IDEA.
  User: wangyu
  Date: 2020/04/01
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
<div class="main-content">
<form id = "mainForm" method = "post" action=<%=process.runtime.continueURL()%>>
    <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
    <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
    <input type="hidden" name="actionTodo" id="actionTodo" />


            <div class="row">
                <div class="col-xs-12">
                    <div class="instruction-content center-content">
                        <h2>${modulename}</h2>
                        <table aria-describedby="" class="table">
                            <thead>
                            <tr>
                                <th scope="col" >HCI Code</th>
                                <th scope="col" >HCI Name</th>
                                <th scope="col" >Address</th>
                                <th scope="col" >Service Name</th>
                                <th scope="col" >Audit Type</th>
                                <th scope="col" >Assigned Inspector</th>
                                <th scope="col" >Reasons for Cancellation</th>
                                <th scope="col" >Select for Cancellation</th>
                                <!--% <th scope="col" >Reason</th> -->
                            </tr>
                            </thead>
                            <tbody>
                            <span class="error-msg" id="error_selectedOne" name="iaisErrorMsg"></span>
                            <c:if test="${empty auditTaskDataDtos}">
                                <tr>
                                    <td colspan="8" align="left">
                                        <iais:message key="GENERAL_ACK018" escape="true"/>
                                    </td>
                                </tr>
                            </c:if>
                            <c:forEach var = "item" items = "${auditTaskDataDtos}" varStatus="status">
                                <tr>
                                    <c:set var="id" value="${status.index}"></c:set>
                                    <td><c:out value="${item.hclCode}"/></td>
                                    <td><c:out value="${empty item.hclName ? 'N/A' :item.hclName}"/></td>
                                    <td><c:out value="${empty item.address ? 'N/A' : item.address}"/></td>
                                    <td><c:out value="${item.svcName}"/></td>
                                    <td>
                                        <iais:code code= "${item.auditType}"/>
                                        <input name="${id}auditType" id="${id}auditType" type="hidden" value="${item.auditType}">
                                    </td>
                                    <td>
                                        <label>${item.inspector}</label>
                                    </td>
                                    <td>
                                        <textarea cols="30" disabled rows="5" name="${id}reason"  maxlength="2000"><c:out value="${item.cancelReason}"></c:out></textarea>
                                    </td>
                                    <td>
                                        <input name="<c:out value="${id}"/>selectForAd" id="<c:out value="${id}"/>selectForAd" type="checkbox" value="ad" <c:if test="${item.selectedForAudit}">checked</c:if>/>
                                        <br/>
                                        <c:set value="error_${id}select" var="errselect"/>
                                        <span class="error-msg" id="<c:out value="${errselect}"/>"
                                              name="iaisErrorMsg"></span>
                                    </td>
                                        <!--    <td>
                                        <textarea cols="30" rows="5" name="${id}newReason"  maxlength="2000"><c:out value="${item.reasonForAO}"></c:out></textarea>
                                        </td> -->
                                </tr>
                            </c:forEach>
                            </tbody>
                        </table>
                            <div class="table-gp">
                                <div class="table-footnote">
                                </div>
                            </div>
                        <iais:action style="text-align:right;">
                            <button type="button" class="btn btn-primary next" onclick="javascript:cancelAudit('0');">
                                Reject
                            </button>
                            <button type="button" class="btn btn-primary next" onclick="javascript:cancelAudit('1');">
                                Confirm
                            </button>
                        </iais:action>
                    </div>
                </div>
            </div>


</form>
</div>
<%@ include file="/WEB-INF/jsp/include/validation.jsp" %>
<script type="text/javascript">
    function cancelAudit(action) {
         showWaiting();
         $("#actionTodo").val(action);
        SOP.Crud.cfxSubmit("mainForm","cancelTask");
    }

</script>
