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
        <div class="row">
            <div class="col-xs-3">
            </div>
            <div class="col-xs-8">
                <div class="dashboard-page-title">
                    <h1>New Licence Application</h1>
                </div>
            </div>
        </div>
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
                        <a class="btn btn-primary next" id="baseContinue" href="javascript:void(0);">Continue</a>
                    </div>
                </div>
            </div>

            <br>
            <input type="text" style="display: none" id="draftsave" name="draftsave" value="${selectDraftNo}">
            <c:if test="${ not empty selectDraftNo }">
                <iais:confirm msg="${new_ack001}" callBack="cancelSaveDraft()" popupOrder="saveDraft"  yesBtnDesc="Resume from draft" cancelBtnDesc="Continue" cancelBtnCls="btn btn-primary" yesBtnCls="btn btn-secondary" cancelFunc="saveDraft()"></iais:confirm>
            </c:if>

            <iais:confirm msg="NEW_ACK44" popupOrder="saveApplicationAddress" cancelBtnDesc="cancel" yesBtnDesc="continue" cancelBtnCls="btn btn-secondary" yesBtnCls="btn btn-primary" callBack="baseContinue()" cancelFunc="cancel()"></iais:confirm>
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
            showWaiting();
            submit('chooseSvc',null,'back');
        });
        $('#baseContinue').click(function () {
            var allNew=true;
            var length = $('input.isNewOrBase').length;
            $('input.isNewOrBase').each(function (k, v) {
                var isNewOrBase = $(v).val();
                if (isNewOrBase=='base'){
                    allNew=false;
                }
            });
            if (allNew&&length>=1){
                $('#saveApplicationAddress').modal('show');
            }else {
                showWaiting();
                submit('chooseAlign',null,'next');
            }
        });

        if( $('#draftsave').val()!=null|| $('#draftsave').val()!=''){
            $('#saveDraft').modal('show');
        }

        $('.existing-base-content input[type="radio"]').prop('disabled',true);
        //reload default choose the first
        if(${empty reloadBaseSvcSelected}){
            $('.speSvcContent').each(function (v,k) {
                $(this).find('.firstStep:eq(0)').prop('checked',true);
                if($(this).find('.firstStep:eq(0)').is('.existing-base')){
                    $(this).find('.base-svc-content:eq(0) .exist-base-lic-content .existing-base-content input[type="radio"]:eq(0)').prop('checked',true);
                }
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
            var $baseLicContent = $(this).closest('div.exist-base-lic-content');
            //clear select when click other base
            if($(this).hasClass('existing-base')){
                $currSpecContent.find('input.isNewOrBase').val('base');
                $currSpecContent.find('div.exist-base-lic-content div.existing-base-content input[type="radio"]').prop('checked',false);
                $currSpecContent.find('div.exist-base-lic-content div.existing-base-content input[type="radio"]').prop('disabled',true);
                $baseLicContent.find('div.existing-base-content input[type="radio"]').prop('disabled',false);
                $baseLicContent.find('div.existing-base-content input[type="radio"]:eq(0)').prop('checked',true);
            }else if($(this).hasClass('diff-base')){
                $currSpecContent.find('input.isNewOrBase').val('new');
                $currSpecContent.find('div.exist-base-lic-content input[type="radio"]').prop('checked',false);
                $currSpecContent.find('div.exist-base-lic-content div.existing-base-content input[type="radio"]').prop('disabled',true);
            }
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

    function cancel() {
        $('#saveApplicationAddress').modal('hide');
    }

    function baseContinue() {
        showWaiting();
        submit('chooseAlign',null,'next');
    }
</script>
