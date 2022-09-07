<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.inspection.InspectionConstants" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<iais:row id="rollBackToRow">
    <label id="rollBackToLabel" class="col-md-4 control-label">Stage to Reset To <span style="color: red"> *</span>
        <a class="btn-tooltip styleguide-tooltip" data-toggle="tooltip" data-html="true" href="javascript:void(0);"
           title='<iais:message key="INSPE_ACK003"/>'
           style="z-index: 10"
           data-original-title="">i</a>
    </label>
    <iais:value width="10" display="true">
        <iais:select name="rollBackTo" id="rollBackTo"
                     firstOption="Please Select"
                     options="rollBackOptions"
                     value=""/>
        <span id="error_rollBackTo1" class="error-msg"
              style="display: none;"><iais:message key="GENERAL_ERR0006"/></span>
    </iais:value>
</iais:row>

<iais:confirm msg="INSPE_ACK001" popupOrder="confirmTag"
              cancelFunc="$('#confirmTag').modal('hide')" cancelBtnCls="btn btn-secondary" cancelBtnDesc="NO"
              callBack="$('#confirmTag').modal('hide');realSubmitFun()" yesBtnCls="btn btn-primary" yesBtnDesc="YES"/>

<script type="text/javascript">

    function showRollBackTo(customValue) {
        const nextStageValue = $('.nextStage').find('option:selected').val();
        const internalRemarkStar = $('#internalRemarkStar');
        const rollBackToRow = $('#rollBackToRow');

        if ('<%=InspectionConstants.PROCESS_DECI_ROLL_BACK%>' === nextStageValue ||'PROCRLR' === nextStageValue || (customValue && customValue=== nextStageValue)) {
            if('PROCRLR' !== nextStageValue){
                rollBackToRow.show();
            }else{
                rollBackToRow.hide();
            }
            internalRemarkStar.show();
        } else {
            rollBackToRow.hide();
            internalRemarkStar.hide();
        }
    }

    let realSubmitFun;

    function submitRollBack(submitFun, customValue) {
        const rollBackToMsg = $('#error_rollBackTo1');
        const internalMsg = $('#error_internalRemarks1');
        const nextStageValue = $('.nextStage').find('option:selected').val();
        rollBackToMsg.hide();
        internalMsg.hide();
        if ('<%=InspectionConstants.PROCESS_DECI_ROLL_BACK%>' === nextStageValue || (customValue && customValue=== nextStageValue)) {
            const rollBackToVal = $("#rollBackTo").val();
            const internalRemarksVal = $('.internalRemarks').val();
            let pass = true;
            if (internalRemarksVal === null || internalRemarksVal === undefined || internalRemarksVal === ''){
                internalMsg.html('The field is mandatory.');
                internalMsg.show();
                pass = false;
            }
            if (rollBackToVal === null || rollBackToVal === undefined || rollBackToVal === '') {
                rollBackToMsg.show();
                pass = false;
            }
            if (pass) {
                $('#confirmTag').modal('show');
                realSubmitFun = submitFun;
            }
        } else {
            submitFun();
        }
    }
</script>