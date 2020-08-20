<%@include file="../assessmentGuideMenuHead.jsp" %>
<%@include file="../assessmentGuideMenuLevel/assessmentGuideMenuLevel.jsp" %>
<div class="self-assessment-item">
    <div class="applyLicence">
        <div class="form-check-gp">
            <div class="component-gp">
                <br>
                <div class="disabledPart">
                    <%@include file="comm/chooseSvcContent.jsp"%>
                </div>
                <br/>
                <div>
                    <%@include file="comm/chooseBaseSvcContent.jsp"%>
                </div>
                <br/>
                <div class="row">
                    <div class="col-xs-12 col-md-3">
                        <div class="text-center-mobile">
                        </div>
                    </div>
                    <div class="col-xs-12 col-md-4">
                        <div class="text-right text-center-mobile">
                            <a class="btn btn-primary next" id="baseContinue">NEXT</a>
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
<%@include file="../assessmentGuideMenuLevel/assessmentGuideMenuLevel2.jsp" %>
<%@include file="../assessmentGuideServiceMenuFoot.jsp" %>
<script type="text/javascript">
    $("#applyLicence").attr('checked', 'true');

    $(document).ready(function () {
        //first enter
        var init = 0;

        //disabled
        $('.disabledPart').find('input[type="radio"]').prop('disabled',true);
        $('.disabledPart').find('input[type="checkbox"]').prop('disabled',true);
        $('.existing-base').find('input[type="radio"]').prop('disabled',true);


        $('#baseBack').click(function () {
            submit('chooseSvc',null,'back');
        });
        $('#baseContinue').click(function () {
            submit('chooseAlign',null,'next');

        });

        if( $('#draftsave').val()!=null|| $('#draftsave').val()!=''){
            $('#saveDraft').modal('show');
        }



        $('.assessment-service').click(function () {
            var $currSpecContent = $(this).closest('div.speSvcContent');
            var $baseLicContent = $(this).closest('div.exist-base-lic-content');

            //the first select
            if($(this).hasClass('firstStep')){
                // if($(this).hasClass('firstStep')){
                //     //disabled the second
                //     $('div.existing-base-content input[type="radio"]').prop('disabled',true);
                // }
                //control curr
                $baseLicContent.find('input[type="radio"]').prop('disabled',false);
                $baseLicContent.find('.existing-base-content input[type="radio"]:eq(0)').prop('checked',true);
                if(init ==0){
                    $currSpecContent.removeClass('disable-point');
                }
                //control other spec
                //the first step
                if($(this).hasClass('existing-base')){
                    $('.disable-point .exist-base-lic-content .firstStep').prop('disabled',false);
                    $('.disable-point .new-base .firstStep').prop('disabled',true);
                    $('.disable-point .new-base .firstStep').prop('checked',false);

                    // $('.disable-point div.existing-base-content').each(function () {
                    //     if($(this).find('input[type="radio"]:checked') == 'false'){
                    //         $(this).find('input[type="radio"]').prop('disabled',true);
                    //         $(this).find('input[type="radio"]').prop('checked',false);
                    //     }
                    //
                    // });
                }else if($(this).hasClass('diff-base')){
                    $('.disable-point .exist-base-lic-content .firstStep').prop('disabled',true);
                    $('.disable-point .new-base .firstStep').prop('disabled',false);
                    $('.disable-point .exist-base-lic-content .firstStep').prop('checked',false);
                    //unChecked the second step
                    $('div.existing-base-content').find('input[type="radio"]').prop('disabled',true);
                    $('div.existing-base-content').find('input[type="radio"]').prop('checked',false);

                }
                init = 1;

                //the second step
                // $('.disable-point div.existing-base-content').find('input[type="radio"]').prop('disabled',true);
                // $('.disable-point div.existing-base-content').find('input[type="radio"]').prop('checked',false);
            }else{
                $(this).closest('div.existing-base-content').find('input[type="radio').prop('disabled',false);
            }
        });

        //reload
        var defaultVal = '${appSvcRelatedInfoList == null || appSvcRelatedInfoList.size() == 0}';
        if(${noExistBaseLic}){
            if(defaultVal == 'true'){
                $('.speSvcContent').each(function (k,v) {
                    $(this).find('.base-svc-content:eq(0) input[type="radio"]:eq(0)').prop('checked',true);
                });
            }else{
                $('.speSvcContent').each(function (k,v) {
                    if( $(this).find('.assessment-service:checked').length == 0){
                        $(this).find('.assessment-service:eq(0)').prop('checked',true);
                    }
                });
            }
        }else{
            if(defaultVal == 'true'){
                $('.speSvcContent').each(function (v,k) {
                    $(this).find('.base-svc-content:eq(0) .exist-base-lic-content input[type="radio"]:eq(0)').prop('checked',true);
                    $(this).find('.base-svc-content:eq(0) .exist-base-lic-content .existing-base-content input[type="radio"]:eq(0)').prop('checked',true);
                    $('.assessment-service:checked').trigger('click');
                });
                $('.existing-base-content input[type="radio"]:checked').each(function () {
                    $(this).closest('div.existing-base-content').find('input[type="radio').prop('disabled',false);
                });
                //
            }else{
                $('.assessment-service:checked').trigger('click');
                $('.existing-base-content input[type="radio"]:checked').each(function () {
                    $(this).closest('div.existing-base-content').find('input[type="radio').prop('disabled',false);
                });

                $('.speSvcContent').each(function (k,v) {
                    if( $(this).find('.assessment-service:checked').length == 0){
                        $(this).find('.assessment-service:eq(0)').prop('checked',true);
                        $(this).find('.existing-base-content:eq(0) input[type="radio"]:eq(0)').prop('checked',true);
                    }
                });
            }
        }

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
