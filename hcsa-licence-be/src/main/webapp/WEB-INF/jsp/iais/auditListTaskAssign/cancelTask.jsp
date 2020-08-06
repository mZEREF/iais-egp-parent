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
    <input type="hidden" name="valEntity" id="valEntity" value="com.ecquaria.cloud.moh.iais.dto.AuditCancelTaskValidateDto"/>
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
                                    <th>HCI Code</th>
                                    <th>HCI Name</th>
                                    <th>Address</th>
                                    <th>Service Name</th>
                                    <th>Audit Type</th>
                                    <th>Assigned Inspector</th>
                                    <th>Status</th>
                                    <th>Select for Cancellation</th>
                                    <th>Resaons for Cancellation</th>
                                </tr>
                                </thead>
                                <tbody>

                                <c:forEach var = "item" items = "${auditTaskDataDtos}" varStatus="status">
                                    <c:if test="${item.selectedForAudit}">
                                        <tr>
                                            <c:set var="id" value="${status.index}"></c:set>

                                            <td><c:out value="${item.hclCode}"/></td>
                                            <td><c:out value="${item.hclName}"/></td>
                                            <td><c:out value="${item.address}"/></td>
                                            <td><c:out value="${item.svcName}"/></td>
                                            <td><iais:code code= "${item.auditType}"/></td>
                                            <td><c:out value="${item.inspector}"/></td>
                                            <td>
                                                <p>Audit Confirmed</p>
                                            </td>
                                            <td>
                                                <input name="selectForAd${(status.index)}" id="selectForAd${(status.index)}" type="checkbox" value="1" <c:if test="${item.selected}">checked</c:if>>
                                                <c:set value = "error_selectForAd${(status.index)}" var = "errname"/>
                                                <br/>
                                                <span class="error-msg"   id="${errname}" name="iaisErrorMsg"></span>
                                            </td>
                                            <td>
                                                <textarea cols="30" rows="5" name="${id}reason"  maxlength="2000"><c:out value="${item.cancelReason}"></c:out></textarea>
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
                            <button type="button" class="btn btn-secondary" onclick="javascript:cancel('${actionCancel}');">Cancel
                            </button>
                            <button type="button" class="btn btn-primary next" onclick="javascript:cancelAudit('${actionCancelAudit}');">
                                Confirm Cancellation
                            </button>
                        </iais:action>
                    </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</form>
<%@ include file="/WEB-INF/jsp/include/validation.jsp" %>
<%@ include file="/WEB-INF/jsp/include/yesNoConfirm.jsp" %>
<script type="text/javascript">
    function cancel(act) {
        showWaiting();
        SOP.Crud.cfxSubmit("mainForm",act);
    }

    function cancelAudit(act) {
        showPopupConfirmation({"msg":"Please click \"Yes\" to confirm cancellation.",
            'confirmCallback':'cancelAuditBack','data':{"act":act},"confirmBtn" : "Yes","cancelBtn":"No"
        });
    }

    function cancelAuditBack(data) {
        showWaiting();
        SOP.Crud.cfxSubmit("mainForm",data.act);
    }
</script>
