<%@include file="../assessmentGuideMenuHead.jsp" %>
<%@include file="../assessmentGuideMenuLevel/assessmentGuideMenuLevel.jsp" %>
<div class="self-assessment-item">
    <div class="applyLicence">
        <div class="form-check-gp">
            <div class="component-gp">
                <div class="disabledPart">
                    <%@include file="comm/chooseSvcContent.jsp"%>
                </div>
                <br/>
                <c:if test="${appSelectSvc.chooseBaseSvc}">
                    <div class="disabledPart">
                        <%@include file="comm/chooseBaseSvcContent.jsp"%>
                    </div>
                    <br/>
                </c:if>
                <div>
                    <%@include file="comm/chooseAlignContent.jsp"%>
                </div>
                <br/>
                <div class="row">
                    <div class="col-xs-12 col-md-3">
                    </div>
                    <div class="col-xs-12 col-md-4">
                        <div class="text-right text-center-mobile">
                            <a class="btn btn-primary next" id="alignContinue" href="javascript:void(0);">NEXT</a>
                        </div>
                    </div>
                </div>

                <br>
                <input type="text" style="display: none" id="draftsave" name="draftsave" value="${selectDraftNo}">
                <c:if test="${ not empty selectDraftNo }">
                    <iais:confirm msg="${new_ack001}" callBack="cancelSaveDraft()" popupOrder="saveDraft"  yesBtnDesc="Resume from draft" cancelBtnDesc="Continue" cancelBtnCls="btn btn-primary" yesBtnCls="btn btn-secondary" cancelFunc="saveDraft()"></iais:confirm>
                </c:if>
                <%@ include file="/WEB-INF/jsp/include/validation.jsp" %>
            </div>
        </div>
    </div>
    <%@include file="../assessmentGuideMenuLevel/assessmentGuideMenuLevel1_1.jsp" %>
</div>
<div class="col-lg-12 col-xs-12" style="padding-left: 20px">
    <a href="/main-web/eservice/INTERNET/MohInternetInbox"><em
            class="fa fa-angle-left"></em> Back</a>
</div>
<%@include file="../assessmentGuideMenuLevel/assessmentGuideMenuLevel2.jsp" %>
<%@include file="../assessmentGuideServiceMenuFoot.jsp" %>
<script type="text/javascript">
    $("#applyLicence").attr('checked', 'true');
    $("#submitService").hide();
    $(document).ready(function () {
        //disabled
        $('.disabledPart').find('input[type="radio"]').prop('disabled',true);
        $('.disabledPart').find('input[type="checkbox"]').prop('disabled',true);

        if( $('#draftsave').val()!=null|| $('#draftsave').val()!=''){
            $('#saveDraft').modal('show');
        }
        $('#alignBack').click(function () {
            <c:choose>
                <c:when test="${onlyBaseSvc}">
                    submit('chooseSvc',null,'back');
                </c:when>
                <c:otherwise>
                    submit('chooseBaseSvc',null,'back');
                </c:otherwise>
            </c:choose>
        });
        $('#alignContinue').click(function () {
            <c:choose>
                <c:when test="${onlyBaseSvc}">
                submit('chooseSvc',null,'next');
                </c:when>
                <c:otherwise>
                submit('chooseBaseSvc',null,'next');
                </c:otherwise>
            </c:choose>
        });

    });

    function saveDraft() {
        let val = $('#draftsave').val();
        $("[name='draftNo']").val(val);
        $("[name='crud_action_value']").val('continue');
        $('#mainForm').submit();
    }

    function cancelSaveDraft() {
        let val = $('#draftsave').val();
        $("[name='draftNo']").val(val);
        $("[name='crud_action_value']").val('resume');
        $('#mainForm').submit();
    }

</script>
