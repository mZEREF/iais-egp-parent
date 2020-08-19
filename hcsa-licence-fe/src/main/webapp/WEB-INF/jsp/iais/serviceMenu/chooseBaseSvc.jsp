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
    <input type="hidden" name="crud_action_value">
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
                <iais:confirm msg="${new_ack001}" callBack="cancelSaveDraft()" popupOrder="saveDraft"  yesBtnDesc="Resume from draft" cancelBtnDesc="Continue" cancelBtnCls="btn btn-primary" yesBtnCls="btn btn-secondary" cancelFunc="saveDraft()"></iais:confirm>
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

        $('.existing-base-content input[type="radio"]').prop('disabled',true);
        //reload default choose the first
        if(${empty reloadBaseSvcSelected}){
            $('.speSvcContent').each(function (v,k) {
                $(this).find('.base-svc-content:eq(0) .exist-base-lic-content input[type="radio"]:eq(0)').prop('checked',true);
                $(this).find('.base-svc-content:eq(0) .exist-base-lic-content .existing-base-content input[type="radio"]:eq(0)').prop('checked',true);
                // $('.form-check-input.first:checked').trigger('click');
                $(this).find('.base-svc-content:eq(0) .exist-base-lic-content .existing-base-content input[type="radio"]').prop('disabled',false);
            });
        }else{
            console.log('reload ...');
            $('.firstStep:checked').each(function () {
                if($(this).hasClass('existing-base')){
                    $(this).closest('div.exist-base-lic-content').find('div.existing-base-content input[type="radio"]').prop('disabled',false);
                }
            });
        }


        $('.firstStep').change(function () {
            var $currSpecContent = $(this).closest('div.speSvcContent');
            var $baseSvcCount = $(this).closest('div.base-svc-content');
            var $baseLicContent = $(this).closest('div.exist-base-lic-content');
            $currSpecContent.removeClass('remark-point');
            //clear select when click other base
            if($(this).hasClass('existing-base')){
                console.log('first step');
                $currSpecContent.find('div.exist-base-lic-content div.existing-base-content input[type="radio"]').prop('checked',false);
                $currSpecContent.find('div.exist-base-lic-content div.existing-base-content input[type="radio"]').prop('disabled',true);
                $baseLicContent.find('div.existing-base-content input[type="radio"]').prop('disabled',false);
                $baseLicContent.find('div.existing-base-content input[type="radio"]:eq(0)').prop('checked',true);
                //var currSvcName = $baseSvcCount.find('input[name="svcName"]').val();
                $('.remark-point').each(function () {
                    console.log('remark-point start...');
                    $(this).find('div.base-svc-content:eq(0) div.exist-base-lic-content input[type="radio"]:eq(0)').prop('checked',true);
                    $(this).find('div.base-svc-content:eq(0) div.exist-base-lic-content div.existing-base-content input[type="radio"]:eq(0)').prop('checked',true);
                });
            }else if($(this).hasClass('diff-base')){
                console.log('diff-base');
                $currSpecContent.find('div.exist-base-lic-content input[type="radio"]').prop('checked',false);
                $currSpecContent.find('div.exist-base-lic-content div.existing-base-content input[type="radio"]').prop('disabled',true);

                //var currSvcName = $baseSvcCount.find('input[name="svcName"]').val();
                $('.remark-point').each(function () {
                    console.log('remark-point start...');
                    $(this).find('div.base-svc-content:eq(0) div.new-base input[type="radio"]:eq(0)').prop('checked',true);
                    $(this).find('div.existing-base-content input[type="radio"]').prop('checked',false);
                });
            }


            $currSpecContent.addClass('remark-point');
        });

        $('.secondStep').change(function () {
            var currHci = $(this).closest('div.form-check').find('input[name="premHci"]').val();
            var $currSpecContent = $(this).closest('div.speSvcContent');

            $currSpecContent.removeClass('remark-point');
            $('.remark-point').each(function () {
                var hadSameHci = false;
                $(this).find('.base-svc-content:eq(0) .exist-base-lic-content .existing-base-content div').each(function () {
                    var hci = $(this).find('input[name="premHci"]').val();
                    //can found the same hci
                    if(currHci == hci){
                        hadSameHci = true;
                        $(this).find('input[type="radio"]').prop('checked',true);
                        $(this).closest('div.exist-base-lic-content').find('.firstStep:eq(0)').prop('checked',true);
                        //let diff disabled
                        $(this).closest('div.base-svc-content').find('div.new-base input[type="radio"]').prop('checked',false);
                        // $(this).closest('div.exist-base-lic-content').prop('disabled',false);
                        return false;//break
                    }
                });
                if(!hadSameHci){
                    $(this).find('.base-svc-content:eq(0) div.new-base input[type="radio"]').prop('checked',true);
                    //let existing disable and uncheck
                    $(this).find('.base-svc-content:eq(0) div.exist-base-lic-content input[type="radio"]').prop('checked',false);
                    $(this).find('.base-svc-content:eq(0) div.exist-base-lic-content div.existing-base-content input[type="radio"]').prop('disabled',true);
                }
            });
            $currSpecContent.addClass('remark-point');
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
