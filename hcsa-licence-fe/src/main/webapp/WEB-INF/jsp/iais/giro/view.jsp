<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");


%>
<webui:setLayout name="iais-internet"/>
<%@include file="../common/dashboard.jsp"%>
<form  method="post" id="mainForm"   action=<%=process.runtime.continueURL()%>>
    <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>


    <div class="container">
        <div class="row">
            <div class="col-xs-12">
                <div class="components">
                    <iais:pagination param="searchGiroAccountParam" result="searchGiroDtoResult"/>
                    <div class="table-responsive">
                        <div class="table-gp">
                            <table aria-describedby="" class="table">
                                <thead>
                                <tr align="center">

                                    <iais:sortableHeader needSort="true" field="LICENCE_NO"
                                                         value="Licence No."/>
                                    <iais:sortableHeader needSort="true" field="SVC_NAME"
                                                         value="Service Type"/>
                                    <iais:sortableHeader needSort="true" field="LICENSEE_NAME"
                                                         value="Licensee"/>
                                    <iais:sortableHeader needSort="true"
                                                         field="ACCT_NAME"
                                                         value="Account Name"/>
                                    <iais:sortableHeader needSort="true"
                                                         field="BANK_NAME"
                                                         value="Bank Name"/>
                                    <iais:sortableHeader needSort="true"
                                                         field="BANK_CODE"
                                                         value="Bank Code"/>
                                    <iais:sortableHeader needSort="true"
                                                         field="BRANCH_CODE"
                                                         value="Branch Code"/>
                                    <iais:sortableHeader needSort="true"
                                                         field="ACCT_NO"
                                                         value="Bank Account No."/>

                                </tr>
                                </thead>
                                <tbody class="form-horizontal">
                                <c:choose>
                                    <c:when test="${empty searchGiroDtoResult.rows}">
                                        <tr>
                                            <td colspan="15">
                                                <iais:message key="GENERAL_ACK018"
                                                              escape="true"/>
                                            </td>
                                        </tr>
                                    </c:when>
                                    <c:otherwise>
                                        <c:forEach var="pool"
                                                   items="${searchGiroDtoResult.rows}"
                                                   varStatus="status">
                                            <tr>

                                                <td>
                                                    <c:out value="${pool.licenceNo}"/>
                                                </td>
                                                <td >
                                                    <c:out value="${pool.svcName}"/>
                                                </td>
                                                <td>
                                                    <c:out value="${pool.licenseeName}"/>
                                                </td>
                                                <td>
                                                    <c:out value="${pool.acctName}"/>
                                                </td>
                                                <td>
                                                    <c:out value="${pool.bankName}"/>
                                                </td>
                                                <td>
                                                    <c:out value="${pool.bankCode}"/>
                                                </td>
                                                <td>
                                                    <c:out value="${pool.branchCode}"/>
                                                </td>
                                                <td>
                                                    <c:out value="${pool.acctNo}"/>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </c:otherwise>
                                </c:choose>
                                </tbody>
                            </table>
                        </div>

                    </div>

                </div>
            </div>
        </div>
    </div>
</form>
<script>
    function jumpToPagechangePage() {
        showWaiting();
        $("#mainForm").submit();
    }


    function sortRecords(sortFieldName, sortType) {
        showWaiting();
        $("[name='crud_action_value']").val(sortFieldName);
        $("[name='crud_action_additional']").val(sortType);
        $("#mainForm").submit();
    }
</script>

