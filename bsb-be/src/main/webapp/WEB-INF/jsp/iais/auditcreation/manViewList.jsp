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

                <div class="col-xs-12">
                    <div class="instruction-content center-content">
                        <h2> Manual Audit List Creation Search Results</h2>
<%--                        <iais:pagination param="auditSearchParam" result="auditSearchResult"/>--%>
                        <div class="table-gp">
                            <table class="table">
                                <thead>
                                <tr>
                                    <th></th>
                                    <th></th>
                                    <th>Facility Name</th>
                                    <th>Facility Classification</th>
                                    <th>Facility Type </th>
                                    <th>Date of Last Audit </th>
                                    <th>Audit Type</th>
                                    <th>Scenario Category</th>
                                    <th>Audit Outcome</th>
                               </tr>
                                </thead>
                                <tbody>
                                <c:forEach var="item" items="${auditSearchResult}">
                                    <tr style="display: table-row;">
<%--                                        <td> <input name="<c:out value="${id}"/>selectForAuditList" id="<c:out value="${id}"/>selectForAuditList" type="checkbox" value="${item.id}" <c:if test="${item.id}">checked</c:if>/></td>--%>
                                        <td><p><input type="checkbox" id="auditCreateId" name = "auditCreateId" value="<iais:mask name='auditInfoId' value='${item.id}'/>"></p></td>
                                        <td></td>
                                        <td><p><c:out  value="${item.facilityName}"/></p></td>
                                        <td><p><c:out  value="${item.facilityClassification}"/></p></td>
                                        <td><p><c:out value="${item.facilityType}"/></p></td>
                                        <td></td>
                                        <td><p><c:out value="${item.auditType}"/></p></td>
                                        <td></td>
                                    </tr>
                                </c:forEach>
                                <input hidden id="rows" value="1"/>
                                </tbody>
                                <span id="error_selectedOne" name="iaisErrorMsg" class="error-msg"></span>
                            </table>
                            <div class="table-footnote">
                            </div>
                        </div>
                        <iais:action style="text-align:right;">
                            <button class="btn btn-secondary" id="clearbtn" type="button"
                                    onclick="javascript:cancel();">
                                Back
                            </button>
                            <button class="btn btn-primary next" type="button" onclick="javascript:addToAudit();">
                                Create List
                            </button>
                        </iais:action>
                    </div>
                </div>
</form>
</div>
<%@ include file="/WEB-INF/jsp/include/validation.jsp" %>
<script type="text/javascript">

    function cancel() {
        SOP.Crud.cfxSubmit("mainForm","doback");
    }
    function jumpToPagechangePage() {
        SOP.Crud.cfxSubmit("mainForm", "changePage");
    }
    function addToAudit(id) {
        $("#auditCreateId").val(id);
        SOP.Crud.cfxSubmit("mainForm","donext",id);
    }

    // function addToAudit(id) {
    //     var checkOne = false;
    //     var chboxVal = [];
    //     var checkBox = document.querySelectorAll('input[name = "auditCreateId"]');
    //     var checkArr = Array.from(checkBox);
    //
    //     checkArr.forEach(item => {
    //         if (item.checked) {
    //             checkOne = true;
    //             chboxVal.push(item.value)
    //         }
    //     });
    //     if (checkOne) {
    //         $("#auditCreateId").val(id);
    //         SOP.Crud.cfxSubmit("mainForm","donext",id);
    //     } else {
    //         $("#error_selectedOne").text("Please select a service for auditing");
    //     }
    // }

</script>
