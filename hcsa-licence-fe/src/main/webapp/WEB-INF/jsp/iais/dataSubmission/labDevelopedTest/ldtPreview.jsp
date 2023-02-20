<%@ include file="./common/ldtHeader.jsp" %>
<c:set value="${LdtSuperDataSubmissionDto.dsLaboratoryDevelopTestDto}" var="dsLaboratoryDevelopTestDto"/>
<form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
    <input type="hidden" id="rfcOutDateFlag" name="rfcOutDateFlag" value="<c:out value="${rfcOutdateFlag}"/>"/>
    <input type="hidden" name="crud_action_type">
    <input type="hidden" name="title" value="${title}">
    <div class="main-content">
        <div class="container center-content">
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
            <%@include file="./common/ldtFooter.jsp" %>
        </div>
        <iais:confirm msg="DS_ERR071" callBack="$('#validateRfcOutdate').modal('hide');" popupOrder="validateRfcOutdate" yesBtnDesc="Close"
                      yesBtnCls="btn btn-secondary" needCancel="false" needFungDuoJi="false"/>
    </div>
</form>
