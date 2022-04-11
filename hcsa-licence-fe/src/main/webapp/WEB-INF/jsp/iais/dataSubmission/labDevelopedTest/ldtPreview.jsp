<%@ include file="./common/ldtHeader.jsp" %>
<c:set value="${LdtSuperDataSubmissionDto.dsLaboratoryDevelopTestDto}" var="dsLaboratoryDevelopTestDto"/>
<form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
    <input type="hidden" name="crud_action_type">
    <div class="main-content">
        <div class="container">
            <div class="row form-group tab-content" style="border-bottom: 1px solid #D1D1D1;">
                <div class="col-xs-12 col-md-10">
                    <strong style="font-size: 2rem;">Preview & Submit</strong>
                </div>
                <div class="col-xs-12 col-md-2 text-right">
                    <p class="print" style="font-size: 16px;">
                        <a href="javascript:void(0)" onclick="printData()"><em class="fa fa-print"></em>Print</a>
                    </p>
                </div>
            </div>
            <%@ include file="./section/prviewLdtSection.jsp" %>
        </div>
    </div>
</form>
<%@include file="./common/ldtFooter.jsp" %>