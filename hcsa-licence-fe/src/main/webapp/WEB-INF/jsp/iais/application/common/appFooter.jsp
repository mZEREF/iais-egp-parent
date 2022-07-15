<div class="application-tab-footer">
    <c:choose>
        <c:when test="${(isRFC || isRenew) && not isRfi}">
            <%@include file="rfcFooter.jsp"%>
        </c:when>
        <c:otherwise>
            <div class="row">
                <div class="col-xs-12 col-md-4">
                    <c:choose>
                        <c:when test="${fromDaftOrRfi}">
                            <a class="back" href="/main-web/eservice/INTERNET/MohInternetInbox?initPage=initApp"><em class="fa fa-angle-left"></em> Back</a>
                        </c:when>
                        <c:when test="${fromAssessGuild}">
                            <a class="back" href="/main-web/eservice/INTERNET/MohAccessmentGuide/jumpInstructionPage"><em class="fa fa-angle-left"></em> Back</a>
                        </c:when>
                        <c:otherwise>
                            <a class="back" id="Back" href="#"><em class="fa fa-angle-left"></em> Back</a>
                        </c:otherwise>
                    </c:choose>
                </div>
                <div class="col-xs-12 col-sm-8">
                    <div class="button-group">
                        <c:if test="${!isRfi}">
                            <input type="hidden" id="selectDraftNo" value="${selectDraftNo}">
                            <input type="hidden" id="saveDraftSuccess" value="${saveDraftSuccess}">
                            <a class="btn btn-secondary premiseSaveDraft" id="SaveDraft" >Save as Draft</a>
                        </c:if>
                        <a class="btn btn-primary next premiseId" id="Next" >Next</a></div>
                </div>
            </div>
        </c:otherwise>
    </c:choose>
</div>
<c:if test="${!isRfi && !(isRFC || isRenew)}">
    <iais:confirm msg="This application has been saved successfully" callBack="$('#saveDraft').modal('hide');" popupOrder="saveDraft"
                  yesBtnDesc="continue" cancelBtnDesc="exit to inbox" cancelBtnCls="btn btn-primary" yesBtnCls="btn btn-secondary"
                  cancelFunc="jumpPage()" needFungDuoJi="false"/>
</c:if>
<script type="text/javascript">
    $(document).ready(function() {
        if($('#saveDraftSuccess').val()=='success'){
            $('#saveDraft').modal('show');
        }
    });
    function jumpPage() {
        submit('premises','saveDraft','jumpPage');
    }
</script>