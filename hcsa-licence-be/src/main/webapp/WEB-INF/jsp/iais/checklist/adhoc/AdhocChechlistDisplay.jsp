<%--
  Created by IntelliJ IDEA.
  User: yichen
  Date: 12/10/2019
  Time: 8:17 PM
  To change this template use File | Settings | File Templates.
--%>

<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<webui:setLayout name="iais-intranet"/>
<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>

<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>

<c:choose>
    <c:when test="${empty inspectionChecklistAttr}">
        <tr>
            <td colspan="6">
                <iais:message key="ACK018" escape="true"></iais:message>
            </td>
        </tr>
    </c:when>
    <c:otherwise>
        <div class="main-content">
            <form id="mainForm" method="post" action=<%=process.runtime.continueURL()%>>
                <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>

                <br><br>
                <span id="error_checklistItem" name="iaisErrorMsg" class="error-msg"></span>
                <div class="tab-pane" id="tabInspection" role="tabpanel">
                    <%@ include file="../common/checklistTab.jsp" %>
                </div>
                <div class="row">
                <div class="col-xs-12 col-sm-12">
                    <div class="text-right text-center-mobile">
                        <a id="nextAdhocItemBtn" class="btn btn-secondary" href="#">Save</a>
                        <a id="addAdhocItemBtn" class="btn btn-primary" href="#">Add Adhoc Item</a>
                    </div>
                </div>
            </div>
            </form>
        </div>
    </c:otherwise>
</c:choose>
<%@include file="/WEB-INF/jsp/include/validation.jsp" %>
<script>
    "use strict"
    addAdhocItemBtn.onclick = function () {
        SOP.Crud.cfxSubmit("mainForm", "receiveItemPool");
    }

    nextAdhocItemBtn.onclick = function () {
        SOP.Crud.cfxSubmit("mainForm", "saveAdhocItem");
    }

    function removeAdhocItem(id) {
        SOP.Crud.cfxSubmit("mainForm", "removeAdhocItem", id);
    }


</script>