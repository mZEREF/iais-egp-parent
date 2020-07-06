<%--
  Created by IntelliJ IDEA.
  User: ZiXian
  Date: 2020/6/15
  Time: 14:33
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-internet"/>
<form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
    <%@include file="comm/comFun.jsp"%>
    <input type="hidden" name="crud_action_type"/>
    <input type="hidden" name="crud_action_additional"/>
    <input type="hidden" name="crud_action_type_form"/>
    <input type="hidden" name="draftNo"/>
    <div class="container">
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
                </div>
                <div class="col-xs-12 col-md-2">
                    <div class="text-center-mobile">
                        <a class="back" id="baseBack"><em class="fa fa-angle-left"></em> Back</a>
                    </div>
                </div>
                <div class="col-xs-12 col-md-4">
                    <div class="text-right text-center-mobile">
                        <a class="btn btn-primary next" id="baseContinue">Continue</a>
                    </div>
                </div>
            </div>

            <br>
            <input type="text" style="display: none" id="draftsave" name="draftsave" value="${selectDraftNo}">
            <c:if test="${ not empty selectDraftNo }">
                <iais:confirm msg="There is an existing draft for the chosen service, if you choose to continue, the draft application will be discarded." callBack="cancelSaveDraft()" popupOrder="saveDraft"  yesBtnDesc="Resume from draft" cancelBtnDesc="Continue" cancelBtnCls="btn btn-primary" yesBtnCls="btn btn-secondary" cancelFunc="saveDraft()"></iais:confirm>
            </c:if>
            <%@ include file="/WEB-INF/jsp/include/validation.jsp" %>
        </div>
    </div>
</form>
<script type="text/javascript">
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



        $('.form-check-input').click(function () {
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
                    if( $(this).find('.form-check-input:checked').length == 0){
                        $(this).find('.form-check-input:eq(0)').prop('checked',true);
                    }
                });
            }
        }else{
            if(defaultVal == 'true'){
                $('.speSvcContent').each(function (v,k) {
                    $(this).find('.base-svc-content:eq(0) .exist-base-lic-content input[type="radio"]:eq(0)').prop('checked',true);
                    $(this).find('.base-svc-content:eq(0) .exist-base-lic-content .existing-base-content input[type="radio"]:eq(0)').prop('checked',true);
                    $('.form-check-input:checked').trigger('click');
                });
                $('.existing-base-content input[type="radio"]:checked').each(function () {
                    $(this).closest('div.existing-base-content').find('input[type="radio').prop('disabled',false);
                });
                //
            }else{
                $('.form-check-input:checked').trigger('click');
                $('.existing-base-content input[type="radio"]:checked').each(function () {
                    $(this).closest('div.existing-base-content').find('input[type="radio').prop('disabled',false);
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
