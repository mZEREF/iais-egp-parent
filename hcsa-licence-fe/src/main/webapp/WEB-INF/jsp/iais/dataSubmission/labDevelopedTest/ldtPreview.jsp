<%@ include file="./common/ldtHeader.jsp" %>
<c:set value="${LdtSuperDataSubmissionDto.dsLaboratoryDevelopTestDto}" var="dsLaboratoryDevelopTestDto"/>
<form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
    <input type="hidden" name="crud_action_type">
    <div class="main-content">
        <div class="container">
            <p class="print">
            <div style="font-size: 16px;text-align: right;">
                <a href="javascript:void(0)" onclick="printData()"><em class="fa fa-print"></em>Print</a>
            </div>
            </p>
            <%@ include file="./section/prviewLdtSection.jsp" %>
        </div>
    </div>
</form>
<%@include file="./common/ldtFooter.jsp" %>