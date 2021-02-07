<%@include file="../assessmentGuideMenuHead.jsp" %>
<%@include file="../assessmentGuideMenuLevel/assessmentGuideMenuLevel.jsp" %>
<div class="self-assessment-item">
    <div class="applyLicence">
        <div class="form-check-gp">
            <div class="component-gp">
                <div id="svcStep1">
                    <%@include file="comm/chooseSvcContent.jsp"%>
                </div>
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
<input type="text" style="display: none" id="draftsave" name="draftsave" value="${selectDraftNo}">
<c:if test="${ not empty selectDraftNo }">
    <iais:confirm msg="${new_ack001}" callBack="cancelSaveDraft()" popupOrder="saveDraft"  yesBtnDesc="Resume from draft" cancelBtnDesc="Continue" cancelBtnCls="btn btn-primary" yesBtnCls="btn btn-secondary" cancelFunc="saveDraft()"></iais:confirm>
</c:if>
<script>
    $("#applyLicence").attr('checked', 'true');

    $(document).ready(function () {
        if( $('#draftsave').val()!=null|| $('#draftsave').val()!=''){
            $('#saveDraft').modal('show');
        }

        $('#Back').click(function(){
            submit('toInbox',null,'back');
        });

        $('#submitService').click(function(){
            submit('chooseSvc',null,'next');
        });
    });
    function saveDraft() {
        let val = $('#draftsave').val();
        $("[name='crud_action_additional']").val(val);
        $("[name='crud_action_value']").val('continue');
        $('#mainForm').submit();
    }

    function cancelSaveDraft() {
        let val = $('#draftsave').val();
        $("[name='crud_action_additional']").val(val);
        $("[name='crud_action_value']").val('resume');
        $('#mainForm').submit();
    }
</script>